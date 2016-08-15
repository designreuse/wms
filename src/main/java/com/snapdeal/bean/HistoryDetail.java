package com.snapdeal.bean;

import java.io.Serializable;

public class HistoryDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String barcode;
	private String action;
	private String date;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
}
