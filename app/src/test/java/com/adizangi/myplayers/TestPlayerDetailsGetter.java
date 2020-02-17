package com.adizangi.myplayers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TestPlayerDetailsGetter {

    public static void main(String[] args) {
        try {
            Document mRankings = Jsoup.connect
                    ("https://www.espn.com/tennis/rankings").get();
            Document wRankings = Jsoup.connect
                    ("https://www.espn.com/tennis/rankings/_/type/wta").get();
            PlayerDetailsGetter detailsGetter = new PlayerDetailsGetter(mRankings, wRankings);
            detailsGetter.getPlayerDetailsMap();
        } catch (Exception e) {

        }
    }

}
