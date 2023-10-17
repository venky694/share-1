package com.exam;


import java.text.DecimalFormat;
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
import org.springframework.web.bind.annotation.ResponseBody;
import com.exam.admin.EmployeeDAO;
import com.exam.medicine.Medicine;
import com.exam.medicine.MedicineSaleDetails;
import com.exam.medicine.Medicine_sale;
import com.exam.medicine.medicineDAO;
import com.exam.numberConverter.NumberToWardConverter;
import com.exam.receptions.Patient;

@Controller
public class MedicineController {
	List<String> medicineNamelist = new ArrayList<>();
	String medichinName = "";

	@Autowired
	medicineDAO dao;

	@Autowired
	EmployeeDAO emdao;

	int voucherNo = 0;
	int medicineStock = 0;
	int medicineID = 0;
	int patientID = 0;

	@RequestMapping("/abcd")
	public String abcd(Model model) {
		return "pharmacy_Department/abc";
	}

	@RequestMapping("addNewMedicine")
	public String newBook(Model model) {
		model.addAttribute("medicineID", dao.getLatestMedicineID() + 1);
		return "pharmacy_Department/addNewMedicine";
	}

	@RequestMapping("/save")
	@ResponseBody
	public String save() {
		String ss = " this from controller";
		return ss;
	}

	public void xyz(Model model) {

		// send all medicine name to view
		model.addAttribute("name", medicineNamelist);

		// get voucher no;
		voucherNo = dao.getVoucherNo() + 1;
		model.addAttribute("voucherNo", voucherNo);

		// Get all medicine name from Voucher data
		List<MedicineSaleDetails> list3;
		list3 = dao.sowVoucherMedicine(voucherNo);
		model.addAttribute("voucherItem", list3);

		// get total voucher value and send to the view
		List<Double> total = dao.getSubtotalDiscountAndGrandTotal(voucherNo);
		model.addAttribute("subtotal", total.get(0));
		model.addAttribute("discount", total.get(1));
		model.addAttribute("total", total.get(2));

	}

	@RequestMapping("removeMedecineFromCart")
	public String removeMedecineFromCart(@ModelAttribute() MedicineSaleDetails object, Model model) {
		try {
			// remove from voucher cart
			dao.removeMedicineFromVoucher(voucherNo, object);
			xyz(model);
		} catch (Exception e) {
			System.out.println(e);
		}
		return "pharmacy_Department/outSideSale";
	}

	@RequestMapping("removeInsideSaleMediciene")
	public String removeInsideSaleMediciene(@ModelAttribute() MedicineSaleDetails object, Model model) {
		try {
			// remove from voucher cart
			dao.removeMedicineFromVoucher(voucherNo, object);
			xyz(model);
		} catch (Exception e) {
			System.out.println(e);
		}
		return "pharmacy_Department/insideSale";
	}

	public void getMedicinePrice(Model model, Medicine medicine) {

		if (medicine.getName().isEmpty()) {

			xyz(model);
			model.addAttribute("wrong", "Plese select medicine name");
		} else if (!medicine.getName().isEmpty()) {

			// get medicine price and stock quantity
			List<Medicine> list = new ArrayList<>();
			list.removeAll(list);

			list.addAll(dao.getMedicinePrice(medicine.getName()));

			// Store medicine name, id and Stock quantity from data base and decrease stock
			// when add to
			// cart
			medicineID = list.get(0).getId();
			medicineStock = list.get(0).getQuantity();
			medichinName = list.get(0).getName();

			model.addAttribute("medicineName", medichinName);
			model.addAttribute("medicineStock", medicineStock);
			model.addAttribute("price", list.get(0).getPrice());

			xyz(model);
		}
	}

	@RequestMapping("findMedicinePrice")
	public String getMedicinePriceForOutSideSale(Model model, Medicine medicine) {
		getMedicinePrice(model, medicine);
		return "pharmacy_Department/outSideSale";
	}

	@RequestMapping("findMedicinePriceInsideSale")
	public String getMedicinePriceForInsideSale(Model model, Medicine medicine) {
		getMedicinePrice(model, medicine);
		return "pharmacy_Department/insideSale";
	}

	@RequestMapping("outSideSale")
	public String outSideSale(Model model) {

		// get all medicine name from medicine table
		medicineNamelist.removeAll(medicineNamelist);
		medicineNamelist.addAll(dao.getMedicineName());

		xyz(model);

		return "pharmacy_Department/outSideSale";
	}

