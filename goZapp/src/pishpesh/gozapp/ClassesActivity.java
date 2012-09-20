package pishpesh.gozapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.support.v4.app.NavUtils;

public class ClassesActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_classes);
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
    
}
