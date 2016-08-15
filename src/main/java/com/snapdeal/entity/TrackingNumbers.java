package com.snapdeal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="tracking_numbers")
public class TrackingNumbers extends BaseEntity{
	
	/**
	 * Tacking Number Entity
	 */
	private static final long serialVersionUID = 1L;
	
	@Version
	private int version;
	
	@Column(name="courier_code",length=200,nullable=false)
	private String courierCode;
	
	@Column(name="tracking_number",length=50,nullable=false,unique=true)
	private String trackingNumber;
	
	@Column(name="is_used",nullable=false)
	private boolean isUsed = false;

	public String getCourierCode() {
		return courierCode;
	}

	public void setCourierCode(String courierCode) {
		this.courierCode = courierCode;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getVersion() {
		return version;
	}
}
