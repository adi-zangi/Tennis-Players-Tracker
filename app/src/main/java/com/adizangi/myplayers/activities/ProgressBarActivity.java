/*
   A screen with a progress bar that shows the progress of loading data when
   the app opens for the first time
 */

package com.adizangi.myplayers.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.adizangi.myplayers.BuildConfig;
import com.adizangi.myplayers.R;
import com.adizangi.myplayers.fragments.AlertMessageCreator;
import com.adizangi.myplayers.objects.FileManager;
import com.adizangi.myplayers.objects.ScheduleManager;
import com.adizangi.myplayers.workers.FetchDataWorker;

import java.util.UUID;

public class ProgressBarActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ScheduleManager scheduleManager;
    private AlertMessageCreator alertMessageCreator;

    private DialogInterface.OnClickListener useAnyNetworkListener =
            new DialogInterface.OnClickListener() {
        @Override
        /*
           Called when the user selects the 'Use any network' option
           Loads data using any network type
         */
        public void onClick(DialogInterface dialog, int which) {
            loadData(NetworkType.CONNECTED);
        }
    };

    private DialogInterface.OnClickListener useUnmeteredOnlyListener =
            new DialogInterface.OnClickListener() {
        /*
           Called when the user selects the 'Use unmetered only' option
           Loads data using unmetered network only
         */
        @Override
        public void onClick(DialogInterface dialog, int which) {
            loadData(NetworkType.UNMETERED);
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
       Opens a dialog that prompts the user to select which network type the
       app is permitted to use, with the options 'use any network' and 'use
       unmetered network only'
       Sets responses to the dialog selections, which load the data using the
       selected network type
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);
        Toolbar toolbar = findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        progressBar = findViewById(R.id.progressBar);
        alertMessageCreator = new AlertMessageCreator(this);

        AlertDialog networkTypeDialog = new AlertDialog.Builder
                (this, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
                .setMessage(R.string.dialog_network_type)
                .setPositiveButton(R.string.button_use_any_network, useAnyNetworkListener)
                .setNegativeButton(R.string.button_use_wifi_only, useUnmeteredOnlyListener)
                .setCancelable(false)
                .create();
        networkTypeDialog.show();
    }

    /*
       Schedules background work that fetches data using the given network type
       Sets an Observer for the work status
     */
    private void loadData(NetworkType networkType) {
        scheduleManager = new ScheduleManager(this, networkType);
        scheduleManager.initializeWorkManager(null);
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
            alertMessageCreator.showMessage("No Connection",
                    getResources().getString(R.string.message_no_connection));
        } else if (state == WorkInfo.State.RUNNING) {
            alertMessageCreator.dismissMessage();
        } else if (state == WorkInfo.State.FAILED) {
            alertMessageCreator.showMessage("",
                    getResources().getString(R.string.message_process_failed));
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
