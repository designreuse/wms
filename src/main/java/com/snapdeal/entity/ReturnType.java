package com.snapdeal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="return_type")
public class ReturnType extends BaseEntity{
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="code",length=50)
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
