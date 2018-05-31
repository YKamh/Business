package com.myself.vuandroidadsdk.okhttp.request;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Kamh on 2018/5/30.
 * @function 封装所有的请求参数到HashMap中。
 */

public class RequestParams {

    public final ConcurrentHashMap<String, String> urlParams = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, Object> fileParam = new ConcurrentHashMap<>();

    public RequestParams(){
        this((Map<String, String>) null);
    }

    public RequestParams(Map<String, String> source){
        if (source != null){
            for (Map.Entry<String, String> entry : source.entrySet()){
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    public RequestParams(final String key, final String value){
        this(new HashMap<String, String>(){
            {
                put(key, value);
            }
        });
    }

    public void put(String key, String value){
        if (key != null && value != null){
            urlParams.put(key, value);
        }
    }

    public void put(String key, Object object) throws FileNotFoundException{
        if (key != null){
            fileParam.put(key, object);
        }
    }

    public boolean hasParams(){
        if (urlParams.size() > 0 || fileParam.size() > 0){
            return true;
        }
        return false;
    }
}
