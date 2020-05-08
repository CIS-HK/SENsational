package edu.cis.sensational.Controller.Colorize;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import edu.cis.sensational.Controller.SharedGames.GamesSharedActivity;
import edu.cis.sensational.Model.Colorize.GameConstants;
import edu.cis.sensational.R;

public class ColorizeStartActivity extends AppCompatActivity {

    Button playButton, quitButton, instructionButton, settingsButton;
    TextView gameName;
    Switch musicSwitch;
    MediaPlayer myMediaPlayer;
    ImageView bottomright, middleleft, middleright, topright, topleft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorize_start);
        gameName = findViewById(R.id.gameName);
        playButton = findViewById(R.id.playButton);
        quitButton = findViewById(R.id.quitButton);
        instructionButton = findViewById(R.id.instructionsButton);
        settingsButton = findViewById(R.id.settingButton);
        bottomright = findViewById(R.id.imageView7);
        middleleft = findViewById(R.id.imageView4);
        middleright = findViewById(R.id.imageView6);
        topright = findViewById(R.id.imageView5);
        topleft = findViewById(R.id.imageView3);


        //https://stackoverflow.com/questions/37244357/how-to-play-music-in-android-studio
        musicSwitch = findViewById(R.id.musicSwitch);
        myMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.colorizemusic);
        GameConstants.mediaPlayer = myMediaPlayer;
        GameConstants.mediaPlayer.setLooping(true);

        setUpButtons();
        //animation();
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
                if (GameConstants.mediaPlayer.isPlaying()){
                    GameConstants.mediaPlayer.stop();
                }
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ColorizeStartActivity.this,ColorizeSettingsActivity.class));
            }
        });

        //https://abhiandroid.com/ui/switch
        //https://www.tutlane.com/tutorial/android/android-switch-on-off-button-with-examples

        musicSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicSwitch.isChecked())
                {
                    GameConstants.mediaPlayer.start();
                    GameConstants.mediaPlayer.setVolume(20,20);
                    GameConstants.MUSIC = true;
                }
                else
                {
                    GameConstants.mediaPlayer.pause();
                    GameConstants.MUSIC = false;
                }
            }
        });

    }

    //https://developer.android.com/training/animation/reposition-view
    private void animation()
    {
        ObjectAnimator animation = ObjectAnimator.ofFloat(bottomright,"translationX", -700f);
        ObjectAnimator animation2 = ObjectAnimator.ofFloat(middleleft,"translationY", -300f);
        ObjectAnimator animation3 = ObjectAnimator.ofFloat(middleright,"translationX", -300f);
        ObjectAnimator animation4 = ObjectAnimator.ofFloat(topright,"translationY", 300f);
        ObjectAnimator animation5 = ObjectAnimator.ofFloat(topleft, "translationX", 600f);
        animation.setDuration(500);
        animation.start();
        animation2.setDuration(500);
        animation2.start();
        animation3.setDuration(500);
        animation3.start();
        animation4.setDuration(500);
        animation4.start();
        animation5.setDuration(500);
        animation5.start();
    }


}