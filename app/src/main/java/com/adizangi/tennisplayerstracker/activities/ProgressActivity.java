/*
   A screen that shows a progress bar filling while the app's data and
   settings are being initialized
 */

package com.adizangi.tennisplayerstracker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adizangi.tennisplayerstracker.R;
import com.adizangi.tennisplayerstracker.dialogs.ErrorDialog;
import com.adizangi.tennisplayerstracker.dialogs.NetworkTypeDialog;
import com.adizangi.tennisplayerstracker.utils_data.FileManager;
import com.adizangi.tennisplayerstracker.utils_data.BackgroundManager;
import com.adizangi.tennisplayerstracker.workers.FetchDataWorker;

import java.util.ArrayList;
import java.util.List;

public class ProgressActivity extends AppCompatActivity
        implements NetworkTypeDialog.NetworkTypeListener {

    private static final String PROGRESS_KEY = "progress";
    private static final String LOADING_STAGE_KEY = "loadingStage";
    private static final String CONNECTION_WARNING_KEY = "connectionWarning";

    private ProgressBar progressBar;
    private TextView loadingStage;
    private TextView connectionWarning;
    private BackgroundManager backgroundManager;
    private SharedPreferences prefs;

    private Observer<List<WorkInfo>> workStateObserver = new Observer<List<WorkInfo>>() {
        /*
           Called when the WorkInfo of the worker changes
           Updates the views based on the WorkInfo
         */
        @Override
        public void onChanged(List<WorkInfo> workInfos) {
            if (!workInfos.isEmpty()) {
                WorkInfo workInfo = workInfos.get(0);
                updateProgress(workInfo);
                switch (workInfo.getState()) {
                    case ENQUEUED:
                        enqueued();
                        break;
                    case RUNNING:
                        running();
                        break;
                    case SUCCEEDED:
                        succeeded();
                        break;
                    case FAILED:
                        failed();
                }
            }
        }
    };

    /*
       Shows the content defined in the layout resource file and sets up an
       action bar
       If this activity is created for the first time, begins initializing the
       app
       If the activity was recreated, restores its previous state
       Registers an observer for the background task that fetches initial data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        progressBar = findViewById(R.id.progress_bar);
        loadingStage = findViewById(R.id.loading_stage);
        connectionWarning = findViewById(R.id.connection_warning);
        backgroundManager = new BackgroundManager(this);
        prefs = getSharedPreferences(
                getString(R.string.shared_prefs_filename), Context.MODE_PRIVATE);
        WorkManager.getInstance(this)
                .getWorkInfosByTagLiveData(BackgroundManager.FETCH_DATA_WORK_TAG)
                .observe(this, workStateObserver);
        if (savedInstanceState == null) {
            initializeActivity();
        } else {
            restoreActivity(savedInstanceState);
        }
    }

    /*
       Called when the activity is about to be recreated, such as when the
       screen configuration changes
       Saves the current values in the views to the given Bundle
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(PROGRESS_KEY, progressBar.getProgress());
        outState.putCharSequence(LOADING_STAGE_KEY, loadingStage.getText());
        outState.putCharSequence(CONNECTION_WARNING_KEY, connectionWarning.getText());
        super.onSaveInstanceState(outState);
    }

    /*
       Initializes everything that needs one-time initialization
       Opens a dialog that asks the user which type of network connection the
       app should use
       When an option from the dialog is selected, the app will fetch all its
       data about tennis players while updating the progress bar
       When the data finishes loading, sends a notification containing a
       summary of today's tennis tournaments
     */
    private void initializeActivity() {
        initializeAppValues();
        NetworkTypeDialog dialog = new NetworkTypeDialog();
        dialog.show(getSupportFragmentManager(), "networkType");
    }

    /*
       Restores the views' previous state from the given Bundle
     */
    private void restoreActivity(Bundle savedInstanceState) {
        progressBar.setProgress(savedInstanceState.getInt(PROGRESS_KEY));
        loadingStage.setText(savedInstanceState
                .getCharSequence(LOADING_STAGE_KEY, ""));
        connectionWarning.setText(savedInstanceState
                .getCharSequence(CONNECTION_WARNING_KEY, ""));
    }

    /*
       Initializes this app's preferences to their default values and the list
       of the user's selected players to an empty list
       Creates a notification channel for the app
     */
    private void initializeAppValues() {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        backgroundManager.createNotificationChannel();
        FileManager fileManager = new FileManager(this);
        fileManager.storeSelectedPlayers(new ArrayList<String>());
    }

    /*
       Called when the user selects the 'any connection' option of the dialog
       Saves this in Settings so the app will always use any connection
       Schedules background work that fetches data and sends a notification
     */
    @Override
    public void onSelectAnyConnection() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.edit().putBoolean(getString(R.string.pref_network_type_key), false).apply();
        backgroundManager.fetchDataSendNotif();
    }

    /*
       Called when the user selects the 'unmetered connection only' option of
       the dialog
       Saves this in Settings so the app will always use unmetered connection
       Schedules background work that fetches data and sends a notification
     */
    @Override
    public void onSelectUnmeteredOnly() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.edit().putBoolean(getString(R.string.pref_network_type_key), true).apply();
        backgroundManager.fetchDataSendNotif();
    }

    /*
       Gets the progress from the given WorkInfo and updates the progress bar
     */
    private void updateProgress(WorkInfo workInfo) {
        Data progressData = workInfo.getProgress();
        int progressPercentage = progressData.getInt(FetchDataWorker.PROGRESS_KEY, 0);
        progressBar.setProgress(progressPercentage);
    }

    /*
       If the work is waiting to start because there is no network connection,
       shows a 'No Connection' message on the screen
       Sets the loading stage TextView to indicate whether the worker is
       preparing to start, retrying, or waiting for connection
     */
    private void enqueued() {
        boolean isConnectedToNetwork = backgroundManager.isConnectedToNetwork();
        boolean isRetryingWork = prefs.getBoolean(getString(R.string.is_worker_retrying_key), false);
        if (!isConnectedToNetwork) {
            connectionWarning.setText(R.string.text_no_connection);
        }
        if (isRetryingWork) {
            if (isConnectedToNetwork) {
                loadingStage.setText(getString(R.string.text_retrying));
            } else {
                loadingStage.setText(R.string.text_stopped);
            }
        } else {
            loadingStage.setText(getString(R.string.text_preparing_to_start));
        }
    }

    /*
       Edits the loading stage TextView to say that the loading has started
       If a 'No Connection' message is showing, un-shows it
     */
    private void running() {
        loadingStage.setText(getString(R.string.text_starting));
        connectionWarning.setText("");
    }

    /*
       Edits the loading stage TextView to say that the loading is finished
       Saves a version code in shared preferences to indicate that the app was
       initialized, and switches back to MainActivity
     */
    private void succeeded() {
        final int VERSION_CODE = 0;
        loadingStage.setText(R.string.text_finished);
        prefs.edit().putInt(getString(R.string.version_code_key), VERSION_CODE).apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /*
       Shows an error message on the screen
     */
    private void failed() {
        ErrorDialog errorDialog = new ErrorDialog();
        errorDialog.show(getSupportFragmentManager(), "error");
    }

}
