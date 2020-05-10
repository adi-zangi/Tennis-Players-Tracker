/*
   Used for custom initialization of WorkManager
   Passes a Handler to every Worker in the constructor in addition to the
   default parameters
 */

package com.adizangi.myplayers.workers;

import android.content.Context;
import android.os.Handler;

import java.lang.reflect.Constructor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.ListenableWorker;
import androidx.work.WorkerFactory;
import androidx.work.WorkerParameters;

public class CustomWorkerFactory extends WorkerFactory {

    private Handler UIHandler;

    /*
       Constructs a CustomWorkerFactory with the given Handler on the UI thread
     */
    public CustomWorkerFactory(Handler handler) {
        UIHandler = handler;
    }

    @Nullable
    @Override
    /*
       Returns a new instance of the specified workerClassName with the given
       context, worker parameters, and Handler on the UI thread
       Returns null if a worker could not be created, which makes the default
       WorkerFactory be used instead
     */
    public ListenableWorker createWorker(@NonNull Context appContext,
                                         @NonNull String workerClassName,
                                         @NonNull WorkerParameters workerParameters) {
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