/*
   Goes through the ESPN tennis website and extracts information
 */

package com.adizangi.myplayers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExtractDataHelper {

    private Document mRankings;
    private Document wRankings;
    private Document scheduleForToday;
    private Document resultsFromYesterday;
    private boolean arePlayersUpdated;

    /*
       Constructs an ExtractDataHelper
       Fetches and parses HTML documents of the Men's Tennis Rankings,
       Women's Tennis Rankings, Daily Results, and Yesterday's Results
       from the ESPN tennis website
       Throws IOException if there was an error while getting an HTML page
     */
    public ExtractDataHelper() throws IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyyMMdd", Locale.US);
        String dateOfYesterday = dateFormat.format(calendar.getTime());
        mRankings = Jsoup.connect("https://www.espn.com/tennis/rankings").get();
        wRankings = Jsoup.connect
                ("https://www.espn.com/tennis/rankings/_/type/wta").get();
        scheduleForToday = Jsoup.connect
                ("http://www.espn.com/tennis/dailyResults").get();
        resultsFromYesterday =
                Jsoup.connect("http://www.espn.com/tennis/dailyResults?date=" +
                        dateOfYesterday).get();
        checkIfPlayersUpdated();
    }

    /*
       Returns a boolean indicating whether updated player rankings for today
       are available
       If this returns false, the methods updateAllPlayersList() and
       updatePlayerDataMap() can still be called but if there is already saved
       data, they will re-extract the data that is already saved
     */
    public boolean arePlayersUpdated() {
        return arePlayersUpdated;
    }

    /*
       Creates a list containing the current top 200 tennis players in the
       world, which include the top 100 men tennis players and top 100 women
       tennis players
       Each player is represented with the player's name followed by the
       player's ranking in parenthesis
       The list is sorted by ranking in descending order, and with each men's
       tennis player followed by the women's tennis player with the same ranking
       The list may have less than 200 players due to a bug in the web page
       Returns the list
     */
    public List<String> getAllPlayersList() {
        List<String> allPlayers = new ArrayList<>();
        Element mRankingsTable = mRankings.selectFirst("table");
        Elements mRows = mRankingsTable.select("tr");
        Element wRankingsTable = wRankings.selectFirst("table");
        Elements wRows = wRankingsTable.select("tr");
        int mNumOfRows = mRows.size();
        int wNumOfRows = wRows.size();
        for (int rowIndex = 1; rowIndex < 101; rowIndex++) {
            if (rowIndex < mNumOfRows) {
                Elements columns = mRows.get(rowIndex).select("td");
                String playerName = columns.get(2).text();
                String playerRanking = columns.get(0).text();
                allPlayers.add(playerName + " (" + playerRanking + ")");
            }
            if (rowIndex < wNumOfRows) {
                Elements columns = wRows.get(rowIndex).select("td");
                String playerName = columns.get(2).text();
                String playerRanking = columns.get(0).text();
                allPlayers.add(playerName + " (" + playerRanking + ")");
            }
        }
        return allPlayers;
    }

    /*
       Creates a hash map from each player to a PlayerData
       The keys in the map are same Strings that represent the players in the
       list returned by getAllPlayersList()
       The values are PlayerData objects associated with each player
       Returns the map
       Throws IOException if there was an error while getting an HTML page
     */
    public Map<String, PlayerData> getPlayerDataMap() throws IOException {
        Map<String, PlayerData> playerDataMap = new HashMap<>();
        Element mRankingsTable = mRankings.selectFirst("table");
        Elements mRows = mRankingsTable.select("tr");
        Element wRankingsTable = wRankings.selectFirst("table");
        Elements wRows = wRankingsTable.select("tr");
        int mNumOfRows = mRows.size();
        int wNumOfRows = wRows.size();
        for (int rowIndex = 1; rowIndex < 101; rowIndex++) {
            if (rowIndex < mNumOfRows) {
                Elements columns = mRows.get(rowIndex).select("td");
                String playerName = columns.get(2).text();
                String playerRanking = columns.get(0).text();
                String key = playerName + " (" + playerRanking + ")";
                PlayerData playerData = getPlayerData(columns);
                playerDataMap.put(key, playerData);
            }
            if (rowIndex < wNumOfRows) {
                Elements columns = mRows.get(rowIndex).select("td");
                String playerName = columns.get(2).text();
                String playerRanking = columns.get(0).text();
                String key = playerName + " (" + playerRanking + ")";
                PlayerData playerData = getPlayerData(columns);
                playerDataMap.put(key, playerData);
            }
        }
        return playerDataMap;
    }

    /*
       Creates a StringBuilder containing the content that will be displayed in
       a notification
       It contains information about current tennis tournaments and recent
       winners of tournaments
       If there are no current tournaments, the notification content will
       specify this
       Returns the StringBuilder
       Throws IOException if there was an error while getting an HTML page
     */
    public StringBuilder getNotificationContent() throws IOException {
        StringBuilder notificationContent = new StringBuilder();
        boolean areThereMatchesToday = scheduleForToday.select("h3.noMatch")
                .isEmpty();
        if (areThereMatchesToday) {
            List<String> dailyTournaments = getDailyTournaments();
            for (String tournament : dailyTournaments) {
                notificationContent.append(tournament);
                notificationContent.append("\n");
            }
        } else {
            notificationContent.append("Nothing is going on today\n");
        }
        boolean wereThereMatchesYesterday = resultsFromYesterday
                .select("h3.noMatch").isEmpty();
        if (wereThereMatchesYesterday) {
            List<String> recentWinners = getRecentWinners();
            for (String recentWinner : recentWinners) {
                notificationContent.append(recentWinner);
                notificationContent.append("\n");
            }
        }
        if (areThereMatchesToday) {
            List<String> dailyFinals = getDailyFinals();
            for (String dailyFinal : dailyFinals) {
                notificationContent.append(dailyFinal);
                notificationContent.append("\n");
            }
        }
        return notificationContent;
    }

    /*
       Checks if there are rankings in the current rankings web pages and
       resolves the case that there aren't
       It's possible for rankings to not exist when the year changes and
       rankings for the new year do not exist yet
       If there are no rankings, sets arePlayersUpdated to false and gets the
       last year's men's and women's rankings pages so data can be obtained
       Otherwise, sets arePlayersUpdated to true
       Throws IOException if there was an error while getting an HTML page
     */
    private void checkIfPlayersUpdated() throws IOException {
        Element mRankingsTable = mRankings.selectFirst("table");
        if (mRankingsTable == null) {
            arePlayersUpdated = false;
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -1);
            int lastYear = calendar.get(Calendar.YEAR);
            mRankings = Jsoup.connect
                    ("https://www.espn.com/tennis/rankings/_/season/" +
                            lastYear).get();
            wRankings = Jsoup.connect
                    ("https://www.espn.com/tennis/rankings/_/type/wta/season/"
                            + lastYear).get();
        } else {
            arePlayersUpdated = true;
        }
    }

    /*
       Uses the given columns of a single row of a rankings table to create a
       PlayerData object representing the player whose name and ranking are in
       the row
       Returns the PlayerData
       Throws IOException if there was an error while getting an HTML page
     */
    private PlayerData getPlayerData(Elements columns) throws IOException {
        Element playerNameLink = columns.get(2).selectFirst("a");
        String playerPageURL = playerNameLink.attr("href");
        Document playerPage = Jsoup.connect(playerPageURL).get(); // ESPN player page

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

        return playerData;
    }

    /*
       Uses the given HTML Document of a player's page to find the number of
       singles titles the player has won in the current year
       Returns a String in the form
       '[year] singles titles: [number of titles]'
     */
    private String getTitlesDescription(Document playerPage) {
        Element playerStatsDiv = playerPage.selectFirst("div.player-stats");
        String statsTitle = playerStatsDiv.selectFirst("div.content > p").text();
        String year = statsTitle.substring(0, statsTitle.indexOf(" "));
        Element statsTable = playerStatsDiv.selectFirst("table");
        String singlesTitles = statsTable.select("tr").get(1)
                .selectFirst("td").text();
        return year + " singles titles: " + singlesTitles;
    }

    /*
       Uses the given player page to find the name of the current tournament
       that the player is competing in (for singles tennis only)
       This includes any tournament that is currently happening, whether the
       player has advanced or has been eliminated from it
       Returns the name of the tournament, or an empty String if the player has
       not entered any current tournament
     */
    private String getCurrentTournament(Document playerPage) {
        Element latestTournamentTable = playerPage.select("table").get(1);
        String tableText = latestTournamentTable.text();
        if (tableText.contains("CURRENT TOURNAMENT") &&
                tableText.contains("Singles")) {
            Element tournamentLink = latestTournamentTable.selectFirst("a");
            String tournamentName = tournamentLink.text();
            return tournamentName;
        } else {
            return "";
        }
    }

    /*
       Uses the given player page to find the player's status in their
       current tournament
       Returns a String in either of the following forms
       'advanced to [round]'
       'out'
       'not playing'
     */
    private String getTournamentStatus(Document playerPage) {
        Element latestTournamentTable = playerPage.select("table").get(1);
        String tableText = latestTournamentTable.text();
        if (tableText.contains("CURRENT TOURNAMENT") &&
                tableText.contains("Singles")) {
            Elements rows = latestTournamentTable.select("tr");
            int numOfRows = rows.size();
            int row = 2;
            while (row < numOfRows) { // Goes over player's singles matches of current tournament
                Elements columns = rows.get(row).select("td");
                String matchResult = columns.get(2).text();
                if (matchResult.equals("L")) {
                    return "out";
                } else if (columns.text().contains("Doubles")) {
                    break;
                }
                row++;
            }
            Element lastSinglesRow = rows.get(row - 1);
            String roundNumber = lastSinglesRow.selectFirst("td").text();
            if (roundNumber.equals("Quarterfinals") ||
                    roundNumber.equals("Semifinals")) {
                return roundNumber;
            } else {
                return roundNumber + " round";
            }
        } else {
            return "not playing";
        }
    }

    /*
       Uses the given player page to find the result of the latest match the
       player has played
       Returns a String that contains the tournament round, the opponent, and
       the score of the match
       Returns an empty String if the player has not entered any current
       tournament
     */
    private String getLatestMatchResult(Document playerPage) {
        return null;
    }

    /*
       Uses the given player page to find the match that this player is
       scheduled to play today
       Returns a String containing the player's name and the time of the match
       Returns an empty String if the player does not have a match scheduled
       for today
     */
    private String getUpcomingMatch(Document playerPage) {
        return null;
    }

    /*
       Creates a list that contains descriptions of the tournaments that are
       being played today
       For each tournament, creates a String that contains the tournament's
       name and the round that is today
       The list only contains tournaments that are not in their final round
       Returns the list
       Throws IOException if there was an error while getting an HTML page
     */
    private List<String> getDailyTournaments() throws IOException {
        List<String> dailyTournaments = new ArrayList<>();
        Elements tournamentHeadlines =
                scheduleForToday.select("div.scoreHeadline");
        int numOfTournaments = tournamentHeadlines.size();
        for (int index = 0; index < numOfTournaments; index++) { // Goes over today's tournaments
            Element tournamentHeadline = tournamentHeadlines.get(index);
            Element tournamentLink = tournamentHeadline.selectFirst("a");
            String tournamentURL = tournamentLink.attr("abs:href");
            Document tournamentSchedule = Jsoup.connect(tournamentURL).get(); // Page with today's schedule
                                                                              // for the tournament
            Elements scheduledMatches =
                    tournamentSchedule.select("div.matchCourt");
            String firstMatchDescription = scheduledMatches.first().text();
            boolean isFinalRound = firstMatchDescription.contains("Finals");
            if (!isFinalRound) {
                dailyTournaments.add(
                        getDailyTournamentDescription(
                                tournamentSchedule, firstMatchDescription));
            }
        }
        return dailyTournaments;
    }

    /*
       Uses the given HTML Document of a tournament's schedule and the given
       description of a tennis match from the schedule to create a String in
       the form
       'The [round] of the [tournament name] is today'
       If there is no round, the String only includes the tournament name
       Returns the String
     */
    private String getDailyTournamentDescription(Document tournamentSchedule,
                                                 String matchDescription) {
        String scheduleTitle = tournamentSchedule.title();
        String tournamentName = scheduleTitle.substring
                (0, scheduleTitle.indexOf("Daily Match Schedule - ESPN") - 1);
        boolean isThereRound = matchDescription.contains(":");
        if (isThereRound) {
            String round = matchDescription.substring
                    (0, matchDescription.indexOf(":"));
            if (round.equals("Semifinals") || round.equals("Quarterfinals")) {
                return "The " + round + " of the " + tournamentName +
                        " are today";
            } else {
                return "The " + round + " of the " + tournamentName +
                        " is today";
            }
        } else {
            return tournamentName + " is today";
        }
    }

    /*
       Creates a list that contains descriptions of the finals matches that
       are being played today
       For each final, creates a String that contains the tournament's name,
       the time when the final starts, and the opponents
       Returns the list
       Throws IOException if there was an error while getting an HTML page
     */
    private List<String> getDailyFinals() throws IOException {
        List<String> dailyFinals = new ArrayList<>();
        Elements tournamentHeadlines =
                scheduleForToday.select("div.scoreHeadline");
        int numOfTournaments = tournamentHeadlines.size();
        for (int index = 0; index < numOfTournaments; index++) { // Goes over today's tournaments
            Element tournamentHeadline = tournamentHeadlines.get(index);
            Element tournamentLink = tournamentHeadline.selectFirst("a");
            String tournamentURL = tournamentLink.attr("abs:href");
            Document tournamentSchedule = Jsoup.connect(tournamentURL).get(); // Page with today's schedule
                                                                              // for the tournament
            Elements scheduledMatches =
                    tournamentSchedule.select("div.matchCourt");
            String firstMatchDescription = scheduledMatches.first().text();
            boolean isFinalRound = firstMatchDescription.contains("Finals");
            if (isFinalRound) {
                dailyFinals.add(
                        getDailyFinalDescription(tournamentSchedule, 0));
                int numOfMatches = scheduledMatches.size();
                for (int matchIndex = 1; matchIndex < numOfMatches; matchIndex++) { // Goes over next matches
                    dailyFinals.add(
                            getDailyFinalDescription(
                                    tournamentSchedule, matchIndex));
                }
            }
        }
        return dailyFinals;
    }

    /*
       Uses the given HTML Document of a tournament's schedule and the given
       index of a match from the schedule to create a String in the form
       'The final of the [tournament name] is today at [time] between
       [opponent1] and [opponent2]'
       Returns the String
     */
    private String getDailyFinalDescription(Document tournamentSchedule,
                                            int matchIndex) {
        String scheduleTitle = tournamentSchedule.title();
        String tournamentName = scheduleTitle.substring
                (0, scheduleTitle.indexOf("Daily Match Schedule - ESPN") - 1);
        String matchTitle = tournamentSchedule.select("div.matchTitle")
                .get(matchIndex).text();
        String timeOfFinal = matchTitle.substring(matchTitle.indexOf(":") + 2,
                matchTitle.lastIndexOf("-") - 1);
        Element opponentsTable = tournamentSchedule.select("div.matchInfo")
                .get(matchIndex).selectFirst("table");
        Elements rows = opponentsTable.select("tr");
        String opponent1 = rows.get(1).text();
        String opponent2 = rows.get(2).text();
        return "The final of the " + tournamentName + " is today at " +
                timeOfFinal + " between " + opponent1 + " and " + opponent2;
    }

    /*
       Creates a list that contains players that won a tournament yesterday
       For each tournament that a player won, adds a String that contains the
       tournament's name and the player who won it
       Returns the list
       Returns an empty list if there were no tournament finals yesterday
       Throws IOException if there was an error while getting an HTML page
     */
    private List<String> getRecentWinners() throws IOException {
        List<String> recentWinners = new ArrayList<>();
        Elements tournamentHeadlines =
                resultsFromYesterday.select("div.scoreHeadline");
        int numOfTournaments = tournamentHeadlines.size();
        for (int index = 0; index < numOfTournaments; index++) { // Goes over yesterday's tournaments
            Element tournamentHeadline = tournamentHeadlines.get(index);
            Element tournamentLink = tournamentHeadline.selectFirst("a");
            String tournamentURL = tournamentLink.attr("abs:href");
            Document tournamentResults = Jsoup.connect(tournamentURL).get(); // Page with yesterday's results
                                                                             // for the tournament
            Elements completedMatches =
                    tournamentResults.select("div.matchCourt");
            String firstMatchDescription = completedMatches.first().text();
            boolean wasFinal = firstMatchDescription.contains("Finals");
            if (wasFinal) {
                recentWinners.add(
                        getRecentWinnerDescription(tournamentResults, 0));
                int numOfMatches = completedMatches.size();
                for (int matchIndex = 1; matchIndex < numOfMatches; matchIndex++) { // Goes over other matches
                    recentWinners.add(
                            getRecentWinnerDescription(
                                    tournamentResults, matchIndex));
                }
            }
        }
        return recentWinners;
    }

    /*
       Uses the given HTML Document of a tournament's results and the given
       index of a match from the schedule to create a String in the form
       'Yesterday [winner] won the [tournament name]'
       Returns the String
     */
    private String getRecentWinnerDescription(Document tournamentSchedule,
                                              int matchIndex) {
        String scheduleTitle = tournamentSchedule.title();
        String tournamentName = scheduleTitle.substring
                (0, scheduleTitle.indexOf("Daily Match Schedule - ESPN") - 1);
        Element opponentsTable = tournamentSchedule.select("div.matchInfo")
                .get(matchIndex).selectFirst("table");
        Elements rows = opponentsTable.select("tr");
        String winnerName;
        if (!rows.get(1).select("div.arrowWrapper").isEmpty()) {
            winnerName = rows.get(1).text();
        } else {
            winnerName = rows.get(2).text();
        }
        return "Yesterday " + winnerName + " won the " + tournamentName;
    }

}
