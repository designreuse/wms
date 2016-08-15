package com.snapdeal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snapdeal.bean.ManifestSearch;
import com.snapdeal.entity.Manifest;
import com.snapdeal.entity.RtvSheet;

/** Service Class of Manifest**/
@Service
public interface ManifestService {

	public List<RtvSheet> getRtvSheetByCourierHavingStatusRTV(String courierCode);
	public void updateInventorySetStatusManifestPrinted(List<Long> inventoryId);
	public Long saveOrUpdateManifest(Manifest manifest);   
	public List<Manifest> getManifestBetweenDates(ManifestSearch manifestsearch);   
	public Manifest getManifestByIdAndFetchRtvSheetEagerly(Long id);
	public RtvSheet getRtvSheetByCourierAndRtvId(String courierCode, Long id);
	public List<RtvSheet> getRtvSheetByIdList(List<Long> rtvListId);
	public List<Manifest> checkManifestedbyIdList(Long rtvListId);

}
