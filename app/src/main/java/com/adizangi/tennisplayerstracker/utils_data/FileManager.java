/*
   Manages reading and writing to files
   Files are saved in internal storage
   They are only accessible to this app and are persistent as long as the app
   is installed, but are removed when the app is uninstalled
 */

package com.adizangi.tennisplayerstracker.utils_data;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileManager extends ContextWrapper {

    private static final String MY_PLAYERS_FILENAME = "my_players";
    private static final String TOTAL_PLAYERS_FILENAME = "total_players";
    private static final String STATS_FILENAME = "player_stats";
    private static final String NOTIF_FILENAME = "notification_list";
    private static final String SHARED_PREFS_FILENAME = "com.adizangi.myplayers.APP_DATA";
    private static final String VERSION_CODE_KEY = "version_code";

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
    public List<String> readMyPlayers() {
        try {
            FileInputStream inputStream = openFileInput(MY_PLAYERS_FILENAME);
            ObjectInputStream objectInputStream =
                    new ObjectInputStream(inputStream);
            List<String> myPlayers =
                    (List<String>) objectInputStream.readObject();
            objectInputStream.close();
            inputStream.close();
            return myPlayers;
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
    public void storeMyPlayers(List<String> myPlayers) {
        try {
            FileOutputStream outputStream = openFileOutput
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

    /*
       Reads the version code from shared preferences and returns it
       Returns -1 if no version code was saved
     */
    public int readVersionCode() {
        SharedPreferences sharedPrefs = getSharedPreferences
                (SHARED_PREFS_FILENAME, Context.MODE_PRIVATE);
        return sharedPrefs.getInt(VERSION_CODE_KEY, -1);
    }

    /*
       Stores the given version code in shared preferences
     */
    public void storeVersionCode(int versionCode) {
        SharedPreferences sharedPrefs = getSharedPreferences
                (SHARED_PREFS_FILENAME, Context.MODE_PRIVATE);
        sharedPrefs.edit().putInt(VERSION_CODE_KEY, versionCode).apply();
    }

}
