package edu.cis.sensational.Controller.BubblesGame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.cis.sensational.R;

public class BubbleStartActivity extends AppCompatActivity {

    private Button play;
    private Button instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble_start);
        play = findViewById(R.id.playButton);
        instructions = findViewById(R.id.instructionsButton);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BubbleStartActivity.this, BubblesMiddleActivity.class);
                startActivity(intent);
            }
        });
        instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BubbleStartActivity.this, BubblesInstructionActivity.class);
                startActivity(intent);
            }
        });
    }
}
