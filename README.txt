Program Title: SENsational
Author: Bernice Tse, Malin Leven, Nicole Xiang, Samvitti Sharma
Class: Yr12IBCS
Date: 17th March, 2020

---- Program Purpose ----
This program consists of games training executive function, calming tools and a parent forum
for parents of SEN children.

---- Installation ----
Works for android API levels 21-29

---- Current Dependencies ----
androidx.appcompat:appcompat:1.0.2
androidx.constraintlayout:constraintlayout:1.1.3
androidx.test.espresso:espresso-core:3.1.1
com.google.firebase:firebase-auth:19.3.0
com.google.firebase:firebase-database:19.2.1
com.google.firebase:firebase-storage:19.1.1
androidx.test.ext:junit:1.1.0
junit:junit:4.12
androidx.legacy:legacy-support-v4:1.0.0
androidx.lifecycle:lifecycle-extensions:2.0.0
com.google.android.material:material:1.0.0
androidx.navigation:navigation-fragment:2.0.0
androidx.navigation:navigation-ui:2.0.0
androidx.preference:preference:1.1.0-alpha05
com.sdsmdg.tastytoast:tastytoast:0.1.1

---- Design ----
There are 4 functions in this app in total: 2 games, 1 calming tool and 1 chat forum. Each function
has its own activities. Each layer describes their methods.

Colorize Game
(Activity) ColorizeStartActivity- The start page is a dynamic screen that displays buttons to
                                   access instructions, settings, play or quit the game
    onCreate -- creates and identifies the various components on screen
    setUpButtons -- identifies actions once the corresponding buttons on the screen is pressed
            addBundle -- places the data gathered from the bundle into a new bundle and pass it onto
                         the next activity
    animation -- sets a timer to run the changePosition method indefinitely
            changePosition -- generates animation for all the images on the screen by changing its X
                             or Y coordinates
    getBundle -- gets the data from the bundle sent from the Settings page by using the String Key

(Activity) ColorizeInstructionsActivity- The instructions page displays the instructions for the
                                          game and prompts user to go back to the main page or play
                                          the game
    onCreate -- creates and identifies the various components on screen
    setUpButtons -- identifies actions once the corresponding buttons on the screen is pressed

(Activity) ColorizeSettings Activity- The settings page prompts user to select their game
                                      preferences such as background, music and time
    onCreate -- creates and identifies the various components on screen
    setBackgroundCheck -- checks the user's preference for background option and sets the checkbox
    setUpMusicChoice -- checks the user's preference for music option and sets the checkbox
    setUpMedia Player -- creates a new media Player
    addTime -- adds the timer options in an arraylist to display on spinner
    setUpButtons -- identifies actions once the corresponding buttons on the screen is pressed
            getTime -- gets what the user option is for time preference

(Activity) ColorizeMainActivity- The main page is where the game will occur
    onCreate -- creates and identifies the various components on screen
    setUpButtons -- identifies actions once the corresponding buttons on the screen is pressed
    setUpTimer -- sets the timer on the screen
            getTime -- retrieves bundle that was passed on from ColorizeStartActivity
    addColors - adds colors and data that needs to be used and processed internally in the
                corresponding data structures
    setUpGame -- starts the timer, generates random text, color of text and background color onto
                screen, as well as the buttons for the users to click on
            setBackgroundColor -- checks user background preference by checking game constant
    play -- identifies actions once the buttons are clicked depending if they chose the right button.
            If it is the right button, adds score, reset timer and repeats the game until the user
            presses the wrong button

(Activity) ColorizeEndActivity- The end activity is where the user will be directed to when they
                                finished the game, it displays the current user score and high
                                score, as well as buttons to play again, to go back to the start
                                page or quit the game completely
    onCreate -- creates and identifies the various components on screen
    setUpButtons -- identifies actions once the corresponding buttons on the screen is pressed
    displayScore -- retrieves user ID from firebase and uses firebase methods to update total user
                    score, check and update high score, and display current and high score on screen
            updateUserScore -- uses user ID to update total user score on firebase by adding current
                               user score
            initialScoring -- uses user ID to set and display current score and high score for first
                              time users
            checkHighScore -- uses user ID to compare current user score with current highscore on
                              the database
                      storeHighScore -- uses user ID to store the updated user high score on
                                        Firebase

Bubbles Game
(Activity) BubblesStartActivity - this is the start screen of the game
    onCreate -- creates and displays components on the screen, allows users to select the game mode,
                and sets up the Instructions and Play buttons so that they complete the right
                actions when pressed

(Activity) BubblesInstructionActivity - this activity displays the instructions of the game
    onCreate -- creates and displays components on the instructions screen and sets up the Back
                button so that users can play the game

