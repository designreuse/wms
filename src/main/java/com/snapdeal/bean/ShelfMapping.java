package com.snapdeal.bean;

import java.io.Serializable;
import java.util.List;

import com.snapdeal.entity.Shelf;

public class ShelfMapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private  List<Shelf> shelfList;
	private Long groupId;
	public List<Shelf> getShelfList() {
		return shelfList;
	}
	public void setShelfList(List<Shelf> shelfList) {
		this.shelfList = shelfList;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

}
