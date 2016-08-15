package com.snapdeal.controller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.snapdeal.bean.SubCategoryDto;
import com.snapdeal.dao.Dao;
import com.snapdeal.service.RuleService;

@Controller
@RequestMapping("/subcategory")
public class SubcategoryUpdateController {

	@Autowired
	Dao dao;

	@Inject
	@Named("ruleService")
	RuleService ruleService;

	@RequestMapping("/update")
	public String updateSubCategory()
	{
		List<SubCategoryDto> subCategoryList = dao.getSubCategoryList();
		ruleService.saveSubCategory(subCategoryList);
		return "redirect:/login";
	}
}
