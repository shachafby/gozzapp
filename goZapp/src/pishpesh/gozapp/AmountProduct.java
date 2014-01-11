package pishpesh.gozapp;

import pishpesh.gozapp.Constants.PRODUCT_TYPE;
import pishpesh.gozapp.Product;

/**
 * Created by DavarGozal on 10/10/13.
 */
public class AmountProduct extends Product {

     private int amount;

    public AmountProduct(String name, long id, int amount) {
        super(name, id);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {

        return name + " ("+amount+" classes)";
    }

    @Override
    public PRODUCT_TYPE getProductType() {
        return PRODUCT_TYPE.ByAmount;
    }

    @Override
    public int getVolume() {
        return getAmount();
    }
}
