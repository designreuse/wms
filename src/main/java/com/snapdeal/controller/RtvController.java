package com.snapdeal.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snapdeal.bean.AWBResult;
import com.snapdeal.bean.ManualRtvData;
import com.snapdeal.bean.Rtv;
import com.snapdeal.bean.RtvData;
import com.snapdeal.bean.RtvDispatch;
import com.snapdeal.bean.RtvGroups;
import com.snapdeal.bean.WarehouseSearch;
import com.snapdeal.component.ClosedFC;
import com.snapdeal.component.Constants;
import com.snapdeal.component.SessionDetails;
import com.snapdeal.dao.Dao;
import com.snapdeal.entity.Address;
import com.snapdeal.entity.Inventory;
import com.snapdeal.entity.ReceiverDetail;
import com.snapdeal.entity.RtvSheet;
import com.snapdeal.entity.Rule;
import com.snapdeal.entity.WarehouseList;
import com.snapdeal.service.CourierService;
import com.snapdeal.service.GroupService;
import com.snapdeal.service.HistoryService;
import com.snapdeal.service.PicklistService;
import com.snapdeal.service.PropertyService;
import com.snapdeal.service.PutawayService;
import com.snapdeal.service.RtvService;
import com.snapdeal.service.RuleService;
import com.snapdeal.service.ShippingService;

@Controller
@RequestMapping("/Rtv")
public class RtvController {

	public static final Integer GROUP_SIZE = 4;

	@Autowired
	Dao dao;

	@Inject
	@Named("sessionDetails")
	SessionDetails sessionDetails;

	@Inject
	@Named("rtvService")
	RtvService rtvService;

	@Inject
	@Named("courierService")
	CourierService courierService;
	
	@Inject
	@Named("shippingService")
	ShippingService shippingService;

	@Inject
	@Named("ruleService")
	RuleService ruleService;

	@Inject
	@Named("putawayService")
	PutawayService putawayService;

	@Inject
	@Named("picklistService")
	PicklistService picklistService;

	@Inject
	@Named("groupService")
	GroupService groupService;
	
	@Inject
	@Named("propertyService")
	PropertyService propertyService;

	@Inject
	@Named("historyService")
	HistoryService historyService;
	
	@Inject
	@Named("closedFC")
	ClosedFC closedFC;


	@RequestMapping("/home")
	public String showHome()
	{
		return "Rtv/home";
	}

	@RequestMapping("/homeWarehouse")
	public String showManualWarehouseHome(ModelMap map)
	{
		ManualRtvData manualRtvData = new ManualRtvData();
		manualRtvData.setReturnType("warehouse");
		map.put("data", manualRtvData);
		return "Rtv/homeManualReturn";
	}

	@RequestMapping("/homeVendor")
	public String showManualVendorHome(ModelMap map)
	{
		ManualRtvData manualRtvData = new ManualRtvData();
		manualRtvData.setReturnType("vendor");
		map.put("data", manualRtvData);
		return "Rtv/homeManualReturn";
	}
	
	@RequestMapping("/home3PL")
	public String showManual3plHome(ModelMap map)
	{
		ManualRtvData manualRtvData = new ManualRtvData();
		manualRtvData.setReturnType("3pl");
		map.put("data", manualRtvData);
		return "Rtv/homeManualReturn";
	}
	@RequestMapping("/homeLiquidation")
	public String showManualLiquidationHome(ModelMap map)
	{
		ManualRtvData manualRtvData = new ManualRtvData();
		manualRtvData.setReturnType("liquidation");
		map.put("data", manualRtvData);
		return "Rtv/homeManualReturn";
	}
	@RequestMapping("/homeCustomer")
	public String showManualCustomerHome(ModelMap map)
	{
		ManualRtvData manualRtvData = new ManualRtvData();
		manualRtvData.setReturnType("customer");
		map.put("data", manualRtvData);
		return "Rtv/homeManualReturn";
	}

	@RequestMapping("/searchSheet")
	public String search(ModelMap map)
	{
		map.put("search",new WarehouseSearch());
		return "Rtv/search";
	}

