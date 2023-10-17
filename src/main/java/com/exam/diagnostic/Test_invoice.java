package com.exam.diagnostic;

import java.time.LocalDate;

public class Test_invoice {
	int voucher_no;
	int patient_id;
	Double sub_total;
	Double discount;
	Double total;
	String status;
	String date;
	
	
	public Test_invoice() {
		super();
	}


	public Test_invoice(int voucher_no) {
		super();
		this.voucher_no = voucher_no;
	}


	


	


	public Test_invoice(int voucher_no, int patient_id, Double sub_total, Double discount, Double total, String status,
			String date) {
		super();
		this.voucher_no = voucher_no;
		this.patient_id = patient_id;
		this.sub_total = sub_total;
		this.discount = discount;
		this.total = total;
		this.status = status;
		this.date = date;
	}


	public int getVoucher_no() {
		return voucher_no;
	}


	public void setVoucher_no(int voucher_no) {
		this.voucher_no = voucher_no;
	}


	public int getPatient_id() {
		return patient_id;
	}


	public void setPatient_id(int patient_id) {
		this.patient_id = patient_id;
	}


	public Double getSub_total() {
		return sub_total;
	}


	public void setSub_total(Double sub_total) {
		this.sub_total = sub_total;
	}


	public Double getDiscount() {
		return discount;
	}


	public void setDiscount(Double discount) {
		this.discount = discount;
	}


	public Double getTotal() {
		return total;
	}


	public void setTotal(Double total) {
		this.total = total;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	@Override
	public String toString() {
		return "Test_invoice [voucher_no=" + voucher_no + ", patient_id=" + patient_id + ", sub_total=" + sub_total
				+ ", discount=" + discount + ", total=" + total + ", status=" + status + "]";
	}


	

}
