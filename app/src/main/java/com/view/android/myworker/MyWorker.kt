package com.view.android.myworker

import android.content.Context
import android.os.Looper
import android.util.Log
import android.util.TimeUtils
import android.widget.Toast
import androidx.work.*
import java.util.concurrent.TimeUnit

/**
 * Created by js on 21/1/19.
 */

class MyWorker(context : Context, params : WorkerParameters) : Worker(context, params){


    override fun doWork(): Result {


//        if (Result.SUCCESS.equals("SUCCESS")){
//            Log.e("DOWORK Success found","")

            val repeatReq = PeriodicWorkRequestBuilder<MyWorker>(10,TimeUnit.SECONDS).build()
            WorkManager.getInstance().enqueueUniquePeriodicWork("REPEAT WM called",ExistingPeriodicWorkPolicy.REPLACE,repeatReq)
//        }




//        Log.e("DOWORK called result",""+Result.SUCCESS)
        return Result.SUCCESS
    }

}