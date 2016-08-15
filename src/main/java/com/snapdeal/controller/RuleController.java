package com.snapdeal.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snapdeal.bean.CcStatusDto;
import com.snapdeal.bean.IssueCategoryDto;
import com.snapdeal.bean.SubCategoryDto;
import com.snapdeal.component.CommonOperations;
import com.snapdeal.dao.Dao;
import com.snapdeal.entity.BaseEntity;
import com.snapdeal.entity.Box;
import com.snapdeal.entity.BulkRule;
import com.snapdeal.entity.CcStatus;
import com.snapdeal.entity.Floor;
import com.snapdeal.entity.FulfillmentModel;
import com.snapdeal.entity.Group;
import com.snapdeal.entity.IssueCategory;
import com.snapdeal.entity.Price;
import com.snapdeal.entity.Rule;
import com.snapdeal.entity.Shelf;
import com.snapdeal.entity.SubCategory;
import com.snapdeal.service.GenericService;
import com.snapdeal.service.GroupService;
import com.snapdeal.service.RuleService;

/** Manages complete operation of Rules and Bulk Rules **/
@Controller
@RequestMapping("/Rule")
public class RuleController {

	@Autowired
	Dao camsDwhDao;

	@Inject
	@Named("ruleService")
	RuleService ruleService;

	@Inject
	@Named("commonOperations")
	CommonOperations commonOperations;

	@Inject
	@Named("groupService")
	GroupService groupService;

	@Inject
	@Named("genericService")
	GenericService genericService;

	@RequestMapping("/create")
	public String createRule(ModelMap map)
	{
		Rule rule = new Rule();
		List<CcStatus> statusList = ruleService.getCcStatus();
		List<FulfillmentModel> fulfillmentModelList = ruleService.getFulfillmentModels();
		List<IssueCategory> issueCategoryList = ruleService.getIssueCategories();
		List<SubCategory> subCategoryList = ruleService.getSubCategories();
		map.put("ccstatus", statusList);
		map.put("models", fulfillmentModelList);
		map.put("issuecategory", issueCategoryList);
		map.put("subcategory", subCategoryList);
		map.put("rule", rule);
		return "Rule/create";
	}

	@RequestMapping("/view")
	public String displayRules(ModelMap map)
	{
		List<Rule> ruleList = ruleService.getRules();
		map.put("rules", ruleList);
		return "Rule/view";
	}

	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public String editRule(@PathVariable("id") Long id,ModelMap map)
	{
		Rule rule = ruleService.findLoadedRuleById(id);
		List<CcStatus> statusList = ruleService.getCcStatus();
		List<FulfillmentModel> fulfillmentModelList = ruleService.getFulfillmentModels();
		List<IssueCategory> issueCategoryList = ruleService.getIssueCategories();
		List<SubCategory> subCategoryList = ruleService.getSubCategories();
		map.put("ccstatus", statusList);
		map.put("models", fulfillmentModelList);
		map.put("issuecategory", issueCategoryList);
		map.put("subcategory", subCategoryList);
		if(rule.getPrice() == null)
		{
			rule.setPrice(new Price());
		}
		map.put("rule",rule);
		map.put("edit", true);
		return "Rule/create";
	}

	@RequestMapping(value="/disable/{id}",method=RequestMethod.GET)
	public String disableRule(@PathVariable("id") Long id,ModelMap map)
	{
		ruleService.disableRule(id);
		List<Rule> ruleList = ruleService.getRules();
		map.put("rules", ruleList);
		map.put("message", "Rule disabled Successfully");
		return "Rule/view";
	}

	@RequestMapping(value="/enable/{id}",method=RequestMethod.GET)
	public String enableRule(@PathVariable("id") Long id,ModelMap map)
	{
		ruleService.enableRule(id);
		List<Rule> ruleList = ruleService.getRules();
		map.put("rules", ruleList);
		map.put("message", "Rule enabled Successfully");
		return "Rule/view";
	}

