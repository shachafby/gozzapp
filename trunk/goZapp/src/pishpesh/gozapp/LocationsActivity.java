package pishpesh.gozapp;

import java.util.ArrayList;
import java.util.Comparator;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v4.app.NavUtils;
import android.text.InputType;

public class LocationsActivity extends ListActivity {

	goZappApplication appState = ((goZappApplication)this.getApplication());

	Activity activity = this;
	
	private String newLocationName="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE );

		setContentView(R.layout.activity_locations);

		appState.datasource.locations = appState.datasource.getAllLocations();

		ArrayAdapter<Location> adapter = new ArrayAdapter<Location>(this,android.R.layout.simple_list_item_1, new ArrayList<Location>(appState.datasource.locations.values()));
		
		adapter.sort(new Comparator<Location> (){
			@Override
			public int compare(Location p1, Location p2) {
				
				return p1.getName().compareTo(p2.getName());
				
			}});
		
		setListAdapter(adapter);
		
		
		final ListView listview = this.getListView();
		listview.setLongClickable(true);
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView parentView, View childView, int position, long id) {
				// this will provide the value
				final Location l = (Location)listview.getItemAtPosition(position);

				AlertDialog.Builder alert = new AlertDialog.Builder(activity);

				alert.setTitle("Are you sure you want to delete Location '"+l.getName()+"'?");

				alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						appState.datasource.deleteLocation(l);
						
						appState.datasource.locations = appState.datasource.getAllLocations();
						
						@SuppressWarnings("unchecked")
						ArrayAdapter<Location> adapter = (ArrayAdapter<Location>) getListAdapter();				

						adapter.notifyDataSetChanged();
						Intent i = new Intent(activity,LocationsActivity.class);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_locations, menu);
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		@SuppressWarnings("unchecked")

		ArrayAdapter<Location> adapter = (ArrayAdapter<Location>) getListAdapter();

		adapter.notifyDataSetChanged();
	}

	public void newLocationView(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Enter Location:");

		// Set up the input
		final EditText input = new EditText(this);
			
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		//input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		builder.setView(input);

		
		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 


			@Override
			public void onClick(DialogInterface dialog, int which) {
				newLocationName = input.getText().toString();

				appState.datasource.createLocation(newLocationName);

				appState.datasource.locations = appState.datasource.getAllLocations();
				
				@SuppressWarnings("unchecked")
				ArrayAdapter<Location> adapter = (ArrayAdapter<Location>) getListAdapter();				

				adapter.notifyDataSetChanged();
				
				Intent i = new Intent(activity,LocationsActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		builder.show();
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

	}

}