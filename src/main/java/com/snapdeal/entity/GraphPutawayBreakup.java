package com.snapdeal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="graph_putaway_breakup")
public class GraphPutawayBreakup {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="ware_id")
	private String wareId;
	
	@Column(name="ware_code")
	private String wareCode;
	
	@Column(name="date")
	private String date;
	
	@Column(name="putaway")
	private Integer putaway;
	
	@Column(name="days_hold_42")
	private String daysHold;
	
	@Column(name="soi_non_faulty")
	private String soiNonFaulty;
	
	@Column(name="courier_debit")
	private String courierDebit;
	
	@Column(name="rtv_eligible")
	private String rtvEligible;
	
	@Column(name="3pl")
	private String pl;
	
	@Column(name="qc_rejected")
	private String qcRejected;

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

	public String getWareCode() {
		return wareCode;
	}

	public void setWareCode(String wareCode) {
		this.wareCode = wareCode;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getPutaway() {
		return putaway;
	}

	public void setPutaway(Integer putaway) {
		this.putaway = putaway;
	}

	public String getDaysHold() {
		return daysHold;
	}

	public void setDaysHold(String daysHold) {
		this.daysHold = daysHold;
	}

	public String getSoiNonFaulty() {
		return soiNonFaulty;
	}

	public void setSoiNonFaulty(String soiNonFaulty) {
		this.soiNonFaulty = soiNonFaulty;
	}

	public String getCourierDebit() {
		return courierDebit;
	}

	public void setCourierDebit(String courierDebit) {
		this.courierDebit = courierDebit;
	}

	public String getRtvEligible() {
		return rtvEligible;
	}

	public void setRtvEligible(String rtvEligible) {
		this.rtvEligible = rtvEligible;
	}

	public String getPl() {
		return pl;
	}

	public void setPl(String pl) {
		this.pl = pl;
	}

	public String getQcRejected() {
		return qcRejected;
	}

	public void setQcRejected(String qcRejected) {
		this.qcRejected = qcRejected;
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
