package com.exam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.exam.doctor.DoctorDAO;
import com.exam.medicine.Medicine;
import com.exam.numberConverter.NumberToWardConverter;
import com.exam.receptions.Patient;
import com.exam.receptions.PatientDAO;

import come.exam.admission.Admission;

@Controller
public class receptionController {
	PatientDAO dao = new PatientDAO();
	DoctorDAO doctors = new DoctorDAO();

	EmployeeDAO emdao = new EmployeeDAO();

	@RequestMapping("receptionHome")
	public String receptionHome() {
		return "receptions_module/receptionHome";
	}

	int patientId = 0;

	@RequestMapping(value = "/getPatientForBill", method = RequestMethod.POST)
	public String getPatientForBill(Model model, @RequestParam int id) {
		// store patient id
		patientId = id;
		model.addAttribute("object", dao.getPatient(id));

		if (dao.getPatient(id).size() == 0) {

			model.addAttribute("msg", "Please enter correct pending appointment patient id");
			return "receptions_module/findPatientForConfirmAppointment";
		}
		String doctorName = dao.getPatient(id).get(0).getDoctor();
		model.addAttribute("doctorFee", dao.getDoctorFee(doctorName));
		return "receptions_module/BillForAppointment";
	}

	@RequestMapping(value = "/CencelAppointment", method = RequestMethod.POST)
	public String CencelAppointment(Model model, @RequestParam int id) {

		dao.cencelAppointment(id);
		List<Patient> pendingAppointment = new ArrayList<>();
		pendingAppointment.removeAll(pendingAppointment);
		pendingAppointment.addAll(dao.getAllPendingAppointment());

		model.addAttribute("object", pendingAppointment);

		return "receptions_module/pendingAppointment";
	}

	@RequestMapping(value = "SaveAndBill", method = RequestMethod.POST)
	public String SaveAndBill(Patient object, Model model) {
		// update patient email and address
		dao.updateEmailAndAddress(object);

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate localDate = LocalDate.now();
		String date = "Date : " + (dtf.format(localDate));
		model.addAttribute("todayDate", date);

		model.addAttribute("object", dao.getPatient(object.getId()));
		String doctorName = dao.getPatient(object.getId()).get(0).getDoctor();

		// get doctor fee
		model.addAttribute("doctorFee", dao.getDoctorFee(doctorName));

		// convert doctor fee to word
		int fee = Integer.parseInt(dao.getDoctorFee(doctorName));
		NumberToWardConverter convert = new NumberToWardConverter();
		String total = "Total in word : " + convert.numberToWord(fee) + " taka only.";
		model.addAttribute("TotalInWord", total);

		return "receptions_module/Bill";
	}

	@RequestMapping("findPatientForConfirmAppointment")
	public String confirmAppointment() {
		return "receptions_module/findPatientForConfirmAppointment";
	}

	public String SaveEmployeeRecord(String name) {
		String res = name;
		// do here some operation
		return res;
	}

	@RequestMapping("receptionHeader")
	public String receptionHeader() {
		return "headerAndFooter/receptionsHeader";
	}

	public void abc(Model model) {

		// collect all Doctor from data base and set to model
		List<String> list;
		list = dao.getDoctor();
		model.addAttribute("doctor", list);

		// collect max id from data base and set to model
		int id = dao.getID() + 1;
		model.addAttribute("id", id);

		// collect all Disease name from data base and set to model
		List<String> list1;
		list1 = dao.getDoctor();
		model.addAttribute("doctor", list1);
		model.addAttribute("diseaseName", dao.getAllDiseaseName());
	}

	@RequestMapping("addAppointment")
	public String addAppointment(Model model) {
		abc(model);
		return "receptions_module/addAppointment";
	}

	@RequestMapping(value = "saveAppointment", method = RequestMethod.POST)
	public String saveAppointment(@ModelAttribute() Patient object, Model model) {
		dao.insert(object);
		model.addAttribute("msg", "Patient  add successfulley");
		abc(model);
		return "receptions_module/addAppointment";
	}

	@RequestMapping(value = "receptionLogin", method = RequestMethod.POST)
	public String finstudent(Model md, ModelMap model, @ModelAttribute("Employee") Employee employee) {
		String log = "";
		try {
			// String st = dao.login(employee.getEmail(), employee.getPassword());
			if ("Success".equals("s")) {
				// model.put("email", email);
				log = "admin_module/adminHome";
			} else {
				md.addAttribute("alert", "Id is wrong");
				log = "admin_module/Login";
			}
		} catch (Exception e) {

			System.out.println(e);
		}
		return log;
	}

	@RequestMapping("findDischargedPatientForm")
	public String findDischargedPatientForm() {
		return "receptions_module/findOldPatientForAppointment";
	}

	@RequestMapping("searchDischargedPatient")
	public String searchDischargedPatient(@RequestParam String id, Model model) {
		// get old patient info and send to appointment form
		model.addAttribute("patientInfo", dao.searchDischargedPaitentForNewAppointment(id));
		abc(model);
		return "receptions_module/oldPatientAppointmentForm";
	}
	
	@RequestMapping(value = "saveAppointment2", method = RequestMethod.POST)
	public String saveAppointment2(@ModelAttribute() Patient object, Model model) {
		dao.insert(object);
		model.addAttribute("msg", "Patient  add successfulley");
		abc(model);
		return "receptions_module/findOldPatientForAppointment";
	}

	List<Patient> admittedPatient = new ArrayList<>();
	List<Admission> admissioninfo = new ArrayList<>();

	@RequestMapping("addmittedPatient1")
	public String allAdmitedPatient1(@ModelAttribute() Medicine object, Model model) {
		model.addAttribute("object", dao.getAllAdmittedPatient());

		return "receptions_module/AdmitedPatient";
	}

	@RequestMapping(value = "searchAdmittedPatientById")
	public String searchAdmittedPatientById(@RequestParam String id, Model model) {
		model.addAttribute("object", dao.searchAdmittedPatient(id));

		return "receptions_module/AdmitedPatient";
	}

	@RequestMapping("pendingAppointment")
	public String pendingAppointment(Model model) {
		List<Patient> pendingAppointment = new ArrayList<>();
		pendingAppointment.removeAll(pendingAppointment);
		pendingAppointment.addAll(dao.getAllPendingAppointment());

		model.addAttribute("object", pendingAppointment);

		return "receptions_module/pendingAppointment";
	}

	@RequestMapping("searchPendingAppointment")
	public String searchPendingAppointment(Model model, @ModelAttribute Patient object) {

		List<Patient> pendingAppointment = new ArrayList<>();
		pendingAppointment.removeAll(pendingAppointment);
		pendingAppointment.addAll(dao.searchPendingAppointment(object.getName()));

		model.addAttribute("object", pendingAppointment);

		return "receptions_module/pendingAppointment";
	}

}
