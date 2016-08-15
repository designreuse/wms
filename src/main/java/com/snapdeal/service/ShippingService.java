package com.snapdeal.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public interface ShippingService {
	public void saveShippingLocation(String courierCode, List<Set<String>> pincodeList, List<String> areaCodeList);
	public String getAreaCode(String courierCode, String pincode);
}
