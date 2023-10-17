package com.exam.medicine;

public class MedicineSaleDetails {

	int id;
	String medicine_name;
	Double price;
	int quantity;
	Double subtotal;
	int voucher_no;
	String status;
	Double discount;
	Double total;
		


	

	public MedicineSaleDetails() {
		super();
	}



	public MedicineSaleDetails(int id) {
		super();
		this.id = id;
	}



	

	

	public MedicineSaleDetails(int id, String medicine_name, Double price, int quantity, Double subtotal,
			int voucher_no, String status, Double discount, Double total) {
		super();
		this.id = id;
		this.medicine_name = medicine_name;
		this.price = price;
		this.quantity = quantity;
		this.subtotal = subtotal;
		this.voucher_no = voucher_no;
		this.status = status;
		this.discount = discount;
		this.total = total;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getMedicine_name() {
		return medicine_name;
	}



	public void setMedicine_name(String medicine_name) {
		this.medicine_name = medicine_name;
	}



	public Double getPrice() {
		return price;
	}



	public void setPrice(Double price) {
		this.price = price;
	}



	public int getQuantity() {
		return quantity;
	}



	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}



	public Double getSubtotal() {
		return subtotal;
	}



	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}



	public int getVoucher_no() {
		return voucher_no;
	}



	public void setVoucher_no(int voucher_no) {
		this.voucher_no = voucher_no;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
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



	@Override
	public String toString() {
		return "MedicineSaleDetails [id=" + id + ", medicine_name=" + medicine_name + ", price=" + price + ", quantity="
				+ quantity + ", subtotal=" + subtotal + ", voucher_no=" + voucher_no + ", status=" + status
				+ ", discount=" + discount + ", total=" + total + "]";
	}



	
}
