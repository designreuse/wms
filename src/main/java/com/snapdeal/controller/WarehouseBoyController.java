package com.snapdeal.controller;

import java.util.ArrayList;
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

import com.snapdeal.bean.WarehouseBoyDetail;
import com.snapdeal.entity.WarehouseBoy;
import com.snapdeal.service.GenericService;
import com.snapdeal.service.WarehouseService;

@Controller
@RequestMapping("/WarehouseBoy")
public class WarehouseBoyController {

	@Inject
	@Named("warehouseService")
	WarehouseService warehouseService;

	@Inject
	@Named("genericService")
	GenericService genericService;

	@RequestMapping("/create")
	public String create(ModelMap map)
	{
		map.put("warehouseBoy", new WarehouseBoy());
		return "WarehouseBoy/create";
	}

	@RequestMapping("/view")
	public String getAllWarehouseBoy(ModelMap map)
	{
		List<WarehouseBoy> warehouseBoyList = warehouseService.getAllWarehouseBoy();
		map.put("boys", warehouseBoyList);
		return "WarehouseBoy/view";
	}

	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public String edit(@PathVariable("id") Long id,ModelMap map)
	{
		map.put("warehouseBoy", warehouseService.findWarehouseBoyById(id));
		return "WarehouseBoy/create";
	}

	@RequestMapping(value="/remove/{id}",method=RequestMethod.GET)
	public String remove(@PathVariable("id") Long id,ModelMap map)
	{
		warehouseService.removeWarehouseBoy(id);
		map.put("message", "Warehouse Boy Removed Successfully");
		List<WarehouseBoy> warehouseBoyList = warehouseService.getAllWarehouseBoy();
		map.put("boys", warehouseBoyList);
		return "WarehouseBoy/view";
	}

	@RequestMapping("/save")
	public String saveOrUpdate(@ModelAttribute("warehouseBoy") WarehouseBoy warehouseBoy,ModelMap map)
	{
		if(warehouseBoy.getId() != null)
		{
			WarehouseBoy persistedBoy = warehouseService.findWarehouseBoyById(warehouseBoy.getId());
			persistedBoy.setContactNumber(warehouseBoy.getContactNumber());
			persistedBoy.setName(warehouseBoy.getName());
			warehouseService.saveOrupdateWarehouseBoy(persistedBoy);
		}
		else {
			warehouseService.saveOrupdateWarehouseBoy(warehouseBoy);
		}
		map.put("message", "Data Saved Successfully");
		List<WarehouseBoy> warehouseBoyList = warehouseService.getAllWarehouseBoy();
		map.put("boys", warehouseBoyList);
		return "WarehouseBoy/view";
	}

	@RequestMapping("/checkName")
	public @ResponseBody String checkName(@RequestParam("name") String name)
	{
		boolean result = genericService.checkName(WarehouseBoy.class, name);
		if(result)
		{
			return "success";
		}
		else{
			return "failure";
		}
	}

	@RequestMapping(value="/getBoyList",method=RequestMethod.GET)
	public @ResponseBody List<WarehouseBoyDetail> getWarehouseBoyList()
	{
		List<WarehouseBoy> warehouseBoys = warehouseService.getAllWarehouseBoy();
		List<WarehouseBoyDetail> warehouseBoyDetails = null;
		if(warehouseBoys != null && warehouseBoys.size() > 0)
		{
			warehouseBoyDetails = new ArrayList<WarehouseBoyDetail>();
			for(WarehouseBoy warehouseBoy : warehouseBoys)
			{
				WarehouseBoyDetail warehouseBoyDetail = new WarehouseBoyDetail();
				warehouseBoyDetail.setId(warehouseBoy.getId());
				warehouseBoyDetail.setName(warehouseBoy.getName());
				warehouseBoyDetails.add(warehouseBoyDetail);
			}
		}
		return warehouseBoyDetails;
	}

}
