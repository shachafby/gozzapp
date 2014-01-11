package pishpesh.gozapp;

import android.content.ContentValues;
import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.String;

public class gozappDBopener extends SQLiteOpenHelper {

    public static final String TABLE_CLASSES = "classes";
    public static final String TABLE_LOCATIONS = "locations";
    public static final String TABLE_CosInClass = "Costumer_in_Class";
    public static final String TABLE_Purchases= "purchases";
    public static final String TABLE_Products= "products";
    public static final String TABLE_COSTUMERS = "costumers";

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_PHONE = "phone";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_CREDIT = "credit";
	public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_DATETIME = "datetime";
    public static final String COLUMN_CostumerID="CostumerID";
    public static final String COLUMN_ClassID="ClassID";
    public static final String COLUMN_PurchaseType="purchase_type";
    public static final String COLUMN_AMOUNT="amount";
    //public static final String COLUMN_DURATION="duration";
    public static final String COLUMN_VOLUME = "volume";
    public static final String COLUMN_START="start";
    public static final String COLUMN_FINISH="finish";
    public static final String COLUMN_CURRENT_METHOD ="current_method";
    public static final String COLUMN_PRODUCT_TYPE ="product_type";



	public static final String DATABASE_NAME = "goZapp.db";
	public static final int DATABASE_VERSION = 3;

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
                    + COLUMN_CURRENT_METHOD + " integer, "
					+ COLUMN_CREDIT + " integer default 0"
					+");";
	private static final String CLASSES_CREATE =
			"create table "
					+ TABLE_CLASSES
					+ "(" 
					+ COLUMN_ID	+ " integer primary key autoincrement, "						
					+ COLUMN_LOCATION + " integer REFERENCES "+TABLE_LOCATIONS+"("+COLUMN_ID+") ON DELETE RESTRICT, "
					+ COLUMN_DATETIME + " text not null "
					+");";

	private static final String LOCATIONS_CREATE =
			"create table "
					+ TABLE_LOCATIONS
					+ "(" 
					+ COLUMN_ID	+ " integer primary key autoincrement, "						
					+ COLUMN_NAME + " text"
					+");";

	private static final String CosInClass_CREATE =
			"create table "
					+ TABLE_CosInClass
					+ "(" 
					+ COLUMN_ID	+ " integer primary key autoincrement, "						
					+ COLUMN_CostumerID + " integer REFERENCES "+TABLE_COSTUMERS+"("+COLUMN_ID+") ON DELETE CASCADE, "
					+ COLUMN_ClassID + " integer REFERENCES "+TABLE_CLASSES+"("+COLUMN_ID+") ON DELETE CASCADE, "
                    + COLUMN_NOTES + " text "
					+");";

	private static final String Purchases_CREATE =
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

