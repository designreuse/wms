package com.snapdeal.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.snapdeal.entity.BaseEntity;
import com.snapdeal.entity.BaseWarehouseEntity;

@Repository
public interface EntityDao{
	public <T extends BaseEntity> void save(T object);
	public <T extends BaseEntity> void saveOrUpdate(T object);
	public <T extends BaseWarehouseEntity> void saveOrUpdateByWarehouse(T object);
	public <T extends BaseEntity> void delete(T object);
	public <T extends BaseEntity> T findById(Class<T> objectClass,Long id);
	public <T extends BaseWarehouseEntity> T findByWarehouseById(Class<T> objectClass,Long id);
	public <T extends BaseEntity> List<T> findAll(Class<T> objectClass);
	public <T extends BaseWarehouseEntity> List<T> findAllByWarehouse(Class<T> objectClass);
	public <T extends BaseEntity> List<T> findAllEnabledObjects(Class<T> objectClass);
	public <T extends BaseWarehouseEntity> List<T> findAllEnabledByWarehouse(Class<T> objectClass);
	public <T extends BaseEntity> Long getMaxId(Class<T> objectClass);
	public void flush();
	public void clear();
	public EntityManager getEntityManager();
}
