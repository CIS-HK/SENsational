package edu.cis.sensational.Controller.BubblesGame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import edu.cis.sensational.Controller.Colorize.GamesSharedActivity;
import edu.cis.sensational.Model.BubblesGame.BubbleConstants;
import edu.cis.sensational.R;

public class BubbleStartActivity extends AppCompatActivity
{
    private Button play;
    private Button back;
    private Button instructions;
    private Spinner spinner;

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
                String mode = spinner.getSelectedItem().toString();
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
                Intent intent = new Intent(BubbleStartActivity.this,
                                                         BubblesInstructionActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BubbleStartActivity.this,
                        GamesSharedActivity.class);
                startActivity(intent);
            }
        });

        // Set up spinner
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> sAdapter = new ArrayAdapter<String>(BubbleStartActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.modes));
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sAdapter);
    }
}
