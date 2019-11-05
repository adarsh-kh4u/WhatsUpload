package com.android.systemnotification.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.android.systemnotification.MainActivity;

public class BootCompletedIntentReceiver extends BroadcastReceiver{
    final String TAG = BootCompletedIntentReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent){

        Log.e(TAG, "Receiver");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O || (intent.getAction() != null && !intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))) {
            startService(context);
        }
    }

    private void startService(Context context){
        ClassHelper classHelper = new ClassHelper();
        if (classHelper.isMyServiceRunning(BackgroundService.class, context)) {


        } else {
            Log.d(TAG, "Starting service from alarm");

            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }
    }
}
