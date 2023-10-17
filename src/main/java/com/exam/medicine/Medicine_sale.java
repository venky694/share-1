package com.exam.medicine;
public class Medicine_sale {

	int voucher_no;
	String customer_name;
	String mobile;
	String address;
	Double subtotal;
	Double discount;
	Double total;
	String date;
	String status;
	
	
	public Medicine_sale() {
		super();
	}


	public Medicine_sale(int voucher_no) {
		super();
		this.voucher_no = voucher_no;
	}





	public Medicine_sale(int voucher_no, String customer_name, String mobile, String address, Double subtotal,
			Double discount, Double total, String date, String status) {
		super();
		this.voucher_no = voucher_no;
		this.customer_name = customer_name;
		this.mobile = mobile;
		this.address = address;
		this.subtotal = subtotal;
		this.discount = discount;
		this.total = total;
		this.date = date;
		this.status = status;
	}


	public int getVoucher_no() {
		return voucher_no;
	}


	public void setVoucher_no(int voucher_no) {
		this.voucher_no = voucher_no;
	}


	public String getCustomer_name() {
		return customer_name;
	}


	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public Double getSubtotal() {
		return subtotal;
	}


	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
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


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return "Medicine_sale [voucher_no=" + voucher_no + ", customer_name=" + customer_name + ", mobile=" + mobile
				+ ", address=" + address + ", subtotal=" + subtotal + ", discount=" + discount + ", total=" + total
				+ ", date=" + date + ", status=" + status + "]";
	}


}

