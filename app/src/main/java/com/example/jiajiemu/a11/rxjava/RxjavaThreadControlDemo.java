package com.example.jiajiemu.a11.rxjava;

import android.util.Log;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jiajiemu on 2017/11/7.
 */

public class RxjavaThreadControlDemo {

    public void doThread() {
        Observable.just(100, 200, 300, 400)
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer number) {
                        Log.d("RxjavaThreadControlDemo", "number:" + number);
                    }
                });
    }
}
