package com.example.jiajiemu.blockdemo.dagger;

/**
 * Created by Mjj on 2017/11/14.
 */

public class ClassA {
    ClassB b;

    public ClassA() {
        b = new ClassB();
    }

    public void test() {
        b.doTest();
    }

}
