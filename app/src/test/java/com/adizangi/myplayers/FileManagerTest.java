package com.adizangi.myplayers;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class FileManagerTest {

    private List<String> myPlayers;
    private List<String> totalPlayers;
    private Map<String, PlayerStats> stats;
    private List<String> notifList;

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
                        "3pm ET",
                        "URL");
        PlayerStats playerStats2 =
                new PlayerStats(
                        "playerName2",
                        "2",
                        "1",
                        "advanced to 2nd round",
                        "French Open",
                        "won 6-3, 6-3",
                        "3pm ET",
                        "URL");
        stats = new HashMap<>();
        stats.put("player1", playerStats1);
        stats.put("player2", playerStats2);
        notifList = new ArrayList<>(Arrays.asList("Notification Text", "Tournament1"));
    }

    @Test
    public void testFileManager() {
        Context context = ApplicationProvider.getApplicationContext();
        FileManager fileManager = new FileManager(context);
        fileManager.storeMyPlayers(myPlayers);
        fileManager.storeTotalPlayers(totalPlayers);
        fileManager.storePlayerStats(stats);
        fileManager.storeNotificationList(notifList);

        assertEquals(myPlayers, fileManager.readMyPlayers());
        assertEquals(totalPlayers, fileManager.readTotalPlayers());
        assertEquals(notifList, fileManager.readNotificationList());
        Map<String, PlayerStats> actual = fileManager.readPlayerStats();
        assertEquals(stats.keySet(), actual.keySet());
        for (String key : stats.keySet()) {
            PlayerStats expectedStats = stats.get(key);
            PlayerStats actualStats = actual.get(key);
            assertEquals(expectedStats.getName(), actualStats.getName());
            assertEquals(expectedStats.getRanking(), actualStats.getRanking());
            assertEquals(expectedStats.getTitles(), actualStats.getTitles());
            assertEquals(expectedStats.getTournamentStanding(),
                    actualStats.getTournamentStanding());
            assertEquals(expectedStats.getCurrentTournament(), actualStats.getCurrentTournament());
            assertEquals(expectedStats.getLatestMatchResult(), actualStats.getLatestMatchResult());
            assertEquals(expectedStats.getUpcomingMatch(), actualStats.getUpcomingMatch());
            assertEquals(expectedStats.getResultsURL(), actualStats.getResultsURL());
        }
    }

}
