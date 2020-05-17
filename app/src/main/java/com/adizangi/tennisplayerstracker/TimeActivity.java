package com.adizangi.tennisplayerstracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.adizangi.tennisplayerstracker.R;

public class TimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        Toolbar toolbar = findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = findViewById(R.id.textView);
        SharedPreferences sharedPrefs = getApplicationContext()
                .getSharedPreferences("Time file", Context.MODE_PRIVATE);
        int hour = sharedPrefs.getInt("Hour", -1);
        int minute = sharedPrefs.getInt("Minute", -1);
        if (hour == -1) {
            String text = "Worker has not run yet";
            textView.setText(text);
        } else {
            String text = "Time of last Worker run: " + hour + ":" + minute;
            textView.setText(text);
        }
    }
}
