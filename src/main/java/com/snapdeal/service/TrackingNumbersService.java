package com.snapdeal.service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface TrackingNumbersService {
	public void saveTrackingNubmers(String courierCode, List<String> trackingNumberList);
	public void removeTrackingNubmers(String courierCode, List<String> trackingNumberList);
}
