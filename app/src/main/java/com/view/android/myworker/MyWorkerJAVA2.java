package com.view.android.myworker;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.widget.Toast;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


        import android.content.Context;
        import android.os.Handler;
        import android.os.Looper;
        import android.support.annotation.NonNull;
        import android.widget.Toast;

        import androidx.work.ExistingPeriodicWorkPolicy;
        import androidx.work.ListenableWorker;
        import androidx.work.PeriodicWorkRequest;
        import androidx.work.WorkManager;
        import androidx.work.Worker;
        import androidx.work.WorkerParameters;

        import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by js on 22/1/19.
 */

public class MyWorkerJAVA2 extends Worker {

    public MyWorkerJAVA2(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {

                Toast.makeText(getApplicationContext(), "Worker 2  called ", Toast.LENGTH_LONG).show();
//                Intent mainint = new Intent(getApplicationContext(),MainActivityJAVA.class);
//                getApplicationContext().startActivity(mainint);
//                Toast.makeText(getApplicationContext(), "Worker 2 activity called ", Toast.LENGTH_LONG).show();

            }
        });
//
//
//        Handler handler1 = new Handler(Looper.getMainLooper());
//        handler1.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //Do something after 100ms
//
//            }
//        }, 5000);
        return Result.SUCCESS;
    }
}