	public void addSelectedMedicine(Model model, MedicineSaleDetails saleMedcinie) {
		// get voucher no;
		voucherNo = dao.getVoucherNo() + 1;
		model.addAttribute("voucherNo", voucherNo);
		if (saleMedcinie.getQuantity() == 0 || saleMedcinie.getQuantity() < 0 || saleMedcinie.getPrice() < 0) {

			// if quantity is more then stock then again search medicine by name and set to
			// the view
			xyz(model);
			model.addAttribute("msg", "Please enter a valid quantity");

		} else if (saleMedcinie.getQuantity() < medicineStock) {

			// save data to voucher cart details
			String msg = dao.saveMedicineSaleDetails(saleMedcinie, voucherNo, medicineStock, medicineID);

			xyz(model);
			model.addAttribute("msg", msg);

		} else if (saleMedcinie.getQuantity() > medicineStock) {
			// if quantity is more then stock then again search medicine by name and set to
			// the view
			xyz(model);
			model.addAttribute("msg", "Stock not suficent");

		}

	}

	@RequestMapping("add")
	public String addOutsideSale(Model model, MedicineSaleDetails saleMedcinie) {
		addSelectedMedicine(model, saleMedcinie);
		return "pharmacy_Department/outSideSale";
	}

	@RequestMapping("addInsideSale")
	public String addInsideSale(Model model, MedicineSaleDetails saleMedcinie) {
		addSelectedMedicine(model, saleMedcinie);
		return "pharmacy_Department/insideSale";
	}

	@RequestMapping("proccedToBill")
	public String proccedToBill(Model model) {

		// get total voucher value and send to the view
		List<Double> total = dao.getSubtotalDiscountAndGrandTotal(voucherNo);
		model.addAttribute("subtotal", total.get(0));
		model.addAttribute("discount", total.get(1));
		model.addAttribute("total", total.get(2));

		// Get all medicine list from voucher cart
		List<MedicineSaleDetails> list3;
		list3 = dao.sowVoucherMedicine(voucherNo);
		model.addAttribute("voucherItem", list3);
		model.addAttribute("voucherNo", voucherNo);
		return "pharmacy_Department/proccedToBill";
	}

	@RequestMapping(value = "SaveAndBills", method = RequestMethod.POST)
	public String SaveAndBill(Model model, Medicine_sale sale) {
		if (sale.getMobile().length() == 11) {
			NumberToWardConverter converter = new NumberToWardConverter();

			// Get Today Date and send to view
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate localDate = LocalDate.now();
			String date = (dtf.format(localDate));
			model.addAttribute("todayDate", date);

			// pass customer information to the view
			model.addAttribute("costomerInfo", sale);

			// get total voucher value and send to the view
			List<Double> total = dao.getSubtotalDiscountAndGrandTotal(voucherNo);
			model.addAttribute("subtotal", total.get(0));
			model.addAttribute("discount", total.get(1));
			model.addAttribute("total", total.get(2));

			// save sale information to the medicine sate table
			dao.saveSaleDetailes(sale, total);

			// Get all medicine list from voucher cart
			List<MedicineSaleDetails> list3;
			list3 = dao.sowVoucherMedicine(voucherNo);
			model.addAttribute("voucherItem", list3);
			model.addAttribute("voucherNo", voucherNo);

			String totalInWord = "Total in word: " + converter.numberToWord((total.get(2).intValue())) + " taka only. ";
			model.addAttribute("totalInWord", totalInWord);

			return "pharmacy_Department/medicineBill";

		}

		proccedToBill(model);
		model.addAttribute("msg", "Mobile number not currect");

		return "pharmacy_Department/proccedToBill";
	}

	@RequestMapping(value = "searchInvoice")
	public String searchInvoice(Model md) {

		return "pharmacy_Department/searchInvoice";
	}

	
	@RequestMapping(value = "searchMedicineIvoice", method = RequestMethod.POST)
	public String searchMedicineIvoice(@RequestParam int voucher_no, Model model) {
		NumberToWardConverter converter = new NumberToWardConverter();
		if (dao.getCustomerInfo(voucher_no).size() == 0 || String.valueOf(voucher_no).length()>8) {
			model.addAttribute("msg", "Invalid invoice number, Please try again.");
			return "pharmacy_Department/searchInvoice";
		}
		
		// Get invoice Date and send to view
		model.addAttribute("todayDate", dao.getCustomerInfo(voucher_no).get(0).getDate());

		// pass customer information to the view
		model.addAttribute("costomerInfo", dao.getCustomerInfo(voucher_no).get(0));
		
		System.out.println( dao.getCustomerInfo(voucher_no).get(0));

		// get total voucher value and send to the view
		List<Double> total = dao.getSubtotalDiscountAndGrandTotal(voucher_no);
		model.addAttribute("subtotal", total.get(0));
		model.addAttribute("discount", total.get(1));
		model.addAttribute("total", total.get(2));

		// Get all medicine list from voucher cart
		List<MedicineSaleDetails> list3;
		list3 = dao.sowVoucherMedicine(voucher_no);
		model.addAttribute("voucherItem", list3);
		model.addAttribute("voucherNo", voucher_no);

		String totalInWord = "Total in word: " + converter.numberToWord((total.get(2).intValue())) + " taka only. ";
		model.addAttribute("totalInWord", totalInWord);
		
		if (!dao.getCustomerInfo(voucher_no).get(0).getStatus().equals("Out Side Sale")  ) {
			model.addAttribute("patient", dao.getPatientMedicineInvoice(Integer.parseInt(dao.getCustomerInfo(voucher_no).get(0).getStatus())));
			return "pharmacy_Department/insideSaleBill";
		}

		return "pharmacy_Department/medicineBill";
	}

