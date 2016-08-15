package com.snapdeal.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.snapdeal.bean.ManualRtvData;
import com.snapdeal.bean.ProductDetails;
import com.snapdeal.bean.RtvDispatch;
import com.snapdeal.bean.RtvGroups;
import com.snapdeal.bean.RuleExecutionResult;
import com.snapdeal.bean.WarehouseSearch;
import com.snapdeal.component.CommonOperations;
import com.snapdeal.component.Constants;
import com.snapdeal.component.DirectRtv;
import com.snapdeal.component.RuleExecutor;
import com.snapdeal.dao.Dao;
import com.snapdeal.entity.BulkRule;
import com.snapdeal.entity.Group;
import com.snapdeal.entity.Inventory;
import com.snapdeal.entity.RtvCheckRule;
import com.snapdeal.entity.RtvSheet;
import com.snapdeal.entity.Rule;
import com.snapdeal.entity.WarehouseBoy;
import com.snapdeal.entity.WarehouseList;
import com.snapdeal.service.BulkRuleService;
import com.snapdeal.service.GroupService;
import com.snapdeal.service.HistoryService;
import com.snapdeal.service.PutawayService;
import com.snapdeal.service.RtvService;
import com.snapdeal.service.RuleService;
import com.snapdeal.service.WarehouseService;


/** Perform Putaway related operations **/
@Controller
@RequestMapping("/Putaway")
public class PutawayController {

	@Inject
	@Named("ruleService")
	RuleService ruleService;

	@Inject
	@Named("commonOperations")
	CommonOperations commonOperations;

	@Inject
	@Named("ruleExecutor")
	RuleExecutor ruleExecutor;

	@Inject
	@Named("putawayService")
	PutawayService putawayService;

	@Inject
	@Named("groupService")
	GroupService groupService;

	@Inject
	@Named("warehouseService")
	WarehouseService warehouseService;

	@Inject
	@Named("historyService")
	HistoryService historyService;

	@Autowired
	Dao dao;
	
	@Inject
	@Named("rtvService")
	RtvService rtvService;
	
	@Inject
	@Named("directRtv")
	DirectRtv directRtv;
	
	@Inject
	@Named("bulkruleService")
	BulkRuleService bulkruleService;
	
	RtvController rtvController;
	 
	
	@RequestMapping("/home")
	public String putawayProduct(ModelMap map) {
		List<Rule> ruleList = ruleService.getActiveRules();
		map.put("ruleList",ruleList);
		return "Putaway/home";
	}

