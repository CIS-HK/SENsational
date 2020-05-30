package edu.cis.sensational.Controller.Colorize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import edu.cis.sensational.Model.Colorize.GameConstants;
import edu.cis.sensational.R;


public class ColorizeMainActivity extends AppCompatActivity
{

    Button answerOne, answerTwo, stopButton;
    TextView timeLabel, scoreLabel, colorWord;
    ImageView backgroundColor;
    ArrayList<String> colorWords;
    ArrayList<Integer> colorInts;
    HashMap<Integer, String> answers;
    String word, correctAnswer, wrongAnswer;
    long timeLeft;
    int color, backColor, colorIndex, seconds;

    CountDownTimer counter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorize_main);
        answerOne = findViewById(R.id.answerOne);
        answerTwo = findViewById(R.id.answerTwo);
        stopButton = findViewById(R.id.stopButton);
        timeLabel = findViewById(R.id.timeLabel);
        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        colorWord = findViewById(R.id.colorWord);
        backgroundColor = findViewById(R.id.backgroundColor);
        colorWords = new ArrayList<String>();
        answers = new HashMap<Integer, String>();
        colorInts = new ArrayList<Integer>();

        setUpButtons();
        setUpTimer();
        addColors();
        setUpGame();
        play();
    }

    private void setUpButtons()
    {
        stopButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                counter.cancel();
                startActivity(new Intent(ColorizeMainActivity.this,
                        ColorizeEndActivity.class));

            }
        });

    }

    private void setUpTimer()
    {
        getTime();
        counter = new CountDownTimer(GameConstants.TIME,GameConstants.INTERVAL)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                timeLeft = millisUntilFinished / GameConstants.INTERVAL;
                timeLabel.setText("" +timeLeft);
            }

            @Override
            public void onFinish()
            {
                finish();
                startActivity(new Intent(ColorizeMainActivity.this,
                        ColorizeEndActivity.class));

                //https://www.youtube.com/watch?v=e7sHAYYubJo
                TastyToast.makeText(getApplicationContext(), GameConstants.TOAST,
                        TastyToast.LENGTH_LONG, TastyToast.WARNING);
            }
        };
    }

    private void getTime()
    {
        //get the bundle that is passed on from start activity by String key
        if (getIntent().getExtras() != null)
        {
            Bundle bundle = getIntent().getExtras();
            seconds = bundle.getInt(GameConstants.TIMESTRING);
            GameConstants.TIME = seconds;
        }
        else
        {
            GameConstants.TIME = GameConstants.DEFAULTTIME;
        }
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

        //loop through colors to find corresponding int, then get value from hashmap for the
        // correct answer
        for (int i = 0; i <= colorInts.size() - 1; i ++)
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

        String answer = randomAnswers.get(new Random().nextInt(GameConstants.BOUND));
        answerOne.setText(answer);
        randomAnswers.remove(answer);
        answerTwo.setText(randomAnswers.get(GameConstants.ZERO));
    }

    private void setBackgroundColor()
    {
        //if user didn't select to have background, set background to white
        if (!GameConstants.BACKGROUND)
        {
            backgroundColor.setBackgroundColor(Color.WHITE);
        }

        //if user did select to have background, generate random background color
        else if (GameConstants.BACKGROUND)
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
                if (answerOne.getText() == correctAnswer && timeLeft > GameConstants.ZERO )
                {
                    counter.cancel();
                    GameConstants.SCORE ++;
                    scoreLabel.setText(""+ GameConstants.SCORE);
                    addColors();
                    setUpGame();
                }

                else
                {
                    TastyToast.makeText(getApplicationContext(), GameConstants.WRONGANSWER,
                            TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    startActivity(new Intent(ColorizeMainActivity.this,
                            ColorizeEndActivity.class));
                    finish();
                }
            }
        });

        answerTwo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                counter.cancel();
                if (answerTwo.getText() == correctAnswer && timeLeft > GameConstants.ZERO)
                {

                    GameConstants.SCORE++;
                    scoreLabel.setText(""+ GameConstants.SCORE);
                    counter.cancel();
                    addColors();
                    setUpGame();
                }
                else
                {
                    TastyToast.makeText(getApplicationContext(), GameConstants.WRONGANSWER,
                            TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    startActivity(new Intent(ColorizeMainActivity.this,
                            ColorizeEndActivity.class));
                    finish();

                }
            }
        });

    }
}
