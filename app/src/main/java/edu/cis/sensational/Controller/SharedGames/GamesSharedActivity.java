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
import edu.cis.sensational.Model.Colorize.GameConstants;
import edu.cis.sensational.Model.Utils.FirebaseMethods;
import edu.cis.sensational.R;

/**
 * This page displays total user score, and users can access the different games, the
 * trophies page or go back to the home page
 */
public class GamesSharedActivity extends AppCompatActivity {

    private Button colorizeIcon;
    private Button homePageButton;
    private Button bubblesIcon;
    private Button trophiesButton;
    private TextView userTotalScore;
    private FirebaseAuth mAuth;
    private String userID;

    /**
     * Creates and identifies the various components on screen such as buttons and labels
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_shared);

        colorizeIcon = findViewById(R.id.colorizeIcon);
        homePageButton = findViewById(R.id.homePageButton);
        bubblesIcon = findViewById(R.id.bubblesButton);
        trophiesButton = findViewById(R.id.trophies);
        userTotalScore = findViewById(R.id.totalScore3);

        //get current user ID
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null)
        {
            userID = mAuth.getCurrentUser().getUid();
        }
        if (userID != null)
        {
            //.retrieve total user score and display on screen
            FirebaseMethods firebaseMethods = new FirebaseMethods(GamesSharedActivity.this);
            firebaseMethods.updateUserScore(userID, GameConstants.ZERO, new FirebaseMethods.Callback()
            {
                @Override
                public void onCallBack(int value)
                {
                    userTotalScore.setText("" + value);
                }
            });
        }

        setUpButtons();
    }

    /**
     * Identifies actions once the corresponding buttons on the screen is pressed
     */
    private void setUpButtons()
    {
        colorizeIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(GamesSharedActivity.this,
                        ColorizeStartActivity.class));
            }
        });

        bubblesIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(GamesSharedActivity.this,
                        BubbleStartActivity.class));
            }
        });


        homePageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(GamesSharedActivity.this,
                        MainActivity.class));
            }
        });

        trophiesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(GamesSharedActivity.this,
                        TrophiesActivity.class));
            }
        });
    }
}