	/** Gets details for Barcode scanned and find a suitable rule to place it in the warehouse **/
	@RequestMapping("/check")
	public String checkOperation(@RequestParam("barcode") String barcode, 
			@RequestParam(value="manualRuleId",required=false) Long manualRuleId,
			@RequestParam(value="listId",required=false) Long listId,ModelMap map)
	{
		boolean directrtv = false;
		WarehouseList putawayList = null;
		
		/** Checks whether product exists in the warehouse or not **/
		if(!putawayService.checkBarcode(barcode))
		{
			/**Checks if product exists in another Warehouse **/
			if(putawayService.checkBarcodeinAnotherWarehouse(barcode))
			{
				map.put("message", "Product exists in Another Warehouse");
				if(listId != null)
				{
					putawayList = putawayService.findPutawayListById(listId);	
				}
			}else
			{			
				RuleExecutionResult ruleExecutionResult = null;
				/** Gets complete details for scanned barcode from RMS, DWH, Score and shipping **/
				ProductDetails productDetails = dao.getDetails(barcode);
				if(productDetails == null){
					map.put("message", "Invalid CRI Code.");
				}
				else
					/** Checks whether it fits any bulk upload rule or not **/
					if(manualRuleId == null || manualRuleId == 0){
						Long ruleId = ruleService.checkBulkUpload(productDetails.getSuborderCode());
						if(ruleId != null)
						{
							/*String sellerName = dao.getSellerName(barcode);*/
							if(productDetails.getSellerName() != null)
							{
								ruleExecutionResult = ruleExecutor.getLocationForBulkRule(productDetails.getSellerName(), ruleId);	
							}
							else {
								ruleExecutionResult = ruleExecutor.getLocationForBulkRule("X", ruleId);
							}
						}
						else {
							/** Load all rules by priority **/
							List<Rule> ruleList = ruleService.getRulesByPriority();
							for(Rule rule : ruleList)
							{
								/** Finds a best match for the scanned product in the defined rules **/
								ruleExecutionResult = ruleExecutor.checkRule(rule, productDetails);
								if(ruleExecutionResult != null)
								{	
									break;
								}
							}	
						}
					}
					else{
						Rule rule = ruleService.findRuleById(manualRuleId);
						ruleExecutionResult = ruleExecutor.checkRule(rule, productDetails);
					}
					
					if(ruleExecutionResult != null && ruleExecutionResult.getLocation() != null)
					{
						Inventory inventory = putawayService.findInventoryByBarcode(barcode);
						if(inventory == null)
						{
							inventory = new Inventory();
						}
						/** Checks for RTV eligible cases **/
						if(ruleExecutionResult.getStatus().equalsIgnoreCase(Constants.RTV_STATUS))
						{
							List<RtvCheckRule> rtvRules = putawayService.getEnabledRtvRules();
							if(rtvRules != null && rtvRules.size() > 0)
							{
								if(ruleExecutor.showAlertOnRtv(rtvRules, productDetails.getVendorCode(), 
										productDetails.getManifestDate(), ruleExecutionResult.getGroupName()))
								{
									map.put("alert", true);
								}
								/*else{
									inventory.setStatus(Constants.RTV_STATUS);
									inventory.setLocation("NA");
								}*/
							}
						
							directrtv=true;
							
							
							groupService.updateLocation(ruleExecutionResult.getLocation());
							inventory.setStatus(Constants.PUTAWAY_STATUS);
							inventory.setLocation(ruleExecutionResult.getLocation());
						}
						else {
							groupService.updateLocation(ruleExecutionResult.getLocation());
							inventory.setStatus(Constants.PUTAWAY_STATUS);
							inventory.setLocation(ruleExecutionResult.getLocation());
						}
					
						inventory.setBarcode(barcode);
						inventory.setBulkRule(ruleExecutionResult.getBulkRule());
						inventory.setGroupName(ruleExecutionResult.getGroupName());
						inventory.setRuleId(ruleExecutionResult.getRuleId());
						
						/** If seller Name is null then, set it to NA hard coded. **/
						if(productDetails.getSellerName() == null)
							productDetails.setSellerName(Constants.SELLER_NAME_HARD_CODED);
							
						inventory.setSellerName(productDetails.getSellerName());
						inventory.setVendorCode(productDetails.getVendorCode());
						inventory.setProductName(productDetails.getProductName());
						inventory.setCustomerName(productDetails.getCustomerName());
						inventory.setSupc(productDetails.getSupc());
						inventory.setSku(productDetails.getSku());
						inventory.setOrderCode(productDetails.getOrderCode());
						inventory.setQcRemarks(productDetails.getQcRemarks());
						inventory.setIssueCategory(productDetails.getIssueCategory());
						inventory.setTicketId(productDetails.getTicketId());
						inventory.setFulfillmentModel(productDetails.getFulfillmentModel());
						inventory.setManifestDate(productDetails.getManifestDate());
						inventory.setPrice(productDetails.getPrice());
						inventory.setWeight(productDetails.getWeight());
						inventory.setRmsCenter(productDetails.getRmsCenter());
						inventory.setSubCategory(productDetails.getSubCategory());
						inventory.setCcStatus(productDetails.getCcStatus());
						inventory.setSuborderCode(productDetails.getSuborderCode());
						inventory.setForwardAwbNumber(productDetails.getForwardAwbNumber());
						
						/** If shipping Mode is still null then, set it to Air hard coded.**/
						if(productDetails.getShippingMode() == null)
							productDetails.setShippingMode(Constants.SHIPPING_MODE_HARD_CODED);
						
						inventory.setShippingMode(productDetails.getShippingMode());
						
						
//						---Excluded ---	
//						if(inventory.getForwardAwbNumber() == null || inventory.getManifestDate() == null){
//							/** Unable to find forward Awb number. Manifest Date for package is < 2014-01-01. **/
//							map.put("message", "Unable to find forward Awb Number.");
//							if(listId != null){
//								putawayList = putawayService.findPutawayListById(listId);	
//							}
//						}
//						------------------
						/** Not allow other status than below**/
						if(productDetails.getCriStatus().equals("PDC"))
						{
							if(!productDetails.getPostQcStatus().equals("HOLD")||!productDetails.getPostQcStatus().equals("RTC")
								|| !productDetails.getPostQcStatus().equals("RTSDP"))
								map.put("message", "Not Allowed! Post QC  Status : " + productDetails.getPostQcStatus());
							if(listId != null){
								putawayList = putawayService.findPutawayListById(listId);	
							}
						}
						else if(!productDetails.getCriStatus().equals("RABW") || !productDetails.getCriStatus().equals("RRBW") )
						{
							map.put("message", "Not Allowed! CRI Status : " + productDetails.getCriStatus());
							if(listId != null){
								putawayList = putawayService.findPutawayListById(listId);	
							}
						}
//						if(productDetails.getCriStatus().equals("BCG") || productDetails.getCriStatus().equals("SCND") 
//								|| productDetails.getCriStatus().equals("MSNG")){
//							/** Don't Allow these CRI Status product into WMS **/
//							map.put("message", "Not Allowed! CRI Status : " + productDetails.getCriStatus());
//							if(listId != null){
//								putawayList = putawayService.findPutawayListById(listId);	
//							}
//						}
						else{
							Boolean flag = false;
							/** To handle multiple requests from same source. **/
							if(!putawayService.checkBarcode(barcode)){
								map.put("inventoryId",putawayService.saveOrUpdateInventory(inventory));
								flag = true;
							}
							else{
								inventory = putawayService.findInventoryByBarcode(barcode);
								map.put("inventoryId",inventory.getId());
							}
							
							List<Inventory> newList = null;
							if(listId != null)
							{
								putawayList = putawayService.findPutawayListById(listId);
								newList = putawayList.getInventoryList();
								newList.add(inventory);
								putawayList.setInventoryList(newList);
							}
							else {
								putawayList = new WarehouseList();
								newList = new ArrayList<Inventory>();
								newList.add(inventory);
								putawayList.setInventoryList(newList);
								putawayList.setType(Constants.PUTAWAY_LIST);
							}
							
							if(flag)
								putawayList = putawayService.saveOrUpdatePutawayList(putawayList);
								historyService.addInventoryHistory(inventory, "Putaway at location : "+inventory.getLocation());
						}
						
					}
					else{
						map.put("message", "System was unable to find suitable action for scanned package.");
						if(listId != null)
						{
							putawayList = putawayService.findPutawayListById(listId);	
						}
				}
			}
		
		}
		else {
			map.put("message", "Product already exists in the Warehouse.");
			if(listId != null)
			{
				putawayList = putawayService.findPutawayListById(listId);	
			}
		}
		if(putawayList != null)
		{
			map.put("putawayList", putawayList);
			/*map.put("listId", putawayList.getId());*/
		}
		
		if(directrtv)
		{
			map.put("directrtv",directrtv);
		}
		List<Rule> ruleList = ruleService.getActiveRules();
		map.put("ruleList",ruleList);
		return "Putaway/home";
	}

