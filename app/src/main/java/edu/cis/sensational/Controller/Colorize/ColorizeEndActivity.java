package edu.cis.sensational.Controller.Colorize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import edu.cis.sensational.Controller.BubblesGame.BubblesEndActivity;
import edu.cis.sensational.Controller.SharedGames.GamesSharedActivity;
import edu.cis.sensational.Model.Colorize.GameConstants;
import edu.cis.sensational.Model.User;
import edu.cis.sensational.Model.Utils.FirebaseMethods;
import edu.cis.sensational.R;

public class ColorizeEndActivity extends AppCompatActivity {

    Button playAgainButton, quitButton;
    TextView scoreLabel, highScoreLabel;
    ImageView smiley;
    Switch musicSwitch2;
    FirebaseAuth mAuth;
    String userID;
    FirebaseMethods firebaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorize_end);
        playAgainButton = findViewById(R.id.playAgainButton);
        quitButton = findViewById(R.id.quitButton);
        scoreLabel = findViewById(R.id.scoreLabel);
        highScoreLabel = findViewById(R.id.highscorelabel);
        smiley = findViewById(R.id.smiley);
        musicSwitch2 = findViewById(R.id.musicSwitch2);
        firebaseMethods = new FirebaseMethods(ColorizeEndActivity.this);
        //same as start screen
        if (GameConstants.MUSIC = true)
        {
            musicSwitch2.setChecked(true);
        }
        else

        {
            musicSwitch2.setChecked(false);
        }

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }



        setUpButtons();
        displayScore();


    }

    public void setUpButtons()
    {
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ColorizeEndActivity.this,ColorizeMainActivity.class));
                finish();
                GameConstants.SCORE = 0;

            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ColorizeEndActivity.this, GamesSharedActivity.class));
                finish();
                GameConstants.SCORE = 0;
                GameConstants.mediaPlayer.stop();
            }
        });

        musicSwitch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicSwitch2.isChecked())
                {
                    GameConstants.mediaPlayer.start();
                    GameConstants.mediaPlayer.setVolume(20,20);
                    GameConstants.MUSIC = true;
                }
                else
                {
                    GameConstants.mediaPlayer.pause();
                    GameConstants.MUSIC = false;
                }
            }
        });
    }
    public void displayScore()
    {



        //if current score is greater than high score, update highscore
        firebaseMethods.checkHighScore(userID, new FirebaseMethods.Callback() {
            @Override
            public void onCallBack(int value) {
                int currentHighScore = value;
                if(GameConstants.SCORE > currentHighScore)
                {
                    GameConstants.HIGHSCORE = GameConstants.SCORE;
                }
            }
        });

        firebaseMethods.storeHighScore(userID,GameConstants.HIGHSCORE);


        scoreLabel.setText(""+GameConstants.SCORE);
        highScoreLabel.setText(GameConstants.DISPLAYHIGHSCORE + GameConstants.HIGHSCORE);


        // Send score to Firebase

        firebaseMethods.updateUserScore(userID, GameConstants.SCORE, new FirebaseMethods.Callback() {
            @Override
            public void onCallBack(int value) {

            }
        });




    }
}
