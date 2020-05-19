package edu.cis.sensational.Controller.BubblesGame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import edu.cis.sensational.R;

/***
 * The instructions page- has instructions on how to play the game and includes visuals to make the
 * instructions more clear to younger users.
 */

public class BubblesInstructionActivity extends AppCompatActivity
{
    private Button back;

    /**
     * Creates and displays the components on the screen (such as the "back" button).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubbles_instruction);

        back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(BubblesInstructionActivity.this,
                                            BubbleStartActivity.class);
                startActivity(intent);
            }
        });
    }
}
