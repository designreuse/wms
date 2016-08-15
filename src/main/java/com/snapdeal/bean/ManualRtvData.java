package com.snapdeal.bean;

import java.io.Serializable;
import java.util.List;

import com.snapdeal.entity.Inventory;

public class ManualRtvData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String barcode;
	private String returnType; 
	private List<Inventory> inventoryList;

	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public List<Inventory> getInventoryList() {
		return inventoryList;
	}
	public void setInventoryList(List<Inventory> inventoryList) {
		this.inventoryList = inventoryList;
	}

}
