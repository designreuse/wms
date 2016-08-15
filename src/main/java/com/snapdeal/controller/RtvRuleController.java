package com.snapdeal.controller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.snapdeal.entity.RtvCheckRule;
import com.snapdeal.service.RuleService;
/** Manages Post RTV Rules **/
@Controller
@RequestMapping("/RtvRule")
public class RtvRuleController {

	@Inject
	@Named("ruleService")
	RuleService ruleService;

	@RequestMapping("/view")
	public String view(ModelMap map)
	{
		List<RtvCheckRule> checkRuleList = ruleService.getRtvRules();
		map.put("rules", checkRuleList);
		return "Rule/rtvRule";
	}

	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public String editRule(@PathVariable("id") Long id,ModelMap map)
	{
		RtvCheckRule rule = ruleService.findRtvCheckRuleById(id);
		map.put("rule", rule);
		return "Rule/rtvEdit";
	}

	@RequestMapping(value="/disable/{id}",method=RequestMethod.GET)
	public String disableRule(@PathVariable("id") Long id, ModelMap map)
	{
		ruleService.disableRtvRule(id);
		List<RtvCheckRule> checkRuleList = ruleService.getRtvRules();
		map.put("rules", checkRuleList);
		map.put("message", "Rule disabled successfully");
		return "Rule/rtvRule";
	}

	@RequestMapping(value="/enable/{id}",method=RequestMethod.GET)
	public String enableRule(@PathVariable("id") Long id,ModelMap map)
	{
		ruleService.enableRtvRule(id);
		List<RtvCheckRule> checkRuleList = ruleService.getRtvRules();
		map.put("rules", checkRuleList);
		map.put("message", "Rule enabled successfully");
		return "Rule/rtvRule";
	}

	@RequestMapping(value="/update")
	public String updateRule(@ModelAttribute("rule") RtvCheckRule rule,ModelMap map)
	{
		if(rule.getId() != null)
		{
			RtvCheckRule persistedRule = ruleService.findRtvCheckRuleById(rule.getId());
			persistedRule.setValue(rule.getValue());
			ruleService.saveOrUpdateRtvCheckRule(persistedRule);
			List<RtvCheckRule> checkRuleList = ruleService.getRtvRules();
			map.put("rules", checkRuleList);
			map.put("message", "Rule Updated Successfully.");
		}
		return "Rule/rtvRule";
	}
}
