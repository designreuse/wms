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
import org.springframework.web.bind.annotation.ResponseBody;

import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.Address;
import com.snapdeal.entity.Liquidation;
import com.snapdeal.service.LiquidationService;
import com.snapdeal.service.WarehouseService;
import com.snapdeal.entity.Warehouse;

@Controller
@RequestMapping(value="/Liquidation")
public class LiquidationController {
	
	@Inject
	@Named("entityDao")
	EntityDao entityDao;
	
	@Inject
	@Named("warehouseService")
	WarehouseService warehouseService;
	
	@Inject
	@Named("liquidationService")
	LiquidationService liquidationService; 
	
	
	@RequestMapping(value="/create")
	public String createLiquidation(@ModelAttribute("liquidation") Liquidation liquidation ,ModelMap map)
	{		
		List<Warehouse> warehouseList = warehouseService.getEnabledWarehouses();
		map.put("warehouse", warehouseList);
		map.put("liquidation",new Liquidation());
		return "Liquidation/create";
	}
	
	@RequestMapping(value="/save")
	public String saveLiquidation(@ModelAttribute("liquidation") Liquidation liquidation,
			@RequestParam(value="warehouseID",required=true) Long warehouseId,ModelMap map)
	{	
		if(liquidation.getId() != null)
		{	
			Liquidation persistedLiquidation = liquidationService.findLiquidationById(liquidation.getId());
			persistedLiquidation.setCode(liquidation.getCode());
			persistedLiquidation.setName(liquidation.getName());
			
			Address persistedAddress = entityDao.findById(Address.class, persistedLiquidation.getAddress().getId());
			persistedAddress.setAddressLine1(liquidation.getAddress().getAddressLine1());
			persistedAddress.setAddressLine2(liquidation.getAddress().getAddressLine2());
			persistedAddress.setCity(liquidation.getAddress().getCity());
			persistedAddress.setState(liquidation.getAddress().getState());
			persistedAddress.setPincode(liquidation.getAddress().getPincode());
			persistedAddress.setContactNumber(liquidation.getAddress().getContactNumber());
			
			persistedLiquidation.setAddress(persistedAddress);
			
			liquidationService.saveOrUpdateLiquidation(persistedLiquidation,warehouseId);
		}
		else
		{
			liquidation.setEnabled(true);
			liquidationService.saveOrUpdateLiquidation(liquidation,warehouseId);
		}
		List<Liquidation> liquidationList = liquidationService.getAllLiquidation();
		map.put("liquidation", liquidationList);
		return "Liquidation/view";
	}
	
	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public String editLiquidation(@PathVariable("id") Long id,ModelMap map)
	{
		Liquidation liquidation = liquidationService.findLiquidationById(id);
		
		map.put("liquidation", liquidation);
		map.put("edit", true);
		List<Warehouse> warehouseList = warehouseService.getEnabledWarehouses();
		map.put("warehouse", warehouseList);
		
		return "Liquidation/create";
	}
	
	@RequestMapping(value="/view")
	public String viewAllLiquidation(ModelMap map)
	{
		List<Liquidation> liquidationList = liquidationService.getAllLiquidation();
		map.put("liquidation", liquidationList);
		return "Liquidation/view";
	}
	
	@RequestMapping(value="/disable/{id}",method=RequestMethod.GET)
	public String disableLiquidation(@PathVariable("id") Long id,ModelMap map)
	{
		liquidationService.disableLiquidation(id);
		List<Liquidation> liquidation = liquidationService.getAllLiquidation();
		map.put("liquidation", liquidation);
		map.put("message", "Liquidation center disabled successfully");
		return "Liquidation/view";
	}

	@RequestMapping(value="/enable/{id}",method=RequestMethod.GET)
	public String enableWarehouse(@PathVariable("id") Long id,ModelMap map)
	{
		liquidationService.enableLiquidation(id);
		List<Liquidation> liquidation = liquidationService.getAllLiquidation();
		map.put("liquidation", liquidation);
		map.put("message", "Liquidation center enabled successfully");
		return "Liquidation/view";
	}
	
	@RequestMapping("/checkName")
	public @ResponseBody String checkName(@ModelAttribute("name") String liquidationName)
	{
		boolean result = liquidationService.checkName(liquidationName);
		if(result)
		{
			return "success";
		}
		else{
			return "failure";
		}
	}

	@RequestMapping("/checkCode")
	public @ResponseBody String checkCode(@ModelAttribute("code") String liquidationCode)
	{
		boolean result = liquidationService.checkCode(liquidationCode);
		if(result)
		{
			return "success";
		}
		else{
			return "failure";
		}
	}
	
	
}

