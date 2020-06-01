package edu.cis.sensational.Controller.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.cis.sensational.Controller.Login.LoginActivity;
import edu.cis.sensational.Controller.MainActivity;
import edu.cis.sensational.Controller.Post.PostActivity;
import edu.cis.sensational.Controller.Post.ViewPostActivity;
import edu.cis.sensational.Model.Post;
import edu.cis.sensational.R;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private Context mContext = HomeActivity.this;

    // firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // buttons
    private Button addPostButton;
    private Button switchButton;
    private Button mainButton;
    private Button searchButton;
    private ImageButton refresh;

    // text widgets
    private EditText searchField;

    // recyclerView
    private RecyclerView recView;
    HomeAdapter myAdapter;

    // vars
    private String userID;
    final Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Set up the components of the page
        setupFirebaseAuth();
        setUpButtons();
        setUpSearch();
        // Retrieves the current user's ID
        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
        // Set up the page
        setUpPublicRecyclerView();
    }

    /**
     * Sets up the home page to display public Posts from all forum users
     */
    public void setUpPublicRecyclerView()
    {
        // initialize the recyclerView
        recView = findViewById(R.id.recView);
        final ArrayList<Post> values = new ArrayList<>(); // create a new ArrayList to hold Posts
        // Look through the posts node
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference attendanceRef = rootRef
                .child("posts");
        ValueEventListener valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    // Get the Post object
                    Post value = ds.getValue(Post.class);
                    if(value.getPrivate() == false)
                    {
                        values.add(value);
                    }
                    // Add the Post to the list of Posts
                }
                // Display the Posts on the page
                showPosts(values);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.d(TAG, databaseError.getMessage());
            }
        };
        attendanceRef.addListenerForSingleValueEvent(valueEventListener);
    }

    /**
     * Sets up the home page to display Posts created by the current user that were set to private
     */
    public void setUpPrivateRecyclerView()
    {
        // initialize the recyclerView
        recView = findViewById(R.id.recView);
        final ArrayList<Post> values = new ArrayList<>(); // create a new ArrayList to hold Posts
        // Look through the user_posts node
        Query query = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.dbname_user_posts))
                .child(userID)
                .orderByChild(getString(R.string.field_private))
                .equalTo(true); // check that the post was selected as private
        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot singleSnapshot :  dataSnapshot.getChildren())
                {
                    // Get the Post object
                    Post value = singleSnapshot.getValue(Post.class);
                    // Add the Post to the list of Posts
                    values.add(value);
                }
                // Display the Posts on the page
                showPosts(values);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
    }

    /**
     * Sets up the RecyclerView for the Posts
     * Sets up the OnCLickListener for the adapter to navigate to Posts
     *
     * @param values
     *
     */
    public void showPosts(ArrayList<Post> values)
    {
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
        // Inverting the order of the list so the most recent posts appear first
        // https://www.techiedelight.com/reverse-list-java-inplace/
        for (int i = 0, j = titleList.size() - 1; i < j; i++)
        {
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

    /**
     * Sets up the search widgets and onClick Listeners
     * Calls search method and refreshes GUI on command
     */
    public void setUpSearch()
    {
        // Set up the widgets
        searchField = (EditText) findViewById(R.id.searchField);
        searchButton = (Button) findViewById(R.id.searchButton);
        refresh = (ImageButton) findViewById(R.id.refreshButton);

        // When the search button is clicked
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the search word inputted and sets it to lowercase
                String searchText = searchField.getText().toString().toLowerCase();
                // Checks if the input is valid
                if(checkInputs(searchText))
                {
                    // Search for the posts with the inputted search word
                    Log.d(TAG, "Searching for posts.");
                    searchForTag(searchText);
                }
            }
        });

        // When the refresh button is clicked
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset the widgets on the page
                Toast.makeText(mContext, "Refreshing page.", Toast.LENGTH_SHORT).show();
                setUpPublicRecyclerView();
                switchButton.setText("See Private");
                searchField.setText("");
            }
        });
    }

    /**
     * Checks that the parameter is valid (ie. the searched word is appropriate)
     * Checks for null
     * Checks for multiple words entered
     * Checks for symbols
     *
     * @param text
     *
     * @return boolean
     */
    private boolean checkInputs(String text)
    {
        // Splits the text into separate words and stores each word in an array
        String [] array = text.trim().split(" ");

        // Checks if the text is null
        if(text.equals(""))
        {
            Log.d(TAG, "checkInputs: checking input for null.");
            Toast.makeText(mContext, "Please input a search word.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Checks if there were multiple words in the text
        else if(array.length != 1)
        {
            Log.d(TAG, "checkInputs: checking tag input for more than one value.");
            Toast.makeText(mContext,"Please input only one tag.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Checks if the tag input exceeds 10 characters
        else if(text.length() > 10)
        {
            Toast.makeText(mContext,"Please input a proper word.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Checks if the text contains symbols
        else if(isNotAlpha(text))
        {
            Log.d(TAG, "checkInputs: checking tag input ");
            Toast.makeText(mContext, "Please input a tag that only contains letters."
                    , Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Checks that the parameter contains alphabetical letters only
     *
     * @param word
     *
     * @return boolean
     */
    public boolean isNotAlpha(String word)
    {
        char[] chars = word.toCharArray();      // Splits the word into individual characters
        // Loops through the characters in the array
        for (char c : chars)
        {
            // Checks if the character is a letter
            if(!Character.isLetter(c))
            {
                return true;
            }
        }
        return false;                           // Returns false if all characters are letters
    }

    /**
     * Searches through the database for Posts with the inputted search word as a tag
     * Retrieves the Posts and displays them on the page
     *
     * @param searchWord
     */
    public void searchForTag(final String searchWord)
    {
        // Create a new ArrayList to store the Posts
        final ArrayList<Post> mPostList = new ArrayList<>();
        // Search through the "tags" node on Firebase
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference attendanceRef = rootRef
                .child("tags")
                .child(searchWord);
        ValueEventListener valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    // Retrieve the Post that matches the searchWord
                    Post value = ds.getValue(Post.class);
                    // If the current view of the feed is on "Private"
                    if(switchButton.getText().equals("See Public"))
                    {
                        // Check to see if the Post retrieved is a private Post
                        if(value.getPrivate() == true)
                        {
                            // If yes, then add the Post to the ArrayList
                            mPostList.add(value);
                        }
                    }
                    // If the current view of the feed is on "Public"
                    else if(switchButton.getText().equals("See Private"))
                    {
                        // Check to see if the Post retrieved is a public Post
                        if(value.getPrivate() == false)
                        {
                            // If yes, then add the Post to the ArrayList
                            mPostList.add(value);
                        }
                    }
                }
                // Display all the Posts in the ArrayList on the page
                showPosts(mPostList);
                // If there are no Posts to display
                if(mPostList.size() == 0)
                {
                    // Display a Toast to the user
                    Toast.makeText(mContext, "No posts with the tag: " + searchWord,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.d(TAG, databaseError.getMessage());
                Toast.makeText(mContext, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        };
        attendanceRef.addListenerForSingleValueEvent(valueEventListener);
    }

    /**
     * Set up the buttons on the page
     */
    public void setUpButtons()
    {
        // Initialize the bottom navigation buttons
        addPostButton = (Button) findViewById(R.id.addPostButton);
        switchButton = (Button) findViewById(R.id.switchButton);
        mainButton = (Button) findViewById(R.id.mainButton);

        // Button to add a Post
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        PostActivity.class);
                startActivity(intent);
            }
        });

        // Button to return to the main app page
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        MainActivity.class);
                startActivity(intent);
            }
        });

        // Button to switch public/private views
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the user wants to display public posts
                if(switchButton.getText().equals("See Public"))
                {
                    switchButton.setText("See Private");
                    setUpPublicRecyclerView();
                    Toast.makeText(mContext, "Displaying public posts."
                            , Toast.LENGTH_SHORT).show();
                }
                // If the user wants to disaply private posts
                else if(switchButton.getText().equals("See Private"))
                {
                    switchButton.setText("See Public");
                    setUpPrivateRecyclerView();
                    Toast.makeText(mContext, "Displaying private posts."
                            , Toast.LENGTH_SHORT).show();
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
    private void checkCurrentUser(FirebaseUser user)
    {
        Log.d(TAG, "checkCurrentUser: checking if user is logged in.");
        // If user is not logged in
        if(user == null)
        {
            // Take user to the login page
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth()
    {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // Checks if the user is logged in
                checkCurrentUser(user);
                if (user != null)
                {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }
                else
                {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        checkCurrentUser(mAuth.getCurrentUser());
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (mAuthListener != null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}