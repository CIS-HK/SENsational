package edu.cis.sensational.Controller.Colorize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.cis.sensational.Controller.Home.HomeAdapter;
import edu.cis.sensational.Model.Colorize.GameConstants;
import edu.cis.sensational.R;

public class ColorizeSettingsActivity extends AppCompatActivity {

    Spinner timeSpinner;
    Button backButton;
    ArrayList<String> times;
    ArrayAdapter myArrayAdapter;
    String selectedTime;
    Integer seconds;
    TextView displayText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorize_settings);
        backButton = findViewById(R.id.backButton);
        times = new ArrayList<String>();
        displayText = findViewById(R.id.displayTime);

        //https://www.javatpoint.com/android-spinner-example
        timeSpinner = findViewById(R.id.spinnerTime);
        addTime();

        myArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, times);
        timeSpinner.setAdapter(myArrayAdapter);
        myArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        getTime();
        setupButtons();

    }

    private void addTime()
    {
        times.add("3 seconds");
        times.add("4 seconds");
        times.add("5 seconds");
    }

    private void setupButtons()
    {


       backButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(ColorizeSettingsActivity.this,ColorizeStartActivity.class);

               intent.putExtra("Time",seconds);

               String someMsg = (String) timeSpinner.getSelectedItem();
               System.out.println(someMsg);
               startActivity(intent);

           }
       });
//       timeSpinner.setOnItemClickdListener(new OnItemSelectedListener() {
//           @Override
//           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
//           {
//               selectedTime = adapterView.getItemAtPosition(i).toString();
//               Toast.makeText(getApplicationContext(),selectedTime,Toast.LENGTH_LONG).show();
//               Log.d("getTime","time");
//           }
//
//           @Override
//           public void onNothingSelected(AdapterView<?> adapterView) {
//
//           }
//       });

//        timeSpinner.setOnItemClickListener(new HomeAdapter.OnItemClickListener());


    }

    private void getTime()
    {
        selectedTime = String.valueOf(timeSpinner.getSelectedItem());
        displayText.setText(selectedTime);

        if(!selectedTime.isEmpty())
        {
            if (selectedTime.equals("3 seconds"))
            {
                seconds = 3000;
            }
            if (selectedTime.equals("4 seconds"))
            {
                seconds = 4000;
            }
            if (selectedTime.equals("5 seconds"))
            {
                seconds = 5000;
            }


        }
    }




}