	@RequestMapping("/search")
	public String search(@ModelAttribute("search") WarehouseSearch warehouseSearch,ModelMap map)
	{
		List<RtvSheet> rtvSheets = rtvService.searchRtvSheetBetweenDates(warehouseSearch);
		if(rtvSheets != null && rtvSheets.size() > 0)
		{
			map.put("list", rtvSheets);	
		}
		else {
			map.put("message", "No sheets found for given date range.");
		}
		map.put("search",new WarehouseSearch());
		return "Rtv/search";
	}
	
	@RequestMapping("/searchByCRI")
	public String searchByCRI(@RequestParam(value="barcode", required=false) String barcode, ModelMap map)
	{
		if(barcode != null && !barcode.isEmpty()){
			List<RtvSheet> rtvSheets = rtvService.searchRtvSheetByBarcode(barcode);
			if(rtvSheets != null && rtvSheets.size() > 0) {
				map.put("list", rtvSheets);	
			}
			else {
				map.put("message", "No sheets found for the given CRI code.");
			}
		}
		return "Rtv/searchByCRI";
	}

	@RequestMapping("/displaySheet/{id}")
	public String displayRtvSheet(@PathVariable("id") Long id,ModelMap map)
	{
		RtvSheet rtvSheet = rtvService.findRtvSheetById(id);
		if(rtvSheet != null)
		{
			if(rtvSheet.getReceiverDetail() == null)
				rtvSheet.setReceiverDetail(dao.getReceiverDetails(rtvSheet.getProductDetails().get(0).getSuborderCode(),rtvSheet.getVendorCode(),rtvSheet.getReturnType()));
			
			List<RtvSheet> rtvSheets = new ArrayList<RtvSheet>();
			rtvSheets.add(rtvSheet);
			map.put("rtvSheets", rtvSheets);
		}
		map.put("rtvDispatch", new RtvDispatch());
		return "Rtv/dispatch";
	}

	@RequestMapping("/checkInventory")
	public @ResponseBody String checkInventory(@ModelAttribute("barcode") String barcode, @ModelAttribute("returnType") String returnType){
		
		Inventory inventory = rtvService.getInventoryForReturn(barcode);			
		String msg = "";	
		
		if(inventory != null){
			if(!inventory.getBulkRule())
			{
				Rule rule = ruleService.findRuleById(inventory.getRuleId());
				if(rule != null) {
					Integer shippingDays = Integer.parseInt(propertyService.getValue(Constants.SHIPPING_DAYS));
					
					DateTime dt1 = new DateTime(new Date());
					DateTime dt2 = new DateTime(inventory.getManifestDate());
					
					Integer manifestDaysFromNow = Days.daysBetween(dt2, dt1).getDays();
					
					if(returnType.equals("vendor")){
						
						if(!rule.getRtv() && shippingDays < manifestDaysFromNow)
							msg = "This package belongs to Rule : " + rule.getName() + " and not eligible for RTV. "
									+ "This package was shipped by seller " + manifestDaysFromNow + " days ago. Do you still want to continue?";
						else if(!rule.getRtv())
							msg = "This package belongs to Rule : " + rule.getName() + " and not eligible for RTV. Do you still want to continue?";
						else if(shippingDays < manifestDaysFromNow)
							msg = "This package was shipped by seller " + manifestDaysFromNow + " days ago. Do you still want to continue?";
					}
					else if(returnType.equals("warehouse")){
						
						if(!rule.getWarehouseFlag() && shippingDays < manifestDaysFromNow)
							msg = "This package belongs to Rule : " + rule.getName() + " and not eligible for Return to SD+ Center. "
								+ "This package was shipped by seller " + manifestDaysFromNow + " days ago. Do you still want to continue?";
						else if(!rule.getWarehouseFlag())
							msg = "This package belongs to Rule : " + rule.getName() + " and not eligible for Return to SD+ Center. Do you still want to continue?";
						else if(shippingDays < manifestDaysFromNow)
							msg = "This package was shipped by seller " + manifestDaysFromNow + " days ago. Do you still want to continue?";
					}
					else if(returnType.equals("3pl"))
					{
						if(!rule.getFlag3pl() && shippingDays < manifestDaysFromNow)
							msg = "This package belongs to Rule : " + rule.getName() + " and not eligible for Return to 3PL. "
								+ "This package was shipped by seller " + manifestDaysFromNow + " days ago. Do you still want to continue?";
						else if(!rule.getFlag3pl())
							msg = "This package belongs to Rule : " + rule.getName() + " and not eligible for Return to 3PL. Do you still want to continue?";
						else if(shippingDays < manifestDaysFromNow)
							msg = "This package was shipped by seller " + manifestDaysFromNow + " days ago. Do you still want to continue?";
					}
					else if(returnType.equals("liquidation"))
					{
						if(!rule.getLiquidation() && shippingDays < manifestDaysFromNow)
							msg = "This package belongs to Rule : " + rule.getName() + " and not eligible for Return to Liquidation. "
								+ "This package was shipped by seller " + manifestDaysFromNow + " days ago. Do you still want to continue?";
						else if(!rule.getLiquidation())
							msg = "This package belongs to Rule : " + rule.getName() + " and not eligible for Return to Liquidation. Do you still want to continue?";
						else if(shippingDays < manifestDaysFromNow)
							msg = "This package was shipped by seller " + manifestDaysFromNow + " days ago. Do you still want to continue?";
					}
					else if(returnType.equals("customer"))
					{
						if(!rule.getRtc() && shippingDays < manifestDaysFromNow)
							msg = "This package belongs to Rule : " + rule.getName() + " and not eligible for Return to Customer. "
								+ "This package was shipped by seller " + manifestDaysFromNow + " days ago. Do you still want to continue?";
						else if(!rule.getRtc())
							msg = "This package belongs to Rule : " + rule.getName() + " and not eligible for Return to Customer. Do you still want to continue?";
						else if(shippingDays < manifestDaysFromNow)
							msg = "This package was shipped by seller " + manifestDaysFromNow + " days ago. Do you still want to continue?";
					}
				}
				
			}
				
		}
		return msg;
	}
	
