package edu.cis.sensational.Model.Utils;

import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;
import edu.cis.sensational.Model.BubblesGame.BubbleConstants;

public class BubblesMethods
{

    public BubblesMethods()
    {

    }

    public ArrayList<String> setUpAllColors(){
        ArrayList<String> allColors = new ArrayList<>();
        allColors.add(BubbleConstants.BLACK);
        allColors.add(BubbleConstants.BLUE);
        allColors.add(BubbleConstants.GREEN);
        allColors.add(BubbleConstants.RED);
        allColors.add(BubbleConstants.YELLOW);
        allColors.add(BubbleConstants.PINK);
        allColors.add(BubbleConstants.PURPLE);
        return allColors;
    }

    public void displayScore(int score, ImageView smiley1, ImageView smiley2)
    {
        if (score == 1)
        {
            smiley1.setVisibility(View.VISIBLE);
        }
        else if (score == 2)
        {
            smiley1.setVisibility(View.VISIBLE);
            smiley2.setVisibility(View.VISIBLE);
        }
    }

    public void displayEndScore(int score, ImageView smiley1, ImageView smiley2, ImageView smiley3)
    {
        if (score == 1)
        {
            smiley1.setVisibility(View.VISIBLE);
        }
        else if (score == 2)
        {
            smiley1.setVisibility(View.VISIBLE);
            smiley2.setVisibility(View.VISIBLE);
        }
        else if (score == 3)
        {
            smiley1.setVisibility(View.VISIBLE);
            smiley2.setVisibility(View.VISIBLE);
            smiley3.setVisibility(View.VISIBLE);
        }
    }
}
