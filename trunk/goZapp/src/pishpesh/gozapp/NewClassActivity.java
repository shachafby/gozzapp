package pishpesh.gozapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.support.v4.app.NavUtils;

public class NewClassActivity extends ListActivity {

	goZappApplication appState = ((goZappApplication)this.getApplication());
	
	private List<Costumer> custumers = new ArrayList<Costumer>(); 
	
    private DatePicker date;
	private Button createBtn;
	private TimePicker time;

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
        
        date = (DatePicker)findViewById(R.id.datePicker1);
        time = (TimePicker)findViewById(R.id.timePicker1);
        time.setIs24HourView(true);
        
        createBtn = (Button)findViewById(R.id.createClassBtn);
        title = (TextView)findViewById(R.id.newClassTitle);
        classList = getListView();
        locationSpinner = (Spinner)findViewById(R.id.locationSpinner);
        counter = (TextView)findViewById(R.id.counterText);
        
        appState.costumers = appState.datasource.getAllCostumers();

        ArrayAdapter<Costumer> adapter = new ArrayAdapter<Costumer>(this,android.R.layout.simple_list_item_multiple_choice, appState.costumers);
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
		
		ArrayAdapter aaadapter = ArrayAdapter.createFromResource(this, R.array.Locations, android.R.layout.simple_spinner_item); 
		aaadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		locationSpinner.setAdapter(aaadapter);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_class, menu);
        return true;
    }
    
    public void onListClick(View v){
    	
    	checked = classList.getCheckedItemPositions();
    	
    	counter.setText("# "+checked.size());
    }
        
    public void onCreateClass(View v){
    	
    	custumers.clear();
    	
    	checked = classList.getCheckedItemPositions();
    	
    	for(int i=0;i<checked.size();i++){
    		if(checked.valueAt(i)==true)
    			custumers.add((Costumer)classList.getAdapter().getItem(checked.keyAt(i)));
    	}
    	
    	Date d = new Date(date.getYear()-1900, date.getMonth(), date.getDayOfMonth(), time.getCurrentHour(), time.getCurrentMinute());
    	
    	String dStr = new SimpleDateFormat("yyyy-MM-dd").format(d);
    	
    	String tStr = new SimpleDateFormat("HH:mm").format(d);
    	    	
    	Class newClass = appState.datasource.createClass(locationSpinner.getSelectedItem().toString(), dStr,tStr,custumers);
    	    	
    	Intent i = new Intent(this,ClassesActivity.class);
    	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(i);
    	
    }


}

