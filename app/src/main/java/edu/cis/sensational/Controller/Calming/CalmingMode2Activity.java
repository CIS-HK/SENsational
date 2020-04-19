package edu.cis.sensational.Controller.Calming;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import edu.cis.sensational.R;

public class CalmingMode2Activity extends AppCompatActivity {



    private float screenWidth;
    private float screenHeight;

    private boolean running;
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calming_mode2);


        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

//        circle.setX(-80.0f);
//        circle.setY(-80.0f);

//        schedualTimer();

    }

//    public void schedualTimer()
//    {
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        running = true;
//                        changePos();
//                    }
//                });
//            }
//        },0,20);
//    }

//    public void changePos()
//    {
//        circley -= 10;
//        if(circle.getY() + circle.getHeight() < 0)
//        {
//            circlex = (float)Math.floor(Math.random()* (screenWidth - circle.getWidth()));
//            circley = screenHeight + 100.0f;
//        }
//        circle.setX(circlex);
//        circle.setY(circley);
//    }


//    public void pauseButton(View view)
//    {
//        if(running == false)
//        {
//            schedualTimer();
//        }
//        else
//        {
//            timer.cancel();
//            running = false;
//        }
}
