package com.exam;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NavigationController {
	
	@RequestMapping("mainHeader") 
	public String mainHeader() {
		return "headerAndFooter/mHeader";
	}

	@RequestMapping("loginHeader") 
	public String loginHeader() {
		return "headerAndFooter/loginHeader";
	}

	@RequestMapping("loginPage") 
	public String loginPage() {
		return "admin_module/adminLogin";
	}
	
	@RequestMapping("receptionsHeader") 
	public String receptionsHeader() {
		return "headerAndFooter/receptionsHeader";
	}
	
	@RequestMapping("medicineHome") 
	public String medicineHome() {
		return "pharmacy_Department/medicineHome";
	}
	
	@RequestMapping("receptionsHome") 
	public String receptionsHome() {
		return "receptions_module/receptionsHome";
	}
	
}
