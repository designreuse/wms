package com.snapdeal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snapdeal.entity.Liquidation;
import com.snapdeal.entity.Warehouse;
import com.snapdeal.entity.WarehouseBoy;

@Service
public interface LiquidationService {

	public List<Liquidation> getAllLiquidation();
	public List<Liquidation> getEnabledLiquidation();
	public void saveOrUpdateLiquidation(Liquidation liquidation,Long warehouseId);
	public Liquidation findLiquidationById(Long id);
	public void enableLiquidation(Long id);
	public void disableLiquidation(Long id);
	public boolean checkName(String liquidationName);
	public boolean checkCode(String liquidationCode);
	public Liquidation findLiquidationByCode(String liquidationCode);
}
