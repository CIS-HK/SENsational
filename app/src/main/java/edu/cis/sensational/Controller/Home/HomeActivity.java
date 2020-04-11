package edu.cis.sensational.Controller.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ImageButton;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
//import com.nostra13.universalimageloader.core.ImageLoader;

import edu.cis.sensational.Controller.Login.LoginActivity;
import edu.cis.sensational.Controller.Profile.ProfileActivity;
//import edu.cis.sensational.Controller.Profile.ProfileFragment;
import edu.cis.sensational.R;
//import edu.cis.sensational.Model.Utils.FirebaseMethods;
//import edu.cis.sensational.Model.Utils.MainFeedListAdapter;
//import edu.cis.sensational.Model.Utils.SectionsPagerAdapter;
//import edu.cis.sensational.Model.Utils.UniversalImageLoader;
//import edu.cis.sensational.Model.Utils.ViewCommentsFragment;
//import edu.cis.sensational.Model.Photo;
//import edu.cis.sensational.View.opengl.AddToStoryDialog;
//import edu.cis.sensational.View.opengl.NewStoryActivity;

public class HomeActivity extends AppCompatActivity {
//        MainFeedListAdapter.OnLoadMoreItemsListener{

//    @Override
//    public void onLoadMoreItems() {
//        Log.d(TAG, "onLoadMoreItems: displaying more photos");
//        HomeFragment fragment = (HomeFragment)getSupportFragmentManager()
//                .findFragmentByTag("android:switcher:" + R.id.viewpager_container + ":" + mViewPager.getCurrentItem());
//        if(fragment != null){
//            fragment.displayMorePhotos();
//        }
//    }

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

    //widgets
    private ViewPager mViewPager;
    private FrameLayout mFrameLayout;
    private RelativeLayout mRelativeLayout;

    private ImageButton addPostButton;
    private ImageButton profilePageButton;
    private ImageButton starPageButton;

    final Context context = this;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        mViewPager = (ViewPager) findViewById(R.id.viewpager_container);
//        mFrameLayout = (FrameLayout) findViewById(R.id.container);
//        mRelativeLayout = (RelativeLayout) findViewById(R.id.relLayoutParent);

        setupFirebaseAuth();
//
//        initImageLoader();
//        setupBottomNavigationView();
//        setupViewPager();

        setUpButtons();


    }

    public void setUpButtons()
    {
        addPostButton = (ImageButton) findViewById(R.id.addPostButton);
        profilePageButton = (ImageButton) findViewById(R.id.profilePageButton);
        starPageButton = (ImageButton) findViewById(R.id.starPageButton);

        profilePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

//    public void onCommentThreadSelected(Photo photo, String callingActivity){
//        Log.d(TAG, "onCommentThreadSelected: selected a coemment thread");
//
//        ViewCommentsFragment fragment  = new ViewCommentsFragment();
//        Bundle args = new Bundle();
//        args.putParcelable(getString(R.string.photo), photo);
//        args.putString(getString(R.string.home_activity), getString(R.string.home_activity));
//        fragment.setArguments(args);
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.container, fragment);
//        transaction.addToBackStack(getString(R.string.view_comments_fragment));
//        transaction.commit();
//
//    }

//    public void hideLayout(){
//        Log.d(TAG, "hideLayout: hiding layout");
//        mRelativeLayout.setVisibility(View.GONE);
//        mFrameLayout.setVisibility(View.VISIBLE);
//    }
//
//
//    public void showLayout(){
//        Log.d(TAG, "hideLayout: showing layout");
//        mRelativeLayout.setVisibility(View.VISIBLE);
//        mFrameLayout.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        if(mFrameLayout.getVisibility() == View.VISIBLE){
//            showLayout();
//        }
//    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d(TAG, "onActivityResult: incoming result.");
//        // Received recording or error from MaterialCamera
//
//        if (requestCode == REQUEST_ADD_NEW_STORY) {
//            Log.d(TAG, "onActivityResult: incoming new story.");
//            if (resultCode == RESULT_ADD_NEW_STORY) {
//                Log.d(TAG, "onActivityResult: got the new story.");
//                Log.d(TAG, "onActivityResult: data type: " + data.getType());
//
//                final HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager_container + ":" + 1);
//                if (fragment != null) {
//
//                    FirebaseMethods firebaseMethods = new FirebaseMethods(this);
//                    firebaseMethods.uploadNewStory(data, fragment);
//
//                }
//                else{
//                    Log.d(TAG, "onActivityResult: could not communicate with home fragment.");
//                }
//            }
//        }
//    }
//
//
//    private void initImageLoader(){
//        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
//        ImageLoader.getInstance().init(universalImageLoader.getConfig());
//    }
//
//    /**
//     * Responsible for adding the 3 tabs: Camera, Home, Messages
//     */
//    private void setupViewPager(){
//        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new CameraFragment()); //index 0
//        adapter.addFragment(new HomeFragment()); //index 1
//        adapter.addFragment(new MessagesFragment()); //index 2
//        mViewPager.setAdapter(adapter);
//
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(mViewPager);
//
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_instagram_black);
//        tabLayout.getTabAt(2).setIcon(R.drawable.ic_arrow);
//    }
//
//    /**
//     * BottomNavigationView setup
//     */
//    private void setupBottomNavigationView(){
//        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
//        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
//        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
//        BottomNavigationViewHelper.enableNavigation(mContext, this,bottomNavigationViewEx);
//        Menu menu = bottomNavigationViewEx.getMenu();
//        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
//        menuItem.setChecked(true);
//    }


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
        mViewPager.setCurrentItem(HOME_FRAGMENT);
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
