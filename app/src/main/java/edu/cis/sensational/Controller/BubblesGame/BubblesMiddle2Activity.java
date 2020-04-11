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
import edu.cis.sensational.R;

public class BubblesMiddle2Activity extends AppCompatActivity {
    private Button settings;
    private Button option1;
    private Button option2;
    private Button option3;
    private ImageView checkOrCross;
    private ImageView smiley1;
    private ImageView smiley2;
    private TextView whichBubble;
    private int numBubbles;
    private ArrayList<String> colorsPicked;
    private ArrayList<String> allColors;
    private int numTimes;
    private int numberCorrect;
    private int score;
    private int roundNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble_middle2);

        settings = findViewById(R.id.settings2);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        smiley1 = findViewById(R.id.smiley1_2);
        smiley2 = findViewById(R.id.smiley2_2);
        checkOrCross = findViewById(R.id.checkOrCross);
        whichBubble = findViewById(R.id.whichBubble);

        roundNumber = getIntent().getIntExtra("roundNumber", roundNumber);
        numBubbles = getIntent().getIntExtra("numBubbles", 0);
        colorsPicked = getIntent().getStringArrayListExtra("colorsPicked");
        allColors = getIntent().getStringArrayListExtra("allColors");
        score = getIntent().getIntExtra("score", 0);

        if (score == 1){
            smiley1.setVisibility(View.VISIBLE);
        }
        else if (score == 2){
            smiley1.setVisibility(View.VISIBLE);
            smiley2.setVisibility(View.VISIBLE);
        }
        numTimes = 1;
        randomizeOptions(numTimes - 1);
    }

    public void randomizeOptions(int index){
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

        // Randomly decide which button has which color
        index = random.nextInt(3);
        String answer1 = options.get(index);
        options.remove(index);
        String imageName = answer1 + "bubble";
        int imageID = getResources().getIdentifier(imageName, "drawable", getPackageName());
        option1.setBackgroundResource(imageID);
        option1.setText(answer1);
        if (answer1.equals(correctAnswer)){
            setUpCorrectButton(option1);
        }
        else {
            setUpWrongButton(option1);
        }

        index = random.nextInt(2);
        String answer2 = options.get(index);
        options.remove(index);
        imageName = answer2 + "bubble";
        imageID = getResources().getIdentifier(imageName, "drawable", getPackageName());
        option2.setBackgroundResource(imageID);
        option2.setText(answer2);
        if (answer2.equals(correctAnswer)){
            setUpCorrectButton(option2);
        }
        else {
            setUpWrongButton(option2);
        }

        String answer3 = options.get(0);
        imageName = answer3 + "bubble";
        imageID = getResources().getIdentifier(imageName, "drawable", getPackageName());
        option3.setBackgroundResource(imageID);
        option3.setText(answer3);
        if (answer3.equals(correctAnswer)){
            setUpCorrectButton(option3);
        }
        else {
            setUpWrongButton(option3);
        }

        // Reset allColors ArrayList
        allColors.add(correctAnswer);
        allColors.add(color2);
        allColors.add(color3);
    }

    public void setUpCorrectButton(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOrCross.setImageResource(R.drawable.check);
                checkOrCross.setVisibility(View.VISIBLE);
                numTimes++;
                numberCorrect++;
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.correct);
                mediaPlayer.setVolume(20, 20);
                mediaPlayer.start();
                checkEnd();
            }
        });

    }

    public void setUpWrongButton(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOrCross.setImageResource(R.drawable.cross);
                numTimes++;
                checkOrCross.setVisibility(View.VISIBLE);
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.wrong);
                mediaPlayer.setVolume(20, 20);
                mediaPlayer.start();
                checkEnd();
            }
        });

    }
    public void checkEnd(){
        if (numTimes > numBubbles){
            if (numberCorrect == numBubbles){
                score++;
            }
            roundNumber++;
            numberCorrect = 0;
            if (roundNumber == 3){
                Intent intent = new Intent(BubblesMiddle2Activity.this, BubblesEndActivity.class);
                intent.putExtra("score", score);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(BubblesMiddle2Activity.this, BubblesMiddleActivity.class);
                intent.putExtra("score", score);
                intent.putExtra("numBubbles", numBubbles);
                intent.putExtra("roundNumber", roundNumber);
                intent.putExtra("firstTime", false);
                startActivity(intent);
            }
        }
        else {
            if (numTimes == 2){
                whichBubble.setText("second");
            }
            else if (numTimes == 3){
                whichBubble.setText("third");
            }
            else if (numTimes == 4){
                whichBubble.setText("fourth");
            }
            else if (numTimes == 5){
                whichBubble.setText("fifth");
            }
            randomizeOptions(numTimes - 1);
        }
    }

}
