package pishpesh.gozapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.*;

/**
 * Created by DavarGozal on 10/13/13.
 */
public class NewProductActivity extends Activity {


    goZappApplication appState = ((goZappApplication)this.getApplication());

    EditText productName;
    RadioButton ByAmountRadioButton;
    RadioButton ByPeriodRadioButton;
    NumberPicker amountPicker;
    NumberPicker monthsPicker;
    Button CreateProductBtn;
    RadioGroup productRadioGroup;
    PRODUCT_TYPE productType;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_new_product);

        productName = (EditText)findViewById(R.id.productName);
        ByAmountRadioButton = (RadioButton)findViewById(R.id.ByAmountRadioButton);
        ByPeriodRadioButton = (RadioButton)findViewById(R.id.ByPeriodRadioButton);
        amountPicker = (NumberPicker)findViewById(R.id.amountPicker);
        monthsPicker = (NumberPicker)findViewById(R.id.monthsPicker);
        CreateProductBtn = (Button)findViewById(R.id.CreateProductBtn);
        productRadioGroup = (RadioGroup) findViewById(R.id.productRadioGroup);

        amountPicker.setEnabled(false);
        monthsPicker.setEnabled(false);
        CreateProductBtn.setEnabled(false);

        amountPicker.setMinValue(0);
        amountPicker.setMaxValue(100);

        monthsPicker.setMinValue(0);
        monthsPicker.setMaxValue(12);

        productRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadio = productRadioGroup.getCheckedRadioButtonId();

                if (checkedRadio==ByAmountRadioButton.getId()){
                    amountPicker.setEnabled(true);
                    monthsPicker.setEnabled(false);

                    productType = PRODUCT_TYPE.ByAmount;
                }
                if (checkedRadio==ByPeriodRadioButton.getId()){
                    amountPicker.setEnabled(false);
                    monthsPicker.setEnabled(true);
                    productType = PRODUCT_TYPE.ByPeriod;
                }

                if(isValidInput())
                    CreateProductBtn.setEnabled(true);
                else
                    CreateProductBtn.setEnabled(false);
                 }
        });

        amountPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(isValidInput())
                    CreateProductBtn.setEnabled(true);
                else
                    CreateProductBtn.setEnabled(false);
            }
        });

        monthsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(isValidInput())
                    CreateProductBtn.setEnabled(true);
                else
                    CreateProductBtn.setEnabled(false);
            }
        });

        productName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(isValidInput())
                    CreateProductBtn.setEnabled(true);
                else
                    CreateProductBtn.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private boolean isValidInput() {
        if(!productName.getText().toString().equals("") &&
                ((ByAmountRadioButton.isChecked()&& amountPicker.getValue()>0) ||
                        (ByPeriodRadioButton.isChecked()&& monthsPicker.getValue()>0)))
           return true;
        else
            return false;
    }

    public void onCreateProduct(View view) {

        Product p = null;

        if(productType==PRODUCT_TYPE.ByAmount)
            p = appState.datasource.createProduct(productName.getText().toString(),"ByAmount",amountPicker.getValue(),0);
        else {
            p = appState.datasource.createProduct(productName.getText().toString(),"ByPeriod" , 0, monthsPicker.getValue());
        }

        Intent i = new Intent(this,ProductsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);


    }
    public enum PRODUCT_TYPE{
        ByAmount (1), ByPeriod (2);

        private int value;

        private PRODUCT_TYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
