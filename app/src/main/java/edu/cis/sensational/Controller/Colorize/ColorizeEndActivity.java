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

        setUpMusicSwitch();
        setUpButtons();
        displayScore();

    }

    private void setUpButtons()
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

    private void setUpMusicSwitch()
    {
        //same as start screen
        if (GameConstants.MUSIC = true)
        {
            musicSwitch2.setChecked(true);
        }
        else

        {
            musicSwitch2.setChecked(false);
        }
    }
    private void displayScore()
    {
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null)
        {
            userID = mAuth.getCurrentUser().getUid();
        }


        firebaseMethods.updateUserScore(userID, GameConstants.SCORE, new FirebaseMethods.Callback()
        {
            @Override
            public void onCallBack(int value)
            {

            }
        });


        //need to fix if new user


        //if current score is greater than high score, update highscore
        firebaseMethods.checkHighScore(userID, new FirebaseMethods.Callback() {
            @Override
            public void onCallBack(int value)
            {
                //retrieve the current high score from the database and compare it to the current
                //user score
                int currentHighScore = value;

                //if current user score is greater than the high score stored on database, replace
                //the value on firebase and display updated highscore on UI
                if(GameConstants.SCORE > currentHighScore)
                {
                    GameConstants.HIGHSCORE = GameConstants.SCORE;
                    firebaseMethods.storeHighScore(userID,GameConstants.HIGHSCORE);
                    highScoreLabel.setText(GameConstants.DISPLAYHIGHSCORE + GameConstants.HIGHSCORE);
                    scoreLabel.setText(""+GameConstants.HIGHSCORE);
                }

                //if the current user score isn't greater, don't update the scores
                else
                {
                    scoreLabel.setText(""+GameConstants.SCORE);
                    highScoreLabel.setText(GameConstants.DISPLAYHIGHSCORE + currentHighScore);
                }
            }
        });

    }
}
