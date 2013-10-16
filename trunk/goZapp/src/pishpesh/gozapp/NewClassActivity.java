package pishpesh.gozapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

public class NewClassActivity extends ListActivity 
implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{

	goZappApplication appState = ((goZappApplication)this.getApplication());

	private List<Costumer> custumers = new ArrayList<Costumer>(); 

	private Date ddate = new Date();
	private Button dateBtn;
	private Button timeBtn;

	private Button createBtn;
	
	private TextView title;
	private TextView counter;

	private ListView classList;

	private Spinner locationSpinner;

	private SparseBooleanArray checked;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_new_class);

		dateBtn = (Button)findViewById(R.id.dateBtn);
		timeBtn = (Button)findViewById(R.id.timeBtn);

		//date = (DatePicker)findViewById(R.id.datePicker1);
		//time = (TimePicker)findViewById(R.id.timePicker1);
		//time.setIs24HourView(true);

		createBtn = (Button)findViewById(R.id.createClassBtn);
		title = (TextView)findViewById(R.id.newClassTitle);
		classList = getListView();
		locationSpinner = (Spinner)findViewById(R.id.locationSpinner);
		counter = (TextView)findViewById(R.id.counterText);

		appState.datasource.costumers = appState.datasource.getAllCostumers();

		ArrayAdapter<Costumer> adapter = new ArrayAdapter<Costumer>(this,android.R.layout.simple_list_item_multiple_choice, appState.datasource.costumers);
		setListAdapter(adapter);

		classList.setFocusableInTouchMode(true);
		classList.requestFocus();

		classList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				checked = classList.getCheckedItemPositions();

				int cnt=0;

				for (int i = 0; i < checked.size(); i++) {
					if(checked.valueAt(i))
						cnt++;
				}

				counter.setText("# "+cnt);

			}
		});

		ArrayAdapter<Location> locAdapter = new ArrayAdapter<Location>(this,android.R.layout.simple_list_item_1, new ArrayList<Location>(appState.datasource.locations.values()));

		locationSpinner.setAdapter(locAdapter);
	}

	public void onListClick(View v){

		checked = classList.getCheckedItemPositions();

		counter.setText("# "+checked.size());
	}

	public void onCreateClass(View v){

		custumers.clear();

		checked = classList.getCheckedItemPositions();
		if(checked!=null){    	
			for(int i=0;i<checked.size();i++){
				if(checked.valueAt(i)==true)
					custumers.add((Costumer)classList.getAdapter().getItem(checked.keyAt(i)));
			}
		}

		String dStr = new SimpleDateFormat("yyyy-MM-dd").format(ddate);

		String tStr = new SimpleDateFormat("HH:mm").format(ddate);

		Class newClass = appState.datasource.createClass(locationSpinner.getSelectedItem().toString(), dStr,tStr,custumers);

		Intent i = new Intent(this,ClassesActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);

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

