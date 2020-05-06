package com.example.jiajiemu.a11.Leak;

import android.content.Context;

/**
 * Created by Mjj on 2017/10/7.
 */

public class SingletonAppliationContext {
    private static SingletonAppliationContext instance;
    private Context context;
    private SingletonAppliationContext(Context context) {
        this.context = context.getApplicationContext();// 使用Application 的context
    }
    public static SingletonAppliationContext getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonAppliationContext(context);
        }
        return instance;
    }
}