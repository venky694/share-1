package com.exam;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.exam.admin.Employee;
import com.exam.admin.EmployeeDAO;
import com.exam.diagnostic.DiagnosticDAO;
import com.exam.medicine.Medicine;
import com.exam.medicine.medicineDAO;

@Controller
public class adminController {
	String email = "";
	EmployeeDAO dao = new EmployeeDAO();
	@Autowired
	medicineDAO mdao;
	@Autowired
	DiagnosticDAO diagnostic;

	// today date
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	LocalDate localDate = LocalDate.now();
	String date = (dtf.format(localDate));

	public adminController() {

	}

	@RequestMapping("adminHome")
	public String adminHome(Model md) {
		homepage(md);
		return "admin_module/adminHome";
	}

	@RequestMapping("adminHeader")
	public String adminHeader(Model model) {
		String[] name=dao.getDoctorName(email).split(" ");
		
		model.addAttribute("name", name[1]);
		return "headerAndFooter/adminHeader";
	}

	@RequestMapping("addEmployee")
	public String addEmployee(Model model) {
		model.addAttribute("id", dao.getMaxEmployeeId());
		return "admin_module/addEmployee";
	}

	@RequestMapping("addDoctor")
	public String addDoctor() {
		return "admin_module/addDoctor";
	}

	@RequestMapping("showAllEmployee")
	public String showAll(Model model) {
		model.addAttribute("object", dao.showAllEmployee());
		return "admin_module/showAllEmployee";
	}
	@RequestMapping("showAllDoctor")
	public String showAllDoctor(Model model) {
		model.addAttribute("object", dao.showAllDoctors());
		return "admin_module/showAllDoctors";
	}
	@RequestMapping("goUpdateForm")
	public String goUpdateForm(Model model, @ModelAttribute() Employee emp) {
		model.addAttribute("object", dao.getEmployeeId(emp.getId()));
		return "admin_module/updateForm";
	}
	@RequestMapping("updateEmployee")
	public String updateEmployee(Model model, @ModelAttribute() Employee emp) {
		String msg=dao.updateEployeeInformation(emp);
		model.addAttribute("object", dao.showAllEmployee());
		model.addAttribute("msg", msg);
		return "admin_module/showAllEmployee";
	}

	@RequestMapping("/forms")
	public String addNewBook(Model model) {
		return "pharmacy_Department/addNewBook";
	}

	@RequestMapping(value = "saveEmployee", method = RequestMethod.POST)
	public String saveEmployee(@ModelAttribute() Employee object, Model model) {
		dao.saveEmployee(object);
		System.out.println(object.toString());
		model.addAttribute("msg", "Employee add successfulley");
		model.addAttribute("id", dao.getMaxEmployeeId());
		return "admin_module/addEmployee";
	}
	@RequestMapping(value = "salarySheet")
	public String salarySheet(@ModelAttribute() Employee object, Model model) {
		model.addAttribute("object", dao.getBasicSalray());
		return "admin_module/salarySheet";
	}

