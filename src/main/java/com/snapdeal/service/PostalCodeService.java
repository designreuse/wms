package com.snapdeal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snapdeal.entity.PostalCode;

@Service
public interface PostalCodeService {
	
	public List<String> getCity();
	public List<String> getState();
	public PostalCode getPostalCode(String pincode);
}
