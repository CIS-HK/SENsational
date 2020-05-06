package edu.cis.sensational.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.cis.sensational.Controller.Calming.CalmingActivity;
import edu.cis.sensational.Controller.SharedGames.GamesSharedActivity;
import edu.cis.sensational.Controller.Home.HomeActivity;
import edu.cis.sensational.R;

public class MainActivity extends AppCompatActivity {
    private Button games;
    private Button calming;
    private Button forum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        games = findViewById(R.id.gamesButton);
        calming = findViewById(R.id.calmingButton);
        forum = findViewById(R.id.forumButton);

        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GamesSharedActivity.class);
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
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });


    }
}
