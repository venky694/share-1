package com.exam.doctor;

public class test_name {
int id;
String catagory;
String test_name;
String test_rate;


public test_name() {
	super();
}


public test_name(int id) {
	super();
	this.id = id;
}


public test_name(int id, String catagory, String test_name, String test_rate) {
	super();
	this.id = id;
	this.catagory = catagory;
	this.test_name = test_name;
	this.test_rate = test_rate;
}


public int getId() {
	return id;
}


public void setId(int id) {
	this.id = id;
}


public String getCatagory() {
	return catagory;
}


public void setCatagory(String catagory) {
	this.catagory = catagory;
}


public String getTest_name() {
	return test_name;
}


public void setTest_name(String test_name) {
	this.test_name = test_name;
}


public String getTest_rate() {
	return test_rate;
}


public void setTest_rate(String test_rate) {
	this.test_rate = test_rate;
}


@Override
public String toString() {
	return "test_name [id=" + id + ", catagory=" + catagory + ", test_name=" + test_name + ", test_rate=" + test_rate
			+ "]";
}




}
