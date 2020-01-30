/*
   Extracts data for the app the first time the app opens
   Saves the data
   Updates the progress bar in the main screen
 */

package com.adizangi.myplayers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class FirstRunDataWorker extends Worker {

    private Context context;
    private Handler UIHandler;
    private List<String> allPlayers;
    private Map<String, PlayerData> playersData;
    private StringBuilder notificationContent;

    /*
       Constructs a FirstRunDataWorker with the given application context,
       worker parameters, and UI Handler
     */
    public FirstRunDataWorker(@NonNull Context context,
                              @NonNull WorkerParameters workerParams,
                              Handler handler) {
        super(context, workerParams);
        this.context = context;
        UIHandler = handler;
        Log.i(getClass().getSimpleName(), "FirstRunDataWorker created");
    }

    @NonNull
    @Override
    /*
       Extracts data from the ESPN tennis website and saves the data
       Updates the progress bar in the main screen while the data is being
       obtained
       If successfully got the data, switches the progress bar screen to the
       main screen and updates the main screen UI with the data
       Returns a Result that indicates whether the work was successful
     */
    public Result doWork() {
        try {
            setProgress(10);
            ExtractDataHelper dataHelper = new ExtractDataHelper();
            allPlayers = dataHelper.getAllPlayersList(); // Gets list of all players user can
                                                         // select,which are the current top
                                                         // 200 players in the world
            setProgress(30);
            playersData = dataHelper.getPlayerDataMap(); // Gets map from each player to PlayerData
            setProgress(70);
            notificationContent = dataHelper.getNotificationContent(); // Gets data to send in
                                                                       // daily notification
            setProgress(90);
            saveData();
            setProgress(100);
            updateScreen();

            int versionCode = BuildConfig.VERSION_CODE;
            SharedPreferences appValuesFile = context.getSharedPreferences
                    (MainActivity.APP_VALUES_FILE_KEY, Context.MODE_PRIVATE);
            appValuesFile.edit().putInt
                    (MainActivity.VERSION_CODE_KEY, versionCode).apply(); // Saves version code
                                                                          // App had first run
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }

    /*
       Sets the progress in the progress bar to the given percentage
     */
    private void setProgress(int percentage) {
        Message UIMessage = UIHandler.obtainMessage
                (MainActivity.PROGRESS_BAR_MSG_CODE, percentage);
        UIMessage.sendToTarget();
    }

    /*
       Saves the data that was obtained to files
     */
    private void saveData() {
        ErrorHandler errorHandler = new ErrorHandler(context);
        errorHandler.writeToFile
                (ErrorHandler.ALL_PLAYERS_FILE_NAME, allPlayers);
        errorHandler.writeToFile
                (ErrorHandler.PLAYER_DATA_FILE_NAME, playersData);
        errorHandler.writeToFile
                (ErrorHandler.NOTIF_CONTENT_FILE_NAME, notificationContent);
    }

    /*
       Switches the progress bar screen to the main screen and updates the
       main screen UI with the data
     */
    private void updateScreen() {
        Message UIMessage;
        UIMessage = UIHandler.obtainMessage
                (MainActivity.ANIMATION_MSG_CODE, R.layout.activity_main);
        UIMessage.sendToTarget(); // Switches progress bar screen to main screen
        UIMessage = UIHandler.obtainMessage
                (MainActivity.SPINNER_MSG_CODE, allPlayers);
        UIMessage.sendToTarget(); // Updates Spinner with list of all players
    }

}
