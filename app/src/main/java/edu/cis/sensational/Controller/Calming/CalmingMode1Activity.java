package edu.cis.sensational.Controller.Calming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;

import edu.cis.sensational.Model.CConstants;
import edu.cis.sensational.R;

public class CalmingMode1Activity extends AppCompatActivity
{
    //Declaring the images and text on the GUI
    private ImageView circle;
    private TextView breathe;
    private TextView number;

    //Declaring constants object
    private CConstants c;

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

        //Setting up CConstants variable to access constants
        c = new CConstants();

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
        final AnimationSet set = Util.sizeControl(circle, this);

        //Animation listener to see what is happening with the animation
        set.setAnimationListener(new Animation.AnimationListener()
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
                    circle.startAnimation(set);
                }
            }

            //When the animation repeats
            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }
        });
    }

    //Method to control the words text view
    public void text()
    {
        //Words to be shown in order on the screen for for seconds, then 6 seconds
        final String[] array2 = {c.bIn, c.hold, c.bOut};
        //Making a new Runnable (loop) for the words string
        Thread t = new Thread(new Runnable()
        {
            int x = 0;
            @Override
            public void run()
            {
                //Setting 3 second interval between the changing of words
                if(x == 1)
                {
                    breathe.postDelayed(this, 2000);
                }
                else
                {
                    breathe.postDelayed(this, 3000);
                }
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

    //Method to control number text view
    public void number()
    {
        //Array that hold the numbers to be shown in order on the screen per 1 second
        final String[] array1 = {c.one, c.two, c.three, c.one, c.two,c.one, c.two, c.three};
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
                if (i == 8)
                {
                    i = 0;
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
        mPlayer.stop();
        mPlayer.release();
        Intent myIntent = new Intent(CalmingMode1Activity.this, CalmingActivity.class);
        startActivity(myIntent);
    }
}
