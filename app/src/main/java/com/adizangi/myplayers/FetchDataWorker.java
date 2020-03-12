package com.adizangi.myplayers;

import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class FetchDataWorker extends Worker {

    public FetchDataWorker(@NonNull Context context,
                           @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        TimeZone timeZone = TimeZone.getTimeZone("US/Eastern");
        Calendar calendar = Calendar.getInstance(timeZone, Locale.US);
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyyMMdd", Locale.US);
        String dateOfYesterday = dateFormat.format(calendar.getTime());
        Document mRankings = Jsoup.connect("https://www.espn.com/tennis/rankings").get();
        Document wRankings = Jsoup.connect("https://www.espn.com/tennis/rankings/_/type/wta").get();
        Document tSchedule = Jsoup.connect("http://www.espn.com/tennis/dailyResults").get();
        Document ySchedule = Jsoup.connect("http://www.espn.com/tennis/dailyResults?date=" +
                dateOfYesterday).get();
        return null;
    }
}
