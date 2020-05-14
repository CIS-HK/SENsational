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
        final Trophy redTrophy = new Trophy(10, "Red Trophy");
        final Trophy orangeTrophy = new Trophy(20, "Orange Trophy");
        final Trophy yellowTrophy = new Trophy(50, "Yellow Trophy");
        final Trophy greenTrophy = new Trophy(100, "Green Trophy");
        final Trophy blueTrophy = new Trophy(200, "Blue Trophy");
        final Trophy purpleTrophy = new Trophy(400, "Purple Trophy");

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }

        final TrophiesAdapter adapter = new TrophiesAdapter(trophies);
        allTrophies.setAdapter(adapter);
        allTrophies.setLayoutManager(new LinearLayoutManager(this));

        FirebaseMethods firebaseMethods = new FirebaseMethods(TrophiesActivity.this);
        firebaseMethods.updateUserScore(userID, 0, new FirebaseMethods.Callback() {
            @Override
            public void onCallBack(int value) {
                userScore = value;
                totalScore.setText("" + value);
                if (userScore >= redTrophy.getSmileyFaces()){
                    trophies.add(redTrophy);
                    adapter.notifyItemInserted(0);
                }
                if (userScore >= orangeTrophy.getSmileyFaces()){
                    trophies.add(orangeTrophy);
                    adapter.notifyItemInserted(1);
                }
                if (userScore >= yellowTrophy.getSmileyFaces()){
                    trophies.add(yellowTrophy);
                    adapter.notifyItemInserted(2);
                }
                if (userScore >= greenTrophy.getSmileyFaces()){
                    trophies.add(greenTrophy);
                    adapter.notifyItemInserted(3);
                }
                if (userScore >= blueTrophy.getSmileyFaces()){
                    trophies.add(blueTrophy);
                    adapter.notifyItemInserted(4);
                }
                if (userScore >= purpleTrophy.getSmileyFaces()){
                    trophies.add(purpleTrophy);
                    adapter.notifyItemInserted(5);
                }

            }
        });
    }
}
