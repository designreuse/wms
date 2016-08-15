package com.snapdeal.dao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.snapdeal.component.SessionDetails;
import com.snapdeal.entity.BaseEntity;
import com.snapdeal.entity.BaseWarehouseEntity;

@Named("entityDao")
public class EntityDaoImpl implements EntityDao{

	@PersistenceContext
	EntityManager entityManager;

	@Inject
	@Named("sessionDetails")
	SessionDetails sessionDetails;
	
	@Override
	public <T extends BaseEntity> void save(T object) {
		entityManager.persist(object);	
	}

	@Override
	public <T extends BaseEntity> void saveOrUpdate(T object) {
		if(object.getId() != null)
		{
			entityManager.merge(object);
		}
		else {
			entityManager.persist(object);	
		}
	}

	@Override
	public <T extends BaseEntity> void delete(T object) {
		entityManager.remove(object);
	}

	@Override
	public void flush(){
		entityManager.flush();
	}

	@Override
	public void clear(){
		entityManager.clear();
	}

	@Override
	public <T extends BaseEntity> T findById(Class<T> objectClass, Long id) {
		T object = null;
		object = entityManager.find(objectClass, id);
		return object;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseWarehouseEntity> T findByWarehouseById(Class<T> objectClass, Long id) {
		Query query = entityManager.createQuery("Select data from "+objectClass.getName()+" data JOIN " +
		"data.warehouse warehouse where warehouse.id = :warehouseId and data.id = :id");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("id", id);
		List<T> object = (List<T>)query.getResultList();
		if(object != null && object.size() > 0)
		{
			return object.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> List<T> findAll(Class<T> objectClass) {
		Query query = entityManager.createQuery("Select data from "+objectClass.getName()+" data");
		List<T> objectList = (List<T>)query.getResultList();
		return objectList;
	}

	@Override
	public <T extends BaseEntity> Long getMaxId(Class<T> objectClass){
		Query query = entityManager.createQuery("Select max(data.id) from " + objectClass.getName()+" data");
		Long id = (Long)query.getSingleResult();
		return id;
	}

	@Override
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> List<T> findAllEnabledObjects(
			Class<T> objectClass) {
		Query query = entityManager.createQuery("Select data from "+objectClass.getName()+" data where data.enabled = true");
		List<T> objectList = (List<T>)query.getResultList();
		return objectList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseWarehouseEntity> List<T> findAllEnabledByWarehouse(
			Class<T> objectClass) {
		Query query = entityManager.createQuery("Select data from "+objectClass.getName()+" data JOIN " +
		"data.warehouse warehouse where warehouse.id = :warehouseId and data.enabled = true");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<T> objectList = (List<T>)query.getResultList();
		return objectList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseWarehouseEntity> List<T> findAllByWarehouse(
			Class<T> objectClass) {
		Query query = entityManager.createQuery("Select data from "+objectClass.getName()+
		" data JOIN data.warehouse warehouse where warehouse.id = :warehouseId");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<T> objectList = (List<T>)query.getResultList();
		return objectList;
	}

	@Override
	public <T extends BaseWarehouseEntity> void saveOrUpdateByWarehouse(T object) {
		if(object.getId() != null)
		{
			entityManager.merge(object);
		}
		else {
			entityManager.persist(object);
		}
	}
}
