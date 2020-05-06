package com.example.jiajiemu.a11.Leak;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.example.jiajiemu.a11.R;

/**
 * Created by Mjj on 2017/10/7.
 * 5.Webview造成的内存泄漏
 */


public class WebviewLeakActivity extends AppCompatActivity {
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.wv_show);
        mWebView.loadUrl("http://www.baidu.com");
    }


    @Override
    protected void onDestroy() {
        destroyWebView();
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }

    private void destroyWebView() {
        if (mWebView != null) {
            mWebView.pauseTimers();
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }

}
