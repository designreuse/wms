package com.snapdeal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snapdeal.bean.AWBResult;
import com.snapdeal.entity.Courier;

/** Service Interface for Courier **/
@Service
public interface CourierService {
	public String saveCourier(Courier courier);
	public Courier searchCourierByCode(String code);
	public boolean checkCourierByEmail(String email);
	public void updateCourier(Courier courier);
	public List<String> getAllEnabledCourierCode();
	public List<String> getAllCourierCode();
	public AWBResult getAWB(String pincode, String shippingMode, String returnType);
	public void unSetAWB(String courierCode, String trackingNumber);
	public List<String> getAllEnabledCourierCodeOfCurrentWarehouse();
}
