package com.snapdeal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.bean.ManifestSearch;
import com.snapdeal.component.Constants;
import com.snapdeal.component.SessionDetails;
import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.Manifest;
import com.snapdeal.entity.RtvSheet;

/** Service Implementation of Manifest **/
@Named("manifestService")
@Transactional
public class ManifestServiceImpl implements ManifestService{

	@Inject
	@Named("entityDao")
	EntityDao entityDao;

	@Inject
	@Named("sessionDetails")
	SessionDetails sessionDetails;
	
	@Inject
	@Named("historyService")
	HistoryService historyService;
	
	@Override
	public Long saveOrUpdateManifest(Manifest manifest) { 
		entityDao.saveOrUpdateByWarehouse(manifest); 
		return manifest.getId(); 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Manifest> getManifestBetweenDates(ManifestSearch manifestSearch) { 
		EntityManager entityManager = entityDao.getEntityManager(); 
		Query query = entityManager.createQuery("Select manifest from Manifest manifest "
				+ "JOIN manifest.warehouse warehouse where warehouse.id = :warehouseId "
				+ "AND manifest.created BETWEEN :start AND :end ORDER BY manifest.created DESC"); 
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId()); 
		query.setParameter("start", manifestSearch.getStartDate()); 
		query.setParameter("end", manifestSearch.getEndDate()); 
		List<Manifest> manifestList = (List<Manifest>)query.getResultList(); 
		return manifestList; 
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public RtvSheet getRtvSheetByCourierAndRtvId(String courierCode, Long id){
		
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select rtvSheet from RtvSheet rtvSheet "
				+ "join rtvSheet.productDetails inventory where rtvSheet.courierCode =:courierCode "
				+ "and rtvSheet.id =:id and inventory.status ='RTV' and rtvSheet.warehouseDetails =:warehouse"); 
		query.setParameter("courierCode", courierCode);
		query.setParameter("id", id);
		query.setParameter("warehouse", sessionDetails.getSessionWarehouse());

		List<RtvSheet> rtvSheetList = (List<RtvSheet>)query.getResultList();
		
		if(rtvSheetList != null && rtvSheetList.size() > 0)
			return rtvSheetList.get(0);
		else
			return null;
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<RtvSheet> getRtvSheetByCourierHavingStatusRTV(String courierCode){
		
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select rtvSheet from RtvSheet rtvSheet "
				+ "join rtvSheet.productDetails inventory where rtvSheet.courierCode =:courierCode "
				+ "and inventory.status ='RTV' and rtvSheet.warehouseDetails =:warehouse"); 
		query.setParameter("courierCode", courierCode); 
		query.setParameter("warehouse", sessionDetails.getSessionWarehouse());

		List<RtvSheet> rtvSheetList = (List<RtvSheet>)query.getResultList();		
		return rtvSheetList;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<RtvSheet> getRtvSheetByIdList(List<Long> rtvListId){
		
		EntityManager entityManager = entityDao.getEntityManager();	
		List<Long> batchRtvSheetListId = new ArrayList<Long>();
		int totalCount = rtvListId.size();
		int count = 0;
		
		List<RtvSheet> rtvSheetList = new ArrayList<RtvSheet>();
		for(Long id : rtvListId){
			totalCount--;
			batchRtvSheetListId.add(id);
			count++;
			
			/** Now, get the list in a set of 20 for faster selection.**/
			if(count == 20 || (totalCount == 0 && batchRtvSheetListId.size() > 0)){
				Query query = entityManager.createQuery("Select rtvSheet from RtvSheet rtvSheet "
						+ "where rtvSheet.id IN (:id)"); 
				query.setParameter("id", batchRtvSheetListId);	
				rtvSheetList.addAll((List<RtvSheet>)query.getResultList());
				entityManager.flush();
				entityManager.clear();
				batchRtvSheetListId.clear();
				count = 0;
			}
		}
		
		return rtvSheetList;	
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Manifest getManifestByIdAndFetchRtvSheetEagerly(Long id) { 
		EntityManager entityManager = entityDao.getEntityManager(); 
		Query query = entityManager.createQuery("SELECT manifest FROM Manifest manifest "
				+ "JOIN FETCH manifest.rtvSheet WHERE manifest.id = (:id)"); 
		query.setParameter("id", id); 
		List<Manifest> manifestList = (List<Manifest>)query.getResultList(); 
		if(manifestList.size() > 0) 
			return manifestList.get(0); 
		else 
			return null; 
	}
	
	@Override
	public void updateInventorySetStatusManifestPrinted(List<Long> inventoryId){
		
		EntityManager entityManager = entityDao.getEntityManager();	
		List<Long> batchInventoryIdList = new ArrayList<Long>();
		int totalCount = inventoryId.size();
		int count = 0;
		
		for(Long id : inventoryId){

			totalCount--;
			batchInventoryIdList.add(id);
			count++;
			
			/** Now, update the list in a set of 20 for faster update.**/
			if(count == 20 || (totalCount == 0 && batchInventoryIdList.size() > 0)){

				Query query = entityManager.createQuery("UPDATE Inventory inventory SET inventory.status = :status," +
							"inventory.updated = :updated, inventory.updatedBy = :updatedBy where inventory.id IN (:id)");
				query.setParameter("id", batchInventoryIdList);
				query.setParameter("updatedBy", sessionDetails.getSessionUser());
				query.setParameter("updated",new Date());
				query.setParameter("status", Constants.MANIFEST_PRINTED);
				query.executeUpdate();		
				entityManager.flush();
				entityManager.clear();
				batchInventoryIdList.clear();
				count = 0;
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Manifest> checkManifestedbyIdList(Long rtvListId) 
	{
		EntityManager entityManager = entityDao.getEntityManager(); 
		Query query = entityManager.createQuery("SELECT manifest FROM Manifest manifest "
				+ "JOIN FETCH manifest.rtvSheet rtvsheet WHERE rtvsheet.id = :id"); 
		query.setParameter("id", rtvListId); 
		List<Manifest> manifestList = (List<Manifest>)query.getResultList(); 
		if(manifestList.size() > 0) 
			return manifestList; 
		else 
			return null; 
		
	}
}
