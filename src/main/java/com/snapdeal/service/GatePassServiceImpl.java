package com.snapdeal.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.component.SessionDetails;
import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.GatePass;
import com.snapdeal.entity.GatePassStatus;

@Named("gatePassService")
@Transactional
public class GatePassServiceImpl implements GatePassService {

	@Inject
	@Named("entityDao")
	EntityDao entityDao;
	
	@Inject
	@Named("sessionDetails")
	SessionDetails sessionDetails;
	
	@SuppressWarnings("unchecked")
	@Override
	public GatePass findGatePassByBarcode(String barcode) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select pass from GatePass pass JOIN " +
		"pass.warehouse warehouse where warehouse.id = :warehouseId and pass.barcode = :barcode");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("barcode", barcode);
		List<GatePass> gatePassList = (List<GatePass>)query.getResultList();
		if(gatePassList != null && gatePassList.size() > 0)
		{
			return gatePassList.get(0);
		}
		return null;
	}

	@Override
	public void saveOrUpdateGatePass(GatePass gatePass) {
		entityDao.saveOrUpdateByWarehouse(gatePass);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getValidGatePassStatus() {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select gpstatus.status from GatePassStatus gpstatus");
		List<String> gatePassStatusList = (List<String>)query.getResultList();
		return gatePassStatusList;
	}

	@Override
	public List<GatePassStatus> getAllLoadedStatus() {
		List<GatePassStatus> gatePassStatusList = entityDao.findAll(GatePassStatus.class);
		List<GatePassStatus> loadedList = null;
		if(gatePassStatusList != null && gatePassStatusList.size() > 0)
		{
			loadedList = new ArrayList<GatePassStatus>();
			for(GatePassStatus gatePassStatus : gatePassStatusList)
			{
				Hibernate.initialize(gatePassStatus.getCreatedBy());
				Hibernate.initialize(gatePassStatus.getUpdatedBy());
				loadedList.add(gatePassStatus);
			}
		}
		return loadedList;
	}

	@Override
	public GatePassStatus findGatePassStatusByid(Long id) {
		return entityDao.findById(GatePassStatus.class, id);
	}

	@Override
	public void saveOrUpdateGatePassStatus(GatePassStatus gatePassStatus) {
		entityDao.saveOrUpdate(gatePassStatus);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean checkStatus(String status) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select gpstatus.status from GatePassStatus gpstatus where " +
		"gpstatus.status = :status");
		query.setParameter("status", status);
		List<String> nameList = (List<String>)query.getResultList();
		if(nameList != null && nameList.size() > 0)
		{
			return false;	
		}
		else {
			return true;
		}
	}

}
