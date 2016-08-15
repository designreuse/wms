package com.snapdeal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.bean.WarehouseSearch;
import com.snapdeal.component.Constants;
import com.snapdeal.component.SessionDetails;
import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.Address;
import com.snapdeal.entity.Centre3PL;
import com.snapdeal.entity.Inventory;
import com.snapdeal.entity.Liquidation;
import com.snapdeal.entity.RtvSheet;
import com.snapdeal.entity.WarehouseList;

@Named("rtvService")
@Transactional
public class RtvServiceImpl implements RtvService {

	@Inject
	@Named("entityDao")
	EntityDao entityDao;

	@Inject
	@Named("sessionDetails")
	SessionDetails sessionDetails;
	
	@Inject
	@Named("historyService")
	HistoryService historyService;

	@SuppressWarnings("unchecked")
	@Override
	public WarehouseList getRtvEligibleListById(Long id) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select list from WarehouseList list JOIN list.warehouse warehouse, " +
				"IN (list.inventoryList) inventory where warehouse.id = :warehouseId and inventory.status NOT IN (:status) and " +
		"list.type = :listType and list.id = :id");
		query.setParameter("id", id);
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("listType",Constants.PICK_LIST);
		List<String> statusList = new ArrayList<String>();
		statusList.add(Constants.IN_WAREHOUSE_STATUS);
		statusList.add(Constants.MISSING);
		statusList.add(Constants.PICK_LIST_GENERATED);
		statusList.add(Constants.PUTAWAY_LIST);
		statusList.add(Constants.PUTAWAY_LIST_PRINTED);
		statusList.add(Constants.PUTAWAY_STATUS);
		statusList.add(Constants.RTV_STATUS);
		query.setParameter("status", statusList);
		List<WarehouseList> warehouseLists = (List<WarehouseList>)query.getResultList();
		if(warehouseLists != null && warehouseLists.size() > 0)
		{
			return warehouseLists.get(0);
		}
		return null;
	}

	@Override
	public void markInventoryInWarehouse(List<Long> inventoryId) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("UPDATE Inventory inventory SET inventory.status = :status," +
		"inventory.updated = :updated, inventory.updatedBy = :updatedBy where inventory.id IN (:id)");
		query.setParameter("id", inventoryId);
		query.setParameter("updatedBy", sessionDetails.getSessionUser());
		query.setParameter("updated",new Date());
		query.setParameter("status", Constants.IN_WAREHOUSE_STATUS);
		query.executeUpdate();
	}

	@Override
	public RtvSheet saveOrUpdateRtvSheet(RtvSheet rtvSheet) {
		entityDao.saveOrUpdateByWarehouse(rtvSheet);
		return rtvSheet;
	}

	@Override
	public RtvSheet findRtvSheetById(Long id) {
		RtvSheet rtvSheet = entityDao.findByWarehouseById(RtvSheet.class, id);
		Hibernate.initialize(rtvSheet.getProductDetails());
		return rtvSheet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Inventory> getLoadedInventoryList(List<Long> inventoryIdList) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select inventory from Inventory inventory JOIN inventory.warehouse warehouse " +
		"where warehouse.id = :warehouseId and inventory.id IN (:inventoryId)");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("inventoryId", inventoryIdList);
		List<Inventory> inventories = (List<Inventory>)query.getResultList();
		return inventories;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Inventory getInventoryForReturn(String barcode) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select inventory from Inventory inventory JOIN inventory.warehouse warehouse " +
		"where warehouse.id = :warehouseId and inventory.status IN (:status) and inventory.barcode = :barcode");
		query.setParameter("barcode", barcode);
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<String> statusList = new ArrayList<String>();
		statusList.add(Constants.IN_WAREHOUSE_STATUS);
		statusList.add(Constants.RTV_INITIATED);
		query.setParameter("status", statusList);
		List<Inventory> inventories = (List<Inventory>)query.getResultList();
		if(inventories != null && inventories.size() > 0)
		{
			return inventories.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean checkScannedProductsForWarehouse(List<Long> inventoryId) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select inventory from Inventory inventory JOIN inventory.warehouse warehouse " +
		"where warehouse.id = :warehouseId and inventory.status IN (:status) and inventory.fulfillmentModel = :model " +
		"and inventory.id IN (:ids)");
		query.setParameter("ids", inventoryId);
		query.setParameter("model", Constants.FCVOI);
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<String> statusList = new ArrayList<String>();
		statusList.add(Constants.IN_WAREHOUSE_STATUS);
		statusList.add(Constants.RTV_INITIATED);
		query.setParameter("status", statusList);
		List<Inventory> inventories = (List<Inventory>)query.getResultList();
		if(inventories != null && inventories.size() > 0 && inventories.size() == inventoryId.size())
		{
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RtvSheet> searchRtvSheetBetweenDates(
			WarehouseSearch warehouseSearch) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select sheet from RtvSheet sheet JOIN sheet.warehouse warehouse " +
		"where warehouse.id = :warehouseId AND sheet.created BETWEEN :start AND :end ORDER BY sheet.created DESC");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("start", warehouseSearch.getStartDate());
		query.setParameter("end", warehouseSearch.getEndDate());
		List<RtvSheet> sheets = (List<RtvSheet>) query.getResultList();
		return sheets;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RtvSheet> searchRtvSheetByBarcode(String barcode){
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("SELECT sheet FROM RtvSheet sheet JOIN sheet.warehouse warehouse "
				+ "JOIN sheet.productDetails productDetails " 
				+ "WHERE warehouse.id = :warehouseId AND productDetails.barcode =:barcode ORDER BY sheet.created DESC");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("barcode", barcode);
		List<RtvSheet> sheets = (List<RtvSheet>) query.getResultList();
		return sheets;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Address get3plLiquidationDetail(String return_type) {
		EntityManager entityManager = entityDao.getEntityManager();	
		Address address = new Address();
		
		if (return_type.equals("liquidation")) {
			Query query=entityManager.createQuery("Select liquidation from Liquidation liquidation where liquidation.warehouse.id = :warehouseId and liquidation.enabled = true ");
			query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
			List<Liquidation> list=(List<Liquidation>)query.getResultList();
			if(list !=null){
				address=list.get(0).getAddress();
				return address;
			}
			else 
				return null;
		}
		else
			if (return_type.equals("3pl")) {
				Query query = entityManager.createQuery("Select centre3pl from Centre3PL centre3pl where centre3pl.warehouse.id = :warehouseId and centre3pl.enabled = true");
				query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
				List<Centre3PL> list=(List<Centre3PL>)query.getResultList();
				if(list !=null){
					address=list.get(0).getAddress();
					return address;
					}
				else 
					return null;
			}
		else
			return null;
		}

	@Override
	@SuppressWarnings("unchecked")
	public boolean checkScannedProductsForLiquidation() {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select liquidation from Liquidation liquidation "+ 
						"where liquidation.warehouse.id = :warehouseId and liquidation.enabled = true ");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<Liquidation> list = (List<Liquidation>)query.getResultList();
		if(list != null && list.size() > 0)
		{
			return true;
		}
		return false;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean checkScannedProductsFor3pl() {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select centre3pl from Centre3PL centre3pl "+ 
						"where centre3pl.warehouse.id = :warehouseId and centre3pl.enabled = true ");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<Centre3PL> list = (List<Centre3PL>)query.getResultList();
		if(list != null && list.size() > 0)
		{
			return true;
		}
		return false;
	}
}
