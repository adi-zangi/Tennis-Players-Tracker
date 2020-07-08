/*
   A background task that sends a notification
 */

package com.adizangi.tennisplayerstracker.workers;

import android.content.Context;

import com.adizangi.tennisplayerstracker.R;
import com.adizangi.tennisplayerstracker.utils_data.FileManager;
import com.adizangi.tennisplayerstracker.utils_data.PlayerStats;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWorker extends Worker {

    /*
       Constructs a NotificationWorker with the given context and worker params
     */
    public NotificationWorker(@NonNull Context context,
                              @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /*
       If the notification text that was fetched in FetchDataWorker is not
       empty, sends a notification containing that text
    */
    @NonNull
    @Override
    public Result doWork() {
        final int NOTIFICATION_ID = 1;
        Context context = getApplicationContext();
        FileManager fileManager = new FileManager(context);
        String contentText = fileManager.readNotificationText();
        List<String> selectedPlayers = fileManager.readSelectedPlayers();
        Map<String, PlayerStats> stats = fileManager.readPlayerStats();
        if (!contentText.isEmpty() && !stats.isEmpty()) {
            contentText = addMatchesToContent(contentText, selectedPlayers, stats);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder
                    (context, context.getString(R.string.notification_channel_id))
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentText(contentText)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText));
            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(context);
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
        return Result.success();
    }

    /*
      For each of the user's selected players that has an upcoming match today,
      adds details about that match to the notification text
    */
    private String addMatchesToContent(String currentContent,
                                       List<String> selectedPlayers,
                                       Map<String, PlayerStats> stats) {
        StringBuilder upcomingMatches = new StringBuilder();
        for (String player : selectedPlayers) {
            PlayerStats playerStats = stats.get(player);
            if (playerStats != null) {
                upcomingMatches.append(playerStats.getUpcomingMatch());
                upcomingMatches.append("\n");
            }
        }
        if (upcomingMatches.length() > 0) {
            upcomingMatches.insert(0, "Matches-\n");
            return currentContent + upcomingMatches.toString();
        }
        return currentContent;
    }

}
