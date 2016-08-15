package com.snapdeal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="shipping_location")
public class ShippingLocation extends BaseEntity {
	
	/**
	 * Shipping Location Entity
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="courier_code",length=200,nullable=false)
	private String courierCode;
	
	@Column(name="pincode",length=50,nullable=false)
	private String pincode;
	
	@Column(name="area_code",length=50,nullable=false)
	private String areaCode;
	
	@Column(name="estimation_delivery_days",nullable=true)
	private Integer estimationDeliveryDays;
	
	@Column(name="enabled")
	private Boolean enabled = false;

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getCourierCode() {
		return courierCode;
	}

	public void setCourierCode(String courierCode) {
		this.courierCode = courierCode;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public Integer getEstimationDeliveryDays() {
		return estimationDeliveryDays;
	}

	public void setEstimationDeliveryDays(Integer estimationDeliveryDays) {
		this.estimationDeliveryDays = estimationDeliveryDays;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaCode() {
		return areaCode;
	}
}
