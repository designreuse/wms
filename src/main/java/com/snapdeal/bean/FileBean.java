package com.snapdeal.bean;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class FileBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MultipartFile postedFile;
	private Long ruleId;

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public MultipartFile getPostedFile() {
		return postedFile;
	}

	public void setPostedFile(MultipartFile postedFile) {
		this.postedFile = postedFile;
	}
}
