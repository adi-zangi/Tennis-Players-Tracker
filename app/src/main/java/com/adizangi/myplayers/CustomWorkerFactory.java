/*
   Creates an instance of ListenableWorker every time a Worker runs
   Passes a Handler to every Worker in the constructor in addition to the
   default parameters
 */

package com.adizangi.myplayers;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.lang.reflect.Constructor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.ListenableWorker;
import androidx.work.WorkerFactory;
import androidx.work.WorkerParameters;

public class CustomWorkerFactory extends WorkerFactory {

    private Handler UIHandler;

    /*
       Constructs a CustomWorkerFactory with the given Handler
       The given Handler is connected to the UI thread
     */
    public CustomWorkerFactory(Handler handler) {
        Log.i(getClass().getSimpleName(), "Instance of CustomWorkerFactory created");
        UIHandler = handler;
    }

    @Nullable
    @Override
    /*
       Returns a new instance of the specified workerClassName with the given context and worker
       parameters, and passes it the Handler for the UI thread
       Returns null if a worker could not be created, which makes the default WorkerFactory be used
       instead
     */
    public ListenableWorker createWorker(@NonNull Context appContext,
                                         @NonNull String workerClassName,
                                         @NonNull WorkerParameters workerParameters) {
        Log.i(getClass().getSimpleName(), "createWorker method called");
        try {
            Class<? extends ListenableWorker> workerClass =
                    Class.forName(workerClassName).asSubclass(ListenableWorker.class);
            Constructor<? extends ListenableWorker> constructor =
                    workerClass.getDeclaredConstructor(Context.class, WorkerParameters.class,
                            Handler.class);
            return constructor.newInstance(appContext, workerParameters, UIHandler);
        } catch (Exception e) {
            return null;
        }
    }

}
