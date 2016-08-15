package com.snapdeal.entity;


import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.security.core.context.SecurityContextHolder;

@MappedSuperclass
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class BaseWarehouseEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Warehouse warehouse;

	public Warehouse getWarehouse() {
		return warehouse;
	}
	
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	@PrePersist
	protected void onCreate()
	{
		User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		this.setCreated(new Date());
		this.setUpdated(new Date());
		this.setUpdatedBy(sessionUser);
		this.setCreatedBy(sessionUser);
		this.warehouse = sessionUser.getActiveWarehouse();
	}
	
	/*@PreUpdate
	public void onUpdate()
	{
		User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		this.setUpdated(new Date());
		this.setUpdatedBy(sessionUser);
		this.warehouse = sessionUser.getActiveWarehouse();
	}*/

}
