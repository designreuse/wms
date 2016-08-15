package com.snapdeal.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.snapdeal.bean.FileBean;
import com.snapdeal.bean.TemplateMap;
import com.snapdeal.entity.BaseEntity;
import com.snapdeal.entity.Courier;
import com.snapdeal.entity.Warehouse;
import com.snapdeal.service.CourierService;
import com.snapdeal.service.ShippingService;
import com.snapdeal.service.TrackingNumbersService;
import com.snapdeal.service.WarehouseService;

@Controller
@RequestMapping("/Courier")
public class CourierController {
	
	@Inject
	@Named("courierService")
	CourierService courierService;
	
	@Inject
	@Named("shippingService")
	ShippingService shippingService;
	
	@Inject
	@Named("trackingNumbersService")
	TrackingNumbersService trackingNumbersService;
	
	@Inject
	@Named("warehouseService")
	WarehouseService warehouseService;
	
	/** Loads the courier creation page **/
	@RequestMapping("/create")
	public String createCourier(ModelMap map){
		/** Get all Enabled warehouse from the DB **/
		List<Warehouse> warehouseList = warehouseService.getEnabledWarehouses(); 
		Courier courier = new Courier();		
		List<TemplateMap> templateList = createTemplateList();
		
		map.put("warehouseList",warehouseList);
		map.put("courier",courier);		
		map.put("templateList",templateList);
		return "Courier/create";
	}
	
	/** Check for the existing email in the database corresponding to a courier entity.**/
	@RequestMapping("/checkCourierEmail")
	public @ResponseBody String checkCourierEmail(@ModelAttribute("email") String email, ModelMap map){
		boolean result = courierService.checkCourierByEmail(email);
		
		/** If exists then return false to the Jquery validator else true.**/
		if(result)
			return "false";
		else
			return "true";
	}
	
