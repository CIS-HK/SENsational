package edu.cis.sensational.Model.Utils;

import android.content.Context;
import android.telecom.Call;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import edu.cis.sensational.Model.Colorize.GameConstants;
import edu.cis.sensational.Model.Comment;
import edu.cis.sensational.Model.Post;
import edu.cis.sensational.Model.User;
import edu.cis.sensational.Model.UserAccountSettings;
import edu.cis.sensational.Model.UserSettings;
import edu.cis.sensational.R;
//import edu.cis.instagramclone.View.materialcamera.MaterialCamera;
//import edu.cis.instagramclone.Model.Comment;
//import edu.cis.instagramclone.Model.Like;
//import edu.cis.instagramclone.Model.Photo;
//import edu.cis.instagramclone.Model.Story;

/**
 * Created by User on 6/26/2017.
 */

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private StorageReference mStorageReference;
    private String userID;

    //vars
    private Context mContext;
    private double mPhotoUploadProgress = 0;

    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mContext = context;

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    private String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
        sdf.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));
        return sdf.format(new Date());
    }

    /**
     * Creates a new Post object with the parameters
     * Calls to upload the Post to Firebase
     *
     * @param title
     * @param description
     * @param tags
     * @param privatePost
     *
     */
    public boolean createNewPost(String title, String description,
                                 String tags, boolean privatePost){

        // Create empty ArrayLists to initialize the Comments and Likes fields
        ArrayList<String> initLikes = new ArrayList();
        ArrayList<Comment> initComments = new ArrayList();

        // retrieve a key for the postID
        String newPostKey = myRef.child(mContext.getString(R.string.dbname_posts)).push().getKey();

        // create a new Post object and set the values given
        Post post = new Post();
        post.setTitle(title);
        post.setDescription(description);
        post.setTags(tags.toLowerCase());
        post.setUser_id(userID);
        post.setDate_created(getTimestamp());
        post.setLikeCount(0);
        post.setLikes(initLikes);
        post.setComments(initComments);
        post.setPrivate(privatePost);
        post.setPostID(newPostKey);

        // upload the post to the database
        uploadNewPost(post, tags);

        return true;
        // TODO figure out a way to listen back from the databse whether or not this has succeeded.
    }

    public void upvoteButtonPressed(final String post_id, final String userID, final Post post){

        post.setLikeCount(post.getLikeCount() + 1);

        final DatabaseReference userRef = myRef
                .child("posts")
                .child(post_id)
                .child("likes");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, "Post: upvoting");

                List<String> userLikesList = new ArrayList<>();
                userLikesList.add(userID);

                myRef.child(mContext.getString(R.string.dbname_posts))
                        .child(post_id)
                        .child(mContext.getString(R.string.field_likes))
                        .setValue(userLikesList);
                myRef.child(mContext.getString(R.string.dbname_user_posts))
                        .child(userID)
                        .child(post_id)
                        .child(mContext.getString(R.string.field_likes))
                        .setValue(userLikesList);
                myRef.child("user_likes")
                        .child(userID)
                        .child(post_id)
                        .setValue(post);

                upvote(post_id);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to retrieve number of likes.", error.toException());
            }
        });
    }

    public void upvote(String post_id){
        final String postID = post_id;

        final DatabaseReference userRef = myRef
                .child("posts")
                .child(postID)
                .child("likeCount");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final long newLikeCount = dataSnapshot.getValue(Long.class) + 1;

                myRef.child(mContext.getString(R.string.dbname_posts))
                        .child(postID)
                        .child(mContext.getString(R.string.field_like_count))
                        .setValue(newLikeCount);
                myRef.child(mContext.getString(R.string.dbname_user_posts))
                        .child(userID)
                        .child(postID)
                        .child(mContext.getString(R.string.field_like_count))
                        .setValue(newLikeCount);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to retrieve number of likes.", error.toException());
            }
        });
    }

    public void downvoteButtonPressed(final String post_id, final String userID, final Post post){

        post.setLikeCount(post.getLikeCount() - 1);

        final DatabaseReference userRef = myRef
                .child("posts")
                .child(post_id)
                .child("unlikes");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, "Post: downvoting");

                List<String> userUnLikesList = new ArrayList<>();
                userUnLikesList.add(userID);

                myRef.child(mContext.getString(R.string.dbname_posts))
                        .child(post_id)
                        .child("unlikes")
                        .setValue(userUnLikesList);
                myRef.child(mContext.getString(R.string.dbname_user_posts))
                        .child(userID)
                        .child(post_id)
                        .child("unlikes")
                        .setValue(userUnLikesList);
                myRef.child("user_unlikes")
                        .child(userID)
                        .child(post_id)
                        .setValue(post);

                downvote(post_id);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to retrieve number of likes.", error.toException());
            }
        });
    }

    public void downvote(String post_id){
        final String postID = post_id;

        final DatabaseReference userRef = myRef
                .child("posts")
                .child(postID)
                .child("likeCount");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue(Long.class) > 0){
                    final long newLikeCount = dataSnapshot.getValue(Long.class) - 1;

                    myRef.child(mContext.getString(R.string.dbname_posts))
                            .child(postID)
                            .child(mContext.getString(R.string.field_like_count))
                            .setValue(newLikeCount);
                    myRef.child(mContext.getString(R.string.dbname_user_posts))
                            .child(userID)
                            .child(postID)
                            .child(mContext.getString(R.string.field_like_count))
                            .setValue(newLikeCount);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to retrieve number of likes.", error.toException());
            }
        });
    }

    /**
     * Uploads the Post to Firebase under all relevant nodes
     * Database: user_posts, posts, tags nodes
     *
     * @param post
     * @param tag
     */
    public void uploadNewPost(Post post, String tag)
    {
        myRef.child("user_posts")
                .child(post.getUser_id())
                .child(post.getPostID())
                .setValue(post);
        myRef.child("posts")
                .child(post.getPostID())
                .setValue(post);
        myRef.child("tags")
                .child(tag)
                .child(post.getPostID())
                .setValue(post);
    }

    /**
     * Creates a new Comment object with the arguments
     * Stores the comment to the corresponding post both locally and on Firebase
     * Database: user_posts, posts node
     *
     * @param post
     * @param post_id
     * @param comment
     */
    public void makeComment(Post post, String post_id, String comment){
        // Create a new Comment instantiation and store the corresponding values
        Comment newComment = new Comment(comment, userID, 0, getTimestamp());
        // Retrieve the ArrayList of Comments already present for this post
        ArrayList<Comment> currentComments = post.getComments();
        // Add the new Comment to the ArrayList of Comments
        currentComments.add(newComment);
        // Set the Comments ArrayList with the newest addition to the corresponding Post
        post.setComments(currentComments);

        // Store the new ArrayList of Comments into the database under the corresponding Post
        myRef.child(mContext.getString(R.string.dbname_user_posts))
                .child(userID)
                .child(post_id)
                .child(mContext.getString(R.string.field_comments))
                .setValue(currentComments);
        myRef.child(mContext.getString(R.string.dbname_posts))
                .child(post_id)
                .child(mContext.getString(R.string.field_comments))
                .setValue(currentComments);
        Log.d(TAG, "New comment set.");
    }

    public void updateUserAccountSettings(String location, String age, String information) {

        Log.d(TAG, "updateUserAccountSettings: updating user account settings.");

        if (location != null) {
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_location))
                    .setValue(location);
        }

        if (age != null) {
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_age))
                    .setValue(age);
        }

        if (information != null) {
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_information))
                    .setValue(information);
        }
    }

    /**
     * update username in the 'users' node and 'user_account_settings' node
     *
     * @param username
     */
    public void updateUsername(String username) {
        Log.d(TAG, "updateUsername: updating username to: " + username);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);
    }

    /**
     * update the email in the 'users' node
     *
     * @param email
     */

    public void updateEmail(String email) {
        Log.d(TAG, "updateEmail: updating email to: " + email);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_email))
                .setValue(email);
    }

    public void updatePassword(String password) {
        Log.d(TAG, "updatePassword: updating password to: " + password);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_password))
                .setValue(password);
    }

    /**
     * Register a new email and password to Firebase Authentication
     *
     * @param email
     * @param password
     * @param username
     */

    public void registerNewEmail(final String email, String password, final String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) { // Check if user creation is successful
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        // If the task fails, display a message to the user.
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                            // If task succeeds, this happens
                        } else if (task.isSuccessful()) {
                            //sends verification email to the registration email inbox
                            sendVerificationEmail();
                            //the auth state listener will be notified and signed in user can be handled in the listener.
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authstate changed: " + userID);
                            Toast.makeText(mContext, "Signup successful. Sending verification email.", Toast.LENGTH_SHORT).show();
                            addNewUser(email, username);
                        }
                    }
                });
    }

    /**
     * Sends verification email to the email used to register new user
     */
    public void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            } else {
                                Toast.makeText(mContext, "couldn't send verification email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    /**
     * Add information to the users nodes
     * Add information to the user_account_settings node
     *
     * @param email
     * @param username
     */
    public void addNewUser(String email, String username) {

        // creates a new User object with the parameters given
        User user = new User(userID, StringManipulation.condenseUsername(username),
                email);

        // saves the user to the database
        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .setValue(user);

        // creates a new UserAccountSettings for this user
        UserAccountSettings settings = new UserAccountSettings(
                StringManipulation.condenseUsername(username),
                email,
                0,
                userID
        );

        // saves the settings to the databse
        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .setValue(settings);
    }


    /**
     * Retrieves the account settings for the user currently logged in
     * Database: user_account_settings node
     *
     * @param dataSnapshot
     * @return UserSettings
     */
    public UserSettings getUserSettings(DataSnapshot dataSnapshot) {
        Log.d(TAG, "getUserSettings: retrieving user account settings from firebase.");


        UserAccountSettings settings = new UserAccountSettings();
        User user = new User();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            // user_account_settings node
            if (ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))) {
                Log.d(TAG, "getUserSettings: user account settings node datasnapshot: " + ds);

                try {

                    settings.setUsername(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getUsername()
                    );

                    settings.setLocation(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getLocation()
                    );

                    settings.setChild_age(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getChild_age()
                    );

                    settings.setChild_gender(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getChild_gender()
                    );

                    settings.setPosts(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getPosts()
                    );

                    Log.d(TAG, "getUserAccountSettings: retrieved user_account_settings information: " + settings.toString());
                } catch (NullPointerException e) {
                    Log.e(TAG, "getUserAccountSettings: NullPointerException: " + e.getMessage());
                }
            }

            // users node
            Log.d(TAG, "getUserSettings: snapshot key: " + ds.getKey());
            if (ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                Log.d(TAG, "getUserAccountSettings: users node datasnapshot: " + ds);

                user.setUsername(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUsername()
                );
                user.setEmail(
                        ds.child(userID)
                                .getValue(User.class)
                                .getEmail()
                );
                user.setUser_id(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUser_id()
                );

                Log.d(TAG, "getUserAccountSettings: retrieved users information: " + user.toString());
            }
        }
        return new UserSettings(user, settings);

    }

    public Post getPost(DataSnapshot dataSnapshot){
        Log.d(TAG, "getPost: retrieving post information from firebase.");

        Post post = new Post();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            // posts node
            if (ds.getKey().equals(mContext.getString(R.string.dbname_posts))) {
                Log.d(TAG, "getUserSettings: posts node datasnapshot: " + ds);

                try {

                    post.setTitle(
                            ds.child(userID)
                                    .getValue(Post.class)
                                    .getTitle()
                    );

                    post.setDescription(
                            ds.child(userID)
                                    .getValue(Post.class)
                                    .getDescription()
                    );

                    post.setTags(
                            ds.child(userID)
                                    .getValue(Post.class)
                                    .getTags()
                    );

                    Log.d(TAG, "getPost: retrieved post information: " + post.toString());
                } catch (NullPointerException e) {
                    Log.e(TAG, "getPost: NullPointerException: " + e.getMessage());
                }
            }
        }
        return post;
    }

    /*
    ---------------------------------- Games Highscore ------------------------------------------
    */

    // https://stackoverflow.com/questions/47847694/how-to-return-datasnapshot-value-as-a-result-of-a-method

    /**
     * Creates a callback interface
     */
    public interface Callback
    {
        void onCallBack(int value);
    }

    /**
     * Uses user ID to update total user score on firebase by adding current user score and retrieve
     * it by using callbacks
     * @param userID
     * @param scoretoinsert
     * @param callback
     */
    public void updateUserScore(final String userID, final int scoretoinsert,
                                final Callback callback)
    {
        if (myRef != null)
        {
            final DatabaseReference userRef = myRef.child("user_scores")
                    .child("user_id")
                    .child(userID)
                    .child("user_score")
                    .child("totalscore");

                userRef.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        //add current user score to total score
                        if (dataSnapshot.getValue() != null)
                        {
                            int score = scoretoinsert + dataSnapshot.getValue(Integer.class);
                            userRef.setValue(score);
                            callback.onCallBack(score);
                        }

                        //for first time users, directly set the user score as total score
                        if (dataSnapshot.getValue() == null)
                        {
                            userRef.setValue(scoretoinsert);
                            callback.onCallBack(scoretoinsert);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        Log.w(TAG, "Failed to retrieve user score.", error.toException());
                    }
                });
        }
    }


    /**
     * Uses user ID to compare current user score with current highscore on the database
     * @param userID
     * @param score
     * @param callback
     */
    public void checkHighScore(final String userID, final int score, final Callback callback)
    {
        final DatabaseReference userRef = myRef.child("user_scores")
                .child("user_id")
                .child(userID)
                .child("user_score")
                .child("colorizehighscore");

        userRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.getValue() != null)
                {
                    int score = dataSnapshot.getValue(Integer.class);
                    callback.onCallBack(score);
                }
                if (dataSnapshot.getValue() == null)
                {
                    userRef.setValue(score);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.w(TAG, "Failed to retrieve user score.", error.toException());
            }
        });

    }

    /**
     * Uses user ID to store the updated user high score on Firebase
     * @param userID
     * @param score
     */
    public void storeHighScore(final String userID, final int score)
    {
        final DatabaseReference userRef = myRef.child("user_scores")
                .child("user_id")
                .child(userID)
                .child("user_score")
                .child("colorizehighscore");

        userRef.setValue(score);

    }

    /**
     * Uses user ID to set and display current score and high score for first time users
     * @param userID
     * @param score
     * @param currentScore
     * @param highScore
     */
    public void initialStoring(final String userID, final int score, final TextView currentScore,
                               final TextView highScore)
    {
        final DatabaseReference userRef = myRef.child("user_scores")
                .child("user_id")
                .child(userID)
                .child("user_score")
                .child("colorizehighscore");

        userRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //for first time users, their highscore is null, so the score for the first round
                //will be their current score and highscore
                if (dataSnapshot.getValue() == null)
                {
                    userRef.setValue(score);
                    currentScore.setText(""+score);
                    highScore.setText(GameConstants.DISPLAYHIGHSCORE +score);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}