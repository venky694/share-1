package com.exam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.exam.receptions.Patient;
import come.exam.admission.AdmissionDAO;
import come.exam.admission.Room;

@Controller
public class AdmissionController {
	String bed_no = "";
	int patient_id = 0;

	AdmissionDAO dao = new AdmissionDAO();

	@RequestMapping("admissionHome")
	public String admissionHome(Model model) {
		
		//get bed type -A
		model.addAttribute("typeA1", dao.getBedByStatusAndType("Available","1 person"));
		model.addAttribute("typeA2", dao.getBedByStatusAndType("Occupied","1 person"));
		model.addAttribute("typeA3", dao.getBedByStatusAndType("Under Maintenance","1 person"));
		model.addAttribute("typeATotal", dao.getTotalbyType("1 person"));
		
		//get bed type -B
		model.addAttribute("typeB1", dao.getBedByStatusAndType("Available","2 person"));
		model.addAttribute("typeB2", dao.getBedByStatusAndType("Occupied","2 person"));
		model.addAttribute("typeB3", dao.getBedByStatusAndType("Under Maintenance","2 person"));
		model.addAttribute("typeBTotal", dao.getTotalbyType("2 person"));
		
		//get bed type -C
		model.addAttribute("typeC1", dao.getBedByStatusAndType("Available","3 person"));
		model.addAttribute("typeC2", dao.getBedByStatusAndType("Occupied","3 person"));
		model.addAttribute("typeC3", dao.getBedByStatusAndType("Under Maintenance","3 person"));
		model.addAttribute("typeCTotal", dao.getTotalbyType("3 person"));
		
		//get bed type -A
		model.addAttribute("typeD1", dao.getBedByStatusAndType("Available","4 person"));
		model.addAttribute("typeD2", dao.getBedByStatusAndType("Occupied","4 person"));
		model.addAttribute("typeD3", dao.getBedByStatusAndType("Under Maintenance","4 person"));
		model.addAttribute("typeDTotal", dao.getTotalbyType("4 person"));
		
	   //status total
		model.addAttribute("Available", dao.getTotalByStatus("Available"));
		model.addAttribute("Occupied", dao.getTotalByStatus("Occupied"));
		model.addAttribute("underMaintenance", dao.getTotalByStatus("Under Maintenance"));
		
		
		model.addAttribute("grandTotal", dao.getTotalBed());
		return "admission_module/admissionHome";
	}

	@RequestMapping("admissionHeader")
	public String admissionHeader() {
		return "headerAndFooter/admissionHeader";
	}

	List<Room> list1 = new ArrayList<>();

	@RequestMapping(value = "checkRoom")
	public String checkRoom(@ModelAttribute() Room rome, Model model) {

		list1.removeAll(list1);
		list1.addAll(dao.show());
		model.addAttribute("room", list1);

		return "admission_module/showAllRoom";
	}

	@RequestMapping(value = "searchRoom", method = RequestMethod.POST)
	public String searchRoom(@ModelAttribute() Room room, Model model) {
		List<Room> list = new ArrayList<>();
		list.removeAll(list);
		list.addAll(dao.roomGetByFloor(room.getFloor()));
		model.addAttribute("room", list);
		model.addAttribute("color", "green");

		return "admission_module/showAllRoom";
	}

	List<Room> list2 = new ArrayList<>();

	@RequestMapping("booking")
	public String booking(@RequestParam String bed_no, Model model) {
		this.bed_no = bed_no;
		list2.removeAll(list2);
		list2.addAll(dao.getBedInfo(bed_no));
		if (list2.size() == 0) {
			model.addAttribute("msg", "This bed no availalble");
			model.addAttribute("room", list1);
			return "admission_module/showAllRoom";
		}
		return "admission_module/findPatient";
	}

	List<Patient> list = new ArrayList<>();

	@RequestMapping("searchPatients")
	public String findPatientInformations(@RequestParam int id, Model model) {
		this.patient_id = id;

		list.removeAll(list);
		list.addAll(dao.getPatient(id));

		if (list.size() == 0) {
			model.addAttribute("msg", "Patient id is not correct or this patient not recomented for admission");
			return "admission_module/findPatient";
		} else {

			// send patient info to the view
			model.addAttribute("patient", list);

			// get bed info and send to the view
			model.addAttribute("bedinfo", list2);

		}

		return "admission_module/findPatient";
	}

	@RequestMapping("admissionBill")
	public String admissionBill(@RequestParam int primary_deposit, Model model) {

		dao.savPatientAdmission(patient_id, primary_deposit, list2.get(0).getBed_no(), list2.get(0).getFloor());

		System.out.println(primary_deposit);
		model.addAttribute("primaryDeposit", primary_deposit);

		// send patient info to the view
		model.addAttribute("patient", list);

		// get bed info and send to the view
		model.addAttribute("bedinfo", list2);

		// Get Today Date and send to view
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate localDate = LocalDate.now();
		String date = (dtf.format(localDate));
		model.addAttribute("todayDate", date);
		return "admission_module/admissionBill";
	}
}
