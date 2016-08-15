package com.snapdeal.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.Address;
import com.snapdeal.entity.Liquidation;
import com.snapdeal.entity.Warehouse;

@Transactional
@Named("liquidationService")
public class LiquidationServiceImpl implements LiquidationService{

	@Inject
	@Named("entityDao")
	EntityDao entityDao;
	
	@Inject
	@Named("warehouseService")
	WarehouseService warehouseService;

	@Override
	public void saveOrUpdateLiquidation(Liquidation liquidation,Long warehouseId)
	{
		Warehouse warehouse = warehouseService.findWarehouseByid(warehouseId);
		liquidation.setWarehouse(warehouse);	
		entityDao.saveOrUpdate(liquidation);	
	}

	@Override
	public List<Liquidation> getAllLiquidation() {
		
		List<Liquidation> list=entityDao.findAll(Liquidation.class);
		return list;
	}

	@Override
	public Liquidation findLiquidationById(Long id) 
	{
		Liquidation liquidation = entityDao.findById(Liquidation.class, id);
		return liquidation;
	}

	@Override
	public List<Liquidation> getEnabledLiquidation() {
		List<Liquidation> list = entityDao.findAllEnabledObjects(Liquidation.class);
		return list;
	}

	@Override
	public void enableLiquidation(Long id) {
		
		Liquidation persitedLiquidation = entityDao.findById(Liquidation.class, id);
		persitedLiquidation.setEnabled(true);
		entityDao.saveOrUpdate(persitedLiquidation);
	}

	@Override
	public void disableLiquidation(Long id) {
		Liquidation persitedLiquidation = entityDao.findById(Liquidation.class, id);
		persitedLiquidation.setEnabled(false);
		entityDao.saveOrUpdate(persitedLiquidation);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean checkName(String liquidationName) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select liquidation.name from Liquidation liquidation where " +
		"liquidation.name = :name");
		query.setParameter("name", liquidationName);
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
	@SuppressWarnings("unchecked")
	public boolean checkCode(String liquidationCode) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select liquidation.code from Liquidation liquidation where " +
		"liquidation.code = :code");
		query.setParameter("code", liquidationCode);
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
	@SuppressWarnings("unchecked")
	public Liquidation findLiquidationByCode(String liquidationCode) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select liquidation from Liquidation liquidation where liquidation.code = :code");
		query.setParameter("code", liquidationCode);
		List<Liquidation> liquidationList = (List<Liquidation>)query.getResultList();
		if(liquidationList != null && liquidationList.size() > 0){
			return liquidationList.get(0);
		}
		else
			return null;
	}
}
