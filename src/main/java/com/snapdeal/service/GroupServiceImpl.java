package com.snapdeal.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.component.Constants;
import com.snapdeal.component.SessionDetails;
import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.Box;
import com.snapdeal.entity.Floor;
import com.snapdeal.entity.Group;
import com.snapdeal.entity.Inventory;
import com.snapdeal.entity.Rule;
import com.snapdeal.entity.Shelf;

@Named("groupService")
@Transactional
public class GroupServiceImpl implements GroupService {

	@Inject
	@Named("entityDao")
	EntityDao entityDao;

	@Inject
	@Named("sessionDetails")
	SessionDetails sessionDetails;

	@Override
	public Group findGroupById(Long id) {
		Group group = entityDao.findByWarehouseById(Group.class, id);
		return group;
	}

	@Override
	public Long saveOrUpdateShelf(Shelf shelf) {
		entityDao.saveOrUpdateByWarehouse(shelf);
		return shelf.getId();
	}

	@Override
	public Shelf findShelfById(Long id) {
		Shelf shelf = entityDao.findByWarehouseById(Shelf.class, id);
		Hibernate.initialize(shelf.getFloorList());
		return shelf;
	}

	@Override
	public Long saveOrUpdateBox(Box box) {
		entityDao.saveOrUpdateByWarehouse(box);
		return box.getId();
	}

	@Override
	public List<Floor> getFloorListByShelf(Long shelfId) {
		Shelf shelf = findShelfById(shelfId);
		Hibernate.initialize(shelf.getFloorList());
		return shelf.getFloorList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateLocation(String boxName) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select box from Box box JOIN box.warehouse warehouse " +
		"where warehouse.id = :warehouseId and box.name = :name");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("name", boxName);
		List<Box> boxList = (List<Box>)query.getResultList();
		if(boxList != null && boxList.size() > 0)
		{
			Box persistedBox = boxList.get(0);
			if(persistedBox.getUsed() < persistedBox.getCapacity())
			{
				persistedBox.setUsed(persistedBox.getUsed() + 1);
				entityDao.saveOrUpdateByWarehouse(persistedBox);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateLocation(String boxName,String warehouseCode) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select box from Box box JOIN box.warehouse warehouse " +
				"where warehouse.code = :warehouseCode and box.name = :name");
		query.setParameter("warehouseCode", warehouseCode);
		query.setParameter("name", boxName);
		List<Box> boxList = (List<Box>)query.getResultList();
		if(boxList != null && boxList.size() > 0)
		{
			Box persistedBox = boxList.get(0);
			if(persistedBox.getUsed() < persistedBox.getCapacity())
			{
				persistedBox.setUsed(persistedBox.getUsed() + 1);
				entityDao.saveOrUpdateByWarehouse(persistedBox);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void freeLocation(String location) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select box from Box box JOIN box.warehouse warehouse " +
				"where warehouse.id = :warehouseId and box.name = :boxName");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("boxName", location);
		List<Box> boxList = (List<Box>)query.getResultList();
		if(boxList != null && boxList.size() > 0)
		{
			Box persistedBox = boxList.get(0);
			if(persistedBox.getUsed() != 0)
			{
				persistedBox.setUsed(persistedBox.getUsed() - 1);
				entityDao.saveOrUpdateByWarehouse(persistedBox);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void freeLocation(String location, Long warehouseId) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select box from Box box JOIN box.warehouse warehouse " +
				"where warehouse.id = :warehouseId and box.name = :boxName");
		query.setParameter("warehouseId", warehouseId);
		query.setParameter("boxName", location);
		List<Box> boxList = (List<Box>)query.getResultList();
		if(boxList != null && boxList.size() > 0)
		{
			Box persistedBox = boxList.get(0);
			if(persistedBox.getUsed() != 0)
			{
				persistedBox.setUsed(persistedBox.getUsed() - 1);
				entityDao.saveOrUpdateByWarehouse(persistedBox);
			}
		}
	}

	@Override
	public void saveOrUpdateGroup(Group group) {
		entityDao.saveOrUpdateByWarehouse(group);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean checkLocation(String location) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select box from Box box JOIN " +
		"box.warehouse warehouse where warehouse.id = :warehouseId and box.name = :boxName");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("boxName", location);
		List<Box> boxList = (List<Box>)query.getResultList();
		if(boxList != null && boxList.size() > 0)
		{
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean checkLocation(String location, String warehouseCode) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select box from Box box JOIN box.warehouse warehouse " +
				"where warehouse.code = :warehouseCode and box.name = :boxName");
		query.setParameter("warehouseCode", warehouseCode);
		query.setParameter("boxName", location);
		List<Box> boxList = (List<Box>)query.getResultList();
		if(boxList != null && boxList.size() > 0)
		{
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Inventory checkInventory(String barcode) {
		EntityManager entityManager = entityDao.getEntityManager();
		/*Query query = entityManager.createQuery("Select inventory from Inventory inventory JOIN " +
				"inventory.warehouse warehouse where warehouse.id = :warehouseId and inventory.barcode = :barcode " +
				"and inventory.status = :status");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());*/
		Query query = entityManager.createQuery("Select inventory from Inventory inventory JOIN FETCH inventory.warehouse " +
				"where inventory.barcode = :barcode and inventory.status = :status");
		query.setParameter("barcode", barcode);
		query.setParameter("status", Constants.IN_WAREHOUSE_STATUS);
		List<Inventory> inventoryList = (List<Inventory>)query.getResultList();
		if(inventoryList != null && inventoryList.size() > 0) {
			return inventoryList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Inventory checkInventoryForGatePass(String barcode) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select inventory from Inventory inventory JOIN FETCH inventory.warehouse " +
				"where inventory.barcode = :barcode and inventory.status in (:statusList)");
		query.setParameter("barcode", barcode);
		
		List<String> statusList = new ArrayList<String>();
		statusList.add(Constants.IN_WAREHOUSE_STATUS);
		statusList.add(Constants.PUTAWAY_LIST);
		statusList.add(Constants.PICK_LIST);
		statusList.add(Constants.PUTAWAY_LIST_PRINTED);
		statusList.add(Constants.PICK_LIST_PRINTED);
		statusList.add(Constants.PICK_LIST_GENERATED);
		statusList.add(Constants.RTV_INITIATED);
		statusList.add(Constants.RTV_STATUS);		
		
		query.setParameter("statusList", statusList);
		List<Inventory> inventoryList = (List<Inventory>)query.getResultList();
		if(inventoryList != null && inventoryList.size() > 0) {
			return inventoryList.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Rule getRuleByGroupId(Long id) {
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select rule from Rule rule JOIN " +
		"rule.warehouse warehouse where warehouse.id = :warehouseId and rule.group.id = :id");
		query.setParameter("warehouseId", sessionDetails.getActiveWarehouseId());
		query.setParameter("id", id);
		List<Rule> ruleList = (List<Rule>)query.getResultList();
		if(ruleList != null && ruleList.size() > 0)
		{
			return ruleList.get(0);
		}
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Group getGroupByName(String name)
	{
		EntityManager entityManager = entityDao.getEntityManager();
		Query query = entityManager.createQuery("Select group from Group group where group.name =:name");
		query.setParameter("name", name);
		List<Group> groupList = query.getResultList();
		if(groupList != null)
		{
			return groupList.get(0);
		}
		else 
			return null;
	}
}
