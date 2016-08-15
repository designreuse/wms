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
import org.springframework.web.bind.annotation.RequestParam;

import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.Address;
import com.snapdeal.entity.Centre3PL;
import com.snapdeal.entity.Warehouse;
import com.snapdeal.service.Centre3PLService;
import com.snapdeal.service.WarehouseService;

@Controller
@RequestMapping(value="/Centre3PL")
public class Centre3PLController {
		
	@Inject
	@Named("entityDao")
	EntityDao entityDao;
	
	@Inject
	@Named("warehouseService")
	WarehouseService warehouseService;
	
	@Inject
	@Named("centre3plService")
	Centre3PLService centre3plService; 
	
	
	@RequestMapping(value="/create")
	public String createLiquidation(@ModelAttribute("liquidation") Centre3PL centre3pl ,ModelMap map)
	{		
		List<Warehouse> warehouseList = warehouseService.getEnabledWarehouses();
		map.put("warehouse", warehouseList);
		map.put("centre3pl",new Centre3PL());
		return "Centre3PL/create";
	}
	
	@RequestMapping(value="/save")
	public String saveLiquidation(@ModelAttribute("centre3pl") Centre3PL centre3pl,
			@RequestParam(value="warehouseID",required=true) Long warehouseId,ModelMap map)
	{	
		if(centre3pl.getId() != null)
		{	
			Centre3PL persistedcentre3pl = centre3plService.findCentre3PLById(centre3pl.getId());
			
			persistedcentre3pl.setCode(centre3pl.getCode());
			persistedcentre3pl.setName(centre3pl.getName());
			Address persistedAddress = persistedcentre3pl.getAddress();
			persistedAddress.setAddressLine1(centre3pl.getAddress().getAddressLine1());
			persistedAddress.setAddressLine2(centre3pl.getAddress().getAddressLine2());
			persistedAddress.setCity(centre3pl.getAddress().getCity());
			persistedAddress.setState(centre3pl.getAddress().getState());
			persistedAddress.setPincode(centre3pl.getAddress().getPincode());
			persistedAddress.setContactNumber(centre3pl.getAddress().getContactNumber());
			persistedcentre3pl.setAddress(persistedAddress);
			
			centre3plService.saveOrUpdateCentre3PL(persistedcentre3pl,warehouseId);
		}
		else
		{
			centre3pl.setEnabled(true);
			centre3plService.saveOrUpdateCentre3PL(centre3pl,warehouseId);
		}
		List<Centre3PL> centre3plList = centre3plService.getAllCentre3PL();
		map.put("centre3pl", centre3plList);
		return "Centre3PL/view";
	}
	
	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public String editCentre3PL(@PathVariable("id") Long id,ModelMap map)
	{
		Centre3PL centre3pl = centre3plService.findCentre3PLById(id);
		
		map.put("centre3pl", centre3pl);
		map.put("edit", true);
		List<Warehouse> warehouseList = warehouseService.getEnabledWarehouses();
		map.put("warehouse", warehouseList);
		
		return "Centre3PL/create";
	}
	
	@RequestMapping(value="/view")
	public String viewAllCentre3PL(ModelMap map)
	{
		List<Centre3PL> centre3plList = centre3plService.getAllCentre3PL();
		map.put("centre3pl", centre3plList);
		return "Centre3PL/view";
	}
	
	@RequestMapping(value="/disable/{id}",method=RequestMethod.GET)
	public String disableCentre3PL(@PathVariable("id") Long id,ModelMap map)
	{
		centre3plService.disableCentre3PL(id);
		List<Centre3PL> centre3pl = centre3plService.getAllCentre3PL();
		map.put("centre3pl", centre3pl);
		map.put("message", "3PL center disabled successfully");
		return "Centre3PL/view";
	}

	@RequestMapping(value="/enable/{id}",method=RequestMethod.GET)
	public String enableWarehouse(@PathVariable("id") Long id,ModelMap map)
	{
		centre3plService.enableCentre3PL(id);
		List<Centre3PL> centre3pl = centre3plService.getAllCentre3PL();
		map.put("centre3pl", centre3pl);
		map.put("message", "3PL center enabled successfully");
		return "Centre3PL/view";
	}
}


