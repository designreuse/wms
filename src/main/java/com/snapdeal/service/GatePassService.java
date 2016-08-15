package com.snapdeal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snapdeal.entity.GatePass;
import com.snapdeal.entity.GatePassStatus;

@Service
public interface GatePassService {

	public GatePass findGatePassByBarcode(String barcode);
	public void saveOrUpdateGatePass(GatePass gatePass);
	public List<String> getValidGatePassStatus();
	public List<GatePassStatus> getAllLoadedStatus();
	public GatePassStatus findGatePassStatusByid(Long id);
	public void saveOrUpdateGatePassStatus(GatePassStatus gatePassStatus);
	public boolean checkStatus(String status);
}
