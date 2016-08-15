package com.snapdeal.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import com.snapdeal.bean.CcStatusDto;
import com.snapdeal.bean.IssueCategoryDto;
import com.snapdeal.bean.SubCategoryDto;
import com.snapdeal.component.SessionDetails;
import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.BulkRule;
import com.snapdeal.entity.CcStatus;
import com.snapdeal.entity.FulfillmentModel;
import com.snapdeal.entity.IssueCategory;
import com.snapdeal.entity.RtvCheckRule;
import com.snapdeal.entity.Rule;
import com.snapdeal.entity.SubCategory;

@Named("ruleService")
@Transactional
public class RuleServiceImpl implements RuleService {

	@Inject
	@Named("entityDao")
	EntityDao entityDao;

	@Inject
	@Named("userService")
	UserService userService;

	@Inject
	@Named("sessionDetails")
	SessionDetails sessionDetails;

	private List<String> removedList;
	
	@Override
	public void saveIssueCategory(List<IssueCategoryDto> issueCategoryList) {
		for(IssueCategoryDto issueCategoryDto:issueCategoryList)
		{
			IssueCategory issueCategory = new IssueCategory();
			issueCategory.setCode(issueCategoryDto.getCode());
			issueCategory.setDescription(issueCategoryDto.getDescription());
			entityDao.saveOrUpdate(issueCategory);
		}
	}

	@Override
	public void saveCcStatus(List<CcStatusDto> ccStatusList) {
		for(CcStatusDto ccStatusDto:ccStatusList)
		{
			CcStatus ccStatus = new CcStatus();
			ccStatus.setCode(ccStatusDto.getCode());
			ccStatus.setDescription(ccStatusDto.getDescription());
			entityDao.saveOrUpdate(ccStatus);
		}
	}