	@RequestMapping("header")
	public String header() {
		return "headerAndFooter/pharmacyHeader";
	}

	@RequestMapping("index")
	public String home() {
		return "index";
	}

	@RequestMapping("findMedicine")
	public String findBook() {
		return "pharmacy_Department/findMedicine";
	}

	@RequestMapping("footer")
	public String footer() {
		return "headerAndFooter/footer";
	}

	@RequestMapping("showAll")
	public String showAll(Model model) {
		model.addAttribute("object", dao.show());
		return "pharmacy_Department/abcd";
	}

	@RequestMapping("reorder_medicineList")
	public String reorder_medicineList(Model model) {
		model.addAttribute("object", dao.reorderMedicineList());
		return "pharmacy_Department/reorder_medicineList";
	}

	@RequestMapping("/form")
	public String addNewBook(Model model) {
		return "pharmacy_Department/addNewBook";
	}

	@RequestMapping(value = "showAll", method = RequestMethod.POST)
	public String save(@ModelAttribute() Medicine object, Model model) {
		dao.insert(object);
		model.addAttribute("object", dao.show());
		model.addAttribute("msg", "Medechine add successfulley");
		return "pharmacy_Department/addNewMedicine";
	}

	@RequestMapping("delete")
	public String delete(@ModelAttribute() Medicine object, Model model) {
		dao.delete(object.getId());
		model.addAttribute("object", dao.show());
		return "pharmacy_Department/showAll";
	}

