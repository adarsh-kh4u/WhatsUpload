package com.android.systemnotification.utils;

import android.app.Application;

import androidx.multidex.MultiDexApplication;

public class MyApplication extends MultiDexApplication {

    private static MyApplication instance;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