	public void homepage(Model model) {	
		// Get Today Date
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		String date = (dtf.format(localDate));
		
		model.addAttribute("totalDoctor", dao.getTotalDoctor() + 20);
		model.addAttribute("nurse", dao.getTotalDoctor() + 32);
		model.addAttribute("totalPatient", dao.getTotalPatient());
		model.addAttribute("totalAdmittedPatient", dao.getTotalAdmittedPatient());
		model.addAttribute("todayPatient", dao.getTodayAppointment());
		model.addAttribute("todayTotalMedicineSale", "Tk. " + mdao.getTodaySaleTotal(date).get(2));
		model.addAttribute("abc", dao.getDailyPatientAppointment());
		model.addAttribute("todayDiagnosticRevenue", diagnostic.getDailyTotalDignosticIncome(date).get(2));
		
	

	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String finstudent(Model md, ModelMap model, @ModelAttribute("Employee") Employee employee) {
		String log = "";
		try {
			String st = dao.login(employee.getEmail(), employee.getPassword());
			if ("Success".equals(st)) {
				email = employee.getEmail();
				md.addAttribute("email", email);
				homepage(md);
				log = "admin_module/adminHome";
			} else {
				md.addAttribute("alert", "Id is wrong");
				log = "admin_module/adminLogin";
			}
		} catch (Exception e) {

			System.out.println(e);
		}
		return log;
	}

	@RequestMapping("deletes")
	public String delete(@ModelAttribute() Medicine object, Model model) {
		dao.delete(object.getId());
		return "pharmacy_Department/showAll";
	}

	@RequestMapping(value = "/searchs", method = RequestMethod.POST)
	public String search(Model md, String name) {
		String result = name.trim();
		if (name.isEmpty()) {
			md.addAttribute("msg", "No Student Found");
		} else {
			md.addAttribute("object", dao.search(result));
		}
		return "pharmacy_Department/showAll";
	}

	
	
	@RequestMapping(value = "saleInGraph")
	public String saleInGraph( Model model) {
		model.addAttribute("abc", dao.getDailyMedicineSales());
		return "admin_module/saleInGraph";
	}
	
	@RequestMapping(value = "monthMedicineSaleGraph")
	public String monthMedicineSaleGraph( Model model) {
		List<Integer> list= new ArrayList<>();
		list.add(mdao.getMonthlyTotalSaleByString("2021-01-01","2021-01-30").get(0));
		list.add(mdao.getMonthlyTotalSaleByString("2021-02-01","2021-02-28").get(0));
		list.add(mdao.getMonthlyTotalSaleByString("2021-03-01","2021-03-31").get(0));
		list.add(mdao.getMonthlyTotalSaleByString("2021-04-01","2021-04-30").get(0));
		list.add(mdao.getMonthlyTotalSaleByString("2021-05-01","2021-05-31").get(0));
		list.add(mdao.getMonthlyTotalSaleByString("2021-06-01","2021-06-30").get(0));
		list.add(mdao.getMonthlyTotalSaleByString("2021-07-01","2021-07-31").get(0));
		list.add(mdao.getMonthlyTotalSaleByString("2021-08-01","2021-08-31").get(0));
		list.add(mdao.getMonthlyTotalSaleByString("2021-09-01","2021-09-30").get(0));
		list.add(mdao.getMonthlyTotalSaleByString("2021-10-01","2021-10-31").get(0));
		list.add(mdao.getMonthlyTotalSaleByString("2021-11-01","2021-11-30").get(0));
		list.add(mdao.getMonthlyTotalSaleByString("2021-12-01","2021-12-31").get(0));
		
		model.addAttribute("abc", list);
		System.out.println(list.toString());
		return "admin_module/monthMedicineSaleGraph";
	}
	
	
	@RequestMapping(value = "admin_todaySale")
	public String todaySale(Model model ) {
		
		// Get Today Date
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		String date = (dtf.format(localDate));
        
		// today all sale
		model.addAttribute("todaySale", mdao.getTodaySale(date));
		model.addAttribute("dailyTotal", mdao.getTodaySaleTotal(date));
		
		// today cash sale
		model.addAttribute("todayCashSale", mdao.getTodayCashSale(date));
		model.addAttribute("cashTotal", mdao.getTodayCashSaleTotal(date));

		// today sale Receivable or due sales
		model.addAttribute("todayDueSale", mdao.getTodayDueSale(date));
		model.addAttribute("dueTotal", mdao.getTodayDueSaleTotal(date));

		return "admin_module/todaySale";
	}

	@RequestMapping(value = "admin_searchByDate")
	public String searchByDate(Model model, @RequestParam String searchItem) {
		model.addAttribute("todaySale", mdao.getTodaySale(searchItem));
		model.addAttribute("dailyTotal", mdao.getTodaySaleTotal(searchItem));

		// today cash sale
		model.addAttribute("todayCashSale", mdao.getTodayCashSale(searchItem));
		model.addAttribute("cashTotal", mdao.getTodayCashSaleTotal(searchItem));

		// today sale Receivable or due sales
		model.addAttribute("todayDueSale", mdao.getTodayDueSale(searchItem));
		model.addAttribute("dueTotal", mdao.getTodayDueSaleTotal(searchItem));

		return "admin_module/todaySale";
	}

	@RequestMapping(value = "admin_monthlySale")
	public String monthlySale(Model model) {
		LocalDate now = LocalDate.now();

		// get first date and last date of month
		LocalDate firstDay = now.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDay = now.with(TemporalAdjusters.lastDayOfMonth());	
		
		// today all sale
		model.addAttribute("monthlySale", mdao.getMonthlySale(firstDay,lastDay));
		model.addAttribute("monthlyTotal", mdao.getMonthlyTotalSaleByMonth(firstDay,lastDay));
		return "admin_module/MonthlySale";
	}

	@RequestMapping(value = "admin_searchMonthlySale", method = RequestMethod.POST)
	public String searchMonthlySale(Model model, @RequestParam String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		LocalDate month = LocalDate.parse(date, formatter);

		LocalDate firstDay = month.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDay = month.with(TemporalAdjusters.lastDayOfMonth());
		
		//final DecimalFormat df = new DecimalFormat("0.00");
		// today all sale
		model.addAttribute("monthlySale", mdao.getMonthlySale(firstDay,lastDay));
		model.addAttribute("monthlyTotal", mdao.getMonthlyTotalSaleByMonth(firstDay,lastDay));
		return "admin_module/MonthlySale";
	}
	
	
	@RequestMapping(value = "admin_yearlySale")
	public String yearlySale(Model model) {	
		model.addAttribute("daylySale", mdao.getDailySale());
		model.addAttribute("october", mdao.getMonthlyTotalSaleByString("2021-10-01","2021-10-31").get(0));
		model.addAttribute("november", mdao.getMonthlyTotalSaleByString("2021-11-01","2021-11-30").get(0));
		model.addAttribute("december", mdao.getMonthlyTotalSaleByString("2021-12-01","2021-12-31").get(0));
		model.addAttribute("yearlyToal",mdao.getMonthlyTotalSaleByString("2021-01-01","2021-12-31").get(0));
		return "admin_module/yearlySale";
	}


	 // Diagnostic report method ..................................................................................
	
	@RequestMapping(value = "admin_diagnostic_dailyIncome")
	public String diagnostic_dailyIncome(Model model) {

		// Get Today Date
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		String date = (dtf.format(localDate));

		// today all sale
		model.addAttribute("todaySale", diagnostic.getDailyDignosticIncome(date));
		model.addAttribute("dailyTotal", diagnostic.getDailyTotalDignosticIncome(date));

		// today cash sale
		model.addAttribute("todayCashSale", diagnostic.getDailyCashDignosticIncome(date));
		model.addAttribute("cashTotal", diagnostic.getDailyTotalCashDignosticIncome(date));

		// today sale Receivable or due sales
		model.addAttribute("todayDueSale", diagnostic.getDailyAcrruedDignosticIncome(date));
		model.addAttribute("dueTotal", diagnostic.getDailyAccruedTotalDignosticIncome(date));

		return "admin_module/dailyDiagnosticIncome";
	}

	@RequestMapping(value = "admin_diagnostic_searchByDate")
	public String diagnostic_searchByDate(Model model, @RequestParam String searchItem) {
		// today all sale
		model.addAttribute("todaySale", diagnostic.getDailyDignosticIncome(searchItem));
		model.addAttribute("dailyTotal", diagnostic.getDailyTotalDignosticIncome(searchItem));

		// today cash sale
		model.addAttribute("todayCashSale", diagnostic.getDailyCashDignosticIncome(searchItem));
		model.addAttribute("cashTotal", diagnostic.getDailyTotalCashDignosticIncome(searchItem));

		// today sale Receivable or due sales
		model.addAttribute("todayDueSale", diagnostic.getDailyAcrruedDignosticIncome(searchItem));
		model.addAttribute("dueTotal", diagnostic.getDailyAccruedTotalDignosticIncome(searchItem));

		return "admin_module/dailyDiagnosticIncome";
	}

	@RequestMapping(value = "admin_monthlyDiagnosticIncome")
	public String monthlyDiagnosticIncome(Model model) {
		LocalDate now = LocalDate.now();

		// get first date and last date of month
		LocalDate firstDay = now.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDay = now.with(TemporalAdjusters.lastDayOfMonth());

		// today all sale
		model.addAttribute("monthlySale", diagnostic.getMonthlyDiagnosticIncome(firstDay,lastDay));
		model.addAttribute("monthlyTotal", diagnostic.getMonthlyTotalDiagnosticIncome(firstDay,lastDay));
		return "admin_module/monthlyDiagnosticIncome";
	}

	@RequestMapping(value = "admin_diagnostic_searchMonthlyIncome", method = RequestMethod.POST)
	public String diagnostic_searchMonthlyIncome(Model model, @RequestParam String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate month = LocalDate.parse(date, formatter);

		LocalDate firstDay = month.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDay = month.with(TemporalAdjusters.lastDayOfMonth());

		model.addAttribute("monthlySale", diagnostic.getMonthlyDiagnosticIncome(firstDay,lastDay));
		model.addAttribute("monthlyTotal", diagnostic.getMonthlyTotalDiagnosticIncome(firstDay,lastDay));
		return "admin_module/monthlyDiagnosticIncome";
	}

	@RequestMapping(value = "admin_yearlyDiagnosticRevenue")
	public String yearlyDiagnosticRevenue(Model model) {
		model.addAttribute("daylySale", diagnostic.getDailyIncomeOverTheYear());
		model.addAttribute("october", diagnostic.getMonthlyTotalIncomeOverTheYear("2021-10-01","2021-10-31").get(0));
		model.addAttribute("november", diagnostic.getMonthlyTotalIncomeOverTheYear("2021-11-01","2021-11-30").get(0));
		model.addAttribute("december", diagnostic.getMonthlyTotalIncomeOverTheYear("2021-12-01","2021-12-31").get(0));
		model.addAttribute("yearlyToal",diagnostic.getMonthlyTotalIncomeOverTheYear("2021-01-01","2021-12-31").get(0));
		return "admin_module/yearlyDiagnosticRevenue";
	}
	
	
	@RequestMapping(value = "admin_dailyDiagnosticRevenueGraph")
	public String dailyDiagnosticRevenueGraph( Model model) {
		model.addAttribute("abc", diagnostic.getDailyDiagnosticeRevenueOverTheMonth());
		return "admin_module/dailyDiagnosticRevenueGraph";
	}
	
	@RequestMapping(value = "admin_monthlyDiagnosticRevenueGraph")
	public String monthlyDiagnosticRevenueGraph( Model model) {
		List<Integer> list= new ArrayList<>();
		list.add(diagnostic.getMonthlyTotalIncomeOverTheYear("2021-01-01","2021-01-30").get(0));
		list.add(diagnostic.getMonthlyTotalIncomeOverTheYear("2021-02-01","2021-02-28").get(0));
		list.add(diagnostic.getMonthlyTotalIncomeOverTheYear("2021-03-01","2021-03-31").get(0));
		list.add(diagnostic.getMonthlyTotalIncomeOverTheYear("2021-04-01","2021-04-30").get(0));
		list.add(diagnostic.getMonthlyTotalIncomeOverTheYear("2021-05-01","2021-05-31").get(0));
		list.add(diagnostic.getMonthlyTotalIncomeOverTheYear("2021-06-01","2021-06-30").get(0));
		list.add(diagnostic.getMonthlyTotalIncomeOverTheYear("2021-07-01","2021-07-31").get(0));
		list.add(diagnostic.getMonthlyTotalIncomeOverTheYear("2021-08-01","2021-08-31").get(0));
		list.add(diagnostic.getMonthlyTotalIncomeOverTheYear("2021-09-01","2021-09-30").get(0));
		list.add(diagnostic.getMonthlyTotalIncomeOverTheYear("2021-10-01","2021-10-31").get(0));
		list.add(diagnostic.getMonthlyTotalIncomeOverTheYear("2021-11-01","2021-11-30").get(0));
		list.add(diagnostic.getMonthlyTotalIncomeOverTheYear("2021-12-01","2021-12-31").get(0));
		
		model.addAttribute("abc", list);
		System.out.println(list.toString());
		return "admin_module/monthlyDiagnosticRevenueGraph";
	}
}
