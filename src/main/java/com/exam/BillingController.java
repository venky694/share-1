package com.exam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.exam.doctor.DoctorDAO;
import com.exam.receptions.Patient;
import com.exam.receptions.PatientDAO;

import come.exam.admission.AdmissionDAO;

@Controller
public class BillingController {
	@Autowired
	PatientDAO patient;
	@Autowired
	DoctorDAO doctordao;

	AdmissionDAO adm = new AdmissionDAO();
	int patientId = 0;

	List<Patient> list = new ArrayList<>();

	@RequestMapping("searchAdmittedPatient2")
	public String findAdmittedPatient3(@RequestParam int patientId, Model model) {
		// get today date and send to view
		

		list.removeAll(list);
		list.addAll(patient.getAdmittedPatient(patientId));
		this.patientId = patientId;

		if (list.size() == 0) {
			model.addAttribute("msg", "No data found, Please enter corect patient id");
			return "billing_module/findAdmittedPatient";
		}
		// calculate total days of admission
		Date today_date = new Date();
		Date admission_date = doctordao.getAdmittedPatientInfoById(patientId).get(0).getAdmission_date();
		long totalDays = ((today_date.getTime() - admission_date.getTime()) / (1000 * 60 * 60 * 24)) + 1;

		// calculate total bed charge
		int bed_rate = adm.getBedRate(doctordao.getAdmittedPatientInfoById(patientId).get(0).getBed_no());
		int total_bed_charge = bed_rate * (int) totalDays;
		int visitingFee = (int) totalDays * 800;
		int nursFee = (int) totalDays * 300;

		// calculate patient deposit balance
		int totalDeposit = adm.getAdmittdPatientTotalDeposite(patientId);
		double totalCost = adm.getTotalCost(patientId) + total_bed_charge + visitingFee + nursFee;
		Double Balance = totalDeposit - totalCost;

		model.addAttribute("patient", list);
		model.addAttribute("admissionInfo", doctordao.getAdmittedPatientInfoById(patientId));
		model.addAttribute("deposit", adm.getAdmittdPatientDeposite(patientId));
		model.addAttribute("totalDeposit", totalDeposit);
		model.addAttribute("medicineCost", adm.getMedicienCost(patientId));
		model.addAttribute("leanth", adm.getMedicienCost(patientId).size() + 3);
		model.addAttribute("totalCost", totalCost);
		model.addAttribute("diagnosticCost", adm.getDiagnosticCost(patientId));
		model.addAttribute("balance", Balance);
		model.addAttribute("totalBedCharge", total_bed_charge);
		model.addAttribute("bed_rate", bed_rate);

		model.addAttribute("totalDays", totalDays);
		model.addAttribute("visitigFee", visitingFee);
		model.addAttribute("nursFee", nursFee);

		return "billing_module/patientProfile";
	}

	@RequestMapping("/dischargeAndCreateBill")
	public String dischargeAndCreateBill(Model model) {
		// get today date and send to view
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		String date = (dtf.format(localDate));
		

		// calculate total days of admission
		Date today_date = new Date();
		Date admission_date = doctordao.getAdmittedPatientInfoById(patientId).get(0).getAdmission_date();
		long totalDays = (today_date.getTime() - admission_date.getTime()) / (1000 * 60 * 60 * 24) +1 ;

		// calculate total bed charge
		int bed_rate = adm.getBedRate(doctordao.getAdmittedPatientInfoById(patientId).get(0).getBed_no());
		int total_bed_charge = bed_rate * (int) totalDays;
		int visitingFee = (int) totalDays * 800;
		int nursFee = (int) totalDays * 300;

		// calculate patient deposit balance
		int totalDeposit = adm.getAdmittdPatientTotalDeposite(patientId);
		double totalCost = adm.getTotalCost(patientId) + total_bed_charge + visitingFee + nursFee;
		Double Balance = totalDeposit - totalCost;

		if (Balance < 0) {
			findAdmittedPatient3(patientId, model);
			model.addAttribute("msg", "Patient has some due , please Deposite to patient account and try to create discharge Bill");
			return "billing_module/patientProfile";
		}
		model.addAttribute("patient", list);
		model.addAttribute("admissionInfo", doctordao.getAdmittedPatientInfoById(patientId));
		model.addAttribute("deposit", adm.getAdmittdPatientDeposite(patientId));
		model.addAttribute("totalDeposit", totalDeposit);
		model.addAttribute("medicineCost", adm.getMedicienCost(patientId));
		model.addAttribute("leanth", adm.getMedicienCost(patientId).size() + 3);
		model.addAttribute("totalCost", totalCost);
		model.addAttribute("diagnosticCost", adm.getDiagnosticCost(patientId));
		model.addAttribute("balance", Balance);
		model.addAttribute("totalBedCharge", total_bed_charge);
		model.addAttribute("bed_rate", bed_rate);
		model.addAttribute("today_date", date);

		model.addAttribute("totalDays", totalDays);
		model.addAttribute("visitigFee", visitingFee);
		model.addAttribute("nursFee", nursFee);
		model.addAttribute("patient", list);

		if(Balance<0) {
			model.addAttribute("bcondition", "true");
		}
		
		
		adm.updateBedStatusAfterDischarge(doctordao.getAdmittedPatientInfoById(patientId).get(0).getBed_no());
		adm.updatePatientStatusAfterDischarge(patientId);
		adm.updateAdmissionTableAfterDischarge(patientId);
		return "billing_module/dischargeAndBill";
	}

	@RequestMapping("cashDeposit")
	public String cashDeposit(@RequestParam int amount, Model model) {

		adm.cashDeposit(patientId, amount);

		findAdmittedPatient3(patientId, model);
		return "billing_module/patientProfile";
	}

	@RequestMapping("billingHome")
	public String billingHeader() {
		return "billing_module/billingHome";
	}

	@RequestMapping("/findAdmittedPatients")
	public String findAdmittedPatients() {
		return "billing_module/findAdmittedPatient";
	}

	@RequestMapping("searchAdmittedPatientForCashDeposit")
	public String searchAdmittedPatientForCashDeposit(Model model) {
		model.addAttribute("hidden", false);
		return "billing_module/cashReceive";
	}

	@RequestMapping("billingHeader")
	public String adminHeader(Model model) {
		return "headerAndFooter/billingHeader";
	}
}