	@Override
	public void saveSubCategory(List<SubCategoryDto> subCategoryList) {
		for(SubCategoryDto subCategoryDto:subCategoryList)
		{
			if(checkSubCategory(subCategoryDto.getSubcategoryName()) == null)
			{
				SubCategory subCategory = new SubCategory();
				subCategory.setName(subCategoryDto.getSubcategoryName());
				subCategory.setUrl(subCategoryDto.getSubcategoryUrl());
				entityDao.saveOrUpdate(subCategory);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public SubCategory checkSubCategory(String name)
	{
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select subcat from SubCategory subcat where subcat.name = :name");
		query.setParameter("name", name);
		List<SubCategory> list  = (List<SubCategory>) query.getResultList();
		if(list != null && list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	@Override
	public void saveFulfillmentModel(List<FulfillmentModel> fulfillmentModelList) {
		for(FulfillmentModel fulfillmentModel:fulfillmentModelList)
		{
			entityDao.saveOrUpdate(fulfillmentModel);
		}
	}

	@Override
	public List<IssueCategory> getIssueCategories() {
		List<IssueCategory> issueCategories = entityDao.findAll(IssueCategory.class);
		return issueCategories;
	}

	@Override
	public List<CcStatus> getCcStatus() {
		List<CcStatus> ccStatusList = entityDao.findAll(CcStatus.class);
		return ccStatusList;
	}

	@Override
	public List<SubCategory> getSubCategories() {
		List<SubCategory> subCategories = entityDao.findAll(SubCategory.class);
		return subCategories;
	}

	@Override
	public List<FulfillmentModel> getFulfillmentModels() {
		List<FulfillmentModel> fulfillmentModels = entityDao.findAll(FulfillmentModel.class);
		return fulfillmentModels;
	}

	@Override
	public Long saveOrUpdateRule(Rule rule) {
		entityDao.saveOrUpdateByWarehouse(rule);
		return rule.getId();
	}

	@Override
	public Rule findRuleById(Long id) {
		Rule rule = entityDao.findByWarehouseById(Rule.class, id);
		
		Hibernate.initialize(rule.getCcStatusList());
		Hibernate.initialize(rule.getIssueCategoryList());
		Hibernate.initialize(rule.getFulfillmentModelList());
		Hibernate.initialize(rule.getSubCategoryList());
		
		return rule;
	}

	public List<Rule> getRules()
	{
		List<Rule> ruleList = (List<Rule>) entityDao.findAllByWarehouse(Rule.class);
		return ruleList;
	}
	
	@Override
	public List<Rule> getActiveRules() {
		List<Rule> ruleList = (List<Rule>) entityDao.findAllEnabledByWarehouse(Rule.class);
		return ruleList;
	}

	public void updateRule(Rule rule)
	{
		rule.onUpdate();
		entityDao.saveOrUpdateByWarehouse(rule);
	}

	@Override
	public Rule findLoadedRuleById(Long id) {
		Rule rule = entityDao.findByWarehouseById(Rule.class, id);
		Hibernate.initialize(rule.getCcStatusList());
		Hibernate.initialize(rule.getIssueCategoryList());
		Hibernate.initialize(rule.getFulfillmentModelList());
		Hibernate.initialize(rule.getSubCategoryList());
		return rule;
	}

	public void disableRule(Long id)
	{
		Rule rule = entityDao.findByWarehouseById(Rule.class, id);
		rule.setEnabled(false);
		entityDao.saveOrUpdateByWarehouse(rule);
	}

	@Override
	public void enableRule(Long id) {
		Rule rule = entityDao.findByWarehouseById(Rule.class, id);
		rule.setEnabled(true);
		entityDao.saveOrUpdateByWarehouse(rule);
	}

	@Override
	public Long saveOrUpdateBulkRule(BulkRule bulkRule) {
		if(bulkRule.getId() != null)
		{
			bulkRule.onUpdate();
		}
		entityDao.saveOrUpdateByWarehouse(bulkRule);
		return bulkRule.getId();
	}

	@Override
	public BulkRule findBulkRuleById(Long id) {
		BulkRule bulkRule = entityDao.findByWarehouseById(BulkRule.class, id);
		return bulkRule;
	}

	@Override
	public List<BulkRule> getBulkRules() {
		List<BulkRule> ruleList = (List<BulkRule>) entityDao.findAllByWarehouse(BulkRule.class);
		return ruleList;
	}

	@Override
	public void disableBulkRule(Long id) {
		BulkRule rule = entityDao.findByWarehouseById(BulkRule.class, id);
		rule.setEnabled(false);
		entityDao.saveOrUpdateByWarehouse(rule);
	}

	@Override
	public void enableBulkRule(Long id) {
		BulkRule rule = entityDao.findByWarehouseById(BulkRule.class, id);
		rule.setEnabled(true);
		entityDao.saveOrUpdateByWarehouse(rule);	
	}

	@Override
	public List<BulkRule> getActiveBulkRules() {
		List<BulkRule> bulkRules = entityDao.findAllEnabledByWarehouse(BulkRule.class);
		return bulkRules;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Rule> getRulesByPriority() {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select rule from Rule rule JOIN rule.warehouse warehouse " +
		"where warehouse.id = :warehouseId and rule.enabled = true Order By rule.priority");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<Rule> ruleList  = (List<Rule>) query.getResultList();
		for(Rule rule: ruleList)
		{
			Hibernate.initialize(rule.getCcStatusList());
			Hibernate.initialize(rule.getIssueCategoryList());
			Hibernate.initialize(rule.getFulfillmentModelList());
			Hibernate.initialize(rule.getSubCategoryList());
		}
		return ruleList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long checkBulkUpload(String suborder) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select bulk.ruleId from BulkUploadData bulk JOIN bulk.warehouse " +
		"warehouse where warehouse.id = :warehouseId and bulk.suborder = :suborder");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("suborder", suborder);
		List<Long> ruleIds = (List<Long>) query.getResultList();
		if(ruleIds.size() > 0)
		{
			return ruleIds.get(0);
		}else {
			return null;
		}
	}

	@Override
	public List<RtvCheckRule> getRtvRules() {
		List<RtvCheckRule> rtvCheckRules = (List<RtvCheckRule>) entityDao.findAllByWarehouse(RtvCheckRule.class);
		return rtvCheckRules;
	}

	@Override
	public RtvCheckRule findRtvCheckRuleById(Long id) {
		return entityDao.findByWarehouseById(RtvCheckRule.class, id);
	}

	@Override
	public void disableRtvRule(Long id) {
		RtvCheckRule rule = entityDao.findByWarehouseById(RtvCheckRule.class, id);
		rule.setEnabled(false);
		entityDao.saveOrUpdateByWarehouse(rule);
	}

	@Override
	public void enableRtvRule(Long id) {
		RtvCheckRule rule = entityDao.findByWarehouseById(RtvCheckRule.class, id);
		rule.setEnabled(true);
		entityDao.saveOrUpdateByWarehouse(rule);
	}

	@Override
	public void saveOrUpdateRtvCheckRule(RtvCheckRule rtvCheckRule) {
		entityDao.saveOrUpdate(rtvCheckRule);
	}

	@Override
	public List<String> checkedUploadList(List<String> checkList) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select bulk.suborder from BulkUploadData bulk JOIN bulk.warehouse " +
		"warehouse where warehouse.id = :warehouseId order by bulk.suborder");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		List<String> uploadedList = (List<String>) query.getResultList();
		
		
		List<String> duplicates = new ArrayList<String>();
		Set<String> hs = new HashSet<String>(checkList);

		checkList.clear();
		checkList.addAll(hs);
		
		Collections.sort(checkList);
	
		for(int i=0,j=0; i<checkList.size() && j<uploadedList.size();)
		{
			
			if(checkList.get(i).equals(uploadedList.get(j)))
			{
				duplicates.add(checkList.get(i));
				checkList.remove(i);
				continue;
			}
			else if(checkList.get(i).compareTo(uploadedList.get(j)) < 0)
			{
				i++;
				continue;
			}
			else if(checkList.get(i).compareTo(uploadedList.get(j)) > 0)
			{
				j++;
				continue;
			}
		}
		
		setRemovedList(duplicates);
		
		return checkList;
	
	}
	
	@Override
	public List<String> getRemovedList()
	{
		return removedList;
	}
	
	public void setRemovedList(List<String> removedList)
	{
		this.removedList=removedList;
	}
}
