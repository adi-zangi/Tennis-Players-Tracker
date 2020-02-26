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
                try {
                    System.out.println(getName(playerDetailsDocument));
                    System.out.println(getRanking(playerDetailsDocument));
                    System.out.println(getTitles(playerDetailsDocument));
                    String standing = getTournamentStanding(playerDetailsDocument);
                    System.out.println(standing);
                    if (!standing.equals("not playing")) {
                        System.out.println(getCurrentTournament(playerDetailsDocument));
                        System.out.println(getLatestMatchResult(playerDetailsDocument));
                    }
                    if (standing.contains("advanced")) {
                        System.out.println(getUpcomingMatch(playerDetailsDocument));
                    }
                    System.out.println(getAllMatchResultsURL(playerDetailsDocument));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (rowIndex < wNumOfRows) {
                Elements wColumns = wRows.get(rowIndex).select("td");
                Element playerNameLink = wColumns.get(2).selectFirst("a");
                String playerDetailsURL = playerNameLink.attr("abs:href");
                Document playerDetailsDocument = Jsoup.connect(playerDetailsURL).get();
                try {
                    System.out.println(getName(playerDetailsDocument));
                    System.out.println(getRanking(playerDetailsDocument));
                    System.out.println(getTitles(playerDetailsDocument));
                    String standing = getTournamentStanding(playerDetailsDocument);
                    System.out.println(standing);
                    if (!standing.equals("not playing")) {
                        System.out.println(getCurrentTournament(playerDetailsDocument));
                        System.out.println(getLatestMatchResult(playerDetailsDocument));
                    }
                    if (standing.contains("advanced")) {
                        System.out.println(getUpcomingMatch(playerDetailsDocument));
                    }
                    System.out.println(getAllMatchResultsURL(playerDetailsDocument));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return playerDetailsMap;
    }

    private String getName(Document playerDetails) {
        return playerDetails.selectFirst("h1").text();
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

    private String getTournamentStanding(Document playerDetails) {
        // doubles and team cup? more complicated because can lose and not be out
        // check if last check is relevant
        Element latestTournamentDiv = playerDetails.selectFirst("#my-players-table");
        String latestTournamentTitle = latestTournamentDiv.selectFirst("h4").text();
        if (!latestTournamentTitle.equals("CURRENT TOURNAMENT")) {
            return "not playing";
        }
        Element latestTournamentTable = latestTournamentDiv.select("table").get(1);
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
            String matchResult = columns.get(2).text();
            if (!matchResult.equals("W") && !matchResult.equals("-")) {
                return "out";
            }
            row++;
        }
        Element lastSinglesRow = rows.get(row - 1);
        Elements columns = lastSinglesRow.select("td");
        String matchResult = columns.get(2).text();
        if (matchResult.equals("-")) {
            String roundNumber = columns.get(0).text();
            return "advanced to " + roundNumber;
        }
        return "winner";
    }

    private String getCurrentTournament(Document playerDetails) {
        // only call if playing is playing (our or in)- makes error otherwise
        Element latestTournamentDiv = playerDetails.selectFirst("#my-players-table");
        return latestTournamentDiv.selectFirst("a").text();
    }

    private String getLatestMatchResult(Document playerDetails) {
        // only call if playing is playing (our or in)- makes error otherwise
        // might want to change format of match result
        Element latestTournamentDiv = playerDetails.selectFirst("#my-players-table");
        Element latestTournamentTable = latestTournamentDiv.select("table").get(1);
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
            matchResult = columns.get(2).text();
        }
        String round = columns.get(0).text();
        String opponent = columns.get(1).text();
        String score = columns.get(3).text();
        return round + "- " + opponent + " " + score;
    }

    private String getUpcomingMatch(Document playerDetails) {
        // only call if advanced
        String playerName = playerDetails.selectFirst("h1").text();
        Element latestTournamentDiv = playerDetails.selectFirst("#my-players-table");
        Element latestTournamentTable = latestTournamentDiv.select("table").get(1);
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
        String upcomingMatchTime = upcomingMatchDetails.substring(secondSpaceIndex + 1);
        return playerName + " is playing today at " + upcomingMatchTime;
    }

    private String getAllMatchResultsURL(Document playerDetails) {
        String resultsURL = playerDetails.selectFirst("a:contains(Results)")
                .attr("abs:href");
        return resultsURL;
    }

}
