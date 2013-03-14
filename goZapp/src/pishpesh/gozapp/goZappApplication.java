package pishpesh.gozapp;

import java.util.Comparator;
import java.util.List;

import android.app.Application;

public class goZappApplication extends Application{

	public static goZappDataSource datasource;
	public static List<Costumer> costumers;
	public static List<Class> classes;
	public static Costumer selectedCostumer;
	public static Class selectedClass;
	public Comparator<Purchase> dateDescComperator;
	public static List<Location> locations;
	
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
