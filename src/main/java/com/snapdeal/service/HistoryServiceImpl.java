package com.snapdeal.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.component.SessionDetails;
import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.Inventory;
import com.snapdeal.entity.InventoryHistory;

@Named("historyService")
@Transactional
public class HistoryServiceImpl implements HistoryService{

	@Inject
	@Named("entityDao")
	EntityDao entityDao;

	@Inject
	@Named("putawayService")
	PutawayService putawayService;

	@Inject
	@Named("sessionDetails")
	SessionDetails sessionDetails;

	@Override
	public void addInventoryHistoryByList(List<Inventory> inventoryList,
			String action) {
		if(inventoryList != null && inventoryList.size() > 0)
		{
			for(Inventory inventory : inventoryList)
			{
				InventoryHistory inventoryHistory = new InventoryHistory();
				inventoryHistory.setBarcode(inventory.getBarcode());
				inventoryHistory.setAction(action);
				entityDao.saveOrUpdateByWarehouse(inventoryHistory);
			}
		}
	}

	@Override
	public void addInventoryHistory(Inventory inventory, String action) {
		if(inventory != null)
		{
			InventoryHistory inventoryHistory = new InventoryHistory();
			inventoryHistory.setBarcode(inventory.getBarcode());
			inventoryHistory.setAction(action);
			entityDao.saveOrUpdateByWarehouse(inventoryHistory);
		}
	}

	@Override
	public void addInventoryHistoryByIdList(List<Long> inventoryId,
			String action) {
		if(inventoryId != null && inventoryId.size() > 0)
		{
			for(Long id:inventoryId)
			{
				Inventory inventory = putawayService.findInventoryById(id);
				if(inventory != null)
				{
					InventoryHistory inventoryHistory = new InventoryHistory();
					inventoryHistory.setBarcode(inventory.getBarcode());
					inventoryHistory.setAction(action);
					entityDao.saveOrUpdateByWarehouse(inventoryHistory);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InventoryHistory> findHistoryByBarcode(String barcode) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select history from InventoryHistory history " +
		" where  history.barcode = :barcode " +
		"ORDER BY history.created");
//		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("barcode", barcode);
		List<InventoryHistory> inventoryHistories = (List<InventoryHistory>) query.getResultList();
		return inventoryHistories;
	}
}
