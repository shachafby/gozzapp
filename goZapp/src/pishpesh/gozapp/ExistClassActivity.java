package pishpesh.gozapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.app.TimePickerDialog;
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

public class ExistClassActivity extends ListActivity 
implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

	goZappApplication appState = ((goZappApplication)this.getApplication());

	private boolean canEditMode=false;


	private Date ddate = new Date();
	private Button dateBtn;
	private Button timeBtn;

	//private DatePicker date;
	private Button editBtn;
	//private TimePicker time;
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


		dateBtn = (Button)findViewById(R.id.dateBtn1);
		timeBtn = (Button)findViewById(R.id.timeBtn1);

		//		date = (DatePicker)findViewById(R.id.datePicker1);
		//		time = (TimePicker)findViewById(R.id.timePicker1);
		//		time.setIs24HourView(true);

		editBtn = (Button)findViewById(R.id.exClassEditBtn);

		costumersList = getListView();
		locationSpinner = (Spinner)findViewById(R.id.exLocationSpinner);
		counter = (TextView)findViewById(R.id.counterText);

		appState.datasource.costumers = appState.datasource.getAllCostumers();

		costumersInclass = appState.datasource.getCostumerInClass(appState.datasource.selectedClass);

		adapter = new ArrayAdapter<Costumer>(this,
				android.R.layout.simple_list_item_multiple_choice, appState.datasource.costumers);
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


		//		date.updateDate(appState.datasource.selectedClass.getDateObj().getYear()+1900, 
		//				appState.datasource.selectedClass.getDateObj().getMonth(), 
		//				appState.datasource.selectedClass.getDateObj().getDate());
		//
		//		time.setCurrentHour(appState.datasource.selectedClass.getDateObj().getHours());
		//		time.setCurrentMinute(appState.datasource.selectedClass.getDateObj().getMinutes());

		dateBtn.setText(appState.datasource.selectedClass.getDateObj().getDate()+"."+
				(appState.datasource.selectedClass.getDateObj().getMonth()+1)+"."+
				(appState.datasource.selectedClass.getDateObj().getYear()+1900));

		timeBtn.setText(appState.datasource.selectedClass.getDateObj().getHours()+":"+
				appState.datasource.selectedClass.getDateObj().getMinutes()
				);

		checkCostumersInClassInList(costumersList, costumersInclass);

		costumersList.setFocusableInTouchMode(true);
		costumersList.requestFocus();

		ArrayAdapter<Location> locAdapter = new ArrayAdapter<Location>(this,android.R.layout.simple_list_item_1, new ArrayList<Location>(appState.datasource.locations.values()));

		locationSpinner.setAdapter(locAdapter);

		setLocSpiner(appState.datasource.selectedClass, locationSpinner);

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

		appState.datasource.costumers = appState.datasource.getAllCostumers();

		SparseBooleanArray checked = costumersList.getCheckedItemPositions();
		costumersInclass.clear();

		List<Long> idList = new ArrayList<Long>(); 

		if(checked!=null){
			for(int i=0;i<checked.size();i++){
				if(checked.valueAt(i)==true)
					idList.add(((Costumer)costumersList.getAdapter().getItem(checked.keyAt(i))).getId());
			}
		}
		for(Costumer c : appState.datasource.costumers){
			for(Long id : idList){
				if(c.getId()==id)
					costumersInclass.add(c);
			}
		}

		String dStr = new SimpleDateFormat("yyyy-MM-dd").format(ddate);

		String tStr = new SimpleDateFormat("HH:mm").format(ddate);


		appState.datasource.selectedClass.setDatetime(dStr+" "+tStr);
		appState.datasource.selectedClass.setLocation(locationSpinner.getSelectedItem().toString());

		int i = appState.datasource.updateClass(appState.datasource.selectedClass);	

		i = appState.datasource.updateCostumersInClass(appState.datasource.selectedClass,costumersInclass);	

	}

	private void enableInput() {
		locationSpinner.setEnabled(true);
		dateBtn.setEnabled(true);
		timeBtn.setEnabled(true);
		costumersList.setEnabled(true);

	}

	private void disableInput() {
		locationSpinner.setEnabled(false);
		dateBtn.setEnabled(false);
		timeBtn.setEnabled(false);
		costumersList.setEnabled(false);

	}

	private boolean inList(List<Costumer> costumersInclass, Costumer p1) {

		for(Costumer c : costumersInclass){
			if(c.getId()==p1.getId())
				return true;
		}
		return false;
	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}

	public void showTimePickerDialog(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getFragmentManager(), "timePicker");
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {

		ddate.setMonth(month);
		ddate.setYear(year);
		ddate.setDate(day);

		dateBtn.setText(day+"."+(month+1)+"."+year);

	}

	@Override
	public void onTimeSet(TimePicker view, int hour, int min) {

		ddate.setHours(hour);
		ddate.setMinutes(min);

		timeBtn.setText(hour+":"+min);
	}

}