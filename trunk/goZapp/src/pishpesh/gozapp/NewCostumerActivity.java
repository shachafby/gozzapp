package pishpesh.gozapp;

import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;

public class NewCostumerActivity extends Activity {

	private goZappDataSource datasource;

	private TextView name;
	private TextView phone;
	private TextView email;
	private TextView notes;
	private Button CreateCostumerBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_new_costumer);

		datasource = new goZappDataSource(this);
		datasource.open();

		name = (TextView) findViewById(R.id.nameText);
		phone = (TextView) findViewById(R.id.phoneText);
		email = (TextView) findViewById(R.id.emailText);
		notes = (TextView) findViewById(R.id.notesText);
		CreateCostumerBtn = (Button) findViewById(R.id.CreateCostumerBtn);

		CreateCostumerBtn.setEnabled(false);
		
		name.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				if(name.getText().toString().equals(""))
					CreateCostumerBtn.setEnabled(false);
				else
					CreateCostumerBtn.setEnabled(true);
			}				
			
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void onCreateCostumer(View view) {
		Costumer costumer = null;

		// Save the new comment to the database
		costumer = datasource.createCostumer(
				name.getText().toString(),
				phone.getText().toString(),
				email.getText().toString(),
				notes.getText().toString());
		
		Intent i = new Intent(this,CostumersActivity.class);
    	
    	startActivity(i);


	}

	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_new_costumer, menu);
		return true;
	}



}
