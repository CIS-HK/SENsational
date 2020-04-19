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

    //Getting the height and width of the screen
    private float screenWidth;
    private float screenHeight;

    //Creating things needed like Timer and handler for images to move on screen, and pause boolean
    //for the pause button
    private boolean pause = false;
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calming_mode3);

        fish1 = findViewById(R.id.fish1);
        fish2 = findViewById(R.id.fish2);
        fish3 = findViewById(R.id.fish3);
        fish4 = findViewById(R.id.fish4);
        fish5 = findViewById(R.id.fish5);
        bubbles1 = findViewById(R.id.bubbles_m_1);
        bubbles2 = findViewById(R.id.bubbles_m_2);
        circle = findViewById(R.id.circle3);
        breathe = findViewById(R.id.breatheTextView3);
        number = findViewById(R.id.numberTextView3);

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

        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

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

    public void sizeControl()
    {
        //https://developer.android.com/reference/android/view/animation/AnimationSet.html
        //Docs for animation set

        final AnimationSet animSet = new AnimationSet(true);

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
        final String[] array1 = {"1", "2", "3", "4", "1", "2", "3", "4", "1", "2", "3", "4"};

        //Words to be shown in order on the screen for for seconds, then 6 seconds
        final String[] array2 = {"Breathe in", "Hold", "Breathe out"};

        number.post(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                number.setText(array1[i]);
                i++;
                if (i == 12) {
                    i = 0;
                }
                number.postDelayed(this, 1000);
                if (pause == true) {
                    i = 0;
                    number.setText(array1[i]);
                }
            }
        });
        breathe.post(new Runnable() {
            int x = 0;
            @Override
            public void run() {
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
                breathe.postDelayed(this, 4000);
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
            text();

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
                            goRight();
                            goLeft();
                            goUp();
                        }
                    });
                }
            },0,40);

        }
    }

    public void backButton(View view)
    {
        Intent myIntent = new Intent(CalmingMode3Activity.this, CalmingActivity.class);
        startActivity(myIntent);
    }


}
