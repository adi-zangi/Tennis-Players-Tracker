/*
   A background task that fetches data that will be displayed in the app and
   sent to the user in notifications
 */

package com.adizangi.myplayers;

import android.content.Context;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class FetchDataWorker extends Worker {

    private Document mRankings;
    private Document wRankings;
    private Document tSchedule;
    private Document ySchedule;

    /*
       Constructs a FetchDataWorker
     */
    public FetchDataWorker(@NonNull Context context,
                           @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    /*
       Fetches the data in the background and saves it to files
       Returns a Result that indicates whether the task was successful
     */
    public Result doWork() {
        try {
            getHTMLDocuments();
            TotalPlayersFetcher playersFetcher =
                    new TotalPlayersFetcher(mRankings, wRankings);
            PlayerStatsFetcher statsFetcher =
                    new PlayerStatsFetcher(mRankings, wRankings);
            NotificationFetcher notifFetcher =
                    new NotificationFetcher(tSchedule, ySchedule);
            List<String> totalPlayers = playersFetcher.getTotalPlayersList();
            Map<String, PlayerStats> stats = statsFetcher.getPlayerDetailsMap();
            List<String> notificationList = notifFetcher.getNotificationList();
            FileManager fileManager = new FileManager(getApplicationContext());
            fileManager.storeTotalPlayers(totalPlayers);
            fileManager.storePlayerStats(stats);
            fileManager.storeNotificationList(notificationList);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }

    /*
       Gets HTML Documents that the data will be taken from
       The Documents include men's tennis rankings, women's tennis rankings,
       today's match schedule, and yesterday's match schedule from the ESPN
       website
     */
    private void getHTMLDocuments() throws IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyyMMdd", Locale.US);
        String dateOfYesterday = dateFormat.format(calendar.getTime());
        mRankings = Jsoup.connect
                ("https://www.espn.com/tennis/rankings").get();
        wRankings = Jsoup.connect
                ("https://www.espn.com/tennis/rankings/_/type/wta").get();
        tSchedule = Jsoup.connect
                ("http://www.espn.com/tennis/dailyResults").get();
        ySchedule = Jsoup.connect
                ("http://www.espn.com/tennis/dailyResults?date=" +
                        dateOfYesterday).get();
    }

}