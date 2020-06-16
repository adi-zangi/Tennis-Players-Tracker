package com.adizangi.tennisplayerstracker.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.adizangi.tennisplayerstracker.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SwitchPreferenceCompat notificationsPref;
    private MultiSelectListPreference daysPref;
    private Preference moreSettingsPref;

    private Preference.OnPreferenceChangeListener notificationPrefListener =
            new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    updateVisibility(newValue);
                    return true;
                }
            };

    private Preference.SummaryProvider<MultiSelectListPreference> daysPrefSummary =
            new Preference.SummaryProvider<MultiSelectListPreference>() {
                @Override
                public CharSequence provideSummary(MultiSelectListPreference preference) {
                    return getDaysSummary(preference);
                }
            };

    @Override
    /*
       Notif prefs are enabled only when notifs are enabled
       Custom summary
       Opens notification channel settings
     */
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        notificationsPref = findPreference(getString(R.string.pref_notifications_key));
        daysPref = findPreference(getString(R.string.pref_notification_days_key));
        moreSettingsPref = findPreference(getString(R.string.pref_more_settings_key));

        if (notificationsPref != null && daysPref != null && moreSettingsPref != null) {
            notificationsPref.setOnPreferenceChangeListener(notificationPrefListener);
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
