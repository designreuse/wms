package com.snapdeal.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.Property;

@Named("propertyService")
@Transactional
public class PropertyServiceImpl implements PropertyService{
	
	@Inject
	@Named("entityDao")
	EntityDao entityDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getPropertyName(){
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select property.name from Property property where property.name != null");
		
		List<String> propertyName = (List<String>) query.getResultList();
		
		return propertyName;
	}
	
	/** Check for the existing report name in the database. **/
	@SuppressWarnings("unchecked")
	@Override
	public Boolean checkPropertyName(String name){
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select property from Property property where property.name =:name");
		query.setParameter("name", name);
		query.setMaxResults(1);
		
		List<Property> propertyList = (List<Property>)query.getResultList();
		
		/** If exists return true else false. **/
		if(propertyList.size() > 0)
			return true;
		else
			return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String getValue(String name){
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select property.value from Property property where property.name =:name");
		query.setParameter("name", name);
		query.setMaxResults(1);
		
		List<String> valueList = (List<String>) query.getResultList();
		
		String value = null;
		
		if(valueList.size() > 0){
			value = valueList.get(0);
		}
		
		return value;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void saveOrUpdate(Property property){
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select property from Property property where property.name =:name");
		query.setParameter("name",property.getName());
		query.setMaxResults(1);
		
		List<Property> propertyList = (List<Property>) query.getResultList();
		
		if(propertyList.size() > 0){
			Property oldProperty = propertyList.get(0);
			
			property.setId(oldProperty.getId());
			property.setCreated(oldProperty.getCreated());
			property.setCreatedBy(oldProperty.getCreatedBy());
		}
		
		entityDao.saveOrUpdate(property);
	}
}
