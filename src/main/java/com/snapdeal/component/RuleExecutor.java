package com.snapdeal.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snapdeal.bean.ProductDetails;
import com.snapdeal.bean.RuleExecutionResult;
import com.snapdeal.dao.Dao;
import com.snapdeal.entity.Box;
import com.snapdeal.entity.BulkRule;
import com.snapdeal.entity.CcStatus;
import com.snapdeal.entity.Floor;
import com.snapdeal.entity.FulfillmentModel;
import com.snapdeal.entity.Group;
import com.snapdeal.entity.IssueCategory;
import com.snapdeal.entity.Price;
import com.snapdeal.entity.RtvCheckRule;
import com.snapdeal.entity.Rule;
import com.snapdeal.entity.Shelf;
import com.snapdeal.entity.SubCategory;
import com.snapdeal.service.GroupService;
import com.snapdeal.service.PutawayService;
import com.snapdeal.service.RuleService;

@Component
@Named("ruleExecutor")
public class RuleExecutor {

	@Inject
	@Named("ruleService")
	RuleService ruleService;

	@Inject
	@Named("groupService")
	GroupService groupService;

	@Autowired
	Dao dao;

	@Inject
	@Named("putawayService")
	PutawayService putawayService;

	public RuleExecutionResult checkRule(Rule rule, ProductDetails productDetails)
	{
		
		if(rule.getPrice() != null && rule.getPrice().getOperator() != null && !rule.getPrice().getOperator().isEmpty())
		{
			if(!checkPrice(rule.getPrice(), productDetails.getPrice()))
			{
				return null;
			}
		}
		if(rule.getManifestOperator() != null && rule.getDaysPassedManifest() != null && !rule.getManifestOperator().isEmpty())
		{
			if(!checkManifestTime(rule.getManifestOperator(), 
					rule.getDaysPassedManifest(), productDetails.getManifestDate()))
			{
				return null;
			}
		}
		if(rule.getCcStatusList() != null && rule.getCcStatusList().size() > 0)
		{
			List<String> ccStatusList = getCcStatusList(rule.getCcStatusList());
			if(!ccStatusList.contains(productDetails.getCcStatus()))
			{
				return null;
			}
		}
		if(rule.getFulfillmentModelList() != null && rule.getFulfillmentModelList().size() > 0)
		{
			List<String> fulfillmentModelList = getFulfillmentModelList(rule.getFulfillmentModelList());
			if(!fulfillmentModelList.contains(productDetails.getFulfillmentModel()))
			{
				return null;
			}
		}
		if(rule.getIssueCategoryList() != null && rule.getIssueCategoryList().size() > 0)
		{
			List<String> issueCategoryList = getIssueCategory(rule.getIssueCategoryList());
			if(!issueCategoryList.contains(productDetails.getIssueCategory()))
			{
				return null;
			}
		}
		if(rule.getSubCategoryList() != null && rule.getSubCategoryList().size() > 0)
		{
			List<String> subcategoryList = getSubCategories(rule.getSubCategoryList());
			if(!subcategoryList.contains(productDetails.getSubCategory()))
			{
				return null;
			}
		}
		if(rule.getQcRemarks() != null && !rule.getQcRemarks().isEmpty())
		{
			if(!productDetails.getQcRemarks().equalsIgnoreCase(rule.getQcRemarks()))
			{
				return null;
			}
		}
		RuleExecutionResult ruleExecutionResult = new RuleExecutionResult();
		if(!rule.getRtv())
		{
			ruleExecutionResult.setStatus(Constants.PUTAWAY_STATUS);
		}
		else {
			ruleExecutionResult.setStatus(Constants.RTV_STATUS);
		}
		ruleExecutionResult.setLocation(getLocation(productDetails.getSellerName(), rule.getGroup()));
		ruleExecutionResult.setGroupName(rule.getGroup().getName());
		ruleExecutionResult.setRuleId(rule.getId());
		return ruleExecutionResult;
	}

	public RuleExecutionResult getLocationForBulkRule(String sellerName, Long ruleId)
	{
		RuleExecutionResult ruleExecutionResult = new RuleExecutionResult();
		BulkRule rule = ruleService.findBulkRuleById(ruleId);
		if(!rule.getRtv())
		{
			ruleExecutionResult.setStatus(Constants.PUTAWAY_STATUS);
		}
		else {
			ruleExecutionResult.setStatus(Constants.RTV_STATUS);
		}
		ruleExecutionResult.setLocation(getLocation(sellerName, rule.getGroup()));
		ruleExecutionResult.setGroupName(rule.getGroup().getName());
		ruleExecutionResult.setRuleId(rule.getId());
		ruleExecutionResult.setBulkRule(true);
		return ruleExecutionResult;
	}