	@RequestMapping(value = "getMedicine", method = RequestMethod.POST)
	public String getMedicine(@ModelAttribute("medicine") Medicine object, Model model) {
		model.addAttribute("object", dao.getById(object.getId()));
		return "pharmacy_Department/update";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String search(Model md, String name) {
		String result = name.trim();
		if (name.isEmpty()) {
			md.addAttribute("msg", "No Student Found");
		} else {
			md.addAttribute("object", dao.search(result));
		}
		return "pharmacy_Department/showAll";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@ModelAttribute() Medicine object, Model model) {
		dao.update(object);
		model.addAttribute("object", dao.show());
		model.addAttribute("msg", "Medechine Update successfulley");
		return "pharmacy_Department/FindMedicine";
	}

//	@RequestMapping(value = "outSideSale", method = RequestMethod.POST)
//	public String addToVoucher(@ModelAttribute() Medicine object, Model model) {
//
//		// get all medicine name and send to view
//		model.addAttribute("object", dao.show());
//
//		// get all selected medicine and send to view
//		model.addAttribute("cart", electedmmdicineName);
//
//		return "pharmacy_Department/outSideSale";
//	}

	@RequestMapping("FindPatient")
	public String insideSale() {
		return "pharmacy_Department/FindPatient";
	}

	List<Patient> patientinfo = new ArrayList<>();

	@RequestMapping("searchPatientForBill")
	public String searchPatientForBill(@RequestParam int patientId, Model model) {
		patientinfo.removeAll(patientinfo);
		patientinfo.addAll(dao.getAdmittedPatient(patientId));

		if (patientinfo.size() == 0) {
			model.addAttribute("msg", "Patient id not correct or  Patient maybe not admitted");
			return "pharmacy_Department/FindPatient";
		} else {
			model.addAttribute("admittionInfo", dao.getPatientAdmissionInfo(patientId));
			// get patient info from database and send to view
			model.addAttribute("patient", patientinfo);

			// get total voucher value and send to the view
			List<Double> total = dao.getSubtotalDiscountAndGrandTotal(voucherNo);
			model.addAttribute("subtotal", total.get(0));
			model.addAttribute("discount", total.get(1));
			model.addAttribute("total", total.get(2));

			// Get all medicine list from voucher cart
			List<MedicineSaleDetails> list3;
			list3 = dao.sowVoucherMedicine(voucherNo);
			model.addAttribute("voucherItem", list3);
			model.addAttribute("voucherNo", voucherNo);

		}

		return "pharmacy_Department/GetPatientInfoForBill";
	}

	@RequestMapping(value = "insidSale")
	public String insideSales(Model model) {

		// get all medicine name from medicine table
		medicineNamelist.removeAll(medicineNamelist);
		medicineNamelist.addAll(dao.getMedicineName());

		xyz(model);

		return "pharmacy_Department/insideSale";
	}

	@RequestMapping(value = "SaveInsideSaleAndBill", method = RequestMethod.POST)
	public String SaveInsideSaleAndBill(Model model, Medicine_sale sale) {

		NumberToWardConverter converter = new NumberToWardConverter();

		// Get Today Date and send to view
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate localDate = LocalDate.now();
		String date = (dtf.format(localDate));
		model.addAttribute("todayDate", date);

		// pass customer information to the view
		model.addAttribute("patient", patientinfo);

		// get total voucher value and send to the view
		List<Double> total = dao.getSubtotalDiscountAndGrandTotal(voucherNo);
		model.addAttribute("subtotal", total.get(0));
		model.addAttribute("discount", total.get(1));
		model.addAttribute("total", total.get(2));

		// save sale information to the medicine sate table
		String msg = dao.saveInsideSale(patientinfo, total, voucherNo);
		model.addAttribute("msg", msg);

		// Get all medicine list from voucher cart
		List<MedicineSaleDetails> list3;
		list3 = dao.sowVoucherMedicine(voucherNo);
		model.addAttribute("voucherItem", list3);
		model.addAttribute("voucherNo", voucherNo);

		String totalInWord = "Total in word: " + converter.numberToWord((total.get(2).intValue())) + " taka only. ";
		model.addAttribute("totalInWord", totalInWord);

		return "pharmacy_Department/insideSaleBill";
	}

	@RequestMapping(value = "todaySale")
	public String todaySale(Model model) {

		// Get Today Date
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		String date = (dtf.format(localDate));

		// today all sale
		model.addAttribute("todaySale", dao.getTodaySale(date));
		model.addAttribute("subtotal", dao.getTodaySaleTotal(date).get(0));
		model.addAttribute("discount", dao.getTodaySaleTotal(date).get(1));
		model.addAttribute("total", dao.getTodaySaleTotal(date).get(2));

		// today cash sale
		model.addAttribute("todayCashSale", dao.getTodayCashSale(date));
		model.addAttribute("cashSubtotal", dao.getTodayCashSaleTotal(date).get(0));
		model.addAttribute("cashDiscount", dao.getTodayCashSaleTotal(date).get(1));
		model.addAttribute("cashTotal", dao.getTodayCashSaleTotal(date).get(2));

		// today sale Receivable or due sales
		model.addAttribute("todayDueSale", dao.getTodayDueSale(date));
		model.addAttribute("dueSubtotal", dao.getTodayDueSaleTotal(date).get(0));
		model.addAttribute("dueDiscount", dao.getTodayDueSaleTotal(date).get(1));
		model.addAttribute("dueTotal", dao.getTodayDueSaleTotal(date).get(2));

		return "pharmacy_Department/todaySale";
	}

	@RequestMapping(value = "searchByDate")
	public String searchByDate(Model model, @RequestParam String searchItem) {
		final DecimalFormat df = new DecimalFormat("0.00");
		model.addAttribute("todaySale", dao.getTodaySale(searchItem));
		model.addAttribute("subtotal", dao.getTodaySaleTotal(searchItem).get(0));
		model.addAttribute("discount", df.format(dao.getTodaySaleTotal(searchItem).get(1)));
		model.addAttribute("total", dao.getTodaySaleTotal(searchItem).get(2));

		// today cash sale
		model.addAttribute("todayCashSale", dao.getTodayCashSale(searchItem));
		model.addAttribute("cashSubtotal", dao.getTodayCashSaleTotal(searchItem).get(0));
		model.addAttribute("cashDiscount", df.format(dao.getTodayCashSaleTotal(searchItem).get(1)));
		model.addAttribute("cashTotal", dao.getTodayCashSaleTotal(searchItem).get(2));

		// today sale Receivable or due sales
		model.addAttribute("todayDueSale", dao.getTodayDueSale(searchItem));
		model.addAttribute("dueSubtotal", dao.getTodayDueSaleTotal(searchItem).get(0));
		model.addAttribute("dueDiscount", df.format(dao.getTodayDueSaleTotal(searchItem).get(1)));
		model.addAttribute("dueTotal", dao.getTodayDueSaleTotal(searchItem).get(2));

		return "pharmacy_Department/todaySale";
	}

	@RequestMapping(value = "monthlySale")
	public String monthlySale(Model model) {
		LocalDate now = LocalDate.now();

		// get first date and last date of month
		LocalDate firstDay = now.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDay = now.with(TemporalAdjusters.lastDayOfMonth());

		final DecimalFormat df = new DecimalFormat("0.00");
		// today all sale
		model.addAttribute("monthlySale", dao.getMonthlySale(firstDay, lastDay));
		model.addAttribute("monthlySubtotal", dao.getMonthlyTotalSaleByMonth(firstDay, lastDay).get(0));
		model.addAttribute("monthlyDiscount", df.format(dao.getMonthlyTotalSaleByMonth(firstDay, lastDay).get(1)));
		model.addAttribute("monthlyTotal", dao.getMonthlyTotalSaleByMonth(firstDay, lastDay).get(2));

		return "pharmacy_Department/MonthlySale";
	}

	@RequestMapping(value = "searchMonthlySale", method = RequestMethod.POST)
	public String searchMonthlySale(Model model, @RequestParam String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate month = LocalDate.parse(date, formatter);

		LocalDate firstDay = month.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDay = month.with(TemporalAdjusters.lastDayOfMonth());

		final DecimalFormat df = new DecimalFormat("0.00");
		// today all sale
		model.addAttribute("monthlySale", dao.getMonthlySale(firstDay, lastDay));
		model.addAttribute("monthlySubtotal", dao.getMonthlyTotalSaleByMonth(firstDay, lastDay).get(0));
		model.addAttribute("monthlyDiscount", df.format(dao.getMonthlyTotalSaleByMonth(firstDay, lastDay).get(1)));
		model.addAttribute("monthlyTotal", dao.getMonthlyTotalSaleByMonth(firstDay, lastDay).get(2));

		return "pharmacy_Department/MonthlySale";
	}

	@RequestMapping(value = "yearlySale")
	public String yearlySale(Model model) {
		model.addAttribute("daylySale", dao.getDailySale());
		model.addAttribute("october", dao.getMonthlyTotalSaleByString("2021-10-01", "2021-10-31").get(0));
		model.addAttribute("november", dao.getMonthlyTotalSaleByString("2021-11-01", "2021-11-30").get(0));
		model.addAttribute("december", dao.getMonthlyTotalSaleByString("2021-12-01", "2021-12-31").get(0));
		model.addAttribute("yearlyToal", dao.getMonthlyTotalSaleByString("2021-01-01", "2021-12-31").get(0));
		return "pharmacy_Department/yearlySale";
	}

	@RequestMapping("dailysaleGraph")
	public String dailysaleGraph(Model model) {

		model.addAttribute("abc", emdao.getDailyMedicineSales());
		return "pharmacy_Department/saleInGraph";
	}

	@RequestMapping(value = "monthlySaleGraph")
	public String monthMedicineSaleGraph(Model model) {
		List<Integer> list = new ArrayList<>();
		list.add(dao.getMonthlyTotalSaleByString("2021-01-01", "2021-01-30").get(0));
		list.add(dao.getMonthlyTotalSaleByString("2021-02-01", "2021-02-28").get(0));
		list.add(dao.getMonthlyTotalSaleByString("2021-03-01", "2021-03-31").get(0));
		list.add(dao.getMonthlyTotalSaleByString("2021-04-01", "2021-04-30").get(0));
		list.add(dao.getMonthlyTotalSaleByString("2021-05-01", "2021-05-31").get(0));
		list.add(dao.getMonthlyTotalSaleByString("2021-06-01", "2021-06-30").get(0));
		list.add(dao.getMonthlyTotalSaleByString("2021-07-01", "2021-07-31").get(0));
		list.add(dao.getMonthlyTotalSaleByString("2021-08-01", "2021-08-31").get(0));
		list.add(dao.getMonthlyTotalSaleByString("2021-09-01", "2021-09-30").get(0));
		list.add(dao.getMonthlyTotalSaleByString("2021-10-01", "2021-10-31").get(0));
		list.add(dao.getMonthlyTotalSaleByString("2021-11-01", "2021-11-30").get(0));
		list.add(dao.getMonthlyTotalSaleByString("2021-12-01", "2021-12-31").get(0));

		model.addAttribute("abc", list);
		System.out.println(list.toString());

		return "pharmacy_Department/monthMedicineSaleGraph";
	}
}
