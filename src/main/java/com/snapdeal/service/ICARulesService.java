package com.snapdeal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snapdeal.entity.ICARules;
import com.snapdeal.entity.ReturnType;

@Service
public interface ICARulesService {

	public void saveICARules(ICARules iCARules);

	public void disableICARulesNotInCourierCodeList(List<String> courierCodeList, String returnType, String type, String location);

	public List<ICARules> getICARules(String returnType, String type, String location,Boolean isRuleEnabled);

	public Boolean isRuleExist(String returnType, String type, String location);

	public List<ICARules> getICARules(String returnType, String type, String location);
	
	public void disableICARulesByTypeLocation(String returnType, String type, String location);

	public List<ReturnType> getReturnType();

	public List<String> getReturnTypeCode();
}
