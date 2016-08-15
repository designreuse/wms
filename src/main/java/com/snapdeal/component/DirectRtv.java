package com.snapdeal.component;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snapdeal.bean.AWBResult;
import com.snapdeal.bean.RtvGroups;
import com.snapdeal.dao.Dao;
import com.snapdeal.entity.Address;
import com.snapdeal.entity.Inventory;
import com.snapdeal.entity.Liquidation;
import com.snapdeal.entity.ReceiverDetail;
import com.snapdeal.entity.RtvSheet;
import com.snapdeal.service.CourierService;
import com.snapdeal.service.HistoryService;
import com.snapdeal.service.PutawayService;
import com.snapdeal.service.RtvService;

@Component
@Named("directRtv")
public class DirectRtv {
	
	@Inject
	@Named("historyService")
	HistoryService historyService;
	
	@Inject
	@Named("rtvService")
	RtvService rtvService;
	
	@Inject
	@Named("courierService")
	CourierService courierService;
	
	@Inject
	@Named("sessionDetails")
	SessionDetails sessionDetails;
	
	@Inject
	@Named("putawayService")
	PutawayService putawayService;
	
	@Autowired
	Dao dao;
	
	@Inject
	@Named("closedFC")
	ClosedFC closedFC;
	
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

	@SuppressWarnings("null")
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
				if(return_type.equals("3pl") || return_type.equals("liquidation")){
				    address =rtvService.get3plLiquidationDetail(return_type);
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
				else
					if(return_type.equals("customer"))
					{
						//retrieving inventory items to be sent to customer
						List<Inventory> inventoryList=rtvGroup.getInventoryList();
						String inventoryCri=new String();
						inventoryCri=getCriCodeListFromInventoryList(inventoryList);
						receiverDetail=dao.getCustomerAddressDetail(inventoryCri);
					}
				//
				else
					receiverDetail = dao.getReceiverDetails(getSuborderCodeFromRtvGroup(rtvGroup),rtvGroup.getVendorCode(),return_type);
				
				if(receiverDetail != null)
				{
					rtvSheet.setReceiverDetail(receiverDetail);
					//Suryasnh
					String returnType = "";
					if(return_type.equals("warehouse"))
						returnType = Constants.RETURN_TYPE_TO_CENTRE;
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
			if(inventory.getBarcode() != null)
			{
				criList+=inventory.getBarcode()+',';
			}
		}
		int ind = criList.lastIndexOf(",");
		criList = new StringBuilder(criList).replace(ind, ind+1,")").toString();
		//crilist.charAt(criList.length()-1)=')';
		return criList;
	}
	
	@SuppressWarnings("null")
	public RtvSheet getRtvSheetsFromInventory(Inventory inventory,String return_type)
	{
		RtvSheet rtvSheet = new RtvSheet();
		Address address;
		ReceiverDetail receiverDetail = new ReceiverDetail();;
		
		if(return_type.equals("3pl") || return_type.equals("liquidation")){
				    address =rtvService.get3plLiquidationDetail(return_type);
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
		else if(return_type.equals("customer"))
					{
						receiverDetail=dao.getCustomerAddressDetail("('"+inventory.getBarcode()+"')");
					}
		else
			receiverDetail = dao.getReceiverDetails(inventory.getSuborderCode(),inventory.getVendorCode(),return_type);
				
		if(receiverDetail != null)
				{
					rtvSheet.setReceiverDetail(receiverDetail);
					String returnType = "";
					if(return_type.equals("warehouse"))
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
							return_type = "liquidation";
						}
						else
							returnType = Constants.RETURN_TYPE_TO_CENTRE;
					else if(return_type.equals("vendor"))
						returnType = Constants.RETURN_TYPE_TO_VENDOR;
					else if(return_type.equals("3pl"))
						returnType = Constants.RETURN_TYPE_TO_3PL;
					else if(return_type.equals("liquidation"))
						returnType = Constants.RETURN_TYPE_TO_LIQUIDATION;
					else if(return_type.equals("customer"))
						returnType = Constants.RETURN_TYPE_TO_CUSTOMER;
					//
					AWBResult awbResult = courierService.getAWB(receiverDetail.getPincode(),inventory.getShippingMode(),returnType);
					rtvSheet.setAwbNumber(awbResult.getTrackingNumber());
					rtvSheet.setCourierCode(awbResult.getCourierCode());
				}else {
					rtvSheet.setReceiverDetail(null);
					rtvSheet.setAwbNumber(null);
					rtvSheet.setCourierCode(null);
				}
		List<Inventory> list = new ArrayList<Inventory>();
		list.add(inventory);
		
		rtvSheet.setProductDetails(list);
				
		/** RTV Initiated Message **/
		historyService.addInventoryHistoryByList(rtvSheet.getProductDetails(), Constants.HISTORY_ACTION_RTV);
				
		/** Add Courier Code and Tracking Number assigned by System in Inventory History **/
		historyService.addInventoryHistoryByList(rtvSheet.getProductDetails(),
						"System assigned Courier : "+rtvSheet.getCourierCode()+" with tracking number : "
						+rtvSheet.getAwbNumber());
				
		rtvSheet.setSellerName(inventory.getSellerName());
		rtvSheet.setVendorCode(inventory.getVendorCode());
		rtvSheet.setWarehouseDetails(sessionDetails.getSessionWarehouse());
		rtvSheet.setReturnType(return_type);
		rtvService.saveOrUpdateRtvSheet(rtvSheet);
			
				
		return rtvSheet;
	}
}
