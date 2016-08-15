package com.snapdeal.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.snapdeal.bean.FileBean;
import com.snapdeal.component.Constants;
import com.snapdeal.entity.GatePass;
import com.snapdeal.entity.GatePassStatus;
import com.snapdeal.entity.Inventory;
import com.snapdeal.service.GatePassService;
import com.snapdeal.service.GroupService;
import com.snapdeal.service.HistoryService;
import com.snapdeal.service.PutawayService;

@Controller
@RequestMapping("/Gatepass")
public class GatePassController {
	@Inject
	@Named("groupService")
	GroupService groupService;

	@Inject
	@Named("putawayService")
	PutawayService putawayService;
	
	@Inject
	@Named("gatePassService")
	GatePassService gatePassService;
	
	@Inject
	@Named("historyService")
	HistoryService historyService;
	
	public static final Logger LOGGER = Logger.getLogger(GatePassController.class);
	
	@RequestMapping("/home")
	public String home(ModelMap map)
	{
		map.put("fileData", new FileBean());
		return "GatePass/home";
	}

	@RequestMapping("/packages")
	public String saveFile(@ModelAttribute("fileData") FileBean file,ModelMap map)
	{
		MultipartFile postedFile = file.getPostedFile();
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
				String errorMessage = "Error while generating gatepass for following barcodes : ";
				while ((line = bufferedReader.readLine()) != null)
				{
					String [] data = line.split(splitBy);
					List<String> statusList = gatePassService.getValidGatePassStatus();
					if(data.length > 0)
					{
						if(header == 1)
						{
							if(!data[0].equalsIgnoreCase("Barcode") && !data[1].equalsIgnoreCase("Status") 
									&& data.length != 3 && !data[2].equalsIgnoreCase("Remarks"))
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
							Inventory persistedInventory = groupService.checkInventoryForGatePass(data[0]);
							if(persistedInventory != null)
							{
								GatePass gatePass = gatePassService.findGatePassByBarcode(data[0]);
								if(gatePass == null)
								{
									gatePass = new GatePass();
								}
								if(data.length == 3)
								{
									if(statusList.contains(data[1]))
									{
										gatePass.setBarcode(data[0]);
										gatePass.setStatus(data[1]);
										gatePass.setRemarks(data[2]);
										gatePassService.saveOrUpdateGatePass(gatePass);
										groupService.freeLocation(persistedInventory.getLocation());
										persistedInventory.setStatus(Constants.GATEPASS);
										putawayService.saveOrUpdateInventory(persistedInventory);
										historyService.addInventoryHistory(persistedInventory, "Gatepass created for status : "+gatePass.getStatus());
									}else {
										error = 1;
										errorMessage +=data[0]+"(Invalid Status), "; 
									}	
								}
								else {
									error = 1;
									errorMessage+=data[0]+"(Empty Record), ";	
								}
								/*groupService.freeLocation(persistedInventory.getLocation());
								persistedInventory.setStatus(Constants.GATEPASS);
								putawayService.saveOrUpdateInventory(persistedInventory);
								historyService.addInventoryHistory(persistedInventory, "Gatepass created for status : "+);*/
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
						map.put("message", "Gate Pass process completed Successfully.");
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
		return "GatePass/home";
	}

	@RequestMapping("/template")
	public void downloadTemplate(HttpServletResponse response)
	{
		try {
			String line="Barcode,Status,Remarks\n";
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", "attachment; filename=Template.csv");
			response.setContentLength(line.length());
			response.getWriter().write(line);
		} catch (IOException e) {
			LOGGER.error("IO Exception in sending template",e);
		}
	}
	
	@RequestMapping("/create")
	public String createGatepassStatus(ModelMap map)
	{
		GatePassStatus gatePassStatus = new GatePassStatus();
		map.put("status", gatePassStatus);
		return "GatePass/create";
	}

	@RequestMapping("/view")
	public String viewAllStatus(ModelMap map)
	{
		List<GatePassStatus> gatePassStatusList = gatePassService.getAllLoadedStatus();
		map.put("list", gatePassStatusList);
		return "GatePass/view";
	}

	@RequestMapping("/save")
	public String saveStatus(@ModelAttribute("status") GatePassStatus gatePassStatus, ModelMap map)
	{
		if(gatePassStatus.getId() != null)
		{
			GatePassStatus persistedStatus = gatePassService.findGatePassStatusByid(gatePassStatus.getId());
			persistedStatus.setStatus(gatePassStatus.getStatus());
			gatePassService.saveOrUpdateGatePassStatus(persistedStatus);
		}
		else {
			gatePassService.saveOrUpdateGatePassStatus(gatePassStatus);
		}
		map.put("message", "Status saved successfully");
		List<GatePassStatus> gatePassStatusList = gatePassService.getAllLoadedStatus();
		map.put("list", gatePassStatusList);
		return "GatePass/view";
	}

	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public String editWarehouse(@PathVariable("id") Long id,ModelMap map)
	{
		GatePassStatus gatePassStatus = gatePassService.findGatePassStatusByid(id);
		map.put("status", gatePassStatus);
		map.put("edit", true);
		return "GatePass/create";
	}

	@RequestMapping("/checkStatus")
	public @ResponseBody String checkStatus(@ModelAttribute("name") String status)
	{
		boolean result = gatePassService.checkStatus(status);
		if(result)
		{
			return "success";
		}
		else{
			return "failure";
		}
	}
}
