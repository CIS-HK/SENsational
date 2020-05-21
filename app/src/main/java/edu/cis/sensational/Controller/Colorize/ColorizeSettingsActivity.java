package edu.cis.sensational.Controller.Colorize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.cis.sensational.Model.Colorize.GameConstants;
import edu.cis.sensational.R;

public class ColorizeSettingsActivity extends AppCompatActivity {

    CheckBox displayBackground;
    Spinner timeSpinner;
    Button backButton;
    ArrayList<String> times;
    ArrayAdapter myArrayAdapter;
    String selectedTime;
    Integer seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorize_settings);
        displayBackground = findViewById(R.id.backgroundCheck);
        displayBackground.setChecked(true);
        times = new ArrayList<String>();

        //https://www.javatpoint.com/android-spinner-example
        timeSpinner = findViewById(R.id.spinnerTime);
        myArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, times);
        myArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(myArrayAdapter);
        backButton = findViewById(R.id.backButton);




        setupButtons();
        addTime();


    }


    private void setupButtons()
    {

       displayBackground.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if (displayBackground.isChecked())
               {
                   GameConstants.BACKGROUND = true;
               }
               if (!displayBackground.isChecked())
               {
                   GameConstants.BACKGROUND = false;
               }
           }
       });


       backButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(ColorizeSettingsActivity.this,ColorizeStartActivity.class);
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
               intent.putExtra("Time",seconds);

               startActivity(intent);

           }
       });


    }

    private void addTime()
    {
        times.add("3 seconds");
        times.add("4 seconds");
        times.add("5 seconds");
    }




}
