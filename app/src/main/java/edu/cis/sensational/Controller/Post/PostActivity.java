package edu.cis.sensational.Controller.Post;

import androidx.appcompat.app.AppCompatActivity;

import edu.cis.sensational.Controller.Login.RegisterActivity;
import edu.cis.sensational.Model.Post;
import edu.cis.sensational.Model.Utils.FirebaseMethods;
import edu.cis.sensational.R;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostActivity extends AppCompatActivity {

    private static final String TAG = "PostActivity";

    private EditText mTitle, mDescription, mTag;

    private String title, description, tag;

    private Button postButton;

    private Context mContext;

    private String userID;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }

        initWidgets();

        init();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        Log.d(TAG, "testing");

    }

    private void initWidgets(){
        Log.d(TAG, "initWidgets: Initializing Widgets.");
        mTitle = (EditText) findViewById(R.id.titleInput);
        mDescription = (EditText) findViewById(R.id.descriptionInput);
        mTag = (EditText) findViewById(R.id.tagInput);

        postButton = (Button) findViewById(R.id.newPostButton);

        mContext = PostActivity.this;
    }

    private void init(){
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = mTitle.getText().toString();
                description = mDescription.getText().toString();
                tag = mTag.getText().toString();

                if(checkInputs(title, description, tag)){
                    FirebaseMethods firebaseMethods = new FirebaseMethods(PostActivity.this);
                    firebaseMethods.createNewPost(title, description, tag);
                }
            }
        });
    }

    private boolean checkInputs(String title, String description, String tag){
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if(title.equals("") || description.equals("") || tag.equals("")){
            Toast.makeText(mContext, "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



}
