package com.snapdeal.controller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snapdeal.component.RtvCheckRuleCreator;
import com.snapdeal.component.SessionDetails;
import com.snapdeal.entity.Address;
import com.snapdeal.entity.User;
import com.snapdeal.entity.Warehouse;
import com.snapdeal.service.UserAuthenticationService;
import com.snapdeal.service.UserService;
import com.snapdeal.service.WarehouseService;

@Controller
@RequestMapping("/Warehouse")
public class WarehouseController {

	@Inject
	@Named("warehouseService")
	WarehouseService warehouseService;

	@Inject
	@Named("sessionDetails")
	SessionDetails sessionDetails;

	@Inject
	@Named("userService")
	UserService userService;

	@Inject
	@Named("rtvRules")
	RtvCheckRuleCreator rtvCheckRuleCreator;

	@Autowired
	UserAuthenticationService userAuthenticationService;

	@RequestMapping("/create")
	public String createWarehouse(ModelMap map)
	{
		Warehouse warehouse = new Warehouse();
		map.put("warehouse", warehouse);
		return "Warehouse/create";
	}

	@RequestMapping("/view")
	public String viewAllWarehouse(ModelMap map)
	{
		List<Warehouse> warehouses = warehouseService.getAllWarehouse();
		map.put("warehouses", warehouses);
		return "Warehouse/view";
	}

	@RequestMapping("/save")
	public String saveWarehouse(@ModelAttribute("warehouse") Warehouse warehouse, ModelMap map)
	{
		if(warehouse.getId() != null)
		{
			Warehouse persistedWarehouse = warehouseService.findWarehouseByid(warehouse.getId());
			persistedWarehouse.setName(warehouse.getName());
			persistedWarehouse.setCode(warehouse.getCode());
			Address persistedAddress = persistedWarehouse.getAddress();
			persistedAddress.setAddressLine1(warehouse.getAddress().getAddressLine1());
			persistedAddress.setAddressLine2(warehouse.getAddress().getAddressLine2());
			persistedAddress.setCity(warehouse.getAddress().getCity());
			persistedAddress.setState(warehouse.getAddress().getState());
			persistedAddress.setPincode(warehouse.getAddress().getPincode());
			persistedAddress.setContactNumber(warehouse.getAddress().getContactNumber());
			persistedWarehouse.setAddress(persistedAddress);
			warehouseService.saveOrUpdateWarehouse(persistedWarehouse);
		}
		else {
			warehouse.setEnabled(true);
			warehouse.setId(warehouseService.saveOrUpdateWarehouse(warehouse));
			rtvCheckRuleCreator.createRtvRulesForNewWarehouse(warehouse);
		}
		map.put("message", "Warehouse saved successfully");
		List<Warehouse> warehouses = warehouseService.getAllWarehouse();
		map.put("warehouses", warehouses);
		return "Warehouse/view";
	}

	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public String editWarehouse(@PathVariable("id") Long id,ModelMap map)
	{
		Warehouse warehouse = warehouseService.findWarehouseByid(id);
		map.put("warehouse", warehouse);
		map.put("edit", true);
		return "Warehouse/create";
	}

	@RequestMapping(value="/disable/{id}",method=RequestMethod.GET)
	public String disableWarehouse(@PathVariable("id") Long id,ModelMap map)
	{
		warehouseService.disableWarehouse(id);
		List<Warehouse> warehouses = warehouseService.getAllWarehouse();
		map.put("warehouses", warehouses);
		map.put("message", "Warehouse disabled successfully");
		return "Warehouse/view";
	}

	@RequestMapping(value="/enable/{id}",method=RequestMethod.GET)
	public String enableWarehouse(@PathVariable("id") Long id,ModelMap map)
	{
		warehouseService.enableWarehouse(id);
		List<Warehouse> warehouses = warehouseService.getAllWarehouse();
		map.put("warehouses", warehouses);
		map.put("message", "Warehouse enabled successfully");
		return "Warehouse/view";
	}

	@RequestMapping(value="/change/{id}",method=RequestMethod.GET)
	public String changeWarehouse(@PathVariable("id") Long id,ModelMap map)
	{
		User sessionUser = sessionDetails.getSessionUser();
		Warehouse activeWarehouse = warehouseService.findWarehouseByid(id);
		sessionUser.setActiveWarehouse(activeWarehouse);
		userService.saveOrUpdateUser(sessionUser);
		userAuthenticationService.loadUserByUsername(sessionUser.getUsername());
		map.put("message", "Warehouse Changed Successfully.");
		return "redirect:/home";
	}

	@RequestMapping("/checkName")
	public @ResponseBody String checkName(@ModelAttribute("name") String warehouseName)
	{
		boolean result = warehouseService.checkName(warehouseName);
		if(result)
		{
			return "success";
		}
		else{
			return "failure";
		}
	}

	@RequestMapping("/checkCode")
	public @ResponseBody String checkCode(@ModelAttribute("code") String warehouseCode)
	{
		boolean result = warehouseService.checkCode(warehouseCode);
		if(result)
		{
			return "success";
		}
		else{
			return "failure";
		}
	}
	
}
