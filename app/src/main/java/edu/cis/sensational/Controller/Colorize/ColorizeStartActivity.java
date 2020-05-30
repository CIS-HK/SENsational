package edu.cis.sensational.Controller.Colorize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import edu.cis.sensational.Controller.SharedGames.GamesSharedActivity;
import edu.cis.sensational.Model.Colorize.GameConstants;
import edu.cis.sensational.R;

public class ColorizeStartActivity extends AppCompatActivity
{

    Button playButton, quitButton, instructionButton, settingsButton;
    TextView gameName;

    ImageView bottomright, middleleft, middleright, topright, topleft;
    int screenWidth, screenHeight;
    float brX,mlY,mrY,trX,tlX;


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

        timer = new Timer();
        handler = new Handler();

        setUpButtons();
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

                //grab bundle from settings activity to this activity
                addBundle(getIntent());

                //pass the bundle from this activity to the games activity
                addBundle(intent);
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
        //get the data from the bundle sent from the Settings page by using the String Key
        if(getIntent().getExtras() != null)
        {
            Bundle b = getIntent().getExtras();
            seconds = b.getInt(GameConstants.TIMESTRING);

        }
        else
        {
            seconds = GameConstants.DEFAULTTIME;
        }
    }

    //place the data gathered from the bundle into a new bundle and pass it onto the next activity
    public void addBundle(Intent intent)
    {
        intent.putExtra(GameConstants.TIMESTRING, seconds);
    }

}

