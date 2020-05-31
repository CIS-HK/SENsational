package edu.cis.sensational.Controller.BubblesGame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;
import edu.cis.sensational.Model.BubblesGame.BubbleConstants;
import edu.cis.sensational.Model.Utils.BubblesMethods;
import edu.cis.sensational.R;

/**
 * Asks users to identify the color of each bubble in the sequence. Users can select from three
 * answer choices displayed on the screen.
 */

public class BubblesMiddle2Activity extends AppCompatActivity
{
    private Button option1;
    private Button option2;
    private Button option3;
    private Button nextRound;
    private ImageView checkOrCross;
    private ImageView smiley1;
    private ImageView smiley2;
    private TextView whichBubble;
    public TextView message;
    private int numBubbles;
    private ArrayList<String> colorsPicked;
    private ArrayList<String> allColors;
    private ArrayList<String> encouragingMessages;
    private ArrayList<String> options;
    private int numTimes;
    private int numberCorrect;
    private int score;
    private int roundNumber;
    private boolean canProceed;

    /**
     * Creates and displays components on the screen (such as ImageViews and buttons) and displays
     * the user's score so far.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble_middle2);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        smiley1 = findViewById(R.id.smiley1_2);
        smiley2 = findViewById(R.id.smiley2_2);
        checkOrCross = findViewById(R.id.checkOrCross);
        whichBubble = findViewById(R.id.whichBubble);
        message = findViewById(R.id.soClose);
        nextRound = findViewById(R.id.nextRound);
        message.setVisibility(View.GONE);
        nextRound.setVisibility(View.GONE);

        // Gets data from previous screen
        roundNumber = getIntent().getIntExtra(BubbleConstants.ROUND_NUM, roundNumber);
        numBubbles = getIntent().getIntExtra(BubbleConstants.NUM_BUBBLES, BubbleConstants.DEFAULT);
        colorsPicked = getIntent().getStringArrayListExtra(BubbleConstants.COLORS_PICKED);
        score = getIntent().getIntExtra(BubbleConstants.SCORE, BubbleConstants.DEFAULT);

        canProceed = false;

        // Creates allColors ArrayList (with all possible colors that the bubble can be)
        BubblesMethods bubblesMethods = new BubblesMethods();
        allColors = bubblesMethods.setUpAllColors();

        // ArrayList with encouraging messages to display if user selects incorrect answer
        encouragingMessages = new ArrayList<>();
        encouragingMessages.add(BubbleConstants.SOCLOSE);
        encouragingMessages.add(BubbleConstants.NICETRY);
        encouragingMessages.add(BubbleConstants.KEEPTRYING);

        // Sets up the 'next round' button
        nextRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canProceed) {
                    if (roundNumber != BubbleConstants.MAX_ROUNDS) {
                        /* If the user needs to complete more rounds,
                           it transitions to BubblesMiddleActivity */
                        Intent intent = new Intent(BubblesMiddle2Activity.this,
                                BubblesMiddleActivity.class);
                        intent.putExtra(BubbleConstants.SCORE, score);
                        intent.putExtra(BubbleConstants.NUM_BUBBLES, numBubbles);
                        intent.putExtra(BubbleConstants.ROUND_NUM, roundNumber);
                        intent.putExtra(BubbleConstants.FIRST_TIME, false);
                        startActivity(intent);
                    }
                    else {
                        // Proceeds to BubblesEndActivity if the user has completed all rounds
                        Intent intent = new Intent(BubblesMiddle2Activity.this,
                                BubblesEndActivity.class);
                        intent.putExtra(BubbleConstants.SCORE, score);
                        startActivity(intent);
                    }
                    canProceed = false;
                }
            }
        });

        // Displays the user's score so far
        bubblesMethods.displayScore(score, smiley1, smiley2);

        numTimes = 1;
        randomizeOptions(numTimes - 1);
    }

    /**
     * Randomly selects three answer options that the user can choose from. One of them is the
     * correct answer. Makes use of the selectColor and setUpAnswerOption methods.
     * @param index the index in the colorsPicked ArrayList that stores the correct color
     *              of the bubble.
     */
    public void randomizeOptions(int index)
    {
        options = new ArrayList<>();

        // Adds correct answer to options ArrayList
        String correctAnswer = selectColor(index, colorsPicked);

        // Adds two other random colors to options ArrayList
        Random random = new Random();
        String color2 = selectColor(random.nextInt(6), allColors);
        String color3 = selectColor(random.nextInt(5), allColors);

        // Resets allColors ArrayList
        allColors.add(correctAnswer);
        allColors.add(color2);
        allColors.add(color3);

        // Randomly decides which button has which color and set up these buttons
        index = random.nextInt(3);
        setUpAnswerOption(index, correctAnswer, option1);

        index = random.nextInt(2);
        setUpAnswerOption(index, correctAnswer, option2);

        index = 0;
        setUpAnswerOption(index, correctAnswer, option3);
    }

    /**
     * Selects a color for the answer option from the ArrayList specified and ensures that none of
     * the answer options show the same color.
     * @param index - the index that the color of the answer option is stored at in the ArrayList
     * @param colorsArrayList - the ArrayList to retrieve the color from
     * @return the color of the answer option (as a String)
     */
    public String selectColor(int index, ArrayList<String> colorsArrayList)
    {
        String answer = colorsArrayList.get(index);
        options.add(answer);

        // So that none of the answer options show the same color
        allColors.remove(answer);
        return answer;
    }

    /**
     * Sets up the answer button by changing its background to the PNG file with the appropriate
     * color. Makes use of the setUpCorrectButton and setUpWrongButton methods so that the answer
     * button completes the appropriate actions when clicked on.
     * @param index - the index that the color of the answer option is stored at in the options
     *                ArrayList
     * @param correctAnswer - the correct color of the bubble
     * @param option - the answer button that will be set up
     */
    public void setUpAnswerOption(int index, String correctAnswer, Button option){
        // Randomly picks a color from the options ArrayList for the answer button
        String answer = options.get(index);

        // So that none of the options show the same color
        options.remove(index);

        String imageName = answer + BubbleConstants.BUBBLE;

        // https://stackoverflow.com/questions/15545753/random-genaration-of-image-from-drawable-folder-in-android
        int imageID = getResources().getIdentifier(imageName,
                BubbleConstants.DRAWABLE,
                getPackageName());

        //Sets the option button's background to the appropriate PNG file and updates its text
        option.setBackgroundResource(imageID);
        option.setText(answer);

        // Sets the option's onClickListener depending on whether it shows the correct answer
        if (answer.equals(correctAnswer))
        {
            setUpCorrectButton(option);
        }
        else
        {
            setUpWrongButton(option);
        }
    }

    /**
     * Sets up the button that displays the correct answer so that it completes the right actions
     * when pressed.
     * @param button the button that displays the correct answer
     */
    public void setUpCorrectButton(Button button)
    {
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /* canProceed is true when users have answered all questions and should proceed to
                the next screen */
                if (!canProceed) {
                    /* Ensures that users can't answer the last question multiple times to get it
                    correct before pressing the nextRound button */

                    // If an encouraging message is displayed from before, stop displaying it
                    message.setVisibility(View.GONE);

                    // Displays the check image on the screen
                    checkOrCross.setImageResource(R.drawable.check);
                    checkOrCross.setVisibility(View.VISIBLE);

                    /* Updates the user's score and how many times the user has selected the color
                    of a bubble in the sequence */
                    numTimes++;
                    numberCorrect++;

                    // Plays a sound effect
                    // https://stackoverflow.com/questions/10451092/how-to-play-a-sound-effect-in-android
                    //https://freesound.org/people/LittleRainySeasons/sounds/335908/

                    MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.correct);
                    mPlayer.setVolume(BubbleConstants.VOLUME, BubbleConstants.VOLUME);
                    mPlayer.start();

                    // Checks if this is the end of the round
                    checkEnd();
                }
            }
        });

    }

    /**
     * Sets up the button that displays an incorrect answer so that it completes the right actions
     * when pressed.
     * @param button the button that displays a wrong answer
     */
    public void setUpWrongButton(Button button)
    {
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /* canProceed is true when users have answered all questions and should proceed to
                the next screen */
                if (! canProceed) {
                    /* Ensures that users can't answer the last question multiple times to get it
                    correct before pressing the nextRound button */

                    // Displays the cross image on the screen
                    checkOrCross.setImageResource(R.drawable.cross);
                    checkOrCross.setVisibility(View.VISIBLE);

                    // Displays a random encouraging message
                    Random random = new Random();
                    message.setText(encouragingMessages.get(random.nextInt(3)));
                    message.setVisibility(View.VISIBLE);

                    /* Updates the number of times the user has selected the color of a bubble in
                    the sequence */
                    numTimes++;

                    // Plays a sound effect
                    // https://stackoverflow.com/questions/10451092/how-to-play-a-sound-effect-in-android
                    // https://freesound.org/people/KevinVG207/sounds/331912/

                    MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(),
                                                              R.raw.incorrect);
                    mPlayer.setVolume(BubbleConstants.VOLUME, BubbleConstants.VOLUME);
                    mPlayer.start();

                    // Checks if this is the end of the round
                    checkEnd();
                }
            }
        });
    }

    /**
     * Checks whether the user has identified the color of each bubble in the sequence. Transitions
     * to the end screen or back to BubblesMiddleActivity depending on the number of rounds that
     * the user has completed.
     */
    public void checkEnd()
    {
        if (numTimes > numBubbles)
        // True when the user has been asked to select the color of all bubbles in the sequence
        {
            if (numberCorrect == numBubbles)
            {
                // Increases the user's score if they got all the questions correct
                score++;
            }
            // Increases the number of rounds the user has completed so far
            roundNumber++;

            // When the user has completed all the rounds:
            if (roundNumber == BubbleConstants.MAX_ROUNDS){
                // The 'next round' button tells the user to proceed to the end screen
                nextRound.setText(BubbleConstants.END_PAGE);
            }
            /*
            If users can proceed to the next round, canProceed is set to true and the 'next round'
            button becomes visible
             */
            canProceed = true;
            nextRound.setVisibility(View.VISIBLE);
        }
        else
        {
            // Ask the user what the color of the next bubble in the sequence is:
            if (numTimes == 2)
            {
                whichBubble.setText(BubbleConstants.SECOND);
            }
            else if (numTimes == 3)
            {
                whichBubble.setText(BubbleConstants.THIRD);
            }
            else if (numTimes == 4)
            {
                whichBubble.setText(BubbleConstants.FOURTH);
            }
            else if (numTimes == 5)
            {
                whichBubble.setText(BubbleConstants.FIFTH);
            }

            // Generate new answer options
            randomizeOptions(numTimes - 1);
        }
    }
}


