/*
   Adi Zangi
   2019-2020
 */

package com.adizangi.myplayers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.os.Bundle;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        /*
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) > 5) {

        }
        PeriodicWorkRequest fetchDataRequest = new PeriodicWorkRequest.Builder(FetchDataWorker.class, )

         */
        OneTimeWorkRequest fetchDataRequest =
                new OneTimeWorkRequest.Builder(FetchDataWorker.class)
                        .setConstraints(constraints)
                        .build();
        WorkManager.getInstance(this).enqueue(fetchDataRequest);
    }

}
