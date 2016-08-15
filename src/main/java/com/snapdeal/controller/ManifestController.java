package com.snapdeal.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snapdeal.bean.ManifestDto;
import com.snapdeal.bean.ManifestSearch;
import com.snapdeal.entity.Inventory;
import com.snapdeal.entity.Manifest;
import com.snapdeal.entity.RtvSheet;
import com.snapdeal.service.CourierService;
import com.snapdeal.service.ManifestService;
import com.snapdeal.service.PutawayService;
import com.snapdeal.service.RtvService;

@Controller
@RequestMapping("/Manifest")
public class ManifestController {

	@Inject
	@Named("courierService")
	CourierService courierService;
	
	@Inject
	@Named("putawayService")
	PutawayService putawayService;
	
	@Inject
	@Named("manifestService")
	ManifestService manifestService;
	
	@Inject
	@Named("rtvService")
	RtvService rtvService;

	@RequestMapping("/printByCourier")
	public String printByCourier(ModelMap map){
		
		List<String> courierCodeList = courierService.getAllEnabledCourierCodeOfCurrentWarehouse();
		map.put("courierCodeList", courierCodeList);		
		return "Manifest/printByCourier";
	}
	
	@RequestMapping("/searchByCourier")
	public @ResponseBody RtvSheet searchByCourier(@ModelAttribute("courierCode") String courierCode, @ModelAttribute("rtvlCode") String rtvlCode){
		
		String rtvId = rtvlCode.replaceAll("[^0-9]", "");
		
		Long id = Long.parseLong(rtvId.equals("")?"0":rtvId);	
		RtvSheet rtvSheet = manifestService.getRtvSheetByCourierAndRtvId(courierCode, id);
		RtvSheet newRtvSheet = null;
		
		/** Return negative id if RtvSheet already manifested **/
		List<Manifest> manifestlist=manifestService.checkManifestedbyIdList(id);
		if(manifestlist !=null)
		{
			newRtvSheet = new RtvSheet();
			newRtvSheet.setId(-1L);
			return newRtvSheet; 
		}
			
		if(rtvSheet != null)
		{
			newRtvSheet = new RtvSheet();
			newRtvSheet.setId(rtvSheet.getId());
			newRtvSheet.setAwbNumber(rtvSheet.getAwbNumber());
			newRtvSheet.setCourierCode(rtvSheet.getCourierCode());
			newRtvSheet.setSellerName(rtvSheet.getSellerName());	
		}
		return newRtvSheet;
			
		}
	
	@RequestMapping("/print")
	public String print(ModelMap map,@ModelAttribute("print") ManifestDto manifestDto, HttpServletResponse response){
		
		Manifest manifest = new Manifest(); 
		if(manifestDto.getType().equals("Courier")) { 
			manifest.setRtvSheet(manifestService.getRtvSheetByIdList(manifestDto.getRtvList())); 
			manifest.setCourierCode(manifestDto.getName()); 
			map.put("courierCode", manifestDto.getName()); 
		} 
		
		if(manifest.getRtvSheet() != null && manifest.getRtvSheet().size() > 0){ 
			Long id = manifestService.saveOrUpdateManifest(manifest); 
			map.put("id", id); 
			List<Long> inventoryId = new ArrayList<Long>(); 
			List<RtvSheet> rtvSheetList = new ArrayList<RtvSheet>();
			Integer i = 0;
			for(Long sheetId : manifestDto.getRtvList()) { 
				RtvSheet persistedSheet = rtvService.findRtvSheetById(sheetId);

				if(persistedSheet != null){
					persistedSheet.setBag(manifestDto.getBagList().get(i));
					rtvSheetList.add(rtvService.saveOrUpdateRtvSheet(persistedSheet));
					for (Inventory inventory : persistedSheet.getProductDetails()) {
						inventoryId.add(inventory.getId()); 
					} 
					i++;
				}
			}   
			
			manifest.setRtvSheet(rtvSheetList);
			map.put("rtvSheetList",manifest.getRtvSheet());
			manifestService.updateInventorySetStatusManifestPrinted(inventoryId); 
		} 
		else { 
			map.put("message", "No RTV Sheet Found for the selected courier."); 
		}
		
		return "Manifest/print"; 
	}
	
