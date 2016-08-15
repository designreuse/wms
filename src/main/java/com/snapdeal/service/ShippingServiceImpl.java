package com.snapdeal.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.ShippingLocation;
import com.snapdeal.entity.User;

import org.springframework.security.core.context.SecurityContextHolder;

/** Service Implementation of shipping location **/
@Named("shippingService")
@Transactional
public class ShippingServiceImpl implements ShippingService{

	@Inject
	@Named("entityDao")
	EntityDao entityDao;

	/** Save the shipping location into database.**/
	@SuppressWarnings("unchecked")
	@Override
	public void saveShippingLocation(String courierCode, List<Set<String>> pincodeList, List<String> areaCodeList){
		/** Get Current User **/
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		/** First update all the shipping location to false corresponding to the courier code **/
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Update ShippingLocation shippingLocation set shippingLocation.enabled=false, shippingLocation.updatedBy=:updatedBy, shippingLocation.updated=:updated where shippingLocation.courierCode =:courierCode");
		query.setParameter("courierCode", courierCode);
		query.setParameter("updatedBy", currentUser);
		query.setParameter("updated", new Date());
		query.executeUpdate();
		
		/** Then, Select the shipping Location from database which are present in the pincodeList provided.**/
		query = entityManager.createQuery("Select shippingLocation.pincode from ShippingLocation shippingLocation Where shippingLocation.courierCode =:courierCode");
		query.setParameter("courierCode", courierCode);
		List<String> oldPincodeList = (List<String>)query.getResultList();
		
		for(String areaCode : areaCodeList){
			/** Get index of the current Area Code.**/
			int position = areaCodeList.indexOf(areaCode);
			
			/** Creating batch update for faster execution. **/
			Set<String> batchPincodeList = new HashSet<String>();
			int count = 0;
			int countInsert = 0;
			int totalCount = pincodeList.get(position).size();
			for(String pincode : pincodeList.get(position)){
	
				totalCount--;
				/** Add the pincode to the update list if it is already presents.**/
				if(oldPincodeList.size() > 0 && oldPincodeList.contains(pincode)){
					batchPincodeList.add(pincode);
					count++;
				}
				/** Else insert a new entry into the database.**/
				else{
					ShippingLocation shippingLocation = new ShippingLocation();
					shippingLocation.setCourierCode(courierCode);
					shippingLocation.setPincode(pincode);
					shippingLocation.setAreaCode(areaCode);
					shippingLocation.setEnabled(true);
					entityDao.saveOrUpdate(shippingLocation);
					countInsert++;
					
					if(countInsert == 20){
						entityDao.flush();
						entityDao.clear();
						countInsert = 0;
					}
				}
				
				/** Now, update the list in a set of 20 for faster updation.**/
				if(count == 20 || (totalCount == 0 && batchPincodeList.size() > 0)){
					query = entityManager.createQuery("Update ShippingLocation shippingLocation set shippingLocation.areaCode=:areaCode, shippingLocation.enabled=true, shippingLocation.updatedBy=:updatedBy, shippingLocation.updated=:updated where shippingLocation.courierCode =:courierCode and shippingLocation.pincode in (:pincode)");
					query.setParameter("areaCode", areaCode);
					query.setParameter("courierCode", courierCode);
					query.setParameter("pincode",batchPincodeList);
					query.setParameter("updatedBy", currentUser);
					query.setParameter("updated", new Date());
					query.executeUpdate();
					entityManager.flush();
					entityManager.clear();
					batchPincodeList.clear();
					count = 0;
				}
			}
		}

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String getAreaCode(String courierCode, String pincode){
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select shippingLocation.areaCode from ShippingLocation shippingLocation where shippingLocation.pincode=:pincode and shippingLocation.courierCode =:courierCode");
		query.setParameter("pincode", pincode);
		query.setParameter("courierCode", courierCode);
		query.setMaxResults(1);
		List<String> areaCodeList = (List<String>)query.getResultList();
		
		if(areaCodeList.size() > 0)
			return areaCodeList.get(0);
		else
			return "";
	}
}