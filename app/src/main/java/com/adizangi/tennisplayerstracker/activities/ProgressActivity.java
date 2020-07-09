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

import com.adizangi.tennisplayerstracker.R;
import com.adizangi.tennisplayerstracker.utils_ui.WarningManager;
import com.adizangi.tennisplayerstracker.fragments.NetworkTypeDialog;
import com.adizangi.tennisplayerstracker.utils_data.FileManager;
import com.adizangi.tennisplayerstracker.utils_data.BackgroundManager;
import com.adizangi.tennisplayerstracker.workers.FetchDataWorker;

import java.util.ArrayList;
import java.util.UUID;

public class ProgressActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private BackgroundManager backgroundManager;
    private SharedPreferences settings;
    private WarningManager warningManager;
    //private CountDownTimer countDown;

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
        backgroundManager = new BackgroundManager(this);
        warningManager = new WarningManager(this);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        //countDown = getCountDownTimer();
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
       If there is no network connection when the work is waiting to run or if
       connection is lost while it runs, shows a 'No Connection' message on the
       screen that stays until connection is back
       If the work's progress changed, updates the progress bar on the screen
       If the work succeeded, saves the app's version code to indicate that
       the app was initialized, and switches back to MainActivity
       If the work failed, shows a message and prevents the user from proceeding
     */
    private void updateWorkProgress(WorkInfo workInfo) {
        Data progressData = workInfo.getProgress();
        int progress = progressData.getInt(FetchDataWorker.PROGRESS_KEY, 0);
        progressBar.setProgress(progress);
        WorkInfo.State state = workInfo.getState();
        if (state == WorkInfo.State.ENQUEUED && !backgroundManager.isConnectedToNetwork()) {
            warningManager.showWarning(getResources().getString(R.string.warning_title_no_connection),
                    getResources().getString(R.string.warning_message_no_connection));
            //countDown.cancel();
        } else if (state == WorkInfo.State.RUNNING) {
            warningManager.dismissWarning();
            //countDown.start();
        } else if (state == WorkInfo.State.FAILED) {
            warningManager.showWarning(getResources().getString(R.string.warning_message_process_failed));
        } else if (state == WorkInfo.State.SUCCEEDED) {
            final int VERSION_CODE = 0;
            //countDown.cancel();
            SharedPreferences prefs = getSharedPreferences(
                    getString(R.string.shared_prefs_filename), Context.MODE_PRIVATE);
            prefs.edit().putInt(getString(R.string.version_code_key), VERSION_CODE).apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    /*
       Returns a CountDownTimer that shows a 20 second countdown on the screen
     */
    private CountDownTimer getCountDownTimer() {
        final TextView countDownView = findViewById(R.id.count_down_view);
        return new CountDownTimer(20000, 1000) {

            /*
               Called each time the timer goes down by 1 second
               Updates the text view with the number of seconds left
             */
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                String secondsShown = countDownView.getText().toString();
                if (secondsShown.isEmpty()) {
                    countDownView.setText(String.valueOf(secondsRemaining));
                } else if (secondsRemaining < Integer.parseInt(secondsShown)) { // needed due to bug
                    countDownView.setText(String.valueOf(secondsRemaining));
                }
            }

            /*
               Called when the time is up
               Updates the text view with "0"
             */
            @Override
            public void onFinish() {
                countDownView.setText("0");
            }

        };
    }

}
