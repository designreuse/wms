package com.snapdeal.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.BulkRule;

@Named("bulkruleService")
@Transactional
public class BulkRuleServiceImpl implements BulkRuleService {

	@Inject
	@Named("entityDao")
	EntityDao entityDao;
	
	@Override
	@SuppressWarnings("unchecked")
	public BulkRule getBulkRulebyGroupId(Long groupId) {
	
		EntityManager entityManager =  entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select bulkrule from BulkRule bulkrule where bulkrule.group.id = :groupid ");
		query.setParameter("groupid", groupId);
		List<BulkRule> ruleList  = query.getResultList();
		if(!ruleList.isEmpty())
		{
			return ruleList.get(0);
		}
		else
			return null;
	}

}
