package pishpesh.gozapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class NewClassActivity extends ListActivity {

	goZappApplication appState = ((goZappApplication)this.getApplication());
	
	private List<Costumer> custumers = new ArrayList<Costumer>(); 
	
    private EditText date;
	private Button createBtn;
	private EditText time;

	private TextView title;

	private ListView classList;

	private Spinner locationSpinner;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_new_class);
        
        date = (EditText)findViewById(R.id.newDateText);
        time = (EditText)findViewById(R.id.newTimeText);
        createBtn = (Button)findViewById(R.id.createClassBtn);
        title = (TextView)findViewById(R.id.newClassTitle);
        classList = getListView();
        locationSpinner = (Spinner)findViewById(R.id.locationSpinner);
        
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat tf = new SimpleDateFormat("HH:00");
 
        date.setText(df.format(c.getTime()));
        time.setText(tf.format(c.getTime()));
        
        appState.costumers = appState.datasource.getAllCostumers();

        ArrayAdapter<Costumer> adapter = new ArrayAdapter<Costumer>(this,android.R.layout.simple_list_item_multiple_choice, appState.costumers);
		setListAdapter(adapter);
		
		classList.setFocusableInTouchMode(true);
		classList.requestFocus();
		
		ArrayAdapter aaadapter = ArrayAdapter.createFromResource(this, R.array.Locations, android.R.layout.simple_spinner_item); 
		aaadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		locationSpinner.setAdapter(aaadapter);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_class, menu);
        return true;
    }
    
    public void onCreateClass(View v){
    	
    	custumers.clear();
    	
    	SparseBooleanArray checked = classList.getCheckedItemPositions();
    	
    	for(int i=0;i<checked.size();i++){
    		if(checked.valueAt(i)==true)
    			custumers.add(appState.costumers.get(i));
    	}
    	
    	Class newClass = appState.datasource.createClass(locationSpinner.getSelectedItem().toString(), date.getText().toString(),time.getText().toString(),custumers);
    	
    	Intent i = new Intent(this,ClassesActivity.class);
    	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(i);
    	
    }


}

