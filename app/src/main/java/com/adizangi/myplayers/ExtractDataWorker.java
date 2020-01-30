/*
   Extracts data for the current day from the ESPN tennis website
   Saves the data
   Updates the main screen UI with the data
 */

package com.adizangi.myplayers;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ExtractDataWorker extends Worker {

    private Handler UIHandler;

    /*
       Constructs a FirstRunDataWorker with the given application context,
       worker parameters, and UI Handler
     */
    public ExtractDataWorker(@NonNull Context context,
                             @NonNull WorkerParameters workerParams,
                             Handler handler) {
        super(context, workerParams);
        UIHandler = handler;
        Log.i(getClass().getSimpleName(), "ExtractDataWorker created");
    }

    @NonNull
    @Override
    /*
       Extracts data from the ESPN tennis website and saves the data
       If successfully got the data, updates the main screen UI with the new
       data
       If getting the data failed, displays an error screen
       Returns a Result that indicates whether the work was successful
     */
    public Result doWork() {
        return null;
    }

}
