package com.snapdeal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snapdeal.bean.WarehouseSearch;
import com.snapdeal.entity.Inventory;
import com.snapdeal.entity.WarehouseList;
import com.snapdeal.entity.RtvCheckRule;

@Service
public interface PutawayService {

	public boolean checkBarcode(String barcode);
	public boolean checkBarcodeForDirectPutaway(String barcode);
	public List<RtvCheckRule> getEnabledRtvRules();
	public boolean checkProductForExistingVendor(String groupName,String vendorCode);
	public Long saveOrUpdateInventory(Inventory inventory);
	public WarehouseList findPutawayListById(Long id);
	public WarehouseList findPutawayListByIdToConfirm(Long id);
	public WarehouseList saveOrUpdatePutawayList(WarehouseList putawayList);
	public Inventory findInventoryById(Long id);
	public Inventory findInventoryByBarcode(String barcode);
	public List<WarehouseList> searchPutawayList(WarehouseSearch putawaySearch);
	public WarehouseList findPutawayListByBarcodeToConfirm(String barcode);
	public boolean checkBarcodeinAnotherWarehouse(String barcode);
}
