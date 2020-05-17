/*
   Manages scheduling background tasks
 */

package com.adizangi.tennisplayerstracker.utils_data;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;

import com.adizangi.tennisplayerstracker.workers.CustomWorkerFactory;
import com.adizangi.tennisplayerstracker.workers.FetchDataWorker;

import java.util.UUID;

import androidx.work.Configuration;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkerFactory;

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
       Initializes WorkManager with the given Handler on the UI
     */
    public void initializeWorkManager(Handler UIHandler) {
        WorkerFactory workerFactory = new CustomWorkerFactory(UIHandler);
        Configuration configuration = new Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build();
        WorkManager.initialize(this, configuration);
    }

    /*
       Schedules background work that fetches data, with a constraint that
       there is network connection
       The work will start as soon as there is network connection of the
       permitted type
       Returns the WorkRequest id
     */
    public UUID fetchDataImmediately() {
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

    public void scheduleDailyDataFetch() {

    }

    /*
       Returns true if there is network connection of the permitted type
       Returns false otherwise
     */
    public boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (networkType == NetworkType.UNMETERED &&
                connectivityManager.isActiveNetworkMetered()) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities capabilities =
                    connectivityManager.getNetworkCapabilities(
                            connectivityManager.getActiveNetwork());
            return capabilities != null &&
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
        } else {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
    }

}
