package com.snapdeal.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.snapdeal.bean.AWBResult;
import com.snapdeal.component.Constants;
import com.snapdeal.service.CourierService;

@Controller
@RequestMapping("/Test")
public class TestController {

	@Inject
	@Named("courierService")
	CourierService courierService;
	
	@RequestMapping("/testAwb")
	public String testAwb()
	{
		return "Test/test";
	}
	
	@RequestMapping("/getAwb")
	public String getAwb(@RequestParam("pincode") String pincode,@RequestParam("shippingMode") String shippingMode,ModelMap map)
	{
		String returnType = Constants.RETURN_TYPE_TO_VENDOR;
		AWBResult awbResult = courierService.getAWB(pincode,shippingMode,returnType);
		if(awbResult != null)
		{
			if(awbResult.getTrackingNumber() != null && awbResult.getCourierCode() != null)
			{
				map.put("message", "Courier Details found with code : "+
						awbResult.getCourierCode()+" and tracking number : "+awbResult.getTrackingNumber());
			}
			else if(awbResult.getCourierCode() != null && awbResult.getTrackingNumber() == null) {
				map.put("message", "No advance Awb uploaded for courier with code : "+awbResult.getCourierCode());
			}
			else {
				map.put("message", "No courier details found for given pincode");
			}
		}
		else {
			map.put("message", "No courier details found for given pincode");
		}
		return "Test/test";
	}
}
