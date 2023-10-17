package come.exam.admission;

import java.sql.Date;




public class Admission {

	
	int id;
	int patient_id;
	String description;
	Date admission_date;
	Date transaction_date;
	double deposit;
	double total_cost;
	String bed_no;
	String floor;
	String status;
	
	
	public Admission() {
		super();
	}


	public Admission(int id) {
		super();
		this.id = id;
	}


	public Admission(int id, int patient_id, String description, Date admission_date, Date transaction_date,
			double deposit, double total_cost, String bed_no, String floor, String status) {
		super();
		this.id = id;
		this.patient_id = patient_id;
		this.description = description;
		this.admission_date = admission_date;
		this.transaction_date = transaction_date;
		this.deposit = deposit;
		this.total_cost = total_cost;
		this.bed_no = bed_no;
		this.floor = floor;
		this.status = status;
	}


	


	


	public Admission(String description, Date transaction_date, double deposit) {
		super();
		this.description = description;
		this.transaction_date = transaction_date;
		this.deposit = deposit;
	}


	public Admission(Date admission_date, String bed_no, String floor) {
		super();
		this.admission_date = admission_date;
		this.bed_no = bed_no;
		this.floor = floor;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getPatient_id() {
		return patient_id;
	}


	public void setPatient_id(int patient_id) {
		this.patient_id = patient_id;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Date getAdmission_date() {
		return admission_date;
	}


	public void setAdmission_date(Date admission_date) {
		this.admission_date = admission_date;
	}


	public Date getTransaction_date() {
		return transaction_date;
	}


	public void setTransaction_date(Date transaction_date) {
		this.transaction_date = transaction_date;
	}


	public double getDeposit() {
		return deposit;
	}


	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}


	public double getTotal_cost() {
		return total_cost;
	}


	public void setTotal_cost(double total_cost) {
		this.total_cost = total_cost;
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


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return "Admission [id=" + id + ", patient_id=" + patient_id + ", description=" + description
				+ ", admission_date=" + admission_date + ", transaction_date=" + transaction_date + ", deposit="
				+ deposit + ", total_cost=" + total_cost + ", bed_no=" + bed_no + ", floor=" + floor + ", status="
				+ status + "]";
	}
	
	
	
	
}