	@RequestMapping("/searchInventory")
	public String searchInventory(@ModelAttribute("data") ManualRtvData data,ModelMap map)
	{
		Inventory inventory = null;
		List<Inventory> inventoryList = null;
		if(data.getBarcode() != null)
		{
			inventory = rtvService.getInventoryForReturn(data.getBarcode());
			if(inventory != null)
			{
				if(!inventory.getPostQcStatus().equals("HOLD"))
				{
					map.put("message", "Product can not be dispatched.");
				}
				else{ 
					if(data.getInventoryList() != null && data.getInventoryList().size() > 0)
					{
						inventoryList = data.getInventoryList();
						inventoryList.add(inventory);
						data.setInventoryList(inventoryList);
					}else {
						inventoryList = new ArrayList<Inventory>();
						inventoryList.add(inventory);
						data.setInventoryList(inventoryList);
						}
					}
			}else {
				map.put("message", "Product not found in warehouse for scanned code.");
			}
		}
		map.put("data", data);
		return "Rtv/homeManualReturn";
	}

	@RequestMapping("/manualManifest")
	public String manualManifest(@ModelAttribute("data") ManualRtvData data,ModelMap map)
	{
//		boolean warehouseFlag = false;//Suryansh
		String return_type = "";
		if(data.getReturnType() != null && data.getReturnType().equalsIgnoreCase("warehouse"))
		{
			return_type = "warehouse";
		}
		if(data.getReturnType() != null && data.getReturnType().equalsIgnoreCase("3pl"))
		{
			return_type = "3pl";
		}
		if(data.getReturnType() != null && data.getReturnType().equalsIgnoreCase("liquidation"))
		{
			return_type = "liquidation";
		}
		if(data.getReturnType() != null && data.getReturnType().equalsIgnoreCase("vendor"))
		{
			return_type = "vendor";
		}
		if(data.getReturnType() != null && data.getReturnType().equalsIgnoreCase("customer"))
		{
			return_type = "customer";
		}
		//

//		if(data.getReturnType() != null && data.getReturnType().equalsIgnoreCase("warehouse"))
//		{
//			warehouseFlag = true;
//		}
		if(data.getInventoryList() != null && data.getInventoryList().size() > 0)
		{
			List<Long> inventoryId = getIdListByObjectList(data.getInventoryList());
			if(return_type.equals("warehouse"))
			{
				if(!rtvService.checkScannedProductsForWarehouse(inventoryId))
				{
					map.put("message", "One or all scanned packages cannot be delivered to our centre.");
					map.put("data", data);
					return "Rtv/homeManualReturn";
				}
			}
			if(return_type.equals("liquidation"))
			{
				if(!rtvService.checkScannedProductsForLiquidation())
				{
					map.put("message", "No Liquidation Center for the packages");
					map.put("data", data);
					return "Rtv/homeManualReturn";
				}
			}
			if(return_type.equals("3pl"))
			{
				if(!rtvService.checkScannedProductsFor3pl())
				{
					map.put("message", "No 3PL Center for the packages");
					map.put("data", data);
					return "Rtv/homeManualReturn";
				}
			}
			List<Inventory> rtvInventory = rtvService.getLoadedInventoryList(inventoryId);
			List<RtvGroups> rtvGroupList = getRtvGroupsFromInventoryList(rtvInventory);
			/*List<RtvSheet> rtvSheets = getRtvSheetsFromRtvGroups(rtvGroupList, warehouseFlag);*/
			/*map.put("rtvSheets", rtvSheets);
			map.put("rtvDispatch", new RtvDispatch());*/
			map.put("groupList", rtvGroupList);
			map.put("rtv", new Rtv());
			map.put("returnType",return_type);
		}
		return "Rtv/group";
	}

