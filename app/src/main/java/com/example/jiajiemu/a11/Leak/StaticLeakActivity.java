package com.example.jiajiemu.a11.Leak;

import android.app.Activity;
import android.os.Bundle;

import com.example.jiajiemu.a11.R;

/**
 * Created by Mjj on 2017/10/7.
 * 2.非静态内部类创建静态实例造成的内存泄漏
 */

public class StaticLeakActivity extends Activity {
    private static noneStaticClass mResource = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (mResource == null) {
            mResource = new noneStaticClass();
        }

    }

    private class noneStaticClass {

    }
}
