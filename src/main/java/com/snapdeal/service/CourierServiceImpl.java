package com.snapdeal.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.Query;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.bean.AWBResult;
import com.snapdeal.component.Constants;
import com.snapdeal.component.SessionDetails;
import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.Courier;
import com.snapdeal.entity.DailyMaxLoad;
import com.snapdeal.entity.PostalCode;
import com.snapdeal.entity.TrackingNumbers;

/** Service Implementation for courier.**/
@Named("courierService")
@Transactional
public class CourierServiceImpl implements CourierService{

	@Inject
	@Named("entityDao")
	EntityDao entityDao;
	
	@Inject
	@Named("postalCodeService")
	PostalCodeService postalCodeService;
	
	@Inject
	@Named("sessionDetails")
	SessionDetails sessionDetails;
	
	private Random randomGenerator;
	
	/** Save courier details in database.**/
	@Override
	public String saveCourier(Courier courier){
		/** Get Maximum Id from the courier table which is used to generate the courier code.**/
		Long maxId = entityDao.getMaxId(Courier.class);
		
		/** Use the courier name to generate the courier code.
		 * Courier Code contains courier names(first two word, if present) followed by maxId+1 which is actually the current id of courier.
		 * **/
		String name[] = courier.getName().split(" ");
		String code = name[0].toUpperCase();
		if(name.length > 1)
			code += "_" + name[1].toUpperCase();
		if(maxId == null)
			maxId = 1L;
		else
			maxId += 1L;
		
		/** Escape all special characters except - and _**/
		code = code.replaceAll("[^a-zA-Z0-9_-]", "");
		code += maxId;
		
		/** Set Courier Code field of Courier. **/
		courier.setCode(code);
		
		/** Set MaxLoad to MAX value of Integer if MaxLoad is null. **/
		if(courier.getMaxLoad() == null)
			courier.setMaxLoad(Integer.MAX_VALUE);
		
		/** Save the courier in database **/
		entityDao.saveOrUpdate(courier);
		return courier.getCode();
	}
	
	/** Check for the existing email in the database corresponding to a courier entity.**/
	@SuppressWarnings("unchecked")
	@Override
	public boolean checkCourierByEmail(String email){
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select courier from Courier courier where courier.primaryEmail = :primaryEmail");
		query.setParameter("primaryEmail", email);
		
		List<Courier> courierList = (List<Courier>)query.getResultList();
		
		/** If exists return true else false. **/
		if(courierList.size() > 0)
			return true;
		else
			return false;
	}

	/** Search courier by courier code in database. **/
	@SuppressWarnings("unchecked")
	@Override
	public Courier searchCourierByCode(String code){
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select courier from Courier courier where courier.code = :code");
		query.setParameter("code", code);
		List<Courier> courierList = (List<Courier>)query.getResultList();
		
		if(courierList.isEmpty())
			return null;
		else
			return courierList.get(0);
	}

	/** Get all enabled courier code list from the database. **/
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllEnabledCourierCode(){
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select courier.code from Courier courier where courier.enabled =true");
		List<String> courierList = (List<String>)query.getResultList();
		return courierList;
	}
	
