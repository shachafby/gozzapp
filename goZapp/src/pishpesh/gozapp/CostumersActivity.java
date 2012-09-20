package pishpesh.gozapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.support.v4.app.NavUtils;

public class CostumersActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_costumers);
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
    
}
