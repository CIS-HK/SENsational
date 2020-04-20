package edu.cis.sensational.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.cis.sensational.Controller.BubblesGame.BubbleStartActivity;
import edu.cis.sensational.Controller.Calming.CalmingActivity;
import edu.cis.sensational.Controller.Colorize.ColorizeStartActivity;
import edu.cis.sensational.Controller.Home.HomeActivity;
import edu.cis.sensational.Controller.Login.LoginActivity;
import edu.cis.sensational.R;

public class MainActivity extends AppCompatActivity {
    private Button bubbles;
    private Button colorize;
    private Button calming;
    private Button forum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bubbles = findViewById(R.id.bubblesButton);
        colorize = findViewById(R.id.colorizeButton);
        calming = findViewById(R.id.calmingButton);
        forum = findViewById(R.id.forumButton);

        bubbles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BubbleStartActivity.class);
                startActivity(intent);
            }
        });

        colorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ColorizeStartActivity.class);
                startActivity(intent);
            }
        });

        calming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalmingActivity.class);
                startActivity(intent);
            }
        });

        forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}
