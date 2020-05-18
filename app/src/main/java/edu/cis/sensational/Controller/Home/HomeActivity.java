package edu.cis.sensational.Controller.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
//import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
//import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import edu.cis.sensational.Controller.Login.LoginActivity;
import edu.cis.sensational.Controller.MainActivity;
import edu.cis.sensational.Controller.Post.PostActivity;
import edu.cis.sensational.Controller.Post.ViewPostActivity;
import edu.cis.sensational.Controller.Profile.ProfileActivity;
import edu.cis.sensational.Model.Post;
import edu.cis.sensational.R;
//import edu.cis.sensational.Model.Utils.MainFeedListAdapter;
//import edu.cis.sensational.Model.Utils.SectionsPagerAdapter;
//import edu.cis.sensational.Model.Utils.UniversalImageLoader;
//import edu.cis.sensational.Model.Utils.ViewCommentsFragment;
//import edu.cis.sensational.Model.Photo;
//import edu.cis.sensational.View.opengl.AddToStoryDialog;
//import edu.cis.sensational.View.opengl.NewStoryActivity;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;
    private static final int HOME_FRAGMENT = 1;
    private static final int RESULT_ADD_NEW_STORY = 7891;
    private final static int CAMERA_RQ = 6969;
    private static final int REQUEST_ADD_NEW_STORY = 8719;

    private Context mContext = HomeActivity.this;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;

    //widgets
    private ViewPager mViewPager;
    private FrameLayout mFrameLayout;
    private RelativeLayout mRelativeLayout;

    private Button addPostButton;
    private Button profilePageButton;
    private Button switchButton;
    private Button mainButton;

    private Button searchButton;
    private EditText searchField;

    private RecyclerView recView;
    HomeAdapter myAdapter;

    private String userID;

    final Context context = this;

    //vars
    private ArrayList<Post> mPostList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupFirebaseAuth();
        setUpButtons();
        setUpSearch();

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }

        setUpPublicRecyclerView();
    }


    public void setUpPublicRecyclerView(){
        // initialize the recyclerView
        recView = findViewById(R.id.recView);
        final ArrayList<Post> values = new ArrayList<>(); // create a new ArrayList to hold Posts

        // Look through the posts node
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference attendanceRef = rootRef
                .child("posts");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    // Get the Post object
                    Post value = ds.getValue(Post.class);
                    if(value.getPrivate() == false){
                        values.add(value);
                    }
                    // Add the Post to the list of Posts
                }
                // Display the Posts on the page
                showPosts(values);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        };
        attendanceRef.addListenerForSingleValueEvent(valueEventListener);
    }

    public void setUpPrivateRecyclerView(){
        // initialize the recyclerView
        recView = findViewById(R.id.recView);
        final ArrayList<Post> values = new ArrayList<>(); // create a new ArrayList to hold Posts

        // Look through the user_posts node
        Query query = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.dbname_user_posts))
                .child(userID)
                .orderByChild(getString(R.string.field_private))
                .equalTo(true); // check that the post was selected as private
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    // Get the Post object
                    Post value = singleSnapshot.getValue(Post.class);
                    // Add the Post to the list of Posts
                    values.add(value);
                }
                // Display the Posts on the page
                showPosts(values);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
    }

    public void showPosts(ArrayList<Post> values){

        // Create empty ArrayLists to separate the main information from each Post
        ArrayList<String> titleList = new ArrayList<>();
        ArrayList<String> descriptionList = new ArrayList<>();
        ArrayList<String> IDList = new ArrayList<>();

        // Fill the ArrayLists with the retrieved values
        for(Post post: values)
        {
            titleList.add(post.getTitle());
            descriptionList.add(post.getDescription());
            IDList.add(post.getPostID());
        }

        // inverting the order of the list so the most recent posts appear first
        //https://www.techiedelight.com/reverse-list-java-inplace/
        for (int i = 0, j = titleList.size() - 1; i < j; i++){
            titleList.add(i, titleList.remove(j));
            descriptionList.add(i, descriptionList.remove(j));
            IDList.add(i, IDList.remove(j));
        }

        // Initializing and setting up the RecyclerView with the three ArrayLists
        // Displaying the information on the page
        myAdapter = new HomeAdapter(titleList, descriptionList, IDList);
        recView.setAdapter(myAdapter);
        recView.setLayoutManager(new LinearLayoutManager(this));

        // Allowing the Posts to be clicked on, which then opens the Post in ViewPostActivity
        myAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(context,
                        ViewPostActivity.class);
                String postID = myAdapter.itemClicked(position); // Retrieves the selected postID
                intent.putExtra("Post", postID); // Passes the PostID into the next Activity
                intent.putExtra("User", userID); // Passes the UserID into the next Activity
                startActivity(intent); // Starts ViewPostActivity with the above information passed
            }
        });
    }

    public void setUpSearch(){
        searchField = (EditText) findViewById(R.id.searchField);
        searchButton = (Button) findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchField.getText().toString();
                if(!(searchText.equals("")))
                {
                    Log.d(TAG, "searching for post");
                    searchForTag(searchText);
                }
                else
                {
                    setUpPublicRecyclerView();
                }
            }
        });
    }

    public void searchForTag(String searchWord){

        //TODO make the search public/private sensitive
        //TODO make the search accept extreme values
        final ArrayList<Post> mPostList = new ArrayList<>();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference attendanceRef = rootRef
                .child("tags")
                .child(searchWord);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Post value = ds.getValue(Post.class);
                    mPostList.add(value);
                }
                showPosts(mPostList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        };
        attendanceRef.addListenerForSingleValueEvent(valueEventListener);
    }

    public void setUpButtons()
    {
        addPostButton = (Button) findViewById(R.id.addPostButton);
//        profilePageButton = (Button) findViewById(R.id.profilePageButton);
        switchButton = (Button) findViewById(R.id.switchButton);
        mainButton = (Button) findViewById(R.id.mainButton);

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        PostActivity.class);
                startActivity(intent);
            }
        });

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        MainActivity.class);
                startActivity(intent);
            }
        });

//        profilePageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context,
//                        ProfileActivity.class);
//                startActivity(intent);
//            }
//        });

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchButton.getText().equals("See Public")){
                    switchButton.setText("See Private");
                    setUpPublicRecyclerView();
                }
                else if(switchButton.getText().equals("See Private")){
                    switchButton.setText("See Public");
                    setUpPrivateRecyclerView();
                }
            }
        });
    }

     /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * checks to see if the @param 'user' is logged in
     * @param user
     */
    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: checking if user is logged in.");

        if(user == null){
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
    }
    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //check if the user is logged in
                checkCurrentUser(user);

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
//        mViewPager.setCurrentItem(HOME_FRAGMENT);
        checkCurrentUser(mAuth.getCurrentUser());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
