package edu.cis.sensational.Controller.Colorize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.cis.sensational.R;

/**
 * The instructions page displays the instructions for the game and prompts user to go back to the
 * main page or play the game
 */

public class ColorizeInstructionsActivity extends AppCompatActivity
{
    Button backButton, playButton;

    /**
     * Creates and identifies the various components on screen such as the buttons
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorize_instructions);
        backButton = findViewById(R.id.backButton);
        playButton = findViewById(R.id.playButton);

        setUpButtons();
    }

    /**
     * Identifies actions once the corresponding buttons on the screen is pressed
     */
    public void setUpButtons()
    {
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ColorizeInstructionsActivity.this,
                        ColorizeStartActivity.class));
            }
        });

        playButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(ColorizeInstructionsActivity.this,
                        ColorizeMainActivity.class));
            }
        });
    }
}