package pishpesh.gozapp;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class CostumersActivity extends ListActivity {

	goZappApplication appState = ((goZappApplication)this.getApplication());
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_costumers);

		appState.costumers = appState.datasource.getAllCostumers();

		//	     Use the SimpleCursorAdapter to show the
		//	     elements in a ListView
		ArrayAdapter<Costumer> adapter = new ArrayAdapter<Costumer>(this,android.R.layout.simple_list_item_1, appState.costumers);
		setListAdapter(adapter);

	} 

	@Override
	protected void onResume() {
		//appState.datasource.open();
		super.onResume();

		//appState.costumers = appState.datasource.getAllCostumers();

		@SuppressWarnings("unchecked")

		ArrayAdapter<Costumer> adapter = (ArrayAdapter<Costumer>) getListAdapter();

		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onPause() {
		//appState.datasource.close();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_costumers, menu);
		return true;
	}

	public void newCostumerView(View view){
		Intent i = new Intent(this,NewCostumerActivity.class);

		startActivity(i);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		Intent i = new Intent(this,ExistCostumerActivity.class);
		//appState.selectedCostumer = appState.costumers.get(position);
		appState.selectedCostumer = (Costumer)l.getAdapter().getItem(position);
		//i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);

	}

}