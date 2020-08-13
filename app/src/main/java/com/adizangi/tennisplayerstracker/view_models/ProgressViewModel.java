/*
   ViewModel for the views in ProgressActivity
 */

package com.adizangi.tennisplayerstracker.view_models;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.adizangi.tennisplayerstracker.R;
import com.adizangi.tennisplayerstracker.utils_data.BackgroundManager;
import com.adizangi.tennisplayerstracker.workers.FetchDataWorker;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class ProgressViewModel extends AndroidViewModel {

    private MutableLiveData<Integer> progress;
    private MutableLiveData<String> stageOfLoading;
    private MutableLiveData<String> slowConnectionWarning;
    private boolean isFirstTimeCreated;
    private SharedPreferences prefs;

    private Observer<List<WorkInfo>> workStateObserver = new Observer<List<WorkInfo>>() {
        /*
           Called when the WorkInfo of the worker changes
         */
        @Override
        public void onChanged(List<WorkInfo> workInfos) {
            if (!workInfos.isEmpty()) {
                updateViewData(workInfos.get(0));
            }
        }
    };

    /*
       Constructs a ProgressViewModel with the given Application reference
       Initializes the mutable live data to empty values
       Sets an observer for the worker that is scheduled in ProgressActivity
     */
    public ProgressViewModel(@NonNull Application application) {
        super(application);
        progress = new MutableLiveData<>();
        stageOfLoading = new MutableLiveData<>();
        slowConnectionWarning = new MutableLiveData<>();
        isFirstTimeCreated = true;
        prefs = application.getSharedPreferences(
                application.getString(R.string.shared_prefs_filename), Context.MODE_PRIVATE);
        WorkManager.getInstance(application)
                .getWorkInfosByTagLiveData(BackgroundManager.FETCH_DATA_WORK_TAG)
                .observeForever(workStateObserver);
    }
    
    /*
       Sets the variable isFirstTimeCreated to the given boolean
     */
    public void setIsFirstTimeCreated(boolean isFirstTimeCreated) {
        this.isFirstTimeCreated = isFirstTimeCreated;
    }
    
    /*
       Returns true if the activity has only been created once
       Returns false if the activity was recreated, such as if the screen
       rotated or the app was minimized
     */
    public boolean isFirstTimeCreated() {
        return isFirstTimeCreated;
    }

    /*
       Returns a MutableLiveData containing the worker's current progress
     */
    public MutableLiveData<Integer> getProgress() {
        return progress;
    }

    /*
       Returns a MutableLiveData containing a string that describes the
       worker's current stage
     */
    public MutableLiveData<String> getStageOfLoading() {
        return stageOfLoading;
    }

    /*
       Returns a MutableLiveData containing a slow connection warning, which
       will be an empty string if the connection isn't slow
     */
    public MutableLiveData<String> getSlowConnectionWarning() {
        return slowConnectionWarning;
    }

    /*
       Updates the live data values based on the given WorkInfo
       Sets the progress data to the current progress and sets the stage
       based on the state of the work
       If the work succeeded, removes the observer
     */
    private void updateViewData(WorkInfo workInfo) {
        Data progressData = workInfo.getProgress();
        int progressPercentage = progressData.getInt(FetchDataWorker.PROGRESS_KEY, 0);
        progress.setValue(progressPercentage);
        WorkInfo.State state = workInfo.getState();
        if (state == WorkInfo.State.ENQUEUED) {
            boolean isRetrying = prefs.getBoolean(getApplication().getString(R.string.is_worker_retrying_key), false);
            if (isRetrying) {
                stageOfLoading.setValue(getApplication().getString(R.string.text_retrying));
            } else {
                stageOfLoading.setValue(getApplication().getString(R.string.text_preparing_to_start));
            }
        } else if (state == WorkInfo.State.RUNNING) {
            stageOfLoading.setValue(getApplication().getString(R.string.text_starting));
        } else if (state == WorkInfo.State.SUCCEEDED) {
            WorkManager.getInstance(getApplication())
                    .getWorkInfosByTagLiveData(BackgroundManager.FETCH_DATA_WORK_TAG)
                    .removeObserver(workStateObserver);
        }
    }

}