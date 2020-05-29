package edu.cis.sensational.Model.Colorize;

import android.media.MediaPlayer;
import android.os.CountDownTimer;

import edu.cis.sensational.R;

public class GameConstants {


    public static boolean BACKGROUND = false;
    public static int SCORE = 0;
    public static int HIGHSCORE = 0;


    public static final int BOUND = 2;

    //strings
    public static final String WRONGANSWER = "Wrong Answer!";
    public static final String DISPLAYHIGHSCORE = "High Score: ";
    public static final String TOAST = "Time's up!";

    //colors
    public static final String RED = "RED";
    public static final String YELLOW = "YELLOW";
    public static final String GREEN = "GREEN";
    public static final String BLACK = "BLACK";
    public static final String GRAY = "GRAY";
    public static final String PINK = "PINK";
    public static final String BLUE = "BLUE";

    //media player
    public static MediaPlayer mediaPlayer = new MediaPlayer();
    public static boolean MUSIC = false;
    public static final int VOLUME = 20;

    //timer
    public static int TIME;
    public static final int DELAY = 0;
    public static final int PERIOD = 13;
    public static final int INTERVAL = 1000;
    public static final int THREESEC = 3000;
    public static final int FOURSEC = 4000;
    public static final int DEFAULTTIME = 5000;
    public static final String TIMESTRING ="Time";
    public static final String THREESECONDS = "3 seconds";
    public static final String FOURSECONDS = "4 seconds";
    public static final String FIVESECONDS = "5 seconds";

    //animations
    public static final int ZERO = 0;
    public static final int MOVEMENTMARGIN = 10;
    public static final float SCREENHEIGHTUP = 2000.0f;
    public static final float SCREENHEIGHTDOWN = 1000.0f;
    public static final int MIDDLERIGHTINITIAL = 1700;
    public static final int TOPLEFTINITIAL = 1200;
    public static final float SCREENWIDTHLEFT = 800.0f;


}
