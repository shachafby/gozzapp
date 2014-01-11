package pishpesh.gozapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import pishpesh.gozapp.Constants.PRODUCT_TYPE;
import pishpesh.gozapp.Entities.Purchase;

public class NewPurchaseActivity extends ListActivity implements DatePickerDialog.OnDateSetListener{

	goZappApplication appState = ((goZappApplication)this.getApplication());

    private ListView ProductList;

    private List<AmountProduct> products = new ArrayList<AmountProduct>();

    //private RadioButton checkedRadioButton;
	//private RadioGroup rGroup;
	private Button addBtn;
	private EditText comment;
    private Button startPeriodButton;
    private Date startDate = new Date();
    private Date finishDate = new Date();
    private Product selectedProduct;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_new_purchase);

        appState.datasource.products = appState.datasource.getAllProducts();

        ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(this,android.R.layout.simple_list_item_single_choice, appState.datasource.products);
        setListAdapter(adapter);

        ProductList = getListView();

        addBtn = (Button)findViewById(R.id.addItemButton);
        addBtn.setEnabled(false);
		comment = (EditText)findViewById(R.id.commentText);
        startPeriodButton = (Button)findViewById(R.id.startPeriodButton);

        ProductList.setFocusableInTouchMode(true);
        ProductList.requestFocus();
        startPeriodButton.setVisibility(View.INVISIBLE);

        ProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                selectedProduct = (Product)ProductList.getAdapter().getItem(position);
                if(selectedProduct instanceof PeriodProduct){
                    addBtn.setEnabled(false);
                    startPeriodButton.setVisibility(View.VISIBLE);
                }
                else{
                    addBtn.setEnabled(true);
                    startPeriodButton.setVisibility(View.INVISIBLE);
                }
            }
        });


        }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void onAdd(View v){

		//int creditDelta=0;
	
		//write purchase__
		Date d = Calendar.getInstance().getTime();
		Purchase p = appState.datasource.createPurchase(
                appState.datasource.selectedCostumer.getId(),
                d,
                selectedProduct.getProductType(),
                startDate,
                finishDate,
                selectedProduct.getVolume(),
                comment.getText().toString());

        /*
        if needed.
        maybe status update
         */
		//add credit
        if(selectedProduct.getProductType()== PRODUCT_TYPE.ByAmount)
		appState.datasource.updateCredit(
                appState.datasource.selectedCostumer.getId(),
                appState.datasource.selectedCostumer.getCredit()+selectedProduct.getVolume());
 
        Intent i = new Intent(this,CostumersActivity.class);
    	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(i);
		
	}

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        startDate = cal.getTime();

        cal.add(Calendar.MONTH, selectedProduct.getVolume());
        finishDate=cal.getTime();

        //startPeriodButton.setText("start:"+day+"."+(month+1)+"."+year);
        startPeriodButton.setText(new SimpleDateFormat("dd/MM/yy").format(startDate)+" - "+
                new SimpleDateFormat("dd/MM/yy").format(finishDate));

        addBtn.setEnabled(true);


    }

}
