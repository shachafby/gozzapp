package pishpesh.gozapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {


	goZappApplication appState = ((goZappApplication)this.getApplication());
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE );
        
        setContentView(R.layout.activity_main);
        

		appState.datasource.open();
		
		appState.datasource.initAppObjects();

    }



	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void costumersView(View view){
    	Intent i = new Intent(this,CostumersActivity.class);
    	
    	startActivity(i);
    }
    
    public void classesView(View view){
    	Intent i = new Intent(this,ClassesActivity.class);
    	
    	startActivity(i);
    }
    public void ExportImportView(View view){
    	Intent i = new Intent(this,ExportImportActivity.class);
    	
    	startActivity(i);
    }
    public void locationsView(View view){
    	Intent i = new Intent(this,LocationsActivity.class);
    	
    	startActivity(i);
    }
    public void productsView(View view){
        Intent i = new Intent(this,ProductsActivity.class);

        startActivity(i);
    }
    
    
}