	@RequestMapping("/getPicklist")
	public String getPicklist(@RequestParam("picklistId") Long id, ModelMap map)
	{
		String page = "Rtv/home";
		WarehouseList picklist = rtvService.getRtvEligibleListById(id);
		if(picklist != null)
		{
			map.put("picklist", picklist);
			map.put("rtvData", new RtvData());
			page = "Rtv/desk";
		}
		else {
			map.put("message", "No eligible picklist found for given identifier.");
		}
		return page;
	}

	@RequestMapping("/manifest")
	public String manifest(@ModelAttribute("rtv") Rtv rtv, ModelMap map)
	{
		if(rtv.getRtvGroupList() != null && rtv.getRtvGroupList().size() > 0)
		{
			List<RtvSheet> rtvSheets = getRtvSheetsFromRtvGroups(rtv.getRtvGroupList(), rtv.getReturnType());
			map.put("rtvSheets", rtvSheets);
			map.put("rtvDispatch", new RtvDispatch());
		}
		return "Rtv/dispatch";
	}

	@RequestMapping("/dispatch")
	public String dispatch(@ModelAttribute("rtvDispatch") RtvDispatch rtvDispatch,@ModelAttribute("returnTo") String page ,ModelMap map)
	{
		if(rtvDispatch.getRtvSheets() != null && rtvDispatch.getRtvSheets().size() > 0){
			List<RtvSheet> rtvSheets = new ArrayList<RtvSheet>();
			for(RtvSheet rtvSheet : rtvDispatch.getRtvSheets()){
				if(rtvSheet.getId() != null){
					RtvSheet persistedSheet = rtvService.findRtvSheetById(rtvSheet.getId());
					for(Inventory inventory : persistedSheet.getProductDetails()){
						
						if(!inventory.getStatus().equalsIgnoreCase(Constants.RTV_INITIATED)){
							groupService.freeLocation(inventory.getLocation());
						}
						
						if(!inventory.getStatus().equals(Constants.MANIFEST_PRINTED)){
							inventory.setStatus(Constants.RTV_STATUS);
						}
						
						putawayService.saveOrUpdateInventory(inventory);
					}
				
					if(persistedSheet.getCourierCode() == null || persistedSheet.getAwbNumber() == null || 
							!persistedSheet.getCourierCode().equals(rtvSheet.getCourierCode()) || 
							!persistedSheet.getAwbNumber().equals(rtvSheet.getAwbNumber())){
						
						/** Unset the previously assigned courier and tracking number **/
//						courierService.unSetAWB(persistedSheet.getCourierCode(), persistedSheet.getAwbNumber());
						
						persistedSheet.setCourierCode(rtvSheet.getCourierCode());
						persistedSheet.setAwbNumber(rtvSheet.getAwbNumber());
						
						historyService.addInventoryHistoryByList(persistedSheet.getProductDetails(),
								"Manually overridden courier : "+persistedSheet.getCourierCode()+" with tracking number : "
								+persistedSheet.getAwbNumber());
					}
					
					if(persistedSheet.getReceiverDetail() == null)
						persistedSheet.setReceiverDetail(rtvSheet.getReceiverDetail());
					
					persistedSheet.setAreaCode(shippingService.getAreaCode(persistedSheet.getCourierCode(),persistedSheet.getReceiverDetail().getPincode()));
					rtvSheets.add(rtvService.saveOrUpdateRtvSheet(persistedSheet));					
					
					historyService.addInventoryHistoryByList(persistedSheet.getProductDetails(),
							"Return Completed by courier : "+persistedSheet.getCourierCode()+" with tracking number : "
							+persistedSheet.getAwbNumber());
				}
			}
			map.put("sheets", rtvSheets);
		}
		
		map.put("returnTo", page);
		return "Rtv/print";
	}

