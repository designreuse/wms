package com.snapdeal.controller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snapdeal.entity.Reports;
import com.snapdeal.service.ReportsService;

@Controller
@RequestMapping("/Reports")
public class ReportsController {
	
	@Inject
	@Named("reportsService")
	ReportsService reportsService;

	@RequestMapping("/addEmail")
	public String addEmail(ModelMap map){
		List<String> reportsName = reportsService.getReportsName();
		map.put("reportsName", reportsName);
		return "Reports/addEmail";
	}
	
	/** This will return the email address corresponding to report from database.**/
	@RequestMapping("/getEmailAddress")
	public @ResponseBody String getEmailAddress(@RequestParam("name") String name, ModelMap map){		
		String toEmail = reportsService.getEmailAddress(name);
		return toEmail;
	}
	
	@RequestMapping("/saveEmailAddress")
	public String saveEmail(@ModelAttribute("reports") Reports reports, ModelMap map){
		reportsService.saveOrUpdate(reports);
		map.put("message", "Email Address saved succesfully for report : " + reports.getName());

		List<String> reportsName = reportsService.getReportsName();
		map.put("reportsName", reportsName);
		return "Reports/addEmail";
	}
	
	@RequestMapping("/addReportName")
	public String addReportName(){
		return "Reports/addReportName";
	}
	
	/** Check for the existing report name in the database**/
	@RequestMapping("/checkReportName")
	public @ResponseBody String checkReportName(@ModelAttribute("name") String name, ModelMap map){
		boolean result = reportsService.checkReportName(name);
		
		/** If exists then return false to the jquery validator else true.**/
		if(result)
			return "false";
		else
			return "true";
	}
	
	@RequestMapping("/saveName")
	public String saveName(@ModelAttribute("reports") Reports reports, ModelMap map){
		reportsService.saveOrUpdate(reports);
		map.put("message", "Name Added Successfully : " + reports.getName());
		return "Reports/addReportName";
	}
}
