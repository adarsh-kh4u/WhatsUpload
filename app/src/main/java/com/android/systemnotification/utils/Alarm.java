package com.android.systemnotification.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.android.systemnotification.MainActivity;


public class Alarm extends BroadcastReceiver {

    final String TAG = Alarm.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "sys:not");
        wl.acquire();

        //Put here YOUR code.
        startXMPPService(context);

        wl.release();
    }

    public void setAlarm(Context context) {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 10, pi); // Millisec * Second * Minute
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60, pi); // Millisec * Second

    }

    public void cancelAlarm(Context context) {
        try {
            Intent intent = new Intent(context, Alarm.class);
            PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(sender);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void startXMPPService(Context context){

        Log.d(TAG, "Alarm");

        ClassHelper classHelper = new ClassHelper();
        if (classHelper.isMyServiceRunning(BackgroundService.class, context)) {


        } else {
            Log.d(TAG, "Starting service from alarm");

            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }
    }
}
