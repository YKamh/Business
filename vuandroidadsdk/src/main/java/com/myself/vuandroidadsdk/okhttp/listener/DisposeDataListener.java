package com.myself.vuandroidadsdk.okhttp.listener;

/**
 * Created by Kamh on 2018/5/31.
 *  在定义事件监听
 */

public interface DisposeDataListener {

    /**
     * 请求成功回调
     * @param responseObj
     */
    void onSuccess(Object responseObj);

    /**
     * 请求失败回调
     * @param reasonObj
     */
    void onFailure(Object reasonObj);

}
