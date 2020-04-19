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
        // make loading screen centered
        // Do work, when status is finished save version code and start main activity
        // maybe also schedule other things, or maybe in main activity after start this activity
        // add class that manages scheduling
    }
}
