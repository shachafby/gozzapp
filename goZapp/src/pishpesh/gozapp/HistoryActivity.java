package pishpesh.gozapp;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.support.v4.app.NavUtils;

public class HistoryActivity extends ListActivity {


	goZappApplication appState = ((goZappApplication)this.getApplication());
	private ListView classList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_history);
		
		TabHost th = (TabHost)findViewById(R.id.tabhost); 
		th.setup();		
		
		TabSpec purSpec = th.newTabSpec("tag1");
		purSpec.setIndicator("Purchases");	
		purSpec.setContent(R.id.tab1);
		th.addTab(purSpec);
		
		TabSpec classSpec = th.newTabSpec("tag2");
		classSpec.setIndicator("Classes");	
		classSpec.setContent(R.id.tab2);
		th.addTab(classSpec);
		
		List<Purchase> purchases = appState.datasource.getPurchasesByCostumer(appState.selectedCostumer);
		ArrayAdapter<Purchase> adapter = new ArrayAdapter<Purchase>(this,android.R.layout.simple_list_item_1, purchases);
		setListAdapter(adapter);

		classList = (ListView) findViewById( R.id.Classes);
		List<Class> classes = appState.datasource.getClassesByCostumer(appState.selectedCostumer);
		classList.setAdapter(new ArrayAdapter<Class>(this,android.R.layout.simple_list_item_1, classes));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_history, menu);
		return true;
	}

}
