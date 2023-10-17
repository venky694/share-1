package com.exam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.exam.diagnostic.DiagnosticDAO;
import com.exam.diagnostic.Test_invoice_details;
import com.exam.doctor.DoctorDAO;
import com.exam.doctor.test_name;
import com.exam.numberConverter.NumberToWardConverter;
import com.exam.receptions.Patient;
import com.exam.receptions.PatientDAO;

@Controller
public class DiagnosticController {

	DoctorDAO doctors = new DoctorDAO();
	PatientDAO patients = new PatientDAO();
	@Autowired
	DiagnosticDAO diagnostic;
	@Autowired
	NumberToWardConverter converter;
	int voucher_no = 0;

	List<Patient> patient = new ArrayList<>();

	@RequestMapping("diagnosticBill")
	public String diagnosticBill(Model model, @ModelAttribute("") Test_invoice_details object1, Patient object,
			test_name test) {
		Double total = diagnostic.getInvoiceTotal(voucher_no).get(0).getTotal();
		if (total <= 0) {

			// get patient information
			model.addAttribute("object", patient);

			abc(model, object, test);

			model.addAttribute("error", "Invoice is empty");
			return "diagnostic_module/createBill";
		} else {

			// save to tesInvoice table
			diagnostic.saveInvoiceDetailsToTestInvoice(diagnostic.getInvoiceTotal(voucher_no), patient.get(0).getId(),
					voucher_no);

			// get patient information
			model.addAttribute("object", patient);

			// get next voucher number and send to view
			model.addAttribute("voucher_no", voucher_no);

			// get today date and send to view
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			LocalDate localDate = LocalDate.now();
			String date = "Date : " + (dtf.format(localDate));
			model.addAttribute("todayDate", date);

			// get all selected test and send to view
			List<Test_invoice_details> list = new ArrayList<>();
			list.addAll(diagnostic.getAllSelectedTest(voucher_no));
			model.addAttribute("invoiceItem", list);

			// get Invoice Total
			model.addAttribute("invoiceTotal", diagnostic.getInvoiceTotal(voucher_no));

			return "diagnostic_module/DiagnosticBill";

		}

	}

	public void abc(Model model, @ModelAttribute("patient") Patient object, test_name test) {

		// get patient information
		model.addAttribute("patient", patient);

		// get all test name and send to view
		List<String> testName = new ArrayList<>();
		testName = doctors.getTestName();
		model.addAttribute("name", testName);

		// get next voucher number and send to view
		voucher_no = diagnostic.getLatestVoucherNO() + 1;
		model.addAttribute("voucher_no", voucher_no);

		// get all selected test and send to view
		List<Test_invoice_details> list = new ArrayList<>();
		list.addAll(diagnostic.getAllSelectedTest(voucher_no));
		model.addAttribute("invoiceItem", list);

		// get selected test name price and send to view
		model.addAttribute("testPrice", diagnostic.getSelectedTestRate(test));

		// get Invoice Total
		model.addAttribute("invoiceTotal", diagnostic.getInvoiceTotal(voucher_no));

	}

	@RequestMapping("searchPatient")
	public String createDignosticBill(Model model, @ModelAttribute("patient") Patient object, test_name test) {
		// get patient info from database and send to view
		patient.removeAll(patient);
		patient.addAll(patients.getPatient(object.getId()));

		if (patient.size() == 0) {
			model.addAttribute("msg", "No data found, please Enter correct patient id");
			return "diagnostic_module/findPatient";
		}
		abc(model, object, test);
		return "diagnostic_module/createBill";
	}

	@RequestMapping("findTestPrice")
	public String findTestPrice(Model model, @ModelAttribute("patient") Patient object, test_name test) {
		abc(model, object, test);

		// get selected test name price and send to view
		model.addAttribute("testPrice", diagnostic.getSelectedTestRate(test));
		model.addAttribute("testName", test.getTest_name());

		return "diagnostic_module/createBill";
	}

