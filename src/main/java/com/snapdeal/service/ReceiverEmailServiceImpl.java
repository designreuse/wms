package com.snapdeal.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.ReceiverEmail;

@Named("receiverEmailService")
@Transactional
public class ReceiverEmailServiceImpl implements ReceiverEmailService{
	
	@Inject
	@Named("entityDao")
	EntityDao entityDao;

	@SuppressWarnings("unchecked")
	@Override
	public ReceiverEmail getReceiver(String code) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select receiverEmail from ReceiverEmail receiverEmail where receiverEmail.code =:code");
		query.setParameter("code", code);
		query.setMaxResults(1);
		
		List<ReceiverEmail> receiverEmailList = (List<ReceiverEmail>) query.getResultList();
		
		ReceiverEmail receiverEmail = null;
		
		if(receiverEmailList.size() > 0){
			receiverEmail = receiverEmailList.get(0);
		}
		
		return receiverEmail;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void saveOrUpdate(ReceiverEmail receiverEmail) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select receiverEmail from ReceiverEmail receiverEmail where receiverEmail.code =:code");
		query.setParameter("code",receiverEmail.getCode());
		query.setMaxResults(1);
		
		List<ReceiverEmail> receiverEmailList = (List<ReceiverEmail>) query.getResultList();
		
		if(receiverEmailList.size() > 0){
			ReceiverEmail oldReceiverEmail = receiverEmailList.get(0);
			
			receiverEmail.setId(oldReceiverEmail.getId());
			receiverEmail.setCreated(oldReceiverEmail.getCreated());
			receiverEmail.setCreatedBy(oldReceiverEmail.getCreatedBy());
		}
		
		entityDao.saveOrUpdate(receiverEmail);
		
	}

	/** Check for the existing receiver in the database. **/
	@SuppressWarnings("unchecked")
	@Override
	public Boolean checkReceiever(String code) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select receiverEmail from ReceiverEmail receiverEmail where receiverEmail.code =:code");
		query.setParameter("code",code);
		query.setMaxResults(1);
		
		List<ReceiverEmail> receiverEmailList = (List<ReceiverEmail>) query.getResultList();
		
		/** If exists return true else false. **/
		if(receiverEmailList.size() > 0)
			return true;
		else
			return false;
	}

}
