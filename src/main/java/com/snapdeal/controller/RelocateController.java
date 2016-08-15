package com.snapdeal.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import com.snapdeal.entity.Group;
import com.snapdeal.entity.Inventory;
import com.snapdeal.entity.Rule;
import com.snapdeal.entity.Warehouse;
import com.snapdeal.service.BulkRuleService;
import com.snapdeal.service.GroupService;
import com.snapdeal.service.HistoryService;
import com.snapdeal.service.PutawayService;
import com.snapdeal.service.WarehouseService;

@Controller
@RequestMapping("/Relocate")
public class RelocateController {

	@Inject
	@Named("groupService")
	GroupService groupService;

	@Inject
	@Named("putawayService")
	PutawayService putawayService;
	
	@Inject
	@Named("warehouseService")
	WarehouseService warehouseService;
	
	@Inject
	@Named("historyService")
	HistoryService historyService;
	
	@Inject
	@Named("bulkruleService")
	BulkRuleService bulkruleService;
	
	public static final Logger LOGGER = Logger.getLogger(RelocateController.class);
	
	@RequestMapping("/home")
	public String home(ModelMap map)
	{
		map.put("fileData", new FileBean());
		return "Relocate/home";
	}

	@RequestMapping("/packages")
	public String saveFile(@ModelAttribute("fileData") FileBean file,ModelMap map)
	{
		MultipartFile postedFile = file.getPostedFile();
		Group group;
		Rule rule;
		if(postedFile != null)
		{
			InputStream inputStream;
			try {
				inputStream = postedFile.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String line = "";
				String splitBy = ",";
				int header = 1;
				int error = 0;
				String errorMessage = "Failed to change location for following barcodes : \n";
				while ((line = bufferedReader.readLine()) != null)
				{
					String [] data = line.split(splitBy);
					if(data.length > 0)
					{
						if(header == 1)
						{
							if(data.length != 3 && !data[0].equalsIgnoreCase("Barcode") && !data[1].equalsIgnoreCase("Location")
									&& !data[2].equalsIgnoreCase("Warehouse Code"))
							{
								error = -1;
								break;
							}
							else{
								header = -1;
							}
						}
						else{
							header = 0;
							Inventory persistedInventory = groupService.checkInventory(data[0]);
							
							if(persistedInventory != null && data.length == 3 && !data[2].isEmpty() && groupService.checkLocation(data[1], data[2]))
							{
								groupService.freeLocation(persistedInventory.getLocation(), persistedInventory.getWarehouse().getId());
								persistedInventory.setLocation(data[1]);
								persistedInventory.setGroupName(data[1].split("-")[0]);
								Warehouse warehouse = warehouseService.findWarehouseByCode(data[2]);
								if(warehouse != null){
									persistedInventory.setWarehouse(warehouse);
								}
								group= groupService.getGroupByName(data[1].split("-")[0]);
								rule = groupService.getRuleByGroupId(group.getId());
								if (rule == null)
								{
									BulkRule bulkrule = bulkruleService.getBulkRulebyGroupId(group.getId());
									persistedInventory.setRuleId(bulkrule.getId());
									persistedInventory.setBulkRule(true);
																	
								}else{
									persistedInventory.setRuleId(rule.getId());
									persistedInventory.setBulkRule(false);
								}
								
								putawayService.saveOrUpdateInventory(persistedInventory);
								groupService.updateLocation(data[1], data[2]);
								historyService.addInventoryHistory(persistedInventory, "Manually Relocated to : "+persistedInventory.getLocation()
										+ " Warehouse : " + data[2]);
							}
							else if(persistedInventory != null && data.length == 2 && groupService.checkLocation(data[1]))
							{
								groupService.freeLocation(persistedInventory.getLocation());
								persistedInventory.setLocation(data[1]);
								persistedInventory.setGroupName(data[1].split("-")[0]);
								
								group = groupService.getGroupByName(data[1].split("-")[0]);
								rule = groupService.getRuleByGroupId(group.getId());
								
								if (rule == null)
								{
									BulkRule bulkrule = bulkruleService.getBulkRulebyGroupId(group.getId());
									persistedInventory.setRuleId(bulkrule.getId());
									persistedInventory.setBulkRule(true);
								
								
								}else{
								
									persistedInventory.setRuleId(rule.getId());
									persistedInventory.setBulkRule(false);
								}
								putawayService.saveOrUpdateInventory(persistedInventory);
								groupService.updateLocation(data[1]);
								historyService.addInventoryHistory(persistedInventory, "Manually Relocated to : "+persistedInventory.getLocation());
							}
							else {
								error = 1;
								errorMessage+=data[0]+", ";
							}
						}
					}
				}
				if(header == 1 || header == -1)
				{
					map.put("message", "Empty File Uploaded.");
				}
				else{
					if(error == -1)
					{
						map.put("message", "Invalid File Uploaded");
					}
					else if (error == 1) {
						map.put("message", errorMessage.substring(0,errorMessage.length() - 2));
					}
					else {
						map.put("message", "Products Relocated Successfully.");
						map.put("success", true);
					}

				}
			} catch (IOException e) {
				LOGGER.error("IO Exception in reading file",e);
				map.put("message", "Invalid File Uploaded");
			}catch (Exception e) {
				LOGGER.error("Exception in reading file",e);
				map.put("message", "Invalid File Uploaded");
			}
		}
		else {
			map.put("message", "Empty File Uploaded.");
		}
		map.put("fileData", new FileBean());
		return "Relocate/home";
	}

	@RequestMapping("/template")
	public void downloadTemplate(HttpServletResponse response)
	{
		try {
			String line="Barcode,Location,Warehouse Code\n";
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", "attachment; filename=Template.csv");
			response.setContentLength(line.length());
			response.getWriter().write(line);
		} catch (IOException e) {
			LOGGER.error("IO Exception in sending template",e);
		}
	}
}
