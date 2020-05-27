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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import edu.cis.sensational.Controller.SharedGames.GamesSharedActivity;
import edu.cis.sensational.Model.Colorize.GameConstants;
import edu.cis.sensational.R;

public class ColorizeStartActivity extends AppCompatActivity
{

    Button playButton, quitButton, instructionButton, settingsButton;
    TextView gameName;
    Switch musicSwitch;
    MediaPlayer myMediaPlayer;
    ImageView bottomright, middleleft, middleright, topright, topleft;
    int screenWidth, screenHeight;
    float brX,mlY,mrY,trX,tlX;
    CheckBox backgroundcheck;

    Handler handler;
    Timer timer;
    Integer seconds;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
        backgroundcheck = findViewById(R.id.backgroundCheck2);
        musicSwitch = findViewById(R.id.musicSwitch);

        timer = new Timer();
        handler = new Handler();

        setUpButtons();
        setUpMediaPlayer();
        animation();
        getBundle();

    }

    /**
     * Sets up listeners for buttons on the screen
     */

    private void setUpButtons() {
        playButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ColorizeStartActivity.this,
                        ColorizeMainActivity.class);
                addBundle(getIntent());
                //addBundle(intent);
                startActivity(intent);

            }
        });

        instructionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ColorizeStartActivity.this,
                        ColorizeInstructionsActivity.class));
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ColorizeStartActivity.this,
                        GamesSharedActivity.class));
                if (GameConstants.mediaPlayer.isPlaying())
                {
                    GameConstants.mediaPlayer.stop();
                }
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ColorizeStartActivity.this,
                        ColorizeSettingsActivity.class));
            }
        });

        //https://abhiandroid.com/ui/switch
        //https://www.tutlane.com/tutorial/android/android-switch-on-off-button-with-examples

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

            backgroundcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {
                    if (backgroundcheck.isChecked())
                    {
                        GameConstants.BACKGROUND = true;
                        Toast.makeText(getApplicationContext(),"Checked", Toast.LENGTH_SHORT).
                                show();
                        Log.d("CHECKED", "yes");
                    }
                    if (!backgroundcheck.isChecked())
                    {
                        GameConstants.BACKGROUND = false;
                        Toast.makeText(getApplicationContext(),"Not checked",
                                Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }

    //https://stackoverflow.com/questions/37244357/how-to-play-music-in-android-studio
    private void setUpMediaPlayer()
    {
        myMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.colorizemusic);
        GameConstants.mediaPlayer = myMediaPlayer;
        GameConstants.mediaPlayer.setLooping(true);
    }

    //https://www.youtube.com/watch?v=UxbJKNjQWD8&app=desktop
    private void animation()
    {
        Point size = new Point();
        screenWidth = size.x;
        screenHeight = size.y;

        final Runnable run = new Runnable()
        {
            @Override
            public void run()
            {
                changePosition();
            }
        };

        TimerTask update = new TimerTask()
        {
            @Override
            public void run()
            {
                handler.post(run);
            }
        };

        timer.schedule(update,GameConstants.DELAY,GameConstants.PERIOD);

    }

    private void changePosition()
    {

        //go up
        mlY -= GameConstants.MOVEMENTMARGIN;
        if (middleleft.getY() + middleleft.getHeight() < GameConstants.ZERO)
        {
            mlY = screenHeight + GameConstants.SCREENHEIGHTUP;
        }

        middleleft.setY(mlY);

        //go down
        mrY += GameConstants.MOVEMENTMARGIN;
        if (middleright.getY() - GameConstants.MIDDLERIGHTINITIAL > screenHeight)
        {
            mrY = -GameConstants.SCREENHEIGHTDOWN;
        }

        middleright.setY(mrY);

        //go right
        tlX += GameConstants.MOVEMENTMARGIN;
        if (topleft.getX() - GameConstants.TOPLEFTINITIAL > screenWidth)
        {
            tlX = -GameConstants.SCREENWIDTHLEFT;
        }

        topleft.setX(tlX);

        //go left
        brX -= GameConstants.MOVEMENTMARGIN;
        if (bottomright.getX() + bottomright.getWidth() < GameConstants.ZERO)
        {
            brX = screenWidth + GameConstants.SCREENWIDTHLEFT;
        }

        bottomright.setX(brX);

        //go left
        trX -=GameConstants.MOVEMENTMARGIN;
        if (topright.getX() + topright.getWidth() < GameConstants.ZERO)
        {
            trX = screenWidth + GameConstants.SCREENWIDTHLEFT;
        }
        topright.setX(trX);

    }


    public void getBundle()
    {
        if(getIntent().getExtras() != null)
        {
            Bundle b = getIntent().getExtras();
            seconds = b.getInt("Time");

        }
        else
        {
            seconds = 5000;
        }
    }

    public void addBundle(Intent intent)
    {
        intent.putExtra("Time", seconds);

    }


}

