package edu.cis.sensational.Controller.Calming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import edu.cis.sensational.Model.CConstants;
import edu.cis.sensational.R;

/**
 * Class for calming mode 2 activity
 */
public class CalmingMode2Activity extends AppCompatActivity
{
    //Declaring the images and text on the GUI
    private ImageView bf1;
    private ImageView bf2;
    private ImageView bf3;
    private ImageView bf4;
    private ImageView bf5;
    private ImageView bf6;
    private ImageView circle;
    private TextView breathe;
    private TextView number;

    //Declaring constants object
    private CConstants c;

    //Setting location of the images
    float bf1Y;
    float bf2Y;
    float bf3Y;
    float bf4Y;
    float bf5Y;
    float bf6Y;

    //Declaring media player that will play a song
    private MediaPlayer mPlayer2;

    //Getting the height and width of the screen
    private float screenHeight;

    //Creating things needed like Timer and handler for images to move on screen, and pause boolean
    //for the pause button
    private boolean pause = false;
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    //Declaring integers for the selected growth, hold, and shrink times
    private int inInt;
    private int holdInt;
    private int outInt;

    //Declaring arraylist for the growth, hold, and shrink times that will be added
    private ArrayList<String> numberArray;


    /**
     * Creates and identifies the various components for mode 2 screen, including buttons, text,
     * and images
     * @param savedInstanceState Saved instance of mode 2 activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Setting up the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calming_mode2);

        //Initialising the circle and text
        circle = findViewById(R.id.circle2);
        breathe = findViewById(R.id.breatheTextView1);
        number = findViewById(R.id.numberTextView2);

        //Setting default growth, hold, and shrink time to 3-2-3
        inInt = 3;
        holdInt = 2;
        outInt = 3;

        //Setting up CConstants variable to access constants
        c = new CConstants();

        //Initialising the butterflies in the background
        bf1 = findViewById(R.id.bf1);
        bf2 = findViewById(R.id.bf2);
        bf3 = findViewById(R.id.bf3);
        bf4 = findViewById(R.id.bf4);
        bf5 = findViewById(R.id.bf5);
        bf6 = findViewById(R.id.bf6);

        //Getting size of screen
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;

        //Setting the location of the fish and bubbles on the screen
        bf1Y = 1600;
        bf2Y = 500;
        bf3Y = 1200;
        bf4Y = 100;
        bf5Y = 2000;
        bf6Y = 900;

        //Calling methods to control the movement of the circle and the changing of the text
        sizeControl();

        //Getting the song MP3 from the raw file and setting it to the media player
        mPlayer2 = MediaPlayer.create(this, R.raw.calming_music_2);

        //Starting the song
        mPlayer2.start();

        //Setting timer for movement on the screen
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
                        goUp();
                    }
                });
            }
        },0,40);
    }

    /**
     * Method to make butterfly images move up across the screen continuously
     */
    public void goUp()
    {
        bf1Y -= 10;
        bf2Y -= 10;
        bf3Y -= 10;
        bf4Y -= 10;
        bf5Y -= 10;
        bf6Y -= 10;

        if(bf1.getY() + bf1.getHeight() < 0)
        {
            bf1Y = screenHeight + 100.0f;
        }
        bf1.setY(bf1Y);

        if(bf2.getY() + bf2.getHeight() < 0)
        {
            bf2Y = screenHeight + 100.0f;
        }
        bf2.setY(bf2Y);

        if(bf3.getY() + bf3.getHeight() < 0)
        {
            bf3Y = screenHeight + 100.0f;
        }
        bf3.setY(bf3Y);

        if(bf4.getY() + bf4.getHeight() < 0)
        {
            bf4Y = screenHeight + 100.0f;
        }
        bf4.setY(bf4Y);

        if(bf5.getY() + bf5.getHeight() < 0)
        {
            bf5Y = screenHeight + 100.0f;
        }
        bf5.setY(bf5Y);

        if(bf6.getY() + bf6.getHeight() < 0)
        {
            bf6Y = screenHeight + 100.0f;
        }
        bf6.setY(bf6Y);
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
        if(getIntent().getExtras().getInt(c.IN) != 0)
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
        final ArrayList<String> wordArray = c.WORD_ARRAY;

        //Making a new Runnable (loop) for the words string
        breathe.post(new Runnable()
        {
            int x = 0;
            @Override
            public void run()
            {
                //Setting  second interval between the changing of words to the values from the
                //parameters

                //Breathe in
                if(x == 0)
                {
                    breathe.postDelayed(this, in * 1000);
                }
                //Hold
                if(x == 1)
                {
                    breathe.postDelayed(this, hold * 1000);
                }
                //Breathe out
                if(x == 2)
                {
                    breathe.postDelayed(this,out * 1000);
                }

                //Setting the text to the words in the array and positively incrementing x
                breathe.setText(wordArray.get(x));
                x++;

                //If paused, i is zero again and it is back to the start
                if(pause == true)
                {
                    x = 0;
                    breathe.setText(wordArray.get(x));
                }
                //When it reaches the end of array, goes back to beginning, continuing the loop
                if (x == 3)
                {
                    x = 0;
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
            int i = 0;
            @Override
            public void run()
            {
                //Setting almost 1 second interval between the changing of numbers
                number.postDelayed(this, 998);

                //Setting the text to the number in the array and positively incrementing i
                number.setText(numberArray.get(i));
                i++;
                //If paused, i is zero again and it is back to the start
                if (pause == true)
                {
                    i = 0;
                    number.setText(numberArray.get(i));
                }
                //When it reaches the end of array, goes back to beginning, continuing the loop
                if (i == numberArray.size())
                {
                    i = 0;
                }
            }
        });
    }

    /**
     * Method for pause button in order to pause all animations and music
     * @param view The view for mode 2 activity
     */
    public void pauseButton(View view)
    {
        //Checking if screen is paused
        if(pause == false)
        {
            //Setting pause boolean to true
            pause = true;

            //Cancelling timer so butterflies stop moving
            timer.cancel();
            timer = null;

            //Clearing circle animation to stop it
            circle.clearAnimation();

            //Setting circle height and width back to beginning
            int height = (int)circle.getHeight();
            int width = (int)circle.getWidth();
            circle.setMaxHeight(height);
            circle.setMaxWidth(width);

            //Pausing the music
            mPlayer2.pause();
        }
        //Resuming
        else
        {
            //Starting the music again
            mPlayer2.start();

            //Setting pause boolean to false
            pause = false;

            // Continuing the circle animation
            sizeControl();

            //Creating and starting new timer for butterflies to move again
            timer = new Timer();
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
                            goUp();
                        }
                    });
                }
            },0,40);
        }
    }

    /**
     * Method for button to go back to main activity from mode 2 activity
     */
    public void backButton(View view)
    {
        mPlayer2.stop();
        mPlayer2.release();
        Intent myIntent = new Intent(CalmingMode2Activity.this, CalmingActivity.class);
        myIntent.putExtra(c.IN, inInt);
        myIntent.putExtra(c.HOLD, holdInt);
        myIntent.putExtra(c.OUT, outInt);
        startActivity(myIntent);
    }
}