package com.example.jiajiemu.a11;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mjj on 2017/10/8.
 */

public class RetrofitS {
    public void MyRetrofit() throws IOException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://xxx.xxx.com/") // 设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava平台
                .build();

        MyInterface myInterface = retrofit.create(MyInterface.class);

        Call call = myInterface.getCall();
        //retrofit同步请求
        call.execute();

        call.enqueue(new retrofit2.Callback() {
            @Override
            public void onResponse(Call call, retrofit2.Response response) {
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call call, Throwable throwable) {
                System.out.println("请求失败");
            }
        });
    }

}
