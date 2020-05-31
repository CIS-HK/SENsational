package edu.cis.sensational.Model.Utils;

import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;
import edu.cis.sensational.Model.BubblesGame.BubbleConstants;

/**
 * Stores methods used by more than one class in the Bubbles Game
 */

public class BubblesMethods
{

    public BubblesMethods()
    {

    }

    /**
     * Sets up the allColors ArrayList, which stores all the possible colors that the bubbles can be
     * @return allColors ArrayList
     */
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

    /**
     * Displays the user's score so far on the screen using smiley faces.
     * @param score the user's score so far (an int)
     * @param smiley1 the first smiley face ImageView
     * @param smiley2 the second smiley face ImageView
     */
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

    /**
     * Displays the user's final score on the end screen using smiley faces.
     * @param score the user's final score (as an int).
     * @param smiley1 the first smiley face ImageView
     * @param smiley2 the second smiley face ImageView
     * @param smiley3 the third smiley face ImageView
     */
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
