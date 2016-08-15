package com.snapdeal.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="rules")
public class Rule extends BaseWarehouseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="rule_name", length=100, nullable=false, unique=true)
	private String name;

	@OneToOne(cascade=CascadeType.ALL)
	private Price price;

	@Column(name="eligible_liquidation")
	private Boolean liquidation = false;

	@Column(name="eligible_rtv")
	private Boolean rtv = false;

	@Column(name="eligible_warehouse")
	private Boolean warehouseFlag = false;
	
	@Column(name="enabled")
	private Boolean enabled = false;
	
	@Column(name="eligible_rtc")
	private Boolean rtc = false;
	
	@Column(name="eligible_3pl")
	private Boolean flag3pl = false;


	@ManyToMany
	private List<CcStatus> ccStatusList;

	@ManyToMany
	private List<FulfillmentModel> fulfillmentModelList;

	@ManyToMany
	private List<IssueCategory> issueCategoryList;

	@ManyToMany
	private List<SubCategory> subCategoryList;

	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private Group group;

	@Column(name="qc_remarks",length=200)
	private String qcRemarks;
	
	@Column(name="priority",nullable=false)
	private Integer priority;
	
	@Column(name="manifest_operator")
	private String manifestOperator;
	
	@Column(name="days_passed")
	private Integer daysPassedManifest;
	
	public Boolean getWarehouseFlag() {
		return warehouseFlag;
	}

	public void setWarehouseFlag(Boolean warehouseFlag) {
		this.warehouseFlag = warehouseFlag;
	}

	public String getManifestOperator() {
		return manifestOperator;
	}

	public void setManifestOperator(String manifestOperator) {
		this.manifestOperator = manifestOperator;
	}

	public String getQcRemarks() {
		return qcRemarks;
	}

	public void setQcRemarks(String qcRemarks) {
		this.qcRemarks = qcRemarks;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getDaysPassedManifest() {
		return daysPassedManifest;
	}

	public void setDaysPassedManifest(Integer daysPassedManifest) {
		this.daysPassedManifest = daysPassedManifest;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public Boolean getLiquidation() {
		return liquidation;
	}

	public void setLiquidation(Boolean liquidation) {
		this.liquidation = liquidation;
	}

	public Boolean getRtv() {
		return rtv;
	}

	public void setRtv(Boolean rtv) {
		this.rtv = rtv;
	}

	public List<CcStatus> getCcStatusList() {
		return ccStatusList;
	}

	public void setCcStatusList(List<CcStatus> ccStatusList) {
		this.ccStatusList = ccStatusList;
	}

	public List<FulfillmentModel> getFulfillmentModelList() {
		return fulfillmentModelList;
	}

	public void setFulfillmentModelList(List<FulfillmentModel> fulfillmentModelList) {
		this.fulfillmentModelList = fulfillmentModelList;
	}

	public List<IssueCategory> getIssueCategoryList() {
		return issueCategoryList;
	}

	public void setIssueCategoryList(List<IssueCategory> issueCategoryList) {
		this.issueCategoryList = issueCategoryList;
	}

	public List<SubCategory> getSubCategoryList() {
		return subCategoryList;
	}

	public void setSubCategoryList(List<SubCategory> subCategoryList) {
		this.subCategoryList = subCategoryList;
	}
	
	public Boolean getRtc() {
		return rtc;
	}

	public void setRtc(Boolean rtc) {
		this.rtc = rtc;
	}

	public Boolean getFlag3pl() {
		return flag3pl;
	}

	public void setFlag3pl(Boolean flag3pl) {
		this.flag3pl = flag3pl;
	}

}
