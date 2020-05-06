package com.example.jiajiemu.a11.Leak;

import android.content.Context;

/**
 * Created by Mjj on 2017/10/7.
 * 1.单例造成的内存泄漏
 */


public class SingletonActivityContext {
    private static SingletonActivityContext instance;
    private Context context;

    private SingletonActivityContext(Context context) {
        this.context = context;
    }

    public static SingletonActivityContext getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonActivityContext(context);
        }
        return instance;
    }
}
