package com.example.tipcalculatorv2;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.NumberFormat;
import java.lang.Math;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements TextWatcher, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemSelectedListener {

    //declare your variables for the widgets
    private EditText editTextBillAmount;
    private TextView textViewBillAmount;
    private TextView percentTextView;
    private TextView textViewTip;
    private TextView perPersonText;
    private SeekBar seekBar;
    private Spinner spinner;

    //declare the variables for the calculations
    private double billAmount = 0.0;
    private double percent = .15;
    private double tip = billAmount * percent;
    private double total = billAmount + tip;
    //set the number formats to be used for the $ amounts , and % amounts
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private static final NumberFormat percentFormat = NumberFormat.getPercentInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextBillAmount = findViewById(R.id.editText_BillAmount);
        editTextBillAmount.addTextChangedListener( this);

        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);

        percentTextView =  findViewById(R.id.tipPercentLbl);

        textViewTip = findViewById(R.id.tipTotal);
        perPersonText = findViewById(R.id.perPerson);

        textViewBillAmount = findViewById(R.id.billTotal);

        spinner = findViewById(R.id.spinner);
        if (spinner != null) {
            spinner.setOnItemSelectedListener(this);
        }
        // Create ArrayAdapter using the string array and default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.split_bill, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
    /*
    Note:   int i, int i1, and int i2
            represent start, before, count respectively
            The charSequence is converted to a String and parsed to a double for you
     */
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {
        Log.d("MainActivity", "inside onTextChanged method: charSequence= "+charSequence);
        //surround risky calculations with try catch (what if billAmount is 0 ?5

        if(charSequence.toString().isEmpty())
        {
            billAmount = 0.0;
            textViewBillAmount.setText(currencyFormat.format(billAmount));
            calculate();
        }
        else
        {
            //charSequence is converted to a String and parsed to a double for you
            billAmount = Double.parseDouble(charSequence.toString()); Log.d("MainActivity", "Bill Amount = "+billAmount);
            //setText on the textView
            textViewBillAmount.setText(currencyFormat.format(billAmount));
            //perform tip and total calculation and update UI by calling calculate
            calculate();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) { }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b)
    {
        percent = progress / 100.0; //calculate percent based on seeker value
        calculate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { }

    // calculate and display tip and total amounts
    private void calculate() {
        Log.d("MainActivity", "inside calculate method");

        // format percent and display in percentTextView
        percentTextView.setText(percentFormat.format(percent));

        tip = billAmount * percent;

        total = billAmount + tip;

        int numPeople = spinner.getSelectedItemPosition() + 1;

        // display tip and total formatted as currency
        //user currencyFormat instead of percentFormat to set the textViewTip
        textViewTip.setText(currencyFormat.format(tip));

        //use the tip example to do the same for the Total
        textViewBillAmount.setText(currencyFormat.format(total));
        perPersonText.setText(currencyFormat.format(total / numPeople));
    }

    public void noRounding(View view)
    {
        calculate();
    }

    public void tipRounding(View view)
    {
        double roundedTip = Math.round(tip * 100) / 100;
        textViewBillAmount.setText(currencyFormat.format(total));
        textViewTip.setText(currencyFormat.format(roundedTip));
    }

    public void totalRounding(View view)
    {
        double roundedTotal = Math.round(total * 100) / 100;
        textViewTip.setText(currencyFormat.format(tip));
        textViewBillAmount.setText(currencyFormat.format(roundedTotal));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        String spinnerLabel = adapterView.getItemAtPosition(i).toString();

        switch (spinnerLabel)
        {
            case "1 person bill":
                calculate();
            case "Split the bill between 2":
                displayToast(spinnerLabel + " selected");
                calculate();
            case "Split the bill between 3":
                displayToast(spinnerLabel + " selected");
                calculate();
            case "Split the bill between 4":
                displayToast(spinnerLabel + " selected");
                calculate();
        }
    }
    // Interface callback for when no spinner item is selected.
    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
        // Do nothing.
    }

    public void displayToast(String message)
    {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }
}