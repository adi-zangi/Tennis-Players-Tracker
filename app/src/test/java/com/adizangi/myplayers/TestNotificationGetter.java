/*
   Test for NotificationGetter
   Prints the notification list so it can be compared to the ESPN website
 */

package com.adizangi.myplayers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TestNotificationGetter {

    public static void main(String[] args) {
        System.out.println("------------ Notification Getter ------------");
        System.out.println();
        try {
            long startTime = System.nanoTime();
            Document mRankings = Jsoup.connect
                    ("https://www.espn.com/tennis/rankings").get();
            Document wRankings = Jsoup.connect
                    ("https://www.espn.com/tennis/rankings/_/type/wta").get();
            long estimatedTime = System.nanoTime() - startTime;
            System.out.println("Time to get documents: " +
                    (estimatedTime / 1000000000.0) + " seconds");
            System.out.println();
            startTime = System.nanoTime();
            PlayerDetailsGetter detailsGetter =
                    new PlayerDetailsGetter(mRankings, wRankings);
            Map<String, PlayerDetails> map = detailsGetter.getPlayerDetailsMap();
            estimatedTime = System.nanoTime() - startTime;
            System.out.println("Success");
            System.out.println();
            System.out.println("Time to get map: " +
                    (estimatedTime / 1000000000.0) + " seconds");
            System.out.println("Size: " + map.size());
            System.out.println();
            System.out.println("Some items from map:");
            printMap(map);
        } catch (Exception e) {
            System.out.println("Failed");
            e.printStackTrace();
        }
    }

}