	@RequestMapping("/generateGroup")
	public String generateGroup(@ModelAttribute("rtvData") RtvData rtvData,ModelMap map)
	{
		List<Long> missingInventory = null;
		if(rtvData.getMissingInventory() != null && rtvData.getMissingInventory().size() > 0)
		{
			missingInventory = new ArrayList<Long>();
			for(Inventory inventory : rtvData.getMissingInventory())
			{
				if(inventory.getId() != null)
				{
					missingInventory.add(inventory.getId());
				}
			}
			rtvService.markInventoryInWarehouse(missingInventory);
			historyService.addInventoryHistoryByIdList(missingInventory,Constants.HISTORY_ACTION_MISSING_PICKLIST);
		}
		WarehouseList picklist = picklistService.findPicklistById(rtvData.getListId());
		List<Inventory> rtvInventory = getRtvInventory(picklist.getInventoryList(), missingInventory);
		/*Map<String,List<Inventory>> vendorMap = getVendorListMap(rtvInventory);
		List<RtvGroups> rtvGroupsList = getListFromMap(vendorMap);*/
		List<RtvGroups> rtvGroupsList = getRtvGroupsFromInventoryList(rtvInventory);
		if(rtvGroupsList != null && rtvGroupsList.size() > 0)
		{
			map.put("groupList", rtvGroupsList);
			map.put("rtv", new Rtv());
			map.put("returnWarehouse", getWarehouseReturnFlag(rtvGroupsList.get(0)));
		}
		return "Rtv/group";
	}

