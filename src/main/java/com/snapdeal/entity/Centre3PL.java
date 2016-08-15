package com.snapdeal.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="center_3pl")
public class Centre3PL extends BaseEntity {

private static final long serialVersionUID = 1L;
	
	@Column(name="name")
	private String name;
	
	@Column(name="code")
	private String code;
	
	@OneToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	private Address address;
	
	@Column(name="enabled")
	private Boolean enabled;
	
	@OneToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	private Warehouse warehouse;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	
}

