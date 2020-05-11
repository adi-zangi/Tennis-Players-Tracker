/*
   A background task that fetches data that will be displayed in the app and
   sent to the user in notifications
 */

package com.adizangi.myplayers.workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.adizangi.myplayers.fragments.AlertMessageCreator;
import com.adizangi.myplayers.objects.FileManager;
import com.adizangi.myplayers.objects.NotificationFetcher;
import com.adizangi.myplayers.objects.PlayerStats;
import com.adizangi.myplayers.objects.PlayerStatsFetcher;
import com.adizangi.myplayers.objects.ScheduleManager;
import com.adizangi.myplayers.objects.TotalPlayersFetcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class FetchDataWorker extends Worker {

    public static final String PROGRESS_KEY = "PROGRESS";

    private Document mRankings;
    private Document wRankings;
    private Document tSchedule;
    private Document ySchedule;

    /*
       Constructs a FetchDataWorker
     */
    public FetchDataWorker(@NonNull Context context,
                           @NonNull WorkerParameters workerParams,
                           Handler UIHandler) {
        super(context, workerParams);
    }

    /*
       Fetches the data in the background and saves it to files
       Updates the observable progress as the work is running
       Returns a Result that indicates whether the task was successful
     */
    @NonNull
    @Override
    public Result doWork() {
        try {
            setProgress(0);
            saveTime(); // method for debugging
            getHTMLDocuments();
            setProgress(10);
            TotalPlayersFetcher playersFetcher =
                    new TotalPlayersFetcher(mRankings, wRankings);
            PlayerStatsFetcher statsFetcher =
                    new PlayerStatsFetcher(mRankings, wRankings);
            NotificationFetcher notifFetcher =
                    new NotificationFetcher(tSchedule, ySchedule);
            List<String> totalPlayers = playersFetcher.getTotalPlayersList();
            setProgress(40);
            Map<String, PlayerStats> stats = statsFetcher.getPlayerStatsMap();
            setProgress(70);
            List<String> notificationList = notifFetcher.getNotificationList();
            setProgress(100);
            FileManager fileManager = new FileManager(getApplicationContext());
            fileManager.storeTotalPlayers(totalPlayers);
            fileManager.storePlayerStats(stats);
            fileManager.storeNotificationList(notificationList);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            // Check network connection and retry if disconnected
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

    /*
       Sets this Work's observable progress to the given progress percentage
     */
    private void setProgress(int progressPercentage) {
        Data progress = new Data.Builder()
                .putInt(PROGRESS_KEY, progressPercentage)
                .build();
        setProgressAsync(progress);
    }

    private void saveTime() {
        SharedPreferences sharedPrefs = getApplicationContext()
                .getSharedPreferences("Time file", Context.MODE_PRIVATE);
        Calendar calendar = Calendar.getInstance();
        sharedPrefs.edit().putInt("Hour", calendar.get(Calendar.HOUR_OF_DAY)).apply();
        sharedPrefs.edit().putInt("Minute", calendar.get(Calendar.MINUTE)).apply();
    }
}
