package com.snapdeal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "bulk_upload", uniqueConstraints = @UniqueConstraint(columnNames={"suborder_code", "warehouse_id"}))
public class BulkUploadData extends BaseWarehouseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="suborder_code",length=100,nullable=false)
	private String suborder;

	@Column(name="rule_id",nullable=false)
	private Long ruleId;

	public String getSuborder() {
		return suborder;
	}

	public void setSuborder(String suborder) {
		this.suborder = suborder;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

}