	/** Generate the template file for tracking numbers. **/
	@RequestMapping("/downloadCSV")
	public void downloadCSV(@ModelAttribute("print") ManifestDto manifestDto, HttpServletResponse response){
		try {
			String line="Suborder Code,RTVL no.,Product Name,Tracking No.,Seller Name,Bag\n";
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", "attachment; filename=Manifest.csv");
			//response.setContentLength(line.length());
			response.getWriter().write(line);
			Integer i = 0;
			for(Long sheetId : manifestDto.getRtvList()) { 
				RtvSheet rtvSheet = rtvService.findRtvSheetById(sheetId);
				if(rtvSheet != null){
					rtvSheet.setBag(manifestDto.getBagList().get(i));
					for(Inventory inventory : rtvSheet.getProductDetails()){
						line = inventory.getSuborderCode();
						line += ",RTVL" + rtvSheet.getId();
						line += "," + inventory.getProductName();
						line += "," + rtvSheet.getAwbNumber();
						line += "," + inventory.getSellerName();
						line += "," + rtvSheet.getBag();
						line += "\n";
						response.getWriter().write(line);
					}
					i++;
				}
			}   
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/search")
	public String search(ModelMap map) { 
		map.put("search", new ManifestSearch()); 
		return "Manifest/search"; 
	}
	
	@RequestMapping("/searchManifest")
	public String searchManifest(ManifestSearch manifestSearch, ModelMap map) { 
		List<Manifest> manifestList = manifestService.getManifestBetweenDates(manifestSearch); 
		if(manifestList != null && manifestList.size() > 0) 
			map.put("manifestList", manifestList); 
		else 
			map.put("message", "No Maniest found for given date range."); 
		
		map.put("search", new ManifestSearch()); 
		return "Manifest/search"; 
	}
	
	@RequestMapping("/reprint/{id}")
	public String reprint(@PathVariable("id") Long id, ModelMap map) { 
		Manifest manifest = manifestService.getManifestByIdAndFetchRtvSheetEagerly(id); 
		if(manifest != null) { 
			
			map.put("id", manifest.getId()); 
			map.put("courierCode", manifest.getCourierCode()); 
			map.put("rtvSheetList",manifest.getRtvSheet());
		} 
		else { 
			map.put("message", "No RTV Sheet Found for the selected manifest."); 
		} 
		return "Manifest/print"; 
	}	
	
	@RequestMapping("/printshare")
	public String printshare(ModelMap map,@ModelAttribute("print") ManifestDto manifestDto, HttpServletResponse response){
		
		Manifest manifest = new Manifest(); 
		if(manifestDto.getType().equals("Courier")) { 
			manifest.setRtvSheet(manifestService.getRtvSheetByIdList(manifestDto.getRtvList())); 
			manifest.setCourierCode(manifestDto.getName()); 
			map.put("courierCode", manifestDto.getName()); 
			manifest.setEmail_sent(true);
		} 
		
		if(manifest.getRtvSheet() != null && manifest.getRtvSheet().size() > 0){ 
			Long id = manifestService.saveOrUpdateManifest(manifest); 
			map.put("id", id); 
			List<Long> inventoryId = new ArrayList<Long>(); 
			List<RtvSheet> rtvSheetList = new ArrayList<RtvSheet>();
			Integer i = 0;
			for(Long sheetId : manifestDto.getRtvList()) { 
				RtvSheet persistedSheet = rtvService.findRtvSheetById(sheetId);

				if(persistedSheet != null){
					persistedSheet.setBag(manifestDto.getBagList().get(i));
					rtvSheetList.add(rtvService.saveOrUpdateRtvSheet(persistedSheet));
					for (Inventory inventory : persistedSheet.getProductDetails()) {
						inventoryId.add(inventory.getId()); 
					} 
					i++;
				}
			}   
			
			manifest.setRtvSheet(rtvSheetList);
			map.put("rtvSheetList",manifest.getRtvSheet());
			manifestService.updateInventorySetStatusManifestPrinted(inventoryId); 
		} 
		else { 
			map.put("message", "No RTV Sheet Found for the selected courier."); 
		}
		
		return "Manifest/print"; 
	}
	
	@RequestMapping("/reprintshare/{id}")
	public String reprintshare(@PathVariable("id") Long id, ModelMap map) { 
		Manifest manifest = manifestService.getManifestByIdAndFetchRtvSheetEagerly(id); 
		if(manifest != null) { 
			manifest.setEmail_sent(true);
			map.put("id", manifest.getId()); 
			map.put("courierCode", manifest.getCourierCode()); 
			map.put("rtvSheetList",manifest.getRtvSheet());
			manifestService.saveOrUpdateManifest(manifest);
			
		} 
		else { 
			map.put("message", "No RTV Sheet Found for the selected manifest."); 
		} 
		return "Manifest/print"; 
	}	
}
