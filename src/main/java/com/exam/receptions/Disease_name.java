package com.exam.receptions;

public class Disease_name {

	int id;
	String name;
	
	public Disease_name() {
		super();
	}

	public Disease_name(int id) {
		super();
		this.id = id;
	}

	public Disease_name(int id, String name) {
		super();
		this.id = id;
		this.name = name;
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

	@Override
	public String toString() {
		return "Disease_name [id=" + id + ", name=" + name + "]";
	}
	
	
}
