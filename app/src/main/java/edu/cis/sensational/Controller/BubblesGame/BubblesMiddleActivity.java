package edu.cis.sensational.Controller.BubblesGame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import edu.cis.sensational.Model.BubblesGame.BubbleConstants;
import edu.cis.sensational.R;

public class BubblesMiddleActivity extends AppCompatActivity
{
    private Button settings;
    private String mode;
    private int numBubbles;
    private ImageView bubble;
    private TextView bubbleNumber;
    private ImageView smiley1;
    private ImageView smiley2;
    private int heightOfScreen;
    private float bubbleY;
    private Handler handler;
    private Timer timer;
    private int numTimes;
    private ArrayList<String> colorsPicked;
    private ArrayList<String> allColors;
    private Boolean firstTime;
    private int score;
    private int roundNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubbles_middle);

        // Set up buttons and ImageView
        settings = findViewById(R.id.settings);
        bubble = findViewById(R.id.floatingBubble);
        bubbleNumber = findViewById(R.id.number);
        smiley1 = findViewById(R.id.smiley1);
        smiley2 = findViewById(R.id.smiley2);

        // Set up mode (if this is the first time starting the screen)
        firstTime = getIntent().getBooleanExtra(BubbleConstants.FIRST_TIME, false);
        if (firstTime)
        {
            mode = getIntent().getStringExtra(BubbleConstants.MODE);
            if (mode != null && mode.equals(BubbleConstants.EASY))
            {
                numBubbles = BubbleConstants.NUM_BUBBLES_EASY;
            }
            if (mode != null && mode.equals(BubbleConstants.MED))
            {
                numBubbles = BubbleConstants.NUM_BUBBLES_MED;
            }
            if (mode != null && mode.equals(BubbleConstants.HARD))
            {
                numBubbles = BubbleConstants.NUM_BUBBLES_HARD;
            }
            roundNumber = BubbleConstants.DEFAULT;
            score = BubbleConstants.DEFAULT;
        }
        else
        {
            numBubbles = getIntent().getIntExtra(BubbleConstants.NUM_BUBBLES,
                                                 BubbleConstants.DEFAULT);
            score = getIntent().getIntExtra(BubbleConstants.SCORE, BubbleConstants.DEFAULT);
            roundNumber = getIntent().getIntExtra(BubbleConstants.ROUND_NUM,
                                                  BubbleConstants.DEFAULT);
            if (score == 1)
            {
                smiley1.setVisibility(View.VISIBLE);
            }
            else if (score == 2)
            {
                smiley1.setVisibility(View.VISIBLE);
                smiley2.setVisibility(View.VISIBLE);
            }
        }

        // Set up allColors ArrayList
        allColors = new ArrayList<>();
        allColors.add(BubbleConstants.BLACK);
        allColors.add(BubbleConstants.BLUE);
        allColors.add(BubbleConstants.GREEN);
        allColors.add(BubbleConstants.RED);
        allColors.add(BubbleConstants.YELLOW);
        allColors.add(BubbleConstants.PINK);
        allColors.add(BubbleConstants.PURPLE);

        colorsPicked = new ArrayList<>();
        numTimes = 1;
        generateRandomSequence();
        setUpBubbles();
    }

    public void generateRandomSequence()
    {
        Random random = new Random();
        String colorBubble = allColors.get(random.nextInt(7));
        colorsPicked.add(colorBubble);
        String imageName = colorBubble + BubbleConstants.BUBBLE;
        int imageID = getResources().getIdentifier(imageName,
                                                   BubbleConstants.DRAWABLE,
                                                   getPackageName());
        bubble.setImageResource(imageID);
    }

    public void setUpBubbles()
    {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point sizeOfScreen = new Point();
        display.getSize(sizeOfScreen);
        heightOfScreen = sizeOfScreen.y;

        playBubble();
    }

    public void playBubble()
    {
        timer = new Timer();
        handler = new Handler();
        bubbleY = heightOfScreen + BubbleConstants.ADD_TO_SCREEN_HEIGHT;
        bubbleNumber.setY(bubbleY + BubbleConstants.ADD_TO_Y);
        bubble.setY(bubbleY);
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
                        changeBubbleCoordinates();
                    }
                });
            }
        }, BubbleConstants.DELAY, BubbleConstants.PERIOD);
    }

    public void changeBubbleCoordinates()
    {
        bubbleY -= BubbleConstants.MINUS_FROM_Y;
        if (bubble.getY() + bubble.getHeight() < 0)
        {
            timer.cancel();
            if (numTimes < numBubbles)
            {
                generateRandomSequence();
                numTimes++;
                String newNumber = BubbleConstants.EMPTY_STR + numTimes;
                bubbleNumber.setText(newNumber);
                playBubble();
            }
            else
            {
                Intent intent = new Intent(BubblesMiddleActivity.this,
                                            BubblesMiddle2Activity.class);
                intent.putExtra(BubbleConstants.NUM_BUBBLES, numBubbles);
                intent.putExtra(BubbleConstants.COLORS_PICKED, colorsPicked);
                intent.putExtra(BubbleConstants.ALL_COLORS, allColors);
                intent.putExtra(BubbleConstants.SCORE, score);
                intent.putExtra(BubbleConstants.ROUND_NUM, roundNumber);
                startActivity(intent);
            }
        }
        bubble.setY(bubbleY);
        bubbleNumber.setY(bubbleY + BubbleConstants.ADD_TO_Y);
    }
}
