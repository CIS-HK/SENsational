package edu.cis.sensational.Controller.Calming;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

import edu.cis.sensational.Model.CConstants;
import edu.cis.sensational.R;

public class CalmingSettingsActivity extends AppCompatActivity
{
    //Declaring spinners on GUI
    private Spinner inSpinner;
    private Spinner holdSpinner;
    private Spinner outSpinner;

    //Declaring strings for in, hold, and out numbers
    private String inString;
    private String holdString;
    private String outString;

    //Declaring contants variable
    private CConstants c;

    //Declaring arraylist for spinner
    private ArrayList<String> list = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calming_settings);

        //Initialising all values/GUI
        inSpinner = findViewById(R.id.calmingInSpinner);
        holdSpinner = findViewById(R.id.calmingHoldSpinner);
        outSpinner = findViewById(R.id.calmingOutSpinner);

        holdString = "";
        inString = "";
        outString = "";

        c = new CConstants();

        //Add numbers 1-5 to spinner list
        list.addAll(Arrays.asList(c.one, c.two, c.three, c.four, c.five));
        addSpinner();
    }

    //https://www.javatpoint.com/android-spinner-example
    //Adding spinners to settings screen and initialising them to have 1-5
    public void addSpinner()
    {
        //Creating new adaptor
        ArrayAdapter myArrayAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        myArrayAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting adaptor for the three spinners
        inSpinner.setAdapter(myArrayAdaptor);
        holdSpinner.setAdapter(myArrayAdaptor);
        outSpinner.setAdapter(myArrayAdaptor);
    }

    //Method to save in, hold, and out settings
    public void saveSettings(View view)
    {
        inString = inSpinner.getSelectedItem().toString();
        holdString = holdSpinner.getSelectedItem().toString();
        outString = outSpinner.getSelectedItem().toString();
    }

    //Button to go back to main activity
    public void backButton(View view)
    {
        Intent myIntent = new Intent(CalmingSettingsActivity.this, CalmingActivity.class);

        //Adding extras with the new settings if user has saved settings
        //https://developer.android.com/reference/android/os/Bundle
        if(!inString.isEmpty() && !holdString.isEmpty() && !outString.isEmpty())
        {
            myIntent.putExtra("In", Integer.parseInt(inString));
            myIntent.putExtra("Hold", Integer.parseInt(holdString));
            myIntent.putExtra("Out", Integer.parseInt(outString));
        }
        startActivity(myIntent);
    }
}