package com.exam.doctor;

public class Prescription {

	
	int id;
	String medicine_name;
	String course_duration;
	String taken_time;
	int patient_id;
	
	
	public Prescription() {
		super();
	}


	
	public Prescription(int id) {
		super();
		this.id = id;
	}



	public Prescription(int id, String medicine_name, String course_duration, String taken_time, int patient_id) {
		super();
		this.id = id;
		this.medicine_name = medicine_name;
		this.course_duration = course_duration;
		this.taken_time = taken_time;
		this.patient_id = patient_id;
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



	public String getCourse_duration() {
		return course_duration;
	}



	public void setCourse_duration(String course_duration) {
		this.course_duration = course_duration;
	}



	public String getTaken_time() {
		return taken_time;
	}



	public void setTaken_time(String taken_time) {
		this.taken_time = taken_time;
	}



	public int getPatient_id() {
		return patient_id;
	}



	public void setPatient_id(int patient_id) {
		this.patient_id = patient_id;
	}



	@Override
	public String toString() {
		return "Prescription [id=" + id + ", medicine_name=" + medicine_name + ", course_duration=" + course_duration
				+ ", taken_time=" + taken_time + ", patient_id=" + patient_id + "]";
	}



	
	
	
}
