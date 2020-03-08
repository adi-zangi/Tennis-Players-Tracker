/*
   Manages reading and saving files in internal storage
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
    private static final String CHOICES_FILENAME = "player_choices";
    private static final String DETAILS_FILENAME = "player_details";
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
       Reads the list of the player choices from the file and returns it
       Returns an empty list if there is an error
     */
    @SuppressWarnings("unchecked")
    List<String> readPlayerChoices() {
        try {
            FileInputStream inputStream =
                    context.openFileInput(CHOICES_FILENAME);
            ObjectInputStream objectInputStream =
                    new ObjectInputStream(inputStream);
            List<String> playerChoices =
                    (List<String>) objectInputStream.readObject();
            objectInputStream.close();
            inputStream.close();
            return playerChoices;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /*
       Reads the player details map from the file and returns it
       Returns an empty map if there is an error
     */
    @SuppressWarnings("unchecked")
    Map<String, PlayerDetails> readPlayerDetails() {
        try {
            FileInputStream inputStream =
                    context.openFileInput(DETAILS_FILENAME);
            ObjectInputStream objectInputStream =
                    new ObjectInputStream(inputStream);
            Map<String, PlayerDetails> playerDetails =
                    (Map<String, PlayerDetails>) objectInputStream.readObject();
            objectInputStream.close();
            inputStream.close();
            return playerDetails;
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
       Stores the given player choices list in a file
     */
    void storePlayerChoices(List<String> playerChoices) {
        try {
            FileOutputStream outputStream = context.openFileOutput
                    (CHOICES_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(playerChoices);
            objectOutputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
       Stores the given player details map in a file
     */
    void storePlayerDetails(Map<String, PlayerDetails> playerDetails) {
        try {
            FileOutputStream outputStream = context.openFileOutput
                    (DETAILS_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(playerDetails);
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
