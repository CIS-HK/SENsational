package edu.cis.sensational.Controller.Login;

import android.content.Context;
import android.content.Intent;
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

/**
 * @author Nicole Xiang
 * Created on 23/03/2020.
 */

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private Context mContext;
    private String email, username, password;
    private EditText mEmail, mPassword, mUsername;
    private Button btnRegister;
    private Button backButton;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private String append = "";

    final Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d(TAG, "onCreate: started.");
        mContext = RegisterActivity.this;
        firebaseMethods = new FirebaseMethods(mContext);
        initWidgets();
        init();
    }

    /**
     * Sets up the register page widgets
     * Sets up OnClickListener for the register button
     */
    private void init()
    {
        backButton = findViewById(R.id.backButton);
        // When the REGISTER button is clicked
        btnRegister.setOnClickListener(new View.OnClickListener()
        {
        @Override
        public void onClick(View v)
        {
            //Get email, username and password from EditTexts, store them in instance variables
            email = mEmail.getText().toString();
            username = mUsername.getText().toString();
            password = mPassword.getText().toString();
            // Check if the user input is valid (ie. check that the three variables aren't empty)
            if(checkInputs(email, username, password))
            {
                // Use a firebaseMethod to register the new email
                firebaseMethods.registerNewEmail(email, password, username);
                // Moves to the Login page
                Intent intent = new Intent(mContext,
                        LoginActivity.class);
                startActivity(intent);
            }
        }
        });

        // Sets up the back button to return to the login page
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext,
                        LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Checks that all the parameters are valid
     *
     * @param email
     * @param username
     * @param password
     *
     * @return boolean
     */
    private boolean checkInputs(String email, String username, String password)
    {
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        // Checking if any of the parameters are null
        if(email.equals("") || username.equals("") || password.equals(""))
        {
            Toast.makeText(mContext, "All fields must be filled out."
                    , Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;        // Returns true if all parameters pass
    }

    /**
     * Initialize the activity widgets
     */
    private void initWidgets()
    {
        Log.d(TAG, "initWidgets: Initializing Widgets.");
        mEmail = (EditText) findViewById(R.id.input_email);
        mUsername = (EditText) findViewById(R.id.input_username);
        mPassword = (EditText) findViewById(R.id.input_password);
        btnRegister = (Button) findViewById(R.id.signUpButton);
        mContext = RegisterActivity.this;
    }

     /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * Check if @param username already exists in the database
     * @param username
     */
    private void checkIfUsernameExists(final String username)
    {
        Log.d(TAG, "checkIfUsernameExists: Checking if  " + username + " already exists.");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_user_id))
                .orderByChild(getString(R.string.field_username));
        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren())
                {
                    if (singleSnapshot.equals(username))
                    {
                        Toast.makeText(mContext, "Username already exists. " +
                                "Please input another.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        // Add new user to the database
                        firebaseMethods.addNewUser(email, username);
                        Toast.makeText(mContext, "Sending verification email."
                                , Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Checking username failed.");
            }
        });
    }

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth()
    {
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

                    myRef.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            checkIfUsernameExists(username);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError)
                        {
                            Log.d(TAG, "authentication check failed.");
                        }
                    });
                    finish();
                }
                else
                {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(context,
                            LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart()
    {
        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
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
