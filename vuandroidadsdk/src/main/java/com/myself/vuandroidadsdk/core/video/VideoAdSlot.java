package com.myself.vuandroidadsdk.core.video;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.myself.vuandroidadsdk.activity.AdBrowserActivity;
import com.myself.vuandroidadsdk.adutil.Utils;
import com.myself.vuandroidadsdk.constant.SDKConstant;
import com.myself.vuandroidadsdk.core.AdParameters;
import com.myself.vuandroidadsdk.module.AdValue;
import com.myself.vuandroidadsdk.report.ReportManager;
import com.myself.vuandroidadsdk.widget.ADVideoPlayerListener;
import com.myself.vuandroidadsdk.widget.CustomVideoView;
import com.myself.vuandroidadsdk.widget.VideoFullDialog;

/**
 * Created by Kamh on 2018/6/14.
 * 广告业务逻辑层
 */

public class VideoAdSlot implements ADVideoPlayerListener{

    /**
     * UI
     */
    private Context mContext;
    private ViewGroup mParentView;//要添加到父容器中
    private CustomVideoView mVideoView;

    private AdValue mXAdInstance;
    private AdSDKSlotListener mSlotListener;
    private boolean canPause = false;//是否可暂停标志位
    private int lastArea = 0; //防止将要滑入滑出时播放器的状态改变

    public VideoAdSlot(AdValue adInstance, AdSDKSlotListener slotListener, CustomVideoView.ADFrameImageLoadListener frameLoadListener) {
        mXAdInstance = adInstance;
        mSlotListener = slotListener;
        mParentView = slotListener.getAdParent();
        mContext = mParentView.getContext();
        initVideoView(frameLoadListener);
    }

    private void initVideoView(CustomVideoView.ADFrameImageLoadListener frameImageLoadListener){
        mVideoView = new CustomVideoView(mContext, mParentView);
        if (mXAdInstance != null){
            mVideoView.setDataSource(mXAdInstance.resource);
            mVideoView.setFrameURI(mXAdInstance.thumb);
            mVideoView.setFrameLoadListener(frameImageLoadListener);
            mVideoView.setListener(this);
        }
        RelativeLayout paddingView = new RelativeLayout(mContext);
        paddingView.setBackgroundColor(mContext.getResources().getColor(android.R.color.black));
        paddingView.setLayoutParams(mVideoView.getLayoutParams());
        mParentView.addView(paddingView);
        mParentView.addView(mVideoView);
    }

    /**
     *  实现滑入播放，滑出暂停功能
     */
    public void updateVideoInSrollView(){
        int currentArea = Utils.getVisiblePercent(mParentView);
        //如果View没有出现在屏幕上，不做任何处理
        if (currentArea <= 0){
            return;
        }
        //刚要滑入和滑出的异常情况处理
        if (Math.abs(currentArea - lastArea) >= 100){
            return;
        }
        //滑动没有超过屏幕50%时走入这些case
        if (currentArea <= SDKConstant.VIDEO_SCREEN_PERCENT){
            if (canPause){
                pauseVideo(false);
                canPause = false;//滑动事件的过滤
            }
            lastArea = 0;
            mVideoView.setIsComplete(false);
            mVideoView.setIsRealPause(false);
            return;
        }
        //当视频进入真正的暂停状态是走此case
        if (isRealPause() || isComplete()){
            pauseVideo(false);
            canPause = false;
        }

        //满足用户视频播放设置条件
        if (Utils.canAutoPlay(mContext, AdParameters.getCurrentSetting()) || isPlaying()){
            //真正去播放视频
            lastArea = currentArea;
            resumeVideo();
            canPause = true;
            mVideoView.setIsRealPause(false);
        }else{
            //不满足用户条件设置
            pauseVideo(false);
            mVideoView.setIsRealPause(true);
        }
    }

    private boolean isPlaying() {
        if (mVideoView != null) {
            return mVideoView.isPlaying();
        }
        return false;
    }

    private boolean isRealPause() {
        if (mVideoView != null) {
            return mVideoView.isRealPause();
        }
        return false;
    }

    private boolean isComplete() {
        if (mVideoView != null) {
            return mVideoView.isComplete();
        }
        return false;
    }

