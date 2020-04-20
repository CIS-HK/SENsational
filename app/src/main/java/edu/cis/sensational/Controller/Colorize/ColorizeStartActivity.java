package edu.cis.sensational.Controller.Colorize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import edu.cis.sensational.R;

public class ColorizeStartActivity extends AppCompatActivity {

    Button playButton, quitButton, instructionButton;
    TextView gameName;
    Switch musicSwitch;
    MediaPlayer myMediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorize_start);
        gameName = findViewById(R.id.gameName);
        playButton = findViewById(R.id.playButton);
        quitButton = findViewById(R.id.quitButton);
        instructionButton = findViewById(R.id.instructionsButton);

        //https://stackoverflow.com/questions/37244357/how-to-play-music-in-android-studio
//        musicSwitch = findViewById(R.id.musicSwitch);
//        musicSwitch.setChecked(false);
//        musicSwitch.setTextOff("OFF");
//        musicSwitch.setTextOn("ON");
        //myMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.colorizemusic);

        setUpButtons();
    }

    private void setUpButtons() {
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ColorizeStartActivity.this, ColorizeMainActivity.class));

            }
        });

        instructionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ColorizeStartActivity.this, ColorizeInstructionsActivity.class));
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ColorizeStartActivity.this, GamesSharedActivity.class));
            }
        });

        //https://abhiandroid.com/ui/switch
        //https://www.tutlane.com/tutorial/android/android-switch-on-off-button-with-examples

//        musicSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (musicSwitch.isChecked())
//                {
//                    myMediaPlayer.start();
//                    myMediaPlayer.setVolume(20,20);
//                }
//                else
//                {
//                    myMediaPlayer.stop();
//                }
//            }
//        });
//
//    }

    }
}