package pishpesh.gozapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TimePicker;
import android.support.v4.app.NavUtils;

public class ExistClassActivity extends ListActivity {

	goZappApplication appState = ((goZappApplication)this.getApplication());

	private boolean canEditMode=false;

	private DatePicker date;
	private Button editBtn;
	private TimePicker time;
	private ListView costumersList;
	private Spinner locationSpinner;
	private List<Costumer> costumersInclass;

	private TextView counter;


	private ArrayAdapter<Costumer> adapter;

	protected SparseBooleanArray checked;

	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_exist_class);

		date = (DatePicker)findViewById(R.id.datePicker1);
		time = (TimePicker)findViewById(R.id.timePicker1);
		time.setIs24HourView(true);

		editBtn = (Button)findViewById(R.id.exClassEditBtn);

		costumersList = getListView();
		locationSpinner = (Spinner)findViewById(R.id.exLocationSpinner);
		counter = (TextView)findViewById(R.id.counterText);

		appState.costumers = appState.datasource.getAllCostumers();

		costumersInclass = appState.datasource.getCostumerInClass(appState.selectedClass);

		adapter = new ArrayAdapter<Costumer>(this,
				android.R.layout.simple_list_item_multiple_choice, appState.costumers);
		adapter.sort(new Comparator<Costumer> (){
			@Override
			public int compare(Costumer p1, Costumer p2) {
				boolean b1 = inList(costumersInclass,p1);
				boolean b2 = inList(costumersInclass,p1);

				if (b1 || b2)
					return -1;
				else
					return 1;

			}
		});

		setListAdapter(adapter);


		date.updateDate(appState.selectedClass.getDateObj().getYear()+1900, 
				appState.selectedClass.getDateObj().getMonth(), 
				appState.selectedClass.getDateObj().getDate());

		time.setCurrentHour(appState.selectedClass.getDateObj().getHours());
		time.setCurrentMinute(appState.selectedClass.getDateObj().getMinutes());

		checkCostumersInClassInList(costumersList, costumersInclass);

		costumersList.setFocusableInTouchMode(true);
		costumersList.requestFocus();

		ArrayAdapter<Location> locAdapter = new ArrayAdapter<Location>(this,android.R.layout.simple_list_item_1, appState.locations);

		locationSpinner.setAdapter(locAdapter);

		setLocSpiner(appState.selectedClass, locationSpinner);

		costumersList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				updateCounter();
			}
		});

		updateCounter();

		disableInput();
	}

	protected void updateCounter() {
		checked = costumersList.getCheckedItemPositions();

		int cnt=0;

		if(checked!=null){
			for (int i = 0; i < checked.size(); i++) {
				if(checked.valueAt(i))
					cnt++;
			}
		}
		counter.setText("#"+cnt);

	}

	private void setLocSpiner(Class selectedClass, Spinner locationSpinner) {
		for(int p=0;p<locationSpinner.getCount();p++){
			if(locationSpinner.getItemAtPosition(p).toString().equals(selectedClass.getLocation()))
				locationSpinner.setSelection(p);
		}
	}

	private void checkCostumersInClassInList(ListView costumersList, List<Costumer> costumersInclass) {
		ListAdapter adapter = costumersList.getAdapter();

		for (Costumer costumer : costumersInclass) {
			for (int i=0; i<adapter.getCount();i++) {

				if(costumer.getId()==((Costumer)adapter.getItem(i)).getId())
					costumersList.setItemChecked(i, true);

			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_exist_class, menu);
		return true;
	}

	public void onEditClassClick(View view) {
		//save pressed
		if(canEditMode){
			canEditMode=false;
			disableInput();
			editBtn.setText("Edit");

			updateClass();

			Intent i = new Intent(this,ClassesActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
		}
		//edit pressed
		else{
			canEditMode=true;
			enableInput();
			editBtn.setText("Save");
		}
	}

	private void updateClass() {

		if(costumersInclass!=null){
			for(Costumer c : costumersInclass){
				appState.datasource.updateCredit(c.getId(), c.getCredit()+1);

			}
		}

		appState.costumers = appState.datasource.getAllCostumers();

		SparseBooleanArray checked = costumersList.getCheckedItemPositions();
		costumersInclass.clear();

		List<Long> idList = new ArrayList<Long>(); 

		if(checked!=null){
			for(int i=0;i<checked.size();i++){
				if(checked.valueAt(i)==true)
					idList.add(((Costumer)costumersList.getAdapter().getItem(checked.keyAt(i))).getId());
			}
		}
		for(Costumer c : appState.costumers){
			for(Long id : idList){
				if(c.getId()==id)
					costumersInclass.add(c);
			}
		}

		String dStr = date.getYear()+"-"+(date.getMonth()+1)+"-"+date.getDayOfMonth();
		String tStr = time.getCurrentHour()+":"+time.getCurrentMinute();


		appState.selectedClass.setDatetime(dStr+" "+tStr);
		appState.selectedClass.setLocation(locationSpinner.getSelectedItem().toString());

		int i = appState.datasource.updateClass(appState.selectedClass);	

		i = appState.datasource.updateCostumersInClass(appState.selectedClass,costumersInclass);	

	}



	private void enableInput() {
		locationSpinner.setEnabled(true);
		date.setEnabled(true);
		time.setEnabled(true);
		costumersList.setEnabled(true);

	}
	private void disableInput() {
		locationSpinner.setEnabled(false);
		date.setEnabled(false);
		time.setEnabled(false);
		costumersList.setEnabled(false);

	}


	private boolean inList(List<Costumer> costumersInclass, Costumer p1) {

		for(Costumer c : costumersInclass){
			if(c.getId()==p1.getId())
				return true;
		}
		return false;
	}

}