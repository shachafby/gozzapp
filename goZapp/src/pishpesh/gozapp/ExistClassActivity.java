package pishpesh.gozapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.NavUtils;

public class ExistClassActivity extends ListActivity {

	goZappApplication appState = ((goZappApplication)this.getApplication());

	private boolean canEditMode=false;

	private EditText date;
	private Button editBtn;
	private EditText time;
	private ListView costumersList;
	private Spinner locationSpinner;
	private List<Costumer> costumersInclass;

	private TextView counter;


	private ArrayAdapter<Costumer> adapter;

	protected SparseBooleanArray checked;

	@SuppressWarnings("static-access")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_exist_class);

		date = (EditText)findViewById(R.id.exDateText);
		time = (EditText)findViewById(R.id.exTimeText);
		editBtn = (Button)findViewById(R.id.exClassEditBtn);

		costumersList = getListView();
		locationSpinner = (Spinner)findViewById(R.id.exLocationSpinner);
		counter = (TextView)findViewById(R.id.counterText);
        

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat tf = new SimpleDateFormat("HH:00");

		date.setText(appState.selectedClass.getDate());
		time.setText(appState.selectedClass.getTime());

		appState.costumers = appState.datasource.getAllCostumers();

		adapter = new ArrayAdapter<Costumer>(this,
				android.R.layout.simple_list_item_multiple_choice, appState.costumers);
		setListAdapter(adapter);

		costumersInclass = appState.datasource.getCostumerInClass(appState.selectedClass);

		checkCostumersInClassInList(costumersList, costumersInclass);

		costumersList.setFocusableInTouchMode(true);
		costumersList.requestFocus();

		ArrayAdapter aaadapter = ArrayAdapter.createFromResource(this, R.array.Locations, android.R.layout.simple_spinner_item); 
		aaadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		locationSpinner.setAdapter(aaadapter);

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
    	
    	for (int i = 0; i < checked.size(); i++) {
			if(checked.valueAt(i))
				cnt++;
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

		for(Costumer c : costumersInclass){
			appState.datasource.updateCredit(c.getId(), c.getCredit()+1);
			
		}
		
		appState.costumers = appState.datasource.getAllCostumers();

		SparseBooleanArray checked = costumersList.getCheckedItemPositions();
		costumersInclass.clear();
		
		List<Long> idList = new ArrayList<Long>(); 
		
		for(int i=0;i<checked.size();i++){
			if(checked.valueAt(i)==true)
				idList.add(((Costumer)costumersList.getAdapter().getItem(checked.keyAt(i))).getId());
		}
		
		for(Costumer c : appState.costumers){
			for(Long id : idList){
				if(c.getId()==id)
					costumersInclass.add(c);
			}
		}
		
		appState.selectedClass.setDatetime(date.getText().toString()+" "+time.getText().toString());
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
}
