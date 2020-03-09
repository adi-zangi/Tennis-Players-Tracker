package com.adizangi.myplayers;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class FileManagerTest {

    private FileManager fileManager;
    private List<String> myPlayers;
    private List<String> playerChoices;
    private Map<String, PlayerDetails> playerDetails;
    private List<String> notificationList;

    @Before
    public void setUp() {
        myPlayers = new ArrayList<>(Arrays.asList("player1", "player2"));
        playerChoices = new ArrayList<>(Arrays.asList("player1", "player2", "player3"));
        playerDetails = new HashMap<>();
        playerDetails.put("player1", new PlayerDetails("player1",
                "", "", "", "",
                "", "", ""));
        playerDetails.put("player2", new PlayerDetails("player2",
                "", "", "", "",
                "", "", ""));
        notificationList = new ArrayList<>(Arrays.asList("Notification text", "tournament1"));
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        fileManager = new FileManager(appContext);
    }

    @Test
    public void testStoreMyPlayers() {

    }

}
