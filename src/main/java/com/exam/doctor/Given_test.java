package com.exam.doctor;

public class Given_test  {

	private int id;
	private String test_name;
	private int patient_id;
	private String status;
	
	
	public Given_test() {
		super();
	}


	public Given_test(int id) {
		super();
		this.id = id;
	}


	


	public Given_test(int id, String test_name,  int patient_id, String status) {
		super();
		this.id = id;
		this.test_name = test_name;
		this.patient_id = patient_id;
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





	public int getPatient_id() {
		return patient_id;
	}


	public void setPatient_id(int patient_id) {
		this.patient_id = patient_id;
	}


	public String getStatus() {
		return status;
	}


	public void setStatusi(String statuse) {
		this.status = statuse;
	}


	@Override
	public String toString() {
		return "Given_test [id=" + id + ", test_name=" + test_name + ", patient_id="
				+ patient_id + ", status=" + status + "]";
	}
	
	
	
}
