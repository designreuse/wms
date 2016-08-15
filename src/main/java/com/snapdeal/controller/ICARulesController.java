package com.snapdeal.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.snapdeal.bean.FileBean;
import com.snapdeal.bean.ICARulesDetails;
import com.snapdeal.bean.ICARulesDto;
import com.snapdeal.entity.Courier;
import com.snapdeal.entity.ICARules;
import com.snapdeal.entity.ReturnType;
import com.snapdeal.service.CourierService;
import com.snapdeal.service.ICARulesService;
import com.snapdeal.service.PostalCodeService;

@Controller
@RequestMapping("/ICARules")
public class ICARulesController {
	
	@Inject
	@Named("courierService")
	CourierService courierService;
	
	@Inject
	@Named("iCARulesService")
	ICARulesService iCARulesService;
	
	@Inject
	@Named("postalCodeService")
	PostalCodeService postalCodeService;

	/** Loads the ica rules create page with enabled courier list.**/
	@RequestMapping("/create")
	public String create(ModelMap map){
		
		ICARules iCARules = new ICARules();
		map.put("iCARules", iCARules);
		
		/** Get the list of all enabled courier from the database. **/
		List<String> courierList = courierService.getAllEnabledCourierCode();
		map.put("courierList", courierList);
		
		/** Get the return type category from the database. **/
		List<ReturnType> returnTypeList = iCARulesService.getReturnType();
		map.put("returnTypeList", returnTypeList);
		return "ICARules/create";
	}
	
	/** This will return the city list from database.**/
	@RequestMapping("/getCity")
	public @ResponseBody List<String> getCity(){
		
		List<String> city = postalCodeService.getCity();
		return city;
	}
	
	/** This will return the state list from database.**/
	@RequestMapping("/getState")
	public @ResponseBody List<String> getState(){
		
		List<String> state = postalCodeService.getState();
		return state;
	}
	
	/** Checks the rule already exists in the database. **/
	@RequestMapping("/isRuleExist")
	public @ResponseBody String isRuleExist(@ModelAttribute("returnType") String returnType,
			@ModelAttribute("type") String type,@ModelAttribute("location") String location){
		boolean result = iCARulesService.isRuleExist(returnType,type,location);
		if(result)
			return "true";
		else
			return "false";
	}
	
	/** Save the ica rule into the database. **/
	@RequestMapping("/save")
	public String saveICARules(@ModelAttribute("iCARules") ICARulesDto iCARulesDto,ModelMap map){
		
		/** Get the enabled courier code list from database. **/
		List<String> courierList = courierService.getAllEnabledCourierCode();
		map.put("courierList", courierList);
		
		/** Get the return type category from the database. **/
		List<ReturnType> returnTypeList = iCARulesService.getReturnType();
		map.put("returnTypeList", returnTypeList);
		
		/** Get the priority list and corresponding details and save it one at a time.**/
		saveOrUpdateICARules(iCARulesDto,"save");
		
		map.put("message", "ICA Rule saved succesfully");
		return "ICARules/create";
	}
	
	/** Loads the search ica rules page with all enabled courier code list. **/
	@RequestMapping("/search")
	public String search(ModelMap map){
	
		List<String> courierList = courierService.getAllEnabledCourierCode();
		map.put("courierList", courierList);
		
		/** Get the return type category from the database. **/
		List<ReturnType> returnTypeList = iCARulesService.getReturnType();
		map.put("returnTypeList", returnTypeList);
		return "ICARules/search";
	}
	
	/** Get the ica rule details from database corresponding to type, location and isRuleEnabled. **/
	@RequestMapping(value = "/getICARules")
	public @ResponseBody List<ICARulesDetails> getICARules(@ModelAttribute("returnType") String returnType, 
			@ModelAttribute("type") String type,@ModelAttribute("location") String location, 
			@ModelAttribute("isRuleEnabled") Boolean isRuleEnabled){
		List<ICARules> iCARuleList = iCARulesService.getICARules(returnType, type,location,isRuleEnabled);
		List<ICARulesDetails> iCARulesDetailsList = null;
		if(iCARuleList != null && iCARuleList.size() > 0){
			iCARulesDetailsList = new ArrayList<ICARulesDetails>();
			for(ICARules iCARule : iCARuleList){
				ICARulesDetails iCARuleDetails = new ICARulesDetails();
				iCARuleDetails.setType(iCARule.getType());
				iCARuleDetails.setLocation(iCARule.getLocation());
				iCARuleDetails.setCourierCode(iCARule.getCourierCode());
				iCARuleDetails.setPriority(iCARule.getPriority());
				iCARuleDetails.setStartDate(iCARule.getStartDate());
				iCARuleDetails.setEndDate(iCARule.getEndDate());
				iCARuleDetails.setEnabled(iCARule.getEnabled());
				iCARuleDetails.setIsRuleEnabled(iCARule.getIsRuleEnabled());
				
				iCARulesDetailsList.add(iCARuleDetails);
			}			
		}
		return iCARulesDetailsList;
	}
	