	@RequestMapping(value="/editGroup/{id}",method=RequestMethod.GET)
	public String editGroupConfiguration(@PathVariable("id") Long id, ModelMap map)
	{
		Group group = groupService.findGroupById(id);
		Shelf lastShelf = group.getShelfList().get(group.getShelfList().size() - 1);
		String[] lastShelfName = lastShelf.getName().split("-");
		String lastShelfNumber = lastShelfName[lastShelfName.length - 1];
		map.put("shelfNumber", lastShelfNumber);
		map.put("group", group);
		map.put("edit", true);
		return "Rule/mapping";	
	}

	@RequestMapping(value="/editShelf/{id}",method=RequestMethod.GET)
	public String editShelfConfiguration(@PathVariable("id") Long id, ModelMap map)
	{
		Shelf shelf = groupService.findShelfById(id);
		map.put("shelf", shelf);
		return "Rule/shelfMapping";	
	}

	@RequestMapping("/save")
	public String saveRule(@ModelAttribute("rule") Rule rule,ModelMap map,
			@RequestParam(value="ccStatus[]",required=false) Long[] ccStatus,
			@RequestParam(value="issueCategory[]",required=false) Long[] issuecategory,
			@RequestParam(value="fulfillmentModel[]",required=false) Long[] fulfillmentModels,
			@RequestParam(value="subcategory[]",required=false) Long[] subCategoy)
	{
		try {
			List<CcStatus> ccStatusList = getListFromArray(ccStatus, CcStatus.class);
			List<IssueCategory> issueCategoryList = getListFromArray(issuecategory, IssueCategory.class);
			List<FulfillmentModel> fulfillmentModelList = getListFromArray(fulfillmentModels, FulfillmentModel.class);
			List<SubCategory> subCategoryList = getListFromArray(subCategoy, SubCategory.class);
			rule.setCcStatusList(ccStatusList);
			rule.setIssueCategoryList(issueCategoryList);
			rule.setFulfillmentModelList(fulfillmentModelList);
			rule.setSubCategoryList(subCategoryList);
			rule.setEnabled(true);
			if(rule.getPrice() != null && rule.getPrice().getOperator() != null && 
					rule.getPrice().getOperator().equals(""))
			{
				rule.setPrice(null);
			}
			if(rule.getGroup() != null)
			{
				List<Shelf> shelfList = getShelfList(rule.getGroup().getNumberOfShelves(), rule.getGroup().getName(),
						rule.getGroup().getNumberOfFloors(),rule.getGroup().getCapacityOfFloors(),rule.getGroup().getCapacityOfBox());
				rule.getGroup().setShelfList(shelfList);
			}
			
			Long id = ruleService.saveOrUpdateRule(rule);
			Rule persistedRule = ruleService.findRuleById(id);
			map.put("group", persistedRule.getGroup());
			map.put("message","Rule Saved Successfully.");
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "Rule/mapping";
	}

	@RequestMapping("/update")
	public String updateRule(@ModelAttribute("rule") Rule rule,ModelMap map,
			@RequestParam(value="ccStatus[]",required=false) Long[] ccStatus,
			@RequestParam(value="issueCategory[]",required=false) Long[] issuecategory,
			@RequestParam(value="fulfillmentModel[]",required=false) Long[] fulfillmentModels,
			@RequestParam(value="subcategory[]",required=false) Long[] subCategoy)
	{
		try {
			List<CcStatus> ccStatusList = getListFromArray(ccStatus, CcStatus.class);
			List<IssueCategory> issueCategoryList = getListFromArray(issuecategory, IssueCategory.class);
			List<FulfillmentModel> fulfillmentModelList = getListFromArray(fulfillmentModels, FulfillmentModel.class);
			List<SubCategory> subCategoryList = getListFromArray(subCategoy, SubCategory.class);
			Rule persistedRule = ruleService.findRuleById(rule.getId());
			persistedRule.setCcStatusList(ccStatusList);
			persistedRule.setIssueCategoryList(issueCategoryList);
			persistedRule.setFulfillmentModelList(fulfillmentModelList);
			persistedRule.setSubCategoryList(subCategoryList);
			persistedRule.setName(rule.getName());
			persistedRule.setLiquidation(rule.getLiquidation());
			persistedRule.setRtv(rule.getRtv());
			persistedRule.setPriority(rule.getPriority());
			persistedRule.setWarehouseFlag(rule.getWarehouseFlag());
			persistedRule.setManifestOperator(rule.getManifestOperator());
			persistedRule.setFlag3pl(rule.getFlag3pl());
			persistedRule.setRtc(rule.getRtc());
			
			if(rule.getManifestOperator() != null && !rule.getManifestOperator().isEmpty())
			{
				persistedRule.setDaysPassedManifest(rule.getDaysPassedManifest());	
			}
			else {
				persistedRule.setDaysPassedManifest(null);
			}
			if(rule.getPrice().getOperator() != null && rule.getPrice().getOperator().equals(""))
			{
				persistedRule.setPrice(null);
			}
			else{
				if(persistedRule.getPrice() != null)
				{
					persistedRule.getPrice().setOperator(rule.getPrice().getOperator());
					persistedRule.getPrice().setValue(rule.getPrice().getValue());
				}
				else
				{
					persistedRule.setPrice(rule.getPrice());
				}
			}
			ruleService.saveOrUpdateRule(persistedRule);
			List<Rule> ruleList = ruleService.getRules();
			map.put("rules", ruleList);
			map.put("message", "Rule Updated Successfully.");
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "Rule/view";
	}

	@RequestMapping("/saveMapping")
	public String saveMapping(@ModelAttribute("group") Group group, ModelMap map)
	{
		Group persistedGroup = groupService.findGroupById(group.getId());
		Map<Long,Shelf> shelfMap = commonOperations.getMapFromList(persistedGroup.getShelfList());
		List<Shelf> shelfList = new ArrayList<Shelf>();
		for(Shelf shelf:group.getShelfList())
		{
			if(shelf != null && shelf.getId() != null)
			{
				if(shelf.getId() == -1)
				{
					Shelf newShelf = new Shelf();
					newShelf.setName(shelf.getName());
					newShelf.setSellerInitial(shelf.getSellerInitial());
					newShelf.setFloorList(getFloorList(persistedGroup.getNumberOfFloors(), persistedGroup.getCapacityOfFloors(),shelf.getName(),
							persistedGroup.getCapacityOfBox()));
					newShelf.setId(groupService.saveOrUpdateShelf(newShelf));
					shelfList.add(newShelf);
				}
				else if(shelfMap.containsKey(shelf.getId()))
				{
					Shelf savedShelf = shelfMap.get(shelf.getId());
					savedShelf.setSellerInitial(shelf.getSellerInitial());
					shelfList.add(savedShelf);
				}
			}
		}
		persistedGroup.setShelfList(shelfList);
		persistedGroup.setNumberOfShelves(shelfList.size());
		groupService.saveOrUpdateGroup(persistedGroup);
		if(groupService.getRuleByGroupId(group.getId()) != null)
		{
			List<Rule> ruleList = ruleService.getRules();
			map.put("rules", ruleList);
			map.put("message", "Group Configuration Updated Successfully.");
			return "Rule/view";	
		}
		else {
			List<BulkRule> ruleList = ruleService.getBulkRules();
			map.put("bulkRules", ruleList);
			return "BulkRule/view";
		}
		
	}

	@RequestMapping("/checkGroup")
	public @ResponseBody String checkGroup(@ModelAttribute("name") String groupName)
	{
		boolean result = genericService.checkName(Group.class, groupName);
		if(result)
		{
			return "success";
		}
		else{
			return "failure";
		}
	}

	@RequestMapping("/checkRule")
	public @ResponseBody String checkRule(@ModelAttribute("name") String ruleName)
	{
		boolean result = genericService.checkName(Rule.class, ruleName);
		if(result)
		{
			return "success";
		}
		else{
			return "failure";
		}
	}

	@RequestMapping("/saveFloorMapping")
	public String saveFloorMapping(@ModelAttribute("shelf") Shelf shelf,ModelMap map)
	{
		Shelf persistedShelf = groupService.findShelfById(shelf.getId());
		Map<Long,Floor> floorMap = commonOperations.getMapFromList(persistedShelf.getFloorList());
		List<Floor> floorList = new ArrayList<Floor>();
		for(Floor floor:shelf.getFloorList())
		{
			if(floor != null && floor.getId() != null)
			{
				if(floor.getId() == -1)
				{
					Floor newFloor = new Floor();
					newFloor.setName(floor.getName());
					List<Box> boxList = new ArrayList<Box>();
					for(Box box: floor.getBoxList())
					{
						if(box != null && box.getId() != null)
						{
							Box newBox = new Box();
							newBox.setName(box.getName());
							newBox.setCapacity(box.getCapacity());
							newBox.setUsed(0);
							boxList.add(newBox);
						}	
					}
					newFloor.setCapacity(boxList.size());
					newFloor.setBoxList(boxList);
					floorList.add(newFloor);
				}
				else if(floorMap.containsKey(floor.getId()))
				{
					Floor savedFloor = floorMap.get(floor.getId());
					Map<Long,Box> boxMap = commonOperations.getMapFromList(savedFloor.getBoxList());
					List<Box> boxList = new ArrayList<Box>();
					for(Box box : floor.getBoxList())
					{
						if(box != null && box.getId() != null)
						{
							if(box.getId() == -1)
							{
								Box newBox = new Box();
								newBox.setName(box.getName());
								newBox.setCapacity(box.getCapacity());
								newBox.setUsed(0);
								boxList.add(newBox);
							}
							else if(boxMap.containsKey(box.getId()))
							{
								Box savedBox = boxMap.get(box.getId());
								savedBox.setCapacity(box.getCapacity());
								savedBox.setUsed(box.getUsed());
								boxList.add(savedBox);
							}
						}
					}
					savedFloor.setCapacity(boxList.size());
					savedFloor.setBoxList(boxList);
					floorList.add(savedFloor);
				}
			}
		}
		persistedShelf.setFloorList(floorList);
		groupService.saveOrUpdateShelf(persistedShelf);
		/*List<Rule> ruleList = ruleService.getRules();
		map.put("rules", ruleList);*/
		map.put("message", "Shelf Configuration Updated Successfully.");
		return "redirect:/home";
	}

	@RequestMapping("/bulk/create")
	public String createBulkRule(ModelMap map)
	{
		BulkRule bulkRule = new BulkRule();
		map.put("bulkRule", bulkRule);
		return "BulkRule/create";
	}

	@RequestMapping("/checkBulkRule")
	public @ResponseBody String checkBulkRule(@ModelAttribute("name") String name)
	{
		boolean result = genericService.checkName(BulkRule.class, name);
		if(result)
		{
			return "success";
		}
		else{
			return "failure";
		}
	}

	@RequestMapping("/saveBulkRule")
	public String saveBulkRule(@ModelAttribute("bulkRule") BulkRule bulkRule,ModelMap map)
	{
		bulkRule.setEnabled(true);
		if(bulkRule.getGroup() != null)
		{
			List<Shelf> shelfList = getShelfList(bulkRule.getGroup().getNumberOfShelves(), bulkRule.getGroup().getName(),
					bulkRule.getGroup().getNumberOfFloors(),bulkRule.getGroup().getCapacityOfFloors(),
					bulkRule.getGroup().getCapacityOfBox());
			bulkRule.getGroup().setShelfList(shelfList);
		}
		
		Long id = ruleService.saveOrUpdateBulkRule(bulkRule);
		BulkRule persistedBulkRule = ruleService.findBulkRuleById(id);
		map.put("group", persistedBulkRule.getGroup());
		map.put("message","Bulk Rule Saved Successfully.");
		return "Rule/mapping";
	}

	@RequestMapping("/updateBulkRule")
	public String updateBulkRule(@ModelAttribute("bulkRule") BulkRule bulkRule,ModelMap map)
	{
		BulkRule persistedRule = ruleService.findBulkRuleById(bulkRule.getId());
		
		persistedRule.setName(bulkRule.getName());
		persistedRule.setFlag3pl(bulkRule.getFlag3pl());
		persistedRule.setLiquidation(bulkRule.getLiquidation());
		persistedRule.setRtc(bulkRule.getRtc());
		persistedRule.setRtv(bulkRule.getRtv());
		persistedRule.setWarehouseFlag(bulkRule.getWarehouseFlag());
		
		
		ruleService.saveOrUpdateBulkRule(persistedRule);
		List<BulkRule> ruleList = ruleService.getBulkRules();
		map.put("bulkRules", ruleList);
		map.put("message", "Bulk Rule Updated Successfully");
		
		return "BulkRule/view";
	}

	@RequestMapping(value="/bulkEdit/{id}",method=RequestMethod.GET)
	public String editBulkRule(@PathVariable("id") Long id,ModelMap map)
	{
		BulkRule bulkRule = ruleService.findBulkRuleById(id);
		map.put("bulkRule",bulkRule);
		map.put("edit", true);
		return "BulkRule/create";
	}

	@RequestMapping("/bulk/view")
	public String displayBulkRules(ModelMap map)
	{
		List<BulkRule> ruleList = ruleService.getBulkRules();
		map.put("bulkRules", ruleList);
		return "BulkRule/view";
	}

	@RequestMapping(value="/disableBulk/{id}",method=RequestMethod.GET)
	public String disableBulkRule(@PathVariable("id") Long id,ModelMap map)
	{
		ruleService.disableBulkRule(id);
		List<BulkRule> ruleList = ruleService.getBulkRules();
		map.put("bulkRules", ruleList);
		map.put("message", "Bulk Rule disabled Successfully");
		return "BulkRule/view";
	}

	@RequestMapping(value="/enableBulk/{id}",method=RequestMethod.GET)
	public String enableBulkRule(@PathVariable("id") Long id,ModelMap map)
	{
		ruleService.enableBulkRule(id);
		List<BulkRule> ruleList = ruleService.getBulkRules();
		map.put("bulkRules", ruleList);
		map.put("message", "Bulk Rule enabled Successfully");
		return "BulkRule/view";
	}

	@RequestMapping("/test")
	public String test(ModelMap map)
	{
		List<SubCategoryDto> subCategoryList = camsDwhDao.getSubCategoryList();
		List<IssueCategoryDto> issueCategoryList = camsDwhDao.getIssueCategoryList();
		List<CcStatusDto> ccStatusList = camsDwhDao.getCcStatusList();
		List<FulfillmentModel> fulfillmentModelList = getFulfillmentModels();
		ruleService.saveCcStatus(ccStatusList);
		ruleService.saveFulfillmentModel(fulfillmentModelList);
		ruleService.saveIssueCategory(issueCategoryList);
		ruleService.saveSubCategory(subCategoryList);
		map.put("issueCategory",issueCategoryList);
		return "Rule/test";
	}

	public List<FulfillmentModel> getFulfillmentModels()
	{
		List<FulfillmentModel> fulfillmentModelList = new ArrayList<FulfillmentModel>();
		String [] models = {"DROPSHIP","FC_VOI","ONESHIP","VENDOR_SELF"};
		for(int i=0;i<models.length;i++)
		{
			FulfillmentModel fulfillmentModel = new  FulfillmentModel();
			fulfillmentModel.setName(models[i]);
			fulfillmentModelList.add(fulfillmentModel);
		}
		return fulfillmentModelList;
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

	/** Generate floor list based on the parameters passed. **/
	public List<Floor> getFloorList(Integer numberOfFloors,Integer capacityFloor,String shelfName,Integer capacityBox)
	{
		List<Floor> floorList = new ArrayList<Floor>();
		for(int h=1;h<=numberOfFloors;h++)
		{
			Floor floor = new Floor();
			StringBuffer floorName = new StringBuffer(shelfName);
			floorName.append("-");
			floorName.append(h);
			floor.setName(floorName.toString());
			floor.setCapacity(capacityFloor);
			List<Box> boxList = new ArrayList<Box>();
			for(int l=1;l<=capacityFloor;l++)
			{
				Box box = new Box();
				StringBuffer boxName = new StringBuffer(floor.getName());
				boxName.append("-");
				boxName.append(l);
				box.setName(boxName.toString());
				box.setCapacity(capacityBox);
				box.setUsed(0);
				boxList.add(box);
			}
			floor.setBoxList(boxList);
			floorList.add(floor);
		}
		return floorList;
	}

	/** Generate shelf list based on the parameters passed. Also allocates seller initials 
	 * depending upon number of shelves entered.**/
	public List<Shelf> getShelfList(Integer numberOfShelves,String groupName,Integer numberOfFloors,
			Integer capacityFloor, Integer capacityBox)
			{
		if(numberOfShelves == null || groupName == null)
		{
			return null;
		}
		List<Shelf> shelfList = new ArrayList<Shelf>();
		char initialSequence = 'A';
		if(numberOfShelves/26 < 1)
		{
			int initialsPerShelf = 26/numberOfShelves;
			int remainingInitials = 26%numberOfShelves;
			int remaining = 1;
			for(int i=1;i<=numberOfShelves;i++)
			{
				Shelf shelf = new Shelf();
				StringBuffer shelfName = new StringBuffer(groupName);
				shelfName.append("-");
				shelfName.append(i);
				shelf.setName(shelfName.toString());
				StringBuffer sellerInitials = new StringBuffer();
				for(int j=0;j<initialsPerShelf;j++)
				{
					sellerInitials.append(initialSequence);
					initialSequence++;
					sellerInitials.append(",");
				}
				if(remaining <= remainingInitials)
				{
					sellerInitials.append(initialSequence);
					initialSequence++;
					sellerInitials.append(",");
					remaining++;
				}
				sellerInitials.deleteCharAt(sellerInitials.length() - 1);
				shelf.setSellerInitial(sellerInitials.toString());
				shelf.setFloorList(getFloorList(numberOfFloors, capacityFloor, shelf.getName(),capacityBox));
				shelfList.add(shelf);
			}
		}
		else if(numberOfShelves/26 == 1 && numberOfShelves%26 == 0)
		{
			for(int i=1;i<=numberOfShelves;i++)
			{
				Shelf shelf = new Shelf();
				StringBuffer shelfName = new StringBuffer(groupName);
				shelfName.append("-");
				shelfName.append(i);
				shelf.setName(shelfName.toString());
				shelf.setSellerInitial(new StringBuffer().append(initialSequence).toString());
				initialSequence++;
				shelf.setFloorList(getFloorList(numberOfFloors, capacityFloor, shelf.getName(),capacityBox));
				shelfList.add(shelf);
			}
		}
		else
		{
			int shelfPerInitial = numberOfShelves/26;
			int shelfLeft = numberOfShelves%26;
			int shelfCreated = 1;
			int left = 1;
			while(shelfCreated<=numberOfShelves)
			{
				for(int j=1;j<=shelfPerInitial;j++)
				{
					Shelf shelf = new Shelf();
					StringBuffer shelfName = new StringBuffer(groupName);
					shelfName.append("-");
					shelfName.append(shelfCreated);
					shelf.setName(shelfName.toString());
					shelf.setSellerInitial(new StringBuffer().append(initialSequence).toString());
					shelf.setFloorList(getFloorList(numberOfFloors, capacityFloor, shelf.getName(),capacityBox));
					shelfList.add(shelf);
					shelfCreated++;
				}
				if(left <= shelfLeft)
				{
					Shelf shelf = new Shelf();
					StringBuffer shelfName = new StringBuffer(groupName);
					shelfName.append("-");
					shelfName.append(shelfCreated);
					shelf.setName(shelfName.toString());
					shelf.setSellerInitial(new StringBuffer().append(initialSequence).toString());
					shelf.setFloorList(getFloorList(numberOfFloors, capacityFloor, shelf.getName(),capacityBox));
					shelfList.add(shelf);
					shelfCreated++;
					left++;
				}
				initialSequence++;
			}
		}
		return shelfList;
	}
}
