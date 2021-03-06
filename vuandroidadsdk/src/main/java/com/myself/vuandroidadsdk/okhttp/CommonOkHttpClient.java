package com.myself.vuandroidadsdk.okhttp;

import com.myself.vuandroidadsdk.okhttp.https.HttpsUtils;
import com.myself.vuandroidadsdk.okhttp.listener.DisposeDataHandle;
import com.myself.vuandroidadsdk.okhttp.request.CommonRequest;
import com.myself.vuandroidadsdk.okhttp.request.RequestParams;
import com.myself.vuandroidadsdk.okhttp.response.CommonFileCallback;
import com.myself.vuandroidadsdk.okhttp.response.CommonJsonCallback;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Kamh on 2018/5/30.
 *
 * @function 请求的发送，请求参数的配置，https的支持。
 */

public class CommonOkHttpClient {

    private static final int TIME_OUT = 30; //超时参数
    private static OkHttpClient mOkHttpClient;

    //为我们的client配置参数
    static {
        //创建我们的client对象的构建者
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .followRedirects(true)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(HttpsUtils.initSSLSocketFactory());
        //生成我们的client对象
        mOkHttpClient = okHttpBuilder.build();
    }

    /**
     * 发送具体的Http/Https请求
     * @param url
     * @param params
     * @param handle
     * @return
     */
    public static Call get(String url, RequestParams params, DisposeDataHandle handle){
        Call call = mOkHttpClient.newCall(CommonRequest.createGetRequest(url, params));
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    public static Call post(String url, RequestParams params, DisposeDataHandle handle){
        Call call = mOkHttpClient.newCall(CommonRequest.createPostRequest(url, params));
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    public static Call downloadFile(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonFileCallback(handle));
        return call;
    }
}
