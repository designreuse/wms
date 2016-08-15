package com.snapdeal.bean;

import java.io.Serializable;
import java.util.List;

import com.snapdeal.entity.Inventory;

public class RtvGroups implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String vendorCode;
	private String sellerName;
	private List<Inventory> inventoryList;

	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public List<Inventory> getInventoryList() {
		return inventoryList;
	}
	public void setInventoryList(List<Inventory> inventoryList) {
		this.inventoryList = inventoryList;
	}

}
