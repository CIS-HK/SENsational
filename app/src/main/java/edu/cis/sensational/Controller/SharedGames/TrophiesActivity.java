package edu.cis.sensational.Controller.SharedGames;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.cis.sensational.Model.BubblesGame.BubbleConstants;
import edu.cis.sensational.Model.Utils.FirebaseMethods;
import edu.cis.sensational.R;

/**
 * The trophies page displays the trophies the user earned, the upcoming trophy and the total user
 * score
 */
public class TrophiesActivity extends AppCompatActivity {
    private Button back;
    private RecyclerView allTrophies;
    private ArrayList<Trophy> trophies;
    private int userScore;
    private TextView totalScore;
    private FirebaseAuth mAuth;
    private String userID;
    private TextView nextTrophy;
    private TextView nextTrophyName;
    private TextView nextTrophyNum;
    private ImageView nextTrophySmiley;

    private static final String TAG = "TrophiesActivity";

    /**
     * Creates and identifies the various components on screen
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophies);

        totalScore = findViewById(R.id.totalScore2);
        back = findViewById(R.id.backB);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(TrophiesActivity.this,
                        GamesSharedActivity.class));
            }
        });

        allTrophies = findViewById(R.id.trophiesRecyclerView);
        nextTrophy = findViewById(R.id.nextTrophy);
        nextTrophyName = findViewById(R.id.trophyName2);
        nextTrophyNum = findViewById(R.id.trophyNum2);
        nextTrophySmiley = findViewById(R.id.trophySmiley2);

        //add trophies to the trophies list
        trophies = new ArrayList<>();
        int imageID = getResources().getIdentifier("redtrophy",
                BubbleConstants.DRAWABLE,
                getPackageName());
        final Trophy redTrophy = new Trophy(10, "Red Trophy", imageID);
        imageID = getResources().getIdentifier("orangetrophy",
                BubbleConstants.DRAWABLE,
                getPackageName());
        final Trophy orangeTrophy = new Trophy(20, "Orange Trophy", imageID);
        imageID = getResources().getIdentifier("greentrophy",
                BubbleConstants.DRAWABLE,
                getPackageName());
        final Trophy greenTrophy = new Trophy(50, "Green Trophy", imageID);
        imageID = getResources().getIdentifier("bluetrophy",
                BubbleConstants.DRAWABLE,
                getPackageName());
        final Trophy blueTrophy = new Trophy(100, "Blue Trophy", imageID);
        imageID = getResources().getIdentifier("purpletrophy",
                BubbleConstants.DRAWABLE,
                getPackageName());
        final Trophy purpleTrophy = new Trophy(200, "Purple Trophy", imageID);
        redTrophy.setNextTrophy(orangeTrophy);
        orangeTrophy.setNextTrophy(greenTrophy);
        greenTrophy.setNextTrophy(blueTrophy);
        blueTrophy.setNextTrophy(purpleTrophy);

        //get current user ID
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null)
        {
            userID = mAuth.getCurrentUser().getUid();
        }
        if (userID != null)
        {
            //set recycler view
            final TrophiesAdapter adapter = new TrophiesAdapter(trophies);
            allTrophies.setAdapter(adapter);
            allTrophies.setLayoutManager(new LinearLayoutManager(this));

            //retrive total user score from database
            FirebaseMethods firebaseMethods = new FirebaseMethods(TrophiesActivity.this);
            firebaseMethods.updateUserScore(userID, 0, new FirebaseMethods.Callback() {
                @Override

                //decide if the user earns a trophy or not and display it on screen
                public void onCallBack(int value)
                {
                    userScore = value;
                    totalScore.setText("" + value);
                    if (userScore >= redTrophy.getSmileyFaces())
                    {
                        trophies.add(redTrophy);
                        // https://stackoverflow.com/questions/31367599/how-to-update-recyclerview-adapter-data
                        adapter.notifyItemInserted(0);
                        nextTrophyName.setText(redTrophy.getNextTrophy().getName());
                        nextTrophyNum.setText("" + redTrophy.getNextTrophy().getSmileyFaces());
                    }
                    if (userScore >= orangeTrophy.getSmileyFaces())
                    {
                        trophies.add(orangeTrophy);
                        adapter.notifyItemInserted(1);
                        nextTrophyName.setText(orangeTrophy.getNextTrophy().getName());
                        nextTrophyNum.setText("" + orangeTrophy.getNextTrophy().getSmileyFaces());
                    }
                    if (userScore >= greenTrophy.getSmileyFaces())
                    {
                        trophies.add(greenTrophy);
                        adapter.notifyItemInserted(2);
                        nextTrophyName.setText(greenTrophy.getNextTrophy().getName());
                        nextTrophyNum.setText("" + greenTrophy.getNextTrophy().getSmileyFaces());
                    }
                    if (userScore >= blueTrophy.getSmileyFaces())
                    {
                        trophies.add(blueTrophy);
                        adapter.notifyItemInserted(3);
                        nextTrophyName.setText(blueTrophy.getNextTrophy().getName());
                        nextTrophyNum.setText("" + blueTrophy.getNextTrophy().getSmileyFaces());
                    }
                    if (userScore >= purpleTrophy.getSmileyFaces())
                    {
                        trophies.add(purpleTrophy);
                        adapter.notifyItemInserted(4);
                        nextTrophyName.setVisibility(View.GONE);
                        nextTrophyNum.setVisibility(View.GONE);
                        nextTrophy.setVisibility(View.GONE);
                        nextTrophySmiley.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
