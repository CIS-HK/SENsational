package edu.cis.sensational.Controller.Calming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.cis.sensational.Model.CConstants;
import edu.cis.sensational.R;

/**
 * Class for calming mode 1 activity
 */
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

    //Declaring integers for the selected growth, hold, and shrink times
    private int inInt;
    private int holdInt;
    private int outInt;

    //Declaring arraylist for the growth, hold, and shrink times that will be added
    private ArrayList<String> numberArray;

    /**
     * Creates and identifies the various components for mode 1 on screen, including buttons, text,
     * and images
     * @param savedInstanceState Saved instance of mode 1 activity
     */
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

        //Setting default growth, hold, and shrink time to 3-2-3
        inInt = c.THREE_INT;
        holdInt = c.TWO_INT;
        outInt = c.THREE_INT;

        //Calling methods to control the movement of the circle and the changing of the text
        sizeControl();

        //Getting the song MP3 from the raw file and setting it to the media player
        mPlayer = MediaPlayer.create(this, R.raw.calming_music_2);

        //Starting the song
        mPlayer.start();
    }

    /**
     * Controls growth and shrinking of circle using default 3-2-3 or information from
     * settings unpacked in bundle from  home activity. Runs Util class method to create
     * number array. Runs Util class method circle control to get animation set, sets an
     * animation listener to repeat animation when finished. Calls the methods to control
     * breathe and number text views.
     */
    public void sizeControl()
    {
        //Checking if intent is empty (if settings have been chosen and saved)
        if(getIntent().getExtras().getInt(c.IN) != c.ZERO_INT)
        {
            //Getting values from bundle in the extras
            Bundle b = getIntent().getExtras();
            inInt = b.getInt(c.IN);
            holdInt = b.getInt(c.HOLD);
            outInt = b.getInt(c.OUT);
        }
        //Initialising arraylist for numbers using the util class method numberArrayList
        numberArray = Util.numberArrayList(inInt,holdInt,outInt);

        //Adding util circle control method to the animation set using number values for growth,
        // hold, and shrink times
        final AnimationSet set = Util.circleControl(circle, this, inInt, holdInt, outInt);

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

        //Running methods to control the text and number on the circle
        text(inInt, holdInt, outInt);
        number();
    }

    /**
     * Method to control the breathe text view
     * @param in Breathe in time
     * @param hold Hold time
     * @param out Breathe out time
     */
    public void text(final int in, final int hold, final int out)
    {
        //Words to be shown in order on the screen
        final ArrayList<String> wordArray = Util.wordArrayList(c);

        //Making a new Runnable (loop) for the words string
        breathe.post(new Runnable()
        {
            int x = c.ZERO_INT;
            @Override
            public void run()
            {
                //Setting  second interval between the changing of words to the values from the
                //parameters

                if(pause == false)
                {
                    //Breathe in
                    if (x == c.ZERO_INT) {
                        breathe.postDelayed(this, in * c.ONE_THOUSAND);
                    }
                    //Hold
                    if (x == c.ONE_INT) {
                        breathe.postDelayed(this, hold * c.ONE_THOUSAND);
                    }
                    //Breathe out
                    if (x == c.TWO_INT) {
                        breathe.postDelayed(this, out * c.ONE_THOUSAND);
                    }

                    //Setting the text to the words in the array and positively incrementing x
                    breathe.setText(wordArray.get(x));
                    x++;

                    //When it reaches the end of array, goes back to beginning, continuing the loop
                    if (x == c.THREE_INT)
                    {
                        x = c.ZERO_INT;
                    }
                }

                //If paused, i is zero again and it is back to the start
                if(pause == true)
                {
                    x = c.ZERO_INT;
                    breathe.setText(wordArray.get(x));
                }
            }
        });
    }

    /**
     * Method to control number text view
     */
    public void number()
    {
        //Making a new Runnable (loop) for the number string
        number.post(new Runnable()
        {
            int i = c.ZERO_INT;
            @Override
            public void run()
            {
                //Setting almost 1 second interval between the changing of numbers
                number.postDelayed(this, c.NINE_NINE_EIGHT);

                //Setting the text to the number in the array and positively incrementing i
                number.setText(numberArray.get(i));
                i++;
                //If paused, i is zero again and it is back to the start
                if (pause == true)
                {
                    i = c.ZERO_INT;
                    number.setText(numberArray.get(i));
                }
                //When it reaches the end of array, goes back to beginning, continuing the loop
                if (i == numberArray.size())
                {
                    i = c.ZERO_INT;
                }
            }
        });
    }

    /**
     * Method for pause button in order to pause all animations and music
     * @param view The view for mode 1 activity
     */
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
            //Starting the music again
            mPlayer.start();

            //Setting pause boolean to false
            pause = false;

            //Continuing the circle animation
            sizeControl();
        }
    }

    /**
     * Method for button to go back to main activity from mode 1 activity
     * @param view The view for this activity
     */
    public void backButton(View view)
    {
        mPlayer.stop();
        mPlayer.release();
        Intent myIntent = new Intent(CalmingMode1Activity.this, CalmingActivity.class);
        myIntent.putExtra(c.IN, inInt);
        myIntent.putExtra(c.HOLD, holdInt);
        myIntent.putExtra(c.OUT, outInt);
        startActivity(myIntent);
    }
}