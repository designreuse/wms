package com.snapdeal.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snapdeal.bean.HistoryDetail;
import com.snapdeal.entity.Inventory;
import com.snapdeal.entity.InventoryHistory;
import com.snapdeal.service.HistoryService;
import com.snapdeal.service.SearchService;

@Controller
@RequestMapping("/Search")
public class SearchController {

	@Inject
	@Named("searchService")
	SearchService searchService;
	
	@Inject
	@Named("historyService")
	HistoryService historyService;
	
	@RequestMapping("/inventory")
	public String getResults(@RequestParam("searchType") String type,@RequestParam("code") String code,ModelMap map)
	{
		List<Inventory> inventoryList = null;
		List<String> codeList = null;
		if(code != null)
		{
			codeList = Arrays.asList(code.split(","));
		}
		if(type.equalsIgnoreCase("barcode"))
		{
			inventoryList = searchService.searchByBarcode(codeList);
		}else {
			inventoryList = searchService.searchByLocation(codeList);
		}
		if(inventoryList != null && inventoryList.size() > 0)
		{
			map.put("list", inventoryList);
		}else {
			map.put("message", "No data found for given input.");
		}
		return "Search/result";
	}
	
	@RequestMapping("/history")
	public @ResponseBody List<HistoryDetail> gethistory(@RequestParam("barcode") String barcode)
	{
		List<InventoryHistory> inventoryHistories = historyService.findHistoryByBarcode(barcode);
		List<HistoryDetail> historyDetailList = null;
		if(inventoryHistories != null && inventoryHistories.size() > 0)
		{
			DateFormat dateFormat = new SimpleDateFormat("MMMMM dd, yyyy hh:mm:ss a");
			historyDetailList = new ArrayList<HistoryDetail>();
			for(InventoryHistory inventoryHistory : inventoryHistories)
			{
				HistoryDetail historyDetail = new HistoryDetail();
				historyDetail.setBarcode(inventoryHistory.getBarcode());
				historyDetail.setAction(inventoryHistory.getAction());
				historyDetail.setDate(dateFormat.format(inventoryHistory.getCreated()));
				historyDetailList.add(historyDetail);
			}
		}
		return historyDetailList;
	}
}
