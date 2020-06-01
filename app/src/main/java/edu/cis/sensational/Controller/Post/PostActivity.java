package edu.cis.sensational.Controller.Post;

import androidx.appcompat.app.AppCompatActivity;

import edu.cis.sensational.Controller.Home.HomeActivity;
import edu.cis.sensational.Model.Utils.FirebaseMethods;
import edu.cis.sensational.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class PostActivity extends AppCompatActivity {

    // Firebase and context
    private static final String TAG = "PostActivity";
    final Context context = this;
    private Context mContext;
    private FirebaseAuth mAuth;

    // widgets
    private EditText mTitle, mDescription, mTag;
    private Button postButton, backButton;
    private Switch privateSwitch;

    // vars
    private String title, description, tag, userID;
    private boolean privatePost;

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
    }

    /**
     * Sets up all widgets on the page
     */
    private void initWidgets(){
        Log.d(TAG, "initWidgets: Initializing Widgets.");

        // Set up the text widgets
        mTitle = (EditText) findViewById(R.id.titleInput);
        mDescription = (EditText) findViewById(R.id.descriptionInput);
        mTag = (EditText) findViewById(R.id.tagInput);

        // Set up the buttons
        postButton = (Button) findViewById(R.id.newPostButton);
        backButton = (Button) findViewById(R.id.backButton);

        // Set up the switch
        privateSwitch = (Switch) findViewById(R.id.privateSwitch);
        privateSwitch.setChecked(false);

        // Set up the context
        mContext = PostActivity.this;
    }

    /**
     * Initializes the buttons for ClickListeners
     * Calls the posting methods or the return to homepage method on command
     */
    private void init(){
        // When the Post Button is clicked
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the inputted values
                title = mTitle.getText().toString();
                description = mDescription.getText().toString();
                tag = mTag.getText().toString();

                // Retrieve the status of the private/public posting setting
                if(privateSwitch.isChecked())               // If private posting was selected
                {
                    privatePost = true;                     // Set private status to true
                }
                else                                        // If public posting was selected
                {
                    privatePost = false;                    // Set private status to false
                }

                if(checkInputs(title, description, tag)){   // Check that the inputs are valid
                    // Call for the creation of a new Post on Firebase
                    FirebaseMethods firebaseMethods =
                            new FirebaseMethods(PostActivity.this);
                    // If posting was successful:
                    if(firebaseMethods.createNewPost(title, description, tag, privatePost)){
                        // Return to main page
                        Toast.makeText(mContext, "Successfully posted."
                                , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context,
                                HomeActivity.class);
                        startActivity(intent);
                    }
                    // If posting was unsuccessful:
                    else
                    {
                        // Prompt the user to try again
                        Toast.makeText(mContext, "Posting unsuccessful. Try again."
                                , Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Set up back button to return to homepage
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Checks that all the parameters are valid
     *
     * @param title
     * @param description
     * @param tag
     *
     * @return boolean
     */
    private boolean checkInputs(String title, String description, String tag){
        Log.d(TAG, "checkInputs: checking inputs for abnormal/unaccepted values.");

        // Splits the tag into separate words
        String [] tagArray = tag.trim().split(" ");

        // Checks if there are any null inputs
        if(title.equals("") || description.equals("") || tag.equals("")){
            Toast.makeText(mContext, "All fields must be filled out."
                    , Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (title.length() > 45){          // Checks if the title input exceeds 45 characters
            Toast.makeText(mContext,"Please input shorter title."
                    , Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(tagArray.length != 1){          // Checks if there were multiple words in the tag
            Toast.makeText(mContext,"Please input only one tag."
                    , Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(tag.length() > 10){             // Checks if the tag input exceeds 10 characters
            Toast.makeText(mContext,"Please input only one tag."
                    , Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(isNotAlpha(tag)){               // Checks if the tag input contains symbols
            Toast.makeText(mContext, "Please input a tag that only contains letters."
                    , Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;                            // Returns true if all inputs pass
    }

    /**
     * Checks that the parameter contains alphabetical letters only
     *
     * @param word
     *
     * @return boolean
     */
    public boolean isNotAlpha(String word) {
        char[] chars = word.toCharArray();      // Splits the word into individual characters
        for (char c : chars) {                  // Loops through the characters in the array
            if(!Character.isLetter(c)) {        // Checks if the character is a letter
                return true;
            }
        }
        return false;                           // Returns false if all characters are letters
    }
}