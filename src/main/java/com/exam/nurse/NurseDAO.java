package com.exam.nurse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import com.exam.receptions.Patient;


@Component
public class NurseDAO {

	Connection con;
	PreparedStatement pst;

	public NurseDAO() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_database", "root", "idb122292");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<Patient> getAdmittedPatient(String floor) {
		List<Patient> list = new ArrayList<>();
		try {
			pst = con.prepareStatement(
					"SELECT patient_id, name,age, gender, mobile, doctor, problem, admission_date, bed_no, floor FROM patient inner join admission on patient.id=admission.patient_id where admission.status='Admitted' and floor='"+floor+"'");
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

}
