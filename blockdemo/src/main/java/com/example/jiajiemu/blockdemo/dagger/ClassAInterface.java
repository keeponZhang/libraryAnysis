package com.example.jiajiemu.blockdemo.dagger;

/**
 * Created by jiajiemu on 2017/11/14.
 */

public class ClassAInterface implements ClassBInterface{
    ClassBInterface classB;

    @Override
    public void setB(ClassBInterface b) {
        classB = b;
    }
}
