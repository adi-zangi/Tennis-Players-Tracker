package com.adizangi.myplayers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDetailsGetter {

    private Document mRankings;
    private Document wRankings;

    public PlayerDetailsGetter(Document mRankings, Document wRankings) {
        this.mRankings = mRankings;
        this.wRankings = wRankings;
    }

    public Map<String, PlayerDetails> getPlayerDetailsMap() {
        Map<String, PlayerDetails> playerDetails = new HashMap<>();
        Element mRankingsTable = mRankings.selectFirst("table");
        if (mRankingsTable == null) {
            return playerDetails;
        }
        Elements mRows = mRankingsTable.select("tr");
        Element wRankingsTable = wRankings.selectFirst("table");
        Elements wRows = wRankingsTable.select("tr");
        int mNumOfRows = mRows.size();
        int wNumOfRows = wRows.size();
        for (int rowIndex = 1; rowIndex < 101; rowIndex++) {
            if (rowIndex < mNumOfRows) {  // Check is needed due to bug in the website
                Elements mColumns = mRows.get(rowIndex).select("td");
                String playerName = mColumns.get(2).text();
                String playerRanking = mColumns.get(0).text();
                playerChoices.add(playerName + " (" + playerRanking + ")");
            }
            if (rowIndex < wNumOfRows) {
                Elements wColumns = wRows.get(rowIndex).select("td");
                String playerName = wColumns.get(2).text();
                String playerRanking = wColumns.get(0).text();
                playerChoices.add(playerName + " (" + playerRanking + ")");
            }
        }
        return playerChoices;
    }

}
