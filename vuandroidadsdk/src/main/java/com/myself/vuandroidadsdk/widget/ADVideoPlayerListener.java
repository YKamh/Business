package com.myself.vuandroidadsdk.widget;

/**
 * Created by Kamh on 2018/6/12.
 * 播放事件监听接口
 */

public interface ADVideoPlayerListener {

    void onBufferUpdate(int time);

    void onClickFullScreenBtn();

    void onClickVideo();

    void onClickBackTBtn();

    void onClickPlay();

    void onAdVideoLoadSuccess();

    void onAdVideoLoadFailed();

    void onAdVideoComplete();

}
