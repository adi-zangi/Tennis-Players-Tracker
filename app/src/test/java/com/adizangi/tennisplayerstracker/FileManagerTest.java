/*
   Test for FileManager class
   Tests storing and reading files with the FileManager class
   Runs with Robolectric to use app resources without running the app
 */

package com.adizangi.tennisplayerstracker;

import android.content.Context;

import com.adizangi.tennisplayerstracker.utils_data.FileManager;
import com.adizangi.tennisplayerstracker.utils_data.PlayerStats;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class FileManagerTest {

    private FileManager fileManager;
    private List<String> myPlayers;
    private List<String> totalPlayers;
    private Map<String, PlayerStats> stats;
    private String notificationText;

    @Before
    public void setup() {
        myPlayers = new ArrayList<>(Arrays.asList("player1", "player2"));
        totalPlayers = new ArrayList<>(Arrays.asList("player1", "player2", "player3"));
        PlayerStats playerStats1 =
                new PlayerStats(
                        "playerName1",
                        "1",
                        "1",
                        "advanced to 2nd round",
                        "French Open",
                        "won 6-3, 6-3",
                        "3pm ET");
        PlayerStats playerStats2 =
                new PlayerStats(
                        "playerName2",
                        "2",
                        "1",
                        "advanced to 2nd round",
                        "French Open",
                        "won 6-3, 6-3",
                        "3pm ET");
        stats = new HashMap<>();
        stats.put("player1", playerStats1);
        stats.put("player2", playerStats2);
        notificationText = "Notification Text";
    }

    @Test
    public void testFileManager() {
        Context context = ApplicationProvider.getApplicationContext();
        fileManager = new FileManager(context);
        storeFiles();
        readFiles();
    }

    private void storeFiles() {
        fileManager.storeSelectedPlayers(myPlayers);
        fileManager.storeTotalPlayers(totalPlayers);
        fileManager.storePlayerStats(stats);
        fileManager.storeNotificationText(notificationText);
    }

    private void readFiles() {
        assertEquals(myPlayers, fileManager.readSelectedPlayers());
        assertEquals(totalPlayers, fileManager.readTotalPlayers());
        assertEquals(stats.size(), fileManager.readPlayerStats().size());
        assertEquals(notificationText, fileManager.readNotificationText());
    }

}
