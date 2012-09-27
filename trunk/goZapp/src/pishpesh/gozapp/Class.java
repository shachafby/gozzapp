package pishpesh.gozapp;

public class Class {

	private long id;
	private String location;
	private String datetime;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	
	@Override
	public String toString() {
		return datetime+" @"+location;
	}
	
	public String getDate() {
		String [] s = datetime.split(" ");
		return s[0];
	}
	public String getTime() {
		String [] s = datetime.split(" ");
		return s[1];
	}
}
