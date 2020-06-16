package com.android.systemnotification.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.android.systemnotification.MainActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class BootCompletedIntentReceiver extends BroadcastReceiver{
    final String TAG = BootCompletedIntentReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent){

        Log.e(TAG, "Receiver");


        startService(context);
    }

    private void startService(Context context){
        ClassHelper classHelper = new ClassHelper();

        if (!classHelper.isMyServiceRunning(BackgroundService.class, context)) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            Alarm alarm = new Alarm();
            alarm.setAlarm(context);
        }
    }
}
