/*
   Adi Zangi
   2019-2020
 */

package com.adizangi.myplayers.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Spinner;

import com.adizangi.myplayers.Adapters.SpinnerAdapter;
import com.adizangi.myplayers.BuildConfig;
import com.adizangi.myplayers.Objects.FileManager;
import com.adizangi.myplayers.Workers.FetchDataWorker;
import com.adizangi.myplayers.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String VERSION_CODE_KEY = "version_code";

    private List<String> myPlayers;
    private Spinner editPlayersSpinner;
    private SpinnerAdapter spinnerAdapter;
    private FileManager fileManager;

    @Override
    /*
       If the app is running for the first time after installation, schedules a
       daily task that fetched data
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FileManager fileManager = new FileManager(this);
        int currentVersionCode = BuildConfig.VERSION_CODE;
        SharedPreferences sharedPrefs = getPreferences(Context.MODE_PRIVATE);
        int savedVersionCode = sharedPrefs.getInt(VERSION_CODE_KEY, -1);
        if (savedVersionCode == -1) {
            scheduleDailyDataFetch();
            myPlayers = new ArrayList<>();
        } else {

        }
        sharedPrefs.edit().putInt(VERSION_CODE_KEY, currentVersionCode).apply();
    }

    /*
       Schedules a background task that fetches data roughly at 5:00a.m.
       every day, with a constraint that there is network connection
       If there isn't network connection at the scheduled time, the task will
       be delayed until there is
     */
    private void scheduleDailyDataFetch() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) > 5 |
                (calendar.get(Calendar.HOUR_OF_DAY) == 4 &&
                        calendar.get(Calendar.MINUTE) == 59)) {
            calendar.add(Calendar.DATE, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 5);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long initialDelay = calendar.getTimeInMillis() -
                System.currentTimeMillis();
        PeriodicWorkRequest fetchDataRequest = new PeriodicWorkRequest.Builder
                (FetchDataWorker.class, 1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(this).enqueue(fetchDataRequest);
    }

}
