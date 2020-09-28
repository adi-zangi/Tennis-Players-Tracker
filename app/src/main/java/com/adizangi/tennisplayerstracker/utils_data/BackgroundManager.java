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
import java.util.concurrent.TimeUnit;

import androidx.preference.PreferenceManager;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class BackgroundManager extends ContextWrapper {

    public static final String FETCH_DATA_WORK_TAG = "fetchData";
    public static final String NOTIFICATION_WORK_TAG = "notification";
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
       Schedules a chain of work that fetches data and then sends a
       notification
       The chain has a constraint that there is network connection
       If connection stops, the system will retry the work as soon as possible
       The work requests contain the tags FETCH_DATA_WORK_TAG and
       NOTIFICATION_WORK_TAG
     */
    public void fetchDataSendNotif() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(getNetworkType())
                .build();
        OneTimeWorkRequest fetchDataRequest = new OneTimeWorkRequest.Builder
                (FetchDataWorker.class)
                .addTag(FETCH_DATA_WORK_TAG)
                .setConstraints(constraints)
                .setBackoffCriteria(
                        BackoffPolicy.LINEAR,
                        OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                        TimeUnit.MILLISECONDS)
                .build();
        OneTimeWorkRequest notificationRequest = new OneTimeWorkRequest.Builder
                (NotificationWorker.class)
                .addTag(NOTIFICATION_WORK_TAG)
                .build();
        WorkManager.getInstance(this)
                .beginWith(fetchDataRequest)
                .then(notificationRequest)
                .enqueue();
    }

    /*
       Schedules a chain of work that refreshes the app's data and then sends
       a notification
       The work is scheduled to run at midnight, but if the device is on doze
       mode at midnight, then the work will be delayed until the device exits
       doze mode
       The notification worker will receive RESCHEDULE_KEY as input to indicate
       that it should schedule this refresh sequence for the next day
     */
    public void scheduleRefresh() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(getNetworkType())
                .build();
        OneTimeWorkRequest fetchDataRequest = new OneTimeWorkRequest.Builder
                (FetchDataWorker.class)
                .addTag(FETCH_DATA_WORK_TAG)
                .setConstraints(constraints)
                .setInitialDelay(getTimeUntilMidnight(), TimeUnit.MILLISECONDS)
                .build();
        OneTimeWorkRequest notificationRequest = new OneTimeWorkRequest.Builder
                (NotificationWorker.class)
                .addTag(NOTIFICATION_WORK_TAG)
                .setInputData(new Data.Builder()
                        .putBoolean(RESCHEDULE_KEY, true)
                        .build())
                .build();
        WorkManager.getInstance(this)
                .beginWith(fetchDataRequest)
                .then(notificationRequest)
                .enqueue();
    }

    /*
       Cancels and reschedules the __ background work from scheduleDailyRefresh(),
       so any changes in network preferences and the time zone will be applied
     */
    public void rescheduleRefresh() {
        WorkManager workManager = WorkManager.getInstance(this);
        workManager.cancelAllWorkByTag(FETCH_DATA_WORK_TAG);
        workManager.cancelAllWorkByTag(NOTIFICATION_WORK_TAG);
        scheduleRefresh();
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
       Returns true if notifications are enabled and today is a day of the week
       on which the user should receive a notification, based on the current
       selections in this app's Settings
       Returns false otherwise
     */
    public boolean shouldNotifyToday() {
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
       Gets the current value of the network type preference in this app's
       Settings, which indicates the type of network connection this app is
       permitted to use
       Returns a NetworkType that corresponds to the preference value
     */
    private NetworkType getNetworkType() {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        boolean useUnmeteredOnly =
                preferences.getBoolean(getString(R.string.pref_network_type_key), true);
        if (useUnmeteredOnly) {
            return NetworkType.UNMETERED;
        } else {
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
