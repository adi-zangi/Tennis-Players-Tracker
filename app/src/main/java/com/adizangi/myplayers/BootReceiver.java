/*
   Receives a broadcast when the device reboots
 */

package com.adizangi.myplayers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

    @Override
    /*
       Restarts the notification alarm
     */
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null &&
                intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

        }
    }

}
