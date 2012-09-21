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
import android.support.v4.app.NavUtils;

public class CostumersActivity extends ListActivity {

	private goZappDataSource datasource;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_costumers);
        
	    datasource = new goZappDataSource(this);
	    datasource.open();
	    
	    List<Costumer> costumers = datasource.getAllCostumers();
	    
//	     Use the SimpleCursorAdapter to show the
//	     elements in a ListView
	    ArrayAdapter<Costumer> adapter = new ArrayAdapter<Costumer>(this,android.R.layout.simple_list_item_1, costumers);
	    setListAdapter(adapter);

    }
    
 

 	  @Override
 	  protected void onResume() {
 	    datasource.open();
 	    super.onResume();
 	    
 	   ArrayAdapter<Costumer> adapter = (ArrayAdapter<Costumer>) getListAdapter();
	   adapter.notifyDataSetChanged();
 	  }

 	  @Override
 	  protected void onPause() {
 	    datasource.close();
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
   
//    public void onClick(View view) {
//	    @SuppressWarnings("unchecked")
//	    ArrayAdapter<Costumer> adapter = (ArrayAdapter<Costumer>) getListAdapter();
//	    Costumer costumer = null;
//	    switch (view.getId()) {
//	    case R.id.newCostumerBtn:
//	      
//	      // Save the new comment to the database
//	      costumer = datasource.createCostumer("aaa","bbb","ccc","ddd");
//	      adapter.add(costumer);
//	      break;
//	      
//	    }
//	    adapter.notifyDataSetChanged();
//	  }


  
}
/*
public class TestDatabaseActivity extends ListActivity {
	  private CommentsDataSource datasource;

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);

	    datasource = new CommentsDataSource(this);
	    datasource.open();

	    List<Comment> values = datasource.getAllComments();

	    // Use the SimpleCursorAdapter to show the
	    // elements in a ListView
	    ArrayAdapter<Comment> adapter = new ArrayAdapter<Comment>(this,
	        android.R.layout.simple_list_item_1, values);
	    setListAdapter(adapter);
	  }

	  // Will be called via the onClick attribute
	  // of the buttons in main.xml
	  public void onClick(View view) {
	    @SuppressWarnings("unchecked")
	    ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) getListAdapter();
	    Comment comment = null;
	    switch (view.getId()) {
	    case R.id.add:
	      String[] comments = new String[] { "Cool", "Very nice", "Hate it" };
	      int nextInt = new Random().nextInt(3);
	      // Save the new comment to the database
	      comment = datasource.createComment(comments[nextInt]);
	      adapter.add(comment);
	      break;
	    case R.id.delete:
	      if (getListAdapter().getCount() > 0) {
	        comment = (Comment) getListAdapter().getItem(0);
	        datasource.deleteComment(comment);
	        adapter.remove(comment);
	      }
	      break;
	    }
	    adapter.notifyDataSetChanged();
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

	}*/ 