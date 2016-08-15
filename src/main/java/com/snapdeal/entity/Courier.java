package com.snapdeal.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="couriers")
public class Courier extends BaseEntity {
	
	/**
	 * Courier Entity
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="name",length=100,nullable=false)
	private String name;
	
	@Column(name="code",length=200,nullable=false)
	private String code;
	
	@Column(name="primary_email",length=200,nullable=false,unique=true)
	private String primaryEmail;
	
	@Column(name="secondary_email",length=500,nullable=true)
	private String secondaryEmail;
	
	@Column(name="score_courier_code",length=200,nullable=true)
	private String scoreCourierCode;
	
	@Column(name="soft_data_subject",length=500,nullable=true)
	private String softDataSubject;

	@Column(name="soft_data_template",length=1000,nullable=true)
	private String softDataTemplate;
	
	@Column(name="soft_data_header",length=500,nullable=true)
	private String softDataHeader;
	
	@Column(name="tracking_link",length=200,nullable=true)
	private String trackingLink;
	
	@Column(name="max_load",nullable=false)
	private Integer maxLoad;
	
	@Column(name="advance_AWBs")
	private Boolean advanceAWBs = false;
	
	@Column(name="shipping_mode",length=50,nullable=false)
	private String shippingMode;
	
	@Column(name="enabled")
	private Boolean enabled = false;
	
	@OneToMany(targetEntity = ICARules.class, fetch = FetchType.LAZY, mappedBy = "courier",cascade = CascadeType.ALL)
	private List<ICARules> iCARules;

	@OneToMany(targetEntity = DailyMaxLoad.class, fetch = FetchType.LAZY, mappedBy = "courier",cascade = CascadeType.ALL)
	private List<DailyMaxLoad> dailyMaxLoad;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Warehouse> warehouseList;
	
	public List<ICARules> getiCARules() {
		return iCARules;
	}

	public void setiCARules(List<ICARules> iCARules) {
		this.iCARules = iCARules;
	}

    public List<DailyMaxLoad> getDailyMaxLoad() {
		return dailyMaxLoad;
	}

	public void setDailyMaxLoad(List<DailyMaxLoad> dailyMaxLoad) {
		this.dailyMaxLoad = dailyMaxLoad;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPrimaryEmail() {
		return primaryEmail;
	}

	public void setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = primaryEmail;
	}

	public String getSecondaryEmail() {
		return secondaryEmail;
	}

	public void setSecondaryEmail(String secondaryEmail) {
		this.secondaryEmail = secondaryEmail;
	}

	public String getScoreCourierCode() {
		return scoreCourierCode;
	}

	public void setScoreCourierCode(String scoreCourierCode) {
		this.scoreCourierCode = scoreCourierCode;
	}

	public String getSoftDataSubject() {
		return softDataSubject;
	}

	public void setSoftDataSubject(String softDataSubject) {
		this.softDataSubject = softDataSubject;
	}

	public String getSoftDataTemplate() {
		return softDataTemplate;
	}

	public void setSoftDataTemplate(String softDataTemplate) {
		this.softDataTemplate = softDataTemplate;
	}

	public String getSoftDataHeader() {
		return softDataHeader;
	}

	public void setSoftDataHeader(String softDataHeader) {
		this.softDataHeader = softDataHeader;
	}

	public String getTrackingLink() {
		return trackingLink;
	}

	public void setTrackingLink(String trackingLink) {
		this.trackingLink = trackingLink;
	}

	public Integer getMaxLoad() {
		return maxLoad;
	}

	public void setMaxLoad(Integer maxLoad) {
		this.maxLoad = maxLoad;
	}

	public Boolean getAdvanceAWBs() {
		return advanceAWBs;
	}

	public void setAdvanceAWBs(Boolean advanceAWBs) {
		this.advanceAWBs = advanceAWBs;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getShippingMode() {
		return shippingMode;
	}

	public void setShippingMode(String shippingMode) {
		this.shippingMode = shippingMode;
	}

	public void setWarehouseList(List<Warehouse> warehouseList) {
		this.warehouseList = warehouseList;
	}

	public List<Warehouse> getWarehouseList() {
		return warehouseList;
	}


}