    private static final String Products_CREATE =
            "create table "
                    + TABLE_Products
                    + "("
                    + COLUMN_ID	+ " integer primary key autoincrement, "
                    + COLUMN_NAME + " text, "
                    + COLUMN_PRODUCT_TYPE + " text not null, "
                    + COLUMN_VOLUME + " integer "
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
        database.execSQL(Products_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

		Log.w(gozappDBopener.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion);

		if(oldVersion==1 &&newVersion==2){
			
			if (!database.isReadOnly()) {
				database.execSQL("PRAGMA foreign_keys = OFF;");

			}
			//add locations table
			database.execSQL(LOCATIONS_CREATE);

			//add previous locations
			ContentValues values = new ContentValues();
			values.put(gozappDBopener.COLUMN_NAME, "����-����");
			long insertId1 = database.insert(gozappDBopener.TABLE_LOCATIONS, null, values);
			assert(insertId1!=-1);

			values = new ContentValues();
			values.put(gozappDBopener.COLUMN_NAME, "���-��");
			long insertId2 = database.insert(gozappDBopener.TABLE_LOCATIONS, null, values);
			assert(insertId2!=-1);

			values = new ContentValues();
			values.put(gozappDBopener.COLUMN_NAME, "���-��");
			long insertId3 = database.insert(gozappDBopener.TABLE_LOCATIONS, null, values);
			assert(insertId3!=-1);		

			//<item>Givat-Haim</item>
			//<item>Gan-Shmuel</item>
			//<item>Yafo</item>

			//add new location coloumn to classes table

			//		Say you have a table and need to rename "colb" to "col_b":
			//			First you rename the old table:
			database.execSQL("ALTER TABLE "+TABLE_CLASSES+" RENAME TO tmp_table_name;");

			//			Then create the new table, based on the old table but with the updated column name:
			database.execSQL("CREATE TABLE "+TABLE_CLASSES +" ("
					+ COLUMN_ID	+ " integer primary key autoincrement, "						
					+ "old_loc" + " text, "
					+ COLUMN_DATETIME + " text"
					//		+ "FOREIGN KEY("+COLUMN_LOCATION+") REFERENCES "+TABLE_LOCATIONS+"("+COLUMN_ID+") "
					+");");

			//			Then copy the contents across from the original table.
			database.execSQL("INSERT INTO "+TABLE_CLASSES+"("+COLUMN_ID+", old_loc, "+COLUMN_DATETIME+")"+
					"SELECT "+COLUMN_ID+", "+COLUMN_LOCATION+", "+COLUMN_DATETIME+
					" FROM tmp_table_name;");

			//					Lastly, drop the old table.
			database.execSQL("DROP TABLE tmp_table_name;");

			database.execSQL("ALTER TABLE "+TABLE_CLASSES+" ADD COLUMN "+COLUMN_LOCATION+" integer;");

			//add new locations data to classes table
			database.execSQL("UPDATE "+TABLE_CLASSES+
					" SET "+COLUMN_LOCATION+"="+insertId1+
					" WHERE "+"old_loc='Givat-Haim';");

			database.execSQL("UPDATE "+TABLE_CLASSES+
					" SET "+COLUMN_LOCATION+"="+insertId2+
					" WHERE "+"old_loc='Gan-Shmuel';");

			database.execSQL("UPDATE "+TABLE_CLASSES+
					" SET "+COLUMN_LOCATION+"="+insertId3+
					" WHERE "+"old_loc='Yafo';");

			//drop old location data from classes table
			database.execSQL("CREATE TEMPORARY TABLE t1_backup("
					+ COLUMN_ID	+ " integer primary key autoincrement, "						
					+ COLUMN_LOCATION + " integer, "
					+ COLUMN_DATETIME + " text"
					+");");
			database.execSQL("INSERT INTO t1_backup SELECT " 
					+ COLUMN_ID	+ ", "						
					+ COLUMN_LOCATION + ", "
					+ COLUMN_DATETIME
					+" FROM "+ TABLE_CLASSES +";");
			database.execSQL("DROP TABLE "+TABLE_CLASSES+";");
			database.execSQL("CREATE TABLE "+TABLE_CLASSES+"("
					+ COLUMN_ID	+ " integer primary key autoincrement, "						
					+ COLUMN_LOCATION + " integer REFERENCES "+TABLE_LOCATIONS+"("+COLUMN_ID+"), "
					+ COLUMN_DATETIME + " text"
					//+ "FOREIGN KEY("+COLUMN_LOCATION+") REFERENCES "+TABLE_LOCATIONS+"("+COLUMN_ID+") "
					+");");
			database.execSQL("INSERT INTO "+TABLE_CLASSES+
					" SELECT "+ COLUMN_ID + ", "						
					+ COLUMN_LOCATION + ", "
					+ COLUMN_DATETIME+" FROM t1_backup;");
			database.execSQL("DROP TABLE t1_backup;");
			
			//**//
			//update TABLE_CosInClass colomns
			database.execSQL("ALTER TABLE "+TABLE_CosInClass+" RENAME TO tmp_table_name;");
			
			database.execSQL("CREATE TABLE "+TABLE_CosInClass+"("
					+ COLUMN_ID	+ " integer primary key autoincrement, "						
					+ COLUMN_CostumerID + " integer REFERENCES "+TABLE_COSTUMERS+"("+COLUMN_ID+") ON DELETE CASCADE, "
					+ COLUMN_ClassID + " integer REFERENCES "+TABLE_CLASSES+"("+COLUMN_ID+") ON DELETE CASCADE"
					+");");
			
			database.execSQL("INSERT INTO "+TABLE_CosInClass+
					" SELECT "
					+ COLUMN_ID + ", "						
					+ COLUMN_CostumerID + ", "
					+ COLUMN_ClassID
					+" FROM tmp_table_name;");
			
			database.execSQL("DROP TABLE tmp_table_name;");
			//**//
			
			
			
			//**//
			//update TABLE_Purchases colomns
			database.execSQL("ALTER TABLE "+TABLE_Purchases+" RENAME TO tmp_table_name;");
			
			database.execSQL("CREATE TABLE "+TABLE_Purchases+"("
					+ COLUMN_ID	+ " integer primary key autoincrement, "						
					+ COLUMN_CostumerID + " integer REFERENCES "+TABLE_COSTUMERS+"("+COLUMN_ID+") ON DELETE CASCADE, "
					+ COLUMN_DATETIME + " text not null, "
					+ COLUMN_PurchaseType + " integer, "						
					+ COLUMN_NOTES + " text "					
					+");");
			
			database.execSQL("INSERT INTO "+TABLE_Purchases+
					" SELECT "
					+ COLUMN_ID	+ ", "						
					+ COLUMN_CostumerID + ", "
					+ COLUMN_DATETIME + ", "
					+ COLUMN_PurchaseType + ", "						
					+ COLUMN_NOTES					
					+" FROM tmp_table_name;");
			
			database.execSQL("DROP TABLE tmp_table_name;");
			//**//
			
			
			
			if (!database.isReadOnly()) {
				database.execSQL("PRAGMA foreign_keys = ON;");

			}
		}
	}
} 