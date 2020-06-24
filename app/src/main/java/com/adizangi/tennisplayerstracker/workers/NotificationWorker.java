package com.adizangi.tennisplayerstracker.workers;

import android.app.Notification;
import android.content.Context;

import com.adizangi.tennisplayerstracker.R;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWorker extends Worker {

    private Context context;

    public NotificationWorker(@NonNull Context context,
                              @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        final int NOTIFICATION_ID = 1;
        Notification notification = new NotificationCompat.Builder
                (context, context.getString(R.string.notification_channel_id))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Notification")
                .setContentText("This is a notification")
                .build();
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, notification);
        return Result.success();
    }
}
