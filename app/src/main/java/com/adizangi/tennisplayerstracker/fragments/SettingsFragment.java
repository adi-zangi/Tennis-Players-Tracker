/*
   Shows and manages the preferences on the Settings screen
 */

package com.adizangi.tennisplayerstracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.adizangi.tennisplayerstracker.R;
import com.adizangi.tennisplayerstracker.utils_data.BackgroundManager;

import java.util.Arrays;

import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SwitchPreferenceCompat notificationsPref;
    private SwitchPreferenceCompat networkTypePref;
    private MultiSelectListPreference daysPref;
    private Preference moreSettingsPref;

    private Preference.OnPreferenceChangeListener notificationPrefListener =
            new Preference.OnPreferenceChangeListener() {
        /*
           Called when the user enables or disables the notifications
           Updates the visibility of the other notification preferences
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            updateVisibility(newValue);
            return true;
        }
    };

    private Preference.OnPreferenceChangeListener networkTypePrefListener =
            new Preference.OnPreferenceChangeListener() {
        /*
           Called when the user changes the network type preference
           Reschedules background tasks with the new network type
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            networkTypePref.setChecked((boolean) newValue);
            new BackgroundManager(requireContext()).resetDailyUpdates();
            return true;
        }
    };

    private Preference.SummaryProvider<MultiSelectListPreference> daysPrefSummary =
            new Preference.SummaryProvider<MultiSelectListPreference>() {
        /*
           Returns the summary of the notification days preference
         */
        @Override
        public CharSequence provideSummary(MultiSelectListPreference preference) {
            return getDaysSummary(preference);
        }
    };

    /*
       Adds the PreferenceScreen defined in the preferences xml file into the
       view with the given root key
       Sets change listeners for the notifications and network type preferences
       Sets a summary provider for the notification days preference
       Makes the "More notification settings" preference open the notification
       channel settings
       If notifications are on, makes the rest of the notification preferences
       visible
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        notificationsPref = findPreference(getString(R.string.pref_notifications_key));
        networkTypePref = findPreference(getString(R.string.pref_network_type_key));
        daysPref = findPreference(getString(R.string.pref_notification_days_key));
        moreSettingsPref = findPreference(getString(R.string.pref_more_settings_key));

        if (notificationsPref != null && networkTypePref != null && daysPref != null
                && moreSettingsPref != null) {
            notificationsPref.setOnPreferenceChangeListener(notificationPrefListener);
            networkTypePref.setOnPreferenceChangeListener(networkTypePrefListener);
            daysPref.setSummaryProvider(daysPrefSummary);
            Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE,
                    requireContext().getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID,
                    getString(R.string.notification_channel_id));
            moreSettingsPref.setIntent(intent);
            if (notificationsPref.isChecked()) {
                daysPref.setEnabled(true);
                moreSettingsPref.setEnabled(true);
            }
        }
    }

    /*
       If notifications are on, makes the other notification preferences
       enabled
       Otherwise, makes the other notification preferences disabled
     */
    private void updateVisibility(Object newValue) {
        Boolean isNotificationsChecked = (Boolean) newValue;
        if (isNotificationsChecked) {
            daysPref.setEnabled(true);
            moreSettingsPref.setEnabled(true);
        } else {
            daysPref.setEnabled(false);
            moreSettingsPref.setEnabled(false);
        }
    }

    /*
       Returns the summary for the notification days preference, which is a
       comma separated list of the days that are currently checked
       If every day is checked, returns "Every day"
     */
    private String getDaysSummary(MultiSelectListPreference preference) {
        Object[] values = preference.getValues().toArray();
        if (Arrays.equals(values, preference.getEntryValues())) {
            return "Every day";
        }
        StringBuilder summary = new StringBuilder();
        if (values != null) {
            int valuesLength = values.length;
            String[] days = getResources().getStringArray(R.array.short_day_names);
            summary.append(days[preference.findIndexOfValue((String) values[0])]);
            for (int i = 1; i < valuesLength; i++) {
                summary.append(", ");
                summary.append(days[preference.findIndexOfValue((String) values[i])]);
            }
        }
        return summary.toString();
    }

}
