package come.exam.admission;

public class Room {
	
	int id;
	String bed_no;
	int rate;
	int room_no;
	String floor;
	String type;
	String status;
	
	
	
	public Room() {
		super();
	}


	public Room(int id) {
		super();
		this.id = id;
	}


	

	public Room(int id, String bed_no, int rate, int room_no, String floor, String type, String status) {
		super();
		this.id = id;
		this.bed_no = bed_no;
		this.rate = rate;
		this.room_no = room_no;
		this.floor = floor;
		this.type = type;
		this.status = status;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getBed_no() {
		return bed_no;
	}


	public void setBed_no(String bed_no) {
		this.bed_no = bed_no;
	}


	public int getRate() {
		return rate;
	}


	public void setRate(int rate) {
		this.rate = rate;
	}


	public int getRoom_no() {
		return room_no;
	}


	public void setRoom_no(int room_no) {
		this.room_no = room_no;
	}


	public String getFloor() {
		return floor;
	}


	public void setFloor(String floor) {
		this.floor = floor;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return "Room [id=" + id + ", bed_no=" + bed_no + ", rate=" + rate + ", room_no=" + room_no + ", floor=" + floor
				+ ", type=" + type + ", status=" + status + "]";
	}


	
	

}