	public List<RtvSheet> getRtvSheetsFromRtvGroups(List<RtvGroups> rtvGroupList,String return_type)
	{//Suryansh
		
		List<RtvSheet> rtvSheets = new ArrayList<RtvSheet>();
		Address address;//
		for(RtvGroups rtvGroup : rtvGroupList)
		{
			
			ReceiverDetail receiverDetail = new ReceiverDetail();
			if(rtvGroup != null && rtvGroup.getInventoryList() != null && 
					rtvGroup.getInventoryList().size() > 0 && rtvGroup.getVendorCode() != null)
			{
				
				RtvSheet rtvSheet = new RtvSheet();
				//Suryansh
				if(return_type.equals("3pl") || return_type.equals("liquidation")){
				    address=rtvService.get3plLiquidationDetail(return_type);
					if(address !=null)
					{
					    receiverDetail.setVendorCode(null);
						receiverDetail.setName(null);
						receiverDetail.setEmail(null);
						receiverDetail.setAddressLine1(address.getAddressLine1());
						receiverDetail.setAddressLine2(address.getAddressLine2());
						receiverDetail.setCity(address.getCity());
						receiverDetail.setState(address.getState());
						receiverDetail.setPincode(""+address.getPincode());
						receiverDetail.setContactNumber(address.getContactNumber());
						}
					}
				else
					if(return_type.equals("customer"))
					{
						//retreiving inventory items to be sent to customer
						List<Inventory> inventoryList=rtvGroup.getInventoryList();

						String inventoryCri=new String();
						inventoryCri=getCriCodeListFromInventoryList(inventoryList);
						receiverDetail=dao.getCustomerAddressDetail(inventoryCri);
					}
				else
					receiverDetail = dao.getReceiverDetails(getSuborderCodeFromRtvGroup(rtvGroup),rtvGroup.getVendorCode(),return_type);
				
				if(receiverDetail != null)
				{
					rtvSheet.setReceiverDetail(receiverDetail);
					//Suryasnh
					String returnType = "";
					if(return_type.equals("warehouse"))
					{
						if (closedFC.checkClosedFC(receiverDetail.getName()))
						{
							address=rtvService.get3plLiquidationDetail("liquidation");
							receiverDetail.setVendorCode(null);
							receiverDetail.setName(null);
							receiverDetail.setEmail(null);
							receiverDetail.setAddressLine1(address.getAddressLine1());
							receiverDetail.setAddressLine2(address.getAddressLine2());
							receiverDetail.setCity(address.getCity());
							receiverDetail.setState(address.getState());
							receiverDetail.setPincode(""+address.getPincode());
							receiverDetail.setContactNumber(address.getContactNumber());
							returnType = Constants.RETURN_TYPE_TO_LIQUIDATION;
						}
						else
							returnType = Constants.RETURN_TYPE_TO_CENTRE;
					}
					else if(return_type.equals("vendor"))
						returnType = Constants.RETURN_TYPE_TO_VENDOR;
					else if(return_type.equals("3pl"))
						returnType = Constants.RETURN_TYPE_TO_3PL;
					else if(return_type.equals("liquidation"))
						returnType = Constants.RETURN_TYPE_TO_LIQUIDATION;
					else if(return_type.equals("customer"))
						returnType = Constants.RETURN_TYPE_TO_CUSTOMER;
					//
					AWBResult awbResult = courierService.getAWB(receiverDetail.getPincode(),rtvGroup.getInventoryList().get(0).getShippingMode(),returnType);
					rtvSheet.setAwbNumber(awbResult.getTrackingNumber());
					rtvSheet.setCourierCode(awbResult.getCourierCode());
				}else {
					rtvSheet.setReceiverDetail(null);
					rtvSheet.setAwbNumber(null);
					rtvSheet.setCourierCode(null);
				}
				rtvSheet.setProductDetails(rtvService.getLoadedInventoryList(getIdListByObjectList(rtvGroup.getInventoryList())));
				
				/** RTV Initiated Message **/
				historyService.addInventoryHistoryByList(rtvSheet.getProductDetails(), Constants.HISTORY_ACTION_RTV);
				
				/** Add Courier Code and Tracking Number assigned by System in Inventory History **/
				historyService.addInventoryHistoryByList(rtvSheet.getProductDetails(),
						"System assigned Courier : "+rtvSheet.getCourierCode()+" with tracking number : "
						+rtvSheet.getAwbNumber());
				
				rtvSheet.setSellerName(rtvGroup.getSellerName());
				rtvSheet.setVendorCode(rtvGroup.getVendorCode());
				rtvSheet.setWarehouseDetails(sessionDetails.getSessionWarehouse());
				rtvSheet.setReturnType(return_type);
				rtvSheets.add(rtvService.saveOrUpdateRtvSheet(rtvSheet));
			}
		}
		return rtvSheets;
	}

	public List<RtvGroups> getRtvGroupsFromInventoryList(List<Inventory> inventoryList)
	{
		if(inventoryList != null && inventoryList.size() > 0)
		{
			List<RtvGroups> rtvGroups = new ArrayList<RtvGroups>();
			for(Inventory inventory : inventoryList)
			{
				RtvGroups rtvGroup = new RtvGroups();
				rtvGroup.setSellerName(inventory.getSellerName());
				rtvGroup.setVendorCode(inventory.getVendorCode());
				List<Inventory> subList = new ArrayList<Inventory>();
				subList.add(inventory);
				rtvGroup.setInventoryList(subList);
				rtvGroups.add(rtvGroup);
			}
			return rtvGroups;
		}
		return null;
	}

	public boolean getWarehouseReturnFlag(RtvGroups rtvGroups)
	{
		if(rtvGroups != null && rtvGroups.getInventoryList() != null && rtvGroups.getInventoryList().size() > 0)
		{
			if(!rtvGroups.getInventoryList().get(0).getBulkRule())
			{
				Rule rule = ruleService.findRuleById(rtvGroups.getInventoryList().get(0).getRuleId());
				if(rule != null && rule.getWarehouseFlag())
				{
					return true;
				}
			}
			else {
				return false;
			}
		}
		return false;
	}

