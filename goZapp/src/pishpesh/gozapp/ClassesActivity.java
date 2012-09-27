package pishpesh.gozapp;

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

public class ClassesActivity extends ListActivity {

	goZappApplication appState = ((goZappApplication)this.getApplication());
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_classes);
        
        appState.classes = appState.datasource.getAllClasses();

		//	     Use the SimpleCursorAdapter to show the
		//	     elements in a ListView
		ArrayAdapter<Class> adapter = new ArrayAdapter<Class>(this,android.R.layout.simple_list_item_1, appState.classes);
		setListAdapter(adapter);
    }

    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		@SuppressWarnings("unchecked")

		ArrayAdapter<Class> adapter = (ArrayAdapter<Class>) getListAdapter();

		adapter.notifyDataSetChanged();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_classes, menu);
        return true;
    }

    public void newClassView(View view){
    	Intent i = new Intent(this,NewClassActivity.class);
    	
    	startActivity(i);
    }
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		appState.selectedClass = (Class)l.getAdapter().getItem(position);
		
		Intent i = new Intent(this,ExistClassActivity.class);
		startActivity(i);


	}

    
}
