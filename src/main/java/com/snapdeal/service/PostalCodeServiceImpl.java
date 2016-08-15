package com.snapdeal.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.PostalCode;

/** Service Implementation for postal code **/
@Named("postalCodeService")
@Transactional
public class PostalCodeServiceImpl implements PostalCodeService{
	@Inject
	@Named("entityDao")
	EntityDao entityDao;

	/** Returns the city list from postal code. **/
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getCity() {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select distinct postalCode.city from PostalCode postalCode order by postalCode.city");
		List<String> cityList = (List<String>)query.getResultList();
		return cityList;
	}

	/** Returns the state list from postal code. **/
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getState() {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select distinct postalCode.state from PostalCode postalCode order by postalCode.state");
		List<String> stateList = (List<String>)query.getResultList();
		return stateList;
	}

	/** Returns the postalCode(contains city and State) corresponding to pincode from database. **/
	@SuppressWarnings("unchecked")
	@Override
	public PostalCode getPostalCode(String pincode) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select postalCode from PostalCode postalCode where postalCode.pincode =:pincode");
		query.setParameter("pincode", pincode);
		List<PostalCode> postalCode = (List<PostalCode>)query.getResultList();
		if(postalCode.size() > 0)
			return postalCode.get(0);
		else
			return null;
	}

}
