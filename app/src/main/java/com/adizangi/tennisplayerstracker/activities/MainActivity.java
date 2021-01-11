/*
   Adi Zangi
   2019-2020

   Tennis Players Tracker is a tool for people who like to watch professional
   tennis
   The app helps the user keep track of tennis players' progress in tournaments
   The user can add players to their list and view statistics and tournament
   results for each of those players, which are refreshed daily
   The user can also get notifications about the most recent tournaments

   MainActivity is the home screen of the app
   It is separated into two tabs-
   The first tab shows the statistics of each selected player, and
   the second tab enables to edit the players list
 */

package com.adizangi.tennisplayerstracker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.adizangi.tennisplayerstracker.BuildConfig;
import com.adizangi.tennisplayerstracker.TimeActivity;
import com.adizangi.tennisplayerstracker.adapters.TabAdapter;
import com.adizangi.tennisplayerstracker.dialogs.CreditsDialog;
import com.adizangi.tennisplayerstracker.dialogs.FeaturesDialog;
import com.adizangi.tennisplayerstracker.utils_data.BackgroundManager;
import com.adizangi.tennisplayerstracker.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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

    /*
       Opens the app's main page based on the state of the app
       If the app is opening for the first time after it was installed or
       cleared, shows a loading screen that initializes the app
       Otherwise, shows the content defined in the layout resource file, sets
       up an action bar, and fills the screen with data
       If this activity is opening for the first time after the loading screen
       finished loading, also shows a dialog that explains how to use the app
       and schedules a task that refreshes the app's data every day
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final int DOESNT_EXIST = -1;
        int currentVersionCode = BuildConfig.VERSION_CODE;
        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.shared_prefs_filename), Context.MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(getString(R.string.version_code_key), DOESNT_EXIST);
        boolean isNewInstall = savedVersionCode == DOESNT_EXIST;
        boolean isInitialized = savedVersionCode < currentVersionCode;
        boolean isRegularRun = savedVersionCode == currentVersionCode;
        if (isRegularRun) {
            Toolbar toolbar = findViewById(R.id.action_bar);
            setSupportActionBar(toolbar);
            setUpTabs();
        } else if (isNewInstall) {
            Intent intent = new Intent(this, ProgressBarActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (isInitialized) {
            Toolbar toolbar = findViewById(R.id.action_bar);
            setSupportActionBar(toolbar);
            setUpTabs();
            FeaturesDialog dialog = new FeaturesDialog();
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), "features");
            BackgroundManager backgroundManager = new BackgroundManager(this);
            backgroundManager.scheduleDailyUpdates();
            prefs.edit().putInt(getString(R.string.version_code_key), currentVersionCode).apply();
        }
    }

    /*
       Initializes the action bar with buttons
       Adds the buttons defined in action_bar.xml to the given Menu
       Returns true so the buttons will show
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_items, menu);
        return true;
    }

    /*
       Called when an item from the action bar is selected
       If 'Settings' was selected, opens SettingsActivity
       If 'How to use' was selected, shows a dialog that explains how to use
       the app
       If 'Credits' was selected, shows a dialog that displays a list of
       credits for the information and images in this app
       If the action was not recognized, passes it to the superclass
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.item_how_to_use:
                FeaturesDialog featuresDialog = new FeaturesDialog();
                featuresDialog.show(getSupportFragmentManager(), "features");
                return true;
            case R.id.item_credits:
                // Add credit for app icon
                CreditsDialog creditsDialog = new CreditsDialog();
                creditsDialog.show(getSupportFragmentManager(), "credits");
                return true;
            case  R.id.item_time:
                // Remove
                Intent i = new Intent(this, TimeActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
       Initializes the TabLayout with a Stats tab and a Players tab
       The tabs can be switched between by either tapping or swiping
     */
    private void setUpTabs() {
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager2 = findViewById(R.id.view_pager_2);
        TabAdapter tabAdapter = new TabAdapter(this);
        viewPager2.setAdapter(tabAdapter);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager2,
                tabConfiguration);
        mediator.attach();
    }

}
