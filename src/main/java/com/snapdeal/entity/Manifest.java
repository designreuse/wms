package com.snapdeal.entity;   

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="manifest")
public class Manifest extends BaseWarehouseEntity {

	private static final long serialVersionUID = 1L; 
	
	@Column(name="courier_code",length=200,nullable=false)
	private String courierCode; 
	
	@OneToMany(fetch = FetchType.LAZY)
	private List<RtvSheet> rtvSheet;
	
	@Column(name="is_email_sent")
	private Boolean email_sent;

	public String getCourierCode() {
		return courierCode;
	}

	public void setCourierCode(String courierCode) {
		this.courierCode = courierCode;
	}

	public List<RtvSheet> getRtvSheet() {
		return rtvSheet;
	}

	public void setRtvSheet(List<RtvSheet> rtvSheet) {
		this.rtvSheet = rtvSheet;
	}

	public Boolean getEmail_sent() {
		return email_sent;
	}

	public void setEmail_sent(Boolean email_sent) {
		this.email_sent = email_sent;
	}
	
}
