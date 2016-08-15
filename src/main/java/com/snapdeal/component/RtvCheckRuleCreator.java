package com.snapdeal.component;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Component;

import com.snapdeal.entity.RtvCheckRule;
import com.snapdeal.entity.Warehouse;
import com.snapdeal.service.RuleService;

@Component
@Named("rtvRules")
public class RtvCheckRuleCreator {

	@Inject
	@Named("ruleService")
	RuleService ruleService;
	
	public void createRtvRulesForNewWarehouse(Warehouse warehouse)
	{
		RtvCheckRule rtvCheckRule = new RtvCheckRule();
		rtvCheckRule.setName("Days Passed Manifest Rule");
		rtvCheckRule.setPriority(1);
		rtvCheckRule.setValue(40);
		rtvCheckRule.setEnabled(false);
		rtvCheckRule.setWarehouse(warehouse);
		ruleService.saveOrUpdateRtvCheckRule(rtvCheckRule);
		RtvCheckRule rtvCheckRule2 = new RtvCheckRule();
		rtvCheckRule2.setName("Existing Vendor Product");
		rtvCheckRule2.setPriority(2);
		rtvCheckRule2.setValue(-1);
		rtvCheckRule2.setEnabled(false);
		rtvCheckRule2.setWarehouse(warehouse);
		ruleService.saveOrUpdateRtvCheckRule(rtvCheckRule2);
		RtvCheckRule rtvCheckRule3 = new RtvCheckRule();
		rtvCheckRule3.setName("Seller Multiple Pickups");
		rtvCheckRule3.setPriority(3);
		rtvCheckRule3.setValue(3);
		rtvCheckRule3.setEnabled(false);
		rtvCheckRule3.setWarehouse(warehouse);
		ruleService.saveOrUpdateRtvCheckRule(rtvCheckRule3);
	}
}
