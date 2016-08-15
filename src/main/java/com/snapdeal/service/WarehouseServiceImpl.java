package com.snapdeal.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.Warehouse;
import com.snapdeal.entity.WarehouseBoy;

@Named("warehouseService")
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

	@Inject
	@Named("entityDao")
	EntityDao entityDao;

	@Override
	public Long saveOrUpdateWarehouse(Warehouse warehouse) {
		entityDao.saveOrUpdate(warehouse);
		return warehouse.getId();
	}

	@Override
	public void enableWarehouse(Long id) {
		Warehouse persitedWarehouse = entityDao.findById(Warehouse.class, id);
		persitedWarehouse.setEnabled(true);
		entityDao.saveOrUpdate(persitedWarehouse);
	}

	@Override
	public void disableWarehouse(Long id) {
		Warehouse persitedWarehouse = entityDao.findById(Warehouse.class, id);
		persitedWarehouse.setEnabled(false);
		entityDao.saveOrUpdate(persitedWarehouse);
	}

	@Override
	public Warehouse findWarehouseByid(Long id) {
		Warehouse warehouse = entityDao.findById(Warehouse.class, id);
		return warehouse;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public Warehouse findWarehouseByCode(String warehouseCode) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select warehouse from Warehouse warehouse where warehouse.code = :code");
		query.setParameter("code", warehouseCode);
		List<Warehouse> warehouseList = (List<Warehouse>)query.getResultList();
		if(warehouseList != null && warehouseList.size() > 0){
			return warehouseList.get(0);
		}
		else
			return null;
	}

	@Override
	public List<Warehouse> getAllWarehouse() {
		List<Warehouse> warehouses = entityDao.findAll(Warehouse.class);
		return warehouses;
	}

	@Override
	public List<Warehouse> getEnabledWarehouses() {
		return entityDao.findAllEnabledObjects(Warehouse.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean checkName(String warehouseName) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select warehouse.name from Warehouse warehouse where " +
		"warehouse.name = :name");
		query.setParameter("name", warehouseName);
		List<String> nameList = (List<String>)query.getResultList();
		if(nameList != null && nameList.size() > 0)
		{
			return false;	
		}
		else {
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean checkCode(String warehouseCode) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select warehouse.code from Warehouse warehouse where " +
		"warehouse.code = :code");
		query.setParameter("code", warehouseCode);
		List<String> nameList = (List<String>)query.getResultList();
		if(nameList != null && nameList.size() > 0)
		{
			return false;	
		}
		else {
			return true;
		}
	}

	@Override
	public void saveOrupdateWarehouseBoy(WarehouseBoy warehouseBoy) {
		entityDao.saveOrUpdateByWarehouse(warehouseBoy);
	}

	@Override
	public List<WarehouseBoy> getAllWarehouseBoy() {
		return entityDao.findAllByWarehouse(WarehouseBoy.class);
	}

	@Override
	public WarehouseBoy findWarehouseBoyById(Long id) {
		return entityDao.findByWarehouseById(WarehouseBoy.class, id);
	}

	@Override
	public void removeWarehouseBoy(Long id) {
		WarehouseBoy warehouseBoy = entityDao.findByWarehouseById(WarehouseBoy.class, id);
		entityDao.delete(warehouseBoy);
	}

}
