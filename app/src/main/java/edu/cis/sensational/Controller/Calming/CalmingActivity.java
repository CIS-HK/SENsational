package edu.cis.sensational.Controller.Calming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.cis.sensational.R;

public class CalmingActivity extends AppCompatActivity {

    Button mode1;
    Button mode2;
    Button mode3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calming);
        mode1 = findViewById(R.id.mode1Button);
        mode2 = findViewById(R.id.mode2Button);
        mode3 = findViewById(R.id.mode3Button);

        setUpButtons();
    }

    public void setUpButtons()
    {
        mode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(CalmingActivity.this, CalmingMode1Activity.class);
                startActivity(myIntent);
            }
        });
        mode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(CalmingActivity.this, CalmingMode2Activity.class);
                startActivity(myIntent);
            }
        });
        mode3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(CalmingActivity.this, CalmingMode3Activity.class);
                startActivity(myIntent);
            }
        });

    }



}
