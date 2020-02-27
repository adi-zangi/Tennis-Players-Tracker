/*
   Gets information from the ESPN website about each player from the player
   choices
 */

package com.adizangi.myplayers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlayerDetailsGetter {

    private Document mRankings;
    private Document wRankings;

    /*
       Constructs a PlayerDetailsGetter with the given HTML documents of the
       men's tennis rankings and women's tennis rankings from ESPN
     */
    public PlayerDetailsGetter(Document mRankings, Document wRankings) {
        this.mRankings = mRankings;
        this.wRankings = wRankings;
    }

    /*
       Returns a map from player to a PlayerDetails object
       The keys are each player's name followed by ranking in parenthesis
       May throw IOException
     */
    public Map<String, PlayerDetails> getPlayerDetailsMap()
            throws IOException {
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
                String playerURL = playerNameLink.attr("abs:href");
                Document playerDocument = Jsoup.connect(playerURL).get();
                String playerKey = getPlayerKey(playerDocument);
                PlayerDetails playerDetails = getPlayerDetails(playerDocument);
                playerDetailsMap.put(playerKey, playerDetails);
            }
            if (rowIndex < wNumOfRows) {
                Elements wColumns = wRows.get(rowIndex).select("td");
                Element playerNameLink = wColumns.get(2).selectFirst("a");
                String playerURL = playerNameLink.attr("abs:href");
                Document playerDocument = Jsoup.connect(playerURL).get();
                String playerKey = getPlayerKey(playerDocument);
                PlayerDetails playerDetails = getPlayerDetails(playerDocument);
                playerDetailsMap.put(playerKey, playerDetails);
            }
        }
        return playerDetailsMap;
    }

    /*
       Returns the key for the player whose information is in the given
       document
     */
    private String getPlayerKey(Document playerDocument) {
        String playerName = getName(playerDocument);
        String playerRanking = getRanking(playerDocument);
        return playerName + " (" + playerRanking + ")";
    }

    /*
       Returns
     */
    private PlayerDetails getPlayerDetails(Document playerDocument) {
        String name = getName(playerDocument);
        String ranking = "Current ranking: " + getRanking(playerDocument);
        String titles = getTitles(playerDocument);
        String standing = getTournamentStanding(playerDocument);
        String currentTournament = "";
        String latestMatchResult = "";
        if (!standing.equals("not playing")) {
            currentTournament = getCurrentTournament(playerDocument);
            latestMatchResult = getLatestMatchResult(playerDocument);
        }
        String upcomingMatch = "";
        if (standing.contains("advanced")) {
            upcomingMatch = getUpcomingMatch(playerDocument);
        }
        String resultsURL = getResultsURL(playerDocument);
        return new PlayerDetails(
                name,
                ranking,
                titles,
                standing,
                currentTournament,
                latestMatchResult,
                upcomingMatch,
                resultsURL);
    }

    private String getName(Document playerDocument) {
        return playerDocument.selectFirst("h1").text();
    }

    private String getRanking(Document playerDocument) {
        Element detailsList = playerDocument.select("ul").get(1);
        Elements listItems = detailsList.select("li");
        String rankingFullText = listItems.get(0).text();
        return rankingFullText.substring(rankingFullText.indexOf("#") + 1);
    }

    private String getTitles(Document playerDocument) {
        Element playerStatsDiv = playerDocument.selectFirst("div.player-stats");
        if (playerStatsDiv.select("p").isEmpty()) { // check needed due to bug in website
            return "Singles titles: unknown";
        }
        String statsFullTitle = playerStatsDiv.selectFirst("p").text();
        String year = statsFullTitle.substring(0, statsFullTitle.indexOf(" "));
        Element statsTable = playerStatsDiv.selectFirst("table");
        String singlesTitles = statsTable.select("tr").get(1)
                .selectFirst("td").text();
        return year + " singles titles: " + singlesTitles;
    }

    private String getTournamentStanding(Document playerDocument) {
        Element latestTournamentDiv =
                playerDocument.selectFirst("#my-players-table");
        String latestTournamentTitle = latestTournamentDiv.selectFirst("h4")
                .text();
        if (!latestTournamentTitle.equals("CURRENT TOURNAMENT")) {
            return "not playing";
        }
        Element latestTournamentTable =
                latestTournamentDiv.select("table").get(1);
        Elements rows = latestTournamentTable.select("tr");
        String tournamentType = rows.get(1).text();
        if (!tournamentType.contains("Singles")) {
            return "not playing";
        }
        int numOfRows = rows.size();
        int row = 2;
        while (row < numOfRows) {
            Elements columns = rows.get(row).select("td");
            if (columns.size() < 4) {
                break;
            }
            row++;
        }
        Element lastSinglesRow = rows.get(row - 1);
        Elements columns = lastSinglesRow.select("td");
        String matchResult = columns.get(2).text();
        if (matchResult.equals("-")) {
            String roundNumber = columns.get(0).text();
            return "advanced to " + roundNumber;
        } else if (!matchResult.equals("W")) {
            return "out";
        }
        return "winner";
    }

    private String getCurrentTournament(Document playerDocument) {
        // only call if playing is playing (our or in)- makes error otherwise
        Element latestTournamentDiv =
                playerDocument.selectFirst("#my-players-table");
        return latestTournamentDiv.selectFirst("a").text();
    }

    private String getLatestMatchResult(Document playerDocument) {
        // only call if is playing (our or in)- makes error otherwise
        Element latestTournamentDiv =
                playerDocument.selectFirst("#my-players-table");
        Element latestTournamentTable =
                latestTournamentDiv.select("table").get(1);
        Elements rows = latestTournamentTable.select("tr");
        int numOfRows = rows.size();
        int row = 2;
        while (row < numOfRows) {
            Elements columns = rows.get(row).select("td");
            if (columns.size() < 4) {
                break;
            }
            row++;
        }
        Element latestResultRow = rows.get(row - 1);
        Elements columns = latestResultRow.select("td");
        String matchResult = columns.get(2).text();
        if (matchResult.equals("-")) {
            latestResultRow = rows.get(row - 2);
            columns = latestResultRow.select("td");
        }
        String round = columns.get(0).text();
        String opponent = columns.get(1).text();
        String score = columns.get(3).text();
        return round + "- " + opponent + " " + score;
    }

    private String getUpcomingMatch(Document playerDocument) {
        // only call if advanced
        String playerName = playerDocument.selectFirst("h1").text();
        Element latestTournamentDiv =
                playerDocument.selectFirst("#my-players-table");
        Element latestTournamentTable =
                latestTournamentDiv.select("table").get(1);
        Elements rows = latestTournamentTable.select("tr");
        int numOfRows = rows.size();
        int row = 2;
        while (row < numOfRows) {
            Elements columns = rows.get(row).select("td");
            String matchResult = columns.get(2).text();
            if (matchResult.equals("-")) {
                break;
            }
            row++;
        }
        Element upcomingMatchRow = rows.get(row);
        Elements columns = upcomingMatchRow.select("td");
        String upcomingMatchDetails = columns.get(3).text();
        int secondSpaceIndex = upcomingMatchDetails
                .indexOf(" ", upcomingMatchDetails.indexOf(" ") + 1);
        String upcomingMatchDate =
                upcomingMatchDetails.substring(secondSpaceIndex);
        // check if date is today
        String upcomingMatchTime =
                upcomingMatchDetails.substring(secondSpaceIndex + 1);
        return playerName + " is playing today at " + upcomingMatchTime;
    }

    private String getResultsURL(Document playerDocument) {
        return playerDocument.selectFirst("a:contains(Results)")
                .attr("abs:href");
    }

}
