package com.snapdeal.component;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.Property;

@Component
@Named("closedFC")
public class ClosedFC {
	
	@Inject
	@Named("entityDao")
	EntityDao entityDao;
	
	@SuppressWarnings("unchecked")
	public boolean checkClosedFC(String warehouseName)
	{
		boolean result = false;
		
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select pr from Property pr where pr.name = 'closed_fc'");
		List<Property> propertyList = query.getResultList();
		
		String[] closedFC = propertyList.get(0).getValue().split(",");
		
		for(String fc : closedFC)
		{
			if(fc.equalsIgnoreCase(warehouseName))
			{
					result = true;
					break;
			}
		}
		return result;
	}
}
