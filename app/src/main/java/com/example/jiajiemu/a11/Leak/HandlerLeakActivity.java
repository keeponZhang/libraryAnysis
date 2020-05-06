package com.example.jiajiemu.a11.Leak;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Mjj on 2017/10/7.
 * 3.handler造成内存泄漏
 * handler、Message、MessageQueue
 * TLS
 */

public class HandlerLeakActivity extends Activity {
    private final Handler mLeakyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLeakyHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 1000 * 60 * 10);

        finish();
    }
    /*
    * 解决办法：
    * 1.将 Handler 声明为静态的
    * 2.通过弱引用的方式引入 Activity
    *
    * */
}
