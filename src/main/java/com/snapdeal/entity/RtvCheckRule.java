package com.snapdeal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.security.core.context.SecurityContextHolder;

@Entity
@Table(name="rtv_rules")
public class RtvCheckRule extends BaseWarehouseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="rule_name",nullable=false)
	private String name;

	@Column(name="enabled")
	private Boolean enabled;

	/*@Column(name="manifest_days")
	private Boolean daysPassedManifest;*/

	@Column(nullable=false)
	private Integer priority;

	@Column(name="rule_value",nullable=false)
	private Integer value;

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
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

	/*public Boolean getDaysPassedManifest() {
		return daysPassedManifest;
	}

	public void setDaysPassedManifest(Boolean daysPassedManifest) {
		this.daysPassedManifest = daysPassedManifest;
	}*/

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@PrePersist
	protected void onCreate()
	{
		User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		this.setCreated(new Date());
		this.setUpdated(new Date());
		this.setUpdatedBy(sessionUser);
		this.setCreatedBy(sessionUser);
	}

	@PreUpdate
	public void onUpdate()
	{
		User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		this.setUpdated(new Date());
		this.setUpdatedBy(sessionUser);
	}
}
