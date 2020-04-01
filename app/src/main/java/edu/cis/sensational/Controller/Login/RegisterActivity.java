package edu.cis.sensational.Controller.Login;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import edu.cis.sensational.R;
import edu.cis.sensational.Model.Utils.FirebaseMethods;
import edu.cis.sensational.Model.Utils.User;

/**
 * Created by User on 6/19/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private Context mContext;
    private String email, username, password, fourDigit, location, age, profile;
    private EditText mEmail, mPassword, mUsername, mFourDigit, mLocation, mAge, mProfile;
    private Button mRegisterButton;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private String append = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = RegisterActivity.this;
        firebaseMethods = new FirebaseMethods(mContext);
        Log.d(TAG, "onCreate: started.");

        initWidgets();
        setupFirebaseAuth();
        init();
    }

    private void init(){
        mRegisterButton.setOnClickListener(new View.OnClickListener() { /***** Part 2: this a creates listener for the register button and register a new email *****/
        @Override
        public void onClick(View v) { //when the button is clicked, this happens
            email = mEmail.getText().toString();
            username = mUsername.getText().toString();
            password = mPassword.getText().toString();
            fourDigit = mFourDigit.getText().toString();
            location = mLocation.getText().toString();
            age = mAge.getText().toString();
            profile = mProfile.getText().toString();


            if(checkInputs(email, username, password)){ /*** TODO 2b: check if user input something valid, look at the email, username and password EditTexts and check that they aren't empty ***/
                /*** TODO 2c : if true, set mProgressBar and loadingPleasWait visibility to View.VISIBLE ***/
                firebaseMethods.registerNewEmail(email, password, username);

                /*** TODO 2d: Use a firebaseMethod to register the new email, you will to looks at the FirebaseMethods class ***/
            }
        }
        });
    }

    private boolean checkInputs(String email, String username, String password){
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if(email.equals("") || username.equals("") || password.equals("")){
            Toast.makeText(mContext, "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    /**
     * Initialize the activity widgets
     */
    private void initWidgets(){
        Log.d(TAG, "initWidgets: Initializing Widgets.");
        mEmail = (EditText) findViewById(R.id.input_email);
        mUsername = (EditText) findViewById(R.id.input_username);
        mPassword = (EditText) findViewById(R.id.input_password);
        mPassword = (EditText) findViewById(R.id.input_password);
        mLocation = (EditText) findViewById(R.id.input_location);
        mRegisterButton = (Button) findViewById(R.id.registerButton);
        mContext = RegisterActivity.this;
    }

    private boolean isStringNull(String string){
        Log.d(TAG, "isStringNull: checking string if null.");

        if(string.equals("")){
            return true;
        }
        else{
            return false;
        }
    }

     /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * Check is @param username already exists in the database
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

                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    if (singleSnapshot.exists()){
                        Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + singleSnapshot.getValue(User.class).getUsername());
                        append = myRef.push().getKey().substring(3,10);
                        Log.d(TAG, "onDataChange: username already exists. Appending random string to name: " + append);
                    }
                }

                String mUsername = "";
                mUsername = username + append;

                //add new user to the database
                firebaseMethods.addNewUser(email, username, location, age, profile);

                Toast.makeText(mContext, "Signup successful. Sending verification email.", Toast.LENGTH_SHORT).show();

                mAuth.signOut();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

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

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            checkIfUsernameExists(username);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    finish();

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