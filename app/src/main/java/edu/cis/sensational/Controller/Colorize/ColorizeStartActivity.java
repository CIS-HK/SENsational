package edu.cis.sensational.Controller.Colorize;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import edu.cis.sensational.Controller.SharedGames.GamesSharedActivity;
import edu.cis.sensational.Model.Colorize.GameConstants;
import edu.cis.sensational.R;

public class ColorizeStartActivity extends AppCompatActivity {

    Button playButton, quitButton, instructionButton, settingsButton;
    TextView gameName;
    Switch musicSwitch;
    MediaPlayer myMediaPlayer;
    ImageView bottomright, middleleft, middleright, topright, topleft;
    int screenWidth, screenHeight;
    float brX,mlY,mrY,trX,tlX;

    Handler handler;
    Timer timer;


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
        animation();

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

    //https://www.youtube.com/watch?v=UxbJKNjQWD8&app=desktop
    private void animation()
    {
        Point size = new Point();
        screenWidth = size.x;
        screenHeight = size.y;

        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        changePosition();
                    }
                });
            }
        },0,13);

    }

    private void changePosition() {

        //go up
        mlY -= 10;
        if (middleleft.getY() + middleleft.getHeight() < 0)
        {
            mlY = screenHeight + 2000.0f;
        }

        middleleft.setY(mlY);

        //go down
        mrY += 10;
        if (middleright.getY() - 1700 > screenHeight)
        {
            mrY = -1000.0f;
        }

        middleright.setY(mrY);

        //go right
        tlX += 10;
        if (topleft.getX() - 1200 > screenWidth)
        {
            tlX = -800.0f;
        }

        topleft.setX(tlX);

        //go left
        brX -= 10;
        if (bottomright.getX() + bottomright.getWidth() < 0)
        {
            brX = screenWidth + 800.0f;
        }

    }

//    private ObjectAnimator imageMovement(ImageView imageView, String propertyName, Float value, int duration)
//    {
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,propertyName,value);
//        objectAnimator.setDuration(duration);
//        objectAnimator.start();
//        return objectAnimator;
//
//    }
//
//    private void animationtwo()
//    {
//        imageMovement(bottomright,"translationX",-700f,500);
//        imageMovement(middleleft,"translationY", -300f,500);
//        imageMovement(middleright,"translationX",-300f, 500);
//        imageMovement(topright,"translationY", 300f, 500);
//        imageMovement(topleft,"translationX", 600f, 500);
//
//    }


}

