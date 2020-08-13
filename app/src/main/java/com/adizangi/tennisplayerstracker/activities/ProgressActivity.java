/*
   A screen that shows a progress bar filling while the app's data and
   settings are being initialized
 */

package com.adizangi.tennisplayerstracker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adizangi.tennisplayerstracker.R;
import com.adizangi.tennisplayerstracker.utils_ui.WarningManager;
import com.adizangi.tennisplayerstracker.fragments.NetworkTypeDialog;
import com.adizangi.tennisplayerstracker.utils_data.FileManager;
import com.adizangi.tennisplayerstracker.utils_data.BackgroundManager;
import com.adizangi.tennisplayerstracker.view_models.ProgressViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProgressActivity extends AppCompatActivity
        implements NetworkTypeDialog.NetworkTypeListener {

    private ProgressBar progressBar;
    private TextView stageOfLoading;
    private TextView slowConnectionWarning;
    private ProgressViewModel viewModel;
    private BackgroundManager backgroundManager;
    private WarningManager warningManager;

    private Observer<List<WorkInfo>> workStateObserver = new Observer<List<WorkInfo>>() {
        /*
           Called when the WorkInfo of the worker changes
         */
        @Override
        public void onChanged(List<WorkInfo> workInfos) {
            if (!workInfos.isEmpty()) {
                updateScreen(workInfos.get(0));
            }
        }
    };

    private Observer<Integer> progressObserver = new Observer<Integer>() {
        /*
           Called when the progress of loading data changes
           Updates the progress bar
         */
        @Override
        public void onChanged(Integer progress) {
            progressBar.setProgress(progress);
        }
    };

    private Observer<String> stageObserver = new Observer<String>() {
        /*
           Called when the stage of loading data changes
           Updates the text view
         */
        @Override
        public void onChanged(String stage) {
            stageOfLoading.setText(stage);
        }
    };

    private Observer<String> slowConnectionObserver = new Observer<String>() {
        /*
           Called when a slow connection warning should appear or disappear
           Updates the text view
         */
        @Override
        public void onChanged(String connectionWarning) {
            slowConnectionWarning.setText(connectionWarning);
        }
    };

    /*
       Shows the content defined in the layout resource file and sets up an
       action bar
       Initializes the app and gets all the app's data about tennis players
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        progressBar = findViewById(R.id.progress_bar);
        stageOfLoading = findViewById(R.id.stage_of_loading);
        slowConnectionWarning = findViewById(R.id.slow_connection_warning);
        backgroundManager = new BackgroundManager(this);
        warningManager = new WarningManager(this);
        ViewModelProvider.AndroidViewModelFactory factory =
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        viewModel = new ViewModelProvider(this, factory).get(ProgressViewModel.class);
        initializeAppValues();
        loadData();
    }

    /*
       Initializes this app's preferences to their default values and the
       list of the user's selected players to an empty list
       Creates a notification channel for the app
     */
    private void initializeAppValues() {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        backgroundManager.createNotificationChannel();
        FileManager fileManager = new FileManager(this);
        fileManager.storeSelectedPlayers(new ArrayList<String>());
    }

    /*
       If the activity was recreated, restores its previous state
       Otherwise, opens a dialog that asks the user which type of network
       connection the app can use, and when an option from the dialog is
       selected, starts loading data
       Registers observers that update the progress bar and the text views on
       the screen while data is being obtained
     */
    private void loadData() {
        viewModel.getProgress().observe(this, progressObserver);
        viewModel.getStageOfLoading().observe(this, stageObserver);
        viewModel.getSlowConnectionWarning().observe(this, slowConnectionObserver);
        WorkManager.getInstance(this)
                .getWorkInfosByTagLiveData(BackgroundManager.FETCH_DATA_WORK_TAG)
                .observe(this, workStateObserver);
        if (viewModel.isFirstTimeCreated()) {
            NetworkTypeDialog dialog = new NetworkTypeDialog();
            dialog.show(getSupportFragmentManager(), "networkType");
            viewModel.setIsFirstTimeCreated(false);
        }
    }

    /*
       Called when the user selects the 'any connection' option of the dialog
       Saves this in Settings so the app will always use any connection
       Schedules background work that loads data, and then sends a notification
       containing a summary of today's tennis tournaments
     */
    @Override
    public void onSelectAnyConnection() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.edit().putBoolean(getString(R.string.pref_network_type_key), false).apply();
        backgroundManager.fetchData();
    }

    /*
       Called when the user selects the 'unmetered connection only' option of
       the dialog
       Saves this in Settings so the app will always use unmetered connection
       Schedules background work that loads data, and then sends a notification
       containing a summary of today's tennis tournaments
     */
    @Override
    public void onSelectUnmeteredOnly() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.edit().putBoolean(getString(R.string.pref_network_type_key), true).apply();
        backgroundManager.fetchData();
    }

    /*
       Makes transitions to the screen based on the given WorkInfo
       If the work is waiting to start because there is no network connection,
       shows a 'No Connection' message on the screen that stays until
       connection is back
       If the work succeeded, saves the app's version code to indicate that
       the app was initialized, and switches back to MainActivity
       If the work failed, shows a message and prevents the user from proceeding
     */
    private void updateScreen(WorkInfo workInfo) {
        WorkInfo.State state = workInfo.getState();
        if (state == WorkInfo.State.ENQUEUED &&
                !backgroundManager.isConnectedToNetwork()) {
            warningManager.showWarning(getResources().getString(R.string.warning_title_no_connection),
                    getResources().getString(R.string.warning_message_no_connection));
        } else if (state == WorkInfo.State.RUNNING) {
            warningManager.dismissWarning();
        } else if (state == WorkInfo.State.SUCCEEDED) {
            final int VERSION_CODE = 0;
            SharedPreferences prefs = getSharedPreferences(
                    getString(R.string.shared_prefs_filename), Context.MODE_PRIVATE);
            prefs.edit().putInt(getString(R.string.version_code_key), VERSION_CODE).apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (state == WorkInfo.State.FAILED) {
            warningManager.showWarning(getResources().getString(R.string.warning_message_process_failed));
        }
    }

}
