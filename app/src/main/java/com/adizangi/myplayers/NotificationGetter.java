/*
   Gets information that will be put in the daily notification sent to the user
   Information is taken from the ESPN website
 */

package com.adizangi.myplayers;

import org.jsoup.nodes.Document;

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

    public List<String> getNotificationList() {
        List<String> notificationList = new ArrayList<>();
        String reportForYesterday = getReportForYesterday(ySchedule);
        String reportForToday = getReportForToday(tSchedule);
        String notificationText = reportForYesterday + "\n" + reportForToday;
        notificationList.add(notificationText);
        notificationList.addAll(getTournaments(tSchedule));
        return notificationList;
    }

    private String getReportForYesterday(Document ySchedule) {
        return null;
    }

    private String getReportForToday(Document tSchedule) {
        return null;
    }

    private List<String> getTournaments(Document tSchedule) {
        return null;
    }

}
