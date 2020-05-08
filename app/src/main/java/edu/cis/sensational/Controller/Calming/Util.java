package edu.cis.sensational.Controller.Calming;

import android.content.Context;
import android.media.Image;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.logging.Handler;

import edu.cis.sensational.Model.CConstants;
import edu.cis.sensational.R;

public final class Util
{
    public static AnimationSet sizeControl(ImageView circle, Context context, Integer growTime,
                                           Integer offsetTime, Integer shrinkTime)
    {
        //https://developer.android.com/reference/android/view/animation/AnimationSet.html
        //Docs for animation set

        //Creating a new final animation set that will be used on the circle
        final AnimationSet animSet = new AnimationSet(true);

        //Getting the grow animation from anim file and setting the duration to 3 seconds, and
        //adding it to the animation set for the circle
        Animation grow = AnimationUtils.loadAnimation(context, R.anim.circleanimation2);
        //3000
        grow.setDuration(growTime * 1000);
        animSet.addAnimation(grow);

        //Getting the shrink animation from anim file and setting the duration to 3 seconds, and
        //the start offset to 5 seconds so there will be a 2 second stop before shrink starts and
        //adding it to the animation set for the circle
        Animation shrink = AnimationUtils.loadAnimation(context, R.anim.circleanimation);
        //3000
        shrink.setDuration(shrinkTime * 1000);

        //5000
        shrink.setStartOffset((offsetTime + growTime) * 1000);
        animSet.addAnimation(shrink);

        //circle starts to perform the animations in animation set
        circle.startAnimation(animSet);

        return animSet;
    }

    //Method to control the words text view
    public static void text(CConstants c, final TextView breathe, final boolean pause)
    {
        //Words to be shown in order on the screen for for seconds, then 6 seconds
        final String[] array2 = {c.bIn, c.hold, c.bOut};
        //Making a new Runnable (loop) for the words string

        Thread t = new Thread(new Runnable()
        {
            int x = 0;
            @Override
            public void run()
            {

                //Setting 3 second interval between the changing of words
                if(x == 1)
                {
                    breathe.postDelayed(this, 2000);
                }
                else
                {
                    breathe.postDelayed(this, 3000);
                }
                //Setting the text to the words in the array and positively incrementing x
                breathe.setText(array2[x]);
                x++;
                //If paused, i is zero again and it is back to the start
                if(pause == true)
                {
                    x = 0;
                    breathe.setText(array2[x]);
                }
                //When it reaches the end of array, goes back to beginning, continuing the loop
                if (x == 3)
                {
                    x = 0;
                }

            }
        });
    }

    //Method to control number text view
    public static void number(CConstants c, final TextView number, final boolean pause, final Handler handler)
    {

        //Array that hold the numbers to be shown in order on the screen per 1 second
        final String[] array1 = {c.one, c.two, c.three, c.one, c.two,c.one, c.two, c.three};
        //Making a new Runnable (loop) for the number string


        number.post(new Runnable()
        {
            int i = 0;
            @Override
            public void run()
            {
                //Setting almost 1 second interval between the changing of numbers
                number.postDelayed(this, 992);
                //Setting the text to the number in the array and positively incrementing i
                number.setText(array1[i]);
                i++;
                //If paused, i is zero again and it is back to the start
                if (pause == true)
                {
                    i = 0;
                    number.setText(array1[i]);
                }
                //When it reaches the end of array, goes back to beginning, continuing the loop
                if (i == 8)
                {
                    i = 0;
                }
            }
        });
    }
}


