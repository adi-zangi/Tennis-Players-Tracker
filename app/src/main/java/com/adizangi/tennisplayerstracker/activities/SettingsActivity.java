/*
   The Settings screen of the app
 */

package com.adizangi.tennisplayerstracker.activities;

import android.os.Bundle;

import com.adizangi.tennisplayerstracker.R;
import com.adizangi.tennisplayerstracker.fragments.SettingsFragment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    @Override
    /*
       Displays the Settings screen
       It includes an action bar with a back button that returns to
       MainActivity, and SettingsFragment which fills the rest of the screen
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
    }
}