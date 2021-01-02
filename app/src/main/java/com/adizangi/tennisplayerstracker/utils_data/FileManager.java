/*
   Manages reading and writing to files
 */

package com.adizangi.tennisplayerstracker.utils_data;

import android.content.Context;
import android.content.ContextWrapper;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileManager extends ContextWrapper {

    private static final String SELECTED_PLAYERS_FILENAME = "selected_players";
    private static final String TOTAL_PLAYERS_FILENAME = "total_players";
    private static final String STATS_FILENAME = "player_stats";
    private static final String NOTIFICATION_FILENAME = "notification_text";

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
            FileInputStream in = openFileInput(SELECTED_PLAYERS_FILENAME);
            ObjectInputStream oin = new ObjectInputStream(in);
            List<String> selectedPlayers = (List<String>) oin.readObject();
            oin.close();
            in.close();
            return selectedPlayers;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,
                    "There was a problem with getting saved players",
                    Toast.LENGTH_LONG).show();
            return new ArrayList<>();
        }
    }

    /*
       Reads the total players list from the file and returns it
       Returns null if there is an error
     */
    @SuppressWarnings("unchecked")
    public List<String> readTotalPlayers() {
        try {
            FileInputStream in = openFileInput(TOTAL_PLAYERS_FILENAME);
            ObjectInputStream oin = new ObjectInputStream(in);
            List<String> totalPlayers = (List<String>) oin.readObject();
            oin.close();
            in.close();
            return totalPlayers;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
       Reads the player stats map from the file and returns it
       Returns null if there is an error
     */
    @SuppressWarnings("unchecked")
    public Map<String, PlayerStats> readPlayerStats() {
        try {
            FileInputStream in = openFileInput(STATS_FILENAME);
            ObjectInputStream oin = new ObjectInputStream(in);
            Map<String, PlayerStats> stats =
                    (Map<String, PlayerStats>) oin.readObject();
            oin.close();
            in.close();
            return stats;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
       Reads the notification text from the file and returns it
       Returns an empty string if there is an error
     */
    public String readNotificationText() {
        try {
            FileInputStream in = openFileInput(NOTIFICATION_FILENAME);
            ObjectInputStream oin = new ObjectInputStream(in);
            String notifText = (String) oin.readObject();
            oin.close();
            in.close();
            return notifText;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /*
       Stores the given list of the user's players in a file
     */
    public void storeSelectedPlayers(List<String> selectedPlayers) {
        try {
            FileOutputStream out = openFileOutput(SELECTED_PLAYERS_FILENAME,
                    Context.MODE_PRIVATE);
            ObjectOutputStream oout = new ObjectOutputStream(out);
            oout.writeObject(selectedPlayers);
            oout.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,
                    "There was a problem with saving the new players",
                    Toast.LENGTH_LONG).show();
        }
    }

    /*
       Stores the given total players list in a file
     */
    public void storeTotalPlayers(List<String> totalPlayers) {
        try {
            FileOutputStream out = openFileOutput(TOTAL_PLAYERS_FILENAME,
                    Context.MODE_PRIVATE);
            ObjectOutputStream oout = new ObjectOutputStream(out);
            oout.writeObject(totalPlayers);
            oout.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
       Stores the given player stats map in a file
     */
    public void storePlayerStats(Map<String, PlayerStats> stats) {
        try {
            FileOutputStream out = openFileOutput(STATS_FILENAME,
                    Context.MODE_PRIVATE);
            ObjectOutputStream oout = new ObjectOutputStream(out);
            oout.writeObject(stats);
            oout.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
       Stores the given notification text in a file
     */
    public void storeNotificationText(String notifText) {
        try {
            FileOutputStream out = openFileOutput(NOTIFICATION_FILENAME,
                    Context.MODE_PRIVATE);
            ObjectOutputStream oout = new ObjectOutputStream(out);
            oout.writeObject(notifText);
            oout.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
