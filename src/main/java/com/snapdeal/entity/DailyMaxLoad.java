package com.snapdeal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "daily_max_load")
public class DailyMaxLoad extends BaseEntity{
	
	/**
	 * Daily MaxLoad Entity
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="courier_code",length=200,nullable=false)
	private String courierCode;
	
	@Column(name="max_load",nullable=false)
	private Integer maxLoad;
	
	@Column(name="enabled", nullable=false)
	private Boolean enabled;
	
    @ManyToOne
    @JoinColumn(name = "courier_code_FK", referencedColumnName = "code")
    private Courier courier;
    
	
	public Courier getCourier() {
		return courier;
	}

	public void setCourier(Courier courier) {
		this.courier = courier;
	}

	public String getCourierCode() {
		return courierCode;
	}

	public void setCourierCode(String courierCode) {
		this.courierCode = courierCode;
	}

	public Integer getMaxLoad() {
		return maxLoad;
	}

	public void setMaxLoad(Integer maxLoad) {
		this.maxLoad = maxLoad;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
