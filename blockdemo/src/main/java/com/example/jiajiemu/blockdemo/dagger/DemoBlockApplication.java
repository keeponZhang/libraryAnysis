package com.example.jiajiemu.blockdemo.dagger;

import android.app.Application;

import com.example.jiajiemu.blockdemo.AppBlockContext;
import com.github.moduth.blockcanary.BlockCanary;

/**
 * Created by jiajiemu on 2017/11/15.
 */

public class DemoBlockApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BlockCanary.install(this, new AppBlockContext()).start();
    }
}
