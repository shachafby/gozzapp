package pishpesh.gozapp;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.support.v4.app.NavUtils;

public class ClassesActivity extends ListActivity {

	goZappApplication appState = ((goZappApplication)this.getApplication());
	Activity activity = this;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_classes);
        
        appState.datasource.classes = appState.datasource.getAllClasses();

		//	     Use the SimpleCursorAdapter to show the
		//	     elements in a ListView
		ArrayAdapter<Class> adapter = new ArrayAdapter<Class>(this,android.R.layout.simple_list_item_1, appState.datasource.classes);
		adapter.sort(new Comparator<Class> (){
			@Override
			public int compare(Class p1, Class p2) {
				if(p1.getDateObj().before(p2.getDateObj()))
					return 1;
				else return -1;
			}});
		setListAdapter(adapter);
		
		
		final ListView listview = this.getListView();
		listview.setLongClickable(true);
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView parentView, View childView, int position, long id) {
				// this will provide the value
				final Class c = (Class)listview.getItemAtPosition(position);

				AlertDialog.Builder alert = new AlertDialog.Builder(activity);

				alert.setTitle("Are you sure you want to delete Class '"+c+"'?");

				alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						appState.datasource.deleteClass(c);
						
						Intent i = new Intent(activity, ClassesActivity.class);
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
		// TODO Auto-generated method stub
		super.onResume();
		
		@SuppressWarnings("unchecked")

		ArrayAdapter<Class> adapter = (ArrayAdapter<Class>) getListAdapter();

		adapter.notifyDataSetChanged();
	}

     public void newClassView(View view){
    	Intent i = new Intent(this,NewClassActivity.class);
    	
    	startActivity(i);
    }
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);
		
		appState.datasource.selectedClass = (Class)l.getAdapter().getItem(position);
		
		Intent i = new Intent(this,ExistClassActivity.class);
		startActivity(i);


	}

    
}
