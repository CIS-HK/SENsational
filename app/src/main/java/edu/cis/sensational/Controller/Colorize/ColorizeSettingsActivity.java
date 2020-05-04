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
import android.widget.Spinner;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorize_settings);
        displayBackground = findViewById(R.id.backgroundCheck);
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
       if (displayBackground.isChecked())
       {
           GameConstants.BACKGROUND = true;
       }
       if (!displayBackground.isChecked())
           {
           GameConstants.BACKGROUND = false;
       }

       backButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(ColorizeSettingsActivity.this,ColorizeStartActivity.class));
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
