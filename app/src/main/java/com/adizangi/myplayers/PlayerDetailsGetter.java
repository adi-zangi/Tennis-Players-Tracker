package com.adizangi.myplayers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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

    public Map<String, PlayerDetails> getPlayerDetailsMap() throws IOException {
        Map<String, PlayerDetails> playerDetailsMap = new HashMap<>();
        Element mRankingsTable = mRankings.selectFirst("table");
        if (mRankingsTable == null) {
            return playerDetailsMap;
        }
        Elements mRows = mRankingsTable.select("tr");
        Element wRankingsTable = wRankings.selectFirst("table");
        Elements wRows = wRankingsTable.select("tr");
        int mNumOfRows = mRows.size();
        int wNumOfRows = wRows.size();
        for (int rowIndex = 1; rowIndex < 101; rowIndex++) {
            if (rowIndex < mNumOfRows) {  // Check is needed due to bug in the website
                Elements mColumns = mRows.get(rowIndex).select("td");
                Element playerNameLink = mColumns.get(2).selectFirst("a");
                String playerDetailsURL = playerNameLink.attr("abs:href");
                Document playerDetailsDocument = Jsoup.connect(playerDetailsURL).get();
                System.out.println(getName(playerDetailsDocument));
                System.out.println(getRanking(playerDetailsDocument));
                System.out.println(getTitles(playerDetailsDocument));

                /*
                getTournamentStanding();
                getCurrentTournament();
                getLatestMatchResult();
                getUpcomingMatch();
                getAllMatchResultsURL();

                String name = columns.get(2).text();
                String rankingDescription = "Current ranking: " + columns.get(0).text();
                String titlesDescription = getTitlesDescription(playerPage);
                String currentTournament = getCurrentTournament(playerPage);
                String tournamentStatus = getTournamentStatus(playerPage);
                String latestMatchResult = getLatestMatchResult(playerPage);
                String upcomingMatch = getUpcomingMatch(playerPage);
                String resultsPageURL = playerPage.selectFirst("a:contains(Results)")
                        .attr("abs:href");

                PlayerData playerData = new PlayerData.PlayerDataBuilder(name)
                        .setRanking(rankingDescription)
                        .setTitles(titlesDescription)
                        .setCurrentTournament(currentTournament)
                        .setTournamentStatus(tournamentStatus)
                        .setLatestMatchResult(latestMatchResult)
                        .setUpcomingMatch(upcomingMatch)
                        .setResultsPageURL(resultsPageURL)
                        .build();

                 */
            }
            if (rowIndex < wNumOfRows) {
                Elements wColumns = wRows.get(rowIndex).select("td");
                String playerName = wColumns.get(2).text();
                String playerRanking = wColumns.get(0).text();
                //playerChoices.add(playerName + " (" + playerRanking + ")");
            }
        }
        return playerDetailsMap;
    }

    private String getName(Document playerDetails) {
        return playerDetails.selectFirst("div.mod-content > h1").text();
    }

    private String getRanking(Document playerDetails) {
        Element detailsList = playerDetails.select("ul").get(1);
        Elements listItems = detailsList.select("li");
        String rankingFullText = listItems.get(0).text();
        String ranking = rankingFullText.substring(rankingFullText.indexOf("#") + 1);
        return "Current ranking: " + ranking;
    }

    private String getTitles(Document playerDetails) {
        Element playerStatsDiv = playerDetails.selectFirst("div.player-stats");
        String statsFullTitle = playerStatsDiv.selectFirst("p").text();
        String year = statsFullTitle.substring(0, statsFullTitle.indexOf(" "));
        Element statsTable = playerStatsDiv.selectFirst("table");
        String singlesTitles = statsTable.select("tr").get(1)
                .selectFirst("td").text();
        return year + " singles titles: " + singlesTitles;
    }


}
