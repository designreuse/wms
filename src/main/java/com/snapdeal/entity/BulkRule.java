package com.snapdeal.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="bulk_upload_rules")
public class BulkRule extends BaseWarehouseEntity {

	/**
	 * 
	*/
	private static final long serialVersionUID = 1L;

	@Column(name="rule_name", length=100, nullable=false, unique=true)
	private String name;

	@Column(name="enabled")
	private Boolean enabled = false;
	
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private Group group;

	@Column(name="eligible_rtv")
	private Boolean rtv = false;
	
	@Column(name="eligible_rtc")
	private Boolean rtc = false;
	
	@Column(name="eligible_3pl")
	private Boolean flag3pl = false;
	
	@Column(name="eligible_liquidation")
	private Boolean liquidation = false;
	
	@Column(name="eligible_warehouse")
	private Boolean warehouseFlag = false;
	
	public Boolean getRtv() {
		return rtv;
	}

	public void setRtv(Boolean rtv) {
		this.rtv = rtv;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
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

	public Boolean getLiquidation() {
		return liquidation;
	}

	public void setLiquidation(Boolean liquidation) {
		this.liquidation = liquidation;
	}

	public void setWarehouseFlag(Boolean warehouseFlag) {
		this.warehouseFlag = warehouseFlag;
	}

	public Boolean getWarehouseFlag() {
		return warehouseFlag;
	}

}
