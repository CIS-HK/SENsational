package edu.cis.sensational.Controller.Colorize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.cis.sensational.Controller.Home.HomeAdapter;
import edu.cis.sensational.Model.Colorize.GameConstants;
import edu.cis.sensational.R;

/**
 * The settings page prompts user to select their game preferences such as background, music and
 * time
 */
public class ColorizeSettingsActivity extends AppCompatActivity {

    Spinner timeSpinner;
    Button backButton;
    ArrayList<String> times;
    ArrayAdapter myArrayAdapter;
    String selectedTime;
    Integer seconds;
    CheckBox backgroundcheck;
    Switch musicSwitch;
    MediaPlayer myMediaPlayer;

    /**
     * Creates and identifies the various components on screen such as buttons, spinners and
     * switches
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorize_settings);
        backButton = findViewById(R.id.backButton);
        times = new ArrayList<String>();
        backgroundcheck = findViewById(R.id.backgroundCheck);
        musicSwitch = findViewById(R.id.musicSwitch);

        //https://www.javatpoint.com/android-spinner-example
        // create a spinner with user choices for time selection
        timeSpinner = findViewById(R.id.spinnerTime);
        addTime();
        myArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
                times);
        timeSpinner.setAdapter(myArrayAdapter);
        myArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        setUpMusicChoice();
        setBackgroundcheck();
        setupButtons();
        setUpMediaPlayer();

    }

    /**
     * Checks the user's preference for background option and sets the checkbox
     */
    private void setBackgroundcheck()
    {
        if (!GameConstants.BACKGROUND)
        {
            backgroundcheck.setChecked(false);
        }

        else if (GameConstants.BACKGROUND)
        {
            backgroundcheck.setChecked(true);
        }

    }

    /**
     * Adds the timer options in an arraylist to display on spinner
     */
    private void addTime()
    {
        times.add(GameConstants.FIVESECONDS);
        times.add(GameConstants.FOURSECONDS);
        times.add(GameConstants.THREESECONDS);
    }

    /**
     * Identifies actions once the corresponding buttons on the screen is pressed
     */
    private void setupButtons()
    {

        musicSwitch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (musicSwitch.isChecked())
                {
                    GameConstants.mediaPlayer.start();
                    GameConstants.mediaPlayer.setVolume(GameConstants.VOLUME,GameConstants.VOLUME);
                    GameConstants.MUSIC = true;
                }
                else
                {

                    GameConstants.mediaPlayer.pause();
                    GameConstants.MUSIC = false;
                }
            }
        });

       backButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(ColorizeSettingsActivity.this,
                       ColorizeStartActivity.class);
               getTime();

               //mapping the user preference to a specific String key and passing it to the next
               //activity
               intent.putExtra(GameConstants.TIMESTRING,seconds);
               startActivity(intent);

           }
       });

        backgroundcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if (backgroundcheck.isChecked())
                {
                    GameConstants.BACKGROUND = true;
                }
                if (!backgroundcheck.isChecked())
                {
                    GameConstants.BACKGROUND = false;
                }
            }
        });
    }

    /**
     * Checks the user's preference for music option and sets the checkbox
     */
    private void setUpMusicChoice()
    {
        if (!GameConstants.MUSIC)
        {
            musicSwitch.setChecked(false);
        }
        else if (GameConstants.MUSIC)
        {
            musicSwitch.setChecked(true);
        }
    }

    /**
     * Creates a new media Player
     */
    private void setUpMediaPlayer()
    {
        myMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.colorizemusic);
        GameConstants.mediaPlayer = myMediaPlayer;
        GameConstants.mediaPlayer.setLooping(true);
    }

    /**
     * Gets what the user option is for time preference
     */
    private void getTime()
    {
        //since the spinner is an array of Strings, have to convert them to int first in order to
        // pass the data as a parameter in the Timer method
        selectedTime = String.valueOf(timeSpinner.getSelectedItem());

        if(!selectedTime.isEmpty())
        {
            if (selectedTime.equals(GameConstants.THREESECONDS))
            {
                seconds = GameConstants.THREESEC;
            }
            if (selectedTime.equals(GameConstants.FOURSECONDS))
            {
                seconds = GameConstants.FOURSEC;
            }
            if (selectedTime.equals(GameConstants.FIVESECONDS))
            {
                seconds = GameConstants.DEFAULTTIME;
            }

        }
    }

}
