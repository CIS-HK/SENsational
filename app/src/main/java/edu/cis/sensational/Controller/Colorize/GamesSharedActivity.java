package edu.cis.sensational.Controller.Colorize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import edu.cis.sensational.Controller.MainActivity;
import edu.cis.sensational.R;

public class GamesSharedActivity extends AppCompatActivity {

    ImageButton colorizeIcon;
    Button homePageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_shared);
        colorizeIcon = findViewById(R.id.colorizeIcon);
        homePageButton = findViewById(R.id.homePageButton);

        setUpButtons();
    }

    private void setUpButtons()
    {
        colorizeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GamesSharedActivity.this, ColorizeStartActivity.class));
            }
        });

        homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GamesSharedActivity.this, MainActivity.class));
            }
        });
    }
}
