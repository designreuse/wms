package com.snapdeal.service;

import org.springframework.stereotype.Service;

import com.snapdeal.entity.BaseWarehouseEntity;

@Service
public interface GenericService {

	public <T extends BaseWarehouseEntity> boolean checkName(Class<T> objectClass,String name);
}
