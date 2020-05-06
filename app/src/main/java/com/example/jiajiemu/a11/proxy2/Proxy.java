package com.example.jiajiemu.a11.proxy2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by mjj on 2017/10/10.
 */

public class Proxy implements InvocationHandler {
    private Object target;//要代理的真实对象
    public Proxy(Subject target){
        this.target = target;
    }
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("proxy:"+proxy.getClass().getName());
        System.out.println("before...");
        method.invoke(target, args);
        System.out.println("after...");
        return null;
    }
}

