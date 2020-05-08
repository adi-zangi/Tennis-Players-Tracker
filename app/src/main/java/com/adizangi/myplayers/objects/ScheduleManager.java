/*
   Manages scheduling background tasks
 */

package com.adizangi.myplayers.objects;

import android.content.Context;
import android.content.ContextWrapper;

import com.adizangi.myplayers.workers.FetchDataWorker;

import java.util.UUID;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class ScheduleManager extends ContextWrapper {

    // Move this to Settings later
    private NetworkType networkType;

    /*
       Constructs a ScheduleManager with the given application context and the
       given network type, which is the type of network background tasks are
       permitted to use
     */
    public ScheduleManager(Context base, NetworkType networkType) {
        super(base);
        this.networkType = networkType;
    }

    /*
       Schedules background work that fetches data, with a constraint that
       there is network connection
       The work will start as soon as there is network connection of the
       permitted type
       Returns the WorkRequest id
     */
    public UUID scheduleImmediateDataFetch() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(networkType)
                .build();
        OneTimeWorkRequest fetchDataRequest = new OneTimeWorkRequest.Builder
                (FetchDataWorker.class)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(this).enqueue(fetchDataRequest);
        return fetchDataRequest.getId();
    }

}
