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
	public static final String COLUMN_TIME = "time";

	private static final String DATABASE_NAME = "goZapp.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = 
			"create table "
					+ TABLE_COSTUMERS 
					+ "(" 
					+ COLUMN_ID	+ " integer primary key autoincrement, " 
					+ COLUMN_NAME + " text not null, "
					+ COLUMN_PHONE + " integer not null, "
					+ COLUMN_EMAIL + " text, "
					+ COLUMN_CREDIT + " integer default 0, "
					+ COLUMN_NOTES + " text"
					+");"

					+"create table "
					+ TABLE_CLASSES
					+ "(" 
					+ COLUMN_ID	+ " integer primary key autoincrement, "						
					+ COLUMN_TIME + " text not null"
					+");";

	public gozappDBopener(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
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