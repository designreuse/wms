package com.snapdeal.dao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.snapdeal.component.SessionDetails;
import com.snapdeal.entity.GraphDailyNumber;
import com.snapdeal.entity.GraphDetails;
import com.snapdeal.entity.GraphPickupPending;
import com.snapdeal.entity.GraphPutawayBreakup;
import com.snapdeal.entity.GraphRprQc;
import com.snapdeal.entity.GraphTotalDispatch;
import com.snapdeal.entity.User;
import com.snapdeal.entity.Warehouse;

@Component
public class HomeDao {

	
	
	@Autowired
	EntityDao entityDao;
	public List<GraphPickupPending> getPickupPendings(Warehouse warehouse){
		
		
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = null;
		//if(check.get(0).getActive() == 0){
		query = entityManager.createQuery("Select gpp from GraphPickupPending gpp where gpp.warehouse =:warehouse");
		query.setParameter("warehouse", warehouse.getCode());
		
		@SuppressWarnings("unchecked")
		List<GraphPickupPending> result=query.getResultList();
		
		return result;
		//}
		
}
	public List<GraphPutawayBreakup> getPutawayBreakup(Warehouse warehouse){
		
		
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = null;

		query = entityManager.createQuery("Select gpb from GraphPutawayBreakup gpb where gpb.wareCode =:warehouse");
		query.setParameter("warehouse", warehouse.getCode());
		
		@SuppressWarnings("unchecked")
		List<GraphPutawayBreakup> result=query.getResultList();
		
		return result;
	
}
	public List<GraphRprQc> getRprToQc(Warehouse warehouse){
				
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = null;
		query = entityManager.createQuery("Select grq from GraphRprQc grq where grq.warehouse =:warehouse");
		query.setParameter("warehouse", warehouse.getCode());
		
		@SuppressWarnings("unchecked")
		List<GraphRprQc> result=query.getResultList();
		
		return result;
	
		
}
	public List<GraphTotalDispatch> getTotalDispatch(Warehouse warehouse){
		
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = null;
		query = entityManager.createQuery("Select gtd from GraphTotalDispatch gtd where gtd.warehouse =:warehouse");
		query.setParameter("warehouse", warehouse.getCode());
	
		@SuppressWarnings("unchecked")
		List<GraphTotalDispatch> result=query.getResultList();
		
		return result;
	
		
}
	public List<GraphDailyNumber> getDailyNumber(Warehouse warehouse){
		
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = null;
		query = entityManager.createQuery("Select gdn from GraphDailyNumber gdn where gdn.warehouse =:warehouse");
		query.setParameter("warehouse", warehouse.getCode());
		
		@SuppressWarnings("unchecked")
		List<GraphDailyNumber> result=query.getResultList();
		
		return result;
}

}

