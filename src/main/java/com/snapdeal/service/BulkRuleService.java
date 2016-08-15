package com.snapdeal.service;

import org.springframework.stereotype.Service;

import com.snapdeal.entity.BulkRule;

@Service
public interface BulkRuleService {
	
	public BulkRule getBulkRulebyGroupId(Long groupId);

}