	/** Get all enabled courier code list from the database. **/
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllEnabledCourierCodeOfCurrentWarehouse(){
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select courier.code from Courier courier where "
				+ "courier.enabled =true and :warehouse MEMBER OF courier.warehouseList"); 
		query.setParameter("warehouse", sessionDetails.getSessionWarehouse()); 
		List<String> courierList = (List<String>)query.getResultList();
		return courierList;
	}
	
	/** Get all courier code list(both enabled and disabled) from the database. **/
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllCourierCode(){
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select courier.code from Courier courier");
		List<String> courierList = (List<String>)query.getResultList();
		return courierList;
	}

	/** Update courier in database.**/
	@SuppressWarnings("unchecked")
	@Override
	public void updateCourier(Courier courier){
		/** Set MaxLoad to Max of Integer if MaxLoad is null**/
		if(courier.getMaxLoad() == null)
			courier.setMaxLoad(Integer.MAX_VALUE);
		
		/** Get the old entry details and set it to the new one.**/
		Courier oldCourier = entityDao.findById(Courier.class, courier.getId());
		courier.setCode(oldCourier.getCode());
		courier.setCreated(oldCourier.getCreated());
		courier.setCreatedBy(oldCourier.getCreatedBy());
		
		/** To get Current user roles **/
		Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		
		/** If the current user is admin then only update the enabled field **/
		if(!authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
			courier.setEnabled(oldCourier.getEnabled());
		}
		entityDao.saveOrUpdate(courier);
	}
	

	/** Get the AWBResult corresponding to a Pincode and return corresponding courier code and tracking number if exists **/
	@SuppressWarnings("unchecked")
	@Override
	public synchronized AWBResult getAWB(String pincode, String shippingMode, String returnType){
		
		AWBResult aWBResult = new AWBResult();
		
		/** 1. Get the courier code list from shipping location corresponding to a pincode. **/
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select shippingLocation.courierCode from ShippingLocation shippingLocation " +
				"where shippingLocation.pincode =:pincode and shippingLocation.enabled =true");
		query.setParameter("pincode", pincode);
		
		List<String> courierCodeList1 = query.getResultList();
		
		System.out.println("1 : All Courier List By Pincode :" + " " + courierCodeList1);
		/** When no courier is present corresponding to the pincode provided return empty object. **/
		if(courierCodeList1.size() == 0)
			return aWBResult;
		
		/** 2. Get the courier code list from courier corresponding to the 1st courierCodeList which are enabled and have shippingMode. **/
