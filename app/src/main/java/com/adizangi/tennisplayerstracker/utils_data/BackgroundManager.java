/*
   Manages background processes like background tasks, network usage, and
   sending notifications
 */

package com.adizangi.tennisplayerstracker.utils_data;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.adizangi.tennisplayerstracker.R;
import com.adizangi.tennisplayerstracker.workers.FetchDataWorker;

import java.util.UUID;

import androidx.preference.PreferenceManager;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class BackgroundManager extends ContextWrapper {

    /*
       Constructs a BackgroundManager with the given application context
     */
    public BackgroundManager(Context base) {
        super(base);
    }

    /*
       Creates a notification channel for this app and registers it with the
       system
       If the notification channel already exists, performs no operations
     */
    public void createNotificationChannel() {
        String id = getString(R.string.notification_channel_id);
        String name = getString(R.string.notification_channel_name);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel =
                new NotificationChannel(id, name, importance);
        NotificationManager notificationManager =
                getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    /*
       Schedules background work that fetches data, with a constraint that
       there is network connection
       The work will start as soon as there is network connection and the
       connection type is selected in Settings as one the app should use
       Returns the WorkRequest id
     */
    public UUID fetchDataImmediately() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(getNetworkType())
                .build();
        OneTimeWorkRequest fetchDataRequest = new OneTimeWorkRequest.Builder
                (FetchDataWorker.class)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(this).enqueue(fetchDataRequest);
        return fetchDataRequest.getId();
    }

    public void scheduleDailyRefresh() {

    }

    /*
       Returns true if there is network connection of a type that this app is
       permitted to use
       Returns false otherwise
     */
    public boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (getNetworkType() == NetworkType.UNMETERED &&
                connectivityManager.isActiveNetworkMetered()) {
            return false;
        }
        NetworkCapabilities capabilities =
                connectivityManager.getNetworkCapabilities(
                        connectivityManager.getActiveNetwork());
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
    }

    /*
       Gets the current value of the network type preference in this app's
       Settings, which indicates the type of network connection this app is
       permitted to use
       Returns a NetworkType that corresponds to the preference value
     */
    private NetworkType getNetworkType() {
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(this);
        boolean useUnmeteredOnly =
                sharedPrefs.getBoolean(getString(R.string.pref_network_type_key), true);
        if (useUnmeteredOnly) {
            return NetworkType.UNMETERED;
        } else {
            return NetworkType.CONNECTED;
        }
    }

}