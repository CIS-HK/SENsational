package edu.cis.sensational.Controller.BubblesGame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import edu.cis.sensational.R;

public class BubblesMiddleActivity extends AppCompatActivity {
    private Button settings;
    private String mode;
    private int numBubbles;
    private ImageView bubble;
    private TextView bubbleNumber;
    private ImageView smiley1;
    private ImageView smiley2;
    private int heightOfScreen;
    private float bubbleY;
    private Handler handler;
    private Timer timer;
    private int numTimes;
    private ArrayList<String> colorsPicked;
    private ArrayList<String> allColors;
    private Boolean firstTime;
    private int score;
    private int roundNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubbles_middle);

        // Set up buttons and ImageView
        settings = findViewById(R.id.settings);
        bubble = findViewById(R.id.floatingBubble);
        bubbleNumber = findViewById(R.id.number);
        smiley1 = findViewById(R.id.smiley1);
        smiley2 = findViewById(R.id.smiley2);

        // Set up mode (if this is the first time starting the screen)
        firstTime = getIntent().getBooleanExtra("firstTime", false);
        if (firstTime){
            mode = getIntent().getStringExtra("mode");
            if (mode != null && mode.equals("Easy")){
                numBubbles = 3;
            }
            if (mode != null && mode.equals("Medium")){
                numBubbles = 4;
            }
            if (mode != null && mode.equals("Hard")){
                numBubbles = 5;
            }
            roundNumber = 0;
            score = 0;
        }
        else {
            numBubbles = getIntent().getIntExtra("numBubbles", 0);
            score = getIntent().getIntExtra("score", 0);
            roundNumber = getIntent().getIntExtra("roundNumber", 0);
            if (score == 1){
                smiley1.setVisibility(View.VISIBLE);
            }
            else if (score == 2){
                smiley1.setVisibility(View.VISIBLE);
                smiley2.setVisibility(View.VISIBLE);
            }
        }

        // Set up allColors ArrayList
        allColors = new ArrayList<>();
        allColors.add("black");
        allColors.add("blue");
        allColors.add("green");
        allColors.add("red");
        allColors.add("yellow");
        allColors.add("pink");
        allColors.add("purple");

        colorsPicked = new ArrayList<>();
        numTimes = 1;
        generateRandomSequence();
        setUpBubbles();
    }

    public void generateRandomSequence(){
        Random random = new Random();
        String colorBubble = allColors.get(random.nextInt(7));
        colorsPicked.add(colorBubble);
        String imageName = colorBubble + "bubble";
        int imageID = getResources().getIdentifier(imageName, "drawable", getPackageName());
        bubble.setImageResource(imageID);
    }

    public void setUpBubbles(){
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point sizeOfScreen = new Point();
        display.getSize(sizeOfScreen);
        heightOfScreen = sizeOfScreen.y;

        playBubble();
    }

    public void changeBubbleCoordinates(){
        bubbleY -= 10;
        if (bubble.getY() + bubble.getHeight() < 0){
            timer.cancel();
            if (numTimes < numBubbles){
                generateRandomSequence();
                numTimes = numTimes + 1;
                String newNumber = "" + numTimes;
                bubbleNumber.setText(newNumber);
                playBubble();
            }
            else {
                Intent intent = new Intent(BubblesMiddleActivity.this, BubblesMiddle2Activity.class);
                intent.putExtra("numBubbles", numBubbles);
                intent.putExtra("colorsPicked", colorsPicked);
                intent.putExtra("allColors", allColors);
                intent.putExtra("score", score);
                intent.putExtra("roundNumber", roundNumber);
                startActivity(intent);
            }
        }
        bubble.setY(bubbleY);
        bubbleNumber.setY(bubbleY + 200.0f);
    }

    public void playBubble()
    {
        timer = new Timer();
        handler = new Handler();
        bubbleY = heightOfScreen + 100.0f;
        bubbleNumber.setY(bubbleY + 200.0f);
        bubble.setY(bubbleY);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        changeBubbleCoordinates();
                    }
                });
            }
        }, 0, 7);
    }
}
