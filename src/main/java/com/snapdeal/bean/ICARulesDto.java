package com.snapdeal.bean;

import java.util.Date;
import java.util.List;

public class ICARulesDto {
	/**
	 * ICA Rules Data Object for form.
	 */
	private String returnType;
	private String type;	
	private String location;	
	private List<String> courierCodeList = null;	
	private List<Integer> priorityList = null;	
	private Date startDate;	
	private Date endDate;	
	private Boolean enabled = true;
	private Boolean isRuleEnabled = false;

	public Boolean getIsRuleEnabled() {
		return isRuleEnabled;
	}
	public void setIsRuleEnabled(Boolean isRuleEnabled) {
		this.isRuleEnabled = isRuleEnabled;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public List<String> getCourierCodeList() {
		return courierCodeList;
	}
	public void setCourierCodeList(List<String> courierCodeList) {
		this.courierCodeList = courierCodeList;
	}
	public List<Integer> getPriorityList() {
		return priorityList;
	}
	public void setPriorityList(List<Integer> priorityList) {
		this.priorityList = priorityList;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
}
