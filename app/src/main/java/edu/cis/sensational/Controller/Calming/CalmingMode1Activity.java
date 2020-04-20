package edu.cis.sensational.Controller.Calming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import edu.cis.sensational.R;

public class CalmingMode1Activity extends AppCompatActivity
{
    //Declaring the images and text on the GUI
    private ImageView circle;
    private TextView breathe;
    private TextView number;

    //Declaring boolean for if screen has been paused
    private boolean pause = false;

    //Declaring media player that will play a song
    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Setting up the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calming_mode1);

        //Initialising the circle and text
        circle = findViewById(R.id.circle1);
        breathe = findViewById(R.id.breatheTextView1);
        number = findViewById(R.id.numberTextView1);

        //Calling methods to control the movement of the circle and the changing of the text
        sizeControl();
        text();

        //Getting the song MP3 from the raw file and setting it to the media player
        mPlayer = MediaPlayer.create(this, R.raw.calming_music_2);

        //Starting the song
        mPlayer.start();

    }

    //Method to control the size of the circle
    public void sizeControl()
    {
        //https://developer.android.com/reference/android/view/animation/AnimationSet.html
        //Docs for animation set

        //Creating a new final animation set that will be used on the circle
        final AnimationSet animSet = new AnimationSet(true);

        //Getting the grow animation from anim file
        Animation grow = AnimationUtils.loadAnimation(this, R.anim.circleanimation2);
        grow.setDuration(4000);
        animSet.addAnimation(grow);

        Animation shrink = AnimationUtils.loadAnimation(this, R.anim.circleanimation);
        shrink.setDuration(4000);
        shrink.setStartOffset(8000);

        animSet.addAnimation(shrink);

        animSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation)
            {
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                if(pause == false)
                {
                    circle.startAnimation(animSet);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }
        });
        circle.startAnimation(animSet);
    }

    public void text() {
        //Numbers to be shown in order on the screen per 1 second
        final String[] array1 = {"1", "2", "3", "4"};

        //Words to be shown in order on the screen for for seconds, then 6 seconds
        final String[] array2 = {"Breathe in", "Hold", "Breathe out"};

        number.post(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                number.postDelayed(this, 992);
                number.setText(array1[i]);
                i++;
                if (pause == true)
                {
                    i = 0;
                    number.setText(array1[i]);
                }
                if (i == 4)
                {
                    i = 0;
                }

            }
        });

        breathe.post(new Runnable() {
            int x = 0;
            @Override
            public void run()
            {
                breathe.postDelayed(this, 4000);
                breathe.setText(array2[x]);
                x++;
                if(pause == true)
                {
                    x = 0;
                    breathe.setText(array2[x]);
                }
                if (x == 3)
                {
                    x = 0;
                }
            }
        });

    }

    public void pauseButton(View view)
    {
        //Checking if screen is paused
        if(pause == false)
        {
            pause = true;

            circle.clearAnimation();
            int height = (int)circle.getHeight();
            int width = (int)circle.getWidth();
            circle.setMaxHeight(height);
            circle.setMaxWidth(width);
            mPlayer.pause();

        }
        else
        {
            mPlayer.start();
            pause = false;
            // Continuing the circle
            sizeControl();

        }
    }

    public void backButton(View view)
    {
        Intent myIntent = new Intent(CalmingMode1Activity.this, CalmingActivity.class);
        startActivity(myIntent);
    }
}
