/*
   Fetches information for today’s notification from the ESPN website
 */

package com.adizangi.tennisplayerstracker.network_calls;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class NotificationFetcher {

    private Document tSchedule;
    private Document ySchedule;

    /*
       Constructs a NotificationFetcher with the given HTML documents of
       today's match schedule and yesterday's match schedule from ESPN
     */
    public NotificationFetcher(Document tSchedule, Document ySchedule) {
        this.tSchedule = tSchedule;
        this.ySchedule = ySchedule;
    }

    /*
       Returns the text for today's notification
       If there is no news about tennis, returns an empty string
       May throw IOException
     */
    public String getNotificationText() throws IOException {
        String notification = yesterdayResults() + todaySchedule();
        return notification;
    }

    /*
       Returns a summary of yesterday's tournament outcomes
       For any tournament finals that were played yesterday, adds the name of
       player who won and the tournament's name to the summary
       If there were no finals yesterday, returns an empty string
       May throw IOException
     */
    private String yesterdayResults() throws IOException {
        if (ySchedule.select("h3.noMatch").size() > 0) {
            /* No matches yesterday- returns an empty string */
            return "";
        }
        StringBuilder reportForYesterday = new StringBuilder();
        Elements tournaments = ySchedule.select("div.scoreHeadline");
        /* Loops over yesterday's tournaments */
        for (Element tournament : tournaments) {
            String tournamentURL = tournament.selectFirst("a")
                    .attr("abs:href");
            Document tournamentDoc = Jsoup.connect(tournamentURL).get();
            String docTitle = tournamentDoc.title();
            String tournamentName = docTitle.substring
                    (0, docTitle.indexOf("Daily Match Schedule - ESPN") - 1);
            String tournamentRound = tournamentDoc
                    .selectFirst("div.matchCourt").text();
            if (tournamentRound.contains("Singles") &&
                    tournamentRound.contains("Final")) {
                /* Adds the winners of a tournament to the summary */
                Elements matchTables = tournamentDoc.select("table");
                int numOfTables = matchTables.size();
                /* Loop to get both men's tennis and women's tennis winners */
                for (int table = 0; table < numOfTables; table += 2) {
                    Elements rows = matchTables.get(table).select("tr");
                    Element firstRow = rows.get(1);
                    Element secondRow = rows.get(2);
                    Element scoreTable = matchTables.get(table + 1);
                    String winner;
                    String opponent;
                    String score;
                    if (firstRow.select("div.arrowWrapper").size() > 0) {
                        winner = firstRow.text();
                        opponent = secondRow.text();
                        score = scoreTable.select("tr").get(1).text();
                    } else {
                        winner = secondRow.text();
                        opponent = firstRow.text();
                        score = scoreTable.select("tr").get(2).text();
                    }
                    String report = winner + " won the " + tournamentName +
                            " against " + opponent + " " + score + "\n";
                    reportForYesterday.append(report);
                }
            }
        }
        if (reportForYesterday.length() > 0) {
            reportForYesterday.insert(0, "Yesterday-\n");
        }
        return reportForYesterday.toString();
    }

    /*
       Returns a summary of the tournaments that are played today and the
       current round of each tournament
       If there is a final today, adds the time when it is scheduled to begin
       If there are no tournaments today, returns an empty string
       May throw IOException
     */
    private String todaySchedule() throws IOException {
        if (tSchedule.select("h3.noMatch").size() > 0) {
            /* No matches yesterday- returns an empty string */
            return "";
        }
        StringBuilder dailyTournaments = new StringBuilder();
        StringBuilder dailyFinals = new StringBuilder();
        Elements tournaments = tSchedule.select("div.scoreHeadline");
        /* Loops over today's tournaments */
        for (Element tournament : tournaments) {
            String tournamentURL = tournament.selectFirst("a")
                    .attr("abs:href");
            Document tournamentDoc = Jsoup.connect(tournamentURL).get();
            String docTitle = tournamentDoc.title();
            String tournamentName = docTitle.substring
                    (0, docTitle.indexOf("Daily Match Schedule - ESPN") - 1);
            String tournamentRound = tournamentDoc
                    .selectFirst("div.matchCourt").text();
            if (tournamentDoc.text().contains("Singles")) {
                if (tournamentRound.contains("Final")) {
                    /* Adds the time when the final is scheduled to begin */
                    Element matchTable = tournamentDoc.selectFirst("table");
                    Elements rows = matchTable.select("tr");
                    String firstOpponent = rows.get(1).text();
                    String secondOpponent = rows.get(2).text();
                    String matchTitle = tournamentDoc
                            .selectFirst("div.matchTitle").text();
                    String time = matchTitle.substring(
                            matchTitle.indexOf(":") + 2,
                            matchTitle.lastIndexOf("-") - 1);
                    String report = tournamentName + " final- " + firstOpponent +
                            " vs. " + secondOpponent + " at " + time + "\n";
                    dailyFinals.append(report);
                } else {
                    /* Adds the tournament's name and the round */
                    String simpleRound = tournamentRound
                            .substring(0, tournamentRound.indexOf(":"));
                    String report = tournamentName + "- " + simpleRound + "\n";
                    dailyTournaments.append(report);
                }
            }
        }
        String reportForToday = dailyTournaments.toString() + dailyFinals.toString();
        if (!reportForToday.isEmpty()) {
            reportForToday = "Today-\n" + reportForToday;
        }
        return reportForToday;
    }

}
