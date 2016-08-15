package com.snapdeal.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="warehouse_list")
public class WarehouseList extends BaseWarehouseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToMany(fetch=FetchType.EAGER,cascade=CascadeType.MERGE)
	private List<Inventory> inventoryList;

	@ManyToOne
	private WarehouseBoy warehouseBoy;

	@Column(name="type")
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public WarehouseBoy getWarehouseBoy() {
		return warehouseBoy;
	}

	public void setWarehouseBoy(WarehouseBoy warehouseBoy) {
		this.warehouseBoy = warehouseBoy;
	}

	public List<Inventory> getInventoryList() {
		return inventoryList;
	}

	public void setInventoryList(List<Inventory> inventoryList) {
		this.inventoryList = inventoryList;
	}

}
