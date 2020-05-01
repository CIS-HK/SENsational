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
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
//import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
//import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cis.sensational.Controller.Login.LoginActivity;
import edu.cis.sensational.Controller.Post.PostActivity;
import edu.cis.sensational.Controller.Post.ViewPostActivity;
import edu.cis.sensational.Controller.Profile.ProfileActivity;
import edu.cis.sensational.Controller.Profile.ProfileFragment;
import edu.cis.sensational.Model.Post;
import edu.cis.sensational.Model.User;
import edu.cis.sensational.R;
import edu.cis.sensational.Model.Utils.FirebaseMethods;
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
    private Button starPageButton;

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

        setUpRecyclerView();

    }

    public void setUpRecyclerView(){
        recView = findViewById(R.id.recView);
        final ArrayList<Post> values = new ArrayList<>();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference attendanceRef = rootRef
                .child("user_posts")
                .child(userID);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Post value = ds.getValue(Post.class);
                    values.add(value);
                }
                showPosts(values);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        };
        attendanceRef.addListenerForSingleValueEvent(valueEventListener);
    }

    public void showPosts(ArrayList<Post> values){

        ArrayList<String> titleList = new ArrayList<>();
        ArrayList<String> descriptionList = new ArrayList<>();
        ArrayList<String> IDList = new ArrayList<>();


        for(Post post: values)
        {
            titleList.add(post.getTitle());
            descriptionList.add(post.getDescription());
            IDList.add(post.getPostID());
        }

        myAdapter = new HomeAdapter(titleList, descriptionList, IDList);

        recView.setAdapter(myAdapter);
        recView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(context,
                        ViewPostActivity.class);
                String postID = myAdapter.itemClicked(position);
                intent.putExtra("Post", postID);
                startActivity(intent);
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
                Log.d(TAG, "searching for post");
                searchForTag(searchText);
            }
        });
    }

    public void searchForTag(String searchWord){

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
        profilePageButton = (Button) findViewById(R.id.profilePageButton);
        starPageButton = (Button) findViewById(R.id.starPageButton);

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        PostActivity.class);
                startActivity(intent);
            }
        });

        profilePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        ProfileActivity.class);
                startActivity(intent);
            }
        });

        starPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        ViewPostActivity.class);
                startActivity(intent);
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
