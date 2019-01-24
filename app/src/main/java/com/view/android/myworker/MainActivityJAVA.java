package com.view.android.myworker;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by js on 22/1/19.
 */

public class MainActivityJAVA extends Activity {

    private static final int REQUEST_RECORD_PERMISSION = 100;
    String TAG = "MYJAVAWORKER";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions
                (MainActivityJAVA.this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_RECORD_PERMISSION);

        PeriodicWorkRequest.Builder pwr = new PeriodicWorkRequest.Builder(MyWorkerJAVA.class, 1,SECONDS);

        PeriodicWorkRequest request = pwr.build();
        WorkManager.getInstance().enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.REPLACE, request);

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //speech.startListening(recognizerIntent);
                    PeriodicWorkRequest.Builder pwr = new PeriodicWorkRequest.Builder(MyWorkerJAVA.class, 5, SECONDS);

                    PeriodicWorkRequest request = pwr.build();
                    WorkManager.getInstance().enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.KEEP, request);

                } else {
                    Toast.makeText(MainActivityJAVA.this, "Permission Denied!", Toast
                            .LENGTH_SHORT).show();
                }
        }
    }

}
