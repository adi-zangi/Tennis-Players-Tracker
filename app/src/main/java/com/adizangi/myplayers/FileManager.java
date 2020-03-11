/*
   Manages reading and saving files
   Files are saved in internal storage
   They are only accessible to this app and are persistent as long as the app
   is installed, but are removed when the app is uninstalled
 */

package com.adizangi.myplayers;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class FileManager {

    private static final String MY_PLAYERS_FILENAME = "my_players";
    private static final String TOTAL_PLAYERS_FILENAME = "total_players";
    private static final String STATS_FILENAME = "player_stats";
    private static final String NOTIF_FILENAME = "notification_list";

    private Context context;

    /*
       Constructs a FileManager with the given application context
     */
    FileManager(Context context) {
        this.context = context;
    }

    /*
       Reads the list of the user's players from the file and returns it
       Returns an empty list if there is an error
     */
    @SuppressWarnings("unchecked")
    List<String> readMyPlayers() {
        try {
            FileInputStream inputStream =
                    context.openFileInput(MY_PLAYERS_FILENAME);
            ObjectInputStream objectInputStream =
                    new ObjectInputStream(inputStream);
            List<String> myPlayers =
                    (List<String>) objectInputStream.readObject();
            objectInputStream.close();
            inputStream.close();
            return myPlayers;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /*
       Reads the total players list from the file and returns it
       Returns an empty list if there is an error
     */
    @SuppressWarnings("unchecked")
    List<String> readTotalPlayers() {
        try {
            FileInputStream inputStream =
                    context.openFileInput(TOTAL_PLAYERS_FILENAME);
            ObjectInputStream objectInputStream =
                    new ObjectInputStream(inputStream);
            List<String> totalPlayers =
                    (List<String>) objectInputStream.readObject();
            objectInputStream.close();
            inputStream.close();
            return totalPlayers;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /*
       Reads the player stats map from the file and returns it
       Returns an empty map if there is an error
     */
    @SuppressWarnings("unchecked")
    Map<String, PlayerStats> readPlayerStats() {
        try {
            FileInputStream inputStream =
                    context.openFileInput(STATS_FILENAME);
            ObjectInputStream objectInputStream =
                    new ObjectInputStream(inputStream);
            Map<String, PlayerStats> stats =
                    (Map<String, PlayerStats>) objectInputStream.readObject();
            objectInputStream.close();
            inputStream.close();
            return stats;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    /*
       Reads the notification list from the file and returns it
       Returns an empty list if there is an error
     */
    @SuppressWarnings("unchecked")
    List<String> readNotificationList() {
        try {
            FileInputStream inputStream =
                    context.openFileInput(NOTIF_FILENAME);
            ObjectInputStream objectInputStream =
                    new ObjectInputStream(inputStream);
            List<String> notifList =
                    (List<String>) objectInputStream.readObject();
            objectInputStream.close();
            inputStream.close();
            return notifList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /*
       Stores the given list of the user's players in a file
     */
    void storeMyPlayers(List<String> myPlayers) {
        try {
            FileOutputStream outputStream = context.openFileOutput
                    (MY_PLAYERS_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(myPlayers);
            objectOutputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
       Stores the given total players list in a file
     */
    void storeTotalPlayers(List<String> totalPlayers) {
        try {
            FileOutputStream outputStream = context.openFileOutput
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
    void storePlayerStats(Map<String, PlayerStats> playerStats) {
        try {
            FileOutputStream outputStream = context.openFileOutput
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
    void storeNotificationList(List<String> notifList) {
        try {
            FileOutputStream outputStream = context.openFileOutput
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
