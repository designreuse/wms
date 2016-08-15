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

import com.snapdeal.entity.Property;
import com.snapdeal.service.PropertyService;

@Controller
@RequestMapping("/Property")
public class PropertyController {
	
	@Inject
	@Named("propertyService")
	PropertyService propertyService;
	
	@RequestMapping("/addProperty")
	public String addProperty(){
		return "Property/addProperty";
	}
	
	/** Check for the existing report name in the database**/
	@RequestMapping("/checkPropertyName")
	public @ResponseBody String checkPropertyName(@ModelAttribute("name") String name, ModelMap map){
		boolean result = propertyService.checkPropertyName(name);
		
		/** If exists then return false to the jquery validator else true.**/
		if(result)
			return "false";
		else
			return "true";
	}
	
	@RequestMapping("/save")
	public String saveName(@ModelAttribute("property") Property property, ModelMap map){
		propertyService.saveOrUpdate(property);
		map.put("message", "Property Added Successfully : " + property.getName());
		return "Property/addProperty";
	}
	
	@RequestMapping("/editProperty")
	public String editProperty(ModelMap map){
		List<String> propertyName = propertyService.getPropertyName();
		map.put("propertyName", propertyName);
		return "Property/editProperty";
	}
	
	/** This will return the value corresponding to the property from database.**/
	@RequestMapping("/getValue")
	public @ResponseBody String getValue(@RequestParam("name") String name, ModelMap map){		
		String value = propertyService.getValue(name);
		return value;
	}
	
	@RequestMapping("/updateProperty")
	public String updateProperty(@ModelAttribute("property") Property property, ModelMap map){
		
		propertyService.saveOrUpdate(property);		
		map.put("message", "Property Updated Successfully : " + property.getName());		
		
		List<String> propertyName = propertyService.getPropertyName();
		map.put("propertyName", propertyName);
		return "Property/editProperty";
	}
}
