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
 * Class for calming mode 3 activity
 */
public class CalmingMode3Activity extends AppCompatActivity
{
    //Declaring the images and text on the GUI
    private ImageView fish1;
    private ImageView fish2;
    private ImageView fish3;
    private ImageView fish4;
    private ImageView fish5;
    private ImageView bubbles1;
    private ImageView bubbles2;
    private ImageView circle;
    private TextView breathe;
    private TextView number;

    //Declaring constants object
    private CConstants c;

    //Setting location of the images
    float fish1X ;
    float fish1Y;
    float fish2X ;
    float fish2Y;
    float fish3X;
    float fish3Y;
    float fish4X;
    float fish4Y;
    float fish5X;
    float fish5Y;
    float bubbles1X;
    float bubbles1Y;
    float bubbles2X;
    float bubbles2Y;

    //Declaring media player that will play a song
    private MediaPlayer mPlayer3;

    //Getting the height and width of the screen
    private float screenWidth;
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
     * Creates and identifies the various components for mode 3 screen, including buttons, text,
     * and images
     * @param savedInstanceState Saved instance of mode 3 activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Setting up the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calming_mode3);

        //Initialising the circle and text
        circle = findViewById(R.id.circle3);
        breathe = findViewById(R.id.breatheTextView3);
        number = findViewById(R.id.numberTextView3);

        //Setting default growth, hold, and shrink time to 3-2-3
        inInt = c.THREE_INT;
        holdInt = c.TWO_INT;
        outInt = c.THREE_INT;

        //Setting up CConstants variable to access constants
        c = new CConstants();

        //Initialising the fish and bubbles on the GUI
        fish1 = findViewById(R.id.fish1);
        fish2 = findViewById(R.id.fish2);
        fish3 = findViewById(R.id.fish3);
        fish4 = findViewById(R.id.fish4);
        fish5 = findViewById(R.id.fish5);
        bubbles1 = findViewById(R.id.bubbles_m_1);
        bubbles2 = findViewById(R.id.bubbles_m_2);

        //Setting the location of the fish and bubbles on the screen
        fish1X = c.NEG_FIVE_HUNDRED;
        fish1Y = c.SEVEN_HUNDRED;

        fish2X = c.TWO_HUNDRED;
        fish2Y = c.ONE_HUNDRED;

        fish3X = c.FIVE_HUNDRED;
        fish3Y = c.ONE_THOUSAND_TWO_HUNDRED;

        fish4X = c.EIGHT_HUNDRED;
        fish4Y = c.THREE_HUNDRED;

        fish5X = c.SIX_HUNDRED;
        fish5Y = c.FOUR_HUNDRED;

        bubbles1X = c.THREE_HUNDRED;
        bubbles1Y = c.ZERO_INT;

        bubbles2X = c.NINE_HUNDRED;
        bubbles2Y = c.ONE_THOUSAND;

        //Getting size of screen
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        //Calling methods to control the movement of the circle and the changing of the text
        sizeControl();

        //Getting the song MP3 from the raw file and setting it to the media player
        mPlayer3 = MediaPlayer.create(this, R.raw.calming_music_2);

        //Starting the song
        mPlayer3.start();

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
                        goRight();
                        goLeft();
                        goUp();
                    }
                });
            }
        },c.ZERO_INT,c.FORTY);
    }

    /**
     * Method to make fish images move right across the screen continuously
     */
    public void goRight()
    {
        fish1X += c.TEN;
        fish2X += c.TEN;
        fish5X += c.TEN;

        if(fish1.getX() > screenWidth)
        {
            fish1X = c.NEG_FIVE_HUNDRED;
        }
        fish1.setX(fish1X);

        if(fish2.getX() > screenWidth)
        {
            fish2X = c.NEG_FIVE_HUNDRED;
        }
        fish2.setX(fish2X);

        if(fish5.getX() > screenWidth)
        {
            fish5X = c.NEG_FIVE_HUNDRED;
        }
        fish5.setX(fish5X);
    }

    /**
     * Method to make fish images move left across the screen continuously
     */
    public void goLeft()
    {
        fish3X -= c.TEN;
        fish4X -= c.TEN;

        if(fish3.getX() + fish3.getWidth() < c.ZERO_INT)
        {
            fish3X = screenWidth + c.HUNDRED;
        }
        fish3.setX(fish3X);

        if(fish4.getX() + fish4.getWidth() < c.ZERO_INT)
        {
            fish4X = screenWidth + c.HUNDRED;
        }
        fish4.setX(fish4X);
    }

    /**
     * Method to make bubble images move up across the screen continuously
     */
    public void goUp()
    {
        bubbles1Y -= c.TEN;
        bubbles2Y -= c.TEN;

        if(bubbles1.getY() + bubbles1.getHeight() < c.ZERO_INT)
        {
            bubbles1Y = screenHeight + c.HUNDRED;
        }
        bubbles1.setY(bubbles1Y);

        if(bubbles2.getY() + bubbles2.getHeight() < c.ZERO_INT)
        {
            bubbles2Y = screenHeight + c.HUNDRED;
        }
        bubbles2.setY(bubbles2Y);
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
     * @param view The view for mode 3 activity
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
            mPlayer3.pause();
        }
        //Resuming
        else
        {
            //Starting the music again
            mPlayer3.start();

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
                            goLeft();
                            goRight();
                        }
                    });
                }
            },c.ZERO_INT,c.FORTY);
        }
    }

    /**
     * Method for button to go back to main activity from mode 3 activity
     */
    public void backButton(View view)
    {
        mPlayer3.stop();
        mPlayer3.release();
        Intent myIntent = new Intent(CalmingMode3Activity.this, CalmingActivity.class);
        myIntent.putExtra(c.IN, inInt);
        myIntent.putExtra(c.HOLD, holdInt);
        myIntent.putExtra(c.OUT, outInt);
        startActivity(myIntent);
    }
}