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
        String notificationText = reportForYesterday + "\n" + reportForToday;
        notificationList.add(notificationText);
        notificationList.addAll(getTournaments(tSchedule));
        return notificationList;
    }

    private String getReportForYesterday(Document ySchedule) throws IOException {
        String reportForYesterday = "";
        Elements tournaments = ySchedule.select("div.scoreHeadline");
        for (Element tournament : tournaments) {
            Element tournamentLink = tournament.selectFirst("a");
            String tournamentURL = tournamentLink.attr("abs:href");
            Document tournamentDocument = Jsoup.connect(tournamentURL).get();
            String tournamentRound = tournamentDocument
                    .selectFirst("div.matchCourt").text();
            if (tournamentRound.contains("Final")) {
                Elements matchTables = tournamentDocument.select("table");
                for (Element table : matchTables) {
                    Elements rows = table.select("td");
                }
            }
        }



        /*
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

         */
        return null;
    }

    private String getReportForToday(Document tSchedule) {
        return null;
    }

    private List<String> getTournaments(Document tSchedule) {
        return null;
    }

}
