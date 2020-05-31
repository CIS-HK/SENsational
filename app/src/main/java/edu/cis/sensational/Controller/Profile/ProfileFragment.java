package edu.cis.sensational.Controller.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.cis.sensational.Model.Post;
import edu.cis.sensational.R;
import edu.cis.sensational.Model.Utils.FirebaseMethods;
//import edu.cis.sensational.Model.Utils.GridImageAdapter;
//import edu.cis.sensational.Model.Utils.UniversalImageLoader;
import edu.cis.sensational.Model.UserAccountSettings;
import edu.cis.sensational.Model.UserSettings;

/**
 * Created by User on 6/29/2017.
 */

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    public interface OnGridImageSelectedListener{
        void onGridImageSelected(Post post, int activityNumber);
    }
    OnGridImageSelectedListener mOnGridImageSelectedListener;

    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMNS = 3;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;


    //widgets
    private TextView mUsername, mEmail, mLocation, mGender, mPassword, mChildGender, mChildAge;
    private Context mContext;

    private GridView gridView;

    private Button backButton;
    private TextView editProfile;

    //vars
    private int mFollowersCount = 0;
    private int mFollowingCount = 0;
    private int mPostsCount = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mUsername = (TextView) view.findViewById(R.id.username);
        mEmail = (TextView) view.findViewById(R.id.email);
        mLocation = (TextView) view.findViewById(R.id.location);
        mGender = (TextView) view.findViewById(R.id.gender);
        mPassword = (TextView) view.findViewById(R.id.password);
        mChildGender = (TextView) view.findViewById(R.id.childGender);
        mChildAge = (TextView) view.findViewById(R.id.childAge);

        editProfile  = (TextView) view.findViewById(R.id.textEditProfile);
        backButton = (Button) view.findViewById(R.id.backButton);

        mContext = getActivity();
        mFirebaseMethods = new FirebaseMethods(getActivity());
        Log.d(TAG, "onCreateView: stared.");

        setupFirebaseAuth();

//        getPostsCount();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to " + mContext.getString(R.string.edit_profile_fragment));
                Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
                intent.putExtra(getString(R.string.calling_activity), getString(R.string.profile_activity));
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        try{
            mOnGridImageSelectedListener = (OnGridImageSelectedListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
        super.onAttach(context);
    }
//
//    private void setupGridView(){
//        Log.d(TAG, "setupGridView: Setting up image grid.");
//
//        final ArrayList<Post> posts = new ArrayList<>();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        Query query = reference
//                .child(getString(R.string.dbname_user_photos))
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
//
//                    Post post = new Post();
//                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();
//
//                    try {
//                        post.setTitle(objectMap.get(getString(R.string.field_caption)).toString());
//                        post.setTags(objectMap.get(getString(R.string.field_tags)).toString());
//                        post.setPostID(objectMap.get(getString(R.string.field_post_id)).toString());
//                        post.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
//                        post.setDate_created(objectMap.get(getString(R.string.field_date_created)).toString());
//
////                        ArrayList<Comment> comments = new ArrayList<Comment>();
////                        for (DataSnapshot dSnapshot : singleSnapshot
////                                .child(getString(R.string.field_comments)).getChildren()) {
////                            Comment comment = new Comment();
////                            comment.setUser_id(dSnapshot.getValue(Comment.class).getUser_id());
////                            comment.setComment(dSnapshot.getValue(Comment.class).getComment());
////                            comment.setDate_created(dSnapshot.getValue(Comment.class).getDate_created());
////                            comments.add(comment);
////                        }
//
////                        post.setComments(comments);
//
////                        List<Like> likesList = new ArrayList<Like>();
////                        for (DataSnapshot dSnapshot : singleSnapshot
////                                .child(getString(R.string.field_likes)).getChildren()) {
////                            Like like = new Like();
////                            like.setUser_id(dSnapshot.getValue(Like.class).getUser_id());
////                            likesList.add(like);
////                        }
////                        photo.setLikes(likesList);
////                        photos.add(photo);
//                    }catch(NullPointerException e){
//                        Log.e(TAG, "onDataChange: NullPointerException: " + e.getMessage() );
//                    }
//                }
//
//                //setup our image grid
//                int gridWidth = getResources().getDisplayMetrics().widthPixels;
//                int imageWidth = gridWidth/NUM_GRID_COLUMNS;
//                gridView.setColumnWidth(imageWidth);
//
//                ArrayList<String> imgUrls = new ArrayList<String>();
//                for(int i = 0; i < posts.size(); i++){
//                    imgUrls.add(posts.get(i).getImage_path());
//                }
//                GridImageAdapter adapter = new GridImageAdapter(getActivity(),R.layout.layout_grid_imageview,
//                        "", imgUrls);
//                gridView.setAdapter(adapter);
//
//                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        mOnGridImageSelectedListener.onGridImageSelected(photos.get(position), ACTIVITY_NUM);
//                    }
//                });
//            }

//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d(TAG, "onCancelled: query cancelled.");
//            }
//        });
//    }

//    private void getPostsCount(){
//        mPostsCount = 0;
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        Query query = reference.child(getString(R.string.dbname_user_photos))
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
//                    Log.d(TAG, "onDataChange: found post:" + singleSnapshot.getValue());
//                    mPostsCount++;
//                }
//                mPosts.setText(String.valueOf(mPostsCount));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }


    private void setProfileWidgets(UserSettings userSettings) {
        //Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.toString());
        //Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.getSettings().getUsername());


        //User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();


//        Glide.with(getActivity())
//                .load(settings.getProfile_photo())
//                .into(mProfilePhoto);

        mUsername.setText(settings.getUsername());
        mLocation.setText(settings.getLocation());
//        mEmail.setText(settings.getEmail());
//        mGender.setText(String.valueOf(settings.getGender()));
        mChildAge.setText(String.valueOf(settings.getChild_age()));
        mChildGender.setText(String.valueOf(settings.getChild_gender()));

    }

//
//    /**
//     * Responsible for setting up the profile toolbar
//     */
//    private void setupToolbar(){
//
//        ((ProfileActivity)getActivity()).setSupportActionBar(toolbar);
//
//        profileMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: navigating to account settings.");
//                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
//                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//            }
//        });
//    }

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


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));

                //retrieve images for the user in question

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