	@RequestMapping("/addTestToVoucehrDetails")
	public String addTestToVoucehrDetails(Model model, @ModelAttribute("patient") Patient object, test_name test,
			Test_invoice_details object1) {
		// Add selected test to data base
		String success = diagnostic.insertSelectTestToVoucherDetails(object1);
		model.addAttribute("msg", success);

		abc(model, object, test);
		return "diagnostic_module/createBill";
	}

	@RequestMapping("/removeSelectedTestNames")
	public String removeSelectedTestName(Model model, @ModelAttribute("patient") Patient object, test_name test,
			Test_invoice_details object1) {
		// remove added test name
		diagnostic.removeSelectedTestName(object1.getId());

		abc(model, object, test);
		return "diagnostic_module/createBill";
	}

	@RequestMapping("diagnosticHeader")
	public String diagnosticHeader() {
		return "headerAndFooter/diagnosticHeder";
	}

	@RequestMapping("diagnosticHome")
	public String diagnosticHome() {
		return "diagnostic_module/diagnosticHome";
	}

	@RequestMapping("findPatient")
	public String findPatient() {
		return "diagnostic_module/findPatient";
	}

	@RequestMapping("findAdmittedPatient")
	public String findAdmittedPatient() {
		return "diagnostic_module/findAdmittedPatient";
	}

	@RequestMapping("searchAdmittedPatient")
	public String searchAdmittedPatient(Model model, @ModelAttribute("patient") Patient object, test_name test) {
		// get patient info from database and send to view
		patient.removeAll(patient);
		patient.addAll(patients.getAdmittedPatient(object.getId()));

		if (patient.size() == 0) {
			model.addAttribute("msg", "No data found, Please Enter correct admitted patient id. ");
			return "diagnostic_module/findAdmittedPatient";
		}
		abc(model, object, test);
		return "diagnostic_module/billForAdmittedPatient";
	}

	@RequestMapping("findTestPriceForAddmittedPatient")
	public String findTestPriceForAddmittedPatient(Model model, @ModelAttribute("patient") Patient object,
			test_name test) {
		abc(model, object, test);
		// get selected test name price and send to view
		model.addAttribute("testPrice", diagnostic.getSelectedTestRate(test));
		model.addAttribute("testName", test.getTest_name());
		return "diagnostic_module/billForAdmittedPatient";
	}

	@RequestMapping("/addTestToVoucehrDetailsForAddmittedPatient")
	public String addTestToVoucehrDetailsForAddmittedPatient(Model model, @ModelAttribute("patient") Patient object,
			test_name test, Test_invoice_details object1) {
		// Add selected test to data base
		String success = diagnostic.insertSelectTestToVoucherDetails(object1);
		model.addAttribute("msg", success);

		abc(model, object, test);
		return "diagnostic_module/billForAdmittedPatient";
	}

	@RequestMapping("/removeSelectedTestNamesForAdmittedPatient")
	public String removeSelectedTestNamesForAdmittedPatient(Model model, @ModelAttribute("patient") Patient object,
			test_name test, Test_invoice_details object1) {

		// remove added test name
		diagnostic.removeSelectedTestName(object1.getId());
		abc(model, object, test);
		return "diagnostic_module/billForAdmittedPatient";
	}

