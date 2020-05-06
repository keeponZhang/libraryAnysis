package com.example.jiajiemu.a11;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Mjj on 2017/10/7.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
