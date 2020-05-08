package com.adizangi.myplayers.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.adizangi.myplayers.R;
import com.adizangi.myplayers.objects.ScheduleManager;
import com.adizangi.myplayers.workers.FetchDataWorker;

import java.util.UUID;

public class ProgressBarActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);
        Toolbar toolbar = findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        // Do work, when status is finished save version code and start main activity
        // do something for notifying if there is no internet before and also when work is stopped
        // maybe also schedule other things, or maybe in main activity after start this activity
        // add class that manages scheduling
        // while work is enqueued or stopped, show dialog that says needs connection (app will
        // not start until there is network connection). No buttons in dialog.
        // have method that checks if connected. If connected and delayed, show dialog that
        // says phone is busy, and may need to be charged.

        // first, say app uses network and ask which network type to use- wifi only or any
        // network. which network type should this app use? use any network/use wifi only.
        // send the right network type when construct schedule manager

        progressBar = findViewById(R.id.progressBar);
        ScheduleManager scheduleManager = new ScheduleManager(this, NetworkType.CONNECTED);
        UUID workRequestId = scheduleManager.fetchDataImmediately();
        WorkManager.getInstance(this)
                .getWorkInfoByIdLiveData(workRequestId)
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        Data progressData = workInfo.getProgress();
                        int progress = progressData.getInt(FetchDataWorker.PROGRESS_KEY, 0);
                        progressBar.setProgress(progress);
                    }
                });
    }
}
