package pishpesh.gozapp;

import android.R.bool;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class ExistCostumerActivity extends Activity {

	goZappApplication appState = ((goZappApplication)this.getApplication());
	
	private EditText name;
	private TextView credit;
	private EditText phone;
	private EditText email;
	private EditText notes;
	private Button editBtn;
	
	
	private boolean canEditMode=false;

	//private Costumer selectedCostumer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_exist_costumer);
        
        name = (EditText)findViewById(R.id.cNameText);
        credit = (TextView)findViewById(R.id.creditText);
        phone = (EditText)findViewById(R.id.cPhoneText);
        email = (EditText)findViewById(R.id.cEmailText);
        notes = (EditText)findViewById(R.id.cNotesText);
        editBtn = (Button)findViewById(R.id.editBtn);
        
        
        loadCostumerData();
        
        disableInput();
        
    }



	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		loadCostumerData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadCostumerData();
	}

	private void loadCostumerData() {
//    	Bundle extras = getIntent().getExtras();
//        long selectedCostumerID = extras.getLong("selectedCostumerID");
//        
//        appState.selectedCostumer = appState.datasource.getCostumerByID(selectedCostumerID);
//        
        name.setText(appState.selectedCostumer.getName());
        credit.setText("Credit: "+String.valueOf(appState.selectedCostumer.getCredit())+" Classes left.");
        phone.setText(appState.selectedCostumer.getPhone());
        email.setText(appState.selectedCostumer.getEmail());
        notes.setText(appState.selectedCostumer.getNotes());
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.activity_exist_costumer, menu);
        return true;
    }
    
    public void onEditClick(View view) {
    	//save pressed
    	if(canEditMode){
    		canEditMode=false;
    		disableInput();
    		editBtn.setText("Edit");
    		
    		updateCostumer();
    	}
    	//edit pressed
    	else{
    		canEditMode=true;
    		enableInput();
    		editBtn.setText("Save");
    	}
	}
    public void onNewPurBtn(View view) {
	Intent i = new Intent(this,NewPurchaseActivity.class);
    	
    	startActivity(i);
	}
    
    public void onHistoryBtn(View view) {
    	Intent i = new Intent(this,HistoryActivity.class);
        	
        	startActivity(i);
    	}
    
	private void updateCostumer() {
		appState.selectedCostumer.setName(name.getText().toString());
		appState.selectedCostumer.setPhone(phone.getText().toString());
		appState.selectedCostumer.setEmail(email.getText().toString());
		appState.selectedCostumer.setNotes(notes.getText().toString());
				
		int i = appState.datasource.updateCustumer(appState.selectedCostumer);
		
	}



	private void enableInput() {
		name.setEnabled(true);
		phone.setEnabled(true);
        email.setEnabled(true);
        notes.setEnabled(true);
		
	}
    private void disableInput() {
    	name.setEnabled(false);
        phone.setEnabled(false);
        email.setEnabled(false);
        notes.setEnabled(false);
        
        }
    
}
