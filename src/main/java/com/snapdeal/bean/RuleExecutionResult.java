package com.snapdeal.bean;

public class RuleExecutionResult {

	private String groupName;
	private String location;
	private Long ruleId;
	private Boolean bulkRule = false;
	private String status;

	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Boolean getBulkRule() {
		return bulkRule;
	}
	public void setBulkRule(Boolean bulkRule) {
		this.bulkRule = bulkRule;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Long getRuleId() {
		return ruleId;
	}
	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

}