	/** Update the ica rule into the database.**/
	@RequestMapping("/update")
	public String updateICARules(@ModelAttribute("iCARules") ICARulesDto iCARulesDto,ModelMap map){
		
		/** Get the enabled courier code list from database. **/
		List<String> courierList = courierService.getAllEnabledCourierCode();
		map.put("courierList", courierList);
		
		/** Get the return type category from the database. **/
		List<ReturnType> returnTypeList = iCARulesService.getReturnType();
		map.put("returnTypeList", returnTypeList);
		
		/** Disable couriers which are not present in current list.**/
		iCARulesService.disableICARulesNotInCourierCodeList(iCARulesDto.getCourierCodeList(),iCARulesDto.getReturnType(), iCARulesDto.getType(), iCARulesDto.getLocation());
		
		/** Calls the Save ICARules function and update the field.**/
		saveOrUpdateICARules(iCARulesDto,"update");
		
		map.put("message", "ICA Rule updated succesfully");
		return "ICARules/search";
	}
	
	public void saveOrUpdateICARules(ICARulesDto iCARulesDto, String callerFunction){
		/** If the list sent is not empty. Then, save or update it.**/
		if(iCARulesDto.getPriorityList() != null){
			for(int i = 0;i<iCARulesDto.getPriorityList().size();i++){
				ICARules iCARules = new ICARules();
				
				iCARules.setReturnType(iCARulesDto.getReturnType());
				iCARules.setType(iCARulesDto.getType());
				iCARules.setLocation(iCARulesDto.getLocation());
				iCARules.setCourierCode(iCARulesDto.getCourierCodeList().get(i));
				iCARules.setPriority(iCARulesDto.getPriorityList().get(i));
				iCARules.setStartDate(iCARulesDto.getStartDate());
				
				/** Added End Date Time 23:59:59 and 999 milliseconds **/
				if(iCARulesDto.getEndDate() != null){
					Calendar c = Calendar.getInstance();
			        c.setTime(iCARulesDto.getEndDate());
			        c.set(Calendar.HOUR_OF_DAY, 23);
			        c.set(Calendar.MINUTE, 59);
			        c.set(Calendar.SECOND, 59);
			        c.set(Calendar.MILLISECOND, 999);
			        iCARulesDto.setEndDate(c.getTime());
				}
				
				iCARules.setEndDate(iCARulesDto.getEndDate());
				iCARules.setEnabled(iCARulesDto.getEnabled());	
				
				/** Only, in case of update set the isRuleEnabled otherwise use default one.**/
				if(callerFunction.equals("update"))
					iCARules.setIsRuleEnabled(iCARulesDto.getIsRuleEnabled());
				
				/** Get courier by passing courier code. **/
				Courier courier = courierService.searchCourierByCode(iCARulesDto.getCourierCodeList().get(i));
				iCARules.setCourier(courier);
				
				iCARulesService.saveICARules(iCARules);
			}
		}
	}
	
	/** Loads download list page**/
	@RequestMapping("/downloadList")
	public String downloadList(){
		return "ICARules/downloadList";
	}
	
	/** Loads Bulk Upload page for ICA Rules**/
	@RequestMapping("/bulkUpload")
	public String bulkUpload(){
		return "ICARules/bulkUpload";
	}
	
