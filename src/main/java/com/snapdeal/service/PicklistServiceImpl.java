package com.snapdeal.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.bean.SellerDetails;
import com.snapdeal.bean.WarehouseSearch;
import com.snapdeal.component.Constants;
import com.snapdeal.component.SessionDetails;
import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.Inventory;
import com.snapdeal.entity.WarehouseList;

@Named("picklistService")
@Transactional
public class PicklistServiceImpl implements PicklistService {

	@Inject
	@Named("entityDao")
	EntityDao entityDao;

	@Inject
	@Named("sessionDetails")
	SessionDetails sessionDetails;

	@SuppressWarnings("unchecked")
	@Override
	public List<SellerDetails> getSellers(String groupName) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("SELECT DISTINCT inventory.vendorCode,inventory.sellerName from Inventory " +
				"inventory JOIN inventory.warehouse warehouse where warehouse.id = :warehouseId and " +
		"inventory.groupName = :groupName and inventory.status = :status");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("groupName", groupName);
		query.setParameter("status", Constants.IN_WAREHOUSE_STATUS);
		List<Object[]> rows = query.getResultList();
		List<SellerDetails> sellerDetailsList = new ArrayList<SellerDetails>();
		for(Object[] row : rows)
		{
			SellerDetails sellerDetails = new SellerDetails();
			sellerDetails.setVendorCode((String)row[0]);
			sellerDetails.setSellerName((String)row[1]);
			sellerDetailsList.add(sellerDetails);
		}
		return sellerDetailsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getGroups() {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("SELECT DISTINCT inventory.groupName from Inventory inventory " +
		"JOIN inventory.warehouse warehouse where warehouse.id = :warehouseId and inventory.status = :status");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("status", Constants.IN_WAREHOUSE_STATUS);
		List<String> groupList = (List<String>)query.getResultList();
		return groupList;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Inventory> findInventoryByGroupAndSeller(String groupName,
			List<String> vendorCode) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("SELECT inventory from Inventory inventory JOIN inventory.warehouse" +
		" warehouse where warehouse.id = :warehouseId and inventory.groupName = :groupName and " +
		"inventory.vendorCode IN (:vendorCode) and inventory.status = :status ORDER BY inventory.manifestDate, inventory.sellerName");
		query.setMaxResults(25);
		query.setParameter("status", Constants.IN_WAREHOUSE_STATUS);
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("groupName", groupName);
		query.setParameter("vendorCode", vendorCode);
		List<Inventory> inventoryList = (List<Inventory>)query.getResultList();
		return inventoryList;
	}

	@Override
	public WarehouseList saveOrUpdatePicklist(WarehouseList pickList) {
		entityDao.saveOrUpdateByWarehouse(pickList);
		return pickList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public WarehouseList findPicklistById(Long id) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select list from WarehouseList list JOIN list.warehouse warehouse " +
		"where warehouse.id = :warehouseId and list.type = :listType and list.id = :id");
		query.setParameter("id", id);
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("listType",Constants.PICK_LIST);
		List<WarehouseList> warehouseLists = (List<WarehouseList>)query.getResultList();
		if(warehouseLists != null && warehouseLists.size() > 0)
		{
			return warehouseLists.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WarehouseList> searchPickList(WarehouseSearch picklistSearch) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select list from WarehouseList list JOIN list.warehouse warehouse " +
		"where warehouse.id = :warehouseId AND list.created BETWEEN :start AND :end  and list.type = :listType ORDER BY list.created DESC");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("start", picklistSearch.getStartDate());
		query.setParameter("end", picklistSearch.getEndDate());
		query.setParameter("listType",Constants.PICK_LIST);
		List<WarehouseList> searchList = (List<WarehouseList>) query.getResultList();
		return searchList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getWarehouseEnabledGroups() {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("SELECT rule.group.name from Rule rule " +
		"JOIN rule.warehouse warehouse where warehouse.id = :warehouseId and rule.warehouseFlag = true");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<String> groupList = (List<String>)query.getResultList();
		return groupList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getRtvEnabledGroups() {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("SELECT rule.group.name from Rule rule " +
		"JOIN rule.warehouse warehouse where warehouse.id = :warehouseId and rule.warehouseFlag = false");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<String> groupList = (List<String>)query.getResultList();
		query = entityManager.createQuery("SELECT rule.group.name from BulkRule rule " +
		"JOIN rule.warehouse warehouse where warehouse.id = :warehouseId");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<String> bulkGroupList = (List<String>)query.getResultList();
		groupList.addAll(bulkGroupList);
		return groupList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getLiquidationEnabledGroups() {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("SELECT rule.group.name from Rule rule " +
		"JOIN rule.warehouse warehouse where warehouse.id = :warehouseId and rule.liquidation = true");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<String> groupList = (List<String>)query.getResultList();
		query = entityManager.createQuery("SELECT rule.group.name from BulkRule rule " +
		"JOIN rule.warehouse warehouse where warehouse.id = :warehouseId and rule.liquidation = true");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<String> bulkGroupList = (List<String>)query.getResultList();
		groupList.addAll(bulkGroupList);
		return groupList;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Inventory> findInventoryByGroup(String groupName) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("SELECT inventory from Inventory inventory JOIN inventory.warehouse" +
		" warehouse where warehouse.id = :warehouseId and inventory.groupName = :groupName and " +
		"inventory.status = :status ORDER BY inventory.sellerName");
		query.setMaxResults(25);
		query.setParameter("status", Constants.IN_WAREHOUSE_STATUS);
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("groupName", groupName);
		List<Inventory> inventoryList = (List<Inventory>)query.getResultList();
		return inventoryList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getRtcEnabledGroups() {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("SELECT rule.group.name from Rule rule " +
		"JOIN rule.warehouse warehouse where warehouse.id = :warehouseId and rule.rtc = true");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<String> groupList = (List<String>)query.getResultList();
		
		query = entityManager.createQuery("SELECT rule.group.name from BulkRule rule " +
		"JOIN rule.warehouse warehouse where warehouse.id = :warehouseId and rule.rtc = true");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<String> bulkGroupList = (List<String>)query.getResultList();
		
		groupList.addAll(bulkGroupList);
		return groupList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> get3PLEnabledGroups() {
		EntityManager entityManager = entityDao.getEntityManager();
		
		Query query = entityManager.createQuery("SELECT rule.group.name from Rule rule " +
		"JOIN rule.warehouse warehouse where warehouse.id = :warehouseId and rule.flag3pl = true");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<String> groupList = (List<String>)query.getResultList();
		
		query = entityManager.createQuery("SELECT rule.group.name from BulkRule rule " +
		"JOIN rule.warehouse warehouse where warehouse.id = :warehouseId and rule.flag3pl = true");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<String> bulkGroupList = (List<String>)query.getResultList();
		
		groupList.addAll(bulkGroupList);
		return groupList;
	}
}
