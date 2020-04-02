package edu.cis.sensational.Controller.Colorize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.cis.sensational.R;

public class ColorizeEndActivity extends AppCompatActivity {

    Button playAgainButton, quitButton;
    TextView scoreLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorize_end);
        playAgainButton = findViewById(R.id.playAgainButton);
        quitButton = findViewById(R.id.quitButton);
        scoreLabel = findViewById(R.id.scoreLabel);

        setUpButtons();

    }

    public void setUpButtons()
    {
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ColorizeEndActivity.this,ColorizeMainActivity.class));
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
