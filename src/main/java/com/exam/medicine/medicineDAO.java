package com.exam.medicine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.exam.receptions.Patient;

import come.exam.admission.Admission;

@Component
public class medicineDAO {
	Connection con;
	PreparedStatement pst;

	public medicineDAO() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_database", "root", "idb122292");
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void insert(Medicine object) {
		try {
			pst = con.prepareStatement("insert into medicine values(?,?,?,?,?,?,?)");
			pst.setInt(1, object.id);
			pst.setString(2, object.name);
			pst.setInt(3, object.getQuantity());
			pst.setDouble(4, object.getPrice());
			pst.setString(5, object.getCategory());
			pst.setString(6, object.getManufacturer());
			pst.setInt(7, object.getReorder_level());

			pst.executeUpdate();
			System.out.println("insert");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void update(Medicine object) {
		try {
			pst = con.prepareStatement("update medicine set  name='" + object.name + "', quantity='" + object.quantity
					+ "', price='" + object.price + "' where id='" + object.id + "'");
			pst.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void delete(int id) {
		try {
			pst = con.prepareStatement("delete from medicine where id=" + id);
			pst.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public List<Medicine> show() {
		List<Medicine> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from medicine");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Medicine medicine = new Medicine(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDouble(4),
						rs.getString(5), rs.getString(6), rs.getInt(7));
				list.add(medicine);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Medicine> reorderMedicineList() {
		List<Medicine> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from medicine where quantity<= reorder_level");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Medicine medicine = new Medicine(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDouble(4),
						rs.getString(5), rs.getString(6), rs.getInt(7));
				list.add(medicine);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public Integer getLatestMedicineID() {
		int id = 0;
		try {
			pst = con.prepareStatement("select max(id) from medicine");
			ResultSet rs = pst.executeQuery();
			rs.next();
			id = rs.getInt(1);
		} catch (Exception e) {
			System.out.println(e);
		}
		return id;
	}

	public List<Medicine> getById(int id) {
		List<Medicine> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from medicine where id=" + id + "");

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Medicine medicine = new Medicine(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDouble(4),
						rs.getString(5), rs.getString(6), rs.getInt(7));
				list.add(medicine);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Medicine> search(String query) {
		List<Medicine> list = new ArrayList<>();
		try {
			pst = con
					.prepareStatement("select * from medicine where name like '%" + query + "%' or id='" + query + "'");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Medicine medicine = new Medicine(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDouble(4),
						rs.getString(5), rs.getString(6), rs.getInt(7));
				list.add(medicine);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<String> getMedicineName() {
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

	public List<Medicine> getMedicinePrice(String medicineNmae) {
		List<Medicine> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from medicine where name='" + medicineNmae + "'");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Medicine medicine = new Medicine(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDouble(4),
						rs.getString(5), rs.getString(6), rs.getInt(7));
				list.add(medicine);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public Integer getVoucherNo() {
		int voucherNo = 0;
		try {
			pst = con.prepareStatement("select max(voucher_no) from medicine_sale");
			ResultSet rs = pst.executeQuery();
			rs.next();
			voucherNo = rs.getInt(1);

		} catch (Exception e) {
			System.out.println(e);
		}
		return voucherNo;
	}

	public String saveMedicineSaleDetails(MedicineSaleDetails object, int voucherNo, int oldStock, int meidcineID) {
		String msg = "";

		boolean has = true;
		try {
			pst = con.prepareStatement("SELECT * FROM medicine_sale_details WHERE medicine_name LIKE '%"
					+ object.getMedicine_name() + "%' and voucher_no=" + voucherNo + "");

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				msg = "This product already exist in the voucher ";
				has = false;

			}
			if (has) {
				Double price = object.price;
				int quanity = object.quantity;
				Double disountRate = object.discount;
				Double sub_total = price * Double.valueOf(quanity);
				String status = "out side sale";
				Double discount = sub_total * (disountRate / 100);
				Double total = sub_total - discount;
				int newStock = oldStock - quanity;

				// add to medicine voucher cart
				pst = con.prepareStatement(
						" insert into medicine_sale_details (medicine_name, price, quantity, subtotal, voucher_no, status, discount, total) VALUES ('"
								+ object.getMedicine_name() + "','" + object.getPrice() + "','" + object.getQuantity()
								+ "','" + sub_total + "','" + voucherNo + "','" + status + "','" + discount + "','"
								+ total + "')");
				pst.executeUpdate();

				// Decrease quantity from Stock
				pst = con.prepareStatement(
						"update medicine set  quantity='" + newStock + "' where id=" + meidcineID + "  ");
				pst.executeUpdate();

				msg = "Medicine add to vouceher ";
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return msg;
	}

	public List<MedicineSaleDetails> sowVoucherMedicine(int voucherNo) {
		List<MedicineSaleDetails> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from medicine_sale_details where voucher_no=" + voucherNo);

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				MedicineSaleDetails medicine = new MedicineSaleDetails(rs.getInt(1), rs.getString(2), rs.getDouble(3),
						rs.getInt(4), rs.getDouble(5), rs.getInt(6), rs.getString(7), rs.getDouble(8), rs.getDouble(9));
				list.add(medicine);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Double> getSubtotalDiscountAndGrandTotal(int voucherNo) {
		List<Double> list = new ArrayList<>();

		try {
			pst = con.prepareStatement(
					"select sum(subtotal), sum(discount), sum(total) sum from medicine_sale_details where voucher_no="
							+ voucherNo);
			ResultSet rs = pst.executeQuery();
			rs.next();
			list.add(0, rs.getDouble(1));
			list.add(1, rs.getDouble(2));
			list.add(2, rs.getDouble(3));

		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public void removeMedicineFromVoucher(int voucherNo, MedicineSaleDetails object) {

		int oldStock = 0;
		int newStock = 0;
		try {
			pst = con.prepareStatement(
					"delete from medicine_sale_details where voucher_no=" + voucherNo + " and id= " + object.id);
			pst.executeUpdate();

			// get old Sock of medicine
			pst = con.prepareStatement("select quantity from medicine where name='" + object.medicine_name + "'");
			ResultSet rs = pst.executeQuery();
			rs.next();
			oldStock = rs.getInt(1);
			newStock = oldStock + object.quantity;
			System.out.println("quantity" + object.quantity);
			System.out.println("old stock " + oldStock);
			System.out.println("new stock" + newStock);
			// increase stock when delete from voucher cart
			pst = con.prepareStatement(
					"update medicine set quantity=" + newStock + " where name ='" + object.medicine_name + "'");
			pst.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void saveSaleDetailes(Medicine_sale object, List<Double> list) {
		// Get Today Date and send to view
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		String date = (dtf.format(localDate));

		String status = "Out Side Sale";
		try {

			pst = con.prepareStatement("insert into medicine_sale values(?,?,?,?,?,?,?,?,?)");
			pst.setInt(1, object.voucher_no);
			pst.setString(2, object.customer_name);
			pst.setString(3, object.mobile);
			pst.setString(4, object.address);
			pst.setDouble(5, list.get(0));
			pst.setDouble(6, list.get(1));
			pst.setDouble(7, list.get(2));
			pst.setString(8, date);
			pst.setString(9, status);

			pst.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public String saveInsideSale(List<Patient> object, List<Double> list, int voucher_no) {
		String msg = "";
		// Get Today Date and send to view
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		String date = (dtf.format(localDate));
		try {

			pst = con.prepareStatement("insert into medicine_sale values(?,?,?,?,?,?,?,?,?)");
			pst.setInt(1, voucher_no);
			pst.setString(2, object.get(0).getName());
			pst.setString(3, object.get(0).getMobile());
			pst.setString(4, object.get(0).getAddress());
			pst.setDouble(5, list.get(0));
			pst.setDouble(6, list.get(1));
			pst.setDouble(7, list.get(2));
			pst.setString(8, date);
			pst.setInt(9, object.get(0).getId());

			pst.executeUpdate();

			// update status of medicine sale details table with patient id
			pst = con.prepareStatement("update medicine_sale_details set status= '" + object.get(0).getId()
					+ "' where voucher_no=  " + voucher_no);
			pst.executeUpdate();
			msg = "success";
		} catch (Exception e) {
			System.out.println(e);
		}
		return msg;
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
	public List<Patient> getPatientMedicineInvoice(int id) {
		List<Patient> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from Patient where id=" + id + "");
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

	public List<Admission> getPatientAdmissionInfo(int patient_id) {
		List<Admission> list = new ArrayList<>();
		try {
			pst = con.prepareStatement(
					"select *  from admission where patient_id= '" + patient_id + "' and status='Admitted'");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Admission admission = new Admission(rs.getDate(4), rs.getString(8), rs.getString(9));
				list.add(admission);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Medicine_sale> getTodaySale(String date) {
		List<Medicine_sale> list = new ArrayList<>();
		try {
			pst = con.prepareStatement("select * from medicine_sale where date='" + date + "'");

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Medicine_sale todaySale = new Medicine_sale(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getString(8),
						rs.getString(9));
				list.add(todaySale);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Medicine_sale> getTodayCashSale(String date) {
		List<Medicine_sale> list = new ArrayList<>();
		try {
			pst = con.prepareStatement(
					"select * from medicine_sale where date='" + date + "' and status='Out Side Sale'");

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Medicine_sale todaySale = new Medicine_sale(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getString(8),
						rs.getString(9));
				list.add(todaySale);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Medicine_sale> getTodayDueSale(String date) {
		List<Medicine_sale> list = new ArrayList<>();
		;
		try {
			pst = con.prepareStatement(
					"select * from medicine_sale where date='" + date + "' and status !='Out Side Sale'");

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Medicine_sale todaySale = new Medicine_sale(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getString(8),
						rs.getString(9));
				list.add(todaySale);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Integer> getTodaySaleTotal(String date) {
		List<Integer> list = new ArrayList<>();
		try {
			pst = con.prepareStatement(
					"select  sum(subtotal), sum(discount), sum(total) from medicine_sale where date='" + date + "'");

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

	public List<Integer> getTodayCashSaleTotal(String date) {
		List<Integer> list = new ArrayList<>();
		try {
			pst = con
					.prepareStatement("select  sum(subtotal), sum(discount), sum(total) from medicine_sale where date='"
							+ date + "' and status ='Out Side Sale'");

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

	public List<Integer> getTodayDueSaleTotal(String date) {
		List<Integer> list = new ArrayList<>();
		try {
			pst = con
					.prepareStatement("select  sum(subtotal), sum(discount), sum(total) from medicine_sale where date='"
							+ date + "' and status !='Out Side Sale'");

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

	public List<Medicine_sale> getMonthlySale(LocalDate firstDay, LocalDate lastDay) {
		List<Medicine_sale> list = new ArrayList<>();
		list.removeAll(list);
		try {
			pst = con.prepareStatement(
					"select * from medicine_sale where date  between '" + firstDay + "' and '" + lastDay + "'");

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Medicine_sale todaySale = new Medicine_sale(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getString(8),
						rs.getString(9));
				list.add(todaySale);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<Integer> getMonthlyTotalSaleByMonth(LocalDate firstDay, LocalDate lastDay) {
		List<Integer> list = new ArrayList<>();
		list.removeAll(list);
		try {
			pst = con.prepareStatement(
					"select sum(subtotal), sum(discount), sum(total) from medicine_sale where date  between '"
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

	public List<Integer> getDailySale() {
		List<Integer> list = new ArrayList<>();
		try {
			for (int i = 0; i < days.length; i++) {
				pst = con.prepareStatement("select sum(total) from medicine_sale where date  between '" + days[i]
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
	
	
	public List<Integer> getMonthlyTotalSaleByString(String firstDay, String lastDay) {
		List<Integer> list = new ArrayList<>();
		list.removeAll(list);
		try {
			pst = con.prepareStatement(
					"select sum(total) from medicine_sale where date  between '"
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
	
	public List<Medicine_sale> getCustomerInfo(int voucher_no) {
		List<Medicine_sale> list = new ArrayList<>();
		try {
			pst = con.prepareStatement(
					"select * from medicine_sale where voucher_no=" + voucher_no );

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Medicine_sale todaySale = new Medicine_sale(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getString(8),
						rs.getString(9));
				list.add(todaySale);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

}