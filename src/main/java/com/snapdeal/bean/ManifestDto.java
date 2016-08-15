package com.snapdeal.bean;

import java.util.List;

public class ManifestDto {
	
	private String name;
	private String type;
	private List<Long> rtvList;
	private List<String> bagList;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Long> getRtvList() {
		return rtvList;
	}
	public void setRtvList(List<Long> rtvList) {
		this.rtvList = rtvList;
	}
	public List<String> getBagList() {
		return bagList;
	}
	public void setBagList(List<String> bagList) {
		this.bagList = bagList;
	}	
}
