package edu.cis.sensational.Controller.BubblesGame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import edu.cis.sensational.Controller.SharedGames.GamesSharedActivity;
import edu.cis.sensational.Model.BubblesGame.BubbleConstants;
import edu.cis.sensational.R;

/**
 * The start page - it displays the aspects of EF that the game targets, allows users to select
 * the mode of the game, and has buttons proceeding to the instructions page and the start of
 * the game.
 */
public class BubbleStartActivity extends AppCompatActivity
{
    private Button play;
    private Button back;
    private Button instructions;
    private Spinner spinner;

    /**
     * Creates and displays the components on the screen (such as buttons and the drop-down menu).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble_start);

        // Set up buttons
        play = findViewById(R.id.playButton);
        instructions = findViewById(R.id.instructionsButton);
        back = findViewById(R.id.back);

        play.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Gets the mode that the user selected
                String mode = spinner.getSelectedItem().toString();

                // Proceeds to the next screen to start the game
                Intent intent = new Intent(BubbleStartActivity.this,
                                                         BubblesMiddleActivity.class);
                intent.putExtra(BubbleConstants.MODE, mode);
                intent.putExtra(BubbleConstants.FIRST_TIME, true);
                startActivity(intent);
            }
        });

        instructions.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Proceeds to the instructions screen
                Intent intent = new Intent(BubbleStartActivity.this,
                                                         BubblesInstructionActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Proceeds back to the Games Shared screen
                Intent intent = new Intent(BubbleStartActivity.this,
                        GamesSharedActivity.class);
                startActivity(intent);
            }
        });

        // Sets up the spinner
        // https://www.youtube.com/watch?v=urQp7KsQhW8
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> sAdapter = new ArrayAdapter<String>(BubbleStartActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.modes));
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sAdapter);
    }
}
