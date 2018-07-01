package com.myself.business.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.myself.business.application.MyApplication;

/**
 * Created by Kamh on 2018/7/1.
 * 配置文件工具类
 */

public class SPManager {

    private final SharedPreferences sp;
    private final SharedPreferences.Editor sEditor;

    private static final String SHARE_PREFERENCE_NAME = "business.pre";
    public static final String VIDEO_SETTING = "video_setting";//视频播放设置

    private static class Holder{
        private static SPManager mInstance = new SPManager();
    }

    public static SPManager getInstance(){
        return Holder.mInstance;
    }

    private SPManager(){
        sp = MyApplication.getInstance().getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        sEditor = sp.edit();
    }

    public void putInt(String key, int value){
        sEditor.putInt(key, value);
        sEditor.commit();
    }

    public int getInt(String key, int defaultValue){
        return sp.getInt(key, defaultValue);
    }

    public void putString(String key, String value){
        sEditor.putString(key, value);
        sEditor.commit();
    }

    public String getString(String key, String defaultValue){
        return sp.getString(key, defaultValue);
    }

    public void putFloat(String key, float value){
        sEditor.putFloat(key, value);
        sEditor.commit();
    }

    public float getFloat(String key, float defaultValue){
        return sp.getFloat(key, defaultValue);
    }

}
