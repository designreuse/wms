package com.snapdeal.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.snapdeal.bean.ProductDetails;
import com.snapdeal.bean.RtvDispatch;
import com.snapdeal.component.ClosedFC;
import com.snapdeal.component.CommonOperations;
import com.snapdeal.component.Constants;
import com.snapdeal.component.DirectRtv;
import com.snapdeal.dao.Dao;
import com.snapdeal.entity.Inventory;
import com.snapdeal.entity.RtvSheet;
import com.snapdeal.entity.WarehouseBoy;
import com.snapdeal.entity.WarehouseList;
import com.snapdeal.service.BulkRuleService;
import com.snapdeal.service.HistoryService;
import com.snapdeal.service.PutawayService;
import com.snapdeal.service.RtvService;
import com.snapdeal.service.RuleService;
import com.snapdeal.service.WarehouseService;

@Controller
@RequestMapping("/PutawayDirect")
public class DirectPutawayController {

	@Inject
	@Named("ruleService")
	RuleService ruleService;

	@Inject
	@Named("commonOperations")
	CommonOperations commonOperations;
	
	@Inject
	@Named("putawayService")
	PutawayService putawayService;

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
	
	
	@RequestMapping("/home")
	public String putawayProduct(ModelMap map) 
	{
		return "Putaway/newPutaway";
	}
	
	
	@RequestMapping("/check")
	public String checkOperation(@RequestParam("barcode") String barcode, 
			@RequestParam(value="listId",required=false) Long listId,ModelMap map)
	{
		WarehouseList putawayList = null;
		ProductDetails productDetails = null;
		Inventory inventory = null;
		String return_type = null;
		/** Checks whether product exists in the warehouse or not **/
		if(!putawayService.checkBarcodeForDirectPutaway(barcode))
		{			
				/** Gets complete details for scanned barcode from RMS, DWH, Score and shipping **/
				productDetails = dao.getProductDetails(barcode);
				if(productDetails == null){
					map.put("message", "Invalid CRI Code.");
					return "Putaway/newPutaway";
				}
				else if(productDetails.getPostQcStatus().equals("HOLD"))
				{
					map.put("message", "Post QC status is HOLD. Not eligible for Direct Putaway");
					return "Putaway/newPutaway";
				}
				else
				{
					inventory = putawayService.findInventoryByBarcode(barcode);
					if(inventory == null)
					{
						inventory = new Inventory();
					}
					inventory.setLocation("NA");
					inventory.setBarcode(barcode);
					inventory.setBulkRule(false);
					inventory.setGroupName(Constants.GROUP_NAME_DIRECT_PUTAWAY);
					inventory.setRuleId(Constants.RULE_ID_DIRECT_PUTAWAY);
					
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
					inventory.setStatus(Constants.IN_WAREHOUSE_STATUS);
					
					inventory.setPostQcStatus(productDetails.getPostQcStatus());
					inventory.setQcStatus(productDetails.getQcStatus());
					
					/** If shipping Mode is still null then, set it to Air hard coded.**/
					if(productDetails.getShippingMode() == null)
						productDetails.setShippingMode(Constants.SHIPPING_MODE_HARD_CODED);
					
					inventory.setShippingMode(productDetails.getShippingMode());
					
//					if(inventory.getForwardAwbNumber() == null || inventory.getManifestDate() == null){
//						/** Unable to find forward Awb number. Manifest Date for package is < 2014-01-01. **/
//						map.put("message", "Unable to find forward Awb Number.");
//						return "Putaway/newPutaway";
//					}
					if(productDetails.getCriStatus().equals("BCG") || productDetails.getCriStatus().equals("SCND") 
							|| productDetails.getCriStatus().equals("MSNG")){
						/** Don't Allow these CRI Status product into WMS **/
						map.put("message", "Not Allowed! CRI Status : " + productDetails.getCriStatus());
						return "Putaway/newPutaway";
					}
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
						
						if(flag){
							putawayList = putawayService.saveOrUpdatePutawayList(putawayList);
							historyService.addInventoryHistory(inventory, "Putaway at location : "+inventory.getLocation());
							map.put("alert", true);
						}
					}
				}
		}
		else {
			map.put("message", "Product already dispatched.");
			return "Putaway/newPutaway";
		}
		
		List<Inventory> list = new ArrayList<Inventory>();
		Inventory persistedInventory = putawayService.findInventoryByBarcode(inventory.getBarcode());
		list.add(persistedInventory);
		
		historyService.addInventoryHistory(persistedInventory, Constants.HISTORY_ACTION_RTV_DIRECT);
		
	
		if(productDetails.getPostQcStatus().equals("RTS"))
			return_type = "vendor";
		else if(productDetails.getPostQcStatus().equals("LQD") 
				|| productDetails.getPostQcStatus().equals("FCD") 
				|| productDetails.getPostQcStatus().equals("RCD"))
			return_type = "liquidation";
		else if(productDetails.getPostQcStatus().equals("3PL"))
			return_type = "3pl";
		else if(productDetails.getPostQcStatus().equals("RTC"))
			return_type = "customer";
		else if(productDetails.getPostQcStatus().equals("RTSDP"))
			return_type = "warehouse";

			
		if(return_type != null){
			RtvSheet rtvSheet = directRtv.getRtvSheetsFromInventory(persistedInventory, return_type);
			
			List<RtvSheet> rtvlist = new ArrayList<RtvSheet>();
			rtvlist.add(rtvSheet);
			
			map.put("rtvSheets", rtvlist);
			map.put("rtvDispatch", new RtvDispatch());
			map.put("returnTo", "putawaydirect");
			map.put("returnType",rtvSheet.getReturnType().toUpperCase());

			return "Rtv/dispatch";
		}
		else
			return "Putaway/newPutaway";
		
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
}
