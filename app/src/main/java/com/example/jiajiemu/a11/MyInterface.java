package com.example.jiajiemu.a11;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Mjj on 2017/10/8.
 */

public interface MyInterface {

    @GET("...／...")
    Call<List<MyResponse>> getCall();
}

