package com.snapdeal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snapdeal.entity.Centre3PL;

@Service
public interface Centre3PLService {
	
	public List<Centre3PL> getAllCentre3PL();
	public List<Centre3PL> getEnabledCentre3PL();
	public void saveOrUpdateCentre3PL(Centre3PL centre3pl,Long id);
	public Centre3PL findCentre3PLById(Long id);
	public void enableCentre3PL(Long id);
	public void disableCentre3PL(Long id);
	public boolean checkName(String centre3plName);
	public boolean checkCode(String centre3plCode);
	public Centre3PL findCentre3PLByCode(String centre3plCode);

}
