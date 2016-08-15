package com.snapdeal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snapdeal.entity.Box;
import com.snapdeal.entity.Floor;
import com.snapdeal.entity.Group;
import com.snapdeal.entity.Inventory;
import com.snapdeal.entity.Rule;
import com.snapdeal.entity.Shelf;

@Service
public interface GroupService {
	
	public Group findGroupById(Long id);
	public Long saveOrUpdateShelf(Shelf shelf);
	public Shelf findShelfById(Long id);
	public void saveOrUpdateGroup(Group group);
	public Long saveOrUpdateBox(Box box);
	public List<Floor> getFloorListByShelf(Long shelfId);
	public void updateLocation(String boxName);
	public void freeLocation(String location);
	public Inventory checkInventory(String barcode);
	public Inventory checkInventoryForGatePass(String barcode);
	public Rule getRuleByGroupId(Long id);
	public boolean checkLocation(String location);
	public void freeLocation(String location, Long warehouseId);
	public void updateLocation(String boxName, String warehouseCode);
	public boolean checkLocation(String location, String warehouseCode);
	public Group getGroupByName(String name);
}
