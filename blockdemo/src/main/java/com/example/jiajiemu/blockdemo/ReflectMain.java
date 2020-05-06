package com.example.jiajiemu.blockdemo;

import java.lang.reflect.Field;

/**
 * Created by Mjj on 2017/10/12.
 */

public class ReflectMain {
    @TestInterface(12)
    public int age;

    public static void main(String[] args) {
        ReflectMain main = new ReflectMain();
        TestInterface testInterface = null;
        try {
            Class clazz = main.getClass();
            Field field = clazz.getField("age");
            testInterface = field.getAnnotation(TestInterface.class);
            System.out.print(testInterface.value());
        } catch (NoSuchFieldException e) {
            System.out.print("no such field");
        }
    }
}
