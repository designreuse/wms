package com.snapdeal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="property")
public class Property extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="name",length=100,nullable=false)
	private String name;
	
	@Lob
	@Column(name="value",nullable=false)
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
