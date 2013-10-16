package pishpesh.gozapp;

import java.util.Comparator;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

public class CostumersActivity extends ListActivity {

	goZappApplication appState = ((goZappApplication)this.getApplication());

	Activity activity = this;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_costumers);

		appState.datasource.costumers = appState.datasource.getAllCostumers();

		//	     Use the SimpleCursorAdapter to show the
		//	     elements in a ListView
		ArrayAdapter<Costumer> adapter = new ArrayAdapter<Costumer>(this,android.R.layout.simple_list_item_1, appState.datasource.costumers);
		adapter.sort(new Comparator<Costumer> (){
			@Override
			public int compare(Costumer p1, Costumer p2) {
				return p1.getName().compareTo(p2.getName());					
			}});
		
		setListAdapter(adapter);
		
		final ListView listview = this.getListView();
		listview.setLongClickable(true);
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView parentView, View childView, int position, long id) {
				// this will provide the value
				final Costumer cos = (Costumer)listview.getItemAtPosition(position);

				AlertDialog.Builder alert = new AlertDialog.Builder(activity);

				alert.setTitle("Are you want to delete Customer '"+cos.getName()+"'?");

				alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						appState.datasource.deleteCostumer(cos);
						
						Intent i = new Intent(activity, CostumersActivity.class);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
					}
				});

				alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

				alert.show();
				// TODO Auto-generated method stub
				return false;
			}
		});


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
		appState.datasource.selectedCostumer = (Costumer)l.getAdapter().getItem(position);
		//i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);

	}

}