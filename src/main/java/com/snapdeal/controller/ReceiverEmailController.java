package com.snapdeal.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snapdeal.entity.ReceiverEmail;
import com.snapdeal.service.ReceiverEmailService;

@Controller
@RequestMapping("/Receiver")
public class ReceiverEmailController {

	@Inject
	@Named("receiverEmailService")
	ReceiverEmailService receiverEmailService;
	
	@RequestMapping("/add")
	public String add(){
		return "Receiver/add";
	}
	
	/** Check for the existing report name in the database**/
	@RequestMapping("/checkReceiver")
	public @ResponseBody String checkReceiver(@ModelAttribute("code") String code, ModelMap map){
		boolean result = receiverEmailService.checkReceiever(code);
		
		/** If exists then return false to the JQUERY validator else true.**/
		if(result)
			return "false";
		else
			return "true";
	}

	@RequestMapping("/save")
	public String save(@ModelAttribute("receiverEmail") ReceiverEmail receiverEmail, ModelMap map){
		receiverEmailService.saveOrUpdate(receiverEmail);
		map.put("message", "Information added succesfully : " + receiverEmail.getCode());
		return "Receiver/add";
	}
	
	@RequestMapping("/search")
	public String search(){
		return "Receiver/search";
	}
	
	/** This will return the email address corresponding to receiver from database.**/
	@RequestMapping("/info")
	public String getEmailAddress(@RequestParam("code") String code, ModelMap map){		
		ReceiverEmail receiverEmail = receiverEmailService.getReceiver(code);
		
		if(receiverEmail == null)
			map.put("message", "Receiver not found");
		else{
			map.put("receiverEmail", receiverEmail);
		}
		return "Receiver/search";
	}
	
	@RequestMapping("/update")
	public String update(@ModelAttribute("receiverEmail") ReceiverEmail receiverEmail, ModelMap map){
		receiverEmailService.saveOrUpdate(receiverEmail);
		map.put("messageUpdate", "Information updated succesfully : " + receiverEmail.getCode());
		return "Receiver/search";
	}
	
}
