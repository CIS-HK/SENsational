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
        number();

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

        //Getting the grow animation from anim file and setting the duration to 4 seconds, and
        //adding it to the animation set for the circle
        Animation grow = AnimationUtils.loadAnimation(this, R.anim.circleanimation2);
        grow.setDuration(4000);
        animSet.addAnimation(grow);

        //Getting the shrink animation from anim file and setting the duration to 4 seconds, and
        //the start offset to 8 seconds so there will be a 4 second stop before shrink starts and
        //adding it to the animation set for the circle
        Animation shrink = AnimationUtils.loadAnimation(this, R.anim.circleanimation);
        shrink.setDuration(4000);
        shrink.setStartOffset(8000);
        animSet.addAnimation(shrink);

        //Animation listener to see what is happening with the animation
        animSet.setAnimationListener(new Animation.AnimationListener()
        {
            //When the animation starts
            @Override
            public void onAnimationStart(Animation animation)
            {
            }

            //When the animation ends
            @Override
            public void onAnimationEnd(Animation animation)
            {
                //If the screen is not paused, the animation will loop again at once it reaches the
                //end of the animation
                if(pause == false)
                {
                    circle.startAnimation(animSet);
                }
            }

            //When the animation repeats
            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }
        });
        //Circle starts to perform the animations in animation set
        circle.startAnimation(animSet);
    }

    //Method to control number text view
    public void number()
    {
        //Array that hold the numbers to be shown in order on the screen per 1 second
        final String[] array1 = {"1", "2", "3", "4"};
        //Making a new Runnable (loop) for the number string
        number.post(new Runnable()
        {
            int i = 0;
            @Override
            public void run()
            {
                //Setting almost 1 second interval between the changing of numbers
                number.postDelayed(this, 992);
                //Setting the text to the number in the array and positively incrementing i
                number.setText(array1[i]);
                i++;
                //If paused, i is zero again and it is back to the start
                if (pause == true)
                {
                    i = 0;
                    number.setText(array1[i]);
                }
                //When it reaches the end of array, goes back to beginning, continuing the loop
                if (i == 4)
                {
                    i = 0;
                }

            }
        });
    }

    //Method to control the words text view
    public void text()
    {
        //Words to be shown in order on the screen for for seconds, then 6 seconds
        final String[] array2 = {"Breathe in", "Hold", "Breathe out"};
        //Making a new Runnable (loop) for the words string
        breathe.post(new Runnable()
        {
            int x = 0;
            @Override
            public void run()
            {
                //Setting 4 second interval between the changing of words
                breathe.postDelayed(this, 4000);
                //Setting the text to the words in the array and positively incrementing x
                breathe.setText(array2[x]);
                x++;
                //If paused, i is zero again and it is back to the start
                if(pause == true)
                {
                    x = 0;
                    breathe.setText(array2[x]);
                }
                //When it reaches the end of array, goes back to beginning, continuing the loop
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
            //Setting pause boolean to true
            pause = true;

            //Clearing circle animation to stop it
            circle.clearAnimation();

            //Setting circle height and width back to beginning
            int height = (int)circle.getHeight();
            int width = (int)circle.getWidth();
            circle.setMaxHeight(height);
            circle.setMaxWidth(width);

            //Pausing the music
            mPlayer.pause();
        }
        //Resuming
        else
        {
            //Starting music again
            mPlayer.start();

            //Setting pause boolean to false
            pause = false;

            //Continuing the circle animation
            sizeControl();

        }
    }

    //Button to go back to main activity
    public void backButton(View view)
    {
        Intent myIntent = new Intent(CalmingMode1Activity.this, CalmingActivity.class);
        startActivity(myIntent);
    }
}
