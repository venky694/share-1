package com.exam.medicine;

public class Medicine {
	int id;
	String name;
	int quantity;
	double price;
	String category;
	String manufacturer;
	int reorder_level;
	
	
	
	
	public Medicine() {
		super();
	}
	public Medicine(int id) {
		super();
		this.id = id;
	}
	public Medicine(int id, String name, int quantity, double price, String category, String manufacturer,
			int reorder_level) {
		super();
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.category = category;
		this.manufacturer = manufacturer;
		this.reorder_level = reorder_level;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public int getReorder_level() {
		return reorder_level;
	}
	public void setReorder_level(int reorder_level) {
		this.reorder_level = reorder_level;
	}
	@Override
	public String toString() {
		return "Medicine [id=" + id + ", name=" + name + ", quantity=" + quantity + ", price=" + price + ", category="
				+ category + ", manufacturer=" + manufacturer + ", reorder_level=" + reorder_level + "]";
	}
	
	
}
