package edu.cis.sensational.Controller.Calming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import edu.cis.sensational.R;

public class CalmingMode1Activity extends AppCompatActivity
{

    //Declaring the images on the GUI
//    private ImageView rainbow;
//    private ImageView candy1;
//    private ImageView candy2;
//    private ImageView candy3;
//    private ImageView cc1;
//    private ImageView cc2;
//    private ImageView cupcake;
//    private ImageView circle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Setting up the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calming_mode1);

        //Initialising all the images
//        rainbow = findViewById(R.id.rainbow);
//        circle = findViewById(R.id.circle1);
//        running = false;

    }

    public void backButton(View view)
    {
        Intent myIntent = new Intent(CalmingMode1Activity.this, CalmingActivity.class);
        startActivity(myIntent);
    }
}
