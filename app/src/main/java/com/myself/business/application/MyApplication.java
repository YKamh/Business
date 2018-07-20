package com.myself.business.application;

import android.app.Application;

import com.mob.MobSDK;
import com.myself.business.share.ShareManager;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

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
        initShareSDK();
        initJPush();
    }

    private void initShareSDK(){
        ShareManager.init(this);
    }

    private void initJPush(){
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    public static MyApplication getInstance(){
        return sMyApplication;
    }

    public void initUMeng(){
        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
    }
}
