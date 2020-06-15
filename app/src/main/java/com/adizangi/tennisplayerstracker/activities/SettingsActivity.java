/*
   The Settings screen
   Currently Android's default Settings screen, will be changed later
 */

package com.adizangi.tennisplayerstracker.activities;

import android.os.Bundle;
import android.util.Log;

import com.adizangi.tennisplayerstracker.R;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.app_bar);
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

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private SwitchPreferenceCompat notificationsPref;
        private MultiSelectListPreference daysPref;
        private Preference moreSettingsPref;

        private Preference.OnPreferenceChangeListener notificationPrefListener =
                new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean isNotificationsChecked = (Boolean) newValue;
                Log.i("Debug", "listener called. value is " + isNotificationsChecked);
                if (isNotificationsChecked) {
                    daysPref.setEnabled(true);
                    moreSettingsPref.setEnabled(true);
                } else {
                    daysPref.setEnabled(false);
                    moreSettingsPref.setEnabled(false);
                }
                return true;
            }
        };

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
            // Add custom summary for list preference which is entries joined with ", "
            // when changes, set summary
            // change visibility of days when notifications value changes
            notificationsPref = findPreference(getString(R.string.pref_notifications_key));
            daysPref = findPreference(getString(R.string.pref_notification_days_key));
            moreSettingsPref = findPreference(getString(R.string.pref_more_settings_key));
            if (notificationsPref != null && daysPref != null && moreSettingsPref != null
                    && notificationsPref.isChecked()) {
                daysPref.setEnabled(true);
                moreSettingsPref.setEnabled(true);
            }
            if (notificationsPref != null) {
                notificationsPref.setOnPreferenceChangeListener(notificationPrefListener);
            }
        }
    }
}