/*
   Receives a broadcast whenever a notification should be sent
   Used with AlarmManager to send periodic notifications
 */

package com.adizangi.myplayers.receivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import androidx.core.app.NotificationManagerCompat;

public class NotificationAlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "MyPlayers Notification Channel";

    private FileManager fileManager;
    private List<String> notificationList;

    @Override
    /*
       Gets the saved notification content, and if it's not empty, sends a
       notification
       If there are current tournaments, adds a button to the notification that
       opens the result of a Google search for each tournament, which will
       display the live scores for each tournament
     */
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);
        fileManager = new FileManager(context);
        notificationList = fileManager.readNotificationList();
        String notificationContent = getNotificationContent();
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentText(notificationContent)
                .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(notificationContent))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        int size = notificationList.size();
        if (size > 1) {
            Intent showScheduleIntent = new Intent(context, ShowScheduleReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast
                    (context, 0, showScheduleIntent, 0);
            builder.setContentIntent(pendingIntent);
            builder.addAction(android.R.color.white, "See Live Scores", pendingIntent);
        }
        Notification notification = builder.build();
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        notificationManager.notify(1, notification);
    }

    /*
       Creates a notification channel for this app and registers it with the
       system, if the phone's version requires it
       If the notification channel already exists, performs no operations
     */
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

    /*
       Gets the saved notification content and adds the upcoming matches for
       each of the user's players at the end of it
       Returns the modified notification content
       Returns an empty string if the notification content is empty
     */
    private String getNotificationContent() {
        if (notificationList.isEmpty()) { // required until app gets data as soon as it opens
            return "";
        }
        String notificationContent = notificationList.get(0);
        List<String> myPlayers = fileManager.readMyPlayers();
        Map<String, PlayerStats> stats = fileManager.readPlayerStats();
        StringBuilder upcomingMatches = new StringBuilder();
        for (String player : myPlayers) {
            PlayerStats playerStats = stats.get(player);
            upcomingMatches.append(playerStats.getUpcomingMatch());
            upcomingMatches.append("\n");
        }
        if (upcomingMatches.length() != 0) {
            upcomingMatches.insert(0, "Matches-\n");
            notificationContent += upcomingMatches.toString();
        }
        return notificationContent;
    }

    /*
       Receives a broadcast when the See Live Scores button in the notification
       is clicked
     */
    public class ShowScheduleReceiver extends BroadcastReceiver {

        @Override
        /*
           Opens the Google search result of each tournament that is currently
           happening, in separate tabs
           The Google search result automatically shows the live scores for
           each tournament
         */
        public void onReceive(Context context, Intent intent) {
            int size = notificationList.size();
            for (int i = 1; i < size; i++) {
                String tournament = notificationList.get(i);
                Intent showScheduleIntent = new Intent(Intent.ACTION_WEB_SEARCH);
                showScheduleIntent.putExtra(SearchManager.QUERY, tournament);
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        }

    }

}
