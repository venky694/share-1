package com.exam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.exam.admin.Employee;
import com.exam.admin.EmployeeDAO;
import com.exam.doctor.DoctorDAO;
import com.exam.doctor.Given_test;
import com.exam.doctor.Prescription;
import com.exam.doctor.test_name;
import com.exam.medicine.medicineDAO;
import com.exam.receptions.Patient;
import com.exam.receptions.PatientDAO;
import come.exam.admission.Admission;



@Controller
public class doctorController {
	String doctorNname = "";
	String email = "";
	EmployeeDAO employe = new EmployeeDAO();
	PatientDAO patient = new PatientDAO();
	DoctorDAO doctors = new DoctorDAO();
	medicineDAO mecicine = new medicineDAO();

	// navigate future appointment page.........................
	@RequestMapping("futureAppointment")
	public String futureAppointment(Model model) {

		doctorNname = employe.getDoctorName(email);

		model.addAttribute("doctorNname", doctorNname);

		model.addAttribute("object", patient.showFutureAppointment(doctorNname));
		return "doctor_module/futureAppointment";
	}

	List<Patient> admittedPatient= new ArrayList<>();
	List<Admission>  admissioninfo= new ArrayList<>();
	// navigate future appointment page.........................
	@RequestMapping("admittedPatient")
	public String AdmittedPatient(Model model) {
		doctorNname = employe.getDoctorName(email);
		model.addAttribute("object", doctors.getAdmittedPatient(doctorNname));
		
		return "doctor_module/admitedPatient";
	}

	/// code for login...................................................
	@RequestMapping("doctorLogin")
	public String doctorHom(Model model) {
		return "doctor_module/doctorLogin";
	}

	@RequestMapping("doctorHeader")
	public String doctorHeader(Model model) {
		model.addAttribute("email", email);
		return "headerAndFooter/doctorHeader";
	}

	@RequestMapping(value = "Login", method = RequestMethod.POST)
	public String finstudent(Model md, ModelMap model, @ModelAttribute("Employee") Employee employee) {
		String log = "";
		try {
			String st = employe.login(employee.getEmail(), employee.getPassword());
			if ("Success".equals(st)) {
				email = employee.getEmail();
				model.addAttribute("doctorName", employe.getDoctorName(email));
				log = "doctor_module/doctorHome";
			} else {
				md.addAttribute("alert", "Id and password is wrong");
				log = "doctor_module/doctorLogin";
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return log;
	}
	/// End code for login...................................................

	// got to my patient page
	@RequestMapping("myPatient")
	public String myPatient(Model model) {

		doctorNname = employe.getDoctorName(email);
		model.addAttribute("doctorNname", doctorNname);
		model.addAttribute("object", patient.showPatient(doctorNname));
		return "doctor_module/myPatient";
	}

	// All method for given test
	// .......................................................................
	List<Patient> patientDetails = new ArrayList<>();

	@RequestMapping(value = "giveTest", method = RequestMethod.POST)
	public String givTest(Model model, @ModelAttribute("Employee") Patient pt) {

		// remove list data if there have any previous patient information
		patientDetails.removeAll(patientDetails);
		// add patient information to the list
		patientDetails.add(pt);

		xyz(model);
		return "doctor_module/test";
	}

	public void xyz(Model model) {
		model.addAttribute("patient", patientDetails.get(0));

		// show all given test name
		model.addAttribute("givenTest", doctors.showAllGivenTest(patientDetails.get(0).getId()));

		// get all test name from data base and show to the dropdaown
		List<String> testName = new ArrayList<>();
		testName = doctors.getTestName();
		model.addAttribute("name", testName);
	}

	@RequestMapping(value = "addTest", method = RequestMethod.POST)
	public String addTest(Model model, @ModelAttribute("test_name") test_name names, Given_test object) {

		// insert given test details
		if (names.getTest_name().isEmpty()) {
			model.addAttribute("msg", "Please select test name");
		} else {
			doctors.insertTestSelectetTest(object);
		}
		xyz(model);
		return "doctor_module/test";
	}

	@RequestMapping(value = "removeSelectedTestName", method = RequestMethod.POST)
	public String removeSelectedTestName(Model model, @ModelAttribute("test_name") test_name names, Given_test object) {

		// remove selected test name
		doctors.removeSelectedTestName(object);
		xyz(model);
		return "doctor_module/test";
	}

	@RequestMapping("pathologyTestForm")
	public String pathologyTestForm(Model model) {
		model.addAttribute("patientDetails", patientDetails.get(0));

		// show all given test name
		model.addAttribute("givenTest", doctors.showAllGivenTest(patientDetails.get(0).getId()));

		// Get Today Date and send to view
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate localDate = LocalDate.now();
		String date = (dtf.format(localDate));
		model.addAttribute("todayDate", date);
		return "doctor_module/pathologyTest";
	}

	/////// Method for prescribing
	/////// medicine.................................................

	List<String> AllMedicineName = new ArrayList<>();

	public void abc(Model model) {
		model.addAttribute("patient", patientDetails.get(0));

		// get all medicine name and send to view
		AllMedicineName = doctors.getAllMedicineName();
		model.addAttribute("name", AllMedicineName);

		// get prescribing medicine name from database and send to view
		model.addAttribute("givenTest", doctors.getPrescribingMedicine(patientDetails.get(0).getId()));

	}

	@RequestMapping(value = "prescription", method = RequestMethod.POST)
	public String prscription(Model model, @ModelAttribute("Employee") Patient patient) {

		// pass all patient details to the view
		patientDetails.removeAll(patientDetails);
		patientDetails.add(patient);

		abc(model);

		return "doctor_module/prescription";
	}

	@RequestMapping(value = "/addMedicineToPrescription", method = RequestMethod.POST)
	public String addMedicineToPrescription(Model model, @ModelAttribute("test_name") Prescription object, Patient pt) {
		// save prescribing medicine to data base
		doctors.saveMedicineToPrescription(object);

		abc(model);
		return "doctor_module/prescription";

	}

	@RequestMapping(value = "/removeSelectedMedicine", method = RequestMethod.POST)
	public String removeSelectedMedicine(Model model, @ModelAttribute("test_name") test_name names, Prescription object,
			Patient pt) {

		// remove selected test name
		doctors.removeSelectedMedicine(object);

		abc(model);

		return "doctor_module/prescription";

	}

	@RequestMapping(value = "/generatePrescription", method = RequestMethod.POST)
	public String generatePrescription(Model model, @ModelAttribute("test_name") test_name names, Prescription object,
			Patient pt) {
		System.out.println(pt.getStatus());
		System.out.println(pt.getId());
		// update patient status admission yes or no
		doctors.updatePatientStatus(pt);

		model.addAttribute("patientDetails", patientDetails.get(0));

		// get prescribing medicine name from database and send to view
		model.addAttribute("givenTest", doctors.getPrescribingMedicine(patientDetails.get(0).getId()));
		// Get Today Date and send to view
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate localDate = LocalDate.now();
		String date = (dtf.format(localDate));
		model.addAttribute("todayDate", date);
		return "doctor_module/generatePrescription";

	}
}