	public String getLocation(String sellerName, Group group)
	{
		String sellerInitial = null;
		if(sellerName != null)
		{
			if(Character.isDigit(sellerName.charAt(0)))
				sellerInitial = "X";
			else
				sellerInitial = sellerName.substring(0,1).toUpperCase();			
		}
		else {
			sellerInitial = "X";
		}
		String location = null;
		for(Shelf shelf:group.getShelfList())
		{
			List<String> initials =Arrays.asList(shelf.getSellerInitial().split(","));
			if(initials.contains(sellerInitial))
			{
				for(Floor floor : groupService.getFloorListByShelf(shelf.getId()))
				{
					for(Box box:floor.getBoxList())
					{
						if(box.getUsed() < box.getCapacity())
						{
							location = box.getName();
							break;
						}
					}
				}
			}
		}
		return location;
	}

	public Boolean checkManifestTime(String operator,Integer daysManifest, Date manifestDate)
	{
		Integer daysPassedManifest = getDaysPassedManifest(manifestDate);
		if(operator.equals("<"))
		{
			if(daysPassedManifest < daysManifest)
			{
				return true;
			}
			else{
				return false;
			}
		}
		else if(operator.equals(">"))
		{
			if(daysPassedManifest > daysManifest)
			{
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	public int getDaysPassedManifest(Date manifestDate) {
		DateTime dt1 = new DateTime(new Date());
		DateTime dt2 = new DateTime(manifestDate);
		
		Integer daysPassed = Days.daysBetween(dt2, dt1).getDays();
		
		/*Long millis = new Date().getTime() - manifestDate.getTime();
		int daysPassed = (int) (millis/(1000*60*60*24));*/
		return daysPassed;
	}

	public Boolean checkPrice(Price price,Long amount)
	{
		if(price.getOperator().equals("BETWEEN"))
		{
			String[] amounts = price.getValue().split(" AND ");
			long amount1 = Long.parseLong(amounts[0]);
			long amount2 = Long.parseLong(amounts[1]);
			if(amount1 - amount2 < 0)
			{
				if(amount >= amount1 && amount <= amount2)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				if(amount <= amount1 && amount >= amount2)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		else if(price.getOperator().equals("NOT BETWEEN"))
		{
			String[] amounts = price.getValue().split(" AND ");
			long amount1 = Long.parseLong(amounts[0]);
			long amount2 = Long.parseLong(amounts[1]);
			if(amount1 - amount2 < 0)
			{
				if(!(amount > amount1 && amount < amount2))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				if(!(amount < amount1 && amount > amount2))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		else if(price.getOperator().equals("<")){
			if(amount < Long.parseLong(price.getValue()))
			{
				return true;
			}
			else{
				return false;
			}
		}
		else if(price.getOperator().equals(">")){
			if(amount > Long.parseLong(price.getValue()))
			{
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}

	public List<String> getCcStatusList(List<CcStatus> ccStatusList)
	{
		List<String> statusList = new ArrayList<String>();
		for(CcStatus ccStatus : ccStatusList)
		{
			statusList.add(ccStatus.getCode());
		}
		return statusList;
	}

	public List<String> getFulfillmentModelList(List<FulfillmentModel> fulfillmentModels)
	{
		List<String> models = new ArrayList<String>();
		for(FulfillmentModel fulfillmentModel : fulfillmentModels)
		{
			models.add(fulfillmentModel.getName());
		}
		return models;
	}

	public List<String> getIssueCategory(List<IssueCategory> issueCategories)
	{
		List<String> issueCategoryList = new ArrayList<String>();
		for(IssueCategory issueCategory : issueCategories)
		{
			issueCategoryList.add(issueCategory.getCode());
		}
		return issueCategoryList;
	}

	public List<String> getSubCategories(List<SubCategory> subCategories)
	{
		List<String> subCategoryList = new ArrayList<String>();
		for(SubCategory subCategory : subCategories)
		{
			subCategoryList.add(subCategory.getUrl());
		}
		return subCategoryList;
	}

	public boolean showAlertOnRtv(List<RtvCheckRule> rtvCheckRules,String vendorCode, 
			Date manifestDate,String groupName)
	{
		List<Boolean> resultList = new ArrayList<Boolean>();
		for(RtvCheckRule rule : rtvCheckRules)
		{
			switch (rule.getPriority()) {
			case 1:
				if(getDaysPassedManifest(manifestDate) > rule.getValue())
				{
					resultList.add(true);
				}
				break;
			case 2:
				if(!putawayService.checkProductForExistingVendor(groupName, vendorCode))
				{
					resultList.add(true);
				}
				break;
			case 3:
				if(dao.getPickupInitiatedBySeller(vendorCode) <= rule.getValue())
				{
					resultList.add(true);
				}
			default:
				resultList.add(false);
			}
		}
		if(resultList.contains(true))
		{
			return true;
		}
		return false;
	}
}
