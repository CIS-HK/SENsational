package edu.cis.sensational.Controller.BubblesGame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import edu.cis.sensational.Model.BubblesGame.BubbleConstants;
import edu.cis.sensational.R;

/**
 * Displays a sequence of bubbles with different colors floating to the top of the screen. Users
 * need to try to remember the colors of each bubble.
 */

public class BubblesMiddleActivity extends AppCompatActivity
{
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

    /**
     * Creates and displays components on the screen (such as ImageViews) and
     * displays the user's score so far.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubbles_middle);

        // Set up ImageViews
        bubble = findViewById(R.id.floatingBubble);
        bubbleNumber = findViewById(R.id.number);
        smiley1 = findViewById(R.id.smiley1);
        smiley2 = findViewById(R.id.smiley2);

        // Get whether or not this is the first time starting the screen
        firstTime = getIntent().getBooleanExtra(BubbleConstants.FIRST_TIME, false);

        if (firstTime)
        {
            // Gets the mode of the game
            mode = getIntent().getStringExtra(BubbleConstants.MODE);

            if (mode != null && mode.equals(BubbleConstants.EASY))
            {
                // Sets the number of bubbles to 3 if the mode is easy
                numBubbles = BubbleConstants.NUM_BUBBLES_EASY;
            }
            if (mode != null && mode.equals(BubbleConstants.MED))
            {
                // Sets the number of bubbles to 4 if the mode is medium
                numBubbles = BubbleConstants.NUM_BUBBLES_MED;
            }
            if (mode != null && mode.equals(BubbleConstants.HARD))
            {
                // Sets the number of bubbles to 5 if the mode is hard
                numBubbles = BubbleConstants.NUM_BUBBLES_HARD;
            }

            // Sets the number of rounds that have already occurred to 0
            roundNumber = BubbleConstants.DEFAULT;

            // Sets the score to 0
            score = BubbleConstants.DEFAULT;
        }
        else
        {
            // Gets the number of bubbles to display
            numBubbles = getIntent().getIntExtra(BubbleConstants.NUM_BUBBLES,
                                                 BubbleConstants.DEFAULT);

            // Gets the user's score
            score = getIntent().getIntExtra(BubbleConstants.SCORE, BubbleConstants.DEFAULT);

            // Gets the number of rounds that have already occurred
            roundNumber = getIntent().getIntExtra(BubbleConstants.ROUND_NUM,
                                                  BubbleConstants.DEFAULT);

            // Displays the user's score so far
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

    /**
     * Randomly generates a color for the bubble that will be displayed on the screen.
     */
    public void generateRandomSequence()
    {
        Random random = new Random();

        // Selects a random color from allColors
        String colorBubble = allColors.get(random.nextInt(allColors.size()));
        colorsPicked.add(colorBubble);
        allColors.remove(colorBubble);
        String imageName = colorBubble + BubbleConstants.BUBBLE;

        // Sets the bubble ImageView's background to the appropriate PNG file (to change its color)
        // https://stackoverflow.com/questions/15545753/random-genaration-of-image-from-drawable-folder-in-android
        int imageID = getResources().getIdentifier(imageName,
                                                   BubbleConstants.DRAWABLE,
                                                   getPackageName());
        bubble.setImageResource(imageID);
    }

    /**
     * Gets the dimensions of the screen and calls on the playBubble method to display the bubble
     * floating up.
     */
    public void setUpBubbles()
    {
        // https://www.youtube.com/watch?v=UxbJKNjQWD8
        // Gets the size of the screen
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point sizeOfScreen = new Point();
        display.getSize(sizeOfScreen);

        // Stores the height of the screen
        heightOfScreen = sizeOfScreen.y;

        playBubble();
    }

    /**
     * Displays the bubble ImageView floating on the screen at a certain speed. Makes use of the
     * changeBubbleCoordinates method to do so.
     */
    public void playBubble()
    {
        // https://www.youtube.com/watch?v=UxbJKNjQWD8
        timer = new Timer();
        handler = new Handler();

        // Moves the bubble to the bottom of the screen
        bubbleY = heightOfScreen + BubbleConstants.ADD_TO_SCREEN_HEIGHT;
        bubble.setY(bubbleY);

        // Moves the number on the bubble so that it's at the center of the bubble
        bubbleNumber.setY(bubbleY + BubbleConstants.ADD_TO_Y);

        // Moves the bubble towards the top of the screen
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

    /**
     * Changes the y-coordinate of the bubble ImageView and the TextView displaying the bubble
     * number. Checks when to display the next bubble or, once all the bubbles have been displayed,
     * when to move on to the next activity.
     */
    public void changeBubbleCoordinates()
    {
        // https://www.youtube.com/watch?v=UxbJKNjQWD8
        // Reduces the bubble's y-coordinate value to move it upwards
        bubbleY -= BubbleConstants.MINUS_FROM_Y;

        // Checks whether the bubble has reached the top of the screen
        if (bubble.getY() + bubble.getHeight() < 0)
        {
            // If the bubble has reached the top, the timer is cancelled and the bubble stops moving
            timer.cancel();

            // If more bubbles need to be displayed, another bubble is displayed floating up
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
                // Proceeds to BubblesMiddle2Activity (where user can select the color of each bubble)
                Intent intent = new Intent(BubblesMiddleActivity.this,
                                            BubblesMiddle2Activity.class);
                intent.putExtra(BubbleConstants.NUM_BUBBLES, numBubbles);
                intent.putExtra(BubbleConstants.COLORS_PICKED, colorsPicked);
                intent.putExtra(BubbleConstants.SCORE, score);
                intent.putExtra(BubbleConstants.ROUND_NUM, roundNumber);
                startActivity(intent);
            }
        }

        // Updates the y-coordinates of the bubble and the number on the bubble
        bubble.setY(bubbleY);
        bubbleNumber.setY(bubbleY + BubbleConstants.ADD_TO_Y);
    }
}

