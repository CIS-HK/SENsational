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

public class TrophiesActivity extends AppCompatActivity {
    private Button back;
    private RecyclerView allTrophies;
    private ArrayList<Trophy> trophies;
    private int userScore;
    private TextView totalScore;
    private FirebaseAuth mAuth;
    private String userID;

    private static final String TAG = "TrophiesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophies);

        totalScore = findViewById(R.id.totalScore2);
        back = findViewById(R.id.backB);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrophiesActivity.this, GamesSharedActivity.class));
            }
        });

        allTrophies = findViewById(R.id.trophiesRecyclerView);

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

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
        if (userID != null) {
            final TrophiesAdapter adapter = new TrophiesAdapter(trophies);
            allTrophies.setAdapter(adapter);
            allTrophies.setLayoutManager(new LinearLayoutManager(this));

            FirebaseMethods firebaseMethods = new FirebaseMethods(TrophiesActivity.this);
            firebaseMethods.updateUserScore(userID, 0, new FirebaseMethods.Callback() {
                @Override
                public void onCallBack(int value) {
                    userScore = value;
                    totalScore.setText("" + value);
                    if (userScore >= redTrophy.getSmileyFaces()) {
                        trophies.add(redTrophy);
                        // https://stackoverflow.com/questions/31367599/how-to-update-recyclerview-adapter-data
                        adapter.notifyItemInserted(0);
                    }
                    if (userScore >= orangeTrophy.getSmileyFaces()) {
                        trophies.add(orangeTrophy);
                        adapter.notifyItemInserted(1);
                    }
                    if (userScore >= greenTrophy.getSmileyFaces()) {
                        trophies.add(greenTrophy);
                        adapter.notifyItemInserted(2);
                    }
                    if (userScore >= blueTrophy.getSmileyFaces()) {
                        trophies.add(blueTrophy);
                        adapter.notifyItemInserted(3);
                    }
                    if (userScore >= purpleTrophy.getSmileyFaces()) {
                        trophies.add(purpleTrophy);
                        adapter.notifyItemInserted(4);
                    }

                }
            });
        }
    }
}
