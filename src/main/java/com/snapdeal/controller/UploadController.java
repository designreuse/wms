package com.snapdeal.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.snapdeal.bean.FileBean;
import com.snapdeal.entity.BulkRule;
import com.snapdeal.entity.BulkUploadData;
import com.snapdeal.service.BulkUploadService;
import com.snapdeal.service.RuleService;

/** Handles bulk upload of CRI codes for bulk rules and download template **/
@Controller
@RequestMapping("/Upload")
public class UploadController {

	@Inject
	@Named("ruleService")
	RuleService ruleService;

	@Inject
	@Named("bulkUploadService")
	BulkUploadService bulkUploadService;

	public static final Logger LOGGER = Logger.getLogger(UploadController.class);

	@RequestMapping("/bulk")
	public String bulkUpload(ModelMap map)
	{
		List<BulkRule> bulkRuleList = ruleService.getActiveBulkRules();
		FileBean file = new FileBean();
		map.put("fileData", file);
		map.put("rules", bulkRuleList);
		return "Upload/home";
	}

	/*@SuppressWarnings("unused")
	@InitBinder
    private void initBinder(HttpServletRequest request,ServletRequestDataBinder binder) throws ServletException{
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }*/

	@RequestMapping("/save")
	public String saveFile(@ModelAttribute("fileData") FileBean file,ModelMap map)
	{
		MultipartFile postedFile = file.getPostedFile();
		List<String> bulkuploadList = new ArrayList<String>();
		if(postedFile != null)
		{
			InputStream inputStream;
			try {
				inputStream = postedFile.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String line = "";
				String splitBy = ",";
				int header = 1;
				int index=0;
				int error = 0;
				while ((line = bufferedReader.readLine()) != null)
				{
					String [] data = line.split(splitBy);
					if(data.length > 0)
					{
						if(header == 1)
						{
							if(!data[0].equals("Suborder Code") && data.length != 1)
							{
								error = 1;
								break;
							}
							else{
								header = -1;
							}
						}
						else{
							error = -1;
/*							if(ruleService.checkBulkUpload(data[0]) == null)
							{
								BulkUploadData bulkUploadData = new BulkUploadData();
								bulkUploadData.setSuborder(data[0]);
								bulkUploadData.setRuleId(file.getRuleId());
								bulkUploadService.saveBulkUploadData(bulkUploadData);
							}
*/							
							bulkuploadList.add(index++, data[0]);						
							}
					}
				}
				if(error == 1)
				{
					map.put("message", "Invalid File Uploaded.");
				}
				else if (error == 0) {
					map.put("message", "Empty File Uploaded.");
				}
				else{
					map.put("success", true);
					map.put("message","File Uploaded Successfully.");
				}
			} catch (IOException e) {
				LOGGER.error("IO exception occurred.",e);
				map.put("message", "Invalid File Uploaded");
			}catch (Exception e) {
				LOGGER.error("Exception in reading file",e);
				map.put("message", "Invalid File Uploaded");
			}
		}
		else {
			map.put("message", "Empty File Uploaded.");
		}
		List<String> finaluploadList = ruleService.checkedUploadList(bulkuploadList);
		
		for(int i=0;i<finaluploadList.size();i++)
		{
			BulkUploadData bulkUploadData = new BulkUploadData();
			bulkUploadData.setSuborder(finaluploadList.get(i));
			bulkUploadData.setRuleId(file.getRuleId());
			bulkUploadService.saveBulkUploadData(bulkUploadData);
		}
		List<String> removedList = ruleService.getRemovedList();
		map.put("removed", removedList);
		
		List<BulkRule> bulkRuleList = ruleService.getActiveBulkRules();
		FileBean newFile = new FileBean();
		map.put("fileData", newFile);
		map.put("rules", bulkRuleList);
		return "Upload/home";
	}

	@RequestMapping("/template")
	public void downloadTemplate(HttpServletResponse response)
	{
		try {
			String line="Suborder Code\n";
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", "attachment; filename=Template.csv");
			response.setContentLength(line.length());
			response.getWriter().write(line);
		} catch (IOException e) {
			LOGGER.error("IO Exception in sending template",e);
		}
	}
}
