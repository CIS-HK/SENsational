package edu.cis.sensational.Controller.SharedGames;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import edu.cis.sensational.R;

public class TrophiesActivity extends AppCompatActivity {
    private Button back;
    private RecyclerView allTrophies;
    private RecyclerView collectedTrophies;
    private ArrayList<String> trophyNames;
    private ArrayList<Integer> trophyNums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophies);

        back = findViewById(R.id.backB);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrophiesActivity.this, GamesSharedActivity.class));
            }
        });

        allTrophies = findViewById(R.id.trophiesRecyclerView);

        trophyNames = new ArrayList<>();
        trophyNames.add("red");
        trophyNames.add("orange");
        trophyNames.add("yellow");
        trophyNames.add("green");
        trophyNames.add("blue");
        trophyNames.add("purple");

        trophyNums = new ArrayList<>();
        trophyNums.add(10);
        trophyNums.add(20);
        trophyNums.add(50);
        trophyNums.add(100);
        trophyNums.add(200);
        trophyNums.add(400);

        TrophiesAdapter adapter = new TrophiesAdapter(trophyNames, trophyNums);
        allTrophies.setAdapter(adapter);
        allTrophies.setLayoutManager(new LinearLayoutManager(this));
    }
}
