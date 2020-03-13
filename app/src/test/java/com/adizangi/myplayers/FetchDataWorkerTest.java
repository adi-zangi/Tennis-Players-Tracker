package com.adizangi.myplayers;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.test.core.app.ApplicationProvider;

@RunWith(AndroidJUnit4ClassRunner)
public class FetchDataWorkerTest {

    private Context context;
    private Executor executor;

    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
        executor = Executors.newSingleThreadExecutor();
    }

    @Test
    public void testFetchDataWorker() {
        FetchDataWorker fetchDataWorker;
    }

}
