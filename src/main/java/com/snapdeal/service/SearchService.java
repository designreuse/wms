package com.snapdeal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snapdeal.entity.Inventory;

@Service
public interface SearchService {

	public List<Inventory> searchByBarcode(List<String> barcode);
	public List<Inventory> searchByLocation(List<String> location);
}
