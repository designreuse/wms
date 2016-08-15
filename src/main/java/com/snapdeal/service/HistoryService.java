package com.snapdeal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snapdeal.entity.Inventory;
import com.snapdeal.entity.InventoryHistory;

@Service
public interface HistoryService {
	public void addInventoryHistoryByList(List<Inventory> inventoryList,String action);
	public void addInventoryHistory(Inventory inventory,String action);
	public void addInventoryHistoryByIdList(List<Long> inventoryId,String action);
	public List<InventoryHistory> findHistoryByBarcode(String barcode);
}
