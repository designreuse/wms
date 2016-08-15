package com.snapdeal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ica_rules")
public class ICARules extends BaseEntity{
	
	/**
	 * ICA Rules Entity
	 */
	private static final long serialVersionUID = 1L;
	@Column(name="return_type",length=50)
	private String returnType;
	
	@Column(name="type",length=50,nullable=false)
	private String type;
	
	@Column(name="location",length=100,nullable=false)
	private String location;
	
	@Column(name="courier_code",length=200,nullable=false)
	private String courierCode;
	
	@Column(name="priority",nullable=false)
	private Integer priority;
	
	@Column(name="start_date",nullable=false)
	private Date startDate;
	
	@Column(name="end_date",nullable=true)
	private Date endDate;
	
	@Column(name="enabled",nullable=false)
	private Boolean enabled = true;
	
	@Column(name="is_rule_enabled",nullable=false)
	private Boolean isRuleEnabled = true;
	
	@ManyToOne
    @JoinColumn(name = "courier_code_FK", referencedColumnName = "code")
    private Courier courier;

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public Courier getCourier() {
		return courier;
	}

	public void setCourier(Courier courier) {
		this.courier = courier;
	}

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
