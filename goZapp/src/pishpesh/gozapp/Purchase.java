package pishpesh.gozapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Purchase {

    /*
       "create table "
					+ TABLE_Purchases
					+ "("
					+ COLUMN_ID	+ " integer primary key autoincrement, "
					+ COLUMN_CostumerID + " integer REFERENCES "+TABLE_COSTUMERS+"("+COLUMN_ID+") ON DELETE CASCADE, "
					+ COLUMN_DATETIME + " text not null, "
					+ COLUMN_PurchaseType + " text, "
                    + COLUMN_START + " text, "
                    + COLUMN_FINISH + " text, "
                    + COLUMN_AMOUNT + " integer, "
					+ COLUMN_NOTES + " text "
					+");";
    */

	private long id;
	private int costumerID;
	private String datetime;
	private PRODUCT_TYPE purchaseType;
    private String startTime;
    private String finishTime;
    private int amount;
    private String comment;

    private Date date;

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

	public PRODUCT_TYPE getPurchaseType() {
		return purchaseType;
	}
	public void setPurchaseType(String purchaseType) {
        if(purchaseType.equals(PRODUCT_TYPE.ByPeriod.name()))
            this.purchaseType = PRODUCT_TYPE.ByPeriod;
        else
            this.purchaseType = PRODUCT_TYPE.ByAmount;
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

        if(purchaseType.equals(PRODUCT_TYPE.ByAmount.name()))
		    return (new SimpleDateFormat("dd/MM/yy").format(date))+" bought "+getAmount()+" classes";
        return (new SimpleDateFormat("dd/MM/yy").format(date))+" bought "+getAmount()+" month(s)";
			
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
