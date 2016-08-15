package com.snapdeal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="inventory_history")
public class InventoryHistory extends BaseWarehouseEntity {

	/**
	 * Inventory History Entity
	 */
	private static final long serialVersionUID = 1L;

	@Column(length=200,nullable=false)
	private String barcode;
	
	@Column(nullable=false,length=500)
	private String action;

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
}
