/*
   Adi Zangi
   2019-2020

   This app is meant for people who like to watch professional tennis
   The purpose of the app Tennis Players Tracker is to help the user keep track
   of tennis players that they select
   The user can select players and view statistics and tournament results for
   each of those players, which are updated daily
   The user can also receive notifications that give a summary of the
   tournaments that are going on for the day and the times at which the
   selected players are playing

   MainActivity is the home screen of the app
   The screen is separated into two tabs
   The first tab shows the information about each selected player, and
   the second tab enables to edit the currently selected players
 */

package com.adizangi.tennisplayerstracker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import androidx.work.Configuration;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkerFactory;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.adizangi.tennisplayerstracker.TimeActivity;
import com.adizangi.tennisplayerstracker.adapters.TabAdapter;
import com.adizangi.tennisplayerstracker.utils_data.FileManager;
import com.adizangi.tennisplayerstracker.receivers.NotifAlarmReceiver;
import com.adizangi.tennisplayerstracker.workers.CustomWorkerFactory;
import com.adizangi.tennisplayerstracker.R;
import com.adizangi.tennisplayerstracker.workers.RefreshDataWorker;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TabLayoutMediator.TabConfigurationStrategy tabConfiguration =
            new TabLayoutMediator.TabConfigurationStrategy() {
        @Override
        /*
           Sets the given tab's title such that it matches the given position
        */
        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
            String tabTitle = TabAdapter.TAB_TITLES[position];
            tab.setText(tabTitle);
        }
    };

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // refresh
        }
    };

    /*
       Called when the app is launched
       Displays the MainActivity layout and fills it with data
       If the app is running for the first time after installation, schedules a
       daily task that fetches data and a daily notification
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeWorkManager();
        ComponentName callingActivity = getCallingActivity();
        FileManager fileManager = new FileManager(this);
        int savedVersionCode = fileManager.readVersionCode();
        if (savedVersionCode == -1) {
            Intent intent = new Intent(this, ProgressActivity.class);
            startActivity(intent);
        } else if (callingActivity != null && callingActivity.getClassName()
                .equals("com.adizangi.tennisplayerstracker.activities.ProgressActivity")) {
            // maybe also schedule other things, or maybe in main activity after start this activity
            // new user dialog
            //scheduleDailyDataFetch();
            //scheduleDailyNotif();
        }
         else {
            Toolbar toolbar = findViewById(R.id.appBar);
            setSupportActionBar(toolbar);
            setUpTabs();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.time) {
            Intent intent = new Intent(this, TimeActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /*
       Initializes WorkManager with a custom worker factory, but only if this
       activity started after the app was launched
       If this activity was started in another way, does nothing
     */
    private void initializeWorkManager() {
        Intent activityIntent = getIntent();
        String intentAction = activityIntent.getAction();
        if (intentAction != null && intentAction.equals("android.intent.action.MAIN")) {
            WorkerFactory workerFactory = new CustomWorkerFactory(handler);
            Configuration configuration = new Configuration.Builder()
                    .setWorkerFactory(workerFactory)
                    .build();
            WorkManager.initialize(this, configuration);
        }
    }

    /*
       Initializes the TabLayout with a Stats tab and a Players tab
       The tabs can be switched between by either tapping or swiping
     */
    private void setUpTabs() {
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager2 = findViewById(R.id.view_pager);
        TabAdapter tabAdapter = new TabAdapter(this);
        viewPager2.setAdapter(tabAdapter);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager2,
                tabConfiguration);
        mediator.attach();
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
        long initialDelay = calendar.getTimeInMillis() - System.currentTimeMillis();
        PeriodicWorkRequest fetchDataRequest = new PeriodicWorkRequest.Builder
                (RefreshDataWorker.class, 1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(this).enqueue(fetchDataRequest);
    }

    /*
       Schedules a notification for the user at 9:30 a.m. daily
       If there is no information to send to the user on a specific day, no
       notification will be sent
     */
    private void scheduleDailyNotif() {
        AlarmManager alarmManager =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotifAlarmReceiver.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(this, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        long elapsedTime = calendar.getTimeInMillis() - System.currentTimeMillis();
        alarmManager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, elapsedTime,
                pendingIntent);
    }

}
