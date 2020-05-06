package com.example.jiajiemu.a11.rxjava;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jiajiemu on 2017/11/2.
 */

public class SupermarketListActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void receiveData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                //从服务端获取北京市海淀区小区列表
                List<House> houses = getHouseFromServer();
                for (House house : houses) {
                    List<SuperMarket> superMarkets = house.superMarket;
                    for (SuperMarket superMarket : superMarkets) {
                        if (superMarket.size >= 500) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //将超市的详细信息添加到屏幕上
                                }
                            });
                        }
                    }
                }
            }
        }.start();
    }

    public List<House> getHouseFromServer() {
        return null;
    }

    private void receiveDataWithRxjava() {
        Observable.from(getHouseFromServer())
                .flatMap(new Func1<House, Observable<SuperMarket>>() {
                    @Override
                    public Observable<SuperMarket> call(House house) {
                        return Observable.from(house.superMarket);
                    }

                }).filter(new Func1<SuperMarket, Boolean>() {
            @Override
            public Boolean call(SuperMarket house) {
                return house.size >= 500;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SuperMarket>() {
                    @Override
                    public void call(SuperMarket superMarket) {
                        //将超市的详细信息添加到屏幕上
                    }
                });
    }


    private void rxJavaExample() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello Imooc Rxjava!");
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return s + "-->map";
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("imooc Rxjava completed!");
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        });
    }

}
