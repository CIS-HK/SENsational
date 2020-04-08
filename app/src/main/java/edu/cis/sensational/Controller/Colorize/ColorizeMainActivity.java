package edu.cis.sensational.Controller.Colorize;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import edu.cis.sensational.Model.Colorize.GameConstants;
import edu.cis.sensational.R;


public class ColorizeMainActivity extends AppCompatActivity {

    Button answerOne, answerTwo, quitButton;
    TextView timeLabel, scoreLabel, colorWord;
    ImageView backgroundColor;
    ArrayList<String> colorWords;
    ArrayList<Integer> colors;
    int color;
    HashMap<Integer, String> answers;
    String word, correctAnswer, wrongAnswer;
    long timeLeft;
    CountDownTimer counter = new CountDownTimer(5000,1000)
    {
        @Override
        public void onTick(long millisUntilFinished) {
            timeLeft = millisUntilFinished/1000;
            timeLabel.setText("Time: " + timeLeft);
        }

        @Override
        public void onFinish() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorize_main);
        answerOne = findViewById(R.id.answerOne);
        answerTwo = findViewById(R.id.answerTwo);
        quitButton = findViewById(R.id.quitButton);
        timeLabel = findViewById(R.id.timeLabel);
        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        colorWord = findViewById(R.id.colorWord);
        backgroundColor = findViewById(R.id.backgroundColor);
        colorWords = new ArrayList<String>();

        answers = new HashMap<Integer, String>();
        colors = new ArrayList<Integer>();



        setUpButtons();
        addColors();
        setUpGame();
        play();
    }

    public void setUpButtons()
    {
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ColorizeMainActivity.this,ColorizeEndActivity.class));
            }
        });

    }

    private void addColors()
    {
        //change to constants
        answers.put(Color.RED, "RED");
        answers.put(Color.YELLOW, "YELLOW");
        answers.put(Color.GREEN, "GREEN");
        answers.put(Color.BLACK, "BLACK");
        answers.put(Color.GRAY, "GRAY");
        answers.put(Color.MAGENTA, "PINK");
        answers.put(Color.BLUE, "BLUE");

        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.MAGENTA);
        colors.add(Color.GRAY);
        colors.add(Color.BLACK);

        colorWords.add("RED");
        colorWords.add("YELLOW");
        colorWords.add("GREEN");
        colorWords.add("BLACK");
        colorWords.add("GRAY");
        colorWords.add("PINK");
        colorWords.add("BLUE");

    }


    private void setUpGame()
    {

        counter.start();
        //generate random index from colorarraylist
        int index = new Random().nextInt(colorWords.size());
        word = colorWords.get(index);
        int colorIndex = new Random().nextInt(colors.size());
        color = colors.get(colorIndex);

        //set text to random color word
        colorWord.setText(word);

        //set word to random color
        colorWord.setTextColor(color);

        //loop through colors to find corresponding int, then get value from hashmap
        for (int i = 0; i<= colors.size()-1; i ++)
        {
            if (color == colors.get(i))
            {
             correctAnswer = answers.get(color);
        }
        }
// set random background color

        // set incorrect answer : need to generate another color arraylist
        ArrayList<String> updateArrayList = (ArrayList<String>) colorWords.clone();
        updateArrayList.remove(correctAnswer);
        int wrongIndex = new Random().nextInt(updateArrayList.size());
        updateArrayList.remove(wrongIndex);
        colorIndex = new Random().nextInt(updateArrayList.size());
        backgroundColor.setColorFilter(colorIndex);
        wrongAnswer = updateArrayList.get(wrongIndex);


        // set buttons randomly
        List<Button> buttons = Arrays.asList(answerOne, answerTwo);
        List<String> randomAnswers = Arrays.asList(correctAnswer, wrongAnswer);
        buttons.get(new Random().nextInt(2)).setText(randomAnswers.get(new Random().nextInt(1)));

    }


    private void play()
    {

        answerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeLeft < 0)
                {
                    startActivity(new Intent(ColorizeMainActivity.this,ColorizeEndActivity.class));
                    finish();
                }
                if (answerOne.getText() == correctAnswer && timeLeft >0 ){
                    counter.cancel();
                    GameConstants.score ++;
                    scoreLabel.setText("Score: " + GameConstants.score);
                    setUpGame();
                }

                else
                    {
                    startActivity(new Intent(ColorizeMainActivity.this,ColorizeEndActivity.class));
                    finish();
                    }
            }
        });

        answerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answerTwo.getText() == correctAnswer && timeLeft >0)
                {
                    GameConstants.score ++;
                    scoreLabel.setText("Score: " + GameConstants.score);
                    counter.cancel();
                    setUpGame();
                }
                else
                {
                    startActivity(new Intent(ColorizeMainActivity.this,ColorizeEndActivity.class));
                    finish();

                }
            }
        });


    }

}
