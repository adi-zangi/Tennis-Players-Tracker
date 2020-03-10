package com.adizangi.myplayers;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileManagerTest {

    private FileManager fileManager;
    private List<String> myPlayers;
    private List<String> playerChoices;
    private Map<String, PlayerStats> playerDetails;
    private List<String> notificationList;

    @Before
    public void setUp() {
        System.out.println("------------ File Manager ------------");
        System.out.println();
        myPlayers = new ArrayList<>(Arrays.asList("player1", "player2"));
        playerChoices = new ArrayList<>(Arrays.asList("player1", "player2", "player3"));
        playerDetails = new HashMap<>();
        playerDetails.put("player1", new PlayerStats("player1",
                "", "", "", "",
                "", "", ""));
        playerDetails.put("player2", new PlayerStats("player2",
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
