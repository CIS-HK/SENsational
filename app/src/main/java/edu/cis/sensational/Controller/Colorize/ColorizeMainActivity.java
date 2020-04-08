package edu.cis.sensational.Controller.Colorize;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import edu.cis.sensational.Model.Colorize.GameConstants;
import edu.cis.sensational.R;


public class ColorizeMainActivity extends AppCompatActivity {

    Button answerOne, answerTwo, quitButton;
    TextView timeLabel, scoreLabel, colorWord;
    ImageView backgroundColor;
    ArrayList<String> colorWords;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorize_main);
        answerOne = findViewById(R.id.answerOne);
        answerTwo = findViewById(R.id.answerTwo);
        quitButton = findViewById(R.id.quitButton);
        timeLabel = findViewById(R.id.timeLabel);
        scoreLabel = findViewById(R.id.scoreLabel);
        colorWord = findViewById(R.id.colorWord);
        backgroundColor = findViewById(R.id.backgroundColor);
        colorWords = new ArrayList<String>();

        setUpButtons();
        addColors();
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
        colorWords.add("RED");
        colorWords.add("ORANGE");
        colorWords.add("YELLOW");
        colorWords.add("GREEN");
        colorWords.add("BLUE");
        colorWords.add("PURPLE");
    }

    private void play()
    {
        //generate random index from colorarraylist
        int index = new Random().nextInt(colorWords.size());
        //set text to random color
        colorWord.setText(colorWords.get(index));

        //need to figure out how to set text on buttons randomly
        answerOne.setText(colorWords.get(index));

        //need to set condition if "correct" button is clicked, if so, score ++

    }






}
