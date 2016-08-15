package com.snapdeal.bean;

import java.io.Serializable;
import java.util.List;

import com.snapdeal.entity.RtvSheet;

public class RtvDispatch implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<RtvSheet> rtvSheets;

	public List<RtvSheet> getRtvSheets() {
		return rtvSheets;
	}

	public void setRtvSheets(List<RtvSheet> rtvSheets) {
		this.rtvSheets = rtvSheets;
	}
	
}
