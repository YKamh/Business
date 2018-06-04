package com.myself.business.network.http;

import com.myself.business.module.recommand.BaseRecommandModel;
import com.myself.vuandroidadsdk.okhttp.CommonOkHttpClient;
import com.myself.vuandroidadsdk.okhttp.listener.DisposeDataHandle;
import com.myself.vuandroidadsdk.okhttp.listener.DisposeDataListener;
import com.myself.vuandroidadsdk.okhttp.request.RequestParams;

/**
 * Created by Kamh on 2018/6/4.
 * @function 存放应用中所有的请求
 */

public class RequestCenter {
    //根据参数发送Post请求
    private static void postRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz){
        CommonOkHttpClient.get(url, params, new DisposeDataHandle(listener, clazz));
    }

    /**
     * 真正的发送首页的数据请求
     * @param listener
     */
    public static void requestRecommandData(DisposeDataListener listener){
        RequestCenter.postRequest(HttpConstants.HOME_RECOMMAND, null, listener, BaseRecommandModel.class);
    }
}
