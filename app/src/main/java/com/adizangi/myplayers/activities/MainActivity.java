/*
   Adi Zangi
   2019-2020
 */

package com.adizangi.myplayers.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.adizangi.myplayers.BuildConfig;
import com.adizangi.myplayers.adapters.TabAdapter;
import com.adizangi.myplayers.objects.FileManager;
import com.adizangi.myplayers.workers.FetchDataWorker;
import com.adizangi.myplayers.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String VERSION_CODE_KEY = "version_code";

    @Override
    /*
       Called when the MainActivity is created
       This activity controls the app's home screen and will be created
       whenever the app is launched
       Displays the MainActivity layout and fills it with data
       If the app is running for the first time after installation, schedules a
       daily task that fetched data
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpTabs();
        int currentVersionCode = BuildConfig.VERSION_CODE;
        SharedPreferences sharedPrefs = getPreferences(Context.MODE_PRIVATE);
        int savedVersionCode = sharedPrefs.getInt(VERSION_CODE_KEY, -1);
        if (savedVersionCode == -1) {
            scheduleDailyDataFetch();
        }
        sharedPrefs.edit().putInt(VERSION_CODE_KEY, currentVersionCode).apply();
    }

    /*
       Adds tabs to the TabLayout
     */
    private void setUpTabs() {
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager2 = findViewById(R.id.view_pager);
        TabAdapter tabAdapter = new TabAdapter(this);
        viewPager2.setAdapter(tabAdapter);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    /*
                       Configures the given tab that is at the given position such that it
                       matches the Fragment at this position in the ViewPager2
                       Sets the tab's title, which is Stats for position 0 and Players for
                       position 1
                     */
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        String tabTitle = TabAdapter.TAB_TITLES[position];
                        tab.setText(tabTitle);
                    }
                });
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
