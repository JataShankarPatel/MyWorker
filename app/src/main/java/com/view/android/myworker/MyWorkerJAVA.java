package com.view.android.myworker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ListenableWorker;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by js on 22/1/19.
 */

public class MyWorkerJAVA extends Worker implements
        RecognitionListener {
    private ProgressBar progressBar;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";
    private String errorMessage;

    public MyWorkerJAVA(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    @NonNull
    @Override
    public Result doWork() {

//        Handler handler = new Handler(Looper.getMainLooper());
//        handler.post(new Runnable() {
//
//            @Override
//            public void run() {
//
//                PeriodicWorkRequest.Builder pwr = new PeriodicWorkRequest.Builder(MyWorkerJAVA.class,2,SECONDS);
//
//                PeriodicWorkRequest request = pwr.build();
//                WorkManager.getInstance().enqueueUniquePeriodicWork("JAVWORKER", ExistingPeriodicWorkPolicy.REPLACE , request);

//            }
//        });

        Log.e("DOWORK called result", "" + Result.SUCCESS);

        Handler handler1 = new Handler(Looper.getMainLooper());
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms


                speech = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                Log.i(LOG_TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(getApplicationContext()));
                speech.setRecognitionListener(MyWorkerJAVA.this);
                recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                        "en");
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);


                PeriodicWorkRequest.Builder pwr = new PeriodicWorkRequest.Builder(MyWorkerJAVA.class, 5, SECONDS);

                PeriodicWorkRequest request = pwr.build();
                WorkManager.getInstance().enqueueUniquePeriodicWork("MYJAVAWORKER", ExistingPeriodicWorkPolicy.REPLACE, request);

                Toast.makeText(getApplicationContext(), "Worker runing 5 sec", Toast.LENGTH_LONG).show();
                try {
                    if (errorMessage.equalsIgnoreCase("RecognitionService busy")) {
                        Log.e("DOWORK called result", "busyyyyy 11=");
                        speech.stopListening();
                        speech.cancel();
                        speech.destroy();
                        speech.startListening(recognizerIntent);
                        return;
                    }
                }
                catch (Exception e){

            }

                Log.e("DOWORK called result", "busyyyyy 22=" );

                //speech.destroy();
               // speech.stopListening();
                speech.startListening(recognizerIntent);

            }
        }, 5000);


        return Result.SUCCESS;
    }


    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        // progressBar.setIndeterminate(false);
        //progressBar.setMax(10);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        //  progressBar.setIndeterminate(true);
        //  toggleButton.setChecked(false);
    }

    @Override
    public void onError(int errorCode) {
        errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        if (errorMessage.equalsIgnoreCase("RecognitionService busy")){
            Log.e("DOWORK called result", "busyyyyy 33=" );
            speech.destroy();


        }

       // if (errorMessage.equalsIgnoreCase("No match")) {
            PeriodicWorkRequest.Builder pwr = new PeriodicWorkRequest.Builder(MyWorkerJAVA.class, 5, SECONDS);

            PeriodicWorkRequest request = pwr.build();
            WorkManager.getInstance().enqueueUniquePeriodicWork("MYJAVAWORKER", ExistingPeriodicWorkPolicy.REPLACE, request);

        ///}
        createNotification("Listening", "Ready for Speech");
        //returnedText.setText(errorMessage);
        //toggleButton.setChecked(false);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        createNotification("Listening", "Ready for Speech");
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {

        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches)
            text += result + "\n";
        Log.i(LOG_TAG, "onResults" + text);
        createNotification("Listening", text);
        //returnedText.setText(text);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
//        progressBar.setProgress((int) rmsdB);
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    private void createNotification(String title, String value) {

        // Create an explicit intent for an Activity in your app

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getApplicationContext().getString(R.string.channel_name);
            String description = getApplicationContext().getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("333", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        Intent intent = new Intent(getApplicationContext(), MainActivityJAVA.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "333")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(value)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(123, mBuilder.build());
    }

}
