/*
   Receives a broadcast the first time that data should be fetched
   Schedules periodic data fetches
 */

package com.adizangi.myplayers.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.adizangi.myplayers.workers.FetchDataWorker;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class InitialFetchReceiver extends BroadcastReceiver {

    @Override
    /*
       Schedules a background task that will fetch data right now, and another
       background task that will fetch data 24 hours from now and will repeat
       every 24 hours after that
       The tasks have a constraint that there is network connection
       If there isn't network connection when the task is scheduled to run, the
       task will be delayed until there is
     */
    public void onReceive(Context context, Intent intent) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest fetchDataImmediately = new OneTimeWorkRequest.Builder
                (FetchDataWorker.class)
                .setConstraints(constraints)
                .build();
        PeriodicWorkRequest fetchDataPeriodically = new PeriodicWorkRequest.Builder
                (FetchDataWorker.class, 1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build();
        WorkManager workManager = WorkManager.getInstance(context);
        workManager.enqueue(fetchDataImmediately);
        workManager.enqueue(fetchDataPeriodically);
    }
}
