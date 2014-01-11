package pishpesh.gozapp.Entities;

import java.text.SimpleDateFormat;

import pishpesh.gozapp.Constants.PRODUCT_TYPE;
import pishpesh.gozapp.goZappDataSource;

public class Costumer {

	private long id;
	private String name;
	private String email;
	private String phone;
	private String notes;
	private int credit;

    //private Purchase currentPurchase;

    public Costumer(long id, String name, String email, String phone, String notes, int credit) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.notes = notes;
        this.credit = credit;
      //  this.currentPurchase = goZappDataSource.getCurrentPurchase(id);
    }

    public long getId() {
		return id;
	}
	public void setId(long id) {
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}

	@Override
	public String toString() {
        Purchase currentPurchase =  goZappDataSource.getCurrentPurchase(this);
        if(currentPurchase==null)
            return name+" (-)";
        if(currentPurchase.getPurchaseType().equals(PRODUCT_TYPE.ByAmount.name()))
		    return name+" ("+credit+")";
        else
            return name+" ("+new SimpleDateFormat("dd/MM/yy").format(currentPurchase.getFinishTime())+")";
	}
}
