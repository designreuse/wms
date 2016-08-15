package com.snapdeal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="graph_total_dispatch")
public class GraphTotalDispatch {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="ware_id")
	private String wareId;
	
	@Column(name="warehouse")
	private String warehouse;
	
	@Column(name="date")
	private String date;
	
	@Column(name="delhivery")
	private Integer delhivery;
	
	@Column(name="dotzot")
	private String dotzot;
	
	@Column(name="red_express")
	private String redExpress;
	
	@Column(name="go_javas")
	private String goJavas;
	
	@Column(name="gati")
	private String gati;
	
	@Column(name="others")
	private String others;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWareId() {
		return wareId;
	}

	public void setWareId(String wareId) {
		this.wareId = wareId;
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

	public Integer getDelhivery() {
		return delhivery;
	}

	public void setDelhivery(Integer delhivery) {
		this.delhivery = delhivery;
	}

	public String getDotzot() {
		return dotzot;
	}

	public void setDotzot(String dotzot) {
		this.dotzot = dotzot;
	}

	public String getRedExpress() {
		return redExpress;
	}

	public void setRedExpress(String redExpress) {
		this.redExpress = redExpress;
	}

	public String getGoJavas() {
		return goJavas;
	}

	public void setGoJavas(String goJavas) {
		this.goJavas = goJavas;
	}

	public String getGati() {
		return gati;
	}

	public void setGati(String gati) {
		this.gati = gati;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
	
}
