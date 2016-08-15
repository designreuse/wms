package com.snapdeal.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.component.SessionDetails;
import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.Inventory;

@Named("searchService")
@Transactional
public class SearchServiceImpl implements SearchService {

	@Inject
	@Named("entityDao")
	EntityDao entityDao;
	
	@Inject
	@Named("sessionDetails")
	SessionDetails sessionDetails;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Inventory> searchByBarcode(List<String> barcode) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("SELECT inventory from Inventory inventory " +
		"JOIN inventory.warehouse warehouse where warehouse.id = :warehouseId and inventory.barcode IN (:barcode)");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("barcode",barcode);
		List<Inventory> inventories = (List<Inventory>)query.getResultList();
		return inventories;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Inventory> searchByLocation(List<String> location) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("SELECT inventory from Inventory inventory " +
		"JOIN inventory.warehouse warehouse where warehouse.id = :warehouseId and inventory.location IN (:location)");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("location",location);
		List<Inventory> inventories = (List<Inventory>)query.getResultList();
		return inventories;
	}

}
