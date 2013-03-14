package pishpesh.gozapp;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class gozappDBopener extends SQLiteOpenHelper {

	public static final String TABLE_COSTUMERS = "costumers";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_PHONE = "phone";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_CREDIT = "credit";
	public static final String COLUMN_NOTES = "notes";

	public static final String TABLE_CLASSES = "classes";
	public static final String TABLE_LOCATIONS = "locations";
	
	public static final String COLUMN_LOCATION = "location";
	public static final String COLUMN_DATETIME = "datetime";


	public static final String TABLE_CosInClass = "Costumer_in_Class";

	public static String COLUMN_CostumerID="CostumerID";
	public static String COLUMN_ClassID="ClassID";
	
	public static String TABLE_Purchases= "purchases";
	public static String COLUMN_PurchaseType="purchase_type";


	public static final String DATABASE_NAME = "goZapp.db";
	public static final int DATABASE_VERSION = 2;

	// Database creation sql statement
	private static final String COSTUMERS_CREATE = 
			"create table "
					+ TABLE_COSTUMERS 
					+ "(" 
					+ COLUMN_ID	+ " integer primary key autoincrement, " 
					+ COLUMN_NAME + " text not null, "
					+ COLUMN_PHONE + " integer not null, "
					+ COLUMN_EMAIL + " text, "					
					+ COLUMN_NOTES + " text, "
					+ COLUMN_CREDIT + " integer default 0"
					+");";
	private static final String CLASSES_CREATE =
			"create table "
					+ TABLE_CLASSES
					+ "(" 
					+ COLUMN_ID	+ " integer primary key autoincrement, "						
					+ COLUMN_LOCATION + " integer not null, "
					+ COLUMN_DATETIME + " text not null, "
					+ "FOREIGN KEY("+COLUMN_LOCATION+") REFERENCES "+TABLE_LOCATIONS+"("+COLUMN_ID+")"
					+");";

	private static final String LOCATIONS_CREATE =
			"create table "
					+ TABLE_LOCATIONS
					+ "(" 
					+ COLUMN_ID	+ " integer primary key autoincrement, "						
					+ COLUMN_NAME + " text not null"
					+");";
	
	private static final String CosInClass_CREATE =
			"create table "
					+ TABLE_CosInClass
					+ "(" 
					+ COLUMN_ID	+ " integer primary key autoincrement, "						
					+ COLUMN_CostumerID + " integer not null, "
					+ COLUMN_ClassID + " integer not null, "
					+ "FOREIGN KEY("+COLUMN_CostumerID+") REFERENCES "+TABLE_COSTUMERS+"("+COLUMN_ID+"), "
					+ "FOREIGN KEY("+COLUMN_ClassID+") REFERENCES "+TABLE_CLASSES+"("+COLUMN_ID+")"
					+");";

	private static final String Purchases_CREATE =
			"create table "
					+ TABLE_Purchases
					+ "(" 
					+ COLUMN_ID	+ " integer primary key autoincrement, "						
					+ COLUMN_CostumerID + " integer not null, "
					+ COLUMN_DATETIME + " text not null, "
					+ COLUMN_PurchaseType + " integer, "						
					+ COLUMN_NOTES + " text, "					
					+ "FOREIGN KEY("+COLUMN_CostumerID+") REFERENCES "+TABLE_COSTUMERS+"("+COLUMN_ID+") "
					+");";
	
	public gozappDBopener(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//context.deleteDatabase(DATABASE_NAME);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {


		database.execSQL(LOCATIONS_CREATE);
		database.execSQL(COSTUMERS_CREATE);
		database.execSQL(CLASSES_CREATE);
		database.execSQL(CosInClass_CREATE);
		database.execSQL(Purchases_CREATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(gozappDBopener.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COSTUMERS);
		onCreate(db);
	}

} 