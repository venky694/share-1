package come.exam.admission;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.exam.diagnostic.Test_invoice_details;
import com.exam.medicine.MedicineSaleDetails;
import com.exam.receptions.Patient;

@Component
public class AdmissionDAO {
	Connection con;
	PreparedStatement pst;

	public AdmissionDAO() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_database", "root", "idb122292");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<Room> show() {
		List<Room> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from room");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Room room = new Room(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5),
						rs.getString(6), rs.getString(7));
				list.add(room);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Room> roomGetByFloor(String object) {
		List<Room> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from room where floor='" + object + "'");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Room room = new Room(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5),
						rs.getString(6), rs.getString(7));
				list.add(room);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Patient> getPatient(int id) {
		List<Patient> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from Patient where id=" + id + " and status='Admission is required'");
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

	public List<Room> getBedInfo(String id) {
		List<Room> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from room where bed_no='" + id + "' and status='available'");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Room room = new Room(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5),
						rs.getString(6), rs.getString(7));
				list.add(room);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public Integer getTotalBed() {
		int number = 0;
		try {
			pst = con.prepareStatement("select COUNT(*) from room ");
			ResultSet rs = pst.executeQuery();
			rs.next();
			number = rs.getInt(1);

		} catch (Exception e) {
			System.out.println(e);
		}
		return number;
	}

	public Integer getTotalByStatus(String status) {
		int number = 0;
		try {
			pst = con.prepareStatement("select COUNT(*) from room where status='" + status + "'");
			ResultSet rs = pst.executeQuery();
			rs.next();
			number = rs.getInt(1);

		} catch (Exception e) {
			System.out.println(e);
		}
		return number;
	}

	public Integer getBedByStatusAndType(String status, String type) {
		int number = 0;
		try {
			pst = con.prepareStatement(
					"select COUNT(*) from room where status='" + status + "' and type='" + type + "'");
			ResultSet rs = pst.executeQuery();
			rs.next();
			number = rs.getInt(1);

		} catch (Exception e) {
			System.out.println(e);
		}
		return number;
	}

	public Integer getTotalbyType(String type) {
		int number = 0;
		try {
			pst = con.prepareStatement("select COUNT(*) from room where type='" + type + "'");
			ResultSet rs = pst.executeQuery();
			rs.next();
			number = rs.getInt(1);

		} catch (Exception e) {
			System.out.println(e);
		}
		return number;
	}

	public void savPatientAdmission(int patient_id, int deposit, String bed_no, String floor) {

		String description = "Admission and primary deposit recived";
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			LocalDate localDate = LocalDate.now();
			String date = (dtf.format(localDate));

			pst = con.prepareStatement(
					"insert into admission (patient_id,  description, admission_date, transaction_date,deposit, bed_no, floor, status) values(?,?,?,?,?,?,?,?)");
			pst.setInt(1, patient_id);
			pst.setString(2, description);
			pst.setString(3, date);
			pst.setString(4, date);
			pst.setInt(5, deposit);
			pst.setString(6, bed_no);
			pst.setString(7, floor);
			pst.setString(8, "Admitted");
			pst.executeUpdate();

			// update patient status
			pst = con.prepareStatement("update  patient set status='Admitted' where id=" + patient_id);
			pst.executeUpdate();

			// update bed status
			pst = con.prepareStatement("update  room set status='Occupied' where bed_no='" + bed_no + "'");
			pst.executeUpdate();

			System.out.println("update");
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	// billing controller method
	// ......................................................
	public List<Admission> getAdmittdPatientDeposite(int patient_id) {
		List<Admission> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select *  from admission where patient_id= '" + patient_id + "'");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Admission admission = new Admission(rs.getString(3), rs.getDate(5), rs.getDouble(6));
				list.add(admission);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public Integer getAdmittdPatientTotalDeposite(int patient_id) {
		int total = 0;
		try {
			pst = con.prepareStatement("select sum(deposit)  from admission where patient_id= '" + patient_id + "'");
			ResultSet rs = pst.executeQuery();
			rs.next();
			total = rs.getInt(1);

		} catch (Exception e) {
			System.out.println(e);
		}
		return total;
	}

	public List<MedicineSaleDetails> getMedicienCost(int patient_id) {
		List<MedicineSaleDetails> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select *  from medicine_sale_details where status= '" + patient_id + "'");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				MedicineSaleDetails medicine = new MedicineSaleDetails(rs.getInt(1), rs.getString(2), rs.getDouble(3),
						rs.getInt(4), rs.getDouble(6), rs.getInt(5), rs.getString(7), rs.getDouble(8), rs.getDouble(9));
				list.add(medicine);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	// get due diagnostic bill
	public List<Test_invoice_details> getDiagnosticCost(int patient_id) {
		List<Test_invoice_details> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select *  from test_invoice_details where status= '" + patient_id + "'");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Test_invoice_details object = new Test_invoice_details(rs.getInt(1), rs.getString(2), rs.getInt(3),
						rs.getDouble(4), rs.getDouble(5), rs.getString(6), rs.getString(7));
				list.add(object);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public Integer getTotalCost(int patient_id) {
		int total = 0;
		try {
			pst = con
					.prepareStatement("select sum(total) from test_invoice_details where status= '" + patient_id + "'");
			ResultSet rs = pst.executeQuery();
			rs.next();
			int testCost = rs.getInt(1);

			pst = con.prepareStatement(
					"select sum(total) from medicine_sale_details where status= '" + patient_id + "'");
			ResultSet rs1 = pst.executeQuery();
			rs1.next();
			int medicineCost = rs1.getInt(1);

			total = testCost + medicineCost;

		} catch (Exception e) {
			System.out.println(e);
		}
		return total;
	}

	// get due diagnostic bill
	public Integer getBedRate(String bed_no) {
		int bedRate = 0;
		try {
			pst = con.prepareStatement("select rate  from room where bed_no= '" + bed_no + "'");
			ResultSet rs = pst.executeQuery();
			rs.next();
			bedRate = rs.getInt(1);
		} catch (Exception e) {
			System.out.println(e);
		}
		return bedRate;
	}

	public void cashDeposit(int patient_id, int deposit) {

		String description = "Account credite by Cash";
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			LocalDate localDate = LocalDate.now();
			String date = (dtf.format(localDate));

			pst = con.prepareStatement(
					"insert into admission (patient_id, description, transaction_date, deposit) values(?,?,?,?)");
			pst.setInt(1, patient_id);
			pst.setString(2, description);
			pst.setString(3, date);
			pst.setInt(4, deposit);

			pst.executeUpdate();

			System.out.println("update");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void updatePatientStatusAfterDischarge(int id) {
		// get today date and send to view
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		String date = (dtf.format(localDate));
		try {
			pst = con.prepareStatement("update patient set status='Discharge', discharge_date='" +date+"' where id= '" + id
					+ "' and status='Admitted'");
			pst.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void updateAdmissionTableAfterDischarge(int id) {
		try {
			pst = con.prepareStatement(
					"update admission set status='Discharge' where patient_id= '" + id + "' and status='Admitted'");
			pst.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void updateBedStatusAfterDischarge(String bed_no) {
		try {
			pst = con.prepareStatement(
					"update room set status='available' where bed_no= '" + bed_no + "'");
			pst.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
