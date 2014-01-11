package pishpesh.gozapp.Entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.text.format.DateFormat;

public class Class {

	private long id;
	private String location;
	private String datetime;
	private Date date;
	private SimpleDateFormat datePrintFormat;
	
	
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
		
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(datetime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {

		datePrintFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
		return datePrintFormat.format(date)+" @"+location;		
		
	}
	
	public String getDate() {
		String [] s = datetime.split(" ");
		return s[0];
	}
	public String getTime() {
		String [] s = datetime.split(" ");
		return s[1];
	}
	public Date getDateObj() {
		
		return date;
	}
}
