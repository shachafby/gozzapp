package pishpesh.gozapp.Entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pishpesh.gozapp.Constants.PRODUCT_TYPE;

public class Purchase {

    private long id;
	private int costumerID;
	private Date creationDate;
	private PRODUCT_TYPE purchaseType;
    private Date startTime;
    private Date finishTime;
    private int volume;
    private String comment;

    public Date getFinishTime() { return finishTime; }
    public Date getStartTime() {
        return startTime;
    }
    public int getVolume() {
        return volume;
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

    public static Date stringDateToString(String dateString){
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
	@Override
	public String toString() {


        if(purchaseType==PRODUCT_TYPE.ByAmount)
		    return (new SimpleDateFormat("dd/MM/yy").format(creationDate))+" bought "+ getVolume()+" class(es)";
        return (new SimpleDateFormat("dd/MM/yy").format(creationDate))+" bought "+ getVolume()+" month(s) from "+new SimpleDateFormat("dd/MM/yy").format(startTime);
			
	}
	
	public Date getDate() {
		return creationDate;
	}
	public void setDate(Date date) {
		this.creationDate = date;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

    public Purchase(long id,
                    int costumerID,
                    String creationDate,
                    String purchaseType,
                    String startTime,
                    String finishTime,
                    int amount,
                    String comment) {
        this.id = id;
        this.costumerID = costumerID;
        this.creationDate = stringDateToString(creationDate);
        this.startTime = stringDateToString(startTime);
        this.finishTime = stringDateToString(finishTime);
        this.purchaseType = purchaseType.equals(PRODUCT_TYPE.ByAmount.name()) ? PRODUCT_TYPE.ByAmount: PRODUCT_TYPE.ByPeriod;
        this.volume = amount;
        this.comment = comment;


    }


    public boolean isRelevant() {

        Date now = Calendar.getInstance().getTime();

        if(purchaseType==PRODUCT_TYPE.ByAmount)
            return true;

        else if(startTime.before(now) && finishTime.after(now))
            return true;
        return false;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
