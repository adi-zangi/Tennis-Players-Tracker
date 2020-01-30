/*
   Used to handle errors
   Handles reading and writing to files and ensures that the daily data
   lookups are on schedule
   Responds to errors by displaying an error screen
 */

package com.adizangi.myplayers;

import android.content.Context;

public class ErrorHandler {

    public static final String ALL_PLAYERS_FILE_NAME = "all_players.txt";
    public static final String PLAYER_DATA_FILE_NAME = "player_data.txt";
    public static final String NOTIF_CONTENT_FILE_NAME =
            "notification_content.txt";

    /*
       Constructs an ErrorHandler
       The given Context is used to access system services
     */
    public ErrorHandler(Context context) {

    }

    /*
       Writes the given object to the file with the given name
	   If there was an error, puts the app in error mode
     */
    public void writeToFile(String fileName, Object object) {

    }

    /*
       Reads the object from the file with the given name and returns it
	   If there was an error, puts the app in error mode
     */
    public Object readFromFile(String fileName) {
        return null;
    }

    /*
       Checks how much time passed since the last successful data update task
	   If more than 24 hours passed since the last update, puts the app in error mode
	   Otherwise, does nothing
     */
    public void ensureWorkerOnSchedule() {

    }

    /*
       Switches the layout in the main activity to the error screen layout, which
	   contains a message that notifies the user there was an error
	   The user cannot click on anything while the error screen is showing
	   The screen is removed when the error is resolved, which may either be
	   the next time a data task is successful or the next time the app opens
	   and no errors happen
     */
    public void startErrorMode() {

    }

}
