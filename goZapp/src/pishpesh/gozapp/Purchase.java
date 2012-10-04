package pishpesh.gozapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Purchase {
	

	private long id;
	private int costumerID;
	private String datetime;
	private int purchaseType;
	private String comment;
	
	
	private Date date;
	
	

	public int getPurchaseType() {
		return purchaseType;
	}
	public void setPurchaseType(int purchaseType) {
		this.purchaseType = purchaseType;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getCostumerID() {
		return costumerID;
	}
	public void setCostumerID(int costumerID) {
		this.costumerID = costumerID;
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

		return "On "+(new SimpleDateFormat("dd/MM/yy").format(date))+" bought "+purchaseType+" classes.";		
			
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	

}
