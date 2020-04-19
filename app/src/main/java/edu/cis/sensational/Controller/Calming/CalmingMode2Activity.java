package edu.cis.sensational.Controller.Calming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
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

    //Getting the height and width of the screen
    private float screenWidth;
    private float screenHeight;

    //Creating things needed like Timer and handler for images to move on screen, and pause boolean
    //for the pause button
    private boolean pause = false;
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calming_mode2);
        circle = findViewById(R.id.circle2);
        breathe = findViewById(R.id.breatheTextView2);
        number = findViewById(R.id.numberTextView2);
        bf1 = findViewById(R.id.bf1);
        bf2 = findViewById(R.id.bf2);
        bf3 = findViewById(R.id.bf3);
        bf4 = findViewById(R.id.bf4);
        bf5 = findViewById(R.id.bf5);
        bf6 = findViewById(R.id.bf6);

        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        //Setting the location of the fish and bubbles on the screen

        bf1Y = 1600;

        bf2Y = 500;

        bf3Y = 1200;

        bf4Y = 100;

        bf5Y = 2000;

        bf6Y = 900;


        sizeControl();
        text();

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


    public void sizeControl()
    {
        //https://developer.android.com/reference/android/view/animation/AnimationSet.html
        //Docs for animation set

        final AnimationSet animSet = new AnimationSet(true);

        Animation grow = AnimationUtils.loadAnimation(this, R.anim.circleanimation2);
        grow.setDuration(4000);
        animSet.addAnimation(grow);

        Animation shrink = AnimationUtils.loadAnimation(this, R.anim.circleanimation);
        shrink.setDuration(6000);
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
        final String[] array1 = {"1", "2", "3", "4", "1", "2", "3", "4", "1", "2", "3", "4", "5", "6"};

        //Words to be shown in order on the screen for for seconds, then 6 seconds
        final String[] array2 = {"Breathe in", "Hold", "Breathe out"};

        number.post(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                number.setText(array1[i]);
                i++;
                if (i == 14) {
                    i = 0;
                }
                number.postDelayed(this, 1000);
                if (pause == true) {
                    number.setText("1");
                    i = 0;
                }
            }

        });
        breathe.post(new Runnable() {
            int x = 0;
            @Override
            public void run() {
                breathe.setText(array2[x]);
                x++;
                if (x == 3) {
                    x = 0;
                }
                if (x == 3 || x == 0) {
                    breathe.postDelayed(this, 6000);
                } else {
                    breathe.postDelayed(this, 4000);
                }
                if(pause == true)
                {
                    breathe.setText("Breathe in");
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
            pause = true;
            timer.cancel();
            timer = null;

            circle.clearAnimation();
            int height = (int)circle.getHeight();
            int width = (int)circle.getWidth();
            circle.setMaxHeight(height);
            circle.setMaxWidth(width);
        }
        else
        {
            pause = false;

            // Continuing the circle
            sizeControl();

            //Creating and starting new timer if button is pressed and screen is paused
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

    public void backButton(View view)
    {
        Intent myIntent = new Intent(CalmingMode2Activity.this, CalmingActivity.class);
        startActivity(myIntent);
    }

}
