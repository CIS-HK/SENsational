package edu.cis.sensational.Controller.BubblesGame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.cis.sensational.R;

public class BubblesMiddle2Activity extends AppCompatActivity {
    private Button settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble_middle2);
        settings = findViewById(R.id.settings2);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BubblesMiddle2Activity.this, BubblesEndActivity.class);
                startActivity(intent);
            }
        });
    }
}
