/*
   Test for PlayerStatsFetcher
   Prints the player details map so it can be compared to the ESPN website
 */

package com.adizangi.myplayers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Map;

class PlayerDetailsGetterTest {

    public static void main(String[] args) {
        System.out.println("------------ Player Details Getter ------------");
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
            PlayerStatsFetcher detailsGetter =
                    new PlayerStatsFetcher(mRankings, wRankings);
            Map<String, PlayerStats> map = detailsGetter.getPlayerDetailsMap();
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

    @SuppressWarnings("ConstantConditions")
    private static void printMap(Map<String, PlayerStats> map) {
        int i = 0;
        for (String player : map.keySet()) {
            if (i == 4) {
                break;
            }
            System.out.println();
            System.out.println(player);
            PlayerStats details = map.get(player);
            System.out.println("Name: " + details.getName());
            System.out.println("Ranking: " + details.getRanking());
            System.out.println("Titles: " + details.getTitles());
            System.out.println("Standing: " + details.getTournamentStanding());
            System.out.println("Tournament: " + details.getCurrentTournament());
            System.out.println("Result: " + details.getLatestMatchResult());
            System.out.println("Upcoming match: " + details.getUpcomingMatch());
            System.out.println("Results URL: " + details.getResultsURL());
            i++;
        }
    }

}
