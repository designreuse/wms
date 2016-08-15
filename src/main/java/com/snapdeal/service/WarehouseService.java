package com.snapdeal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snapdeal.entity.Warehouse;
import com.snapdeal.entity.WarehouseBoy;

@Service
public interface WarehouseService {

	public Long saveOrUpdateWarehouse(Warehouse warehouse);
	public void enableWarehouse(Long id);
	public void disableWarehouse(Long id);
	public Warehouse findWarehouseByid(Long id);
	public List<Warehouse> getAllWarehouse();
	public List<Warehouse> getEnabledWarehouses();
	public boolean checkName(String warehouseName);
	public boolean checkCode(String warehouseCode);
	public void saveOrupdateWarehouseBoy(WarehouseBoy warehouseBoy);
	public List<WarehouseBoy> getAllWarehouseBoy();
	public WarehouseBoy findWarehouseBoyById(Long id);
	public void removeWarehouseBoy(Long id);
	public Warehouse findWarehouseByCode(String warehouseCode);
}
