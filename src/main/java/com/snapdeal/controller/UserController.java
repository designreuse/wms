package com.snapdeal.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snapdeal.entity.Roles;
import com.snapdeal.entity.User;
import com.snapdeal.entity.Warehouse;
import com.snapdeal.service.UserService;
import com.snapdeal.service.WarehouseService;

/** Manages complete user operation **/
@Controller
@RequestMapping("/User")
public class UserController {

	@Inject
	@Named("userService")
	UserService userService;

	@Inject
	@Named("warehouseService")
	WarehouseService warehouseService;

	@RequestMapping("/create")
	public String addUser(ModelMap map)
	{
		User user = new User();
		List<Roles> userRoles = userService.getAllRoles();
		List<Warehouse> userWarehouse = warehouseService.getEnabledWarehouses();
		map.put("warehouses", userWarehouse);
		map.put("roles", userRoles);
		map.put("user", user);
		return "User/create";
	}

	@RequestMapping("/role/create")
	public String addRoles(ModelMap map)
	{
		Roles role = new Roles();
		map.put("role", role);
		return "User/createRole";
	}

	@RequestMapping(value="/role/save",method=RequestMethod.POST)
	public String saveRole(@ModelAttribute("role") Roles role,ModelMap map)
	{
		userService.saveOrUpdateRole(role);
		Roles newRole = new Roles();
		map.put("role", newRole);
		map.put("message", "Role Saved Successfully");
		return "User/createRole";
	}

	@RequestMapping("/save")
	public String saveUser(@ModelAttribute("user") User user,ModelMap map,@RequestParam(value="role[]") Long[] userRoles,
			@RequestParam(value="warehouse[]") Long[] userWarehouse)
	{
		if(userRoles != null && userRoles.length > 0 && userWarehouse != null && userWarehouse.length > 0)
		{
			List<Roles> finalRoles = new ArrayList<Roles>();
			List<Warehouse> finalWarehouses = new ArrayList<Warehouse>();
			for(Long roleId : userRoles)
			{
				Roles r = new Roles();
				r.setId(roleId);
				finalRoles.add(r);
			}
			for(Long warehouseId : userWarehouse)
			{
				Warehouse warehouse = new Warehouse();
				warehouse.setId(warehouseId);
				finalWarehouses.add(warehouse);
			}
			if(finalWarehouses.size() > 0)
			{
				user.setActiveWarehouse(finalWarehouses.get(0));
			}
			if(user.getId() != null)
			{
				User persistedUser = userService.findUserById(user.getId());
				persistedUser.setUserRoles(finalRoles);
				persistedUser.setUserWarehouses(finalWarehouses);
				persistedUser.setActiveWarehouse(user.getActiveWarehouse());
				persistedUser.setUserName(user.getUserName());
				userService.saveOrUpdateUser(persistedUser);
			}
			else {
				user.setUserRoles(finalRoles);
				user.setUserWarehouses(finalWarehouses);
				userService.saveOrUpdateUser(user);	
			}
			List<Roles> userRoleList = userService.getAllRoles();
			List<Warehouse> warehouses = warehouseService.getEnabledWarehouses();
			map.put("warehouses", warehouses);
			map.put("roles", userRoleList);
			map.put("user", new User());
			map.put("message", "User saved Successfully");
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			List<User> userList = userService.getUsersExceptLoggedIn(userName);
			map.put("users", userList);
			return "User/view";
		}else {
			List<Roles> userRolesList = userService.getAllRoles();
			List<Warehouse> userWarehouseList = warehouseService.getEnabledWarehouses();
			map.put("warehouses", userWarehouseList);
			map.put("roles", userRolesList);
			map.put("user", user);
			map.put("edit", true);
			map.put("message", "Please select role and warehouse for the user.");
			return "User/create";
		}
	}

	@RequestMapping("/view")
	public String viewAllUser(ModelMap map)
	{
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		List<User> userList = userService.getUsersExceptLoggedIn(userName);
		map.put("users", userList);
		return "User/view";
	}

	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public String editUser(@PathVariable("id") Long id,ModelMap map)
	{
		User user = userService.findUserById(id);
		List<Roles> userRoles = userService.getAllRoles();
		List<Warehouse> userWarehouse = warehouseService.getEnabledWarehouses();
		map.put("warehouses", userWarehouse);
		map.put("roles", userRoles);
		map.put("user", user);
		map.put("edit", true);
		return "User/create";
	}

	@RequestMapping(value="/disable/{id}",method=RequestMethod.GET)
	public String disableUser(@PathVariable("id") Long id,ModelMap map)
	{
		userService.disableUser(id);
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		List<User> userList = userService.getUsersExceptLoggedIn(userName);
		map.put("users", userList);
		map.put("message", "User disabled Successfully");
		return "User/view";
	}

	@RequestMapping(value="/enable/{id}",method=RequestMethod.GET)
	public String enableUser(@PathVariable("id") Long id,ModelMap map)
	{
		userService.enableUser(id);
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		List<User> userList = userService.getUsersExceptLoggedIn(userName);
		map.put("users", userList);
		map.put("message", "User enabled Successfully");
		return "User/view";
	}

	@RequestMapping("/checkUser")
	public @ResponseBody String checkUser(@ModelAttribute("name") String userName)
	{
		boolean result = userService.checkUser(userName);
		if(result)
		{
			return "success";
		}
		else{
			return "failure";
		}
	}
}
