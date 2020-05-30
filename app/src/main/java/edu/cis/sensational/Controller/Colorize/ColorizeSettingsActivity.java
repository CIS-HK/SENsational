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
    private void addTime()
    {
        times.add(GameConstants.FIVESECONDS);
        times.add(GameConstants.FOURSECONDS);
        times.add(GameConstants.THREESECONDS);
    }

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

    private void setUpMediaPlayer()
    {
        myMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.colorizemusic);
        GameConstants.mediaPlayer = myMediaPlayer;
        GameConstants.mediaPlayer.setLooping(true);
    }

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
