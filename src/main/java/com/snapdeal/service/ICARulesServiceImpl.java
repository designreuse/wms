package com.snapdeal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.ICARules;
import com.snapdeal.entity.ReturnType;
import com.snapdeal.entity.User;

/** Service Implementation for ica rules**/
@Named("iCARulesService")
@Transactional
public class ICARulesServiceImpl implements ICARulesService{
	@Inject
	@Named("entityDao")
	EntityDao entityDao;
	
	@Override
	public List<ReturnType> getReturnType(){
		List<ReturnType> returnTypeList = entityDao.findAll(ReturnType.class);
		return returnTypeList;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<String> getReturnTypeCode(){
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select returnType.code from ReturnType returnType");
		List<String> returnTypeList = (List<String>)query.getResultList();
		return returnTypeList;
	}

	/** Saves the ICA rules into the database.**/
	@SuppressWarnings("unchecked")
	@Override
	public void saveICARules(ICARules iCARules){
		
		/** Get the ica rule details if already present and is enabled.**/
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select iCARules from ICARules iCARules where iCARules.returnType = :returnType "
				+ "and iCARules.type = :type and iCARules.location = :location and iCARules.courierCode =:courierCode "
				+ "and iCARules.enabled =true"); 
		query.setParameter("returnType", iCARules.getReturnType());
		query.setParameter("type", iCARules.getType());
		query.setParameter("location", iCARules.getLocation());
		query.setParameter("courierCode", iCARules.getCourierCode());
		List<ICARules> iCARulesList = (List<ICARules>)query.getResultList();
		
		/** save or update accordingly **/
		if(iCARulesList.size() > 0){
			ICARules oldICARule = iCARulesList.get(0);
			
			iCARules.setId(oldICARule.getId());
			iCARules.setCreated(oldICARule.getCreated());
			iCARules.setCreatedBy(oldICARule.getCreatedBy());
		}
		entityDao.saveOrUpdate(iCARules);
	}
	
	/** Disable courier codes which are not present in the current courier code list.**/
	@Override
	public void disableICARulesNotInCourierCodeList(List<String> courierCodeList, String returnType, String type, String location){
		/** Get Current User **/
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		/** Update the courier code set enabled and isRuleEnabled to false which are not present.**/
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Update ICARules iCARules set iCARules.enabled =false, iCARules.isRuleEnabled = false, "
				+ "iCARules.updated =:updated, iCARules.updatedBy =:updatedBy "
				+ "where iCARules.returnType = :returnType and iCARules.type = :type and iCARules.location = :location "
				+ "and iCARules.enabled =true and iCARules.courierCode not in (:courierCode)");
		query.setParameter("returnType", returnType);
		query.setParameter("type", type);
		query.setParameter("location", location);
		
		/** Adding a dummy value if the courier Code list is empty which means nothing to update.
		 *  Added for null pointer exception.
		 * **/
		if(courierCodeList == null){
			courierCodeList = new ArrayList<String>();
			courierCodeList.add("dummyValue");
		}
		
		query.setParameter("courierCode", courierCodeList);
		query.setParameter("updatedBy", currentUser);
		query.setParameter("updated", new Date());
		
		query.executeUpdate();
	}
	
	/** Disable courier codes by type and location.**/
	@Override
	public void disableICARulesByTypeLocation(String returnType, String type, String location){
		/** Get Current User **/
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		/** Update the courier code set enabled and isRuleEnabled to false which are not present.**/
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Update ICARules iCARules set iCARules.enabled =false, iCARules.isRuleEnabled = false, "
				+ "iCARules.updated =:updated, iCARules.updatedBy =:updatedBy "
				+ "where iCARules.returnType = :returnType and iCARules.type = :type and iCARules.location = :location and iCARules.enabled =true");
		query.setParameter("returnType", returnType);
		query.setParameter("type", type);
		query.setParameter("location", location);
		query.setParameter("updatedBy", currentUser);
		query.setParameter("updated", new Date());
		
		query.executeUpdate();
	}
	
	/** Checks whether the rule exists or not. **/
	@Override
	public Boolean isRuleExist(String returnType, String type, String location){
		List<ICARules> iCARulesList = getICARules(returnType, type, location);
		if(iCARulesList.size() > 0)
			return true;
		else
			return false;
	}

	/** Get ICA rule whether enabled or disabled corresponding to type and location. **/
	@SuppressWarnings("unchecked")
	@Override
	public List<ICARules> getICARules(String returnType, String type, String location){
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select iCARules from ICARules iCARules where iCARules.type = :type "
				+ "and iCARules.location = :location and iCARules.returnType = :returnType");
		query.setParameter("returnType", returnType);
		query.setParameter("type", type);
		query.setParameter("location", location);
		List<ICARules> iCARulesList = (List<ICARules>)query.getResultList();
		
		return iCARulesList;
	}

	/** Get ICA rule corresponding to type ,location and isRuleEnabled. **/
	@SuppressWarnings("unchecked")
	@Override
	public List<ICARules> getICARules(String returnType, String type, String location,Boolean isRuleEnabled) {
		
		/** Get ICA rule corresponding to type ,location and isRuleEnabled. **/
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select iCARules from ICARules iCARules where iCARules.returnType = :returnType "
				+ "and iCARules.type = :type and iCARules.location = :location and iCARules.enabled =true "
				+ "and iCARules.isRuleEnabled =:isRuleEnabled");
		query.setParameter("returnType", returnType);
		query.setParameter("type", type);
		query.setParameter("location", location);
		query.setParameter("isRuleEnabled", isRuleEnabled);
		List<ICARules> iCARulesList = (List<ICARules>)query.getResultList();
		
		/** Get ICA rule corresponding to type ,location and isRuleEnabled = true. **/
		query = entityManager.createQuery("Select iCARules from ICARules iCARules where iCARules.returnType = :returnType "
				+ "and iCARules.type = :type and iCARules.location = :location and iCARules.enabled =true and iCARules.isRuleEnabled =true");
		query.setParameter("returnType", returnType);
		query.setParameter("type", type);
		query.setParameter("location", location);
		List<ICARules> newICARulesList = (List<ICARules>)query.getResultList();
		
		/** If both the list are comes out to be empty then, it means there is no rule present for any courier
		 * Then, set the fields according to any courier code which is in disabled corresponding to type and location.
		 * **/
		if(iCARulesList.size() == 0 && newICARulesList.size() == 0){
			
			query = entityManager.createQuery("Select iCARules from ICARules iCARules where iCARules.returnType = :returnType "
					+ "and iCARules.type = :type and iCARules.location = :location and iCARules.isRuleEnabled =:isRuleEnabled");
			query.setParameter("returnType", returnType);
			query.setParameter("type", type);
			query.setParameter("location", location);
			query.setParameter("isRuleEnabled", isRuleEnabled);
			newICARulesList = (List<ICARules>)query.getResultList();
			
			if(newICARulesList.size() > 0){
				ICARules iCARules = new ICARules();
				iCARules.setLocation(newICARulesList.get(0).getLocation());
				iCARules.setType(newICARulesList.get(0).getType());
				iCARules.setStartDate(newICARulesList.get(0).getStartDate());
				iCARules.setEndDate(newICARulesList.get(0).getEndDate());
				iCARules.setIsRuleEnabled(newICARulesList.get(0).getIsRuleEnabled());
				
				iCARulesList = new ArrayList<ICARules>();
				iCARulesList.add(iCARules);
			}
		}
		
		return iCARulesList;
	}
}
