package com.exam.doctor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import com.exam.receptions.Patient;
import come.exam.admission.Admission;

@Component
public class DoctorDAO {

	Connection con;
	PreparedStatement pst;

	public DoctorDAO() {
		super();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_database", "root", "idb122292");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<String> getTestName() {
		List<String> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select test_name from test_name");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add(rs.getString(1));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Patient> getAdmittedPatient(String doctorName) {
		List<Patient> list = new ArrayList<>();
		try {
			pst = con.prepareStatement(
					"SELECT patient_id, name,age, gender, mobile, doctor, problem, admission_date, bed_no, floor FROM patient inner join admission on patient.id=admission.patient_id where admission.status='Admitted' and patient.doctor='"+doctorName+"'");
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
	public List<Patient> getAdmittedPatientInfoById(int id) {
		List<Patient> list = new ArrayList<>();
		try {
			pst = con.prepareStatement(
					"SELECT patient_id, name,age, gender, mobile, doctor, problem, admission_date, bed_no, floor FROM patient inner join admission on patient.id=admission.patient_id where admission.status='Admitted' and patient.id='"+id+"'");
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

	public void insertTestSelectetTest(Given_test object) {
		String sst = "Pending";
		try {
			pst = con.prepareStatement(" insert given_test (test_name, patient_id, status) VALUES ('"
					+ object.getTest_name() + "','" + object.getPatient_id() + "','" + sst + "')");
			pst.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public List<Given_test> showAllGivenTest(int patientid) {
		List<Given_test> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from given_test where patient_id=" + patientid);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Given_test Given_test = new Given_test(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4));
				list.add(Given_test);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public void removeSelectedTestName(Given_test object) {
		try {
			pst = con.prepareStatement("delete  from given_test where id=" + object.getId() + " and patient_id="
					+ object.getPatient_id() + "");
			pst.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public List<String> getAllMedicineName() {
		List<String> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select name from medicine");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add(rs.getString(1));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public void saveMedicineToPrescription(Prescription object) {
		try {
			pst = con.prepareStatement(
					" insert prescription (medicine_name, course_duration, taken_time, patient_id)  VALUES (?,?,?,?)");

			pst.setString(1, object.getMedicine_name());
			pst.setString(2, object.getCourse_duration());
			pst.setString(3, object.getTaken_time());
			pst.setInt(4, object.getPatient_id());
			pst.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public List<Prescription> getPrescribingMedicine(int patientid) {
		List<Prescription> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from prescription where patient_id=" + patientid);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Prescription Prescription = new Prescription(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getInt(5));
				list.add(Prescription);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public void removeSelectedMedicine(Prescription object) {
		try {
			pst = con.prepareStatement("delete  from prescription where id=" + object.getId() + " and patient_id="
					+ object.getPatient_id() + "");
			pst.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void updatePatientStatus(Patient pt) {
		String status = "Discharge";
		if ("Yes".equals(pt.getStatus())) {
			status = "Admission is required";
		}
		try {
			pst = con.prepareStatement("update patient set status='" + status + "' where id=" + pt.getId() + "");
			pst.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
