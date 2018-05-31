package com.myself.vuandroidadsdk.okhttp.response;

import java.io.IOException;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myself.vuandroidadsdk.okhttp.exception.OkHttpException;
import com.myself.vuandroidadsdk.okhttp.listener.DisposeDataHandle;
import com.myself.vuandroidadsdk.okhttp.listener.DisposeDataListener;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Kamh on 2018/5/31.
 */

public class CommonJsonCallback implements Callback {

    //与服务器返回的字段的对应关系
    protected final String RESULT_CODE = "ecode";
    protected final int RESULT_CODE_VALUE = 0;
    protected final String ERROR_MSG = "emsg";
    protected final String EMPTY_MSG = "";

    /**
     * 自定义异常类型
     */
    protected final int NETWORK_ERROR = -1;
    protected final int JSON_ERROR = -2;
    protected final int OTHER_ERROR = -3;

    private final Handler mDeliveryHandler;   //进行消息的转发
    private final DisposeDataListener mListener;
    private final Class<?> mClass;

    public CommonJsonCallback(DisposeDataHandle disposeDataHandle) {
        this.mListener = disposeDataHandle.mListener;
        this.mClass = disposeDataHandle.mClass;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    //请求失败处理
    @Override
    public void onFailure(Call call, final IOException e) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, e));
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handlerResponse(result);
            }
        });
    }

    /**
     * 处理服务器返回的响应数据
     *
     * @param resultObj
     */
    private void handlerResponse(Object resultObj) {
        //为了保证代码的健壮性
        if (TextUtils.isEmpty(resultObj.toString().trim())) {
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }
        try {
            JSONObject result = JSON.parseObject(resultObj.toString());
            if (result.getInteger(RESULT_CODE) == RESULT_CODE_VALUE) {
                if (mClass == null) {
                    mListener.onSuccess(resultObj);
                } else {
                    //需要我们把Json对象转化为实体对象
                    Object obj = JSON.parseObject(resultObj.toString(), mClass);
                    if (obj != null){
                        mListener.onSuccess(obj);
                    }else{
                        //返回的Json不是一个合法的Json
                        mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                    }
                }
            }else{
                mListener.onFailure(new OkHttpException(OTHER_ERROR, result.getString(ERROR_MSG)));
            }
        } catch (Exception e) {
            mListener.onFailure(new OkHttpException(OTHER_ERROR, EMPTY_MSG));
        }
    }
}
