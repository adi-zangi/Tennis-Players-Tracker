/*
   A screen that shows a progress bar filling while the app's data and
   settings are being initialized
 */

package com.adizangi.tennisplayerstracker.activities;

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
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adizangi.tennisplayerstracker.R;
import com.adizangi.tennisplayerstracker.utils_ui.Timer;
import com.adizangi.tennisplayerstracker.utils_ui.WarningManager;
import com.adizangi.tennisplayerstracker.fragments.NetworkTypeDialog;
import com.adizangi.tennisplayerstracker.utils_data.FileManager;
import com.adizangi.tennisplayerstracker.utils_data.BackgroundManager;
import com.adizangi.tennisplayerstracker.workers.FetchDataWorker;

import java.util.ArrayList;
import java.util.UUID;

public class ProgressActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView progressState;
    private TextView connectionWarning;
    private TextView countDownView;

    private BackgroundManager backgroundManager;
    private WarningManager warningManager;
    private SharedPreferences settings;
    private SharedPreferences prefs;
    private Timer timer;

    private NetworkTypeDialog.NetworkTypeListener networkTypeListener =
            new NetworkTypeDialog.NetworkTypeListener() {
        /*
           Called when the user selects the 'any connection' option
           Saves this in Settings so the app will always use any connection
           Begins loading the data
         */
        @Override
        public void onSelectAnyConnection() {
            settings.edit()
                    .putBoolean(getString(R.string.pref_network_type_key), false).apply();
            loadData();
        }

        /*
           Called when the user selects the 'unmetered connection only' option
           Saves this in Settings so the app will always use unmetered connection
           Begins loading the data
         */
        @Override
        public void onSelectUnmeteredOnly() {
            settings.edit()
                    .putBoolean(getString(R.string.pref_network_type_key), true).apply();
            loadData();
        }
    };

    private Observer<WorkInfo> workStateObserver = new Observer<WorkInfo>() {
        @Override
        public void onChanged(WorkInfo workInfo) {
            updateWorkProgress(workInfo);
        }
    };

    @Override
    /*
       Shows the content defined in the layout resource file and sets up an
       action bar
       Initializes this app's preferences to their default values and creates
       a notification channel
       Opens a dialog that prompts the user to select which network connection
       the app is permitted to use, with the options 'any connection' and
       'unmetered connection only'
       Sets responses to the dialog selections, which load the app's data using
       the selected connection type
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        progressBar = findViewById(R.id.progress_bar);
        progressState = findViewById(R.id.progress_state);
        connectionWarning = findViewById(R.id.connection_warning);
        countDownView = findViewById(R.id.count_down_view);
        backgroundManager = new BackgroundManager(this);
        warningManager = new WarningManager(this);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        prefs = getSharedPreferences(
                getString(R.string.shared_prefs_filename), Context.MODE_PRIVATE);
        timer = new Timer(20000, 1000, countDownView);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        backgroundManager.createNotificationChannel();
        FileManager fileManager = new FileManager(this);
        fileManager.storeSelectedPlayers(new ArrayList<String>());
        NetworkTypeDialog dialog = new NetworkTypeDialog();
        dialog.setNetworkTypeListener(networkTypeListener);
        dialog.show(getSupportFragmentManager(), "networkType");
    }

    /*
       Schedules background work that loads data and then sends a notification
       containing a summary of today's tennis tournaments
       Sets an Observer for the work status
     */
    private void loadData() {
        UUID workRequestId = backgroundManager.fetchDataImmediately();
        WorkManager.getInstance(this)
                .getWorkInfoByIdLiveData(workRequestId)
                .observe(this, workStateObserver);
    }

    /*
       Updates the UI based on the given WorkInfo
       If the work's progress changed, updates the progress bar
       If the work's state changed, updates the state text view
       If the work succeeded, saves the app's version code to indicate that
       the app was initialized, and switches back to MainActivity
       If the work failed, shows a message and prevents the user from proceeding
     */
    private void updateWorkProgress(WorkInfo workInfo) {
        Data progressData = workInfo.getProgress();
        int progress = progressData.getInt(FetchDataWorker.PROGRESS_KEY, 0);
        progressBar.setProgress(progress);
        WorkInfo.State state = workInfo.getState();
        if (state == WorkInfo.State.ENQUEUED) {
            timer.cancel();
            checkEnqueuedState();
        } else if (state == WorkInfo.State.RUNNING) {
            warningManager.dismissWarning();
            progressState.setText(R.string.text_starting);
            timer.start();
        } else if (state == WorkInfo.State.FAILED) {
            warningManager.showWarning(getResources().getString(R.string.warning_message_process_failed));
        } else if (state == WorkInfo.State.SUCCEEDED) {
            final int VERSION_CODE = 0;
            timer.cancel();
            prefs.edit().putInt(getString(R.string.version_code_key), VERSION_CODE).apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    /*
       Updates the screen based on the reason the work is enqueued
       If the work is waiting to start because there is no network connection,
       shows a 'No Connection' message on the screen that stays until
       connection is back
       If network connection is slow, shows a warning on the screen
       Sets the timer that will show on the screen to 30 seconds if connection
       is slow, and to 20 seconds otherwise
     */
    private void checkEnqueuedState() {
        boolean isConnected = backgroundManager.isConnectedToNetwork();
        boolean isConnectionSlow = backgroundManager.isConnectionSlow();
        boolean isRetrying = prefs.getBoolean(
                getString(R.string.is_worker_retrying_key), false);
        if (!isConnected) {
            warningManager.showWarning(getResources().getString(R.string.warning_title_no_connection),
                    getResources().getString(R.string.warning_message_no_connection));
        }
        if (isRetrying) {
            progressState.setText(R.string.text_retrying);
        } else {
            progressState.setText(R.string.text_preparing_to_start);
        }
        if (isConnectionSlow) {
            connectionWarning.setText(R.string.text_slow_connection);
            timer = new Timer(30000, 1000, countDownView);
        } else {
            timer = new Timer(20000, 1000, countDownView);
        }
    }

}
