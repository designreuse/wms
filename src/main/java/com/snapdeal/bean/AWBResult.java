package com.snapdeal.bean;

public class AWBResult {
	/*
	 * This will store the result of the API call for courierCode and tracking number.
	 */
	
	String courierCode;
	String trackingNumber;
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
}