//		query = entityManager.createQuery("Select courier.code from Courier courier where courier.code in (:courierCode) " +
//				"and courier.enabled =true and (courier.shippingMode ='Both' or courier.shippingMode =:shippingMode)");
//		query = entityManager.createQuery("Select courier.code from Courier courier join courier.warehouseList warehouse " +
//				"where courier.code in (:courierCode) " +
//				"and courier.enabled =true and (courier.shippingMode ='Both' or courier.shippingMode =:shippingMode)" +
//				"and warehouse.id =:warehouseId");
		query = entityManager.createQuery("Select courier.code from Courier courier where courier.code in (:courierCode) " +
				"and courier.enabled =true and (courier.shippingMode ='Both' or courier.shippingMode =:shippingMode) " +
				"and :warehouse MEMBER OF courier.warehouseList");

		query.setParameter("courierCode",courierCodeList1);
		query.setParameter("shippingMode",shippingMode);
		query.setParameter("warehouse", sessionDetails.getSessionWarehouse());
		List<String> courierCodeList2 = query.getResultList();
		
		System.out.println("2 Only Enabled Courier List By Pincode :"  + " " + courierCodeList2);
		/** When no enabled courier is present then return empty object. **/
		if(courierCodeList2.size() == 0)
			return aWBResult;
		
		/** 3. Get the courier code list from courier corresponding to the 2nd courierCodeList whose daily Maximum Load is not passed. **/
		query = entityManager.createQuery("Select courier.code from Courier courier where courier.code not in (" +
				"Select courier.code from DailyMaxLoad dml join dml.courier courier " +
				"with courier.code in (:courierCode) where dml.created >= :today and courier.maxLoad <= dml.maxLoad) and courier.code in (:courierCode)");
		query.setParameter("courierCode", courierCodeList2);

		
		/** Get today's date and time set to beginning of day which is 00:00:00.**/
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();	
	
		try {
			Date today = dateFormat.parse(dateFormat.format(date));			
			query.setParameter("today",today);			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<String> courierCodeList3 = query.getResultList();
		
		System.out.println("3.a Courier Code list where dailyMaxLoad is not crossed." + " " + courierCodeList3);
		
		/** When all courier code are present in today's date with max load passed then, list3 with list2. **/
		if(courierCodeList3.size() == 0)
			courierCodeList3 = courierCodeList2;

		System.out.println("3.b " + courierCodeList3.size() + " " + courierCodeList3);
		
		/** Get the state and city corresponding to the pincode provided from the database.**/
		PostalCode postalCode = postalCodeService.getPostalCode(pincode);
		String city = postalCode.getCity();
		String state = postalCode.getState();
		System.out.println(city + " " + state);
		
		/** 4. Get courier code by city, state and all from ica rules order by priority. **/
		query = entityManager.createQuery("Select iCARules.courierCode from ICARules iCARules " +
				"where iCARules.courierCode in (:courierCode) and iCARules.returnType =:returnType and "
				+ "iCARules.location =:location and iCARules.startDate <= :date " +
				"and (iCARules.endDate >= :date or iCARules.endDate is null) and enabled =true and isRuleEnabled =true order by iCARules.priority");
		query.setParameter("courierCode", courierCodeList3);
		query.setParameter("returnType", returnType);
		query.setParameter("date", date);
		
		/** 4.a Get courier code by city and priority from ica Rules. **/
		query.setParameter("location",city);
		List<String> courierCodeListByCity = query.getResultList();
		
		/** 4.b Get courier Code by State and priority from ica Rules. **/
		query.setParameter("location",state);
		List<String> courierCodeListByState = query.getResultList();
		
		/** 4.c Get courier Code by All and priority from ica Rules. **/
		query.setParameter("location","All");
		List<String> courierCodeListByAll = query.getResultList();
		
		/** Now append list 4a, 4b and 4c to courierCodeList4 
		 * Such that it automatically checks for city first, then state and then for all.
		 * **/
		List<String> courierCodeList4 = new ArrayList<String>();
		
		courierCodeList4.addAll(courierCodeListByCity);
		courierCodeList4.addAll(courierCodeListByState);
		courierCodeList4.addAll(courierCodeListByAll);
		
		System.out.println("4.a ICA Rules courierList By City, state and then all :" + " " + courierCodeList4);		
		
		/** If all the lists are empty then assign the list4 with list3. **/
		if(courierCodeList4.size() == 0)
			courierCodeList4 = courierCodeList3;
		
		System.out.println("4.b After ICA Rules size check :" + " " + courierCodeList4);
		
		/** Now, checks for Advance AWBs and tracking Number for each courier **/
		for(String courierCode : courierCodeList4){
			
			Courier courier = searchCourierByCode(courierCode);
			
			/** If advanced AWB is present then, retrieve the tracking Number corresponding to the particular courier **/
			if(courier.getAdvanceAWBs()){
				Integer retryCount = 0;
				Boolean jobDone = false;
				
				synchronized(this){
					//while(!jobDone){
						try {
							/** Retrieving the tracking number from database **/
							query = entityManager.createQuery("Select trackingNumbers from TrackingNumbers trackingNumbers " +
									"where trackingNumbers.courierCode =:courierCode and trackingNumbers.isUsed = false");
							query.setMaxResults(100);
							query.setParameter("courierCode",courierCode);
							List<TrackingNumbers> trackingNumbers = query.getResultList();
							
							/** If tracking Number is present then set that tracking number to used.
							 * And return this tracking number along with corresponding courier. 
							 * **/
							if(trackingNumbers != null && trackingNumbers.size() > 0){
								
								System.out.println("5 " + trackingNumbers.get(0).getTrackingNumber() + " " + trackingNumbers.get(0).getCourierCode());
								
								randomGenerator = new Random();
								int index = randomGenerator.nextInt(trackingNumbers.size());
								trackingNumbers.get(index).setUsed(true);
								
								/** Update tracking numbers and set the current tracking number to used. **/
								entityDao.saveOrUpdate(trackingNumbers.get(index));
								entityDao.getEntityManager().lock(trackingNumbers.get(index), LockModeType.WRITE);
								
								/** Get DailyMaxLoad data of current courier if exists. **/ 
								query = entityManager.createQuery("Select dml from DailyMaxLoad dml where dml.courierCode =:courierCode and dml.created >= :today");
								query.setParameter("courierCode", courierCode);
								
								try {	
									Date today = dateFormat.parse(dateFormat.format(date));			
									query.setParameter("today",today);			
									
								} catch (ParseException e) {
									e.printStackTrace();
								}
								
								List<DailyMaxLoad> dailyMaxLoadList = query.getResultList();
								DailyMaxLoad dailyMaxLoad;
								
								/** If not exists, then add new daily max load entry into database. **/
								if(dailyMaxLoadList.size() == 0){
									dailyMaxLoad = new DailyMaxLoad();
									
									dailyMaxLoad.setCourier(courier);
									dailyMaxLoad.setCourierCode(courierCode);
									dailyMaxLoad.setEnabled(true);
									dailyMaxLoad.setMaxLoad(1);
									
								}
								/** If exists, then update the dailyMaxLoad by 1. **/
								else{
									dailyMaxLoad = dailyMaxLoadList.get(0);
									Integer newMaxLoad = dailyMaxLoad.getMaxLoad() + 1;
									
									dailyMaxLoad.setMaxLoad(newMaxLoad);
								}
								
								/** Save or update the daily max load **/
								entityDao.saveOrUpdate(dailyMaxLoad);
								
								/** Now, set the courier code and tracking numbers and break the loop.**/
								aWBResult.setCourierCode(courierCode);
								aWBResult.setTrackingNumber(trackingNumbers.get(index).getTrackingNumber());
								jobDone = true;
								break;
							}
						} catch (OptimisticLockException e) {
							if (++retryCount >= Constants.MAX_NO_OF_RETRY) {
				                throw e;
				            }
							else{/*
								try {
								    Thread.sleep(100);          
								} catch(InterruptedException ex) {
								    Thread.currentThread().interrupt();
								}*/
								continue;
							}
						}
					//}
				}
					
				if(jobDone)
					break;
			}
			/** If advanced AWB is false, then assign courier directly without tracking number. **/
			else{
				aWBResult.setCourierCode(courier.getCode());
				break;
			}
		}
		
		/** 1.) If no courier is present then return empty object. 
		 *  2.) If Advanced AWB is false, then return only courier code and empty tracking Number.
		 *  3.) If Advanced AWB is true, then return both courier code and tracking Number.
		 * **/
		return aWBResult;		
	}

	/** Unset Tracking Number and Reduce Daily Max Load for manual courier code and tracking number.**/
	@SuppressWarnings("unchecked")
	@Override
	public void unSetAWB(String courierCode, String trackingNumber){
		
		/** Get tracking number object from database corresponding to courier code and tracking number**/
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select trackingNumbers from TrackingNumbers trackingNumbers " +
					"where trackingNumbers.trackingNumber =:trackingNumber and trackingNumbers.courierCode =:courierCode " +
					"and trackingNumbers.isUsed = true");
		
		query.setMaxResults(1);
		query.setParameter("trackingNumber",trackingNumber);
		query.setParameter("courierCode",courierCode);
		List<TrackingNumbers> trackingNumbers = query.getResultList();
		
		if(trackingNumbers != null && trackingNumbers.size() > 0){
			trackingNumbers.get(0).setUsed(false);
			
			/** Update tracking numbers and set the current tracking number to unused. **/
			entityDao.saveOrUpdate(trackingNumbers.get(0));
			
			
			/** Get DailyMaxLoad data of current courier code. **/ 
			query = entityManager.createQuery("Select dml from DailyMaxLoad dml where dml.courierCode =:courierCode and dml.created >= :today");
			
			query.setMaxResults(1);
			query.setParameter("courierCode", courierCode);
			
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			Date date = new Date();	
			
			try {	
				Date today = dateFormat.parse(dateFormat.format(date));			
				query.setParameter("today",today);			
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			List<DailyMaxLoad> dailyMaxLoadList = query.getResultList();
			
			if(dailyMaxLoadList.size() > 0){
				DailyMaxLoad dailyMaxLoad = dailyMaxLoadList.get(0);
				
				Integer newMaxLoad = dailyMaxLoad.getMaxLoad() - 1;
				
				dailyMaxLoad.setMaxLoad(newMaxLoad);
				
				/** Save or update the daily max load **/
				entityDao.saveOrUpdate(dailyMaxLoad);
			}
		}
		
	}
}
