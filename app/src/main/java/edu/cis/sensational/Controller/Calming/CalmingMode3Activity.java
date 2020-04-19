package edu.cis.sensational.Controller.Calming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import edu.cis.sensational.R;

public class CalmingMode3Activity extends AppCompatActivity
{
    //Declaring the images on the GUI
    private ImageView fish1;
    private ImageView fish2;
    private ImageView fish3;
    private ImageView fish4;
    private ImageView fish5;
    private ImageView bubbles1;
    private ImageView bubbles2;

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

        fish1X = -500.0f;
        fish1Y = 700;

        fish2X = 200;
        fish2Y = 100;



        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        goRight();
                        goLeft();
                    }
                });
            }
        },0,40);
    }


    public void goRight()
    {
        fish1X += 10;
        fish2X +=10;

        if(fish1.getX() > screenWidth)
        {
            fish1X = -500.0f;
            fish1Y = 700;
        }
        fish1.setX(fish1X);
        fish1.setY(fish1Y);

        if(fish2.getX() > screenWidth)
        {
            fish2X = -500.0f;
            fish2Y = 100;
        }

        fish2.setX(fish2X);
        fish2.setY(fish2Y);
    }

    public void goLeft()
    {

    }


    //Method to pause movement on screen
    public void pauseButton(View view)
    {
        //Checking if screen is paused
        if(pause == false) {
            pause = true;
            timer.cancel();
            timer = null;
        }
        else
        {
            pause = false;
            //Creating and starting new timer if button is pressed and screen is paused
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            goRight();
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
