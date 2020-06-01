package edu.cis.sensational.Controller.Post;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
 * @author Nicole Xiang
 * Created on 23/03/2020.
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

    /**
     * Sets up all the widgets on the page
     */
    private void initWidgets()
    {
        // initialize the text widgets
        title = (TextView) findViewById(R.id.titleView);
        description = (TextView) findViewById(R.id.descriptionView);
        tags = (TextView) findViewById(R.id.tagView);
        date = (TextView) findViewById(R.id.dateView);
        likes = (TextView) findViewById(R.id.likes);
        comment = (EditText) findViewById(R.id.commentField);

        //https://stackoverflow.com/questions/1748977/making-textview-scrollable-on-android
        description.setMovementMethod(new ScrollingMovementMethod());

        // Initialize the buttons
        backButton = (Button) findViewById(R.id.backButton);
        upvote = (Button) findViewById(R.id.upvoteButton);
        downvote = (Button) findViewById(R.id.downvoteButton);
        commentButton = (Button) findViewById(R.id.commentButton);

        // Initialize the recyclerView
        commentsView = (RecyclerView) findViewById(R.id.commentView);

        // Bring the user back to the home feed page if back button is pressed
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context,
                        HomeActivity.class);
                startActivity(intent);
            }
        });

        // Call the makeComment method if comment button is pressed
        commentButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Get the inputted comment text
                commentText = comment.getText().toString();
                // Checks if the text is invalid (ie. is empty or just filled with spaces)
                if (commentText.isEmpty() || commentText.
                        replaceAll(" ","").isEmpty())
                {
                    // Prompt the user to input text
                    Toast.makeText(context, "Please input some text."
                            , Toast.LENGTH_SHORT).show();
                }
                // If text is valid
                else
                {
                    // Call the makeComment method with the corresponding arguments
                    FirebaseMethods firebaseMethods =
                            new FirebaseMethods(ViewPostActivity.this);
                    firebaseMethods.makeComment(mPost, currentPost, commentText);
                    // Update the comments displayed on the page
                    displayComments();
                    // Clear the comment input text box
                    comment.setText("");
                }
            }
        });

        // Call the upvote method if the upvote button is pressed
        upvote.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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

        // Call the downvote method if the downvote button is pressed
        downvote.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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
    public void displayComments()
    {
        // Retrieve the current comments from the Post object
        ArrayList<Comment> commentsList = mPost.getComments();
        // Displays the comments on a RecyclerView adapter
        CommentsAdapter myAdapter = new CommentsAdapter(commentsList);
        commentsView.setAdapter(myAdapter);
        commentsView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Searches through Firebase for the desired Post
     */
    private void init()
    {
        try{
            // Create a new query that goes through the posts node
            Query query = FirebaseDatabase.getInstance().getReference()
                    .child("posts")
                    .orderByChild(getString(R.string.field_post_id))
                    .equalTo(currentPost); // Finds the post that matches the current PostID
            query.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    for (DataSnapshot singleSnapshot :  dataSnapshot.getChildren())
                    {
                        // Retrieves the Post and sets it to the class Post variable
                        mPost = singleSnapshot.getValue(Post.class);
                        // Calls the methods which sets up the information on the page
                        getCurrentUser();
                        getPostDetails();
                        setupWidgets();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    Log.d(TAG, "onCancelled: query cancelled.");
                }
            });

        }
        catch (NullPointerException e)
        {
            Log.e(TAG, "onCreateView: NullPointerException: " + e.getMessage() );
        }
    }

    /**
     * Retrieves the current user's information from Firebase
     */
    private void getCurrentUser()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_user_id))
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot singleSnapshot :  dataSnapshot.getChildren())
                {
                    mCurrentUser = singleSnapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });

    }

    /**
     * Retrieves the user information of this Post from Firebase
     */
    private void getPostDetails()
    {
        Log.d(TAG, "getPostDetails: retrieving post details.");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        // Query through the database to find the user's information
        Query query = reference
                .child(getString(R.string.dbname_user_account_settings))
                .orderByChild(getString(R.string.field_user_id))
                .equalTo(mPost.getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    // Store the user's information
                    mUserAccountSettings = singleSnapshot.getValue(UserAccountSettings.class);
                }
                // Set up the page
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
    private void setupWidgets()
    {
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
        // Loop through all the userIDs that liked this current Post
        for(String like: mPost.getLikes())
        {
            // If the system finds a match with the current User
            if(like.equals(userID))
            {
                upvote.setBackgroundColor(Color.BLUE);      // Set the upvote button to blue
                upvote.setTextColor(Color.WHITE);           // Set the upvote text to white
                upvote.setEnabled(false);                   // Disable the upvote button
                downvote.setEnabled(false);                 // Disable the downvote button
            }
        }
        // Loop through all the userIDs that disliked this current Post
        for(String unlike: mPost.getUnLikes())
        {
            // If the system finds a match with the current User
            if (unlike.equals(userID))
            {
                downvote.setBackgroundColor(Color.RED);     // Set the downvote button to red
                downvote.setTextColor(Color.WHITE);         // Set the downvote text to white
                downvote.setEnabled(false);                 // Disable the downvote button
                upvote.setEnabled(false);                   // Disable the upvote button
            }
        }
        // Retrieve the number of votes for this Post and display it on the page
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

        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null)
                {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }
                else
                {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}