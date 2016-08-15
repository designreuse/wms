package com.snapdeal.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.springframework.stereotype.Component;

import com.snapdeal.entity.BaseWarehouseEntity;

@Component
@Named("commonOperations")
public class CommonOperations {

	public <T extends BaseWarehouseEntity> Map<Long,T> getMapFromList(List<T> objectList)
	{
		Map<Long,T> mapFromList = new HashMap<Long, T>();
		for(T object : objectList)
		{
			mapFromList.put(object.getId(), object);
		}
		return mapFromList;
	}
}
