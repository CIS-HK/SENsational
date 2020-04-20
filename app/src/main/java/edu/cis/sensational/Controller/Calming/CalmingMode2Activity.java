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

import java.util.Timer;
import java.util.TimerTask;

import edu.cis.sensational.R;

public class CalmingMode2Activity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Setting up the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calming_mode2);

        //Initialising the circle and text
        circle = findViewById(R.id.circle2);
        breathe = findViewById(R.id.breatheTextView2);
        number = findViewById(R.id.numberTextView2);

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
        text();
        number();

        //Getting the song MP3 from the raw file and setting it to the media player
        mPlayer2 = MediaPlayer.create(this, R.raw.calming_music);

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

    //Button to go back to main activity
    public void backButton(View view)
    {
        Intent myIntent = new Intent(CalmingMode2Activity.this, CalmingActivity.class);
        startActivity(myIntent);
    }

}
