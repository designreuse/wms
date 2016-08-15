package com.snapdeal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snapdeal.bean.CcStatusDto;
import com.snapdeal.bean.IssueCategoryDto;
import com.snapdeal.bean.SubCategoryDto;
import com.snapdeal.entity.BulkRule;
import com.snapdeal.entity.CcStatus;
import com.snapdeal.entity.FulfillmentModel;
import com.snapdeal.entity.IssueCategory;
import com.snapdeal.entity.RtvCheckRule;
import com.snapdeal.entity.Rule;
import com.snapdeal.entity.SubCategory;

@Service
public interface RuleService {

	public void saveIssueCategory(List<IssueCategoryDto> issueCategoryList);
	public void saveCcStatus(List<CcStatusDto> ccStatusList);
	public void saveSubCategory(List<SubCategoryDto> subCategoryList);
	public void saveFulfillmentModel(List<FulfillmentModel> fulfillmentModelList);
	public List<IssueCategory> getIssueCategories();
	public List<CcStatus> getCcStatus();
	public List<SubCategory> getSubCategories();
	public List<FulfillmentModel> getFulfillmentModels();
	public List<RtvCheckRule> getRtvRules();
	public Long saveOrUpdateRule(Rule rule);
	public Rule findRuleById(Long id);
	public List<Rule> getRules();
	public Rule findLoadedRuleById(Long id);
	public RtvCheckRule findRtvCheckRuleById(Long id);
	public void disableRule(Long id);
	public void enableRule(Long id);
	public void disableRtvRule(Long id);
	public void enableRtvRule(Long id);
	public void saveOrUpdateRtvCheckRule(RtvCheckRule rtvCheckRule);
	public Long saveOrUpdateBulkRule(BulkRule bulkRule);
	public BulkRule findBulkRuleById(Long id);
	public List<BulkRule> getBulkRules();
	public List<BulkRule> getActiveBulkRules();
	public void disableBulkRule(Long id);
	public void enableBulkRule(Long id);
	public List<Rule> getRulesByPriority();
	public Long checkBulkUpload(String suborder);
	public List<Rule> getActiveRules();
	
	public List<String> checkedUploadList(List<String> checkList);
	public List<String> getRemovedList();
	
}
