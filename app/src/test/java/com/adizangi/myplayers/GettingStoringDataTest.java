package com.adizangi.myplayers;

import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GettingStoringDataTest {

    @Mock Context mockContext;
    @Mock FileOutputStream fileOutputStream;
    @Mock FileInputStream fileInputStream;

    private Document mRankings;
    private Document wRankings;
    private Document tSchedule;
    private Document ySchedule;
    private List<String> playerChoices;
    private Map<String, PlayerDetails> playerDetails;
    private List<String> notificationList;

    @Before
    public void setup(){
        try {
            MockitoAnnotations.initMocks(this);
            when(mockContext.openFileOutput(anyString(), anyInt())).thenReturn(fileOutputStream);
            when(mockContext.openFileInput(anyString())).thenReturn(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGettingStoringData() {
        try {
            System.out.println("-------------- Test for Getting and Storing Data --------------");
            System.out.println();
            long startTime = System.nanoTime();
            getHTMLDocuments();
            getPlayerChoices();
            //getPlayerDetails(); // remove
            getNotification();
            storeData();
            readData();
            long estimatedTime = System.nanoTime() - startTime;
            System.out.println("Total time (seconds): " + (estimatedTime / 1000000000.0));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private void getHTMLDocuments() throws IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
        String dateOfYesterday = dateFormat.format(calendar.getTime());
        mRankings = Jsoup.connect("https://www.espn.com/tennis/rankings").get();
        wRankings = Jsoup.connect("https://www.espn.com/tennis/rankings/_/type/wta").get();
        tSchedule = Jsoup.connect("http://www.espn.com/tennis/dailyResults").get();
        ySchedule = Jsoup.connect("http://www.espn.com/tennis/dailyResults?date=" +
                dateOfYesterday).get();
    }

    private void getPlayerChoices() {
        System.out.println("---------- Player Choices Getter ----------");
        System.out.println();
        PlayerChoicesGetter choicesGetter = new PlayerChoicesGetter(mRankings, wRankings);
        playerChoices = choicesGetter.getPlayerChoicesList();
        System.out.println("List (size = " + playerChoices.size() + "):");
        for (String player : playerChoices) {
            System.out.println(player);
        }
        System.out.println();
    }

    private void getPlayerDetails() throws IOException {
        System.out.println("---------- Player Details Getter ----------");
        System.out.println();
        PlayerDetailsGetter detailsGetter = new PlayerDetailsGetter(mRankings, wRankings);
        playerDetails = detailsGetter.getPlayerDetailsMap();
        System.out.println("Some items from map (size = " + playerDetails.size() + "):");
        int i = 0;
        for (String player : playerDetails.keySet()) {
            if (i == 4) {
                break;
            }
            System.out.println();
            System.out.println(player);
            PlayerDetails details = playerDetails.get(player);
            System.out.println("Name: " + details.getName());
            System.out.println("Ranking: " + details.getRanking());
            System.out.println("Titles: " + details.getTitles());
            System.out.println("Standing: " + details.getTournamentStanding());
            System.out.println("Tournament: " + details.getCurrentTournament());
            System.out.println("Result: " + details.getLatestMatchResult());
            System.out.println("Upcoming match: " + details.getUpcomingMatch());
            System.out.println("Results URL: " + details.getResultsURL());
            i++;
        }
        System.out.println();
    }

    private void getNotification() throws IOException {
        System.out.println("---------- Notification Getter ----------");
        System.out.println();
        NotificationGetter notificationGetter = new NotificationGetter(tSchedule, ySchedule);
        notificationList = notificationGetter.getNotificationList();
        System.out.println("List (size = " + notificationList.size() + "):");
        for (String item : notificationList) {
            System.out.println(item);
        }
        System.out.println();
    }

    private void storeData() {
        FileManager fileManager = new FileManager(mockContext);
        fileManager.storePlayerChoices(playerChoices);
        //fileManager.storePlayerDetails(playerDetails); // remove
        fileManager.storeNotificationList(notificationList);
    }

    private void readData() {
        FileManager fileManager = new FileManager(mockContext);
        List<String> playerChoicesFile = fileManager.readPlayerChoices();
        //Map<String, PlayerDetails> playerDetailsFile = fileManager.readPlayerDetails(); // remove
        List<String> notifListFile = fileManager.readNotificationList();
        assertEquals(playerChoices, playerChoicesFile);
        //assertEquals(playerDetails, playerDetailsFile); // remove
        assertEquals(notificationList, notifListFile);
    }

}
