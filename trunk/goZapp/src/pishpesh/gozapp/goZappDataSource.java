package pishpesh.gozapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import pishpesh.gozapp.Constants.PRODUCT_TYPE;
import pishpesh.gozapp.Entities.*;
import pishpesh.gozapp.Entities.Class;

public class goZappDataSource {

    // Database fields
    private static SQLiteDatabase database;
    private static gozappDBopener dbHelper;

    public static List<Costumer> costumers;
    public static List<pishpesh.gozapp.Entities.Class> classes;
    public static Costumer selectedCostumer;
    public static Class selectedClass;
    public Product selectedProduct;
    public static List<Product> products;

    public static Map<Integer,Location> locations;

    static String[] costumerColumns = {
            gozappDBopener.TABLE_COSTUMERS+"."+gozappDBopener.COLUMN_ID,
            gozappDBopener.TABLE_COSTUMERS+"."+gozappDBopener.COLUMN_NAME,
            gozappDBopener.TABLE_COSTUMERS+"."+gozappDBopener.COLUMN_PHONE,
            gozappDBopener.TABLE_COSTUMERS+"."+gozappDBopener.COLUMN_EMAIL,
            gozappDBopener.TABLE_COSTUMERS+"."+gozappDBopener.COLUMN_NOTES,
            gozappDBopener.TABLE_COSTUMERS+"."+gozappDBopener.COLUMN_CREDIT
    };

    static String[] locationColumns = {
            gozappDBopener.TABLE_LOCATIONS+"."+gozappDBopener.COLUMN_ID,
            gozappDBopener.TABLE_LOCATIONS+"."+gozappDBopener.COLUMN_NAME
    };

    static String[] classColumns = {
            gozappDBopener.TABLE_CLASSES+"."+gozappDBopener.COLUMN_ID,
            gozappDBopener.TABLE_CLASSES+"."+gozappDBopener.COLUMN_LOCATION,
            gozappDBopener.TABLE_CLASSES+"."+gozappDBopener.COLUMN_DATETIME
    };

    static String[] purchaseColumns = {
            gozappDBopener.TABLE_Purchases+"."+gozappDBopener.COLUMN_ID,
            gozappDBopener.TABLE_Purchases+"."+gozappDBopener.COLUMN_CostumerID,
            gozappDBopener.TABLE_Purchases+"."+gozappDBopener.COLUMN_DATETIME,
            gozappDBopener.TABLE_Purchases+"."+gozappDBopener.COLUMN_PurchaseType,
            gozappDBopener.TABLE_Purchases+"."+gozappDBopener.COLUMN_START,
            gozappDBopener.TABLE_Purchases+"."+gozappDBopener.COLUMN_FINISH,
            gozappDBopener.TABLE_Purchases+"."+gozappDBopener.COLUMN_AMOUNT,
            gozappDBopener.TABLE_Purchases+"."+gozappDBopener.COLUMN_NOTES
    };

    static String[] productColumns = {
            gozappDBopener.TABLE_Products+"."+gozappDBopener.COLUMN_ID,
            gozappDBopener.TABLE_Products+"."+gozappDBopener.COLUMN_NAME,
            gozappDBopener.TABLE_Products+"."+gozappDBopener.COLUMN_PRODUCT_TYPE,
            gozappDBopener.TABLE_Products+"."+gozappDBopener.COLUMN_VOLUME
    };

