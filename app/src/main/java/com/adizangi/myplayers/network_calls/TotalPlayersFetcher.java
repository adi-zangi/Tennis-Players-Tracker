/*
   Fetches a list of all the players the user can add
   The players in the list are based on professional tennis player rankings in
   the ESPN website
 */

package com.adizangi.myplayers.network_calls;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class TotalPlayersFetcher {

    private Document mRankings;
    private Document wRankings;

    /*
       Constructs a TotalPlayersFetcher with the given HTML documents of the
       men's tennis rankings and women's tennis rankings from ESPN
     */
    public TotalPlayersFetcher(Document mRankings, Document wRankings) {
        this.mRankings = mRankings;
        this.wRankings = wRankings;
    }

    /*
       Returns the total players list
       The list contains the top 100 men players and the top 100 women players
       Each player is represented by name followed by ranking in parenthesis
       The ESPN website doesn't have tennis rankings when a new year starts
       and there have not been any tennis tournaments in the new year
       In this case, returns an empty list
     */
    public List<String> getTotalPlayersList() {
        List<String> players = new ArrayList<>();
        Element mRankingsTable = mRankings.selectFirst("table");
        if (mRankingsTable == null) {
            return players;
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
                players.add(playerName + " (" + playerRanking + ")");
            }
            if (rowIndex < wNumOfRows) {
                Elements wColumns = wRows.get(rowIndex).select("td");
                String playerName = wColumns.get(2).text();
                String playerRanking = wColumns.get(0).text();
                players.add(playerName + " (" + playerRanking + ")");
            }
        }
        return players;
    }

}
