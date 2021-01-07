package edu.cis.sensational.Controller.Colorize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import edu.cis.sensational.Controller.SharedGames.GamesSharedActivity;
import edu.cis.sensational.Model.Colorize.GameConstants;
import edu.cis.sensational.Model.User;
import edu.cis.sensational.Model.Utils.FirebaseMethods;
import edu.cis.sensational.R;

/**
 * The end activity is where the user will be directed to when they finished the game, it displays
 * the current user score and high score, as well as buttons to play again, to go back to the start
 * page or quit the game completely
 */
public class ColorizeEndActivity extends AppCompatActivity {

    Button playAgainButton, quitButton, homePageButton;
    TextView scoreLabel, highScoreLabel;
    ImageView smiley;
    FirebaseAuth mAuth;
    String userID;
    FirebaseMethods firebaseMethods;

    /**
     * Creates and identifies the various components on screen such as buttons and labels
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorize_end);
        playAgainButton = findViewById(R.id.playAgainButton);
        quitButton = findViewById(R.id.quitButton);
        homePageButton = findViewById(R.id.homePageButton);
        scoreLabel = findViewById(R.id.scoreLabel);
        highScoreLabel = findViewById(R.id.highscorelabel);
        smiley = findViewById(R.id.smiley);
        firebaseMethods = new FirebaseMethods(ColorizeEndActivity.this);
        setUpButtons();
        displayScore();
    }

    /**
     * Identifies actions once the corresponding buttons on the screen is pressed
     */
    private void setUpButtons()
    {
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ColorizeEndActivity.this,ColorizeMainActivity.class));
                finish();
                GameConstants.SCORE = GameConstants.ZERO;

            }
        });

        homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ColorizeEndActivity.this,ColorizeStartActivity.class));
                finish();
                GameConstants.SCORE = GameConstants.ZERO;
                GameConstants.mediaPlayer.stop();
                GameConstants.MUSIC = false;
            }
        });
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ColorizeEndActivity.this, GamesSharedActivity.class));
                finish();
                GameConstants.SCORE = GameConstants.ZERO;
                GameConstants.mediaPlayer.stop();
            }
        });

    }

    /**
     * Retrieves user ID from firebase and uses firebase methods to update total user score, check
     * and update high score, and display current and high score on screen
     */
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
                scoreLabel.setText("" + value);
            }
        });

        firebaseMethods.initialStoring(userID,GameConstants.SCORE,scoreLabel,highScoreLabel);

        //if current score is greater than high score, update highscore
        firebaseMethods.checkHighScore(userID, GameConstants.SCORE,new FirebaseMethods.Callback() {
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
