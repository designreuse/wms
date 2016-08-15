package com.snapdeal.controller;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.snapdeal.component.SessionDetails;
import com.snapdeal.entity.GraphDailyNumber;
import com.snapdeal.entity.GraphPickupPending;
import com.snapdeal.entity.GraphPutawayBreakup;
import com.snapdeal.entity.GraphRprQc;
import com.snapdeal.entity.GraphTotalDispatch;
import com.snapdeal.entity.Warehouse;
import com.snapdeal.service.HomeService;
/** This controller is called after user authentication to redirect user to its home page **/

@Controller
public class HomeController {
	
	@Inject
	@Named("sessionDetails")
	SessionDetails sessionDetails;
	
	@Inject
	@Named("HomeService")
	HomeService homeService;
	
	
	
	
	/** Redirects to home page **/
	@RequestMapping("/home")
	public String getHome(@RequestParam(value="message",required=false) String message,ModelMap map)
	{
		if(message != null)
		{
			map.put("message", message);
		}
		
		
		Warehouse warehouse = sessionDetails.getSessionWarehouse();
		List<GraphPickupPending> pickupGraph = homeService.getPickupPending(warehouse);
		if(pickupGraph.size()>0){
			map.put("pickupGraph", pickupGraph);
			map.put("warehouse", warehouse);
		}
		else{
			map.put("graphPickup", "Graph not available - Pickup Pending for RPR");
			map.put("warehouse", warehouse);
		}
		
		List<GraphPutawayBreakup> putawayBreakup = homeService.getPutawayBreakup(warehouse);
		if(putawayBreakup.size()>0){
			System.out.println("putaeawawaw"+putawayBreakup.get(0).getPutaway());
			map.put("putawayBreakup", putawayBreakup);
			map.put("warehouse", warehouse);
		}
		else{
			map.put("graphPutaway", "Graph not available - Putaway Breakup");
			map.put("warehouse", warehouse);
		}
		
		List<GraphRprQc> rpr = homeService.getRprToQc(warehouse);
		if(rpr.size()>0){
			System.out.println("daiiiiiiiiii"+rpr.get(0).getHrs12());
			map.put("rpr", rpr);
			map.put("warehouse", warehouse);
		}
		else{
			map.put("graphRpr", "Graph not available - RPR to QC");
			map.put("warehouse", warehouse);
		}
		
		List<GraphTotalDispatch> td = homeService.getTotalDispatch(warehouse);
		if(td.size()>0){
			map.put("td", td);
			map.put("warehouse", warehouse);
		}
		else{
			map.put("graphTd", "Graph not available - Total Dispatch");
			map.put("warehouse", warehouse);
		}
		
		List<GraphDailyNumber> daily = homeService.getDailyNumber(warehouse);
		if(daily.size()>0){
			map.put("dailyNumber", daily);
			map.put("warehouse", warehouse);
		}
		else{
			map.put("graphDaily", "Graph not available - Daily Activity");
			map.put("warehouse", warehouse);
		}
		
		
		return "home";
	}
	
	/** Redirects to access denied page **/
	@RequestMapping("/error")
	public String getErrorPage()
	{
		return "error";
	}

}
