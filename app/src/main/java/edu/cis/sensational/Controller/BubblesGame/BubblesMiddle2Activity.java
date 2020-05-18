package edu.cis.sensational.Controller.BubblesGame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import edu.cis.sensational.Model.BubblesGame.BubbleConstants;
import edu.cis.sensational.R;

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
    private int numTimes;
    private int numberCorrect;
    private int score;
    private int roundNumber;
    private boolean canProceed;

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

        roundNumber = getIntent().getIntExtra(BubbleConstants.ROUND_NUM, roundNumber);
        numBubbles = getIntent().getIntExtra(BubbleConstants.NUM_BUBBLES, BubbleConstants.DEFAULT);
        colorsPicked = getIntent().getStringArrayListExtra(BubbleConstants.COLORS_PICKED);
        score = getIntent().getIntExtra(BubbleConstants.SCORE, BubbleConstants.DEFAULT);

        canProceed = false;

        allColors = new ArrayList<>();
        allColors.add(BubbleConstants.BLACK);
        allColors.add(BubbleConstants.BLUE);
        allColors.add(BubbleConstants.GREEN);
        allColors.add(BubbleConstants.RED);
        allColors.add(BubbleConstants.YELLOW);
        allColors.add(BubbleConstants.PINK);
        allColors.add(BubbleConstants.PURPLE);

        encouragingMessages = new ArrayList<>();
        encouragingMessages.add("So close!");
        encouragingMessages.add("Nice try!");
        encouragingMessages.add("Keep trying!");

        nextRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canProceed) {
                    if (roundNumber != BubbleConstants.MAX_ROUNDS) {
                        Intent intent = new Intent(BubblesMiddle2Activity.this,
                                BubblesMiddleActivity.class);
                        intent.putExtra(BubbleConstants.SCORE, score);
                        intent.putExtra(BubbleConstants.NUM_BUBBLES, numBubbles);
                        intent.putExtra(BubbleConstants.ROUND_NUM, roundNumber);
                        intent.putExtra(BubbleConstants.FIRST_TIME, false);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(BubblesMiddle2Activity.this,
                                BubblesEndActivity.class);
                        intent.putExtra(BubbleConstants.SCORE, score);
                        startActivity(intent);
                    }
                    canProceed = false;
                }
            }
        });

        if (score == 1)
        {
            smiley1.setVisibility(View.VISIBLE);
        }
        else if (score == 2)
        {
            smiley1.setVisibility(View.VISIBLE);
            smiley2.setVisibility(View.VISIBLE);
        }
        numTimes = 1;
        randomizeOptions(numTimes - 1);
    }

    public void randomizeOptions(int index)
    {
        ArrayList<String> options = new ArrayList<>();

        // Add correct answer to options ArrayList
        String correctAnswer = colorsPicked.get(index);
        options.add(correctAnswer);
        allColors.remove(correctAnswer);

        // Add two other random colors to options ArrayList
        Random random = new Random();
        String color2 = allColors.get(random.nextInt(6));
        options.add(color2);
        allColors.remove(color2);
        String color3 = allColors.get(random.nextInt(5));
        options.add(color3);

        // Reset allColors ArrayList
        allColors.add(correctAnswer);
        allColors.add(color2);

        // Randomly decide which button has which color
        index = random.nextInt(3);
        String answer1 = options.get(index);
        options.remove(index);
        String imageName = answer1 + BubbleConstants.BUBBLE;

        // https://stackoverflow.com/questions/15545753/random-genaration-of-image-from-drawable-folder-in-android
        int imageID = getResources().getIdentifier(imageName,
                                                   BubbleConstants.DRAWABLE,
                                                   getPackageName());

        option1.setBackgroundResource(imageID);
        option1.setText(answer1);
        if (answer1.equals(correctAnswer))
        {
            setUpCorrectButton(option1);
        }
        else
        {
            setUpWrongButton(option1);
        }

        index = random.nextInt(2);
        String answer2 = options.get(index);
        options.remove(index);
        imageName = answer2 + BubbleConstants.BUBBLE;
        imageID = getResources().getIdentifier(imageName,
                                               BubbleConstants.DRAWABLE,
                                               getPackageName());
        option2.setBackgroundResource(imageID);
        option2.setText(answer2);
        if (answer2.equals(correctAnswer))
        {
            setUpCorrectButton(option2);
        }
        else
        {
            setUpWrongButton(option2);
        }

        String answer3 = options.get(0);
        imageName = answer3 + BubbleConstants.BUBBLE;
        imageID = getResources().getIdentifier(imageName,
                                               BubbleConstants.DRAWABLE,
                                               getPackageName());
        option3.setBackgroundResource(imageID);
        option3.setText(answer3);
        if (answer3.equals(correctAnswer))
        {
            setUpCorrectButton(option3);
        }
        else
        {
            setUpWrongButton(option3);
        }
    }

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
                    // // https://freesound.org/people/KevinVG207/sounds/331912/

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

    public void checkEnd()
    {
        if (numTimes > numBubbles)
        {
            if (numberCorrect == numBubbles)
            {
                score++;
            }
            roundNumber++;
            if (roundNumber == BubbleConstants.MAX_ROUNDS){
                nextRound.setText(BubbleConstants.END_PAGE);
            }
            canProceed = true;
            nextRound.setVisibility(View.VISIBLE);
        }
        else
        {
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
            randomizeOptions(numTimes - 1);
        }
    }
}