	@RequestMapping("/saveBulkUpload")
	public String saveBulkUpload(@ModelAttribute("fileData") FileBean file,ModelMap map){
		
		/** Get the posted file containing - type, location, courier code, priority, action. **/
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
				int row = 0;
				String message = "";
				
				/** Get enabled courier, city and state list **/
				List<String> returnTypeCodeList = iCARulesService.getReturnTypeCode();
				List<String> courierList = courierService.getAllEnabledCourierCode();
				List<String> cityList = postalCodeService.getCity();
				List<String> stateList = postalCodeService.getState();
				List<String> alreadyProcessedTypeLocation = new ArrayList<String>();
				
				while ((line = bufferedReader.readLine()) != null)
				{
					String [] data = line.split(splitBy);
					row++;
					if(header == 1)
					{
						/** Checks the file header which must contains - Return Type, Type, Location, Courier Code, Priority and Action **/
						if(data.length != 6 || !data[0].equals("Return Type")|| !data[1].equals("Type")||!data[2].equals("Location")
								||!data[3].equals("Courier Code")||!data[4].equals("Priority")||!data[5].equals("Action"))
						{
							error = 1;
							break;
						}
						else{
							header = 2;
						}
					}
					else if(data.length != 6){
						error = 2;
						message += "Invalid number of columns for row :" + row + "<br>";
						continue;
					}
					else{
						ICARules iCARules = new ICARules();
						
						if(!returnTypeCodeList.contains(data[0])){
							error = 2;
							message += "Invalid Return Type for Return Type : " + data[0] + " Type :" + data[1] + " Location :" + data[2] + " Courier Code :" + data[3] + "<br>";
							continue;
						}
						iCARules.setReturnType(data[0]);
						
						if(!data[1].equals("City") && !data[1].equals("State")){
							error = 2;
							message += "Invalid Type for Type :" + data[1] + " Location :" + data[2] + " Courier Code :" + data[3] + "<br>";
							continue;
						}
						iCARules.setType(data[1]);
						
						if((data[1].equals("City") && !cityList.contains(data[2])) || (data[1].equals("State") && !stateList.contains(data[2]))){
							error = 2;
							message += "Invalid location for Type :" + data[1] + " Location :" + data[2] + " Courier Code :" + data[3] + "<br>";
							continue;
						}
						iCARules.setLocation(data[2]);
						
						if(!courierList.contains(data[3])){
							error = 2;
							message += "Invalid courier code for Type :" + data[1] + " Location :" + data[2] + " Courier Code :" + data[3] + "<br>";
							continue;
						}
						iCARules.setCourierCode(data[3]);
						
						try {
							iCARules.setPriority(Integer.parseInt(data[4]));
						} catch (NumberFormatException e) {
							error = 2;
							message += "Invalid Priority for Type :" + data[1] + " Location :" + data[2] + " Courier Code :" + data[3] + "<br>";
							continue;
						}
						
						/** Set Current date and time as start date.**/
						iCARules.setStartDate(new Date());	
						
						/** Get courier by passing courier code. **/
						Courier courier = courierService.searchCourierByCode(data[3]);
						iCARules.setCourier(courier);
						
						if(data[5].equals("Edit")){
							iCARulesService.saveICARules(iCARules);
						}
						else if(data[5].equals("Add_Replace")){
							if(!alreadyProcessedTypeLocation.contains(data[1] + data[2])){
								alreadyProcessedTypeLocation.add(data[1] + data[2]);
								
								/** Disable couriers of the type and location.**/
								iCARulesService.disableICARulesByTypeLocation(iCARules.getReturnType(), iCARules.getType(), iCARules.getLocation());								
							}

							iCARulesService.saveICARules(iCARules);
						}
						else{
							error = 2;
							message += "Invalid Action for Type :" + data[1] + " Location :" + data[2] + " Courier Code :" + data[3] + "<br>";
							continue;
						}
					}
					
				}
				if(error == 1){
					map.put("message", "Invalid file uploaded.");
				}
				else if(error == 2){
					map.put("message", message);
				}
				else{
					map.put("success", true);
					map.put("message","Records saved successfully!");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			map.put("message", "Empty File Uploaded.");
		}
		
		FileBean newFile = new FileBean();
		map.put("fileData", newFile);
		
		return "ICARules/bulkUpload";
	}
	
	/** Generate the template file for shipping Location. **/
	@RequestMapping("/download")
	public void download(@RequestParam("type") String type, HttpServletResponse response){	
		try {
			String line = "";
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", "attachment; filename=List.csv");
			
			List<String> content = new ArrayList<String>();
			
			if(type.equals("City") || type.equals("State")){
				line = "Type,Location\n";				
				response.getWriter().write(line);
				
				if(type.equals("State")){
					content = postalCodeService.getState();
					
					for(String data : content){
						line = "State," + data + "\n";
						response.getWriter().write(line);
					}
				}
				else{
					content = postalCodeService.getCity();	
					
					for(String data : content){
						line = "City," + data + "\n";
						response.getWriter().write(line);
					}
				}
			}
			else{
				line = "Courier Code\n";				
				response.getWriter().write(line);
				
				content = courierService.getAllEnabledCourierCode();
				
				for(String data : content){
					line = data + "\n";
					response.getWriter().write(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Generate the template file for ICA Rules bulk upload. **/
	@RequestMapping("/templateBulkUpload")
	public void templateBulkUpload(HttpServletResponse response){
		try {
			String line="Return Type,Type,Location,Courier Code,Priority,Action\n";
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", "attachment; filename=Template.csv");
			response.setContentLength(line.length());
			response.getWriter().write(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Init Binder used to for endDate in case user enters null endDate.**/
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    dateFormat.setLenient(false);

	    // true passed to CustomDateEditor constructor means convert empty String to null
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
