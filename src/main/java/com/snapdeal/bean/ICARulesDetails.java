package com.snapdeal.bean;

import java.io.Serializable;
import java.util.Date;

public class ICARulesDetails implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String type;	
	private String location;	
	private String courierCode;	
	private Integer priority;	
	private Date startDate;	
	private Date endDate;	
	private Boolean enabled;
	private Boolean isRuleEnabled;
	
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
	public String getCourierCode() {
		return courierCode;
	}
	public void setCourierCode(String courierCode) {
		this.courierCode = courierCode;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
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
	
}
