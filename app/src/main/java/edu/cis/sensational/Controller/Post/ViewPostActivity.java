package edu.cis.sensational.Controller.Post;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import edu.cis.sensational.Controller.Home.HomeActivity;
import edu.cis.sensational.Model.Utils.FirebaseMethods;
import edu.cis.sensational.R;
import edu.cis.sensational.Model.Comment;
import edu.cis.sensational.Model.Likes;
import edu.cis.sensational.Model.Post;
import edu.cis.sensational.Model.User;
import edu.cis.sensational.Model.UserAccountSettings;

/**
 * Created by User on 8/12/2017.
 */

public class ViewPostActivity extends AppCompatActivity {

    private static final String TAG = "ViewPostActivity";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;


    //widgets
    private TextView title, description, tags, date;
    private Button backButton, upvote, downvote;

    //vars
    private Post mPost;
    private int mActivityNumber = 0;
    private String photoUsername = "";
    private String profilePhotoUrl = "";
    private UserAccountSettings mUserAccountSettings;
    private GestureDetector mGestureDetector;
//    private Heart mHeart;
    private Boolean mLikedByCurrentUser;
    private StringBuilder mUsers;
    private String mLikesString = "";
    private User mCurrentUser;

    private String currentPost;

    final Context context = this;

    private String userID;


    @Nullable


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        Log.d(TAG, "onCreate: started.");

        setupFirebaseAuth();

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }

        //https://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-android-application
        currentPost = getIntent().getStringExtra("Post");


        init();

        initWidgets();
    }

    private void initWidgets(){
        title = (TextView) findViewById(R.id.titleView);
        description = (TextView) findViewById(R.id.descriptionView);
        tags = (TextView) findViewById(R.id.tagView);
        date = (TextView) findViewById(R.id.dateView);

        backButton = (Button) findViewById(R.id.backButton);
        upvote = (Button) findViewById(R.id.upvoteButton);
        downvote = (Button) findViewById(R.id.downvoteButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        HomeActivity.class);
                startActivity(intent);
            }
        });

        upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "upvote button clicked");


                FirebaseMethods firebaseMethods = new FirebaseMethods(ViewPostActivity.this);
                firebaseMethods.upvoteButtonPressed(currentPost, userID, mPost);
            }
        });

        downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMethods firebaseMethods = new FirebaseMethods(ViewPostActivity.this);
//                firebaseMethods.setLikes(currentPost, 0, userID);
            }
        });
    }

    private void init(){
        try{
            String photo_id = currentPost;

            Query query = FirebaseDatabase.getInstance().getReference()
                    .child(getString(R.string.dbname_posts))
                    .orderByChild(getString(R.string.field_post_id))
                    .equalTo(photo_id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                        Post newPost = new Post();
                        Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                        newPost.setTitle(objectMap.get(getString(R.string.field_title)).toString());
                        newPost.setTags(objectMap.get(getString(R.string.field_tags)).toString());
                        newPost.setDescription(objectMap.get(getString(R.string.field_description)).toString());
                        newPost.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
                        newPost.setDate_created(objectMap.get(getString(R.string.field_date_created)).toString());

                        mPost = newPost;

                        getCurrentUser();
                        getPostDetails();
                        setupWidgets();

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: query cancelled.");
                }
            });

        }catch (NullPointerException e){
            Log.e(TAG, "onCreateView: NullPointerException: " + e.getMessage() );
        }
    }

    private void getCurrentUser(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_user_id))
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    mCurrentUser = singleSnapshot.getValue(User.class);
                }
//                getLikesString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
    }

    private void getPostDetails(){
        Log.d(TAG, "getPhotoDetails: retrieving photo details.");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_user_account_settings))
                .orderByChild(getString(R.string.field_user_id))
                .equalTo(mPost.getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    mUserAccountSettings = singleSnapshot.getValue(UserAccountSettings.class);
                }
                setupWidgets();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
    }



    private void setupWidgets() {

        title.setText(mPost.getTitle());
        description.setText(mPost.getDescription());
        tags.setText(mPost.getTags());
        date.setText(mPost.getDate_created().substring(0, 9));

    }

       /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}