(Activity) BubblesMiddleActivity - this activity shows various bubbles floating on the screen
    onCreate -- sets up ImageViews, displays the user's score so far, and initializes instance
                variables that will be used in other methods

    generateRandomSequence - randomly generates a color for the bubble that will be displayed

    setUpBubbles - gets the dimensions of the screen and calls on the playBubble method

    playBubble - displays the bubble floating on the screen at a certain speed. Makes use of the
                 changeBubbleCoordinates method

    changeBubbleCoordinates - changes the y-coordinate of the appropriate ImageViews and TextViews.
                              Checks when to display the next bubble/move on to the next activity

(Activity) BubblesMiddle2Activity - this activity asks users to identify the color of each bubble
                                    in the sequence by selecting one of three answer choices.
     onCreate -- sets up ImageViews, displays the user's score so far, sets up the Next Round
                 button and initializes instance variables that will be used later on

     randomizeOptions -- randomly selects three answer options that the user can choose from. One
                         of them is the correct answer.

     setUpCorrectButton -- sets up the button that displays the correct answer so that it completes
                           the right actions when pressed

     setUpWrongButton -- sets up the button that displays the wrong answer so that it completes
                         the right actions when pressed

     checkEnd-- checks whether the user has identified the color of each of the bubbles in the
                sequence. Transitions to the end screen or back to BubblesMiddleActivity depending on
                the number of rounds that the user has completed.

(Activity) BubblesEndActivity -- this is the end screen of the game
    onCreate -- displays the user's final score, updates and displays the user's total number of
                smiley faces, and sets up the Exit Game and Play Again buttons.

Shared Games
(Activity) Games Shared Activity- This page displays game icons and users can access the different
                                  games, the trophies page or go back to the home page
    onCreate -- creates and identifies the various components on screen
    setUpButtons -- identifies actions once the corresponding buttons on the screen is pressed

(Activity) Trophies Activity- This page displays the trophies that the user earned, the upcoming
                              trophy and the total user score
    onCreate -- creates and identifies the various components on screen

(Class) Trophies Adapter- this class creates a customised recycler view adapter
    onCreateViewHolder -- creates view holder for the trophies
    onBindViewHolder -- creates a customised bind view holder
    getItemCount -- returns the size of the trophy list

(Class) Trophies View Holder - this class creates a customised recycler view holder

(Class) Trophy - This class identifies the attributes of a trophy
    setName - sets the trophy name
    getName - gets the trophy name
    setSmileyFaces - sets the smiley faces
    getSmileyFaces - gets the smiley faces
    setImageID - sets the image ID
    getImageID - gets the image ID
    setNextTrophy - sets the next trophy
    getNextTrophy - gets the next trophy


Calming
(Activity) CalmingActivity - This is the activity for the start page of the calming function
    onCreate -- creates and identifies the various components on screen
    setUpButtons -- sets up buttons to go to mode 1, 2 and 3 activities, settings page, and back to
                Clover home page
    getBundle -- gets settings information from bundle from setting activity intent
    addBundle -- adds setting information (from get bundle) to bundle to be sent to mode 1, 2, 3

(Activity) CalmingMode1Activity - This is the activity for the mode 1 page
    onCreate -- creates and identifies the various components on screen
    sizeControl -- controls growth and shrinking of circle using default 3-2-3 or information from
                settings unpacked in bundle from  home activity. Runs Util class method to create
                number array. Runs Util class method circle control to get animation set, sets an
                animation listener to repeat animation when finished. Calls the methods to control
                breathe and number text views.
    text -- controls breathe in/hold/breathe out text view
    number -- controls number text view
    pausebutton -- method to control pause button, pausing animations, and resuming when paused

(Activity) CalmingMode2Activity - This is the activity for the mode 2 page
   onCreate -- creates and identifies the various components on screen
   sizeControl -- controls growth and shrinking of circle using default 3-2-3 or information from
                settings unpacked in bundle from  home activity. Runs Util class method to create
                number array. Runs Util class method circle control to get animation set, sets an
                animation listener to repeat animation when finished. Calls the methods to control
                breathe and number text views.
   goUp -- controls movements of butterflies up the screen
   text -- controls breathe in/hold/breathe out text view
   number -- controls number text view
   pausebutton -- method to control pause button, pausing animations, and resuming when paused

(Activity) CalmingMode3Activity - This is the activity for the mode 3 page
    onCreate -- creates and identifies the various components on screen
    sizeControl -- controls growth and shrinking of circle using default 3-2-3 or information from
                 settings unpacked in bundle from  home activity. Runs Util class method to create
                 number array. Runs Util class method circle control to get animation set, sets an
                 animation listener to repeat animation when finished. Calls the methods to control
                 breathe and number text views.
    goUp -- controls movements of bubbles up the screen
    goLeft -- controls movements of fishes going left on screen
    goRight -- controls movements of fishes going right on screen
    text -- controls breathe in/hold/breathe out text view
    number -- controls number text view
    pausebutton -- method to control pause button, pausing animations, and resuming when paused

(Activity) CalmingSettingsActivity - This is the activity for the calming settings page
    onCreate -- creates and identifies the various components on screen
    addSpinner -- creates adaptor for spinner, sets the adaptor to the three spinners
    saveSettings -- method to control save button, saving settings chosen on spinner for breathing
                  exercise time
    backButton -- method to control back button to go back to main activity, saving the spinner
                  details in a bundle to send to main activity intent

