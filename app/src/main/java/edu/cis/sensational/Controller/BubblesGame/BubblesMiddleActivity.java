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
    private ImageView smiley3;
    private int widthOfScreen;
    private int heightOfScreen;
    private float bubbleY;
    private Handler handler;
    private Timer timer;
    private int numTimes;
    private ArrayList<String> colorsPicked;
    private ArrayList<String> allColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubbles_middle);

        // Set up mode
        mode = getIntent().getStringExtra("mode");
        if (mode.equals("Easy")){
            numBubbles = 3;
        }
        if (mode.equals("Medium")){
            numBubbles = 4;
        }
        if (mode.equals("Hard")){
            numBubbles = 5;
        }

        // Set up buttons and ImageView
        settings = findViewById(R.id.settings);
        bubble = findViewById(R.id.floatingBubble);
        bubbleNumber = findViewById(R.id.number);
        smiley1 = findViewById(R.id.smiley1);
        smiley2 = findViewById(R.id.smiley2);

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
        widthOfScreen = sizeOfScreen.x;
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
