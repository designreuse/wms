package com.snapdeal.bean;

import java.io.Serializable;
import java.util.List;

public class Rtv implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String  returnType;
	private List<RtvGroups> rtvGroupList;


	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public List<RtvGroups> getRtvGroupList() {
		return rtvGroupList;
	}

	public void setRtvGroupList(List<RtvGroups> rtvGroupList) {
		this.rtvGroupList = rtvGroupList;
	}

}
