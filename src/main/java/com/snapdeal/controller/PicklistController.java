package com.snapdeal.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snapdeal.bean.SellerDetails;
import com.snapdeal.bean.WarehouseSearch;
import com.snapdeal.component.Constants;
import com.snapdeal.entity.Inventory;
import com.snapdeal.entity.WarehouseBoy;
import com.snapdeal.entity.WarehouseList;
import com.snapdeal.service.HistoryService;
import com.snapdeal.service.PicklistService;
import com.snapdeal.service.PutawayService;
import com.snapdeal.service.WarehouseService;

/** Includes Picklist related operations **/
@Controller
@RequestMapping("/Picklist")
public class PicklistController {

	@Inject
	@Named("picklistService")
	PicklistService picklistService;

	@Inject
	@Named("putawayService")
	PutawayService putawayService;

	@Inject
	@Named("warehouseService")
	WarehouseService warehouseService;

	@Inject
	@Named("historyService")
	HistoryService historyService;

	/** Redirects to home page for generating picklist **/
	@RequestMapping("/home")
	public String home(ModelMap map)
	{
		map.put("groups", picklistService.getGroups());
		return "Picklist/home";
	}

	/** Get Group Names for RTV or Warehouse return cases available in Inventory**/
	@RequestMapping("/getGroups") 
	public @ResponseBody List<String> getGroups(@RequestParam("choice") String choice)
	{
		if(choice != null)
		{
			List<String> inventoryGroups = picklistService.getGroups();
			List<String> ruleGroups = null;
			
			if(choice.equalsIgnoreCase("rtv"))
			{
				ruleGroups = picklistService.getRtvEnabledGroups();
			}
			else if(choice.equalsIgnoreCase("rtc"))  
			{
				ruleGroups = picklistService.getRtcEnabledGroups();
			}
			else if(choice.equalsIgnoreCase("liq"))
			{
				ruleGroups = picklistService.getLiquidationEnabledGroups();
			}
			else if(choice.equalsIgnoreCase("3pl"))
			{
				ruleGroups = picklistService.get3PLEnabledGroups();
			}
			else
			{
				ruleGroups = picklistService.getWarehouseEnabledGroups();				
			}
			
			List<String> finalGroups = new ArrayList<String>();
			for(String group : ruleGroups)
			{
				if(inventoryGroups.contains(group))
				{
					finalGroups.add(group);
				}
			}
			return finalGroups;
		}
		return null;
	}

	/**  Gets seller list of sellers whose products are available in inventory of a particular group **/
	@RequestMapping("/getSellerList")
	public @ResponseBody List<SellerDetails> getSellerList(@RequestParam("name") String groupName)
	{
		List<SellerDetails> sellerDetails = picklistService.getSellers(groupName);
		return sellerDetails;
	}

	/** Searches for product in inventory for given group name and seller codes **/
	@RequestMapping("/search")
	public String search(@RequestParam("groupName") String groupName,
			@RequestParam(value="sellerCode[]",required=false) String[] sellerCode,ModelMap map)
	{
		List<Inventory> searchList = null;
		if(sellerCode != null && sellerCode.length <= 5)
		{
			List<String> vendorCode = Arrays.asList(sellerCode);
			searchList = picklistService.findInventoryByGroupAndSeller(groupName, vendorCode);

		}
		else {
			searchList = picklistService.findInventoryByGroup(groupName);
		}
		if(searchList != null && searchList.size() > 0)
		{
			List<Inventory> updatedList = new ArrayList<Inventory>();
			for(Inventory inventory : searchList)
			{
				inventory.setStatus(Constants.PICK_LIST_GENERATED);
				putawayService.saveOrUpdateInventory(inventory);
				updatedList.add(inventory);
			}
			WarehouseList pickList = new WarehouseList();
			pickList.setInventoryList(updatedList);
			pickList.setType(Constants.PICK_LIST);
			map.put("picklist", picklistService.saveOrUpdatePicklist(pickList));	
		}
		else {
			map.put("message", "Invalid Details Passed.");
		}
		map.put("groups", picklistService.getGroups());
		return "Picklist/home";
	}

	/** Prints the picklist with details of warehouse boy **/
	@RequestMapping("/printList")
	public String printList(@RequestParam("listId") Long listId,@RequestParam("warehouseBoy") Long boyId,
			ModelMap map)
	{
		WarehouseList picklist = picklistService.findPicklistById(listId);
		WarehouseBoy warehouseBoy = warehouseService.findWarehouseBoyById(boyId);
		if(picklist != null && picklist.getWarehouseBoy() == null)
		{
			picklist.setWarehouseBoy(warehouseBoy);
			List<Inventory> newList = new ArrayList<Inventory>();
			for(Inventory inventory : picklist.getInventoryList())
			{
				inventory.setStatus(Constants.PICK_LIST_PRINTED);
				newList.add(inventory);
			}
			picklist.setInventoryList(newList);
			picklistService.saveOrUpdatePicklist(picklist);
			historyService.addInventoryHistoryByList(newList, Constants.HISTORY_ACTION_PICKLIST);	
		}
		Collections.sort(picklist.getInventoryList());
		map.put("picklist", picklist);
		map.put("date",new Date());
		return "Picklist/print";
	}

	/** Redirects to page to allow searching of picklist between two dates **/
	@RequestMapping("/searchPicklist")
	public String searchPicklist(ModelMap map)
	{
		map.put("search",new WarehouseSearch());
		return "Picklist/search";
	}

	/** Searches Picklist for given date range specified by user **/
	@RequestMapping("/searchList")
	public String getSearchResults(@ModelAttribute("search") WarehouseSearch picklistSearch,ModelMap map)
	{
		List<WarehouseList> pickLists = picklistService.searchPickList(picklistSearch);
		if(pickLists != null && pickLists.size() > 0)
		{
			map.put("list", pickLists);	
		}
		else {
			map.put("message", "No lists found for given date range.");
		}
		map.put("search",new WarehouseSearch());
		return "Picklist/search";
	}
}
