package com.adizangi.tennisplayerstracker.workers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.adizangi.tennisplayerstracker.R;
import com.adizangi.tennisplayerstracker.receivers.NotificationButtonReceiver;
import com.adizangi.tennisplayerstracker.utils_data.FileManager;
import com.adizangi.tennisplayerstracker.utils_data.PlayerStats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWorker extends Worker {

    public static final String TOURNAMENTS = "com.adizangi.tennisplayerstracker.TOURNAMENTS";

    public NotificationWorker(@NonNull Context context,
                              @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        final int NOTIFICATION_ID = 1;
        Context context = getApplicationContext();
        FileManager fileManager = new FileManager(context);
        List<String> notificationList = fileManager.readNotificationList();
        notificationList.set(0, "bla..");
        notificationList.add("Roland Garros");
        notificationList.add("Australian Open");
        List<String> selectedPlayers = fileManager.readSelectedPlayers();
        Map<String, PlayerStats> stats = fileManager.readPlayerStats();
        int size = notificationList.size();
        if (!notificationList.isEmpty() && !stats.isEmpty()
                && !notificationList.get(0).isEmpty()) {
            String contentText = notificationList.get(0);
            if (!selectedPlayers.isEmpty()) {
                contentText = addMatchesToContent(contentText, selectedPlayers, stats);
            }
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder
                    (context, context.getString(R.string.notification_channel_id))
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentText(contentText)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText));
            if (size > 1) {
                String[] tournaments =
                        notificationList.subList(1, size).toArray(new String[size - 1]);
                addNotificationButton(notificationBuilder, tournaments);
            }
            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(context);
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
        return Result.success();
    }

    private String addMatchesToContent(String currentContent,
                                       List<String> selectedPlayers,
                                       Map<String, PlayerStats> stats) {
        StringBuilder upcomingMatches = new StringBuilder("Matches-\n");
        for (String player : selectedPlayers) {
            PlayerStats playerStats = stats.get(player);
            if (playerStats != null) {
                upcomingMatches.append(playerStats.getUpcomingMatch());
                upcomingMatches.append("\n");
            }
        }
        return currentContent + upcomingMatches.toString();
    }

    private void addNotificationButton(NotificationCompat.Builder builder,
                                       String[] tournaments) {
        Context context = getApplicationContext();
        Intent buttonIntent = new Intent(context, NotificationButtonReceiver.class)
                .putExtra(TOURNAMENTS, tournaments);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (context, 0, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(android.R.color.white,
                context.getString(R.string.button_live_scores), pendingIntent);
    }

}
