package com.exam.receptions;
import java.sql.Date;

public class Patient {
int id;
String name;
String email;
String age;
String gender;
String address;
String mobile;
int payment;
Date app_date;
Date app_create_date;
String doctor;
String status;
String problem;
Date admission_date;
String bed_no;
String floor;
Date discharge_date;

public Patient() {
	super();
}
public Patient(int id) {
	super();
	this.id = id;
}
public Patient(int id, String name, String email, String age, String gender, String address, String mobile, int payment,
		Date app_date, Date app_create_date, String doctor, String status, String problem) {
	super();
	this.id = id;
	this.name = name;
	this.email = email;
	this.age = age;
	this.gender = gender;
	this.address = address;
	this.mobile = mobile;
	this.payment = payment;
	this.app_date = app_date;
	this.app_create_date = app_create_date;
	this.doctor = doctor;
	this.status = status;
	this.problem = problem;
}



public Patient(int id, String name, String age, String gender, String mobile, String doctor, String problem,
		Date admission_date, String bed_no, String floor) {
	super();
	this.id = id;
	this.name = name;
	this.age = age;
	this.gender = gender;
	this.mobile = mobile;
	this.doctor = doctor;
	this.problem = problem;
	this.admission_date = admission_date;
	this.bed_no = bed_no;
	this.floor = floor;
}


public Date getDischarge_date() {
	return discharge_date;
}
public void setDischarge_date(Date discharge_date) {
	this.discharge_date = discharge_date;
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
public String getAge() {
	return age;
}
public void setAge(String age) {
	this.age = age;
}
public String getGender() {
	return gender;
}
public void setGender(String gender) {
	this.gender = gender;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public String getMobile() {
	return mobile;
}
public void setMobile(String mobile) {
	this.mobile = mobile;
}
public int getPayment() {
	return payment;
}
public void setPayment(int payment) {
	this.payment = payment;
}
public Date getApp_date() {
	return app_date;
}
public void setApp_date(Date app_date) {
	this.app_date = app_date;
}
public Date getApp_create_date() {
	return app_create_date;
}
public void setApp_create_date(Date app_create_date) {
	this.app_create_date = app_create_date;
}
public String getDoctor() {
	return doctor;
}
public void setDoctor(String doctor) {
	this.doctor = doctor;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public String getProblem() {
	return problem;
}
public void setProblem(String problem) {
	this.problem = problem;
}



public Date getAdmission_date() {
	return admission_date;
}
public void setAdmission_date(Date admission_date) {
	this.admission_date = admission_date;
}
public String getBed_no() {
	return bed_no;
}
public void setBed_no(String bed_no) {
	this.bed_no = bed_no;
}
public String getFloor() {
	return floor;
}
public void setFloor(String floor) {
	this.floor = floor;
}
@Override
public String toString() {
	return "Patient [id=" + id + ", name=" + name + ", email=" + email + ", age=" + age + ", gender=" + gender
			+ ", address=" + address + ", mobile=" + mobile + ", payment=" + payment + ", app_date=" + app_date
			+ ", app_create_date=" + app_create_date + ", doctor=" + doctor + ", status=" + status + ", problem="
			+ problem + ", admission_date=" + admission_date + ", bed_no=" + bed_no + ", floor=" + floor + "]";
}





}
