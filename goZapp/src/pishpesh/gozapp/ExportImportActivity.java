package pishpesh.gozapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

public class ExportImportActivity extends Activity {
	private static final String MY_PACKAGE_NAME = "pishpesh.gozapp";
	private static final String BACKUP_FOLDER = "/BackupFolder/";


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//creating a new folder for the database to be backuped to
		File direct = new File(Environment.getExternalStorageDirectory() + BACKUP_FOLDER);

		if(!direct.exists())
		{
			if(direct.mkdir()) 
			{
				//directory is created;
			}

		}

		//exportDB();
		//importDB();
		

        setContentView(R.layout.activity_export_import);
        

	}

	//importing database
	private void importDB() {
		// TODO Auto-generated method stub

		try {
			File sd = Environment.getExternalStorageDirectory();
			File data  = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String  currentDBPath= "//data//" + MY_PACKAGE_NAME	+ "//databases//" + gozappDBopener.DATABASE_NAME;
				String backupDBPath  = BACKUP_FOLDER + gozappDBopener.DATABASE_NAME;
				File  backupDB= new File(data, currentDBPath);
				File currentDB  = new File(sd, backupDBPath);

				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
				Toast.makeText(getBaseContext(), backupDB.toString(),
						Toast.LENGTH_LONG).show();

			}
		} catch (Exception e) {

			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
			.show();

		}
	}
	//exporting database 
	private void exportDB() {
		// TODO Auto-generated method stub

		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String  currentDBPath= "//data//" + MY_PACKAGE_NAME
						+ "//databases//" + gozappDBopener.DATABASE_NAME;
				String backupDBPath  = BACKUP_FOLDER+ gozappDBopener.DATABASE_NAME;
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
				Toast.makeText(getBaseContext(), backupDB.toString(),
						Toast.LENGTH_LONG).show();

			}
		} catch (Exception e) {

			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
			.show();

		}
	}

	public void importDB(View v){
		importDB();
	}


	public void exportDB(View v){
		exportDB();
	}
}