package com.view.android.myworker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit
import androidx.work.PeriodicWorkRequest



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Define the constraint for custom workmanager
        val constraint = Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiresBatteryNotLow(true)
                .build()
        val data = Data.Builder()
                .putString("PAssWord", "123Test")
                .build()
        //Initlize the OneTImeRequest
        val myWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
                .setConstraints(constraint)
                .setInputData(data)
                .addTag("WorkRequest1")
                .build()

        // TO set periodic request
        val myPeriodicReq = PeriodicWorkRequestBuilder<MyWorker>(1,TimeUnit.SECONDS).build()
       // val myWorkBuilder = PeriodicWorkRequest.Builder(MyWorker::class.java, 1, TimeUnit.SECONDS)

        //Running the First Work ONeTimerequest
           //WorkManager.getInstance().enqueue(myWorkRequest)

        // Running First Periodic Request
        val repeatReq = PeriodicWorkRequestBuilder<MyWorker>(10,TimeUnit.SECONDS).build()
        WorkManager.getInstance().enqueueUniquePeriodicWork("REPEAT WM called",ExistingPeriodicWorkPolicy.REPLACE,repeatReq)
//        }
    }
}

