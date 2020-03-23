package com.adizangi.myplayers.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.adizangi.myplayers.objects.FileManager;

import java.util.List;

public class NotificationAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        FileManager fileManager = new FileManager(context);
        List<String> notificationList = fileManager.readNotificationList();
        String notificationContent = notificationList.get(0);
        if (!notificationContent.isEmpty()) {

        }
    }
}
