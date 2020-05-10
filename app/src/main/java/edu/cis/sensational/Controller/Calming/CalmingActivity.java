package edu.cis.sensational.Controller.Calming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import edu.cis.sensational.Controller.MainActivity;
import edu.cis.sensational.R;

public class CalmingActivity extends AppCompatActivity
{
    //Declaring the buttons for each mode on the start page
    private Button mode1;
    private Button mode2;
    private Button mode3;
    private Button back;
    private ImageButton settings;

    private int inInt;
    private int holdInt;
    private int outInt;

    private boolean goneSettings;

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

        System.out.println("gone: " + goneSettings);


//        @Override
//        public void onSaveInstanceState(Bundle savedInstanceState)
//        {
//            super.onSaveInstanceState(savedInstanceState);
//            savedInstanceState.putBoolean("goneSettings", goneSettings);
//        }
//
//        @Override
//        public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        goneSettings = savedInstanceState.getBoolean("goneSettings");

    }





    //Method to send the user to different pages for the mode they click
    public void setUpButtons()
    {
        mode1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getBundle();
                //Sends the user to the Mode 1 activity
                Intent myIntent = new Intent(CalmingActivity.this,
                        CalmingMode1Activity.class);
                //Sending information to next intent
                addBundle(myIntent);
                startActivity(myIntent);
            }
        });
        mode2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Sends the user to the Mode 2 activity
                Intent myIntent = new Intent(CalmingActivity.this,
                        CalmingMode2Activity.class);
                //Sending information to next intent
//                addBundle(myIntent);
                startActivity(myIntent);
            }
        });
        mode3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Sends the user to the Mode 3 activity
                Intent myIntent = new Intent(CalmingActivity.this,
                        CalmingMode3Activity.class);
                //Sending information to next intent
//                addBundle(myIntent);
                startActivity(myIntent);
            }
        });

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
        settings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goneSettings = true;
                Intent intent = new Intent(CalmingActivity.this,
                        CalmingSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getBundle()
    {
        if(getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            inInt = b.getInt("In");
            holdInt = b.getInt("Hold");
            outInt = b.getInt("Out");
            System.out.println(inInt + ". " + holdInt + " . " + outInt);
        }
    }

    //Adding information for circle growth and shrinking into an intent
    public void addBundle(Intent intent)
    {
        intent.putExtra("In", inInt);
        intent.putExtra("Hold", holdInt);
        intent.putExtra("Out", outInt);
    }

}
