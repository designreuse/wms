package com.snapdeal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name="sub_category")
public class SubCategory extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="subcategory_name", length=200, nullable=false)
	private String name;

	@Column(name="subcategory_url", length=200, nullable=false)
	private String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	@PrePersist
	protected void onCreate() {
		User dummyUser = new User();
		dummyUser.setId(Long.parseLong("2"));
		this.setCreated(new Date());
		this.setUpdated(new Date());
		this.setCreatedBy(dummyUser);
		this.setUpdatedBy(dummyUser);
	}


}
