/*
   Test for PlayerChoicesGetter
   Prints the player choices list so it can be compared to the ESPN website
 */

package com.adizangi.myplayers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

class PlayerChoicesGetterTest {

    public static void main(String[] args) {
        System.out.println("------------ Player Choices Getter ------------");
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
            PlayerChoicesGetter playersGetter =
                    new PlayerChoicesGetter(mRankings, wRankings);
            List<String> playerChoices = playersGetter.getPlayerChoicesList();
            estimatedTime = System.nanoTime() - startTime;
            System.out.println("Success");
            System.out.println();
            System.out.println("Time to get list: " +
                    (estimatedTime / 1000000000.0) + " seconds");
            System.out.println("List size: " + playerChoices.size());
            System.out.println();
            System.out.println("List:");
            for (String player : playerChoices) {
                System.out.println(player);
            }
        } catch (Exception e) {
            System.out.println("Failed");
            e.printStackTrace();
        }
    }

}
