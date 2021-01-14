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
import com.adizangi.tennisplayerstracker.dialogs.NetworkPermissionsDialog;
import com.adizangi.tennisplayerstracker.utils_data.FileManager;
import com.adizangi.tennisplayerstracker.utils_data.BackgroundManager;
import com.adizangi.tennisplayerstracker.workers.FetchDataWorker;

import java.util.ArrayList;
import java.util.UUID;

public class ProgressBarActivity extends AppCompatActivity
        implements NetworkPermissionsDialog.OnClickListener {

    private static final String PROGRESS_KEY = "progress";
    private static final String PROGRESS_STATE_KEY = "progressState";
    private static final String MESSAGE_KEY = "message";

    private ProgressBar progressBar;
    private TextView progressState;
    private TextView message;
    private BackgroundManager backgroundManager;
    private SharedPreferences prefs;

    private Observer<WorkInfo> workObserver = new Observer<WorkInfo>() {
        /*
           Called when the WorkInfo of the worker changes
         */
        @Override
        public void onChanged(WorkInfo workInfo) {
            if (workInfo != null) {
                updateScreen(workInfo);
            }
        }
    };

    /*
       Shows the content defined in the layout resource file and sets up an
       action bar
       If this activity is created for the first time, begins initializing the
       app
       If the activity was recreated, restores its previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        progressBar = findViewById(R.id.progress_bar);
        progressState = findViewById(R.id.progress_state);
        message = findViewById(R.id.message);
        backgroundManager = new BackgroundManager(this);
        prefs = getSharedPreferences(
                getString(R.string.shared_prefs_filename), Context.MODE_PRIVATE);
        if (savedInstanceState == null) {
            initializeActivity();
        } else {
            restoreActivity(savedInstanceState);
        }
    }

    /*
       Called when the user clicks 'Yes' in the network permissions dialog,
       which indicates the user is allowing the app to use mobile data
       Begins downloading content using the allowed connection type
     */
    @Override
    public void onSelectYes() {
        downloadContent(false);
    }

    /*
       Called when the user clicks 'No' in the network permissions dialog,
       which indicates the user is not allowing the app to use mobile data
       Begins downloading content using the allowed connection type
     */
    @Override
    public void onSelectNo() {
        downloadContent(true);
    }

    /*
       Sets the 'use wifi only' preference in Settings to the given boolean,
       so that the app will always use the connection type given by the boolean
       Begins a work chain that downloads content, which consists of a worker
       that fetches tennis data from the ESPN website, followed by a worker
       that sends a notification with the newest events
       Registers a live Observer for the work chain
     */
    private void downloadContent(boolean useWifiOnly) {
        backgroundManager.setNetworkPreference(useWifiOnly);
        UUID[] uuids = backgroundManager.downloadContent();
        WorkManager.getInstance(this)
                .getWorkInfoByIdLiveData(uuids[0])
                .observe(this, workObserver);
    }

    /*
       Called when the activity is about to be recreated, such as when the
       screen configuration changes
       Saves the current values in the views to the given Bundle
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(PROGRESS_KEY, progressBar.getProgress());
        outState.putCharSequence(PROGRESS_STATE_KEY, progressState.getText());
        outState.putCharSequence(MESSAGE_KEY, message.getText());
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
        NetworkPermissionsDialog dialog = new NetworkPermissionsDialog();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "networkType");
    }

    /*
       Restores the views' previous state from the given Bundle
     */
    private void restoreActivity(Bundle savedInstanceState) {
        progressBar.setProgress(savedInstanceState.getInt(PROGRESS_KEY));
        progressState.setText(savedInstanceState
                .getCharSequence(PROGRESS_STATE_KEY, ""));
        message.setText(savedInstanceState
                .getCharSequence(MESSAGE_KEY, ""));
    }

    /*
       Initializes this app's preferences to their default values and the list
       * defined in preferences.xml
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
       Gets the state and the progress of the background task from the given
       WorkInfo
       Updates the views on the screen based on the state and progress changes
     */
    private void updateScreen(WorkInfo workInfo) {
        /* Updates the progress bar */
        Data progressData = workInfo.getProgress();
        int progressPercentage = progressData
                .getInt(FetchDataWorker.PROGRESS_KEY, 0);
        progressBar.setProgress(progressPercentage);
        switch (workInfo.getState()) {
            case ENQUEUED:
                boolean isRetrying = prefs.getBoolean(getString(R.string.is_worker_retrying_key), false);
                if (isRetrying) {
                    /* Shows retrying message and sets isRetrying to false */
                    progressState.setText(getString(R.string.text_retrying));
                    message.setText(getString(R.string.text_retrying_message));
                    prefs.edit().putBoolean(getString(R.string.is_worker_retrying_key), false).apply();
                } else {
                    /* Shows regular waiting message */
                    progressState.setText(getString(R.string.text_preparing_to_start));
                    message.setText(getString(R.string.text_waiting_message));
                }
                break;
            case RUNNING:
                /* Shows starting message */
                progressState.setText(getString(R.string.text_starting));
                break;
            case SUCCEEDED:
                /* Shows success message and sets the version code preference
                   to 0 to indicate that the app has finished initializing

                   Switches back to MainActivity */
                progressState.setText(R.string.text_finished);
                prefs.edit().putInt(getString(R.string.version_code_key), 0).apply();
                Intent intent = new Intent(this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
        }
    }

}
