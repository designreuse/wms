package com.snapdeal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="graph_rpr_qc")
public class GraphRprQc {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="date")
	private String date;
	
	@Column(name="total_rpr")
	private String totalRpr;
	
	@Column(name="hrs_12")
	private String hrs12;
	
	@Column(name="hrs_24")
	private String hrs24;
	
	@Column(name="hrs_48")
	private String hrs48;
	
	@Column(name="hrs_72")
	private String hrs72;
	
	@Column(name="hrs_72plus")
	private String hrs72Plus;
	
	@Column(name="pending")
	private String pending;
	
	@Column(name="warehouse")
	private String warehouse;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTotalRpr() {
		return totalRpr;
	}

	public void setTotalRpr(String totalRpr) {
		this.totalRpr = totalRpr;
	}

	public String getHrs12() {
		return hrs12;
	}

	public void setHrs12(String hrs12) {
		this.hrs12 = hrs12;
	}

	public String getHrs24() {
		return hrs24;
	}

	public void setHrs24(String hrs24) {
		this.hrs24 = hrs24;
	}

	public String getHrs48() {
		return hrs48;
	}

	public void setHrs48(String hrs48) {
		this.hrs48 = hrs48;
	}

	public String getHrs72() {
		return hrs72;
	}

	public void setHrs72(String hrs72) {
		this.hrs72 = hrs72;
	}

	public String getHrs72Plus() {
		return hrs72Plus;
	}

	public void setHrs72Plus(String hrs72Plus) {
		this.hrs72Plus = hrs72Plus;
	}

	public String getPending() {
		return pending;
	}

	public void setPending(String pending) {
		this.pending = pending;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
