package pishpesh.gozapp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class goZappDataSource {

  // Database fields
  private SQLiteDatabase database;
  private gozappDBopener dbHelper;
  private String[] costumerColumns = { 
		  gozappDBopener.COLUMN_ID,
		  gozappDBopener.COLUMN_NAME,
		  gozappDBopener.COLUMN_PHONE,
		  gozappDBopener.COLUMN_EMAIL,
		  gozappDBopener.COLUMN_NOTES,
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
    return costumer;
  }
} 