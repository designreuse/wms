package com.snapdeal.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.Centre3PL;
import com.snapdeal.entity.Warehouse;

@Transactional
@Named("centre3plService")
public class Centre3PLServiceImpl implements Centre3PLService{

	@Inject
	@Named("entityDao")
	EntityDao entityDao;
	
	@Inject
	@Named("warehouseService")
	WarehouseService warehouseService;

	@Override
	public List<Centre3PL> getAllCentre3PL() {
		List<Centre3PL> list=entityDao.findAll(Centre3PL.class);
		return list;
	}

	@Override
	public List<Centre3PL> getEnabledCentre3PL() {
		List<Centre3PL> list = entityDao.findAllEnabledObjects(Centre3PL.class);
		return list;
	}

	@Override
	public void saveOrUpdateCentre3PL(Centre3PL centre3pl, Long id) {
		
		Warehouse warehouse = warehouseService.findWarehouseByid(id);
		centre3pl.setWarehouse(warehouse);
		entityDao.saveOrUpdate(centre3pl);
	}

	@Override
	public Centre3PL findCentre3PLById(Long id) {
		Centre3PL centre3pl = entityDao.findById(Centre3PL.class, id);
		return centre3pl;
	}

	@Override
	public void enableCentre3PL(Long id) {
		
		Centre3PL persitedCentre3PL = entityDao.findById(Centre3PL.class, id);
		persitedCentre3PL.setEnabled(true);
		entityDao.saveOrUpdate(persitedCentre3PL);
	}

	@Override
	public void disableCentre3PL(Long id) {
		
		Centre3PL persitedCentre3PL = entityDao.findById(Centre3PL.class, id);
		persitedCentre3PL.setEnabled(false);
		entityDao.saveOrUpdate(persitedCentre3PL);
	}

	@Override
	public boolean checkName(String centre3plName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkCode(String centre3plCode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Centre3PL findCentre3PLByCode(String centre3plCode) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select centre3pl from Centre3PL centre3pl where centre3pl.code = :code");
		query.setParameter("code", centre3plCode);
		List<Centre3PL> centre3plList = (List<Centre3PL>)query.getResultList();
		if(centre3plList != null && centre3plList.size() > 0){
			return centre3plList.get(0);
		}
		else
			return null;
	}

}
