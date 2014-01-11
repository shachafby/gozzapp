package pishpesh.gozapp;

import pishpesh.gozapp.Constants.PRODUCT_TYPE;
import pishpesh.gozapp.Product;

/**
 * Created by DavarGozal on 10/10/13.
 */
public class PeriodProduct extends Product {

    private int duration;

    public PeriodProduct(String name, long id, int duration) {
        super(name, id);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {

        return name + " ("+duration+" months)";
    }

    @Override
    public PRODUCT_TYPE getProductType() {
        return PRODUCT_TYPE.ByPeriod;
    }

    @Override
    public int getVolume() {
        return getDuration();
    }
}
