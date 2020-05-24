package edu.cis.sensational.Controller.SharedGames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import edu.cis.sensational.Controller.BubblesGame.BubbleStartActivity;
import edu.cis.sensational.Controller.Colorize.ColorizeStartActivity;
import edu.cis.sensational.Controller.MainActivity;
import edu.cis.sensational.Model.Utils.FirebaseMethods;
import edu.cis.sensational.R;

public class GamesSharedActivity extends AppCompatActivity {

    private Button colorizeIcon;
    private Button homePageButton;
    private Button bubblesIcon;
    private Button trophiesButton;
    private TextView userTotalScore;
    private FirebaseAuth mAuth;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_shared);

        colorizeIcon = findViewById(R.id.colorizeIcon);
        homePageButton = findViewById(R.id.homePageButton);
        bubblesIcon = findViewById(R.id.bubblesButton);
        trophiesButton = findViewById(R.id.trophies);
        userTotalScore = findViewById(R.id.totalScore3);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
        if (userID != null) {
            FirebaseMethods firebaseMethods = new FirebaseMethods(GamesSharedActivity.this);
            firebaseMethods.updateUserScore(userID, 0, new FirebaseMethods.Callback() {
                @Override
                public void onCallBack(int value) {
                    userTotalScore.setText("" + value);
                }
            });
        }

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

        bubblesIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GamesSharedActivity.this, BubbleStartActivity.class));
            }
        });

        trophiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GamesSharedActivity.this, TrophiesActivity.class));
            }
        });
    }
}
