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

import com.adizangi.tennisplayerstracker.R;
import com.adizangi.tennisplayerstracker.workers.FetchDataWorker;
import com.adizangi.tennisplayerstracker.workers.NotificationWorker;

import java.util.Calendar;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.preference.PreferenceManager;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class BackgroundManager extends ContextWrapper {

    public static final String DOWNLOAD_CONTENT_WORK_NAME = "downloadContent";
    public static final String RESCHEDULE_KEY = "reschedule";

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
       Returns true if notifications are enabled, and if this day of the week is
       included in the selected notification days in Settings
     */
    public boolean isNotificationEnabled() {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        boolean areNotificationsEnabled =
                preferences.getBoolean(getString(R.string.pref_notifications_key), false);
        Set<String> selectedDays = preferences.getStringSet(
                getString(R.string.pref_notification_days_key), null);
        if (areNotificationsEnabled && selectedDays != null) {
            Calendar calendar = Calendar.getInstance();
            String today = Integer.toString(calendar.get(Calendar.DAY_OF_WEEK));
            return selectedDays.contains(today);
        }
        return false;
    }

    /*
       Starts a chain of background work that downloads content
       The chain consists of a worker that fetches tennis data from the ESPN
       website, followed by a worker that sends a notification with the
       newest events
       It will begin right away as long as there is network connection
       If the network disconnects, the work will be retried as soon as possible
       Returns an array of the UUIDs of the work requests in the chain's order
     */
    public UUID[] downloadContent() {
        OneTimeWorkRequest fetchDataReq = new OneTimeWorkRequest.Builder
                (FetchDataWorker.class)
                .setConstraints(new Constraints.Builder()
                        .setRequiredNetworkType(getPermittedNetwork())
                        .build())
                .setBackoffCriteria(
                        BackoffPolicy.LINEAR,
                        OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                        TimeUnit.MILLISECONDS)
                .build();
        OneTimeWorkRequest notificationReq = new OneTimeWorkRequest.Builder
                (NotificationWorker.class)
                .setInputData(new Data.Builder()
                        .putBoolean(RESCHEDULE_KEY, false)
                        .build())
                .build();
        WorkManager.getInstance(this)
                .beginUniqueWork(DOWNLOAD_CONTENT_WORK_NAME,
                        ExistingWorkPolicy.REPLACE,
                        fetchDataReq)
                .then(notificationReq)
                .enqueue();
        return new UUID[]{fetchDataReq.getId(), notificationReq.getId()};
    }

    /*
       Schedules a repeating chain of work that makes a daily update
       The chain consists of a worker that fetches updated tennis data from the
       ESPN website, followed by a worker that sends a notification with the
       newest events
       It is scheduled for midnight each day, but if the device is on doze mode
       at midnight, it will be delayed until the device exits doze mode
       It also requires network connection to start
       The repeating chain is created by adding RESCHEDULE_KEY with the value
       true, so that NotificationWorker will reschedule the next chain
     */
    public void scheduleDailyUpdates() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(getPermittedNetwork())
                .build();
        OneTimeWorkRequest fetchDataReq = new OneTimeWorkRequest.Builder
                (FetchDataWorker.class)
                .setConstraints(constraints)
                .setInitialDelay(getTimeUntilMidnight(), TimeUnit.MILLISECONDS)
                .build();
        OneTimeWorkRequest notificationReq = new OneTimeWorkRequest.Builder
                (NotificationWorker.class)
                .setInputData(new Data.Builder()
                        .putBoolean(RESCHEDULE_KEY, true)
                        .build())
                .build();
        WorkManager.getInstance(this)
                .beginUniqueWork(DOWNLOAD_CONTENT_WORK_NAME,
                        ExistingWorkPolicy.REPLACE,
                        fetchDataReq)
                .then(notificationReq)
                .enqueue();
    }

    /*
       Cancels and reschedules the work chain that is created in
       scheduleDailyUpdates()
       This method can be used to apply changes in the time zone or network
       preferences
     */
    public void resetDailyUpdates() {
        /* Since the work chain was created with the REPLACE policy, it's
           enough to call scheduleDailyUpdates()
           The older chain will be cancelled and the newer one will replace it */
        scheduleDailyUpdates();
    }

    /*
       Sets the 'use wifi only' preference in Settings to the given boolean
     */
    public void setNetworkPreference(boolean useWifiOnly) {
        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(this);
        settings.edit().putBoolean(getString(R.string.pref_connection_key), useWifiOnly).apply();
    }

    /*
       Gets the value of the 'use wifi only' preference in Settings
       If the value is true, returns NetworkType.UNMETERED
       If the value is false, returns NetworkType.CONNECTED
     */
    public NetworkType getPermittedNetwork() {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        boolean useWifiOnly = preferences.getBoolean(getString(R.string.pref_connection_key), true);
        if (useWifiOnly) {
            /* Returns NetworkType.UNMETERED so that only unmetered connection
               (wifi) will be used */
            return NetworkType.UNMETERED;
        } else {
            /* Returns NetworkType.CONNECTED so that any working connection
               will be used, including wifi and cellular data */
            return NetworkType.CONNECTED;
        }
    }

    /*
       Returns the number of milliseconds between the current time and 12:00am
     */
    private long getTimeUntilMidnight() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis() - System.currentTimeMillis();
    }

}
