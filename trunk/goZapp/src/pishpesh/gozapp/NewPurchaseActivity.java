package pishpesh.gozapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.support.v4.app.NavUtils;

public class NewPurchaseActivity extends Activity {


	goZappApplication appState = ((goZappApplication)this.getApplication());

	private RadioButton checkedRadioButton;
	private RadioGroup rGroup;
	private Button addBtn;
	private EditText comment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_new_purchase);

		// This will get the radiogroup
		rGroup = (RadioGroup)findViewById(R.id.radioGroup1);
		rGroup.check(R.id.fiveClassRadio);
		// This will get the radiobutton in the radiogroup that is checked

		addBtn = (Button)findViewById(R.id.addItemButton);

		comment = (EditText)findViewById(R.id.commentText);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_new_purchase, menu);
		return true;
	}

	public void onAdd(View v){

		int creditDelta=0;

		checkedRadioButton = (RadioButton)rGroup.findViewById(rGroup.getCheckedRadioButtonId());

		switch (checkedRadioButton.getId()){
		case R.id.fiveClassRadio:
			creditDelta=5;
			break;
		case R.id.tenClassRadio:
			creditDelta=10;
			break;
		case R.id.trailRadio:
			creditDelta=1;
			break;
		}
		
		
		//write purchase
		Date d = Calendar.getInstance().getTime();
		Purchase p = appState.datasource.createPurchase(appState.datasource.selectedCostumer.getId(), creditDelta, comment.getText().toString(), d);

		//add credit		
		appState.datasource.updateCredit(appState.datasource.selectedCostumer.getId(), appState.datasource.selectedCostumer.getCredit()+creditDelta);
 
        Intent i = new Intent(this,CostumersActivity.class);
    	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(i);
		
	}


}
