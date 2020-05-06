package com.example.jiajiemu.a11.rxjava;

import android.graphics.Bitmap;
import android.util.Log;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Mjj on 2017/10/7.
 */

public class RxjavaOperatorDemo {
    private void map() {
        Observable.just("map/image/map.png")
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String filePath) {
                        return getBitmap(filePath);
                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        showBitmap(bitmap);
                    }
                });
    }

    private void showBitmap(Bitmap bitmap) {
        // TODO: 2017/10/7
    }

    private Bitmap getBitmap(String filePath) {
        return null;
    }

    private Subscription processNetAddress() {
        return Observable.just(
                "http://www.baidu.com/",
                "https://www.sina.com/",
                "https://www.sohu.com/")
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return createObservable(s);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                    }
                });
    }

    private Observable<String> createObservable(String s) {
        return null;
    }
}
