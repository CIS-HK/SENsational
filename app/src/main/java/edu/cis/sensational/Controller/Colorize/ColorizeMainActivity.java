package edu.cis.sensational.Controller.Colorize;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import edu.cis.sensational.R;


public class ColorizeMainActivity extends AppCompatActivity {

    Button answerOne, answerTwo, quitButton;
    TextView timeLabel, scoreLabel, colorWord;
    ArrayList colorwords;

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

        setUpButtons();

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

}
