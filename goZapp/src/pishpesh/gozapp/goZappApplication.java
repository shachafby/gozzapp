package pishpesh.gozapp;

import java.util.List;

import android.app.Application;

public class goZappApplication extends Application{

	public static goZappDataSource datasource;
	public static List<Costumer> costumers;
	public static List<Class> classes;
	public static Costumer selectedCostumer;
	public static Class selectedClass;
	
	@Override
	public void onCreate() {
		super.onCreate();
		datasource= new goZappDataSource(this);
	}
	
}