    //pause the  video
    private void pauseVideo(boolean isAuto) {
        if (mVideoView != null) {
            if (isAuto) {
                //发自动暂停监测
                if (!isRealPause() && isPlaying()) {
                    try {
                        ReportManager.pauseVideoReport(mXAdInstance.event.pause.content, getPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            mVideoView.seekAndPause(0);
        }
    }

    //resume the video
    private void resumeVideo() {
        if (mVideoView != null) {
            mVideoView.resume();
            if (isPlaying()) {
                sendSUSReport(true); //发自动播放监测
            }
        }
    }

    /**
     * 发送视频开始播放监测
     *
     * @param isAuto
     */
    private void sendSUSReport(boolean isAuto) {
        try {
            ReportManager.susReport(mXAdInstance.startMonitor, isAuto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAdInScrollView() {
        int currentArea = Utils.getVisiblePercent(mParentView);
        //小于0表示未出现在屏幕上，不做任何处理
        if (currentArea <= 0) {
            return;
        }
        //刚要滑入和滑出时，异常状态的处理
        if (Math.abs(currentArea - lastArea) >= 100) {
            return;
        }
        if (currentArea < SDKConstant.VIDEO_SCREEN_PERCENT) {
            //进入自动暂停状态
            if (canPause) {
                pauseVideo(true);
                canPause = false;
            }
            lastArea = 0;
            mVideoView.setIsComplete(false); // 滑动出50%后标记为从头开始播
            mVideoView.setIsRealPause(false); //以前叫setPauseButtonClick()
            return;
        }

        if (isRealPause() || isComplete()) {
            //进入手动暂停或者播放结束，播放结束和不满足自动播放条件都作为手动暂停
            pauseVideo(false);
            canPause = false;
            return;
        }

        //满足自动播放条件或者用户主动点击播放，开始播放
        if (Utils.canAutoPlay(mContext, AdParameters.getCurrentSetting())
                || isPlaying()) {
            lastArea = currentArea;
            resumeVideo();
            canPause = true;
            mVideoView.setIsRealPause(false);
        } else {
            pauseVideo(false);
            mVideoView.setIsRealPause(true); //不能自动播放则设置为手动暂停效果
        }
    }

    public void destroy() {
        mVideoView.destroy();
        mVideoView = null;
        mContext = null;
        mXAdInstance = null;
    }

    @Override
    public void onBufferUpdate(int time) {

    }

    /**
     * 实现从小屏到全屏播放的功能接口
     */
    @Override
    public void onClickFullScreenBtn() {
        mParentView.removeView(mVideoView);
        //创建全屏播放Dialog
        VideoFullDialog dialog = new VideoFullDialog(mContext, mVideoView, mXAdInstance, mVideoView.getCurrentPosition());
        dialog.setListener(new VideoFullDialog.FullToSmallListener() {
            @Override
            public void getCurrentPlayPosition(int position) {
                //在全屏视屏播放的时候点击返回
                backToSmallMode(position);
            }

            @Override
            public void playComplete() {
                //全屏播放完以后的事件回调
                bigPlayComplete();
            }
        });
        dialog.show();
    }

    /**
     * 全屏播放结束后的事件回调
     */
    private void bigPlayComplete() {
        if (mVideoView.getParent() == null){
            mParentView.addView(mVideoView);
        }
        mVideoView.isShowFullBtn(true);
        mVideoView.mute(true);
        mVideoView.setListener(this);
        mVideoView.seekAndPause(0);
        canPause = false;
    }

    /**
     * 返回小屏模式的时候
     * @param position
     */
    private void backToSmallMode(int position) {
        if (mVideoView.getParent() == null){
            mParentView.addView(mVideoView);
        }

        mVideoView.isShowFullBtn(true);//显示全屏按钮
        mVideoView.mute(true);//小屏静音播放
        mVideoView.setListener(this);//重新设置监听为我们的业务逻辑层
        mVideoView.seekAndResume(position);//使跳到指定位置并播放
    }

    @Override
    public void onClickVideo() {
        String desationUrl = mXAdInstance.clickUrl;
        //跳转到webView页面
        if (!TextUtils.isEmpty(desationUrl)){
            Intent intent = new Intent(mContext, ADVideoPlayerListener.class);
            intent.putExtra(AdBrowserActivity.KEY_URL, mXAdInstance.clickUrl);
            mContext.startActivity(intent);
        }
    }

    @Override
    public void onClickBackTBtn() {

    }

    @Override
    public void onClickPlay() {

    }

    @Override
    public void onAdVideoLoadSuccess() {
        if (mSlotListener != null){
            mSlotListener.onAdVideoLoadSuccess();
        }
    }

    @Override
    public void onAdVideoLoadFailed() {
        if (mSlotListener != null){
            mSlotListener.onAdVideoLoadFailed();
        }
    }

    @Override
    public void onAdVideoComplete() {
        try{
            ReportManager.sueReport(mXAdInstance.endMonitor, false, getDuration());
        }catch (Exception e){
            e.printStackTrace();
        }
        if (mSlotListener != null){
            mSlotListener.onAdVideoLoadComplete();
        }
        mVideoView.setIsRealPause(true);
    }

    private int getPosition() {
        return mVideoView.getCurrentPosition() / SDKConstant.MILLION_UNIT;
    }

    private int getDuration() {
        return mVideoView.getDuration() / SDKConstant.MILLION_UNIT;
    }

    //传递消息到appcontext层
    public interface AdSDKSlotListener {

        public ViewGroup getAdParent();

        public void onAdVideoLoadSuccess();

        public void onAdVideoLoadFailed();

        public void onAdVideoLoadComplete();

        public void onClickVideo(String url);
    }
}
