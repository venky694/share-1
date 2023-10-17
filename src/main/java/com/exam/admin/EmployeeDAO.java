package com.exam.admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import org.springframework.stereotype.Component;

@Component
public class EmployeeDAO {
	Connection con;
	PreparedStatement pst;

	public EmployeeDAO() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_database", "root", "idb122292");

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void saveEmployee(Employee object) {
		try {

			pst = con.prepareStatement("insert into Employee values(?,?,?,?,?,?,?,?,?,?,?)");
			pst.setInt(1, object.id);
			pst.setString(2, object.name);
			pst.setString(3, object.email);
			pst.setString(4, object.mobile);
			pst.setInt(5, object.salary);
			pst.setString(6, object.designation);
			pst.setString(7, object.password);
			pst.setString(8, object.birth_day);
			pst.setString(9, object.address);
			pst.setString(10, object.qualification);
			pst.setString(11, object.photo);

			pst.executeUpdate();
			System.out.println("insert");
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public String updateEployeeInformation(Employee object) {
		String msg = "";
		try {
			pst = con.prepareStatement("update Employee set  name='" + object.name + "', email='" + object.email
					+ "', mobile='" + object.mobile + "',salary='" + object.salary + "',designation='"
					+ object.designation + "',password='" + object.password + "',birth_date='" + object.birth_day
					+ "',address='" + object.address + "',qualification='" + object.qualification + "',photo='"
					+ object.photo + "' where em_id='" + object.id + "'");
			pst.executeUpdate();

			msg = "Information update successfully";
		} catch (Exception e) {
			System.out.println(e);
		}
		return msg;
	}

	public void delete(int id) {
		try {
			pst = con.prepareStatement("delete from Employee where id=" + id);
			pst.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public Integer getMaxEmployeeId() {
		int id = 0;
		try {
			pst = con.prepareStatement("select max(em_id)from Employee");
			ResultSet rs = pst.executeQuery();
			rs.next();
			id = rs.getInt(1) + 1;
		} catch (Exception e) {
			System.out.println(e);
		}
		return id;
	}

	public List<Employee> showAllEmployee() {
		List<Employee> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from Employee");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Employee employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getInt(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
						rs.getString(10), rs.getString(11));
				list.add(employee);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Employee> showAllDoctors() {
		List<Employee> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from Employee where designation='Doctor'");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Employee employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getInt(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
						rs.getString(10), rs.getString(11));
				list.add(employee);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Employee> showAllEmployee2() {
		List<Employee> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from Employee");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Employee employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getInt(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
						rs.getString(10), rs.getString(11));
				list.add(employee);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Employee> getBasicSalray() {
		List<Employee> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from Employee");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				int salary = rs.getInt(5);
				if (rs.getString(6).equals("Doctor")) {
					salary = rs.getInt(5) * 30;
				}

				Employee employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						salary, rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10),
						rs.getString(11));
				list.add(employee);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Employee> getEmployeeId(int id) {
		List<Employee> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from employee where em_id=" + id + "");

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Employee employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getInt(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
						rs.getString(10), rs.getString(11));
				list.add(employee);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;

	}

	public List<Employee> search(String query) {
		List<Employee> list = new ArrayList<>();
		try {
			pst = con
					.prepareStatement("select * from Employee where name like '%" + query + "%' or id='" + query + "'");

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Employee employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getInt(5), rs.getString(6), rs.getString(7));
				list.add(employee);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;

	}

	public String login(String email, String password) {
		String ss = "fail";
		try {
			pst = con.prepareStatement(
					"select * from Employee where email='" + email + "' and password='" + password + "'");

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				ss = "Success";

			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return ss;
	}

	public String getDoctorName(String email) {
		String doctorName = "";
		try {
			pst = con.prepareStatement("select name from employee where email='" + email + "'");

			ResultSet rs = pst.executeQuery();
			rs.next();
			doctorName = rs.getString(1);
		} catch (Exception e) {
			System.out.println(e);
		}
		return doctorName;

	}

	public Integer getTotalDoctor() {
		int totalDoctor = 0;
		try {
			pst = con.prepareStatement("select count(*) from employee where designation='Doctor'");
			ResultSet rs = pst.executeQuery();
			rs.next();
			totalDoctor = rs.getInt(1);
		} catch (Exception e) {
			System.out.println(e);
		}
		return totalDoctor;
	}

	public Integer getTotalPatient() {
		int totalDoctor = 0;
		try {
			pst = con.prepareStatement("select count(*) from patient");
			ResultSet rs = pst.executeQuery();
			rs.next();
			totalDoctor = rs.getInt(1);
		} catch (Exception e) {
			System.out.println(e);
		}
		return totalDoctor;
	}

	public Integer getTotalAdmittedPatient() {
		int totalDoctor = 0;
		try {
			pst = con.prepareStatement("select count(*) from patient where status='Admitted'");
			ResultSet rs = pst.executeQuery();
			rs.next();
			totalDoctor = rs.getInt(1);
		} catch (Exception e) {
			System.out.println(e);
		}
		return totalDoctor;
	}

	public Integer getTodayAppointment() {
		// today date
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		String date = (dtf.format(localDate));

		int totalDoctor = 0;
		try {
			pst = con.prepareStatement("select count(*) from patient where app_date='" + date + "'");
			ResultSet rs = pst.executeQuery();
			rs.next();
			totalDoctor = rs.getInt(1);
		} catch (Exception e) {
			System.out.println(e);
		}
		return totalDoctor;
	}

	public List<Integer> getDailyPatientAppointment() {
		List<Integer> list = new ArrayList<>();
		LocalDate today = LocalDate.now();
		int dayofMonth = today.getDayOfMonth();
		LocalDate firstDay = today.with(TemporalAdjusters.firstDayOfMonth());
		try {
			for (int i = 0; i < dayofMonth; i++) {
				LocalDate nextday = firstDay.plusDays(i); // increase by 1 day
				pst = con.prepareStatement("select count(*) from patient where app_date='" + nextday + "'");
				ResultSet rs = pst.executeQuery();
				rs.next();
				list.add(rs.getInt(1));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Integer> getDailyMedicineSales() {
		List<Integer> list = new ArrayList<>();
		LocalDate today = LocalDate.now();
		LocalDate firstDay = today.with(TemporalAdjusters.firstDayOfMonth());
		try {
			for (int i = 0; i <= 30; i++) {
				LocalDate nextday = firstDay.plusDays(i); // increase by 1 day
				pst = con.prepareStatement("select sum(total) from medicine_sale where date='" + nextday + "'");
				ResultSet rs = pst.executeQuery();
				rs.next();
				list.add(rs.getInt(1));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}
}
