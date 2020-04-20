package edu.cis.sensational.Controller.BubblesGame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import edu.cis.sensational.Controller.MainActivity;
import edu.cis.sensational.Model.BubblesGame.BubbleConstants;
import edu.cis.sensational.R;

public class BubblesEndActivity extends AppCompatActivity
{
    private int score;
    private ImageView smiley1;
    private ImageView smiley2;
    private ImageView smiley3;
    private Button playAgain;
    private Button exitGame;

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

        score = getIntent().getIntExtra(BubbleConstants.SCORE, BubbleConstants.DEFAULT);
        System.out.println(score);
        if (score == 1)
        {
            smiley1.setVisibility(View.VISIBLE);
        }
        else if (score == 2)
        {
            smiley1.setVisibility(View.VISIBLE);
            smiley2.setVisibility(View.VISIBLE);
        }
        else if (score == 3)
        {
            smiley1.setVisibility(View.VISIBLE);
            smiley2.setVisibility(View.VISIBLE);
            smiley3.setVisibility(View.VISIBLE);
        }

        playAgain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(BubblesEndActivity.this,
                                            BubbleStartActivity.class);
                startActivity(intent);
            }
        });

        exitGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BubblesEndActivity.this,
                                            MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
