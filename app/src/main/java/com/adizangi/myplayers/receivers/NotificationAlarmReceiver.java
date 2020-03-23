package com.adizangi.myplayers.receivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.adizangi.myplayers.R;
import com.adizangi.myplayers.objects.FileManager;
import com.adizangi.myplayers.objects.PlayerStats;

import java.util.List;
import java.util.Map;

import androidx.core.app.NotificationCompat;

public class NotificationAlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "MyPlayers Notification Channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);
        FileManager fileManager = new FileManager(context);
        List<String> notificationList = fileManager.readNotificationList();
        if (!notificationList.isEmpty() && !notificationList.get(0).isEmpty()) {
            String notificationContent = notificationList.get(0);
            List<String> myPlayers = fileManager.readMyPlayers();
            Map<String, PlayerStats> stats = fileManager.readPlayerStats();
            StringBuilder upcomingMatches = new StringBuilder();
            for (String player : myPlayers) {
                PlayerStats playerStats = stats.get(player);
                upcomingMatches.append(playerStats.getUpcomingMatch());
                upcomingMatches.append("\n");
            }
            notificationContent += upcomingMatches.toString();
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentText(notificationContent)
                    .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(notificationContent))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            int size = notificationList.size();
            if (size > 1) {
                // make web searches
            }
        }
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = context.getString(R.string.notification_channel_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, name, importance);
            NotificationManager notificationManager =
                    context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
