package edu.cis.sensational.Controller.SharedGames;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import edu.cis.sensational.R;

public class TrophiesActivity extends AppCompatActivity {
    private Button back;
    private RecyclerView allTrophies;
    private RecyclerView collectedTrophies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophies);

        allTrophies = findViewById(R.id.allRecyclerView);
        collectedTrophies = findViewById(R.id.collectedRecyclerView);

    }
}
