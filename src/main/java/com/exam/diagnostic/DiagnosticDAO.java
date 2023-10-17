package com.exam.diagnostic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.exam.doctor.test_name;
import com.exam.medicine.Medicine_sale;
import com.exam.receptions.Patient;

@Component
public class DiagnosticDAO {

	Connection con;
	PreparedStatement pst;

	public DiagnosticDAO() {
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

	public String insertSelectTestToVoucherDetails(Test_invoice_details object) {

		String msg = "";
		Boolean has = true;
		String status="paid";

		int rate = object.getRate();
		double discount_rate = object.getDiscount();
		Double total_discount = rate * discount_rate / 100;
		Double total = rate - total_discount;
		try {

			pst = con.prepareStatement("SELECT * FROM test_invoice_details WHERE test_name LIKE '%"
					+ object.getTest_name() + "%' and voucher_no=" + object.getVoucher_no() + "");

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				msg = "This test already exist in the voucher ";
				has = false;
			}
			if (has) {

				pst = con.prepareStatement(
						" insert into test_invoice_details (test_name, rate, discount, total, voucher_no, status) VALUES (?,?,?,?,?,?)");
				pst.setString(1, object.getTest_name());
				pst.setInt(2, rate);
				pst.setDouble(3, total_discount);
				pst.setDouble(4, total);
				pst.setString(5, object.getVoucher_no());
				pst.setString(6, status);
				pst.executeUpdate();
				msg = "Test Add to invoice ";
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return msg;
	}
	
	public void saveInvoiceDetailsToTestInvoice(List<Test_invoice_details> object, int patientId, int voucher_no) {
		String status="paid";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		String date = (dtf.format(localDate));
		try {
				pst = con.prepareStatement(" insert into test_invoice VALUES (?,?,?,?,?,?,?)");
				pst.setInt(1,voucher_no);
				pst.setInt(2, patientId);
				pst.setDouble(3, object.get(0).getRate());
				pst.setDouble(4, object.get(0).getDiscount());
				pst.setDouble(5, object.get(0).getTotal());
				pst.setString(6, status);
				pst.setString(7, date);
				pst.executeUpdate();			
		
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}

	
	public List<Test_invoice_details> getAllSelectedTest(int voucherNo) {
		List<Test_invoice_details> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from Test_invoice_details where voucher_no= '" + voucherNo + "'");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Test_invoice_details slected_test = new Test_invoice_details(rs.getInt(1), rs.getString(2),
						rs.getInt(3), rs.getDouble(4), rs.getDouble(5), rs.getString(6),rs.getString(7));
				list.add(slected_test);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Test_invoice_details> getInvoiceTotal(int voucherNo) {
		List<Test_invoice_details> list = new ArrayList<>();
		try {
			pst = con.prepareStatement(
					"select sum(rate), sum(discount), sum(total) from Test_invoice_details where voucher_no= '"
							+ voucherNo + "'");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Test_invoice_details slected_test = new Test_invoice_details(rs.getInt(1), rs.getDouble(2),
						rs.getDouble(3));
				list.add(slected_test);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public void removeSelectedTestName(int id) {
		try {
			pst = con.prepareStatement("delete  from Test_invoice_details where id=" + id);
			pst.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// get latest or largest voucher number form test invoice table
	public Integer getLatestVoucherNO() {
		int vouchreNO = 0;
		try {
			pst = con.prepareStatement("select max(voucher_no) from test_invoice");
			ResultSet rs = pst.executeQuery();
			rs.next();
			vouchreNO = rs.getInt(1);
		} catch (Exception e) {
			System.out.println(e);
		}
		return vouchreNO;
	}

	public Integer getSelectedTestRate(test_name testName) {
		int rate = 0;
		try {
			pst = con.prepareStatement(
					"select test_rate from test_name where test_name='" + testName.getTest_name() + "'");
			ResultSet rs = pst.executeQuery();
			rs.next();
			rate = rs.getInt(1);
		} catch (Exception e) {
			System.out.println(e);
		}
		return rate;
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
	
	public void saveInvoiceDetailsToTestInvoice2(List<Test_invoice_details> object, int patientId, int voucher_no) {
		String status="Due";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		String date = (dtf.format(localDate));
		try {
				pst = con.prepareStatement(" insert into test_invoice VALUES (?,?,?,?,?,?,?)");
				pst.setInt(1,voucher_no);
				pst.setInt(2, patientId);
				pst.setDouble(3, object.get(0).getRate());
				pst.setDouble(4, object.get(0).getDiscount());
				pst.setDouble(5, object.get(0).getTotal());
				pst.setString(6, status);
				pst.setString(7, date);
				pst.executeUpdate();			
			
				pst = con.prepareStatement(" update Test_invoice_details set status='"+patientId+"' where voucher_no="+ voucher_no+"");
				pst.executeUpdate();	
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	
	
	// code for report.........................................................................................................
	public List<Test_invoice> getDailyDignosticIncome(String date) {
		List<Test_invoice> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from test_invoice where date='" + date + "'");

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Test_invoice todaySale = new Test_invoice(rs.getInt(1), rs.getInt(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getString(6),rs.getString(7));
				list.add(todaySale);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Test_invoice> getDailyCashDignosticIncome(String date) {
		List<Test_invoice> list = new ArrayList<>();
		try {
			pst = con.prepareStatement(
					"select * from test_invoice where date='" + date + "' and status='paid'");

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Test_invoice todaySale = new Test_invoice(rs.getInt(1), rs.getInt(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getString(6),rs.getString(7));
				list.add(todaySale);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Test_invoice> getDailyAcrruedDignosticIncome(String date) {
		List<Test_invoice> list = new ArrayList<>();
		;
		try {
			pst = con.prepareStatement(
					"select * from test_invoice where date='" + date + "' and status !='paid'");

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Test_invoice todaySale =new Test_invoice(rs.getInt(1), rs.getInt(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getString(6),rs.getString(7));
				list.add(todaySale);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Integer> getDailyTotalDignosticIncome(String date) {
		List<Integer> list = new ArrayList<>();
		try {
			pst = con.prepareStatement(
					"select  sum(sub_total), sum(discount), sum(total) from test_invoice where date='" + date + "'");

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add((int)rs.getDouble(1));
				list.add((int)rs.getDouble(2));
				list.add((int)rs.getDouble(3));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Integer> getDailyTotalCashDignosticIncome(String date) {
		List<Integer> list = new ArrayList<>();
		try {
			pst = con
					.prepareStatement("select  sum(sub_total), sum(discount), sum(total) from test_invoice where date='"
							+ date + "' and status ='paid'");

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add((int)rs.getDouble(1));
				list.add((int)rs.getDouble(2));
				list.add((int)rs.getDouble(3));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Integer> getDailyAccruedTotalDignosticIncome(String date) {
		List<Integer> list = new ArrayList<>();
		try {
			pst = con
					.prepareStatement("select  sum(sub_total), sum(discount), sum(total) from test_invoice where date='"
							+ date + "' and status !='paid'");

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add((int)rs.getDouble(1));
				list.add((int)rs.getDouble(2));
				list.add((int)rs.getDouble(3));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Test_invoice> getMonthlyDiagnosticIncome(LocalDate firstDay, LocalDate lastDay) {
		List<Test_invoice> list = new ArrayList<>();
		list.removeAll(list);
		try {
			pst = con.prepareStatement(
					"select * from test_invoice where date  between '" + firstDay + "' and '" + lastDay + "'");

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Test_invoice todaySale =new Test_invoice(rs.getInt(1), rs.getInt(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getString(6),rs.getString(7));
				list.add(todaySale);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Integer> getMonthlyTotalDiagnosticIncome(LocalDate firstDay, LocalDate lastDay) {
		List<Integer> list = new ArrayList<>();
		list.removeAll(list);
		try {
			pst = con.prepareStatement(
					"select sum(sub_total), sum(discount), sum(total) from test_invoice where date  between '"
							+ firstDay + "' and '" + lastDay + "'");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add((int)rs.getDouble(1));
				list.add((int)rs.getDouble(2));
				list.add((int)rs.getDouble(3));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	
	String[] days = { "2021-01-01", "2021-01-02", "2021-01-03", "2021-01-04", "2021-01-05", "2021-01-06", "2021-01-07",
			"2021-01-08", "2021-01-09", "2021-01-10", "2021-01-11", "2021-01-12", "2021-01-13", "2021-01-14",
			"2021-01-15", "2021-01-16", "2021-01-17", "2021-01-18", "2021-01-19", "2021-01-120", "2021-01-21",
			"2021-01-22", "2021-01-23", "2021-01-24", "2021-01-25", "2021-01-26", "2021-01-27", "2021-01-28",
			"2021-01-29", "2021-01-30", "2021-01-31", "2021-02-01", "2021-02-02", "2021-02-03", "2021-02-04",
			"2021-02-05", "2021-02-06", "2021-02-07", "2021-02-08", "2021-02-09", "2021-02-10", "2021-02-11",
			"2021-02-12", "2021-02-13", "2021-02-14", "2021-02-15", "2021-02-16", "2021-02-17", "2021-02-18",
			"2021-02-19", "2021-02-20", "2021-02-21", "2021-02-22", "2021-02-23", "2021-02-24", "2021-02-25",
			"2021-02-26", "2021-02-27", "2021-02-28", "2021-03-01", "2021-03-02", "2021-03-03", "2021-03-04",
			"2021-03-05", "2021-03-06", "2021-03-07", "2021-03-08", "2021-03-09", "2021-03-10", "2021-03-11",
			"2021-03-12", "2021-03-13", "2021-03-14", "2021-03-15", "2021-03-16", "2021-03-17", "2021-03-18",
			"2021-03-19", "2021-03-20", "2021-03-21", "2021-03-22", "2021-03-23", "2021-03-24", "2021-03-25",
			"2021-03-26", "2021-03-27", "2021-03-28", "2021-03-29", "2021-03-30", "2021-03-31", "2021-04-01",
			"2021-04-02", "2021-04-03", "2021-04-04", "2021-04-05", "2021-04-06", "2021-04-07", "2021-04-08",
			"2021-04-09", "2021-04-10", "2021-04-11", "2021-04-12", "2021-04-13", "2021-04-14", "2021-04-15",
			"2021-04-16", "2021-04-17", "2021-04-18", "2021-04-19", "2021-04-20", "2021-04-21", "2021-04-22",
			"2021-04-23", "2021-04-24", "2021-04-25", "2021-04-26", "2021-04-27", "2021-04-28", "2021-04-29",
			"2021-04-30", "2021-05-01", "2021-05-02", "2021-05-03", "2021-05-04", "2021-05-05", "2021-05-06",
			"2021-05-07", "2021-05-08", "2021-05-09", "2021-05-10", "2021-05-11", "2021-05-12", "2021-05-13",
			"2021-05-14", "2021-05-15", "2021-05-16", "2021-05-17", "2021-05-18", "2021-05-19", "2021-05-20",
			"2021-05-21", "2021-05-22", "2021-05-23", "2021-05-24", "2021-05-25", "2021-05-26", "2021-05-27",
			"2021-05-28", "2021-05-29", "2021-05-30", "2021-05-31", "2021-06-01", "2021-06-02", "2021-06-03",
			"2021-06-04", "2021-06-05", "2021-06-06", "2021-06-07", "2021-06-08", "2021-06-09", "2021-06-10",
			"2021-06-11", "2021-06-12", "2021-06-13", "2021-06-14", "2021-06-15", "2021-06-16", "2021-06-17",
			"2021-06-18", "2021-06-19", "2021-06-20", "2021-06-21", "2021-06-22", "2021-06-23", "2021-06-24",
			"2021-06-25", "2021-06-26", "2021-06-27", "2021-06-28", "2021-06-29", "2021-06-30", "2021-07-01",
			"2021-07-02", "2021-07-03", "2021-07-04", "2021-07-05", "2021-07-06", "2021-07-07", "2021-07-08",
			"2021-07-09", "2021-07-10", "2021-07-11", "2021-07-12", "2021-07-13", "2021-07-14", "2021-07-15",
			"2021-07-16", "2021-07-17", "2021-07-18", "2021-07-19", "2021-07-20", "2021-07-21", "2021-07-22",
			"2021-07-23", "2021-07-24", "2021-07-25", "2021-07-26", "2021-07-27", "2021-07-28", "2021-07-29",
			"2021-07-30", "2021-07-31", "2021-08-01", "2021-08-02", "2021-08-03", "2021-08-04", "2021-08-05",
			"2021-08-06", "2021-08-07", "2021-08-08", "2021-08-09", "2021-08-10", "2021-08-11", "2021-08-12",
			"2021-08-13", "2021-08-14", "2021-08-15", "2021-08-16", "2021-08-17", "2021-08-18", "2021-08-19",
			"2021-08-20", "2021-08-21", "2021-08-22", "2021-08-23", "2021-08-24", "2021-08-25", "2021-08-26",
			"2021-08-27", "2021-08-28", "2021-08-29", "2021-08-30", "2021-08-31", "2021-09-01", "2021-09-02",
			"2021-09-03", "2021-09-04", "2021-09-05", "2021-09-06", "2021-09-07", "2021-09-08", "2021-09-09",
			"2021-09-10", "2021-09-11", "2021-09-12", "2021-09-13", "2021-09-14", "2021-09-15", "2021-09-16",
			"2021-09-17", "2021-09-18", "2021-09-19", "2021-09-20", "2021-09-21", "2021-09-22", "2021-09-23",
			"2021-09-24", "2021-09-25", "2021-09-26", "2021-09-27", "2021-09-28", "2021-09-29", "2021-09-30",
			"2021-10-01", "2021-10-02", "2021-10-03", "2021-10-04", "2021-10-05", "2021-10-06", "2021-10-07",
			"2021-10-08", "2021-10-09", "2021-10-10", "2021-10-11", "2021-10-12", "2021-10-13", "2021-10-14",
			"2021-10-15", "2021-10-16", "2021-10-17", "2021-10-18", "2021-10-19", "2021-10-20", "2021-10-21",
			"2021-10-22", "2021-10-23", "2021-10-24", "2021-10-25", "2021-10-26", "2021-10-27", "2021-10-28",
			"2021-10-29", "2021-10-30", "2021-10-31", "2021-11-01", "2021-11-02", "2021-11-03", "2021-11-04",
			"2021-11-05", "2021-11-06", "2021-11-07", "2021-11-08", "2021-11-09", "2021-11-10", "2021-11-11",
			"2021-11-12", "2021-11-13", "2021-11-14", "2021-11-15", "2021-11-16", "2021-11-17", "2021-11-18",
			"2021-11-19", "2021-11-20", "2021-11-21", "2021-11-22", "2021-11-23", "2021-11-24", "2021-11-25",
			"2021-11-26", "2021-11-27", "2021-11-28", "2021-11-29", "2021-11-30", "2021-12-01", "2021-12-02",
			"2021-12-03", "2021-12-04", "2021-12-05", "2021-12-06", "2021-12-07", "2021-12-08", "2021-12-09",
			"2021-12-10", "2021-12-11", "2021-12-12", "2021-12-13", "2021-12-14", "2021-12-15", "2021-12-16",
			"2021-12-17", "2021-12-18", "2021-12-19", "2021-12-20", "2021-12-21", "2021-12-22", "2021-12-23",
			"2021-12-24", "2021-12-25", "2021-12-26", "2021-12-27", "2021-12-28", "2021-12-29", "2021-12-30",
			"2021-12-31", };

	public List<Integer> getDailyIncomeOverTheYear() {
		List<Integer> list = new ArrayList<>();
		try {
			for (int i = 0; i < days.length; i++) {
				pst = con.prepareStatement("select sum(total) from test_invoice where date  between '" + days[i]
						+ "' and '" + days[i] + "'");
				ResultSet rs = pst.executeQuery();
				rs.next();
				int ss=(int)rs.getDouble(1);
				list.add(ss);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}
	
	
	public List<Integer> getMonthlyTotalIncomeOverTheYear(String firstDay, String lastDay) {
		List<Integer> list = new ArrayList<>();
		list.removeAll(list);
		try {
			pst = con.prepareStatement(
					"select sum(total) from test_invoice where date  between '"
							+ firstDay + "' and '" + lastDay + "'");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				list.add((int)rs.getDouble(1));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Integer> getDailyDiagnosticeRevenueOverTheMonth() {
		List<Integer> list = new ArrayList<>();
		LocalDate today = LocalDate.now();
		LocalDate firstDay = today.with(TemporalAdjusters.firstDayOfMonth());
		try {
			for (int i = 0; i <= 30; i++) {
				LocalDate nextday = firstDay.plusDays(i); // increase by 1 day
				pst = con.prepareStatement("select sum(total) from test_invoice where date='" + nextday + "'");
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
