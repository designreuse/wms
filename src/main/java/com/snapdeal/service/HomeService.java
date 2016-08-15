package com.snapdeal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snapdeal.entity.GraphDailyNumber;
import com.snapdeal.entity.GraphPickupPending;
import com.snapdeal.entity.GraphPutawayBreakup;
import com.snapdeal.entity.GraphRprQc;
import com.snapdeal.entity.GraphTotalDispatch;
import com.snapdeal.entity.Warehouse;

@Service
public interface HomeService {
	
	public List<GraphPickupPending> getPickupPending(Warehouse warehouse);
	
	public List<GraphPutawayBreakup> getPutawayBreakup(Warehouse warehouse);
	
	public List<GraphRprQc> getRprToQc(Warehouse warehouse);
	
	public List<GraphTotalDispatch> getTotalDispatch(Warehouse warehouse);
	
	public List<GraphDailyNumber> getDailyNumber(Warehouse warehouse);

}
