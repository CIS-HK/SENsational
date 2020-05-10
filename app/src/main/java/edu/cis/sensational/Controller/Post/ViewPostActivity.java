package edu.cis.sensational.Controller.Post;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.cis.sensational.Controller.Home.HomeActivity;
import edu.cis.sensational.Model.Utils.FirebaseMethods;
import edu.cis.sensational.R;
import edu.cis.sensational.Model.Comment;
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
    private TextView title, description, tags, date, likes;
    private Button backButton, upvote, downvote, commentButton;
    private EditText comment;
    private ImageButton heartButton;

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

    private String currentUser;

    private String currentPost;
    private String commentText;

    private RecyclerView commentsView;

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

        currentUser = getIntent().getStringExtra("User");

        init();

        initWidgets();
//        displayLikeCount();

    }

    private void initWidgets(){
        title = (TextView) findViewById(R.id.titleView);
        description = (TextView) findViewById(R.id.descriptionView);
        tags = (TextView) findViewById(R.id.tagView);
        date = (TextView) findViewById(R.id.dateView);
        likes = (TextView) findViewById(R.id.likes);
        comment = (EditText) findViewById(R.id.commentField);

        backButton = (Button) findViewById(R.id.backButton);
        upvote = (Button) findViewById(R.id.upvoteButton);
        downvote = (Button) findViewById(R.id.downvoteButton);
        commentButton = (Button) findViewById(R.id.commentButton);

        commentsView = (RecyclerView) findViewById(R.id.commentView);

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
                displayLikeCount();
            }
        });

        downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMethods firebaseMethods = new FirebaseMethods(ViewPostActivity.this);
                firebaseMethods.downvoteButtonPressed(currentPost, userID, mPost);
            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentText = comment.getText().toString();
                FirebaseMethods firebaseMethods = new FirebaseMethods(ViewPostActivity.this);
                firebaseMethods.makeComment(mPost, currentPost, commentText);
            }
        });
    }

    public void displayLikeCount(){

        final DatabaseReference userRef = myRef
                .child("user_likes")
                .child(userID);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (mPost.equals(post)) {
                        upvote.setBackgroundColor(Color.BLUE);
                    }
                    else{
                        upvote.setBackgroundColor(Color.WHITE);
                    }
                }

                Log.d(TAG, dataSnapshot +"");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed.", error.toException());
            }
        });


        likes.setText(mPost.getLikeCount() + "");
    }

    private void setUpHeart(){

        heartButton = (ImageButton) findViewById(R.id.heartButton);

        heartButton.setBackgroundResource(R.drawable.heart_hollow);

        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void init(){
        try{
            String photo_id = currentPost;

            Query query = FirebaseDatabase.getInstance().getReference()
                    .child(getString(R.string.dbname_user_posts))
                    .child(currentUser)
                    .orderByChild(getString(R.string.field_post_id))
                    .equalTo(photo_id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){

                        mPost = singleSnapshot.getValue(Post.class);

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
        Log.d(TAG, "getPostDetails: retrieving post details.");
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

        String postDate = mPost.getDate_created().substring(0,10);

        Log.d(TAG, "" + postDate);

        date.setText(postDate);
        ArrayList<Comment> commentsList = mPost.getComments();

        CommentsAdapter myAdapter = new CommentsAdapter(commentsList);
        commentsView.setAdapter(myAdapter);
        commentsView.setLayoutManager(new LinearLayoutManager(this));

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