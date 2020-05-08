package com.adizangi.myplayers.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.adizangi.myplayers.R;

public class ProgressBarActivity extends AppCompatActivity {

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
    }
}