	/** Update RTV status for RTV selected cases **/
	@RequestMapping("/updateRtv")
	public String updateRtv(@ModelAttribute("listId") Long listId, @ModelAttribute("inventoryId") Long inventoryId,ModelMap map) {
		if(inventoryId != null) {
			Inventory inventory = putawayService.findInventoryById(inventoryId);
			if(listId != null) {
				
				WarehouseList putawayList = putawayService.findPutawayListById(listId);
				int index = 0;
				for(Inventory listInventory : putawayList.getInventoryList()) {
					
					if(listInventory.getId().equals(inventoryId)) {
						putawayList.getInventoryList().remove(index);
						putawayList = putawayService.saveOrUpdatePutawayList(putawayList);
						break;
					}
					else{
						index++;
					}
				}
				map.put("putawayList", putawayList);
				map.put("inventoryId", "");
				/*map.put("listId",putawayList.getId());*/

			}
			inventory.setStatus(Constants.RTV_INITIATED);
			groupService.freeLocation(inventory.getLocation());
			putawayService.saveOrUpdateInventory(inventory);
			historyService.addInventoryHistory(inventory, Constants.HISTORY_ACTION_RTV_DIRECT);
		}
		
		List<Rule> ruleList = ruleService.getActiveRules();
		map.put("ruleList",ruleList);
		return "Putaway/home";
	}

