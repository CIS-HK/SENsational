package edu.cis.sensational.Controller.Calming;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import edu.cis.sensational.R;

public class CalmingSettingsActivity extends AppCompatActivity
{
    Spinner inSpinner;
    ArrayList <String> list = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calming_settings);

        inSpinner = findViewById(R.id.calmingInSpinner);
        list.addAll(Arrays.asList("1", "2", "3", "4", "5"));
        addSpinner();
    }

    //https://www.javatpoint.com/android-spinner-example
    public void addSpinner()
    {
       ArrayAdapter myArrayAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
       myArrayAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       inSpinner.setAdapter(myArrayAdaptor);
    }

    //Button to go back to main activity
    public void backButton(View view)
    {
        Intent myIntent = new Intent(CalmingSettingsActivity.this, CalmingActivity.class);
        startActivity(myIntent);
    }
}