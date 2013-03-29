package pishpesh.gozapp;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import android.app.Application;

public class goZappApplication extends Application{

	public static goZappDataSource datasource;
	
	public Comparator<Purchase> dateDescComperator;
	
	@Override
	public void onCreate() {
		super.onCreate();
		datasource= new goZappDataSource(this);
		dateDescComperator = new Comparator<Purchase> (){

			@Override
			public int compare(Purchase p1, Purchase p2) {
				if(p1.getDate().before(p2.getDate()))
					return 1;
				else return -1;
			}};
	}
	
}
