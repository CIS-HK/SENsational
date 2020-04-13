package edu.cis.sensational.Controller.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

//import de.hdodenhof.circleimageview.CircleImageView;
import edu.cis.sensational.R;
//import edu.cis.sensational.Controller.Share.ShareActivity;
import edu.cis.sensational.Model.Utils.FirebaseMethods;
//import edu.cis.instagramclone.Model.Utils.UniversalImageLoader;
//import edu.cis.sensational.View.dialogs.ConfirmPasswordDialog;
import edu.cis.sensational.Model.User;
import edu.cis.sensational.Model.UserAccountSettings;
import edu.cis.sensational.Model.UserSettings;

/**
 * Created by User on 6/4/2017.
 */

public class EditProfileFragment extends Fragment {
//        implements
//        ConfirmPasswordDialog.OnConfirmPasswordListener{


//    @Override
    public void onConfirmPassword(String password) {
        Log.d(TAG, "onConfirmPassword: got the password: " + password);

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(mAuth.getCurrentUser().getEmail(), password);

        // Prompt the user to re-provide their sign-in credentials
        mAuth.getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "User re-authenticated.");

                            //check to see if the email is not already present in the database
                            mAuth.fetchProvidersForEmail(mEmail.getText()
                                    .toString())
                                    .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                            if(task.isSuccessful()){
                                                try{
                                                    if(task.getResult().getProviders().size() == 1){
                                                        Log.d(TAG, "onComplete: that email is already in use.");
                                                        Toast.makeText(getActivity(),
                                                                "That email is already in use",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        Log.d(TAG, "onComplete: That email is available.");

                                                        //the email is available so update it
                                                        mAuth.getCurrentUser()
                                                                .updateEmail(mEmail.getText().toString())
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Log.d(TAG, "User email address updated.");
                                                                            Toast.makeText(getActivity(), "email updated", Toast.LENGTH_SHORT).show();
                                                                            mFirebaseMethods.updateEmail(mEmail.getText().toString());
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }catch (NullPointerException e){
                                                    Log.e(TAG, "onComplete: NullPointerException: "  +e.getMessage() );
                                                }
                                            }
                                        }
                                    });





                        }else{
                            Log.d(TAG, "onComplete: re-authentication failed.");
                        }

                    }
                });
    }

    private static final String TAG = "EditProfileFragment";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;
    private String userID;


    //EditProfile Fragment widgets
    private EditText mPassword, mUsername, mLocation, mAge, mEmail, mInfo;

    //vars
    private UserSettings mUserSettings;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        mUsername = (EditText) view.findViewById(R.id.editUsername);
        mPassword = (EditText) view.findViewById(R.id.editPassword);
        mEmail = (EditText) view.findViewById(R.id.editEmail);
        mLocation = (EditText) view.findViewById(R.id.editLocation);
        mAge = (EditText) view.findViewById(R.id.editChildAge);
        mInfo = (EditText) view.findViewById(R.id.editInfo);

        mFirebaseMethods = new FirebaseMethods(getActivity());

        setupFirebaseAuth();

        //back arrow for navigating back to "ProfileActivity"
////        ImageView backArrow = (ImageView) view.findViewById(R.id.backArrow);
//        backArrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: navigating back to ProfileActivity");
//                getActivity().finish();
//            }
//        });

        Button btnSave = (Button) view.findViewById(R.id.saveProfileButton);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to save changes.");
                /*** TODO 4a: call the method that will save the profile settings when the checkmark is clicked ***/
                saveProfileSettings();
            }
        });

        return view;
    }


    /**
     * Retrieves the data contained in the widgets and submits it to the database
     * Before donig so it chekcs to make sure the username chosen is unqiue
     */
    private void saveProfileSettings(){
        final String username = mUsername.getText().toString();
        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();

        final String location = mLocation.getText().toString();
        final String age = mAge.getText().toString();
        final String info = mInfo.getText().toString();

        //TODO 4b : This is a really bad way to update a database. Fix this so that is uses one single UserSettings Object and a single call to updateUserAccountSettings.
        //update displayname
        mFirebaseMethods.updateUserAccountSettings(location, age, info);
        mFirebaseMethods.updateUsername(username);
        mFirebaseMethods.updateEmail(email);
        mFirebaseMethods.updatePassword(password);
    }

    /**
     * Check is @param username already exists in teh database
     * @param username
     */
    private void checkIfUsernameExists(final String username) {
        Log.d(TAG, "checkIfUsernameExists: Checking if  " + username + " already exists.");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){
                    //add the username
                    mFirebaseMethods.updateUsername(username);
                    Toast.makeText(getActivity(), "saved username.", Toast.LENGTH_SHORT).show();

                }
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    if (singleSnapshot.exists()){
                        Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + singleSnapshot.getValue(User.class).getUsername());
                        Toast.makeText(getActivity(), "That username already exists.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setProfileWidgets(UserSettings userSettings){
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.toString());
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.getUser().getEmail());

        mUserSettings = userSettings;
        //User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();
        mUsername.setText(settings.getUsername());
        mEmail.setText(userSettings.getUser().getEmail());
        mAge.setText(userSettings.getUser().getUser_child_age());
        mLocation.setText(userSettings.getUser().getUser_location());
        mInfo.setText(userSettings.getUser().getUser_child_profile());
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
        userID = mAuth.getCurrentUser().getUid();

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