	public List<Inventory> getRtvInventory(List<Inventory> listInventory, List<Long> missingInventory)
	{
		List<Inventory> rtvInventory = null;
		if(missingInventory != null && missingInventory.size() > 0)
		{
			rtvInventory = new ArrayList<Inventory>();
			for(Inventory inventory : listInventory)
			{
				if(!missingInventory.contains(inventory.getId()))
				{
					rtvInventory.add(inventory);
				}
			}
		}
		else {
			rtvInventory = listInventory;
		}
		return rtvInventory;
	}

	/*public Map<String,List<Inventory>> getVendorListMap(List<Inventory> rtvInventory)
	{
		Map<String,List<Inventory>> vendorMap = new HashMap<String, List<Inventory>>();
		for(Inventory inventory : rtvInventory)
		{
			if(vendorMap.containsKey(inventory.getVendorCode()))
			{
				vendorMap.get(inventory.getVendorCode()).add(inventory);
			}
			else {
				List<Inventory> newList = new ArrayList<Inventory>();
				newList.add(inventory);
				vendorMap.put(inventory.getVendorCode(), newList);
			}
		}
		return vendorMap;
	}

	public List<RtvGroups> getListFromMap(Map<String,List<Inventory>> vendorMap)
	{
		List<RtvGroups> rtvGroupsList = new ArrayList<RtvGroups>();
		Iterator<Map.Entry<String, List<Inventory>>> iterator = vendorMap.entrySet().iterator();
		while(iterator.hasNext())
		{
			Map.Entry<String, List<Inventory>> entry = iterator.next();
			if(entry.getValue() != null && entry.getValue().size() > 0 && entry.getValue().size() <= GROUP_SIZE)
			{
				RtvGroups rtvGroups = new RtvGroups();
				rtvGroups.setVendorCode(entry.getKey());
				rtvGroups.setSellerName(entry.getValue().get(0).getSellerName());
				rtvGroups.setInventoryList(entry.getValue());
				rtvGroupsList.add(rtvGroups);
			}
			else if(entry.getValue() != null && entry.getValue().size() > 0 && entry.getValue().size() > GROUP_SIZE){
				int split = 1;
				RtvGroups rtvGroups = null;
				List<Inventory> subList = null;
				for(Inventory inventory : entry.getValue())
				{
					if(split == 1)
					{
						rtvGroups = new RtvGroups();
						rtvGroups.setVendorCode(entry.getKey());
						rtvGroups.setSellerName(inventory.getSellerName());
						subList = new ArrayList<Inventory>();
						subList.add(inventory);
						rtvGroups.setInventoryList(subList);
						rtvGroupsList.add(rtvGroups);
					}
					else if (split == GROUP_SIZE) {
						subList.add(inventory);
						split = 0;
					}
					else {
						subList.add(inventory);
					}
					split++;
				}
			}
		}
		return rtvGroupsList;
	}
	 */
	public List<Long> getIdListByObjectList(List<Inventory> inventoryList)
	{
		List<Long> idList = new ArrayList<Long>();
		for(Inventory inventory : inventoryList)
		{
			if(inventory.getId() != null)
			{
				idList.add(inventory.getId());
			}
		}
		return idList;
	}
	
	public String getSuborderCodeFromRtvGroup(RtvGroups rtvGroup)
	{
		String suborderCode;
		Inventory inv=putawayService.findInventoryById(rtvGroup.getInventoryList().get(0).getId());
		suborderCode=inv.getSuborderCode();
		
		return suborderCode;
	}
	
	public String getCriCodeListFromInventoryList(List<Inventory> inventoryList)
	{
		String criList = "(";
		for(Inventory inventory : inventoryList)
		{	
			if(inventory.getId() != null)
			{
				Inventory temp=putawayService.findInventoryById(inventory.getId());
				criList+="'"+temp.getBarcode()+"',";
			}
		}
		int ind = criList.lastIndexOf(",");
		StringBuffer str = new StringBuffer(criList);
		str.replace(ind, str.length(), ")");
		criList=str.toString();
	

		return criList;
	}
}
