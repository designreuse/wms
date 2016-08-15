package com.snapdeal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snapdeal.bean.SellerDetails;
import com.snapdeal.bean.WarehouseSearch;
import com.snapdeal.entity.Inventory;
import com.snapdeal.entity.WarehouseList;

@Service
public interface PicklistService {

	public List<SellerDetails> getSellers(String groupName);
	public List<String> getGroups();
	public List<Inventory> findInventoryByGroupAndSeller(String groupName,List<String> vendorCode);
	public List<Inventory> findInventoryByGroup(String groupName);
	public WarehouseList saveOrUpdatePicklist(WarehouseList pickList);
	public WarehouseList findPicklistById(Long id);
	public List<WarehouseList> searchPickList(WarehouseSearch picklistSearch);
	public List<String> getWarehouseEnabledGroups();
	public List<String> getRtvEnabledGroups();
	public List<String> getLiquidationEnabledGroups();
	public List<String> getRtcEnabledGroups();
	public List<String> get3PLEnabledGroups();
}
