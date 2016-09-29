/* Homework 1
 * Calculator App
 *
 * @author: Corey Noble (cjn8)
 * @version: 1.0 (Fall 2016)
 */

package com.example.cjn8.homework1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.text.NumberFormat;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, TextView.OnEditorActionListener {

    //variables
    private EditText value1;
    private EditText value2;
    private Spinner operatorSpinner;
    private Button calcButton;
    private TextView resultL;

    private String value1String = "";
    private String value2String = "";
    private String operatorString = "+";

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize widgets
        value1 = (EditText) findViewById(R.id.val1Input);
        value2 = (EditText) findViewById(R.id.val2Input);
        operatorSpinner = (Spinner) findViewById(R.id.operator_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.operator_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operatorSpinner.setAdapter(adapter);
        calcButton = (Button) findViewById(R.id.calcButton);
        resultL = (TextView) findViewById(R.id.resultLabel);

        calcButton.setOnClickListener(this);
        operatorSpinner.setOnItemSelectedListener(this);

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public void onPause() {
        Editor editor = savedValues.edit();
        editor.putString("value1String", value1String);
        editor.putString("value2String", value2String);
        editor.apply();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        value1String = savedValues.getString("value1String", "");
        value2String = savedValues.getString("value2String", "");
        calculateAndDisplay();
    }

    public void calculateAndDisplay() {
        value1String = value1.getText().toString();
        value2String = value2.getText().toString();
        float resultF;
        if (value1String.equals("")){
            resultF = 0;
        }
        else if (value2String.equals("")) {
            resultF = 0;
        }

        //calculate based on operator selected
        else {
            if (operatorString.equals("+")) {
                resultF = Float.parseFloat(value1String) + Float.parseFloat(value2String);
            } else if (operatorString.equals("-")) {
                resultF = Float.parseFloat(value1String) - Float.parseFloat(value2String);
            } else if (operatorString.equals("*")) {
                resultF = Float.parseFloat(value1String) * Float.parseFloat(value2String);
            } else {
                resultF = Float.parseFloat(value1String) / Float.parseFloat(value2String);
            }
        }

        NumberFormat number = NumberFormat.getNumberInstance();

        //set result label as result
        resultL.setText(number.format(resultF));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        operatorString = parent.getItemAtPosition(position).toString();
    }
    public void onNothingSelected(AdapterView<?> arg0){

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            calculateAndDisplay();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        calculateAndDisplay();
    }
}
