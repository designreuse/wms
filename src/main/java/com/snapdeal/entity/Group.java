package com.snapdeal.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="groups")
public class Group extends BaseWarehouseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="group_name", length=100, nullable=false, unique=true)
	private String name;

	@Column(name="number_shelves",nullable=false)
	private Integer numberOfShelves;
	
	@Column(name="number_floors",nullable=false)
	private Integer numberOfFloors;
	
	@Column(name="capacity_floors",nullable=false)
	private Integer capacityOfFloors;
	
	@Column(name="capacity_box",nullable=false)
	private Integer capacityOfBox;
	
	@OneToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	List<Shelf> shelfList;
	
	public Integer getCapacityOfBox() {
		return capacityOfBox;
	}

	public void setCapacityOfBox(Integer capacityOfBox) {
		this.capacityOfBox = capacityOfBox;
	}

	public List<Shelf> getShelfList() {
		return shelfList;
	}

	public void setShelfList(List<Shelf> shelfList) {
		this.shelfList = shelfList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNumberOfShelves() {
		return numberOfShelves;
	}

	public void setNumberOfShelves(Integer numberOfShelves) {
		this.numberOfShelves = numberOfShelves;
	}

	public Integer getNumberOfFloors() {
		return numberOfFloors;
	}

	public void setNumberOfFloors(Integer numberOfFloors) {
		this.numberOfFloors = numberOfFloors;
	}

	public Integer getCapacityOfFloors() {
		return capacityOfFloors;
	}

	public void setCapacityOfFloors(Integer capacityOfFloors) {
		this.capacityOfFloors = capacityOfFloors;
	}
}
