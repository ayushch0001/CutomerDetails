package com.assement.CusotmerApplication.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.assement.CusotmerApplication.entity.Customer;
import com.assement.CusotmerApplication.entity.User;
import com.assement.CusotmerApplication.service.CustomerService;


@Controller
@RequestMapping("/customer")
public class CustomerController {

	
	
	@Autowired
	CustomerService customerService;
	
	@RequestMapping("/login")
	public String getLogin(Model model)
	{
		User user = new User();
		model.addAttribute("user",user);
		return "loginPage";
	}
	
	@RequestMapping("/token")
	public String getToken(@RequestParam("login_id") String login_id,@RequestParam("password") String password)
	{
		User user = new User(login_id,password);
		customerService.getToken(user);
		return "redirect:/customer/customerList";
	}
	
	
	@RequestMapping("/newCustomer")
	public String createCustomer(Model model)
	{
		Customer customer = new Customer();
		model.addAttribute("customer",customer);
		return "customerDetails";
	}
	
	@RequestMapping("/addCustomer")
	public String addCustomer(@ModelAttribute Customer customer)
	{
		customerService.createCustomer(customer);
		return "redirect:/customer/customerList";
	}
	

	@RequestMapping("/customerList")
	public String listOfCustomer(Model model)
	{
		ArrayList<HashMap<String,String>> list = customerService.listOfCustomer();
		model.addAttribute("list", list);
		return "listPage";
	}
	
	@RequestMapping("/updateCustomer")
	public String updateCustomer(@RequestParam String uuid,Model model)
	{		
		Customer customer = new Customer();
		
		model.addAttribute("customer",customer);
		customerService.updateCustomer(uuid,customer);
		return "customerDetails";
	}
	

	@RequestMapping("/delete")
	public String deleteCustomer(@RequestParam String uuid)
	{
		customerService.deleteCustomer(uuid);
		return "redirect:/customer/customerList";
	}
}
