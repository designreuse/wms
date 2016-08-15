package com.snapdeal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author snehil
 *
 */
@Entity
@Table(name="graph_pickup_pending")
public class GraphPickupPending{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="date")
	private String date;
	
	@Column(name="total_pickup")
	private Integer totalPickup;
	
	@Column(name="nuvo")
	private Integer nuvo;
	
	@Column(name="jv_exp")
	private Integer jvExp;
	
	@Column(name="delhivery")
	private Integer delhivery;
	
	@Column(name="bd")
	private Integer bd;
	
	@Column(name="others")
	private Integer others;
	
	
	
	@Column(name="warehouse")
	private String warehouse;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getTotalPickup() {
		return totalPickup;
	}

	public void setTotalPickup(Integer totalPickup) {
		this.totalPickup = totalPickup;
	}

	public Integer getNuvo() {
		return nuvo;
	}

	public void setNuvo(Integer nuvo) {
		this.nuvo = nuvo;
	}

	public Integer getJvExp() {
		return jvExp;
	}

	public void setJvExp(Integer jvExp) {
		this.jvExp = jvExp;
	}

	public Integer getDelhivery() {
		return delhivery;
	}

	public void setDelhivery(Integer delhivery) {
		this.delhivery = delhivery;
	}

	public Integer getBd() {
		return bd;
	}

	public void setBd(Integer bd) {
		this.bd = bd;
	}

	public Integer getOthers() {
		return others;
	}

	public void setOthers(Integer others) {
		this.others = others;
	}


	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	
	
	
	
}
