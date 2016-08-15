package com.snapdeal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="boxes")
public class Box extends BaseWarehouseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="box_name",length=100,nullable=false)
	private String name;

	@Column(name="capacity",nullable=false)
	private Integer capacity;

	@Column(name="used",nullable=false)
	private Integer used;

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public Integer getUsed() {
		return used;
	}

	public void setUsed(Integer used) {
		this.used = used;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
