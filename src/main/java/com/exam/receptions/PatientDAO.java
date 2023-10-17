package com.exam.receptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class PatientDAO {
	Connection con;
	PreparedStatement pst;

	public PatientDAO() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_database", "root", "idb122292");
		} catch (Exception e) {
			
		}
	}

	public String insert(Patient object) {
		String msg="";
		try {
			
			String str = object.name;
			String status = "appPending";

			// Split sentence into words
			String words[] = str.split(" ");
			String name = "";

			for (String w : words) {
				String first = w.substring(0, 1); // First Letter
				String rest = w.substring(1); // Rest of the letters

				// Concatenete and reassign after
				// converting the first letter to uppercase
				name += first.toUpperCase() + rest + " ";
			}
         String patinetName = name.substring(0,name.length()-2);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			LocalDate localDate = LocalDate.now();
			String date = (dtf.format(localDate));

			pst = con.prepareStatement(
					"insert into Patient (id, name, mobile, gender,age, problem, app_date, doctor, app_create_date, status) values(?,?,?,?,?,?,?,?,?,?)");
			pst.setInt(1, object.id);
			pst.setString(2, patinetName);
			pst.setString(3, object.mobile);
			pst.setString(4, object.gender);
			pst.setString(5, object.age);
			pst.setString(6, object.problem);
			pst.setDate(7, object.app_date);
			pst.setString(8, object.doctor);
			pst.setString(9, date);
			pst.setString(10, status);

			pst.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e);
			msg=e.toString();
		}
     return msg;
	}
	public void delete(int id) {
		try {
			pst = con.prepareStatement("delete from Patient where id=" + id);
			pst.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public List<Patient> show() {
		List<Patient> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from patient");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Patient patient = new Patient(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getDate(9), rs.getDate(10),
						rs.getString(11), rs.getString(12), rs.getString(13));
				list.add(patient);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Patient> getPatient(int id) {
		List<Patient> list = new ArrayList<>();
		try {		
			pst = con.prepareStatement("select * from Patient where id=" + id );
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Patient patient = new Patient(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getDate(9), rs.getDate(10),
						rs.getString(11), rs.getString(12), rs.getString(13));
				list.add(patient);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}
	
	public List<Patient> getAdmittedPatient(int id) {
		List<Patient> list = new ArrayList<>();
		try {		
			pst = con.prepareStatement("select * from Patient where id=" + id + " and status='Admitted'");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Patient patient = new Patient(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getDate(9), rs.getDate(10),
						rs.getString(11), rs.getString(12), rs.getString(13));
				list.add(patient);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}
	
	public List<Patient> getAllAdmittedPatient() {
		List<Patient> list = new ArrayList<>();
		try {
			pst = con.prepareStatement(
					"SELECT patient_id, name,age, gender, mobile, doctor, problem, admission_date, bed_no, floor FROM patient inner join admission on patient.id=admission.patient_id where admission.status='Admitted'");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Patient pt = new Patient(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getDate(8), rs.getString(9),
						rs.getString(10));
				list.add(pt);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}
	public List<Patient> searchAdmittedPatient(String abc) {
		List<Patient> list = new ArrayList<>();
		try {
			pst = con.prepareStatement(
					"SELECT patient_id, name,age, gender, mobile, doctor, problem, admission_date, bed_no, floor FROM patient inner join admission on patient.id=admission.patient_id where admission.status='Admitted' and   (patient.name like '%"+abc+"%' or patient.id='"+abc+"')");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Patient pt = new Patient(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getDate(8), rs.getString(9),
						rs.getString(10));
				list.add(pt);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}
	
	public void cencelAppointment( int id) {
		try {
			pst = con.prepareStatement(" delete from patient where id="+id+"");
			pst.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	
	public List<Patient> getAllPendingAppointment() {
		List<Patient> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from patient where  status='appPending' order by app_date");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Patient patient = new Patient(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getDate(9), rs.getDate(10),
						rs.getString(11), rs.getString(12), rs.getString(13));
				list.add(patient);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	
	public List<Patient> searchPendingAppointment(String query) {
		
		List<Patient> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from patient where name like '%" + query + "%' or id='" + query + "' and status='appPending'");

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Patient patient = new Patient(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getDate(9), rs.getDate(10),
						rs.getString(11), rs.getString(12), rs.getString(13));
				list.add(patient);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;

	}
	public Integer getID() {
		int id = 0;
		try {
			pst = con.prepareStatement("select Max(id) from Patient");

			ResultSet rs = pst.executeQuery();
			rs.next();
			id = rs.getInt(1);
		} catch (Exception e) {
			System.out.println(e);
		}
		return id;
	}

	public List<String> getDoctor() {
		List<String> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select name from employee where designation ='doctor'");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add(rs.getString(1));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public String getDoctorFee(String doctroName) {
		String doctorFee = "";
		try {
			pst = con.prepareStatement("select salary from employee where name ='" + doctroName + "'");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				doctorFee = rs.getString(1);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return doctorFee;
	}

	public void updateEmailAndAddress(Patient object) {
		String status = "Confirm Appointment";
		String email = object.email;
		if (object.email.isEmpty()) {
			email = "";
		}
		try {
			pst = con.prepareStatement("update patient set address='" + object.address + "', email='" + email
					+ "', payment='" + object.payment + "', status='" + status + "' where id=" + object.getId());
			pst.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// code for doctor module ...........................................

	public List<Patient> showPatient(String doctorName) {
		List<Patient> list = new ArrayList<>();
		String status = "Confirm Appointment";
		try {
			pst = con.prepareStatement(
					"select * from patient where doctor='" + doctorName + "' and status='" + status + "'");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Patient patient = new Patient(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getDate(9), rs.getDate(10),
						rs.getString(11), rs.getString(12), rs.getString(13));
				list.add(patient);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Patient> showFutureAppointment(String doctorName) {
		List<Patient> list = new ArrayList<>();
		String status = "AppPending";
		try {
			pst = con.prepareStatement("select * from patient where doctor='" + doctorName + "' and status='" + status
					+ "' order by app_date");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Patient patient = new Patient(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getDate(9), rs.getDate(10),
						rs.getString(11), rs.getString(12), rs.getString(13));
				list.add(patient);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}
	
	public List<Patient> searchDischargedPaitentForNewAppointment(String searchItem) {
		List<Patient> list = new ArrayList<>();
		String status = "Discharge";
		try {
			pst = con.prepareStatement("select * from patient where id='" + searchItem + "' or mobile='" + searchItem + "' and status='" + status
					+ "'");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Patient patient = new Patient(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), rs.getDate(9), rs.getDate(10),
						rs.getString(11), rs.getString(12), rs.getString(13));
				list.add(patient);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<String> getAllDiseaseName() {
		List<String> list= new ArrayList<>();
		try {
			pst = con.prepareStatement("select name from disease_name");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add(rs.getString(1));
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

}
