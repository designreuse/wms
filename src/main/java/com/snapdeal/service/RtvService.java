package com.snapdeal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snapdeal.bean.WarehouseSearch;
import com.snapdeal.entity.Address;
import com.snapdeal.entity.Inventory;
import com.snapdeal.entity.Liquidation;
import com.snapdeal.entity.RtvSheet;
import com.snapdeal.entity.WarehouseList;

@Service
public interface RtvService {

	public WarehouseList getRtvEligibleListById(Long id);
	public void markInventoryInWarehouse(List<Long> inventoryId);
	public RtvSheet saveOrUpdateRtvSheet(RtvSheet rtvSheet);
	public RtvSheet findRtvSheetById(Long id);
	public List<Inventory> getLoadedInventoryList(List<Long> inventoryIdList);
	public Inventory getInventoryForReturn(String barcode);
	public boolean checkScannedProductsForWarehouse(List<Long> inventoryId);
	public List<RtvSheet> searchRtvSheetBetweenDates(WarehouseSearch warehouseSearch);
	public List<RtvSheet> searchRtvSheetByBarcode(String barcode);
	public Address get3plLiquidationDetail(String return_type);
//	public Address getCustomerAddressDetail(String return_type);
	public boolean checkScannedProductsForLiquidation();
	public boolean checkScannedProductsFor3pl();
}
