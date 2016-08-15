package com.snapdeal.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.TrackingNumbers;
import com.snapdeal.entity.User;

/** Service Implementation of tracking numbers **/
@Named("trackingNumbersService")
@Transactional
public class TrackingNumbersServiceImpl implements TrackingNumbersService{
	
	@Inject
	@Named("entityDao")
	EntityDao entityDao;

	/** Save the tracking numbers into database.**/
	@SuppressWarnings("unchecked")
	@Override
	public void saveTrackingNubmers(String courierCode, List<String> trackingNumberList){
		
		/** Get All the tracking Numbers from database corresponding to the current courier code. **/
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select trackingNumbers.trackingNumber from TrackingNumbers trackingNumbers where trackingNumbers.courierCode=:courierCode");
		query.setParameter("courierCode", courierCode);
		List<String> oldTrackingNumbers = new ArrayList<String>();
		oldTrackingNumbers = query.getResultList();
		int countInsert = 0;
		
		List<String> newTrackingNumbersList = notPresentTrackingNumbers(oldTrackingNumbers, trackingNumberList);
		
		for(String trackingNumber : newTrackingNumbersList){
			TrackingNumbers trackingNumbers = new TrackingNumbers();
			trackingNumbers.setCourierCode(courierCode);
			trackingNumbers.setTrackingNumber(trackingNumber);
			
			entityDao.save(trackingNumbers);
			countInsert++;
			
			/** For fast insertion. **/
			if(countInsert == 20){
				entityDao.flush();
				entityDao.clear();
				countInsert = 0;
			}
		}
		
		/*for(String trackingNumber : trackingNumberList){
			*//** If tracking number is already present, then skip that tracking number
			 * else add it into the database.
			 * **//*
			if(oldTrackingNumbers.size() == 0 || !oldTrackingNumbers.contains(trackingNumber)){
				TrackingNumbers trackingNumbers = new TrackingNumbers();
				trackingNumbers.setCourierCode(courierCode);
				trackingNumbers.setTrackingNumber(trackingNumber);
				
				entityDao.save(trackingNumbers);
				// entityDao.getEntityManager().lock(trackingNumbers, LockModeType.WRITE);
				countInsert++;
				
				*//** For fast insertion. **//*
				if(countInsert == 20){
					entityDao.flush();
					entityDao.clear();
					countInsert = 0;
				}
			}
		}*/
	}
	
	public List<String> notPresentTrackingNumbers(List<String> oldTrackingNumbersList, List<String> newTrackingNumbersList){
		List<String> trackingNumbersList = new ArrayList<String>();
		
		Collections.sort(newTrackingNumbersList);
		Collections.sort(oldTrackingNumbersList);
		
		int i,j;
		
		for(i = 0,j= 0;i < oldTrackingNumbersList.size() && j < newTrackingNumbersList.size();){
			if(oldTrackingNumbersList.get(i).equals(newTrackingNumbersList.get(j))){
				i++; j++;
			}
			else if(oldTrackingNumbersList.get(i).compareTo(newTrackingNumbersList.get(j)) < 0){
				i++;
			}
			else {
				trackingNumbersList.add(newTrackingNumbersList.get(j));
				j++;
			}
		}
		
		for(;j < newTrackingNumbersList.size();j++)
			trackingNumbersList.add(newTrackingNumbersList.get(j));
		
		return trackingNumbersList;
	}
	
	/** Remove the tracking numbers from database.**/
	@SuppressWarnings("unchecked")
	@Override
	public void removeTrackingNubmers(String courierCode, List<String> trackingNumberList){
		/** Get Current User **/
		User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		/** Get All the tracking Numbers which are not used from database corresponding to the current courier code. **/
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select trackingNumbers.trackingNumber from TrackingNumbers trackingNumbers where trackingNumbers.isUsed = false and trackingNumbers.courierCode=:courierCode");
		query.setParameter("courierCode", courierCode);
		
		List<String> oldTrackingNumbers = query.getResultList();
		List<String> batchTrackingNumbersList = new ArrayList<String>();
		int totalCount = trackingNumberList.size();
		int count = 0;
		
		for(String trackingNumber : trackingNumberList){
			
			/** If tracking number is present, then add it into the list
			 * **/
			totalCount--;
			if(oldTrackingNumbers.size() > 0 && oldTrackingNumbers.contains(trackingNumber)){
				batchTrackingNumbersList.add(trackingNumber);
				count++;
			}
			
			/** Now, update the list in a set of 20 for faster updation.**/
			if(count == 20 || (totalCount == 0 && batchTrackingNumbersList.size() > 0)){
				query = entityManager.createQuery("Update TrackingNumbers trackingNumbers set trackingNumbers.isUsed = true, trackingNumbers.updatedBy=:updatedBy, trackingNumbers.updated=:updated where trackingNumbers.courierCode =:courierCode and trackingNumbers.trackingNumber in (:trackingNumber)");
				query.setParameter("courierCode", courierCode);
				query.setParameter("trackingNumber",batchTrackingNumbersList);
				query.setParameter("updatedBy", currentUser);
				query.setParameter("updated", new Date());
				query.executeUpdate();
				entityManager.flush();
				entityManager.clear();
				batchTrackingNumbersList.clear();
				count = 0;
			}
		}
	}
}
