/*
   Gets information that will be put in the daily notification sent to the user
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

public class NotificationGetter {

    private Document tSchedule;
    private Document ySchedule;

    /*
       Constructs a NotificationGetter with the given HTML documents of
       today's match schedule and yesterday's match schedule from ESPN
     */
    public NotificationGetter(Document tSchedule, Document ySchedule) {
        this.tSchedule = tSchedule;
        this.ySchedule = ySchedule;
    }

    public List<String> getNotificationList() throws IOException {
        List<String> notificationList = new ArrayList<>();
        String reportForYesterday = getReportForYesterday(ySchedule);
        String reportForToday = getReportForToday(tSchedule);
        String notificationText = reportForYesterday + reportForToday;
        notificationList.add(notificationText);
        notificationList.addAll(getTournaments(tSchedule));
        return notificationList;
    }

    /*
       May throw IOException
       is check for singles good?
       add check for matches- this will cause error
     */
    private String getReportForYesterday(Document ySchedule) throws IOException {
        StringBuilder report = new StringBuilder();
        Elements tournaments = ySchedule.select("div.scoreHeadline");
        for (Element tournament : tournaments) {
            Element tournamentLink = tournament.selectFirst("a");
            String tournamentURL = tournamentLink.attr("abs:href");
            Document tournamentDocument = Jsoup.connect(tournamentURL).get();
            String documentTitle = tournamentDocument.title();
            String tournamentName = documentTitle.substring
                    (0, documentTitle.indexOf("Daily Match Schedule - ESPN") - 1);
            String tournamentRound = tournamentDocument
                    .selectFirst("div.matchCourt").text();
            if (tournamentRound.contains("Singles") &&
                    tournamentRound.contains("Final")) {
                Elements matchTables = tournamentDocument.select("table");
                for (Element table : matchTables) {
                    Elements rows = table.select("td");
                    Element firstRow = rows.get(1);
                    Element secondRow = rows.get(2);
                    if (firstRow.select("div.arrowWrapper").size() > 0) {
                        String player = firstRow.text();
                        report.append(player);
                    } else {
                        String player = secondRow.text();
                        report.append(player);
                    }
                    report.append(" won ");
                    report.append(tournamentName);
                    report.append("\n");
                }
            }
        }
        if (report.length() > 0) {
            report.insert(0, "Yesterday-\n");
        }
        return report.toString();
    }

    /*
       May throw IOException
     */
    private String getReportForToday(Document tSchedule) throws IOException {
        StringBuilder dailyTournaments = new StringBuilder();
        StringBuilder dailyFinals = new StringBuilder();
        Elements tournaments = ySchedule.select("div.scoreHeadline");
        for (Element tournament : tournaments) {
            Element tournamentLink = tournament.selectFirst("a");
            String tournamentURL = tournamentLink.attr("abs:href");
            Document tournamentDocument = Jsoup.connect(tournamentURL).get();
            String documentTitle = tournamentDocument.title();
            String tournamentName = documentTitle.substring
                    (0, documentTitle.indexOf("Daily Match Schedule - ESPN") - 1);
            String tournamentRound = tournamentDocument
                    .selectFirst("div.matchCourt").text();
            if (tournamentRound.contains("Final")) {
                Element matchTable = tournamentDocument.selectFirst("table");
                Elements rows = matchTable.select("tr");
                String firstOpponent = rows.get(1).text();
                String secondOpponent = rows.get(2).text();

            } else {
                dailyTournaments.append(tournamentName);
                dailyTournaments.append("- ");
                dailyTournaments.append(tournamentRound);
                dailyTournaments.append("\n");
            }
        }
        return null;
    }

    private List<String> getTournaments(Document tSchedule) {
        return null;
    }

}
