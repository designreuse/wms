package com.snapdeal.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="shelf")
public class Shelf extends BaseWarehouseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="name", length=100, nullable=false)
	private String name;
	
	@Column(name="seller_initials", length=100, nullable=false)
	private String sellerInitial;

	@OneToMany(cascade=CascadeType.ALL)
	private List<Floor> floorList;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSellerInitial() {
		return sellerInitial;
	}

	public void setSellerInitial(String sellerInitial) {
		this.sellerInitial = sellerInitial;
	}

	public List<Floor> getFloorList() {
		return floorList;
	}

	public void setFloorList(List<Floor> floorList) {
		this.floorList = floorList;
	}
}
