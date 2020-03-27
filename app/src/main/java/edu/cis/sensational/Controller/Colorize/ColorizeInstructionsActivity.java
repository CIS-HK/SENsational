package edu.cis.sensational.Controller.Colorize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.cis.sensational.R;

public class ColorizeInstructionsActivity extends AppCompatActivity {

    Button backButton, playButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorize_instructions);
        backButton = findViewById(R.id.backButton);
        playButton = findViewById(R.id.playButton);

        setUpButtons();
    }

    public void setUpButtons(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ColorizeInstructionsActivity.this,ColorizeStartActivity.class));
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ColorizeInstructionsActivity.this,ColorizeMainActivity.class));
            }
        });
    }
}
