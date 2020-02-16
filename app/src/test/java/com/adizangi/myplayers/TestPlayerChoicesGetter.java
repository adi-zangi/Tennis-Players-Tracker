/*
   Test for PlayerChoicesGetter
   Prints the player choices list so it can be compared to the ESPN website
 */

package com.adizangi.myplayers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

public class TestPlayerChoicesGetter {

    public static void main(String[] args) {
        System.out.println("------------ Player Choices Getter ------------");
        Document mRankings = null;
        Document wRankings = null;
        try {
            long startTime = System.nanoTime();
            mRankings = Jsoup.connect
                    ("https://www.espn.com/tennis/rankings").get();
            wRankings = Jsoup.connect
                    ("https://www.espn.com/tennis/rankings/_/type/wta").get();
            long estimatedTime = System.nanoTime() - startTime;
            System.out.println("Time to get documents: " +
                    (estimatedTime / 1000000000.0) + " seconds");
        } catch (Exception e) {
            System.out.println("Failed- error when getting documents");
            e.printStackTrace();
        }
        try {
            long startTime = System.nanoTime();
            PlayerChoicesGetter playersGetter =
                    new PlayerChoicesGetter(mRankings, wRankings);
            List<String> playerChoices = playersGetter.getPlayerChoicesList();
            long estimatedTime = System.nanoTime() - startTime;
            System.out.println("Success");
            System.out.println("Time to get list: " +
                    (estimatedTime / 1000000000.0) + " seconds");
            System.out.println("List size: " + playerChoices.size());
            System.out.println("List:");
            for (String player : playerChoices) {
                System.out.println(player);
            }
        } catch (Exception e) {
            System.out.println("Failed- error in PlayerChoicesGetter class");
            e.printStackTrace();
        }
    }

}
