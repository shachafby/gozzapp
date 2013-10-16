package pishpesh.gozapp;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Comparator;

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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by DavarGozal on 10/11/13.
 */
public class ProductsActivity extends ListActivity {

    goZappApplication appState = ((goZappApplication)this.getApplication());

    Activity activity = this;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE );

        setContentView(R.layout.activity_products);

        appState.datasource.products = appState.datasource.getAllProducts();

        ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(this,android.R.layout.simple_list_item_1, appState.datasource.products);

        adapter.sort(new Comparator<Product> (){
            @Override
            public int compare(Product p1, Product p2) {
                return p1.getName().compareTo(p2.getName());
            }});

        setListAdapter(adapter);

        final ListView listview = this.getListView();
        listview.setLongClickable(true);
        listview.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parentView, View childView, int position, long id) {
                // this will provide the value
                final Product p = (Product) listview.getItemAtPosition(position);

                AlertDialog.Builder alert = new AlertDialog.Builder(activity);

                alert.setTitle("Are you sure you want to delete Product '" + p.getName() + "'?");

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        appState.datasource.deleteProduct(p);

                        appState.datasource.products = appState.datasource.getAllProducts();

                        @SuppressWarnings("unchecked")
                        ArrayAdapter<Product> adapter = (ArrayAdapter<Product>) getListAdapter();

                        adapter.notifyDataSetChanged();
                        Intent i = new Intent(activity, ProductsActivity.class);
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

        ArrayAdapter<Product> adapter = (ArrayAdapter<Product>) getListAdapter();

        adapter.notifyDataSetChanged();
    }

    public void newProductView(View view){
        Intent i = new Intent(this,NewProductActivity.class);

        startActivity(i);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);

    }
}