(Class) Util - This is the util class for different methods to be used in the activity classes
    circleControl -- creates animation set and add animations from anim files, setting duration
                  specific to the setting (or default 3-2-3).
    numberArrayList -- creates a number ArrayList to be used for the number text view, counting from
                  1 - chosen number for the breathe in, hold, and breathe out numbers.

(anim) circleanimation -- Shrinks circle to 0.7 scale

(anim) circleanimation2 -- Grows circle to 1.3 scale

Forum
(Class) User - This is the User class which stores the variables associated with each user of the
                forum. Consists of getters and setters to access the data.
(Class) Post - This is the Post class which stores the data associated with each Post on the forum.
                Consists of getters and setters to access the data.
(Class) Comment - This is the Comment class which stores the information for each Comment.
                    Consists of getters and setters to access the data.
(Model) FirebaseMethods - This is a Utils class that consists of all the methods that write to the
                            database. This allows for other Activities to call methods when needed.
This Model consists of the following methods:
    getTimestamp -- Retrieves the instantaneous time when the method is called
    createNewPost -- Creates a new Post object with the parameters
    upvoteButtonPressed -- Updates the likes String of the corresponding Post on database
    upvote -- Searches through the Database to retrieve the post's current upvote number and adds 1
    downvoteButtonPressed -- Updates the unlikes String of the corresponding Post on database
    downvote -- Searches through the Database, retrieves  post's current upvote number and minuses 1
    uploadNewPost -- Uploads the Post to Firebase under all relevant nodes
    makeComment -- Creates a new Comment object with the arguments
    updateUserAccountSettings -- Updates the user's account's settings on Firebase
    updateUsername -- update username in the 'users' node and 'user_account_settings' node
    updateEmail -- update the email in the 'users' node
    updatePassword -- update the password in the 'users' node
    registerNewEmail -- Register a new email and password to Firebase authentication
    sendVerificationEmail -- Sends verification email to the email used to register new user
    addNewUser -- Adds User to Firebase
    UserSettings -- Retrieves the account settings for the user currently logged in

(Activity) HomeActivity - This is the activity that provides the main feed for the forum feature.
This activity allows the user to access on the functionality the forum provides:
    1. Viewing of the Posts from the database
    2. Searching of the Posts using tags
    3. Adding new Posts
    4. Switching between public and private posting
    5. Returning to the MainActivity page of the app
This activity has the following methods:
    onCreate -- sets up all the components of the Activity
    setUpPublicRecyclerView -- Sets up the home page to display public Posts from all forum users
    setUpPrivateRecyclerView -- Sets up the home page to display Posts created
                                by the current user that were set to private
    showPosts -- Sets up the RecyclerView for the Posts
    setUpSearch -- Sets up the search widgets and onClick Listeners
    checkInputs -- Checks that the parameter is valid (ie. the searched word is appropriate)
    isNotAlpha -- Checks that the parameter contains alphabetical letters only
    searchForTag -- Searches through the database and retrieves Posts tagged with the inputted word
    setUpButtons -- Set up the buttons on the page
    checkCurrentUser -- checks to see if the @param 'user' is logged in
    setupFirebaseAuth -- Setup the firebase auth object

(Activity) PostActivity - This is the activity that provides the posting function for forum users.
This activity has the following methods:
    onCreate -- sets up all the components of the Activity
    initWidgets -- Sets up all widgets on the page
    init -- Initializes the buttons for ClickListeners
    checkInputs -- Checks that all the parameters are valid
    isNotAlpha -- Checks that the parameter contains alphabetical letters only

(Activity) ViewPostActivity - This is the activity that allows users to view the Posts.
This activity has the following methods:
    onCreate -- sets up all the components of the Activity
    initWidgets -- Sets up all the widgets on the page
    displayComments -- Displays the RecyclerView with Comments for this Post
    init -- Searches through Firebase for the desired Post
    getCurrentUser -- Retrieves the current user's information from Firebase
    getPostDetails -- Retrieves the user information of this Post from Firebase
    setupWidgets -- Sets up the display of the widgets on the page
    setUpVotes -- Sets up the display for the voting function of the page
    setupFirebaseAuth -- Setup the firebase auth object

(Activity) LoginActivity - This is the activity that allows users to login with their account.
This activity has the following methods:
    onCreate -- sets up all the components of the Activity
    init -- Initialize the widgets on the page
    setupFirebaseAuth -- Setup the firebase auth object

(Activity) RegisterActivity - This is the activity that allows users to register a new account.
This activity has the following methods:
    onCreate -- sets up all the components of the Activity
    init -- Sets up the register page widgets
    checkInputs -- Checks that all the parameters are valid
    initWidgets -- Initialize the activity widgets
    checkIfUsernameExists -- Check if @param username already exists in the database
    setupFirebaseAuth -- Setup the firebase auth object
