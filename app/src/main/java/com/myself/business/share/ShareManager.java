package com.myself.business.share;

import android.content.Context;

import com.mob.MobSDK;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

/**
 * Created by Kamh on 2018/7/6.
 * @function 分享功能统一入口，负责发送数据到指定平台，采用单例模式
 */

public class ShareManager {

    private Platform mCurrentPlantform;//当前平台类型

    private ShareManager(){}

    private static class Holder{
        private static final ShareManager INSTANCE = new ShareManager();
    }

    public static ShareManager getInstance(){
        return Holder.INSTANCE;
    }

    //分享数据入口
    public void shareData(ShareData data, PlatformActionListener listener){
        switch (data.mType){
            case QQ:
                mCurrentPlantform = ShareSDK.getPlatform(QQ.NAME);
                break;
//            case QZONE:
//                mCurrentPlantform = ShareSDK.getPlatform(.NAME);
//                break;
            case WeChat:
                mCurrentPlantform = ShareSDK.getPlatform(QQ.NAME);
                break;
//            case WeChatMoment:
//                mCurrentPlantform = ShareSDK.getPlatform(QQ.NAME);
//                break;
            default:
                break;
        }
        mCurrentPlantform.setPlatformActionListener(listener);
        mCurrentPlantform.share(data.mShareParams);
    }

    public static void init(Context context){
        MobSDK.init(context);
    }

}
