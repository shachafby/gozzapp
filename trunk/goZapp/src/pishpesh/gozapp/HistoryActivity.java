package pishpesh.gozapp;

import java.util.Comparator;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TabHost.TabSpec;

public class HistoryActivity extends ListActivity {


	goZappApplication appState = ((goZappApplication)this.getApplication());
	private ListView classList;

	Activity activity = this;
	
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
		
		List<Purchase> purchases = appState.datasource.getPurchasesByCostumer(appState.datasource.selectedCostumer);
		ArrayAdapter<Purchase> adapter = new ArrayAdapter<Purchase>(this,android.R.layout.simple_list_item_1, purchases);
		
		adapter.sort(new Comparator<Purchase> (){
			@Override
			public int compare(Purchase p1, Purchase p2) {
				if(p1.getDate().before(p2.getDate()))
					return 1;
				else return -1;
			}});
		setListAdapter(adapter);

		classList = (ListView) findViewById( R.id.Classes);
		List<Class> classes = appState.datasource.getClassesByCostumer(appState.datasource.selectedCostumer);
		classList.setAdapter(new ArrayAdapter<Class>(this,android.R.layout.simple_list_item_1, classes));
		
		((ArrayAdapter) classList.getAdapter()).sort(new Comparator<Class> (){
			@Override
			public int compare(Class p1, Class p2) {
				if(p1.getDateObj().before(p2.getDateObj()))
					return 1;
				else return -1;
			}});
		
		final ListView listview = this.getListView();
		listview.setLongClickable(true);
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView parentView, View childView, int position, long id) {
				// this will provide the value
				final Purchase p = (Purchase)listview.getItemAtPosition(position);

				AlertDialog.Builder alert = new AlertDialog.Builder(activity);

				alert.setTitle("Are you sure you want to delete Purchase '"+p+"'?");

				alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						
						//remove credit		
						appState.datasource.updateCredit(appState.datasource.selectedCostumer.getId(), 
								appState.datasource.selectedCostumer.getCredit()-p.getPurchaseType());
						//remove purchase
						appState.datasource.deletePurchase(p);
						
						appState.datasource.initAppObjects();
												
						Intent i = new Intent(activity, HistoryActivity.class);
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
	
		classList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView parentView, View childView, int position, long id) {
				// this will provide the value
				final Class c = (Class)classList.getItemAtPosition(position);

				AlertDialog.Builder alert = new AlertDialog.Builder(activity);

				alert.setTitle("Are you sure you want to remove custumer '"+appState.datasource.selectedCostumer.getName()+"' from class '"+c+"'?");

				alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						
						
						//add 1 credit		
						appState.datasource.updateCredit(appState.datasource.selectedCostumer.getId(), 
								appState.datasource.selectedCostumer.getCredit()+1);
						
						//remove CustumerFromClass
						appState.datasource.removeCustomerFromClass(c, appState.datasource.selectedCostumer);
						
						appState.datasource.initAppObjects();
						
						Intent i = new Intent(activity, HistoryActivity.class);
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

	protected void onResume() {
		//appState.datasource.open();
		super.onResume();

		//appState.costumers = appState.datasource.getAllCostumers();

		@SuppressWarnings("unchecked")

		ArrayAdapter<Costumer> adapter = (ArrayAdapter<Costumer>) getListAdapter();

		adapter.notifyDataSetChanged();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.activity_history, menu);
	return true;

	}

}
