package com.snapdeal.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.Reports;

/** Service Implementation for courier.**/
@Named("reportsService")
@Transactional
public class ReportsServiceImpl implements ReportsService{
	
	@Inject
	@Named("entityDao")
	EntityDao entityDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getReportsName(){
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select reports.name from Reports reports where reports.name != null");
		
		List<String> reportsName = (List<String>) query.getResultList();
		
		return reportsName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getEmailAddress(String name){
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select reports.toEmail from Reports reports where reports.name =:name");
		query.setParameter("name", name);
		query.setMaxResults(1);
		
		List<String> toEmailList = (List<String>) query.getResultList();
		
		String toEmail = null;
		
		if(toEmailList.size() > 0){
			toEmail = toEmailList.get(0);
		}
		
		return toEmail;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void saveOrUpdate(Reports reports){
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select reports from Reports reports where reports.name =:name");
		query.setParameter("name",reports.getName());
		query.setMaxResults(1);
		
		List<Reports> reportsList = (List<Reports>) query.getResultList();
		
		if(reportsList.size() > 0){
			Reports oldReports = reportsList.get(0);
			
			reports.setId(oldReports.getId());
			reports.setCreated(oldReports.getCreated());
			reports.setCreatedBy(oldReports.getCreatedBy());
		}
		
		entityDao.saveOrUpdate(reports);
	}
	
	/** Check for the existing report name in the database. **/
	@SuppressWarnings("unchecked")
	@Override
	public Boolean checkReportName(String name){
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select reports from Reports reports where reports.name =:name");
		query.setParameter("name", name);
		query.setMaxResults(1);
		
		List<Reports> reportsList = (List<Reports>)query.getResultList();
		
		/** If exists return true else false. **/
		if(reportsList.size() > 0)
			return true;
		else
			return false;
	}
}
