package pishpesh.gozapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class ExistClassActivity extends ListActivity {

goZappApplication appState = ((goZappApplication)this.getApplication());
	
	//private List<Costumer> custumers = new ArrayList<Costumer>(); 
	
    private EditText date;
	private Button editBtn;
	private EditText time;

	//private TextView title;

	private ListView classList;

	private Spinner locationSpinner;

	private List<Costumer> costumersInclass;

	@SuppressWarnings("static-access")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_exist_class);
        
        date = (EditText)findViewById(R.id.exDateText);
        time = (EditText)findViewById(R.id.exTimeText);
        editBtn = (Button)findViewById(R.id.exClassEditBtn);
        
        classList = getListView();
        locationSpinner = (Spinner)findViewById(R.id.exLocationSpinner);
        
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat tf = new SimpleDateFormat("HH:00");
 
        date.setText(appState.selectedClass.getDate());
        time.setText(appState.selectedClass.getTime());
        
        appState.costumers = appState.datasource.getAllCostumers();
        //jhkjhkjh
        ArrayAdapter<Costumer> adapter = new ArrayAdapter<Costumer>(this,android.R.layout.simple_list_item_multiple_choice, appState.costumers);
		setListAdapter(adapter);
		
		classList.setFocusableInTouchMode(true);
		classList.requestFocus();
		
		costumersInclass = appState.datasource.getCostumerInClass(appState.selectedClass);
		
		
		
		ArrayAdapter aaadapter = ArrayAdapter.createFromResource(this, R.array.Locations, android.R.layout.simple_spinner_item); 
		aaadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		locationSpinner.setAdapter(aaadapter);
	
		disableInput();
	}

    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_exist_class, menu);
        return true;
    }

 
    private void enableInput() {
		locationSpinner.setEnabled(true);
		date.setEnabled(true);
        time.setEnabled(true);
        classList.setEnabled(true);
		
	}
    private void disableInput() {
    	locationSpinner.setEnabled(false);
    	date.setEnabled(false);
    	time.setEnabled(false);
    	classList.setEnabled(false);
        
        }
}