	/** Prints putaway list **/
	@RequestMapping("/printList")
	public String printList(@RequestParam("listId") Long listId,@RequestParam("warehouseBoy") Long boyId,
			ModelMap map)
	{
		WarehouseList putawayList = putawayService.findPutawayListById(listId);
		WarehouseBoy warehouseBoy = warehouseService.findWarehouseBoyById(boyId);
		if(putawayList != null && putawayList.getWarehouseBoy() == null)
		{
			putawayList.setWarehouseBoy(warehouseBoy);
			List<Inventory> newList = new ArrayList<Inventory>();
			for(Inventory inventory : putawayList.getInventoryList())
			{
				inventory.setStatus(Constants.PUTAWAY_LIST_PRINTED);
				newList.add(inventory);
			}
			putawayList.setInventoryList(newList);
			putawayService.saveOrUpdatePutawayList(putawayList);	
		}
		Collections.sort(putawayList.getInventoryList());
		map.put("putawayList", putawayList.getInventoryList());
		map.put("listId", putawayList.getId());
		map.put("warehouseBoy", warehouseBoy.getName());
		map.put("date",new Date());
		return "Putaway/print";
	}

	/** Prints location barcode for every product **/
	@RequestMapping("/printBarcode/{id}")
	public void downloadBarcode(@PathVariable("id") Long id,HttpServletResponse response)
	{
		try {
			Inventory inventory = putawayService.findInventoryById(id);
			String line="Barcode,Location,Seller Name, Product name\n"+
			inventory.getBarcode()+","+inventory.getLocation()+","+inventory.getSellerName()+","+inventory.getProductName()+"\n";
			response.setContentType("application/force-download");
			response.setContentLength(line.length());
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-Disposition", "attachment; filename=Inventory.csv");
			response.getWriter().write(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*@RequestMapping("/bulkUpload")
	public String bulkUpload(ModelMap map)
	{
		FileBean fileBean = new FileBean();
		map.put("fileBean", fileBean);
		return "Putaway/bulkUpload";
	}

	@RequestMapping("/action")
	public void getPutaway(@ModelAttribute("fileData") FileBean file,ModelMap map,HttpServletResponse response)
	{
		try {
			MultipartFile postedFile = file.getPostedFile();
			String downloadData = "Barcode,Status,Location,RuleId\n";
			if(postedFile != null)
			{
				InputStream inputStream;
				inputStream = postedFile.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String line = "";
				String splitBy = ",";
				int header = 1;
				int error = 0;
				while ((line = bufferedReader.readLine()) != null)
				{
					String [] data = line.split(splitBy);
					if(header == 1)
					{
						if(!data[0].equals("Barcode"))
						{
							error = 1;
							break;
						}
						else{
							header = -1;
						}
					}
					else{
						System.out.println("Working on code : "+data[0]+" at : "+new Date());
						RuleExecutionResult ruleExecutionResult = null;
						Long ruleId = ruleService.checkBulkUpload(data[0]);
						if(ruleId != null)
						{
							String sellerName = dao.getSellerName(data[0]);
							if(sellerName != null)
							{
								ruleExecutionResult = ruleExecutor.getLocationForBulkRule(sellerName, ruleId);	
							}
							else {
								ruleExecutionResult = ruleExecutor.getLocationForBulkRule("X", ruleId);
							}
						}
						else {
							ProductDetails productDetails = dao.getDetails(data[0]);
							List<Rule> ruleList = ruleService.getRulesByPriority();
							for(Rule rule : ruleList)
							{
								ruleExecutionResult = ruleExecutor.checkRule(rule, productDetails);
								if(ruleExecutionResult != null)
								{	
									break;
								}
							}	
						}
						if(ruleExecutionResult != null)
						{
							groupService.updateLocation(ruleExecutionResult.getLocation());
							if(ruleExecutionResult.getLocation() != null)
							{
								System.out.println("Location for : "+data[0]+" is - "+ruleExecutionResult.getLocation());
								ruleService.updateLocation(ruleExecutionResult.getLocation());	
							}
							else{
								System.out.println("Location for : "+data[0]+" is not found.");
								ruleExecutionResult.setLocation("NA");
							}
							downloadData += data[0]+","+ruleExecutionResult.getStatus()+","+ruleExecutionResult.getLocation()+
							","+ruleExecutionResult.getRuleId()+"\n";
						}
						else{
							downloadData += data[0]+",Failed To Find Suitable Action,Not Found,NA \n";
						}
						System.out.println("Completed Working on code : "+data[0] +" at : "+new Date());
					}
				}
				if(error == 1)
				{
					map.put("message", "Invalid File Uploaded.");
				}
			} 
			else {
				map.put("message", "Empty File Uploaded.");
			}
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", "attachment; filename=Putaway.csv");
			response.setContentLength(downloadData.length());
			response.getWriter().write(downloadData);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	 */

	@RequestMapping("/searchPutaway")
	public String searchPutaway(ModelMap map)
	{
		map.put("search",new WarehouseSearch());
		return "Putaway/search";
	}

	/** Searches for putaway list between two dates **/
	@RequestMapping("/search")
	public String search(@ModelAttribute("search") WarehouseSearch putawaySearch,ModelMap map)
	{
		List<WarehouseList> putawayLists = putawayService.searchPutawayList(putawaySearch);
		if(putawayLists != null && putawayLists.size() > 0)
		{
			map.put("list", putawayLists);	
		}
		else {
			map.put("message", "No lists found for given date range.");
		}
		map.put("search",new WarehouseSearch());
		return "Putaway/search";
	}

	@RequestMapping("/confirmPutaway")
	public String confirmPutaway()
	{
		return "Putaway/confirm";
	}

	/** Searches for putaway list for scanned id **/
	@RequestMapping("/confirmSearch")
	public String searchForConfirm(@RequestParam(value="listId", required=false) Long id, @RequestParam(value="barcode", required=false) String barcode, ModelMap map)
	{
		
		WarehouseList putawayList;
		if(barcode != null && !barcode.isEmpty()){
			putawayList = putawayService.findPutawayListByBarcodeToConfirm(barcode);
		}
		else{
			putawayList = putawayService.findPutawayListByIdToConfirm(id);
		}
		
		if(putawayList != null)
		{
			if(putawayList.getInventoryList() != null && putawayList.getInventoryList().size() > 0)
			{
				if(putawayList.getInventoryList().get(0).getStatus().equalsIgnoreCase(Constants.IN_WAREHOUSE_STATUS))
				{
					map.put("message", "Putaway List has already been confirmed.");
				}
				/*else if (putawayList.getWarehouseBoy() == null) {
					map.put("message", "Please print this putaway list before confirming.");
				}*/
				else {
					Collections.sort(putawayList.getInventoryList());
					map.put("putawayList",putawayList);
				}
			}
			else {
				map.put("message", "Something went wrong while searching putaway list.");
			}
		}
		else {
			map.put("message", "No list found for given paramter.");
		}
		return "Putaway/confirm";
	}

	/** Confirms putaway for a list. Also provide option to relocate products. **/
	@RequestMapping("/confirm")
	public String confirm(@ModelAttribute("putawayPostList") WarehouseList putawayList,ModelMap map)
	{
		WarehouseList persistedPutawayList = putawayService.findPutawayListById(putawayList.getId());
		Map<Long, Inventory> inventoryMap = commonOperations.getMapFromList(persistedPutawayList.getInventoryList());
		List<Inventory> confirmList = new ArrayList<Inventory>();
		String error = "Invalid location entered for following barcodes :";
		int errorFlag = 0;
		if(putawayList.getInventoryList().size() == persistedPutawayList.getInventoryList().size())
		{
			for(Inventory inventory : putawayList.getInventoryList())
			{
				if(inventoryMap.containsKey(inventory.getId()))
				{
					Inventory updatedInventory = inventoryMap.get(inventory.getId());
					if(!updatedInventory.getLocation().equalsIgnoreCase(inventory.getLocation()))
					{
						if(groupService.checkLocation(inventory.getLocation()))
						{
							groupService.freeLocation(updatedInventory.getLocation());
							groupService.updateLocation(inventory.getLocation());
							
							Group group=groupService.getGroupByName(inventory.getLocation().split("-")[0]);
							Rule rule = groupService.getRuleByGroupId(group.getId());
							
							if (rule == null)
							{
								BulkRule bulkrule = bulkruleService.getBulkRulebyGroupId(group.getId());
								if(bulkrule != null)
								{
									updatedInventory.setRuleId(bulkrule.getId());
									updatedInventory.setBulkRule(true);
									updatedInventory.setLocation(inventory.getLocation());
									updatedInventory.setGroupName(inventory.getLocation().split("-")[0]);
									historyService.addInventoryHistory(updatedInventory, 
											"Product relocated to location : "+updatedInventory.getLocation()+" while confirming putaway.");			
							
								}else{
									errorFlag = 1;
									error+="Rule Error";
									}
							}
							else
							{
								updatedInventory.setRuleId(rule.getId());
								updatedInventory.setLocation(inventory.getLocation());
								updatedInventory.setGroupName(inventory.getLocation().split("-")[0]);
								historyService.addInventoryHistory(updatedInventory, 
									"Product relocated to location : "+updatedInventory.getLocation()+" while confirming putaway.");
							}
						}
						else {
							errorFlag = 1;
							error+=updatedInventory.getBarcode()+", ";
						}
					}
					updatedInventory.setStatus(Constants.IN_WAREHOUSE_STATUS);
					confirmList.add(updatedInventory);
				}
			}
			persistedPutawayList.setInventoryList(confirmList);
			putawayService.saveOrUpdatePutawayList(persistedPutawayList);
			historyService.addInventoryHistoryByList(confirmList, Constants.HISTORY_ACTION_IW);
		}
		else{
			error = "Wrong Data Posted.";
		}
		if(errorFlag == 0)
		{
			map.put("message", "Data Updated Successfully");
			map.put("success", true);
		}
		else {
			map.put("message", error.substring(0, error.length() - 2));
		}
		return "Putaway/confirm";
	}
	
	@RequestMapping(value="/Directdispatch")
	public String dispatchRedirect(@ModelAttribute("inventoryId") Long invId,@ModelAttribute("listId") Long listId, ModelMap map)
	{			
		Inventory inventory = putawayService.findInventoryById(invId);
		
		inventory.setStatus(Constants.IN_WAREHOUSE_STATUS);
		putawayService.saveOrUpdateInventory(inventory);
		
		/**remove from putaway list **/
		WarehouseList putawayList = putawayService.findPutawayListById(listId);
		int index = 0;
		for(Inventory listInventory : putawayList.getInventoryList()) {
			
			if(listInventory.getId().equals(invId)) {
				putawayList.getInventoryList().remove(index);
				putawayList = putawayService.saveOrUpdatePutawayList(putawayList);
				break;
			}
			else{
				index++;
			}
		}
		
		List<Inventory> list = new ArrayList<Inventory>();
		list.add(inventory);
		
		historyService.addInventoryHistory(inventory, Constants.HISTORY_ACTION_RTV_DIRECT);
		
		ManualRtvData manualRtvData = new ManualRtvData();
		manualRtvData.setInventoryList(list);
		manualRtvData.setReturnType("vendor");
		
		List<Long> inventoryId = directRtv.getIdListByObjectList(manualRtvData.getInventoryList());
		
		List<Inventory> rtvInventory = rtvService.getLoadedInventoryList(inventoryId);
		List<RtvGroups> rtvGroupList = directRtv.getRtvGroupsFromInventoryList(rtvInventory);
	
		if(rtvGroupList != null && rtvGroupList.size() > 0)
			{
				List<RtvSheet> rtvSheets = directRtv.getRtvSheetsFromRtvGroups(rtvGroupList, "vendor");
				map.put("rtvSheets", rtvSheets);
				map.put("rtvDispatch", new RtvDispatch());
				map.put("returnTo", "putaway");
			}

		return "Rtv/dispatch";

	}
}
