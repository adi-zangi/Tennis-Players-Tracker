/*
   Test for TotalPlayersFetcher, PlayerStatsFetcher, and NotificationFetcher
   Prints the data fetched by the classes so it can be compared to the ESPN website
   Prints the total time taken to fetch the data
 */

package com.adizangi.tennisplayerstracker;

import android.util.Log;

import com.adizangi.tennisplayerstracker.network_calls.NotificationFetcher;
import com.adizangi.tennisplayerstracker.utils_data.PlayerStats;
import com.adizangi.tennisplayerstracker.network_calls.PlayerStatsFetcher;
import com.adizangi.tennisplayerstracker.network_calls.TotalPlayersFetcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.fail;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class FetchDataTest {

    private Document mRankings;
    private Document wRankings;
    private Document tSchedule;
    private Document ySchedule;

    @Test
    public void testFetchingData() {
        PowerMockito.mockStatic(Log.class);
        try {
            System.out.println("---------------- Test for Fetching Data ----------------");
            System.out.println();
            long startTime = System.nanoTime();
            fetchHTMLDocuments();
            fetchTotalPlayers();
            fetchPlayerStats();
            fetchNotification();
            long estimatedTime = System.nanoTime() - startTime;
            System.out.println("Total time (seconds): " + (estimatedTime / 1000000000.0));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private void fetchHTMLDocuments() throws IOException {
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

    private void fetchTotalPlayers() {
        System.out.println("---------- Total Players Fetcher ----------");
        System.out.println();
        TotalPlayersFetcher fetcher = new TotalPlayersFetcher(mRankings, wRankings);
        List<String> totalPlayers = fetcher.getTotalPlayersList();
        System.out.println("List (size = " + totalPlayers.size() + "):");
        for (String player : totalPlayers) {
            System.out.println(player);
        }
        System.out.println();
    }

    @SuppressWarnings("ConstantConditions")
    private void fetchPlayerStats() throws IOException {
        System.out.println("---------- Player Stats Fetcher ----------");
        System.out.println();
        PlayerStatsFetcher fetcher = new PlayerStatsFetcher(mRankings, wRankings);
        Map<String, PlayerStats> statsMap = fetcher.getPlayerStatsMap();
        System.out.println("Some items from map (size = " + statsMap.size() + "):");
        int i = 0;
        for (String player : statsMap.keySet()) {
            if (i == 4) {
                break;
            }
            System.out.println();
            System.out.println(player);
            PlayerStats stats = statsMap.get(player);
            System.out.println("Name: " + stats.getName());
            System.out.println("Ranking: " + stats.getRanking());
            System.out.println("Titles: " + stats.getTitles());
            System.out.println("Standing: " + stats.getTournamentStanding());
            System.out.println("Tournament: " + stats.getCurrentTournament());
            System.out.println("Result: " + stats.getLatestMatchResult());
            System.out.println("Upcoming match: " + stats.getUpcomingMatch());
            i++;
        }
        System.out.println();
    }

    private void fetchNotification() throws IOException {
        System.out.println("---------- Notification Fetcher ----------");
        System.out.println();
        NotificationFetcher fetcher = new NotificationFetcher(tSchedule, ySchedule);
        String notificationText = fetcher.getNotificationText();
        System.out.println("Text:\n" + notificationText);
        System.out.println();
    }

}
