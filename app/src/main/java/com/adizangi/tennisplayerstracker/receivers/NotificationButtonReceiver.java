package com.adizangi.tennisplayerstracker.receivers;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.adizangi.tennisplayerstracker.workers.NotificationWorker;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotificationButtonReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String[] tournaments = intent.getStringArrayExtra(NotificationWorker.TOURNAMENTS);
            int length = tournaments.length;
            Intent[] intents = new Intent[length];
            for (int i = 0; i < length; i++) {
                String query = URLEncoder.encode(tournaments[i], "UTF-8");
                Uri uri = Uri.parse("http://www.google.com/#q=" + query);
                Intent liveScoresIntent = new Intent(Intent.ACTION_VIEW, uri)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                /*
                Intent liveScoresIntent = new Intent(Intent.ACTION_WEB_SEARCH)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(SearchManager.QUERY, tournaments[i]);


                 */
                intents[i] = liveScoresIntent;
            }
            Log.i("Debug", Arrays.toString(intents));
            if (intents[0].resolveActivity(context.getPackageManager()) != null) {
                context.startActivities(intents);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
