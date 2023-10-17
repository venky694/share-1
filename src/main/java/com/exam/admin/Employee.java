package com.exam.admin;

public class Employee {
	int id;
	String name;
	String email;
	String mobile;
	int salary;
	String designation;
	String password;
	String birth_day;
	String address;
	String qualification;
	String photo;
	
	
	
	
	
	public Employee() {
		super();
	}
	public Employee(int id) {
		super();
		this.id = id;
	}
	
	

	public Employee(int id, String name, String email, String mobile, int salary, String designation, String password) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.salary = salary;
		this.designation = designation;
		this.password = password;
	}
	
	
	public Employee(int id, String name, String email, String mobile, int salary, String designation, String password,
			String birth_day, String address, String qualification, String photo) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.salary = salary;
		this.designation = designation;
		this.password = password;
		this.birth_day = birth_day;
		this.address = address;
		this.qualification = qualification;
		this.photo = photo;
	}
	
	
	public String getBirth_day() {
		return birth_day;
	}
	public void setBirth_day(String birth_day) {
		this.birth_day = birth_day;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", email=" + email + ", mobile=" + mobile + ", salary="
				+ salary + ", designation=" + designation + ", password=" + password + ", birth_day=" + birth_day
				+ ", address=" + address + ", qualification=" + qualification + ", photo=" + photo + "]";
	}
	
	
	
	
	
	
}
