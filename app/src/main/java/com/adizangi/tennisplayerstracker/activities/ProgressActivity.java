/*
   A screen that shows the progress of loading data when the app opens for the
   first time
 */

package com.adizangi.tennisplayerstracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.adizangi.tennisplayerstracker.BuildConfig;
import com.adizangi.tennisplayerstracker.R;
import com.adizangi.tennisplayerstracker.utils_ui.WarningManager;
import com.adizangi.tennisplayerstracker.fragments.NetworkTypeDialog;
import com.adizangi.tennisplayerstracker.utils_data.FileManager;
import com.adizangi.tennisplayerstracker.utils_data.ScheduleManager;
import com.adizangi.tennisplayerstracker.workers.FetchDataWorker;

import java.util.UUID;

public class ProgressActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ScheduleManager scheduleManager;
    private SharedPreferences sharedPrefs;
    private WarningManager warningManager;

    private NetworkTypeDialog.NetworkTypeListener networkTypeListener =
            new NetworkTypeDialog.NetworkTypeListener() {
        /*
           Called when the user selects the 'any connection' option
           Saves this in Settings so the app will always use any connection
           Begins loading the data
         */
        @Override
        public void onSelectAnyConnection() {
            sharedPrefs.edit()
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
            sharedPrefs.edit()
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
       Displays the ProgressBarActivity layout
       Opens a dialog that prompts the user to select which network connection
       the app is permitted to use, with the options 'any connection' and
       'unmetered connection only'
       Sets responses to the dialog selections, which load the data using the
       selected connection type
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        progressBar = findViewById(R.id.progress_bar);
        warningManager = new WarningManager(this);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        NetworkTypeDialog dialog = new NetworkTypeDialog();
        dialog.setNetworkTypeListener(networkTypeListener);
        dialog.show(getSupportFragmentManager(), "networkType");
    }

    /*
       Schedules background work that loads data
       Sets an Observer for the work status
     */
    private void loadData() {
        scheduleManager = new ScheduleManager(this);
        UUID workRequestId = scheduleManager.fetchDataImmediately();
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
       the app had its first run, and opens MainActivity
       If the work failed, shows a message and prevents the user from proceeding
     */
    private void updateWorkProgress(WorkInfo workInfo) {
        WorkInfo.State state = workInfo.getState();
        if (state == WorkInfo.State.ENQUEUED &&
                !scheduleManager.isConnectedToNetwork()) {
            warningManager.showWarning(getResources().getString(R.string.warning_title_no_connection),
                    getResources().getString(R.string.warning_message_no_connection));
        } else if (state == WorkInfo.State.RUNNING) {
            warningManager.dismissWarning();
        } else if (state == WorkInfo.State.FAILED) {
            warningManager.showWarning(getResources().getString(R.string.warning_message_process_failed));
        } else if (state == WorkInfo.State.SUCCEEDED) {
            FileManager fileManager = new FileManager(this);
            int versionCode = BuildConfig.VERSION_CODE;
            fileManager.storeVersionCode(versionCode);
            Intent intent = new Intent(this, MainActivity.class);
            startActivityForResult(intent, 0);
        }
        Data progressData = workInfo.getProgress();
        int progress = progressData.getInt(FetchDataWorker.PROGRESS_KEY, 0);
        progressBar.setProgress(progress);
    }

}
