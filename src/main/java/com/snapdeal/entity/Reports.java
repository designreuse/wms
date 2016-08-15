package com.snapdeal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="reports")
public class Reports extends BaseEntity{
	
	/**
	 * Reports Entity
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="name",length=200,nullable=false)
	private String name;
	
	@Column(name="to_email")
	private String toEmail;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
}
