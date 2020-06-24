/*
   Manages reading and writing to files
 */

package com.adizangi.tennisplayerstracker.utils_data;

import android.content.Context;
import android.content.ContextWrapper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileManager extends ContextWrapper {

    private static final String SELECTED_PLAYERS_FILENAME = "selected_players";
    private static final String TOTAL_PLAYERS_FILENAME = "total_players";
    private static final String STATS_FILENAME = "player_stats";
    private static final String NOTIF_FILENAME = "notification_list";

    /*
       Constructs a FileManager with the given application context
     */
    public FileManager(Context base) {
        super(base);
    }

    /*
       Reads the list of the user's players from the file and returns it
       Returns an empty list if there is an error
     */
    @SuppressWarnings("unchecked")
    public List<String> readSelectedPlayers() {
        try {
            FileInputStream inputStream = openFileInput(SELECTED_PLAYERS_FILENAME);
            ObjectInputStream objectInputStream =
                    new ObjectInputStream(inputStream);
            List<String> selectedPlayers =
                    (List<String>) objectInputStream.readObject();
            objectInputStream.close();
            inputStream.close();
            return selectedPlayers;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /*
       Reads the total players list from the file and returns it
       Returns an empty list if there is an error
     */
    @SuppressWarnings("unchecked")
    public List<String> readTotalPlayers() {
        try {
            FileInputStream inputStream =
                    openFileInput(TOTAL_PLAYERS_FILENAME);
            ObjectInputStream objectInputStream =
                    new ObjectInputStream(inputStream);
            List<String> totalPlayers =
                    (List<String>) objectInputStream.readObject();
            objectInputStream.close();
            inputStream.close();
            return totalPlayers;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /*
       Reads the player stats map from the file and returns it
       Returns an empty map if there is an error
     */
    @SuppressWarnings("unchecked")
    public Map<String, PlayerStats> readPlayerStats() {
        try {
            FileInputStream inputStream = openFileInput(STATS_FILENAME);
            ObjectInputStream objectInputStream =
                    new ObjectInputStream(inputStream);
            Map<String, PlayerStats> stats =
                    (Map<String, PlayerStats>) objectInputStream.readObject();
            objectInputStream.close();
            inputStream.close();
            return stats;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    /*
       Reads the notification list from the file and returns it
       Returns an empty list if there is an error
     */
    @SuppressWarnings("unchecked")
    public List<String> readNotificationList() {
        try {
            FileInputStream inputStream = openFileInput(NOTIF_FILENAME);
            ObjectInputStream objectInputStream =
                    new ObjectInputStream(inputStream);
            List<String> notifList =
                    (List<String>) objectInputStream.readObject();
            objectInputStream.close();
            inputStream.close();
            return notifList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /*
       Stores the given list of the user's players in a file
     */
    public void storeSelectedPlayers(List<String> selectedPlayers) {
        try {
            FileOutputStream outputStream = openFileOutput
                    (SELECTED_PLAYERS_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(selectedPlayers);
            objectOutputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
       Stores the given total players list in a file
     */
    public void storeTotalPlayers(List<String> totalPlayers) {
        try {
            FileOutputStream outputStream = openFileOutput
                    (TOTAL_PLAYERS_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(totalPlayers);
            objectOutputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
       Stores the given player stats map in a file
     */
    public void storePlayerStats(Map<String, PlayerStats> playerStats) {
        try {
            FileOutputStream outputStream = openFileOutput
                    (STATS_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(playerStats);
            objectOutputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
       Stores the given notification list in a file
     */
    public void storeNotificationList(List<String> notifList) {
        try {
            FileOutputStream outputStream = openFileOutput
                    (NOTIF_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(notifList);
            objectOutputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
