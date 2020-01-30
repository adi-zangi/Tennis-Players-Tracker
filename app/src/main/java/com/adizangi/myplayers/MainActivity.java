/*
   Adi Zangi
   2019-2020

   The app MyPlayers lets the user stay informed about tennis players' scores
   and about tennis tournaments that are going on
   It enables to select tennis players to track and view those players' latest
   scores, which are updated daily
   It also sends notifications to inform the user when the selected players are
   playing, which tournament is currently going on, and who won each tournament
   */

/*
   This activity is the window that opens when the app is launched
   It displays a drop-down list that contains the user's selected players and
   an option to edit them
   It also displays a list of entries where each entry contains general information
   and the latest score of one of the selected players
 */

package com.adizangi.myplayers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Configuration;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkerFactory;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    public static final String APP_VALUES_FILE_KEY =
            "com.adizangi.myplayers.APP_VALUES";
    public static final String VERSION_CODE_KEY  =
            "com.adizangi.myplayers.VERSION_CODE";
    public static final int PROGRESS_BAR_MSG_CODE = 0;
    public static final int SPINNER_MSG_CODE = 1;
    public static final int RECYCLER_VIEW_MSG_CODE = 2;
    public static final int ANIMATION_MSG_CODE = 3;

    private Toolbar toolbar;
    private Spinner myPlayersSpinner;
    private RecyclerView playerDataRecyclerView;


    @Override
    /*
       Initializes the activity
       Fills the views in the main activity layout with data
       If the app is running for the first time after it was installed, or if
       the app data was cleared, schedules daily data lookups and runs
       an initial data lookup in the background
       While the initial data lookup runs, displays a progress screen that
       shows the progress
       In addition, on the app's first run displays a dialog that informs the
       user about what the app does
     */
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getSimpleName(), "Instance of MainActivity created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        myPlayersSpinner = findViewById(R.id.myPlayersSpinner);
        playerDataRecyclerView = findViewById(R.id.playerDataRecyclerView);
        setSupportActionBar(toolbar); // Sets up this activity's app bar,
                                      // which displays the activity's name
                                      // and an options menu
        SharedPreferences appValuesFile =
                getSharedPreferences(APP_VALUES_FILE_KEY, MODE_PRIVATE);
        int savedVersionCode = appValuesFile.getInt(VERSION_CODE_KEY, -1);
        if (savedVersionCode == -1) { // Version code isn't saved
                                      // App's first run
            initFirstRun();
        } else { // Version code was saved in the first run data lookup worker
                 // Not app's first run
            initRegularRun();
        }
    }

    @Override
    /*
       Called when the app bar is created, which is done in onCreate
       Initializes the app bar's options menu with a customized menu in place
       of the default one
       Fills the given Menu with a resource containing the app bar options
       Returns true to display the app bar
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar, menu);
        return true;
    }

    @Override
    /*
       Called when the user selects an item from the app bar
       If Settings was selected, opens the Setting activity
       If Sources was selected, opens the Sources dialog
       Otherwise, invokes the superclass to handle the selection
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /*
       Called when the user taps the Add button in the Spinner
       Adds the player that is currently selected
     */
    public void addPlayer() {

    }

    /*
       Called when the user taps the Remove button in one of the Spinner
       entries
       Removes the player in the entry from the Spinner and the RecyclerView
     */
    public void removePlayer() {

    }

    /*
       Called when the user taps the ’See more match results’ button in one of
       the RecyclerView entries
       Opens an ESPN web page that has all the match results of the player
     */
    public void openESPNPlayerPage() {

    }

    /*
       Initializes the activity when the app runs for the first time
       Schedules daily data lookups and runs an initial data lookup in the
       background
       While the initial data lookup runs, displays a progress screen that
       shows the progress and switches to the main screen once all data is
       obtained
       Displays a dialog that informs the user about what the app does
     */
    private void initFirstRun() {
        Handler handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.i(MainActivity.this.getClass().getSimpleName(), "Handler received message");
                super.handleMessage(msg);
            }
        };
        WorkerFactory workerFactory = new CustomWorkerFactory(handler);
        Configuration configuration = new Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build();
        WorkManager.initialize(this, configuration);
        OneTimeWorkRequest extractDataWork =
                new OneTimeWorkRequest.Builder(FirstRunDataWorker.class)
                        .build();
        WorkManager.getInstance(this).enqueue(extractDataWork);
    }

    /*
       Initializes the activity when the app runs not for the first time
       Fills the Spinner with the names of the user's selected players
       and fills the RecyclerView with data about the players
     */
    private void initRegularRun() {

    }

}
