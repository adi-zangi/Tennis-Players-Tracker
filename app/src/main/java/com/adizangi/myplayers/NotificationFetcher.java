/*
   Fetches information that will be put in the notification sent to the user
   Information is taken from the ESPN website
 */

package com.adizangi.myplayers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class NotificationFetcher {

    private Document tSchedule;
    private Document ySchedule;

    /*
       Constructs a NotificationFetcher with the given HTML documents of
       today's match schedule and yesterday's match schedule from ESPN
     */
    NotificationFetcher(Document tSchedule, Document ySchedule) {
        this.tSchedule = tSchedule;
        this.ySchedule = ySchedule;
    }

    /*
       Returns a list of strings that contain information for the notification
       The first item in the list is the text of the notification
       The rest of the items are the names of current tournaments, which will
       be used for the 'View Live Scores' button of the notification
       If there are no current tournaments, the text will be an empty string
       and the list will not have tournament names
       May throw IOException
     */
    List<String> getNotificationList() throws IOException {
        List<String> notificationList = new ArrayList<>();
        String reportForYesterday = getReportForYesterday();
        String reportForToday = getReportForToday();
        String notificationText = reportForYesterday + reportForToday;
        notificationList.add(notificationText);
        notificationList.addAll(getTournaments());
        return notificationList;
    }

    /*
       Returns a string that contains the names of players that won a
       tournament yesterday and the name of the tournament
       If there were no tournament finals yesterday, returns an empty string
       May throw IOException
     */
    private String getReportForYesterday() throws IOException {
        if (ySchedule.select("h3.noMatch").size() > 0) {
            return "";
        }
        StringBuilder reportForYesterday = new StringBuilder();
        Elements tournaments = ySchedule.select("div.scoreHeadline");
        for (Element tournament : tournaments) {
            Element tournamentLink = tournament.selectFirst("a");
            String tournamentURL = tournamentLink.attr("abs:href");
            Document tournamentDoc = Jsoup.connect(tournamentURL).get();
            String documentTitle = tournamentDoc.title();
            String tournamentName = documentTitle.substring
                    (0, documentTitle.indexOf("Daily Match Schedule - ESPN") - 1);
            String tournamentRound = tournamentDoc
                    .selectFirst("div.matchCourt").text();
            if (tournamentRound.contains("Singles") &&
                    tournamentRound.contains("Final")) {
                Elements matchTables = tournamentDoc.select("table");
                int numOfTables = matchTables.size();
                for (int table = 0; table < numOfTables; table += 2) {
                    Elements rows = matchTables.get(table).select("td");
                    Element firstRow = rows.get(1);
                    Element secondRow = rows.get(2);
                    String player;
                    if (firstRow.select("div.arrowWrapper").size() > 0) {
                        player = firstRow.text();
                    } else {
                        player = secondRow.text();
                    }
                    String report = player + " won " + tournamentName + "\n";
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
       Returns a string that contains the names of tournaments that are
       happening today, the current round for each tournament, and the time
       of any tournament finals that are happening today
       If there are no tournaments today, returns an empty string
       May throw IOException
     */
    private String getReportForToday() throws IOException {
        if (tSchedule.select("h3.noMatch").size() > 0) {
            return "";
        }
        StringBuilder dailyTournaments = new StringBuilder("Today-\n");
        StringBuilder dailyFinals = new StringBuilder();
        Elements tournaments = tSchedule.select("div.scoreHeadline");
        for (Element tournament : tournaments) {
            Element tournamentLink = tournament.selectFirst("a");
            String tournamentURL = tournamentLink.attr("abs:href");
            Document tournamentDocument = Jsoup.connect(tournamentURL).get();
            String documentTitle = tournamentDocument.title();
            String tournamentName = documentTitle.substring
                    (0, documentTitle.indexOf("Daily Match Schedule - ESPN") - 1);
            String tournamentRound = tournamentDocument
                    .selectFirst("div.matchCourt").text();
            if (tournamentRound.contains("Singles")) {
                if (tournamentRound.contains("Final")) {
                    Element matchTable = tournamentDocument.selectFirst("table");
                    Elements rows = matchTable.select("tr");
                    String firstOpponent = rows.get(1).text();
                    String secondOpponent = rows.get(2).text();
                    String matchTitle = tournamentDocument
                            .selectFirst("div.matchTitle").text();
                    String time = matchTitle.substring(
                            matchTitle.indexOf(":") + 2,
                            matchTitle.lastIndexOf("-") - 1);
                    String report = tournamentName + " final- " + firstOpponent +
                            " vs. " + secondOpponent + " at " + time + "\n";
                    dailyFinals.append(report);
                } else {
                    String report = tournamentName + "- " + tournamentRound + "\n";
                    dailyTournaments.append(report);
                }
            }
        }
        return dailyTournaments.toString() + dailyFinals.toString();
    }

    /*
       Returns a list of the tournaments that are happening today
       If there are no tournaments today, the list will be empty
     */
    private List<String> getTournaments() {
        List<String> list = new ArrayList<>();
        if (tSchedule.select("h3.noMatch").size() > 0) {
            return list;
        }
        Elements tournaments = tSchedule.select("div.scoreHeadline");
        for (Element tournament : tournaments) {
            String fullTitle = tournament.text();
            String tournamentName = fullTitle.substring(
                    fullTitle.indexOf(" ") + 1,
                    fullTitle.lastIndexOf(" "));
            list.add(tournamentName);
        }
        return list;
    }

}