	@RequestMapping("diagnosticBillForAdmittedPatient")
	public String diagnosticBillForAdmittedPatient(Model model, @ModelAttribute("") Test_invoice_details object1,
			Patient object, test_name test) {
		Double total = diagnostic.getInvoiceTotal(voucher_no).get(0).getTotal();
		if (total <= 0) {

			// get patient information
			model.addAttribute("object", patient);
			abc(model, object, test);
			model.addAttribute("error", "Invoice is empty");
			return "diagnostic_module/createBill";
		} else {

			// save to tesInvoice table
			diagnostic.saveInvoiceDetailsToTestInvoice2(diagnostic.getInvoiceTotal(voucher_no), patient.get(0).getId(),
					voucher_no);

			// get patient information
			model.addAttribute("object", patient);

			// get next voucher number and send to view
			model.addAttribute("voucher_no", voucher_no);

			// get today date and send to view
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			LocalDate localDate = LocalDate.now();
			String date = "Date : " + (dtf.format(localDate));
			model.addAttribute("todayDate", date);

			// get all selected test and send to view
			List<Test_invoice_details> list = new ArrayList<>();
			list.addAll(diagnostic.getAllSelectedTest(voucher_no));
			model.addAttribute("invoiceItem", list);

			// get Invoice Total
			model.addAttribute("invoiceTotal", diagnostic.getInvoiceTotal(voucher_no));

			// cod for convert number to word
			Double Total = diagnostic.getInvoiceTotal(voucher_no).get(0).getTotal();
			int abc = Total.intValue();
			model.addAttribute("totalInWord", "Total in word : " + converter.numberToWord(abc) + "taka only");

			return "diagnostic_module/DiagnosticBill2";

		}

	}

	@RequestMapping(value = "diagnostic_dailyIncome")
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

		return "diagnostic_module/dailyDiagnosticIncome";
	}

	@RequestMapping(value = "diagnostic_searchByDate")
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

		return "diagnostic_module/dailyDiagnosticIncome";
	}

	@RequestMapping(value = "monthlyDiagnosticIncome")
	public String monthlyDiagnosticIncome(Model model) {
		LocalDate now = LocalDate.now();

		// get first date and last date of month
		LocalDate firstDay = now.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDay = now.with(TemporalAdjusters.lastDayOfMonth());

		// today all sale
		model.addAttribute("monthlySale", diagnostic.getMonthlyDiagnosticIncome(firstDay,lastDay));
		model.addAttribute("monthlyTotal", diagnostic.getMonthlyTotalDiagnosticIncome(firstDay,lastDay));
		return "diagnostic_module/monthlyDiagnosticIncome";
	}

	@RequestMapping(value = "diagnostic_searchMonthlyIncome", method = RequestMethod.POST)
	public String diagnostic_searchMonthlyIncome(Model model, @RequestParam String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate month = LocalDate.parse(date, formatter);

		LocalDate firstDay = month.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDay = month.with(TemporalAdjusters.lastDayOfMonth());

		model.addAttribute("monthlySale", diagnostic.getMonthlyDiagnosticIncome(firstDay,lastDay));
		model.addAttribute("monthlyTotal", diagnostic.getMonthlyTotalDiagnosticIncome(firstDay,lastDay));
		return "diagnostic_module/monthlyDiagnosticIncome";
	}

	@RequestMapping(value = "yearlyDiagnosticRevenue")
	public String yearlyDiagnosticRevenue(Model model) {
		model.addAttribute("daylySale", diagnostic.getDailyIncomeOverTheYear());
		model.addAttribute("october", diagnostic.getMonthlyTotalIncomeOverTheYear("2021-10-01","2021-10-31").get(0));
		model.addAttribute("november", diagnostic.getMonthlyTotalIncomeOverTheYear("2021-11-01","2021-11-30").get(0));
		model.addAttribute("december", diagnostic.getMonthlyTotalIncomeOverTheYear("2021-12-01","2021-12-31").get(0));
		model.addAttribute("yearlyToal",diagnostic.getMonthlyTotalIncomeOverTheYear("2021-01-01","2021-12-31").get(0));
		return "diagnostic_module/yearlyDiagnosticRevenue";
	}
	
	
	@RequestMapping(value = "dailyDiagnosticRevenueGraph")
	public String dailyDiagnosticRevenueGraph( Model model) {
		model.addAttribute("abc", diagnostic.getDailyDiagnosticeRevenueOverTheMonth());
		return "diagnostic_module/dailyDiagnosticRevenueGraph";
	}
	
	@RequestMapping(value = "monthlyDiagnosticRevenueGraph")
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
		return "diagnostic_module/monthlyDiagnosticRevenueGraph";
	}

}
