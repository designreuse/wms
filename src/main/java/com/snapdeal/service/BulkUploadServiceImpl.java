package com.snapdeal.service;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.dao.EntityDao;
import com.snapdeal.entity.BulkUploadData;

@Named("bulkUploadService")
@Transactional
public class BulkUploadServiceImpl implements BulkUploadService {

	@Inject
	@Named("entityDao")
	EntityDao entityDao;
	
	@Override
	public void saveBulkUploadData(BulkUploadData bulkUploadData) {
		entityDao.saveOrUpdateByWarehouse(bulkUploadData);
	}

}
