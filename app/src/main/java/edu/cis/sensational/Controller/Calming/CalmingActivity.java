package edu.cis.sensational.Controller.Calming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import edu.cis.sensational.Controller.MainActivity;
import edu.cis.sensational.Model.CConstants;
import edu.cis.sensational.R;

/**
 * Class for calming start page activity
 */
public class CalmingActivity extends AppCompatActivity
{
    //Declaring the buttons for each mode on the start page
    private Button mode1;
    private Button mode2;
    private Button mode3;
    private Button back;
    private ImageButton settings;

    //Declaring numbers for circle
    private int inInt;
    private int holdInt;
    private int outInt;

    private CConstants c = new CConstants();

    /**
     * Creates and identifies the various components for calming home activity on screen, including
     * buttons, text, and images
     * @param savedInstanceState Saved instance of main page activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Setting up the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calming);

        //Initialising the buttons
        mode1 = findViewById(R.id.mode1Button);
        mode2 = findViewById(R.id.mode2Button);
        mode3 = findViewById(R.id.mode3Button);
        back = findViewById(R.id.backbutton);
        settings = findViewById(R.id.settingsButtonCalming);

        //Calling a method to set up the buttons
        setUpButtons();

        //Getting info from bundle
        getBundle();
    }

    /**
     * Method to set up buttons to go to mode 1, mode 2, mode 3 or settings activity, or back to
     * main home page
     */
    public void setUpButtons()
    {
        //Method to go mode 1 page
        mode1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Sends the user to the Mode 1 activity
                Intent myIntent = new Intent(CalmingActivity.this,
                        CalmingMode1Activity.class);
                //Sending information to next intent
                addBundle(getIntent());
                addBundle(myIntent);
                startActivity(myIntent);
            }
        });

        //Method to go mode 2 page
        mode2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Sends the user to the Mode 2 activity
                Intent myIntent = new Intent(CalmingActivity.this,
                        CalmingMode2Activity.class);
                //Sending information to next intent
                addBundle(myIntent);
                startActivity(myIntent);
            }
        });

        //Method to go mode 3 page
        mode3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Sends the user to the Mode 3 activity
                Intent myIntent = new Intent(CalmingActivity.this,
                        CalmingMode3Activity.class);
                //Sending information to next intent
                addBundle(myIntent);
                startActivity(myIntent);
            }
        });

        //Method to go back to start page
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(CalmingActivity.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });

        //Method to go to settings page
        settings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(CalmingActivity.this,
                        CalmingSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Getting bundle from extras of an intent, setting variables to information from bundle
     */
    public void getBundle()
    {
        //Checking if bundle is empty (if setting have been saved or not)
        if(getIntent().getExtras() != null)
        {
            Bundle b = getIntent().getExtras();
            inInt = b.getInt(c.IN);
            holdInt = b.getInt(c.HOLD);
            outInt = b.getInt(c.OUT);
        }
    }

    /**
     * Method to add a bundle of Strings with settings about breathing exercise timing to an intent
     * @param intent is the intent the extra information should be added to
     */
    public void addBundle(Intent intent)
    {
        intent.putExtra(c.IN, inInt);
        intent.putExtra(c.HOLD, holdInt);
        intent.putExtra(c.OUT, outInt);
    }
}