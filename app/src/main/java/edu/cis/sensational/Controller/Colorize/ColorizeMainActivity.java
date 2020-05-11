package edu.cis.sensational.Controller.Colorize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.cis.sensational.Model.Colorize.GameConstants;
import edu.cis.sensational.R;


public class ColorizeMainActivity extends AppCompatActivity
{

    Button answerOne, answerTwo, quitButton;
    TextView timeLabel, scoreLabel, colorWord;
    ImageView backgroundColor;
    ArrayList<String> colorWords;
    ArrayList<Integer> colorInts;
    HashMap<Integer, String> answers;
    String word, correctAnswer, wrongAnswer;
    long timeLeft;
    int color, backColor, colorIndex;

    CountDownTimer counter = new CountDownTimer(GameConstants.TIME,1000)
    {
        @Override
        public void onTick(long millisUntilFinished) {
            timeLeft = millisUntilFinished/1000;
            timeLabel.setText("" +timeLeft);
        }

        @Override
        public void onFinish()
        {
            finish();
            startActivity(new Intent(ColorizeMainActivity.this,ColorizeEndActivity.class));

            Context context = getApplicationContext();
            CharSequence text = GameConstants.TOAST;
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

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
        colorInts = new ArrayList<Integer>();

        setUpButtons();
        addColors();
        setUpGame();
        play();
    }

    public void setUpButtons()
    {
        quitButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                counter.cancel();
                startActivity(new Intent(ColorizeMainActivity.this,ColorizeEndActivity.class));

            }
        });

    }

    //add colors to respective arraylists
    private void addColors()
    {
        answers.clear();
        colorInts.clear();
        colorWords.clear();

        //Hashmap<Integer, String> to define the corresponding correct answers
        answers.put(Color.RED, GameConstants.RED);
        answers.put(Color.YELLOW, GameConstants.YELLOW);
        answers.put(Color.GREEN, GameConstants.GREEN);
        answers.put(Color.BLACK, GameConstants.BLACK);
        answers.put(Color.GRAY, GameConstants.GRAY);
        answers.put(Color.MAGENTA, GameConstants.PINK);
        answers.put(Color.BLUE, GameConstants.BLUE);

        //Arraylist<Integer> containing android's predefined color constants
        colorInts.add(Color.RED);
        colorInts.add(Color.YELLOW);
        colorInts.add(Color.GREEN);
        colorInts.add(Color.BLACK);
        colorInts.add(Color.GRAY);
        colorInts.add(Color.MAGENTA);
        colorInts.add(Color.BLUE);

        //Arraylist<String> containing all the words that will be displayed on UI
        colorWords.add(GameConstants.RED);
        colorWords.add(GameConstants.YELLOW);
        colorWords.add(GameConstants.GREEN);
        colorWords.add(GameConstants.BLACK);
        colorWords.add(GameConstants.GRAY);
        colorWords.add(GameConstants.PINK);
        colorWords.add(GameConstants.BLUE);
    }


    private void setUpGame()
    {
        // start time
        counter.start();

        //generate random word
        int index = new Random().nextInt(colorWords.size());
        word = colorWords.get(index);

        //generate random color for word
        colorIndex = new Random().nextInt(colorInts.size());
        color = colorInts.get(colorIndex);

        //set text to random word
        colorWord.setText(word);

        //set word to random color
        colorWord.setTextColor(color);

        //loop through colors to find corresponding int, then get value from hashmap for the correct answer
        for (int i = 0; i<= colorInts.size()-1; i ++)
        {
            if (color == colorInts.get(i))
            {
                correctAnswer = answers.get(color);
            }
        }

        // set incorrect answer
        colorWords.remove(correctAnswer);
        int wrongIndex = new Random().nextInt(colorWords.size());
        wrongAnswer = colorWords.get(wrongIndex);

        setBackgroundColor();

        ArrayList<String> randomAnswers = new ArrayList<>();
        randomAnswers.add(correctAnswer);
        randomAnswers.add(wrongAnswer);

        String answer = randomAnswers.get(new Random().nextInt(2));
        answerOne.setText(answer);
        randomAnswers.remove(answer);
        answerTwo.setText(randomAnswers.get(0));
    }

    private void setBackgroundColor()
    {
        if (!GameConstants.BACKGROUND)
        {
            backgroundColor.setBackgroundColor(Color.TRANSPARENT);
        }

        if (GameConstants.BACKGROUND = true)
        {
            // background color cannot be the same as color of word
            colorInts.remove(colorIndex);
            int backgroundIndex = new Random().nextInt(colorInts.size());
            backColor = colorInts.get(backgroundIndex);
            backgroundColor.setBackgroundColor(backColor);
        }
    }

    private void play()
    {
        answerOne.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                counter.cancel();
                if (answerOne.getText() == correctAnswer && timeLeft > 0 )
                {
                    counter.cancel();
                    GameConstants.SCORE ++;
                    scoreLabel.setText(""+ GameConstants.SCORE);
                    addColors();
                    setUpGame();
                }

                else
                {
                    startActivity(new Intent(ColorizeMainActivity.this,ColorizeEndActivity.class));
                    finish();
                }
            }
        });

        answerTwo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                counter.cancel();
                if (answerTwo.getText() == correctAnswer && timeLeft > 0)
                {

                    GameConstants.SCORE++;
                    scoreLabel.setText(""+ GameConstants.SCORE);
                    counter.cancel();
                    addColors();
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
