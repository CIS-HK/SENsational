package edu.cis.sensational.Controller.SharedGames;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import edu.cis.sensational.Model.BubblesGame.BubbleConstants;
import edu.cis.sensational.Model.SharedGamesConstants;
import edu.cis.sensational.Model.Utils.FirebaseMethods;
import edu.cis.sensational.R;

public class TrophiesActivity extends AppCompatActivity
{
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophies);

        totalScore = findViewById(R.id.totalScore2);
        back = findViewById(R.id.backB);

        // Set up back button
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Proceeds to GamesSharedActivity
                startActivity(new Intent(TrophiesActivity.this,
                                          GamesSharedActivity.class));
            }
        });

        // Sets up RecyclerView to display trophies won
        allTrophies = findViewById(R.id.trophiesRecyclerView);
        nextTrophy = findViewById(R.id.nextTrophy);
        nextTrophyName = findViewById(R.id.trophyName2);
        nextTrophyNum = findViewById(R.id.trophyNum2);
        nextTrophySmiley = findViewById(R.id.trophySmiley2);

        // Creates all trophies
        trophies = new ArrayList<>();
        int imageID = getResources().getIdentifier(SharedGamesConstants.REDTROPHY1,
                BubbleConstants.DRAWABLE,
                getPackageName());
        final Trophy redTrophy = new Trophy(SharedGamesConstants.REDSMILEYS,
                                            SharedGamesConstants.REDTROPHY2,
                                            imageID);
        imageID = getResources().getIdentifier(SharedGamesConstants.ORANGETROPHY1,
                BubbleConstants.DRAWABLE,
                getPackageName());
        final Trophy orangeTrophy = new Trophy(SharedGamesConstants.ORANGESMILEYS,
                                                SharedGamesConstants.ORANGETROPHY2,
                                                imageID);
        imageID = getResources().getIdentifier(SharedGamesConstants.GREENTROPHY1,
                BubbleConstants.DRAWABLE,
                getPackageName());
        final Trophy greenTrophy = new Trophy(SharedGamesConstants.GREENSMILEYS,
                                              SharedGamesConstants.GREENTROPHY2,
                                              imageID);
        imageID = getResources().getIdentifier(SharedGamesConstants.BLUETROPHY1,
                BubbleConstants.DRAWABLE,
                getPackageName());
        final Trophy blueTrophy = new Trophy(SharedGamesConstants.BLUESMILEYS,
                                            SharedGamesConstants.BLUETROPHY2,
                                            imageID);
        imageID = getResources().getIdentifier(SharedGamesConstants.PURPLETROPHY1,
                BubbleConstants.DRAWABLE,
                getPackageName());
        final Trophy purpleTrophy = new Trophy(SharedGamesConstants.PURPLESMILEYS,
                                               SharedGamesConstants.PURPLETROPHY2,
                                               imageID);
        redTrophy.setNextTrophy(orangeTrophy);
        orangeTrophy.setNextTrophy(greenTrophy);
        greenTrophy.setNextTrophy(blueTrophy);
        blueTrophy.setNextTrophy(purpleTrophy);

        // Gets score from Firebase
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null)
        {
            userID = mAuth.getCurrentUser().getUid();
        }
        if (userID != null)
        {
            final TrophiesAdapter adapter = new TrophiesAdapter(trophies);
            allTrophies.setAdapter(adapter);
            allTrophies.setLayoutManager(new LinearLayoutManager(this));

            FirebaseMethods firebaseMethods = new FirebaseMethods(TrophiesActivity.this);
            firebaseMethods.updateUserScore(userID,
                                            SharedGamesConstants.SCORETOINSERT,
                                            new FirebaseMethods.Callback()
            {
                @Override
                public void onCallBack(int value) {
                    // Updates RecyclerView to display trophies won
                    userScore = value;
                    totalScore.setText("" + value);
                    if (userScore >= redTrophy.getSmileyFaces())
                    {
                        trophies.add(redTrophy);
                        // https://stackoverflow.com/questions/31367599/how-to-update-recyclerview-adapter-data
                        adapter.notifyItemInserted(SharedGamesConstants.INDEX0);
                        nextTrophyName.setText(redTrophy.getNextTrophy().getName());
                        nextTrophyNum.setText("" + redTrophy.getNextTrophy().getSmileyFaces());
                    }
                    if (userScore >= orangeTrophy.getSmileyFaces())
                    {
                        trophies.add(orangeTrophy);
                        adapter.notifyItemInserted(SharedGamesConstants.INDEX1);
                        nextTrophyName.setText(orangeTrophy.getNextTrophy().getName());
                        nextTrophyNum.setText("" + orangeTrophy.getNextTrophy().getSmileyFaces());
                    }
                    if (userScore >= greenTrophy.getSmileyFaces())
                    {
                        trophies.add(greenTrophy);
                        adapter.notifyItemInserted(SharedGamesConstants.INDEX2);
                        nextTrophyName.setText(greenTrophy.getNextTrophy().getName());
                        nextTrophyNum.setText("" + greenTrophy.getNextTrophy().getSmileyFaces());
                    }
                    if (userScore >= blueTrophy.getSmileyFaces())
                    {
                        trophies.add(blueTrophy);
                        adapter.notifyItemInserted(SharedGamesConstants.INDEX3);
                        nextTrophyName.setText(blueTrophy.getNextTrophy().getName());
                        nextTrophyNum.setText("" + blueTrophy.getNextTrophy().getSmileyFaces());
                    }
                    if (userScore >= purpleTrophy.getSmileyFaces())
                    {
                        trophies.add(purpleTrophy);
                        adapter.notifyItemInserted(SharedGamesConstants.INDEX4);
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