    public goZappDataSource(Context context) {
        dbHelper = new gozappDBopener(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();

        // Enable foreign key constraints
        if (!database.isReadOnly()) {
            database.execSQL("PRAGMA foreign_keys = ON;");

        }
    }

    public void close() {
        dbHelper.close();
    }

    public void initAppObjects() {

        costumers = getAllCostumers();

        locations = getAllLocations();

        classes = getAllClasses();

    }

    public Costumer createCostumer(String name, String phone, String email, String notes) {
        ContentValues values = new ContentValues();

        values.put(gozappDBopener.COLUMN_NAME, name);
        values.put(gozappDBopener.COLUMN_PHONE, phone);
        values.put(gozappDBopener.COLUMN_EMAIL, email);
        values.put(gozappDBopener.COLUMN_NOTES, notes);

        long insertId = database.insert(gozappDBopener.TABLE_COSTUMERS, null, values);

        assert(insertId!=-1);

        Cursor cursor = database.query(gozappDBopener.TABLE_COSTUMERS, costumerColumns, gozappDBopener.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Costumer newCostumer = cursorToCostumer(cursor);
        cursor.close();
        return newCostumer;
    }

    public void deleteCostumer(Costumer Costumer) {
        long id = Costumer.getId();
        System.out.println("Costumer deleted with id: " + id);
        database.delete(gozappDBopener.TABLE_COSTUMERS, gozappDBopener.COLUMN_ID
                + " = " + id, null);
    }

    public List<Costumer> getAllCostumers() {
        List<Costumer> Costumers = new ArrayList<Costumer>();

        Cursor cursor = database.query(gozappDBopener.TABLE_COSTUMERS, costumerColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Costumer costumer = cursorToCostumer(cursor);
            Costumers.add(costumer);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return Costumers;
    }

    public Map<Integer, Location> getAllLocations(){
        Map<Integer, Location> Locations = new HashMap<Integer, Location>();

        Cursor cursor = database.query(gozappDBopener.TABLE_LOCATIONS, locationColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Location location = cursorToLocation(cursor);
            Locations.put((int)location.getId(), location);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return Locations;
    }

    private Location cursorToLocation(Cursor cursor) {
        Location location = new Location();
        location.setId(cursor.getLong(0));
        location.setName(cursor.getString(1));

        return location;
    }

    private static Costumer cursorToCostumer(Cursor cursor) {
        Costumer costumer = new Costumer(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(3),
                cursor.getString(2),
                cursor.getString(4),
                Integer.parseInt(cursor.getString(5)));
        return costumer;
    }

    public static Costumer getCostumerByID(long selectedCostumerID) {

        Costumer costumer=null;

        Cursor cursor = database.query(
                gozappDBopener.TABLE_COSTUMERS, costumerColumns,
                gozappDBopener.COLUMN_ID+"=?",new String[]{String.valueOf(selectedCostumerID)},
                null, null, null);

        cursor.moveToFirst();
        costumer = cursorToCostumer(cursor);

        // Make sure to close the cursor
        cursor.close();

        return costumer;
    }

    public int updateCustomer(Costumer c) {

        ContentValues values = new ContentValues();

        values.put(gozappDBopener.COLUMN_NAME, c.getName());
        values.put(gozappDBopener.COLUMN_PHONE, c.getPhone());
        values.put(gozappDBopener.COLUMN_EMAIL, c.getEmail());
        values.put(gozappDBopener.COLUMN_NOTES, c.getNotes());


        return database.update(
                gozappDBopener.TABLE_COSTUMERS,
                values,
                gozappDBopener.COLUMN_ID+"=?",
                new String[]{String.valueOf(c.getId())}
        );

    }

    public void updateCredit(long costumerID, int newCredit) {
        ContentValues values = new ContentValues();

        values.put(gozappDBopener.COLUMN_CREDIT, newCredit);

        database.update(
                gozappDBopener.TABLE_COSTUMERS,
                values,
                gozappDBopener.COLUMN_ID+"=?",
                new String[]{String.valueOf(costumerID)}
        );


    }

    public Class createClass(String location, String date, String time, List<Costumer> costumers) {
        ContentValues values = new ContentValues();

        values.put(gozappDBopener.COLUMN_LOCATION, getKeyFromLocationStr(location));
        values.put(gozappDBopener.COLUMN_DATETIME, date+" "+time);

        long insertId = database.insert(gozappDBopener.TABLE_CLASSES, null, values);

        assert(insertId!=-1);

        Cursor cursor = database.query(gozappDBopener.TABLE_CLASSES, classColumns, gozappDBopener.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Class newClass = cursorToClass(cursor);
        cursor.close();

        boolean b = false;

        for (Costumer costumer : costumers) {
            b = addCostumerToClass(newClass, costumer);
        }

        return newClass;
    }

    private int getKeyFromLocationStr(String location) {
        for (Map.Entry<Integer, Location> entry : locations.entrySet()) {
            if(entry.getValue().getName()==location)
                return entry.getKey();
        }
        return -1;

    }

    private boolean addCostumerToClass(Class newClass, Costumer costumer) {

        ContentValues values = new ContentValues();

        values.put(gozappDBopener.COLUMN_CostumerID, costumer.getId());
        values.put(gozappDBopener.COLUMN_ClassID, newClass.getId());

        long insertId = database.insert(gozappDBopener.TABLE_CosInClass, null, values);

        updateCostumerAccount(costumer);

        return (insertId!=-1);
    }

    private void updateCostumerAccount(Costumer costumer) {
        if(getCurrentPurchase(costumer).getPurchaseType()==PRODUCT_TYPE.ByAmount)
        {
            updateCredit(costumer.getId(), costumer.getCredit() - 1);
            costumer.setCredit(costumer.getCredit()-1);
        }
        else{
            //do nothing when costumer is on period purchase
        }
    }

    private Class cursorToClass(Cursor cursor) {
        Class c = new Class();
        c.setId(cursor.getLong(0));
        c.setLocation(locations.get(cursor.getInt(1)).getName());
        c.setDatetime(cursor.getString(2));

        return c;
    }

    public List<Class> getAllClasses() {
        List<Class> Classes = new ArrayList<Class>();

        Cursor cursor = database.query(gozappDBopener.TABLE_CLASSES, classColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Class c = cursorToClass(cursor);
            Classes.add(c);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return Classes;
    }

    public List<Costumer> getCostumerInClass(Class selectedClass) {
        List<Costumer> Costumers = new ArrayList<Costumer>();

        Cursor cursor = database.rawQuery(
                "SELECT "+ arrToRaw(costumerColumns)
                        +" FROM "+ gozappDBopener.TABLE_COSTUMERS+", "
                        + gozappDBopener.TABLE_CLASSES+", "
                        + gozappDBopener.TABLE_CosInClass
                        +" WHERE "
                        +gozappDBopener.TABLE_COSTUMERS+"."+gozappDBopener.COLUMN_ID
                        +"="
                        +gozappDBopener.TABLE_CosInClass+"."+gozappDBopener.COLUMN_CostumerID
                        +" AND "
                        +gozappDBopener.TABLE_CLASSES+"."+gozappDBopener.COLUMN_ID
                        +"="
                        +gozappDBopener.TABLE_CosInClass+"."+gozappDBopener.COLUMN_ClassID
                        +" AND "
                        +gozappDBopener.TABLE_CLASSES+"."+gozappDBopener.COLUMN_ID
                        +"="
                        +selectedClass.getId()
                        +";"
                , null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Costumer Costumer = cursorToCostumer(cursor);
            Costumers.add(Costumer);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return Costumers;
    }

    private String arrToRaw(String[] arr) {

        String res="";

        for (int i = 0; i < arr.length; i++) {
            String s = arr[i];
            res+=s;
            if(i<arr.length-1)
                res+=", ";
        }
        return res;
    }

    public int updateClass(Class c) {

        ContentValues values = new ContentValues();

        values.put(gozappDBopener.COLUMN_LOCATION, getKeyFromLocationStr(c.getLocation()));
        values.put(gozappDBopener.COLUMN_DATETIME, c.getDatetime());

        return database.update(
                gozappDBopener.TABLE_CLASSES,
                values,
                gozappDBopener.COLUMN_ID+"=?",
                new String[]{String.valueOf(c.getId())}
        );
    }

    public int updateCostumersInClass(Class selectedClass, List<Costumer> costumersInClass) {

        database.delete(gozappDBopener.TABLE_CosInClass, gozappDBopener.COLUMN_ClassID+"=?"	,
                new String[]{String.valueOf(selectedClass.getId())});

        for (Costumer costumer : costumersInClass)
            addCostumerToClass(selectedClass, costumer);

        return 0;
    }

    public Purchase createPurchase(long cosId, Date d, PRODUCT_TYPE product_type, Date start, Date finish, int amount, String comment) {
        ContentValues values = new ContentValues();

        values.put(gozappDBopener.COLUMN_CostumerID, cosId);
        values.put(gozappDBopener.COLUMN_DATETIME, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(d));
        values.put(gozappDBopener.COLUMN_PurchaseType, product_type.name());
        values.put(gozappDBopener.COLUMN_START, new SimpleDateFormat("yyyy-MM-dd 00:00").format(start));
        values.put(gozappDBopener.COLUMN_FINISH, new SimpleDateFormat("yyyy-MM-dd 00:00").format(finish));
        values.put(gozappDBopener.COLUMN_AMOUNT, amount);
        values.put(gozappDBopener.COLUMN_NOTES, comment);

        long insertId = database.insert(gozappDBopener.TABLE_Purchases, null, values);

        assert(insertId!=-1);

        Cursor cursor = database.query(gozappDBopener.TABLE_Purchases, purchaseColumns, gozappDBopener.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Purchase p = cursorToPurchase(cursor);
        cursor.close();
        return p;
    }

    private static Purchase cursorToPurchase(Cursor cursor) {
        Purchase p = new Purchase(
                cursor.getLong(0),//id
                cursor.getInt(1),//cusid
                cursor.getString(2),//prodtype
                cursor.getString(3),//date
                cursor.getString(4),//start
                cursor.getString(5),//finish
                cursor.getInt(6),//amount
                cursor.getString(7));//notes
        return p;
    }

    public static List<Purchase> getPurchasesByCostumer(Costumer selectedCostumer) {

        List<Purchase> purchases = new ArrayList<Purchase>();

        Cursor cursor = database.query(gozappDBopener.TABLE_Purchases , purchaseColumns ,
                gozappDBopener.COLUMN_CostumerID+"=?",new String[]{String.valueOf(selectedCostumer.getId())}, null, null, null);


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Purchase c = cursorToPurchase(cursor);
            purchases.add(c);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return purchases;
    }

    public List<Class> getClassesByCostumer(Costumer selectedCostumer) {
        List<Class> classes = new ArrayList<Class>();

        Cursor cursor = database.rawQuery(
                "SELECT "+ arrToRaw(classColumns)
                        +" FROM "
                        + gozappDBopener.TABLE_COSTUMERS+", "
                        + gozappDBopener.TABLE_CLASSES+", "
                        + gozappDBopener.TABLE_CosInClass
                        +" WHERE "
                        +gozappDBopener.TABLE_COSTUMERS+"."+gozappDBopener.COLUMN_ID
                        +"="
                        +gozappDBopener.TABLE_CosInClass+"."+gozappDBopener.COLUMN_CostumerID
                        +" AND "
                        +gozappDBopener.TABLE_CLASSES+"."+gozappDBopener.COLUMN_ID
                        +"="
                        +gozappDBopener.TABLE_CosInClass+"."+gozappDBopener.COLUMN_ClassID
                        +" AND "
                        +gozappDBopener.TABLE_COSTUMERS+"."+gozappDBopener.COLUMN_ID
                        +"="
                        +selectedCostumer.getId()
                        +";"
                , null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Class c = cursorToClass(cursor);
            classes.add(c);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return classes;
    }

    public Location createLocation(String newLocationName) {
        ContentValues values = new ContentValues();

        values.put(gozappDBopener.COLUMN_NAME, newLocationName);

        long insertId = database.insert(gozappDBopener.TABLE_LOCATIONS, null, values);

        assert(insertId!=-1);

        Cursor cursor = database.query(gozappDBopener.TABLE_LOCATIONS, locationColumns, gozappDBopener.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Location loc = cursorToLocation(cursor);
        cursor.close();
        return loc;

    }

    public void deleteLocation(Location l) {
        long id = l.getId();
        try{
            database.delete(gozappDBopener.TABLE_LOCATIONS, gozappDBopener.COLUMN_ID
                    + " = " + id, null);
        }
        catch(SQLiteConstraintException e){

        }

    }

    public void deleteClass(Class c) {
        long id = c.getId();

        database.delete(gozappDBopener.TABLE_CLASSES, gozappDBopener.COLUMN_ID
                + " = " + id, null);
    }

    public void deletePurchase(Purchase p) {
        database.delete(gozappDBopener.TABLE_Purchases, gozappDBopener.COLUMN_ID
                + " = " + p.getId(), null);

    }

    public void removeCustomerFromClass(Class cl, Costumer customer) {
        database.delete(gozappDBopener.TABLE_CosInClass, gozappDBopener.COLUMN_ClassID+"=? AND "+ gozappDBopener.COLUMN_CostumerID+"=?"	,
                new String[]{String.valueOf(cl.getId()), String.valueOf(customer.getId())});

    }

    public Product createProduct(String ProductName, String ProductType, int volume){

        ContentValues values = new ContentValues();
        values.put(gozappDBopener.COLUMN_NAME, ProductName);
        values.put(gozappDBopener.COLUMN_PRODUCT_TYPE, ProductType);
        values.put(gozappDBopener.COLUMN_VOLUME, volume);

        long insertId = database.insert(gozappDBopener.TABLE_Products, null, values);

        assert(insertId!=-1);

        Cursor cursor = database.query(gozappDBopener.TABLE_Products, productColumns, gozappDBopener.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Product product = cursorToProduct(cursor);
        cursor.close();
        return product;

    }

    private Product cursorToProduct(Cursor cursor) {

        Product p;// = new Product();

        String productTypeString=cursor.getString(2);

        if(productTypeString.equals(PRODUCT_TYPE.ByAmount.name()))
            p=new AmountProduct(cursor.getString(1), cursor.getLong(0), cursor.getInt(3));
        else
            p=new PeriodProduct(cursor.getString(1), cursor.getLong(0), cursor.getInt(3));

        return p;

    }

    public void deleteProduct(Product p) {
        long id = p.getId();

        database.delete(gozappDBopener.TABLE_Products, gozappDBopener.COLUMN_ID
                + " = " + id, null);
    }

    public List<Product> getAllProducts() {
        List<Product> Products = new ArrayList<Product>();

        Cursor cursor = database.query(gozappDBopener.TABLE_Products, productColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Product p = cursorToProduct(cursor);
            Products.add(p);
            cursor.moveToNext();
        }
        // Make sure to close the cursors
        cursor.close();
        return Products;
    }

    public static Purchase getCurrentPurchase(Costumer costumer) {

        List<Purchase> purchases = getPurchasesByCostumer(costumer);

        if(purchases==null)
            return null;

        Purchase returnPurchase=null;
        Date now = Calendar.getInstance().getTime();

        for(Purchase candidatePurchase : purchases){
            if(!candidatePurchase.isRelevant())
                continue;

            //first
            if(returnPurchase==null){
                returnPurchase=candidatePurchase;
                continue;
            }

            //period vs period - by finish time
            if(candidatePurchase.getPurchaseType()==PRODUCT_TYPE.ByPeriod
                    && returnPurchase.getPurchaseType()==PRODUCT_TYPE.ByPeriod){
                if(candidatePurchase.getFinishTime().after(returnPurchase.getFinishTime()))
                    returnPurchase=candidatePurchase;
                continue;
            }

            //period vs amount
            if(candidatePurchase.getPurchaseType()==PRODUCT_TYPE.ByPeriod
                    && returnPurchase.getPurchaseType()==PRODUCT_TYPE.ByAmount){
                returnPurchase=candidatePurchase;
                continue;
            }
            if(candidatePurchase.getPurchaseType()==PRODUCT_TYPE.ByAmount
                    && returnPurchase.getPurchaseType()==PRODUCT_TYPE.ByPeriod){
                continue;
            }

            //amount vs amount
            if(candidatePurchase.getPurchaseType()==PRODUCT_TYPE.ByAmount
                    && returnPurchase.getPurchaseType()==PRODUCT_TYPE.ByAmount){
                if(candidatePurchase.getCreationDate().after(returnPurchase.getCreationDate()))
                    returnPurchase=candidatePurchase;
                continue;
            }
        }

        return returnPurchase;
    }
}