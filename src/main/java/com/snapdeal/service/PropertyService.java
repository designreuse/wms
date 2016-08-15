package com.snapdeal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snapdeal.entity.Property;

/** Service Interface for Property **/
@Service
public interface PropertyService {
	public Boolean checkPropertyName(String name);
	public void saveOrUpdate(Property property);
	public String getValue(String name);
	public List<String> getPropertyName();
}
