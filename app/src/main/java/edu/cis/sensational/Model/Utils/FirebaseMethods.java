package edu.cis.sensational.Model.Utils;

import android.content.Context;
import android.util.Log;
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
import edu.cis.sensational.Model.Comment;
import edu.cis.sensational.Model.Post;
import edu.cis.sensational.Model.User;
import edu.cis.sensational.Model.UserAccountSettings;
import edu.cis.sensational.R;

/**
 * @author Nicole Xiang
 * Created on 23/03/2020.
 */

public class FirebaseMethods
{
    // firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private StorageReference mStorageReference;
    private String userID;

    // vars
    private Context mContext;
    private static final String TAG = "FirebaseMethods";

    public FirebaseMethods(Context context)
    {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mContext = context;
        // Checks if there's a User logged in
        if (mAuth.getCurrentUser() != null)
        {
            userID = mAuth.getCurrentUser().getUid();   // Retrieves the current User's userID
        }
    }

    /**
     * Retrieves the instantaneous time when the method is called
     *
     * @return sdf
     */
    private String getTimestamp()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"
                , Locale.CANADA);
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
                                 String tags, boolean privatePost)
    {
        // Create empty ArrayLists to initialize the comments and likes fields
        ArrayList<String> initLikes = new ArrayList();
        ArrayList<Comment> initComments = new ArrayList();
        // Retrieve a key for the postID
        String newPostKey = myRef.child(mContext.getString(R.string.dbname_posts)).push().getKey();
        // Create a new Post object and set the values given
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
        // Upload the post to the database
        uploadNewPost(post, tags);
        return true;
    }

    /**
     * Updates the likes String of the corresponding Post on database
     * Accesses the posts, user_posts, and user_likes nodes
     * Adds the UserID of the User that liked the Post to Firebase
     *
     * @param post_id
     * @param userID
     * @param post
     *
     */
    public void upvoteButtonPressed(final String post_id, final String userID, final Post post)
    {
        // Set the local Post object's like count to current + 1
        post.setLikeCount(post.getLikeCount() + 1);
        final DatabaseReference userRef = myRef
                .child(mContext.getString(R.string.dbname_posts))
                .child(post_id)
                .child(mContext.getString(R.string.dbname_user_likes));
        userRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
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
                myRef.child(mContext.getString(R.string.dbname_user_likes))
                        .child(userID)
                        .child(post_id)
                        .setValue(post);
                upvote(post_id);
            }
            @Override
            public void onCancelled(DatabaseError error)
            {
                Log.w(TAG, "Failed to retrieve number of likes.", error.toException());
            }
        });
    }

    /**
     * Searches through the Database to retrieve the post's current upvote number and adds one
     *
     * @param post_id
     */
    public void upvote(String post_id){
        final String postID = post_id;
        final DatabaseReference userRef = myRef
                .child(mContext.getString(R.string.dbname_posts))
                .child(postID)
                .child(mContext.getString(R.string.field_like_count));
        userRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
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
            public void onCancelled(DatabaseError error)
            {
                Log.w(TAG, "Failed to retrieve number of likes.", error.toException());
            }
        });
    }

    /**
     * Updates the unlikes String of the corresponding Post on database
     * Accesses the posts, user_posts, and user_unlikes nodes
     * Adds the UserID of the User that unliked the Post to Firebase
     *
     * @param post_id
     * @param userID
     * @param post
     *
     */
    public void downvoteButtonPressed(final String post_id, final String userID, final Post post)
    {
        // Set the local Post object's like count to current -1
        post.setLikeCount(post.getLikeCount() - 1);
        final DatabaseReference userRef = myRef
                .child(mContext.getString(R.string.field_posts))
                .child(post_id)
                .child(mContext.getString(R.string.field_unlikes));
        userRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.d(TAG, "Post: downvoting");
                List<String> userUnLikesList = new ArrayList<>();
                userUnLikesList.add(userID);
                myRef.child(mContext.getString(R.string.dbname_posts))
                        .child(post_id)
                        .child(mContext.getString(R.string.field_unlikes))
                        .setValue(userUnLikesList);
                myRef.child(mContext.getString(R.string.dbname_user_posts))
                        .child(userID)
                        .child(post_id)
                        .child(mContext.getString(R.string.field_unlikes))
                        .setValue(userUnLikesList);
                myRef.child(mContext.getString(R.string.dbname_user_unlikes))
                        .child(userID)
                        .child(post_id)
                        .setValue(post);
                downvote(post_id);
            }
            @Override
            public void onCancelled(DatabaseError error)
            {
                Log.w(TAG, "Failed to retrieve number of likes.", error.toException());
            }
        });
    }

    /**
     * Searches through the Database to retrieve the post's current upvote number and minuses one
     *
     * @param post_id
     */
    public void downvote(String post_id)
    {
        final String postID = post_id;
        final DatabaseReference userRef = myRef
                .child(mContext.getString(R.string.field_posts))
                .child(postID)
                .child(mContext.getString(R.string.field_like_count));
        userRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.getValue(Long.class) > 0)
                {
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
            public void onCancelled(DatabaseError error)
            {
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
        // Store Post under "user_posts" node
        myRef.child(mContext.getString(R.string.dbname_user_posts))
                .child(post.getUser_id())
                .child(post.getPostID())
                .setValue(post);
        // Store Post under "posts" node
        myRef.child(mContext.getString(R.string.dbname_posts))
                .child(post.getPostID())
                .setValue(post);
        // Store Post under "tags" node
        myRef.child(mContext.getString(R.string.dbname_tags))
                .child(tag.toLowerCase())
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
    public void makeComment(Post post, String post_id, String comment)
    {
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

    /**
     * Register a new email and password to Firebase authentication
     *
     * @param email
     * @param password
     * @param username
     */
    public void registerNewEmail(final String email, String password, final String username)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) { // Check if user creation is successful
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        // If the task fails, display a message to the user.
                        if (!task.isSuccessful())
                        {
                            Log.d(TAG, R.string.auth_failed + "");
                            Toast.makeText(mContext, R.string.register_failed,
                                    Toast.LENGTH_SHORT).show();
                            // If task succeeds, this happens
                        }
                        else if (task.isSuccessful())
                        {
                            // sends verification email to the registration email inbox
                            sendVerificationEmail();
                            // the auth state listener will be notified and
                            // signed in user can be handled in the listener.
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authstate changed: " + userID);
                            Toast.makeText(mContext, R.string.register_success,
                                    Toast.LENGTH_SHORT).show();
                            addNewUser(email, username);
                        }
                    }
                });
    }

    /**
     * Sends verification email to the email used to register new user
     */
    public void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // If sending fails
                            if (!(task.isSuccessful()))
                            {
                                Toast.makeText(mContext, R.string.email_fail
                                        , Toast.LENGTH_SHORT).show();
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
    public void addNewUser(String email, String username)
    {
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

    /*
    ---------------------------------- Games Highscore ------------------------------------------
    */

    // https://stackoverflow.com/questions/47847694/how-to-return-datasnapshot-value-as-a-result-of-a-method

    public interface Callback
    {
        void onCallBack(int value);
    }

    public void updateUserScore(final String userID, final int scoretoinsert,
                                final Callback callback) {
        if (myRef != null) {
            final DatabaseReference userRef = myRef.child("user_scores")
                    .child("user_id")
                    .child(userID)
                    .child("user_score")
                    .child("totalscore");

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            int score = scoretoinsert + dataSnapshot.getValue(Integer.class);
                            userRef.setValue(score);
                            callback.onCallBack(score);
                        }
                        if (dataSnapshot.getValue() == null)
                        {
                            userRef.setValue(scoretoinsert);
                            callback.onCallBack(scoretoinsert);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w(TAG, "Failed to retrieve user score.", error.toException());
                    }
                });
        }
    }


    public void checkHighScore(final String userID, final Callback callback)
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
                    userRef.setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.w(TAG, "Failed to retrieve user score.", error.toException());
            }
        });
    }

    public void storeHighScore(final String userID, final int score)
    {
        final DatabaseReference userRef = myRef.child("user_scores")
                .child("user_id")
                .child(userID)
                .child("user_score")
                .child("colorizehighscore");

        userRef.setValue(score);

    }
}