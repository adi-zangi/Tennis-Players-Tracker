/*
   Test for NotificationGetter
   Prints the notification list so it can be compared to the ESPN website
 */

package com.adizangi.myplayers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

class TestNotificationGetter {

    public static void main(String[] args) {
        System.out.println("------------ Notification Getter ------------");
        System.out.println();
        try {
            long startTime = System.nanoTime();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            SimpleDateFormat dateFormat =
                    new SimpleDateFormat("yyyyMMdd", Locale.US);
            String dateOfYesterday = dateFormat.format(calendar.getTime());
            Document tSchedule = Jsoup.connect
                    ("http://www.espn.com/tennis/dailyResults").get();
            Document ySchedule = Jsoup.connect(
                    "http://www.espn.com/tennis/dailyResults?date=" +
                            dateOfYesterday).get();
            long estimatedTime = System.nanoTime() - startTime;
            System.out.println("Time to get documents: " +
                    (estimatedTime / 1000000000.0) + " seconds");
            System.out.println();
            startTime = System.nanoTime();
            NotificationGetter notificationGetter =
                    new NotificationGetter(tSchedule, ySchedule);
            List<String> list = notificationGetter.getNotificationList();
            estimatedTime = System.nanoTime() - startTime;
            System.out.println("Success");
            System.out.println();
            System.out.println("Time to get list: " +
                    (estimatedTime / 1000000000.0) + " seconds");
            System.out.println("List size: " + list.size());
            System.out.println();
            System.out.println("List:");
            for (String item : list) {
                System.out.println(item);
            }
        } catch (Exception e) {
            System.out.println("Failed");
            e.printStackTrace();
        }
    }

}
