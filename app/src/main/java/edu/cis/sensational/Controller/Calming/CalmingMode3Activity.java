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
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import edu.cis.sensational.Model.CConstants;
import edu.cis.sensational.R;

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
    private ArrayList<String> array1;

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
        inInt = 3;
        holdInt = 2;
        outInt = 3;

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
        fish1X = -500.0f;
        fish1Y = 700;

        fish2X = 200;
        fish2Y = 100;

        fish3X = 500;
        fish3Y = 1200;

        fish4X = 800;
        fish4Y = 300;

        fish5X = 600;
        fish5Y = 400;

        bubbles1X = 300;
        bubbles1Y = 0;

        bubbles2X = 900;
        bubbles2Y = 1000;

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
        },0,40);
    }

    //Method to make fish move to the right
    public void goRight()
    {
        fish1X += 10;
        fish2X +=10;
        fish5X += 10;

        if(fish1.getX() > screenWidth)
        {
            fish1X = -500.0f;
        }
        fish1.setX(fish1X);

        if(fish2.getX() > screenWidth)
        {
            fish2X = -500.0f;
        }
        fish2.setX(fish2X);

        if(fish5.getX() > screenWidth)
        {
            fish5X = -500.0f;
        }
        fish5.setX(fish5X);
    }

    //Method to make fish move to the left
    public void goLeft()
    {
        fish3X -= 10;
        fish4X -= 10;

        if(fish3.getX() + fish3.getWidth() < 0)
        {
            fish3X = screenWidth + 100.0f;
        }
        fish3.setX(fish3X);

        if(fish4.getX() + fish4.getWidth() < 0)
        {
            fish4X = screenWidth + 100.0f;
        }
        fish4.setX(fish4X);
    }

    //Method to make the bubbles move up
    public void goUp()
    {
        bubbles1Y -= 10;
        bubbles2Y -= 10;

        if(bubbles1.getY() + bubbles1.getHeight() < 0)
        {
            bubbles1Y = screenHeight + 100.0f;
        }
        bubbles1.setY(bubbles1Y);

        if(bubbles2.getY() + bubbles2.getHeight() < 0)
        {
            bubbles2Y = screenHeight + 100.0f;
        }
        bubbles2.setY(bubbles2Y);
    }

    //Method to control the size of the circle
    public void sizeControl()
    {
        //Checking if intent is empty (if settings have been chosen and saved)
        if(getIntent().getExtras().getInt("In") != 0)
        {
            //Getting values from bundle in the extras
            Bundle b = getIntent().getExtras();
            inInt = b.getInt("In");
            holdInt = b.getInt("Hold");
            outInt = b.getInt("Out");
        }
        //Initialising arraylist for numbers using the util class method numberArrayList
        array1 = Util.numberArrayList(inInt,holdInt,outInt);

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

    //Method to control the words text view, taking in variables for length of growth, hold, shrink
    public void text(final int in, final int hold, final int out)
    {
        //Words to be shown in order on the screen
        final ArrayList<String> array2 = c.wordArray;

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
                breathe.setText(array2.get(x));
                x++;

                //If paused, i is zero again and it is back to the start
                if(pause == true)
                {
                    x = 0;
                    breathe.setText(array2.get(x));
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
                number.setText(array1.get(i));
                i++;
                //If paused, i is zero again and it is back to the start
                if (pause == true)
                {
                    i = 0;
                    number.setText(array1.get(i));
                }
                //When it reaches the end of array, goes back to beginning, continuing the loop
                if (i == array1.size())
                {
                    i = 0;
                }
            }
        });
    }

    //Method to pause movement on screen
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
            },0,40);
        }
    }

    //Button to go back to main activity
    public void backButton(View view)
    {
        mPlayer3.stop();
        mPlayer3.release();
        Intent myIntent = new Intent(CalmingMode3Activity.this, CalmingActivity.class);
        startActivity(myIntent);
    }
}
