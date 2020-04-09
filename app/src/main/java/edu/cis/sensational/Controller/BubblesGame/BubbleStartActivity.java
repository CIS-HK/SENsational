package edu.cis.sensational.Controller.BubblesGame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import edu.cis.sensational.R;

public class BubbleStartActivity extends AppCompatActivity
{

    private Button play;
    private Button instructions;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble_start);
        play = findViewById(R.id.playButton);
        instructions = findViewById(R.id.instructionsButton);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mode = spinner.getSelectedItem().toString();
                Intent intent = new Intent(BubbleStartActivity.this, BubblesMiddleActivity.class);
                intent.putExtra("mode", mode);
                startActivity(intent);
            }
        });
        instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BubbleStartActivity.this, BubblesInstructionActivity.class);
                startActivity(intent);
            }
        });
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(BubbleStartActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.modes));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }
}
