package com.snapdeal.service;

import org.springframework.stereotype.Service;

import com.snapdeal.entity.BulkUploadData;

@Service
public interface BulkUploadService {

	public void saveBulkUploadData(BulkUploadData bulkUploadData);
}
