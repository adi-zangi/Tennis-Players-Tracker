/*
   Receives a broadcast when the time zone changes
 */

package com.adizangi.tennisplayerstracker.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.adizangi.tennisplayerstracker.utils_data.BackgroundManager;

public class TimeZoneChangedReceiver extends BroadcastReceiver {

    /*
       If the broadcast was received due to a time zone change, reschedules
       background tasks that are dependent on time
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (intentAction != null && intentAction.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
            new BackgroundManager(context).resetDailyUpdates();
        }
    }
}
