package com.snapdeal.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="floors")
public class Floor extends BaseWarehouseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="floor_name",length=100,nullable=false)
	private String name;
	
	@Column(name="capacity",nullable=false)
	private Integer capacity;

	@OneToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	private List<Box> boxList;
	
	public List<Box> getBoxList() {
		return boxList;
	}

	public void setBoxList(List<Box> boxList) {
		this.boxList = boxList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
}
