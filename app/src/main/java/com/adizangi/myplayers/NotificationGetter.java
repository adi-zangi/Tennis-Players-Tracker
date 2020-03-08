/*
   Gets information for the daily notification that will be sent to the user
   Information is taken from the ESPN website
 */

package com.adizangi.myplayers;

import org.jsoup.nodes.Document;

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
        return null;
    }

}
