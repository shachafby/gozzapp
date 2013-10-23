package pishpesh.gozapp;

/**
 * Created by DavarGozal on 10/18/13.
 */
public enum PRODUCT_TYPE{
    ByAmount (1), ByPeriod (2);

    private int value;

    private PRODUCT_TYPE(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
