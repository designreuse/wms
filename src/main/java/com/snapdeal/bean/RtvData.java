package com.snapdeal.bean;

import java.io.Serializable;
import java.util.List;

import com.snapdeal.entity.Inventory;

public class RtvData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long listId;
	private List<Inventory> missingInventory;
	public Long getListId() {
		return listId;
	}
	public void setListId(Long listId) {
		this.listId = listId;
	}
	public List<Inventory> getMissingInventory() {
		return missingInventory;
	}
	public void setMissingInventory(List<Inventory> missingInventory) {
		this.missingInventory = missingInventory;
	}
}
