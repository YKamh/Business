package com.myself.vuandroidadsdk.core;

import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.myself.vuandroidadsdk.adutil.Utils;
import com.myself.vuandroidadsdk.core.video.VideoAdSlot;
import com.myself.vuandroidadsdk.module.AdValue;
import com.myself.vuandroidadsdk.okhttp.HttpConstant;
import com.myself.vuandroidadsdk.report.ReportManager;
import com.myself.vuandroidadsdk.widget.CustomVideoView;

/**
 * Created by Kamh on 2018/6/27.
 */

public class VideoAdContext implements VideoAdSlot.AdSDKSlotListener {

    private ViewGroup mParentView;
    private VideoAdSlot mAdSlot;
    private AdValue mInstance = null;
    private AdContextInterface mListener;
    private CustomVideoView.ADFrameImageLoadListener mFrameLoadListener;

    public VideoAdContext(ViewGroup parentView, String instance,
                          CustomVideoView.ADFrameImageLoadListener frameLoadListener) {
        mParentView = parentView;
        mInstance = JSON.parseObject(instance, AdValue.class);
        this.mFrameLoadListener = frameLoadListener;
        load();
    }

    /**
     * 创建Slot业务逻辑类， 不调用则不会创建最底层的CustomVideoView
     */
    private void load() {
        if (mInstance != null && mInstance.resource != null) {
            mAdSlot = new VideoAdSlot(mInstance, this, mFrameLoadListener);
            sendAnalizeReport(HttpConstant.Params.ad_analize, HttpConstant.AD_DATA_SUCCESS);
        } else {
            mAdSlot = new VideoAdSlot(null, this, mFrameLoadListener);//创建空的slot，比响应任何事件
            if (mListener != null) {
                mListener.onAdFailed();
            }
            sendAnalizeReport(HttpConstant.Params.ad_analize, HttpConstant.AD_DATA_FAILED);
        }
    }

    /**
     * release the ad
     */
    public void destroy() {
        mAdSlot.destroy();
    }

    public void updateAdInScrollView() {
        if (mAdSlot != null) {
            mAdSlot.updateAdInScrollView();
        }
    }

    public void setAdResultListener(AdContextInterface listener) {
        this.mListener = listener;
    }

    private void sendAnalizeReport(HttpConstant.Params ad_analize, String adDataSuccess) {
        try {
            ReportManager.sendAdMonitor(Utils.isPad(mParentView.getContext().getApplicationContext()), mInstance == null ? "" : mInstance.resourceID
                    , (mInstance == null ? null : mInstance.adid), Utils.getAppVersion(mParentView.getContext().getApplicationContext()), ad_analize, adDataSuccess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewGroup getAdParent() {
        return null;
    }

    @Override
    public void onAdVideoLoadSuccess() {

    }

    @Override
    public void onAdVideoLoadFailed() {

    }

    @Override
    public void onAdVideoLoadComplete() {

    }

    @Override
    public void onClickVideo(String url) {

    }
}
