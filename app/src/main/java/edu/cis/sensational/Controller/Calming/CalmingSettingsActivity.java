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

public class CalmingSettingsActivity extends AppCompatActivity {
    Spinner inSpinner;
    Spinner holdSpinner;
    Spinner outSpinner;

    String inString;
    String holdString;
    String outString;

    ArrayList<String> list = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calming_settings);

        inSpinner = findViewById(R.id.calmingInSpinner);
        holdSpinner = findViewById(R.id.calmingHoldSpinner);
        outSpinner = findViewById(R.id.calmingOutSpinner);

        holdString = "";
        inString = "";
        outString = "";


        list.addAll(Arrays.asList("1", "2", "3", "4", "5"));
        addSpinner();
    }

    //https://www.javatpoint.com/android-spinner-example
    //Adding spinners to settings screen and initialising them to have 1-5
    public void addSpinner() {
        ArrayAdapter myArrayAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        myArrayAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inSpinner.setAdapter(myArrayAdaptor);
        holdSpinner.setAdapter(myArrayAdaptor);
        outSpinner.setAdapter(myArrayAdaptor);
    }


    public void saveSettings(View view) {
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