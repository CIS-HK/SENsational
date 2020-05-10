package edu.cis.sensational.Controller.Calming;

import android.content.Context;
import android.media.Image;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Handler;

import edu.cis.sensational.Model.CConstants;
import edu.cis.sensational.R;

public final class Util
{
    public static AnimationSet circleControl(ImageView circle, Context context, Integer growTime,
                                             Integer offsetTime, Integer shrinkTime)
    {
        //https://developer.android.com/reference/android/view/animation/AnimationSet.html
        //Docs for animation set

        //Creating a new final animation set that will be used on the circle
        final AnimationSet animSet = new AnimationSet(true);

        //Getting the grow animation from anim file and setting the duration to 3 seconds, and
        //adding it to the animation set for the circle
        Animation grow = AnimationUtils.loadAnimation(context, R.anim.circleanimation2);
        //Time selected * 1000 so it is in milliseconds
        grow.setDuration(growTime * 1000);
        animSet.addAnimation(grow);

        //Getting the shrink animation from anim file and setting the duration to 3 seconds, and
        //the start offset to 5 seconds so there will be a 2 second stop before shrink starts and
        //adding it to the animation set for the circle
        Animation shrink = AnimationUtils.loadAnimation(context, R.anim.circleanimation);
        //Time selected * 1000 so it is in milliseconds
        shrink.setDuration(shrinkTime * 1000);

        //Time selected * 1000 so it is in milliseconds
        shrink.setStartOffset((offsetTime + growTime) * 1000);
        animSet.addAnimation(shrink);

        //circle starts to perform the animations in animation set
        circle.startAnimation(animSet);

        return animSet;
    }

    //Method to make number arraylist for the number on the circle
    public static ArrayList<String> numberArrayList(int in, int hold, int out)
    {
        ArrayList<String> array1 = new ArrayList<>();

        for (int i = 1; i <= in; i++)
        {
            array1.add(i + "");
        }

        for (int i = 1; i <= hold; i++)
        {
            array1.add(i + "");
        }

        for (int i = 1; i <= out; i++)
        {
            array1.add(i + "");
        }
        return array1;
    }
}




