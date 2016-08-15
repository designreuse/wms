package com.snapdeal.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.bean.WarehouseSearch;
import com.snapdeal.component.Constants;
import com.snapdeal.component.SessionDetails;
import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.Inventory;
import com.snapdeal.entity.WarehouseList;
import com.snapdeal.entity.RtvCheckRule;

@Named("putawayService")
@Transactional
public class PutawayServiceImpl implements PutawayService {

	@Inject
	@Named("entityDao")
	EntityDao entityDao;

	@Inject
	@Named("sessionDetails")
	SessionDetails sessionDetails;

	@SuppressWarnings("unchecked")
	@Override
	public boolean checkBarcode(String barcode) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select inventory from Inventory inventory JOIN " +
				"inventory.warehouse warehouse where warehouse.id = :warehouseId and inventory.barcode = :barcode " +
				"and inventory.status IN (:status)");
		List<String> statusList = new ArrayList<String>();

		statusList.add(Constants.PUTAWAY_STATUS);
		statusList.add(Constants.PUTAWAY_LIST_PRINTED);
		statusList.add(Constants.IN_WAREHOUSE_STATUS);
		statusList.add(Constants.PICK_LIST_GENERATED);
		statusList.add(Constants.PICK_LIST_PRINTED);
		/*statusList.add(Constants.RTV_STATUS);*/
		query.setParameter("barcode", barcode);
		query.setParameter("status", statusList);
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<Inventory> inventoryList = (List<Inventory>)query.getResultList();
		if(inventoryList != null && inventoryList.size() > 0)
		{
			return true;
		}
		else{
			return false;	
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean checkBarcodeForDirectPutaway(String barcode) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select inventory from Inventory inventory JOIN " +
				"inventory.warehouse warehouse where warehouse.id = :warehouseId and inventory.barcode = :barcode " +
				"and inventory.status IN (:status)");
		List<String> statusList = new ArrayList<String>();

		statusList.add(Constants.RTV_STATUS);
		
		query.setParameter("barcode", barcode);
		query.setParameter("status", statusList);
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<Inventory> inventoryList = (List<Inventory>)query.getResultList();
		if(inventoryList != null && inventoryList.size() > 0)
		{
			return true;
		}
		else{
			return false;	
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<RtvCheckRule> getEnabledRtvRules() {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select rule from RtvCheckRule rule JOIN rule.warehouse" +
		" warehouse where warehouse.id = :warehouseId order by rule.priority");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<RtvCheckRule> rtvCheckRules = (List<RtvCheckRule>) query.getResultList();
		return rtvCheckRules;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean checkProductForExistingVendor(String groupName,
			String vendorCode) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select inventory from Inventory inventory JOIN inventory.warehouse" +
				" warehouse where warehouse.id = :warehouseId and inventory.groupName = :groupName " +
		"and inventory.vendorCode = :vendor and inventory.status = :status");
		query.setParameter("groupName", groupName);
		query.setParameter("vendor", vendorCode);
		query.setParameter("status", Constants.PUTAWAY_STATUS);
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<Inventory> inventoryList = (List<Inventory>)query.getResultList();
		if(inventoryList != null && inventoryList.size() > 0)
		{
			return true;
		}
		else {
			return false;	
		}
	}

	@Override
	public Long saveOrUpdateInventory(Inventory inventory) {
		entityDao.saveOrUpdateByWarehouse(inventory);
		return inventory.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public WarehouseList findPutawayListById(Long id) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select list from WarehouseList list JOIN list.warehouse warehouse " +
		"where warehouse.id = :warehouseId and list.type = :listType and list.id = :id");
		query.setParameter("id", id);
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("listType",Constants.PUTAWAY_LIST);
		List<WarehouseList> warehouseLists = (List<WarehouseList>)query.getResultList();
		if(warehouseLists != null && warehouseLists.size() > 0)
		{
			return warehouseLists.get(0);
		}
		return null;
	}

	@Override
	public WarehouseList saveOrUpdatePutawayList(WarehouseList putawayList) {
		entityDao.saveOrUpdateByWarehouse(putawayList);
		return putawayList;
	}

	public Inventory findInventoryById(Long id)
	{
		return entityDao.findByWarehouseById(Inventory.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WarehouseList> searchPutawayList(WarehouseSearch putawaySearch) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select list from WarehouseList list JOIN list.warehouse warehouse " +
		"where warehouse.id = :warehouseId AND list.created BETWEEN :start AND :end  and list.type = :listType ORDER BY list.created DESC");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("start", putawaySearch.getStartDate());
		query.setParameter("end", putawaySearch.getEndDate());
		query.setParameter("listType",Constants.PUTAWAY_LIST);
		List<WarehouseList> searchList = (List<WarehouseList>) query.getResultList();
		return searchList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Inventory findInventoryByBarcode(String barcode) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select inventory from Inventory inventory JOIN " +
		"inventory.warehouse warehouse where warehouse.id = :warehouseId and inventory.barcode = :barcode");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("barcode", barcode);
		List<Inventory> inventoryList = (List<Inventory>)query.getResultList();
		if(inventoryList != null && inventoryList.size() > 0)
		{
			return inventoryList.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public WarehouseList findPutawayListByIdToConfirm(Long id) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select list from WarehouseList list JOIN list.warehouse warehouse," +
				" IN (list.inventoryList) inventory where warehouse.id = :warehouseId and list.type = :listType" +
		" and list.id = :id and inventory.status IN (:status)");
		query.setParameter("id", id);
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("listType",Constants.PUTAWAY_LIST);
		List<String> status = new ArrayList<String>();
		status.add(Constants.PUTAWAY_STATUS);
		status.add(Constants.PUTAWAY_LIST_PRINTED);
		query.setParameter("status", status);
		List<WarehouseList> warehouseLists = (List<WarehouseList>)query.getResultList();
		if(warehouseLists != null && warehouseLists.size() > 0)
		{
			return warehouseLists.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public WarehouseList findPutawayListByBarcodeToConfirm(String barcode) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select list from WarehouseList list JOIN list.warehouse warehouse," +
				" IN (list.inventoryList) inventory where warehouse.id = :warehouseId and list.type = :listType" +
				" and inventory.barcode = :barcode and inventory.status IN (:status) order by list.id desc");
		query.setParameter("barcode", barcode);
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("listType",Constants.PUTAWAY_LIST);
		List<String> status = new ArrayList<String>();
		status.add(Constants.PUTAWAY_STATUS);
		status.add(Constants.PUTAWAY_LIST_PRINTED);
		query.setParameter("status", status);
		List<WarehouseList> warehouseLists = (List<WarehouseList>)query.getResultList();
		if(warehouseLists != null && warehouseLists.size() > 0)
		{
			return warehouseLists.get(0);
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean checkBarcodeinAnotherWarehouse(String barcode) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select inventory from Inventory inventory " +
				" where  inventory.barcode = :barcode " +
				"and inventory.status IN (:status)");
		List<String> statusList = new ArrayList<String>();
		statusList.add(Constants.PUTAWAY_STATUS);
		statusList.add(Constants.PUTAWAY_LIST_PRINTED);
		statusList.add(Constants.IN_WAREHOUSE_STATUS);
		statusList.add(Constants.PICK_LIST_GENERATED);
		statusList.add(Constants.PICK_LIST_PRINTED);
		/*statusList.add(Constants.RTV_STATUS);*/
		query.setParameter("barcode", barcode);
		query.setParameter("status", statusList);
//		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<Inventory> inventoryList = (List<Inventory>)query.getResultList();
		if(inventoryList != null && inventoryList.size() > 0)
		{
			return true;
		}
		else{
			return false;	
		}
	}

}
