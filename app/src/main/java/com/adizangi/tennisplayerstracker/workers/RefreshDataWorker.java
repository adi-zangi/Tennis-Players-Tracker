/*
   A background task that fetches data from the web and refreshes the UI with
   updated data
 */

package com.adizangi.tennisplayerstracker.workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.adizangi.tennisplayerstracker.network_calls.NotificationFetcher;
import com.adizangi.tennisplayerstracker.network_calls.PlayerStatsFetcher;
import com.adizangi.tennisplayerstracker.network_calls.TotalPlayersFetcher;
import com.adizangi.tennisplayerstracker.utils_data.FileManager;
import com.adizangi.tennisplayerstracker.utils_data.PlayerStats;

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

public class RefreshDataWorker extends Worker {

    public static final int REFRESH_MSG_CODE = 0;

    private Handler UIHandler;
    private Document mRankings;
    private Document wRankings;
    private Document tSchedule;
    private Document ySchedule;

    /*
       Constructs a RefreshDataWorker with the given context, worker params,
       and UI handler
     */
    public RefreshDataWorker(@NonNull Context context,
                           @NonNull WorkerParameters workerParams,
                           Handler UIHandler) {
        super(context, workerParams);
        this.UIHandler = UIHandler;
    }

    @NonNull
    @Override
    /*
       Fetches the data in the background and saves it to files
       Refreshes the UI
       Returns a Result that indicates whether the task was successful
     */
    public Result doWork() {
        try {
            saveTime(); // method for debugging
            getHTMLDocuments();
            TotalPlayersFetcher playersFetcher =
                    new TotalPlayersFetcher(mRankings, wRankings);
            PlayerStatsFetcher statsFetcher =
                    new PlayerStatsFetcher(mRankings, wRankings);
            NotificationFetcher notifFetcher =
                    new NotificationFetcher(tSchedule, ySchedule);
            List<String> totalPlayers = playersFetcher.getTotalPlayersList();
            Map<String, PlayerStats> stats = statsFetcher.getPlayerStatsMap();
            List<String> notificationList = notifFetcher.getNotificationList();
            FileManager fileManager = new FileManager(getApplicationContext());
            fileManager.storeTotalPlayers(totalPlayers);
            fileManager.storePlayerStats(stats);
            fileManager.storeNotificationList(notificationList);
            Message refreshMessage = UIHandler.obtainMessage(REFRESH_MSG_CODE);
            refreshMessage.sendToTarget();
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

    private void saveTime() {
        SharedPreferences sharedPrefs = getApplicationContext()
                .getSharedPreferences("Time file", Context.MODE_PRIVATE);
        Calendar calendar = Calendar.getInstance();
        sharedPrefs.edit().putInt("Hour", calendar.get(Calendar.HOUR_OF_DAY)).apply();
        sharedPrefs.edit().putInt("Minute", calendar.get(Calendar.MINUTE)).apply();
    }

}
