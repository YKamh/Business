package com.myself.business.application;

import android.app.Application;

/**
 * Created by Kamh on 2018/5/29.
 * 1、application类是整个程序的入口
 * 2、初始化工作
 * 3、为整个应用的其他模块提供上下文
 */

public class MyApplication extends Application{

    private static MyApplication sMyApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        sMyApplication = this;
    }

    public static MyApplication getInstance(){
        return sMyApplication;
    }
}