	/** Create and save courier after successful completion of courier details. **/
	@RequestMapping("/save")
	public String saveCourier(@ModelAttribute("courier") Courier courier, @RequestParam(value="warehouse[]",required=false) Long[] warehouse, ModelMap map){
		try {
			List<Warehouse> newWarehouseList = null;
			
			if(warehouse != null)
				newWarehouseList = getListFromArray(warehouse,Warehouse.class);
			else
				newWarehouseList = warehouseService.getEnabledWarehouses();
			
			courier.setWarehouseList(newWarehouseList);
			
			String code = courierService.saveCourier(courier);
			map.put("message", "Courier saved succesfully with code : " + code);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		/** Get all Enabled warehouse from the DB **/
		List<Warehouse> warehouseList = warehouseService.getEnabledWarehouses();		
		map.put("warehouseList",warehouseList);
		return "Courier/create";
	}
	
	/** Get both enabled and disabled courier from database.**/
	@RequestMapping("/search")
	public String searchCourier(ModelMap map){
		List<String> courierCodeList = courierService.getAllCourierCode();
		map.put("courierCodeList", courierCodeList);
		return "Courier/search";
	}
	
	/** Get the courier details from the database corresponding to a particular courier code. **/
	@RequestMapping("/info")
	public String showCourierInfo(@RequestParam("code") String code, ModelMap map){
		List<String> courierCodeList = courierService.getAllCourierCode();
		map.put("courierCodeList", courierCodeList);
		
		Courier courier = courierService.searchCourierByCode(code);
		if(courier == null)
			map.put("message", "Courier not found");
		else{
			map.put("courier", courier);
			List<TemplateMap> templateMapList = parseXml(courier.getSoftDataTemplate());
			List<TemplateMap> templateList = createTemplateList();
			
			map.put("templateMapList", templateMapList);
			map.put("templateList", templateList);
		}
		
		/** Get all Enabled warehouse from the DB **/
		List<Warehouse> warehouseList = warehouseService.getEnabledWarehouses();		
		map.put("warehouseList",warehouseList);
		
		return "Courier/search";
	}
	
	/** Update the courier details.**/
	@RequestMapping("/update")
	public String updateCourier(@ModelAttribute Courier courier, @RequestParam(value="warehouse[]",required=false) Long[] warehouse, ModelMap map){
		List<String> courierCodeList = courierService.getAllCourierCode();
		map.put("courierCodeList", courierCodeList);
		
		try {	
			List<Warehouse> newWarehouseList = null;
			if(warehouse != null)
				newWarehouseList = getListFromArray(warehouse,Warehouse.class);
			else
				newWarehouseList = warehouseService.getEnabledWarehouses();
			
			courier.setWarehouseList(newWarehouseList);
			
			courierService.updateCourier(courier);
			map.put("messageUpdate", "Courier details updated successfully");
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		/** Get all Enabled warehouse from the DB **/
		List<Warehouse> warehouseList = warehouseService.getEnabledWarehouses();		
		map.put("warehouseList",warehouseList);
		
		List<TemplateMap> templateMapList = parseXml(courier.getSoftDataTemplate());
		List<TemplateMap> templateList = createTemplateList();
		
		map.put("templateMapList", templateMapList);
		map.put("templateList", templateList);
		
		return "Courier/search";
	}
	
	/** Loads the shipping Location page **/
	@RequestMapping("/shippingLocation")
	public String shippingLocation(ModelMap map){
		FileBean file = new FileBean();
		map.put("fileData", file);
		return "Courier/shippingLocation";
	}
	
	/** Save the shipping Location **/
	@RequestMapping("/saveLocation")
	public String saveLocation(@ModelAttribute("fileData") FileBean file,ModelMap map){
		
		/** Get the posted file containing - courier code and pincode. **/
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

				/** Get the enabled courier code List from database. **/
				List<String> courierCodeList = courierService.getAllEnabledCourierCode();
				
				List<String> areaCodeList = new ArrayList<String>();
				List<Set<String>> pincodeList = new ArrayList<Set<String>>();
				String courierCode = new String();
				
				while ((line = bufferedReader.readLine()) != null)
				{
					String [] data = line.split(splitBy);
					if(header == 1)
					{
						/** Checks the file header which must contains - Courier code, Pincode, Area Code **/
						//System.out.println(data[0] + " " + data[1] + " " + data[2]);
						if(!data[0].equals("Courier Code")||!data[1].equals("Pincode")||!data[2].equals("Area Code"))
						{
							error = 1;
							break;
						}
						else{
							header = 2;
						}
					}
					else if(header == 2){
						/** Checks the provided courier code is enabled or not from courierCodeList **/
						if(courierCodeList.contains(data[0])){
							courierCode = data[0];
							header = -1;
						}
						else{
							error = 2;
							break;
						}
					}
					if(header == -1){
						/** Checks that courier code is same for whole one file and Pincode must not be null **/
						if(data.length >= 2 && courierCode.equals(data[0]) && !data[1].equals(null) && !data[1].equals("")){
							String areaCode;
							String pincode = data[1];
							if(data.length < 3 || data[2].equals(null) || data[2].equals("")){
								areaCode = new String("");
							}
							else
								areaCode = data[2];
							
							if(areaCodeList.contains(areaCode)){
								int position = areaCodeList.indexOf(areaCode);
								pincodeList.get(position).add(pincode);
							}
							else{
								areaCodeList.add(areaCode);
								Set<String> pincodeSet = new HashSet<String>();
								pincodeSet.add(pincode);
								pincodeList.add(pincodeSet);
							}
						}
					}
				}
				if(error == 1){
					map.put("message", "Invalid file uploaded.");
				}
				else if(error == 2){
					map.put("message", "Courier code not present.");
				}
				else{
					/** Save the shipping Location and input provided are - courier code and pincode list **/
					shippingService.saveShippingLocation(courierCode,pincodeList,areaCodeList);
					map.put("success", true);
					map.put("message","Records saved successfully of courier code : " + courierCode);
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
		
		return "Courier/shippingLocation";
	}
	
	/** Generate the template file for shipping Location. **/
	@RequestMapping("/templateShippingLocation")
	public void templateShippingLocation(HttpServletResponse response){
		try {
			String line="Courier Code,Pincode,Area Code\n";
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", "attachment; filename=Template.csv");
			response.setContentLength(line.length());
			response.getWriter().write(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Loads the tracking numbers page. **/
//	@RequestMapping("/trackingNumbers")
//	public String trackingNumbers(ModelMap map){
//		FileBean file = new FileBean();
//		map.put("fileData", file);
//		return "Courier/trackingNumbers";
//	}
	
	/** Loads and Save the tracking numbers into database **/
	@RequestMapping("/trackingNumbers")
	public synchronized String saveTrackingNumbers(@ModelAttribute("fileData") FileBean file,ModelMap map){
		
		/** Get the posted file containing - courier code and tracking numbers. **/
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

				/** Get the enabled courier code List from database. **/
				List<String> courierCodeList = courierService.getAllEnabledCourierCode();
				List<String> trackingNumberList = new ArrayList<String>();
				String courierCode = new String();
				
				while ((line = bufferedReader.readLine()) != null)
				{
					String [] data = line.split(splitBy);
					if(header == 1)
					{
						/** Checks the file header which must contains - Courier code and Tracking Number **/
						if(!data[0].equals("Courier Code")||!data[1].equals("Tracking Number"))
						{
							error = 1;
							break;
						}
						else{
							header = 2;
						}
					}
					else if(header == 2){
						/** Checks the provided courier code is present or not from courierCodeList **/
						if(courierCodeList.contains(data[0])){
							courierCode = data[0];
							header = -1;
						}
						else{
							error = 2;
							break;
						}
					}
					if(header == -1){
						/** Checks that courier code is same for whole one file and tracking numbers must not be null **/
						if(courierCode.equals(data[0]) && data[1] != null && data[1] != ""){
							trackingNumberList.add(data[1]);
						}
					}
				}
				if(error == 1){
					map.put("message", "Invalid file uploaded.");
				}
				else if(error == 2){
					map.put("message", "Courier code not present.");
				}
				else{
					/** Save the tracking numbers and input provided are - courier code and tracking number list **/
					trackingNumbersService.saveTrackingNubmers(courierCode, trackingNumberList);
					map.put("success", true);
					map.put("message","Records saved successfully of courier code : " + courierCode);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		else {
//			map.put("message", "Empty File Uploaded.");
//		}
		
		FileBean newFile = new FileBean();
		map.put("fileData", newFile);
		
		return "Courier/trackingNumbers";
	}
	
	/** Loads the tracking numbers page. **/
	@RequestMapping("/trackingNumbers/remove")
	public String removeTrackingNumbers(@ModelAttribute("fileData") FileBean file,ModelMap map){
		
		/** Get the posted file containing - courier code and tracking numbers. **/
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

				/** Get the enabled courier code List from database. **/
				List<String> courierCodeList = courierService.getAllEnabledCourierCode();
				List<String> trackingNumberList = new ArrayList<String>();
				String courierCode = new String();
				
				while ((line = bufferedReader.readLine()) != null)
				{
					String [] data = line.split(splitBy);
					if(header == 1)
					{
						/** Checks the file header which must contains - Courier code and Tracking Number **/
						if(!data[0].equals("Courier Code")||!data[1].equals("Tracking Number"))
						{
							error = 1;
							break;
						}
						else{
							header = 2;
						}
					}
					else if(header == 2){
						/** Checks the provided courier code is enabled or not from courierCodeList **/
						if(courierCodeList.contains(data[0])){
							courierCode = data[0];
							header = -1;
						}
						else{
							error = 2;
							break;
						}
					}
					if(header == -1){
						/** Checks that courier code is same for whole one file and tracking numbers must not be null **/
						if(courierCode.equals(data[0]) && data[1] != null && data[1] != ""){
							trackingNumberList.add(data[1]);
						}
					}
				}
				if(error == 1){
					map.put("message", "Invalid file uploaded.");
				}
				else if(error == 2){
					map.put("message", "Courier code not present.");
				}
				else{
					/** Save the tracking numbers and input provided are - courier code and tracking number list **/
					trackingNumbersService.removeTrackingNubmers(courierCode, trackingNumberList);
					map.put("success", true);
					map.put("message","Records saved successfully of courier code : " + courierCode);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		FileBean newFile = new FileBean();
		map.put("fileData", newFile);
		
		return "Courier/removeTrackingNumbers";
	}

	
	/** Generate the template file for tracking numbers. **/
	@RequestMapping("/templateTrackingNumbers")
	public void templateTrackingNumbers(HttpServletResponse response){
		try {
			String line="Courier Code,Tracking Number\n";
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", "attachment; filename=Template.csv");
			response.setContentLength(line.length());
			response.getWriter().write(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<TemplateMap> createTemplateList(){
		List<TemplateMap> templateList = new ArrayList<TemplateMap>();
		templateList.add(createTemplateMap("sno","sno"));
		templateList.add(createTemplateMap("Order Code","inventory.order_code"));
		templateList.add(createTemplateMap("Barcode","inventory.barcode"));
		templateList.add(createTemplateMap("Vendor Code","inventory.vendor_code"));
		templateList.add(createTemplateMap("Seller Name","inventory.seller_name"));
		templateList.add(createTemplateMap("Product Name","inventory.product_name"));
		templateList.add(createTemplateMap("ForwardAwbNumber","inventory.forwardAwbNumber"));
		templateList.add(createTemplateMap("Price","inventory.price"));
		templateList.add(createTemplateMap("Weight","inventory.weight"));
		templateList.add(createTemplateMap("SubOrderCode","inventory.suborderCode"));
		templateList.add(createTemplateMap("Shipping Mode","inventory.shipping_mode"));
		templateList.add(createTemplateMap("IssueCategory","inventory.issueCategory"));
		templateList.add(createTemplateMap("RtvlNo.","rtv_sheet_detail.id"));
		templateList.add(createTemplateMap("Created","manifest.created"));
		templateList.add(createTemplateMap("AWB Number","rtv_sheet_detail.awb_number"));
//		templateList.add("bag");
		templateList.add(createTemplateMap("Receiver Address Line1","receiver_detail.address_line1"));
		templateList.add(createTemplateMap("Receiver Address Line2","receiver_detail.address_line2"));
		templateList.add(createTemplateMap("Receiver City","receiver_detail.city"));
		templateList.add(createTemplateMap("Receiver State","receiver_detail.state"));
		templateList.add(createTemplateMap("Receiver Pincode","receiver_detail.pincode"));
		templateList.add(createTemplateMap("Receiver Contact Number","receiver_detail.contact_number"));
		templateList.add(createTemplateMap("Receiver Email","receiver_detail.email"));
		templateList.add(createTemplateMap("Receiver Name","receiver_detail.name"));
		templateList.add(createTemplateMap("Center Name","warehouse.name"));
		templateList.add(createTemplateMap("Center AddressLine1","address_details.addressLine1"));
		templateList.add(createTemplateMap("Center AddressLine2","address_details.addressLine2"));
		templateList.add(createTemplateMap("Center City","address_details.city"));
		templateList.add(createTemplateMap("Center State","address_details.state"));
		templateList.add(createTemplateMap("Center Contact Number","address_details.contactNumber"));
		
		return templateList;		
	}
	
	private List<TemplateMap> parseXml(String xmlContent){
		try {
			 
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new StringReader(xmlContent)));
			
			doc.getDocumentElement().normalize();
		 
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			
			NodeList nList = doc.getElementsByTagName("Column");
			
			List<TemplateMap> templateMapList = new ArrayList<TemplateMap>(); 
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				 
				Node nNode = nList.item(temp);
		 
				//System.out.println("\nCurrent Element :" + nNode.getNodeName());
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
					
					TemplateMap map = new TemplateMap();
		 
					map.setTitle(eElement.getElementsByTagName("Title").item(0).getTextContent());
					map.setMap(eElement.getElementsByTagName("Map").item(0).getTextContent());
					map.setValue(eElement.getElementsByTagName("Value").item(0).getTextContent());
					
					templateMapList.add(map);
				}
			}
			
			return templateMapList;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static<T extends BaseEntity> List<T> getListFromArray(Long [] objectIds,Class<T> objectClass) throws InstantiationException, IllegalAccessException {
		if(objectIds != null)
		{
			List<T> finalList = new ArrayList<T>();
			for(Long id : objectIds)
			{
				T object = objectClass.newInstance();
				object.setId(id);
				finalList.add(object);
			}
			return finalList;
		}
		else{
			return null;
		}
	}
	
	public TemplateMap createTemplateMap(String title,String map)
	{
		TemplateMap templatemap = new TemplateMap();
		
		templatemap.setTitle(title);
		templatemap.setMap(map);
		return templatemap;
	}
}
