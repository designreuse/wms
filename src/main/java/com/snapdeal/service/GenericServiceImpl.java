package com.snapdeal.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.component.SessionDetails;
import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.BaseWarehouseEntity;

@Named("genericService")
@Transactional
public class GenericServiceImpl implements GenericService {

	@Inject
	@Named("entityDao")
	EntityDao entityDao;
	
	@Inject
	@Named("sessionDetails")
	SessionDetails sessionDetails;
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseWarehouseEntity> boolean checkName(Class<T> objectClass,
			String name) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select data.name from "+objectClass.getName()+" data " +
				"JOIN data.warehouse warehouse where warehouse.id = :warehouseId and data.name = :name");
		query.setParameter("name", name);
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<String> results = (List<String>) query.getResultList();
		if(results.size() > 0)
		{
			return false;	
		}
		else
		{
			return true;
		}
	}
}
