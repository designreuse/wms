package com.snapdeal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="postal_code")
public class PostalCode extends BaseEntity{
	/**
	 * Postal Code Entity
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="pincode",length=12,nullable=false)
	private String pincode;
	
	@Column(name="state",length=100,nullable=false)
	private String state;
	
	@Column(name="city",length=100,nullable=false)
	private String city;

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
