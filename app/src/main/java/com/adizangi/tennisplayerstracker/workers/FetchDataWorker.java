/*
   A background task that fetches data from the web and has progress that is
   observable by the UI
 */

package com.adizangi.tennisplayerstracker.workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.adizangi.tennisplayerstracker.R;
import com.adizangi.tennisplayerstracker.network_calls.NotificationFetcher;
import com.adizangi.tennisplayerstracker.network_calls.PlayerStatsFetcher;
import com.adizangi.tennisplayerstracker.network_calls.TotalPlayersFetcher;
import com.adizangi.tennisplayerstracker.utils_data.BackgroundManager;
import com.adizangi.tennisplayerstracker.utils_data.FileManager;
import com.adizangi.tennisplayerstracker.utils_data.PlayerStats;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class FetchDataWorker extends Worker {

    public static final String PROGRESS_KEY = "progress";

    private Document mRankings;
    private Document wRankings;
    private Document tSchedule;
    private Document ySchedule;

    /*
       Constructs a FetchDataWorker with the given context and worker params
     */
    public FetchDataWorker(@NonNull Context context,
                           @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /*
       Fetches the data in the background and saves it in files
       Updates the observable progress while the work is running
       After the data is saved, schedules another worker that sends a
       notification
       Returns Result.success() if the work was successful, Result.retry() if
       the work failed due to a problem with the network, and Result.failure()
       if the work failed for another reason
     */
    @NonNull
    @Override
    public Result doWork() {
        try {
            setProgress(0); log("FetchDataWorker starting work");
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
            setProgress(40); log("Got total players list");
            Map<String, PlayerStats> stats = statsFetcher.getPlayerStatsMap();
            setProgress(70); log("Got player stats map");
            String notificationText = notifFetcher.getNotificationText();
            setProgress(99); log("Got notification text");
            FileManager fileManager = new FileManager(getApplicationContext());
            fileManager.storeTotalPlayers(totalPlayers);
            fileManager.storePlayerStats(stats);
            fileManager.storeNotificationText(notificationText);
            log("Stored data in files");
            BackgroundManager backgroundManager = new BackgroundManager(getApplicationContext());
            backgroundManager.scheduleNotification();
            setProgress(100); log("Scheduled a notification");
            setIsRetrying(false);
            log("FetchDataWorker done");
            return Result.success();
        } catch (UnknownHostException | SocketException | SocketTimeoutException e) {
            e.printStackTrace();
            setIsRetrying(true);
            return Result.retry();
        } catch (Exception e) {
            e.printStackTrace();
            setIsRetrying(false);
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
        log("Got men's rankings document");
        wRankings = Jsoup.connect
                ("https://www.espn.com/tennis/rankings/_/type/wta").get();
        log("Got women's rankings document");
        tSchedule = Jsoup.connect
                ("http://www.espn.com/tennis/dailyResults").get();
        log("Got today's schedule document");
        ySchedule = Jsoup.connect
                ("http://www.espn.com/tennis/dailyResults?date=" +
                        dateOfYesterday).get();
        log("Got yesterday's schedule document");
    }

    /*
       Sets the observable progress to the given progress percentage
     */
    private void setProgress(int progressPercentage) {
        Data progress = new Data.Builder()
                .putInt(PROGRESS_KEY, progressPercentage)
                .build();
        setProgressAsync(progress);
    }

    /*
       Writes the given message to the logcat
     */
    private void log(String msg) {
        Log.i(getApplicationContext().getString(R.string.fetching_data_log), msg);
    }

    /*
       Saves the given value in shared preferences to indicate whether the
       worker is retrying
     */
    private void setIsRetrying(boolean value) {
        Context context = getApplicationContext();
        SharedPreferences prefs = context.getSharedPreferences(
                context.getString(R.string.shared_prefs_filename), Context.MODE_PRIVATE);
        prefs.edit().putBoolean(
                context.getString(R.string.is_worker_retrying_key), value).apply();
    }

    private void saveTime() {
        SharedPreferences sharedPrefs = getApplicationContext()
                .getSharedPreferences("Time file", Context.MODE_PRIVATE);
        Calendar calendar = Calendar.getInstance();
        sharedPrefs.edit().putInt("Hour", calendar.get(Calendar.HOUR_OF_DAY)).apply();
        sharedPrefs.edit().putInt("Minute", calendar.get(Calendar.MINUTE)).apply();
    }

}
