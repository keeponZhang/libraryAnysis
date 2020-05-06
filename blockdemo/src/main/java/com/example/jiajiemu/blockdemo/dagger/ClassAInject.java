package com.example.jiajiemu.blockdemo.dagger;

import javax.inject.Inject;

import dagger.Provides;

/**
 * Created by Mjj on 2017/11/14.
 */

public class ClassAInject {
    @Inject  ClassB classB;

    public ClassAInject() {}
}
