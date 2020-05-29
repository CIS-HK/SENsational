package edu.cis.sensational.Controller.Post;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import edu.cis.sensational.Controller.Home.HomeActivity;
import edu.cis.sensational.Model.Utils.FirebaseMethods;
import edu.cis.sensational.R;
import edu.cis.sensational.Model.Comment;
import edu.cis.sensational.Model.Post;
import edu.cis.sensational.Model.User;
import edu.cis.sensational.Model.UserAccountSettings;

/**
 * Created by Nicole Xiang on 29/04/2020.
 */

public class ViewPostActivity extends AppCompatActivity {

    private static final String TAG = "ViewPostActivity";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    //widgets
    private TextView title, description, tags, date, likes;
    private Button backButton, upvote, downvote, commentButton;
    private EditText comment;

    //vars
    private Post mPost;
    private User mCurrentUser;
    private UserAccountSettings mUserAccountSettings;
    private String currentUser;
    private String currentPost;
    private String commentText;
    private String userID;
    private RecyclerView commentsView;

    final Context context = this;

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

    }

    private void initWidgets(){
        // initialize the text widgets
        title = (TextView) findViewById(R.id.titleView);
        description = (TextView) findViewById(R.id.descriptionView);
        tags = (TextView) findViewById(R.id.tagView);
        date = (TextView) findViewById(R.id.dateView);
        likes = (TextView) findViewById(R.id.likes);
        comment = (EditText) findViewById(R.id.commentField);

        // initialize the buttons
        backButton = (Button) findViewById(R.id.backButton);
        upvote = (Button) findViewById(R.id.upvoteButton);
        downvote = (Button) findViewById(R.id.downvoteButton);
        commentButton = (Button) findViewById(R.id.commentButton);

        // initialize the recyclerView
        commentsView = (RecyclerView) findViewById(R.id.commentView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        HomeActivity.class);
                startActivity(intent);
            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the inputted comment text
                commentText = comment.getText().toString();
                // Call the makeComment method with the corresponding arguments
                FirebaseMethods firebaseMethods =
                        new FirebaseMethods(ViewPostActivity.this);
                firebaseMethods.makeComment(mPost, currentPost, commentText);
                // Update the comments displayed on the page
                displayComments();
                // Clear the comment input text box
                comment.setText("");
                // TODO need to check inputs to ensure they are valid.
            }
        });

        upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "upvote button clicked");
                // Call method to upvote the post in Firebase
                FirebaseMethods firebaseMethods =
                        new FirebaseMethods(ViewPostActivity.this);
                firebaseMethods.upvoteButtonPressed(currentPost, userID, mPost);
                ArrayList <String> likesList = mPost.getLikes();
                likesList.add(userID);
                mPost.setLikes(likesList);
                // Refresh the page with the new like count
                setUpVotes();
            }
        });

        downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "downvote button clicked");
                // Call method to downvote the post in Firebase
                FirebaseMethods firebaseMethods =
                        new FirebaseMethods(ViewPostActivity.this);
                firebaseMethods.downvoteButtonPressed(currentPost, userID, mPost);
                ArrayList <String> unlikes = mPost.getUnLikes();
                unlikes.add(userID);
                mPost.setUnLikes(unlikes);
                // Refresh the page with the new like count
                setUpVotes();
            }
        });

    }

    /**
     * Displays the RecyclerView with Comments for this Post
     */
    public void displayComments(){
        // Retrieve the current comments from the Post object
        ArrayList<Comment> commentsList = mPost.getComments();

        // Displays the comments on a RecyclerView adapter
        CommentsAdapter myAdapter = new CommentsAdapter(commentsList);
        commentsView.setAdapter(myAdapter);
        commentsView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void init(){
        try{
            // Create a new query that goes through the posts node
            Query query = FirebaseDatabase.getInstance().getReference()
                    .child("posts")
                    .orderByChild(getString(R.string.field_post_id))
                    .equalTo(currentPost); // Finds the post that matches the current PostID
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){

                        // Retrieves the Post and sets it to the class Post variable
                        mPost = singleSnapshot.getValue(Post.class);

                        // Calls the methods which sets up the information on the page
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

    /**
     * Retrieves the current user's information from Firebase
     */
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });

    }

    /**
     * Retrieves the user information of this Post from Firebase
     */
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

    /**
     * Sets up the display of the widgets on the page
     */
    private void setupWidgets() {
        // Retrieves the main post information and displays it on the page
        title.setText(mPost.getTitle());
        description.setText(mPost.getDescription());
        tags.setText(mPost.getTags());
        String postDate = mPost.getDate_created().substring(0,10);
        date.setText(postDate);

        // Retrieves the comments from this post and displays using RecyclerView
        ArrayList<Comment> commentsList = mPost.getComments();
        CommentsAdapter myAdapter = new CommentsAdapter(commentsList);
        commentsView.setAdapter(myAdapter);
        commentsView.setLayoutManager(new LinearLayoutManager(this));

        // Checks if the user has upvoted / downvoted this post
        setUpVotes();
        likes.setText(mPost.getLikeCount() + "");
    }

    /**
     * Sets up the display for the voting function of the page
     * Checks whether the user has liked/unliked this post and reflects that information
     * Disables the buttons if the user has already made their vote
     * Displays the vote count
     */
    private void setUpVotes()
    {
        for(String like: mPost.getLikes()){
            if(like.equals(userID)){
                upvote.setBackgroundColor(Color.BLUE);
                upvote.setEnabled(false);
                downvote.setEnabled(false);
                upvote.setTextColor(Color.WHITE);
            }
        }

        for(String unlike: mPost.getUnLikes()){
            if (unlike.equals(userID)) {
                downvote.setBackgroundColor(Color.RED);
                downvote.setEnabled(false);
                upvote.setEnabled(false);
                downvote.setTextColor(Color.WHITE);
            }
        }

        likes.setText(mPost.getLikeCount() + "");

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