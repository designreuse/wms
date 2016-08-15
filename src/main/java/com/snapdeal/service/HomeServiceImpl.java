package com.snapdeal.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.dao.HomeDao;
import com.snapdeal.entity.GraphDailyNumber;
import com.snapdeal.entity.GraphPickupPending;
import com.snapdeal.entity.GraphPutawayBreakup;
import com.snapdeal.entity.GraphRprQc;
import com.snapdeal.entity.GraphTotalDispatch;
import com.snapdeal.entity.Warehouse;

@Transactional
@Named("HomeService")
public class HomeServiceImpl implements HomeService{
	
	@Inject
	HomeDao homeDao;
	
	public List<GraphPickupPending> getPickupPending(Warehouse warehouse) {
		return homeDao.getPickupPendings(warehouse);
		
	}
	
	public List<GraphPutawayBreakup> getPutawayBreakup(Warehouse warehouse) {
		return homeDao.getPutawayBreakup(warehouse);
		
	}
	public List<GraphRprQc> getRprToQc(Warehouse warehouse) {
		return homeDao.getRprToQc(warehouse);
		
	}
	
	public List<GraphTotalDispatch> getTotalDispatch(Warehouse warehouse){
		return homeDao.getTotalDispatch(warehouse);
	}
	
	public List<GraphDailyNumber> getDailyNumber(Warehouse warehouse) {
		return homeDao.getDailyNumber(warehouse);
		
	}

}
