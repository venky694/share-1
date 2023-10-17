package com.exam.diagnostic;

public class Test_invoice_details {

	int id;
	String test_name;
	int rate;
	Double discount;
	Double total;
	String voucher_no;
	String status;
	
	
	public Test_invoice_details() {
		super();
	}


	public Test_invoice_details(int id) {
		super();
		this.id = id;
	}


	
	public Test_invoice_details(int rate, Double discount, Double total) {
		super();
		this.rate = rate;
		this.discount = discount;
		this.total = total;
	}


	


	public Test_invoice_details(int id, String test_name, int rate, Double discount, Double total, String voucher_no,
			String status) {
		super();
		this.id = id;
		this.test_name = test_name;
		this.rate = rate;
		this.discount = discount;
		this.total = total;
		this.voucher_no = voucher_no;
		this.status = status;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getTest_name() {
		return test_name;
	}


	public void setTest_name(String test_name) {
		this.test_name = test_name;
	}


	public int getRate() {
		return rate;
	}


	public void setRate(int rate) {
		this.rate = rate;
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


	public String getVoucher_no() {
		return voucher_no;
	}


	public void setVoucher_no(String voucher_no) {
		this.voucher_no = voucher_no;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return "Test_invoice_details [id=" + id + ", test_name=" + test_name + ", rate=" + rate + ", discount="
				+ discount + ", total=" + total + ", voucher_no=" + voucher_no + ", status=" + status + "]";
	}


	
	
	
	
}
