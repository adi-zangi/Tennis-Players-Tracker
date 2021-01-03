/*
   A background task that sends a notification
 */

package com.adizangi.tennisplayerstracker.workers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.adizangi.tennisplayerstracker.R;
import com.adizangi.tennisplayerstracker.activities.MainActivity;
import com.adizangi.tennisplayerstracker.utils_data.BackgroundManager;
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

    private FileManager fileManager;
    private BackgroundManager backgroundManager;

    /*
       Constructs a NotificationWorker with the given context and worker params
     */
    public NotificationWorker(@NonNull Context context,
                              @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        fileManager = new FileManager(context);
        backgroundManager = new BackgroundManager(context);
    }

    /*
       If a notification should be sent today based on the selections in
       Settings, and if the notification text that was fetched in
       FetchDataWorker is not empty, sends a notification containing that text
       If the input data contains BackgroundManager.RESCHEDULE_KEY, schedules
       the next data refresh
    */
    @NonNull
    @Override
    public Result doWork() {
        final int NOTIFICATION_ID = 1;
        Context context = getApplicationContext();
        String contentText = fileManager.readNotificationText();
        List<String> selectedPlayers = fileManager.readSelectedPlayers();
        Map<String, PlayerStats> stats = fileManager.readPlayerStats();
        if (backgroundManager.shouldNotifyToday() &&
                !contentText.isEmpty() && !stats.isEmpty()) {
            contentText = addMatchesToContent(contentText, selectedPlayers, stats);
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent
                    .getActivity(context, 0, intent, 0);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder
                    (context, context.getString(R.string.notification_channel_id))
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentText(contentText)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText))
                    .setContentIntent(pendingIntent);
            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(context);
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
        if (getInputData().getBoolean(BackgroundManager.RESCHEDULE_KEY, false)) {
            backgroundManager.scheduleRefresh();
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
        System.out.println("stats");
        for(String player : stats.keySet()) {
            System.out.println(player);
            System.out.println(stats.get(player).getUpcomingMatch());
        }
        System.out.println("selected: " + selectedPlayers);
        StringBuilder upcomingMatches = new StringBuilder();
        for (String player : selectedPlayers) {
            PlayerStats playerStats = stats.get(player);
            if (playerStats != null && !playerStats.getUpcomingMatch().isEmpty()) {
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
