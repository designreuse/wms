package com.snapdeal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="graph_daily_number")
public class GraphDailyNumber {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="warehouse")
	private String warehouse;
	
	@Column(name="date")
	private String date;
	
	@Column(name="total_received")
	private Integer totalReceived;
	
	@Column(name="total_qc")
	private String totalQc;
	
	@Column(name="total_dispatch")
	private String totalDispatch;
	
	@Column(name="total_putaway")
	private String totalPutaway;
	
	@Column(name="total_gatepass")
	private String totalGatepass;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getTotalReceived() {
		return totalReceived;
	}

	public void setTotalReceived(Integer totalReceived) {
		this.totalReceived = totalReceived;
	}

	public String getTotalQc() {
		return totalQc;
	}

	public void setTotalQc(String totalQc) {
		this.totalQc = totalQc;
	}

	public String getTotalDispatch() {
		return totalDispatch;
	}

	public void setTotalDispatch(String totalDispatch) {
		this.totalDispatch = totalDispatch;
	}

	public String getTotalPutaway() {
		return totalPutaway;
	}

	public void setTotalPutaway(String totalPutaway) {
		this.totalPutaway = totalPutaway;
	}

	public String getTotalGatepass() {
		return totalGatepass;
	}

	public void setTotalGatepass(String totalGatepass) {
		this.totalGatepass = totalGatepass;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
