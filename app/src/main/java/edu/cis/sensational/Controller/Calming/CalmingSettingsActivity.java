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

/**
 * Class for calming settings activity
 */
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

    /**
     * Creates and identifies the various components for settings on screen, including buttons, text,
     * and images
     * @param savedInstanceState Saved instance of settings activity
     */
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
        list.addAll(Arrays.asList(c.ONE, c.TWO, c.THREE, c.FOUR, c.FIVE));
        addSpinner();
    }

    /**
     * Adds spinners to settings screen and initializes them to show number options 1-5
     * https://www.javatpoint.com/android-spinner-example
     */
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

    /**
     * Method to save in, hold, and out settings selections as strings from spinners
     * @param view The view for settings activity
     */
    public void saveSettings(View view)
    {
        inString = inSpinner.getSelectedItem().toString();
        holdString = holdSpinner.getSelectedItem().toString();
        outString = outSpinner.getSelectedItem().toString();
    }

    /**
     * Method for button to go back to main activity from settings activity
     * @param view The view for settings activity
     */
    public void backButton(View view)
    {
        Intent myIntent = new Intent(CalmingSettingsActivity.this, CalmingActivity.class);

        //Adding extras with the new settings if user has saved settings
        //https://developer.android.com/reference/android/os/Bundle
        if(!inString.isEmpty() && !holdString.isEmpty() && !outString.isEmpty())
        {
            myIntent.putExtra(c.IN, Integer.parseInt(inString));
            myIntent.putExtra(c.HOLD, Integer.parseInt(holdString));
            myIntent.putExtra(c.OUT, Integer.parseInt(outString));
        }
        startActivity(myIntent);
    }
}