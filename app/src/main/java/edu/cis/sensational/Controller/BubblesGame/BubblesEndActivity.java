package edu.cis.sensational.Controller.BubblesGame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import edu.cis.sensational.Controller.SharedGames.GamesSharedActivity;
import edu.cis.sensational.Model.BubblesGame.BubbleConstants;
import edu.cis.sensational.Model.Utils.BubblesMethods;
import edu.cis.sensational.Model.Utils.FirebaseMethods;
import edu.cis.sensational.R;

/**
 * The end page- displays the user’s final score (in smiley faces), updates and displays the total
 * number of smiley faces that the user has won overall, and allows users to exit the game or play
 * again.
 */

public class BubblesEndActivity extends AppCompatActivity
{
    private int score;
    private ImageView smiley1;
    private ImageView smiley2;
    private ImageView smiley3;
    private TextView totalScore;
    private Button playAgain;
    private Button exitGame;
    private FirebaseAuth mAuth;
    private String userID;

    /**
     * Creates and displays the components on the screen (such as ImageViews, TextViews and
     * buttons). Updates the user's total number of smiley faces and sends the updated score back
     * to Firebase.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubbles_end);

        smiley1 = findViewById(R.id.wonSmiley1);
        smiley2 = findViewById(R.id.wonSmiley2);
        smiley3 = findViewById(R.id.wonSmiley3);
        playAgain = findViewById(R.id.playAgain);
        exitGame = findViewById(R.id.exitGame);
        totalScore = findViewById(R.id.totalScore);

        // Displays the user's final score
        score = getIntent().getIntExtra(BubbleConstants.SCORE, BubbleConstants.DEFAULT);
        BubblesMethods bubblesMethods = new BubblesMethods();
        bubblesMethods.displayEndScore(score, smiley1, smiley2, smiley3);

        // Updates and sends total number of smiley faces to Firebase
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null)
        {
            userID = mAuth.getCurrentUser().getUid();
        }

        if (userID != null)
        {
            FirebaseMethods firebaseMethods = new FirebaseMethods(BubblesEndActivity.this);
            firebaseMethods.updateUserScore(userID, score, new FirebaseMethods.Callback()
            {
                @Override
                public void onCallBack(int value) {
                    totalScore.setText("" + value);
                }
            });
        }

        // Sets up the 'play again' button
        playAgain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Proceeds to the Start screen
                Intent intent = new Intent(BubblesEndActivity.this,
                                            BubbleStartActivity.class);
                startActivity(intent);
            }
        });

        exitGame.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Proceeds to the Games Shared screen
                Intent intent = new Intent(BubblesEndActivity.this,
                                            GamesSharedActivity.class);
                startActivity(intent);
            }
        });
    }
}
