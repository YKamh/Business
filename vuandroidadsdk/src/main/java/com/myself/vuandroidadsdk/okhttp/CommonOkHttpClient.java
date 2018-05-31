package com.myself.vuandroidadsdk.okhttp;

import com.myself.vuandroidadsdk.okhttp.https.HttpsUtils;
import com.myself.vuandroidadsdk.okhttp.request.RequestParams;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
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
     * @param request
     * @param commCallback
     * @return  Call的实例
     */
    public static Call sendRequest(Request request, Callback commCallback){
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(commCallback);

        return call;
    }
}
