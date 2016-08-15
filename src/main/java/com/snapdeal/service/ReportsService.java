package com.snapdeal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snapdeal.entity.Reports;

/** Service Interface for Reports **/
@Service
public interface ReportsService {
	public String getEmailAddress(String name);
	public void saveOrUpdate(Reports reports);
	public Boolean checkReportName(String name);
	public List<String> getReportsName();
}
