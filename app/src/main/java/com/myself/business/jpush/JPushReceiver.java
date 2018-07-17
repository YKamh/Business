package com.myself.business.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.myself.business.activity.HomeActivity;
import com.myself.business.activity.LoginActivity;
import com.myself.business.manager.UserManager;
import com.myself.business.model.PushMessage;
import com.myself.business.util.Util;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Kamh on 2018/7/16.
 *
 * @function 用来接收极光SDK推送给App的消息
 */

public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = JPushReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (intent.getAction().equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {
            Log.d(TAG, "onReceive: 不需要跳转的action");
        } else if (intent.getAction().equals(JPushInterface.ACTION_NOTIFICATION_OPENED)) {
            PushMessage message = JSON.parseObject(bundle.getString(JPushInterface.EXTRA_EXTRA), PushMessage.class);
            if (Util.getCurrentTask(context)) {
                Intent pushIntent = new Intent();
                pushIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                pushIntent.putExtra("pushMessage", message);
                //应用已启动
                if (message != null && message.messageType.equals("2") && !UserManager.getInstance().hasLogined()) {
                    //如果需要登录且当前没有登录
                    pushIntent.setClass(context, LoginActivity.class);
                    pushIntent.putExtra("fromPush", true);
                } else {
                    //用户已经登录或者用户不需要登录， 直接跳转到消息展示页面
                    pushIntent.setClass(context, PushMessageActivity.class);
                }
                context.startActivity(pushIntent);
            } else {
                //应用未启动
                Intent mainIntent = new Intent(context, HomeActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (message != null && message.messageType.equals("2")){
                    //跳转到登录界面
                    Intent loginIntent = new Intent(context, LoginActivity.class);
                    loginIntent.putExtra("fromPush", true);
                    loginIntent.putExtra("pushMessage", message);

                    context.startActivities(new Intent[]{mainIntent, loginIntent});
                }else{
                    Intent pushIntent = new Intent(context, PushMessageActivity.class);
                    pushIntent.putExtra("pushMessage", message);

                    context.startActivities(new Intent[]{mainIntent, pushIntent});
                }
            }
        }
    }
}
