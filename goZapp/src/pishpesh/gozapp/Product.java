package pishpesh.gozapp;

/**
 * Created by DavarGozal on 10/10/13.
 */
public class Product {

    private long id;
    private String name;
    private String productType;
    private int amount;
    private int duration;

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

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        int val;

        val = amount>0 ? amount : duration;
        return name + " ("+productType+" "+val+")";
    }
}
