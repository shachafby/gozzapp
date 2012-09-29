package pishpesh.gozapp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;

public class goZappDataSource {

	// Database fields
	private SQLiteDatabase database;
	private gozappDBopener dbHelper;

	private String[] costumerColumns = { 
			gozappDBopener.TABLE_COSTUMERS+"."+gozappDBopener.COLUMN_ID,
			gozappDBopener.TABLE_COSTUMERS+"."+gozappDBopener.COLUMN_NAME,
			gozappDBopener.TABLE_COSTUMERS+"."+gozappDBopener.COLUMN_PHONE,
			gozappDBopener.TABLE_COSTUMERS+"."+gozappDBopener.COLUMN_EMAIL,
			gozappDBopener.TABLE_COSTUMERS+"."+gozappDBopener.COLUMN_NOTES,
			gozappDBopener.TABLE_COSTUMERS+"."+gozappDBopener.COLUMN_CREDIT
	};

	private String[] classColumns = { 
			gozappDBopener.TABLE_CLASSES+"."+gozappDBopener.COLUMN_ID,
			gozappDBopener.TABLE_CLASSES+"."+gozappDBopener.COLUMN_LOCATION,
			gozappDBopener.TABLE_CLASSES+"."+gozappDBopener.COLUMN_DATETIME,
	};

	public goZappDataSource(Context context) {
		dbHelper = new gozappDBopener(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
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
			Costumer Costumer = cursorToCostumer(cursor);
			Costumers.add(Costumer);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return Costumers;
	}

	private Costumer cursorToCostumer(Cursor cursor) {
		Costumer costumer = new Costumer();
		costumer.setId(cursor.getLong(0));
		costumer.setName(cursor.getString(1));
		costumer.setPhone(cursor.getString(2));
		costumer.setEmail(cursor.getString(3));
		costumer.setNotes(cursor.getString(4));
		costumer.setCredit(Integer.parseInt(cursor.getString(5)));
		return costumer;
	}

	public Costumer getCostumerByID(long selectedCostumerID) {

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

	public int updateCustumer(Costumer c) {

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

	public Class createClass(String location, String date, String time, List<Costumer> custumers) {
		ContentValues values = new ContentValues();

		values.put(gozappDBopener.COLUMN_LOCATION, location);
		values.put(gozappDBopener.COLUMN_DATETIME, date+" "+time);

		long insertId = database.insert(gozappDBopener.TABLE_CLASSES, null, values);

		assert(insertId!=-1);

		Cursor cursor = database.query(gozappDBopener.TABLE_CLASSES, classColumns, gozappDBopener.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Class newClass = cursorToClass(cursor);
		cursor.close();

		boolean b = false;

		for (Costumer costumer : custumers) {
			b = addCoustumerToClass(newClass,costumer);
		}

		return newClass;
	}

	private boolean addCoustumerToClass(Class newClass, Costumer costumer) {

		ContentValues values = new ContentValues();

		values.put(gozappDBopener.COLUMN_CostumerID, costumer.getId());
		values.put(gozappDBopener.COLUMN_ClassID, newClass.getId());

		long insertId = database.insert(gozappDBopener.TABLE_CosInClass, null, values);

		updateCredit(costumer.getId(), costumer.getCredit() - 1);
		costumer.setCredit(costumer.getCredit()-1);
		
		return (insertId!=-1);
	}

	private Class cursorToClass(Cursor cursor) {
		Class c = new Class();
		c.setId(cursor.getLong(0));
		c.setLocation(cursor.getString(1));
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

		values.put(gozappDBopener.COLUMN_LOCATION, c.getLocation());
		values.put(gozappDBopener.COLUMN_DATETIME, c.getDatetime());

		return database.update(
				gozappDBopener.TABLE_CLASSES, 
				values, 
				gozappDBopener.COLUMN_ID+"=?",
				new String[]{String.valueOf(c.getId())}
				);
	}


	public int updateCostumersInClass(Class selectedClass, List<Costumer> costumersInclass) {

		database.delete(gozappDBopener.TABLE_CosInClass, gozappDBopener.COLUMN_ClassID+"=?"	, 
				new String[]{String.valueOf(selectedClass.getId())});

		for (Costumer costumer : costumersInclass)
			addCoustumerToClass(selectedClass,costumer);

		return 0;
	}


} 