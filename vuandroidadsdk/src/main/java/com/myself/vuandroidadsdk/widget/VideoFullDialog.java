package com.myself.vuandroidadsdk.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.myself.vuandroidadsdk.R;
import com.myself.vuandroidadsdk.module.AdValue;

/**
 * Created by Kamh on 2018/6/12.
 */

public class VideoFullDialog extends Dialog implements ADVideoPlayerListener{

    private static final String TAG = VideoFullDialog.class.getName();

    private CustomVideoView mVideoView;
    private ViewGroup mParentView;
    private ImageView mBackButton;

    private AdValue mXAdInstance;
    private FullToSmallListener mListener;
    private int mPosition;//从小屏到全屏的播放位置
    private boolean isFirst = true;

    public VideoFullDialog(@NonNull Context context, CustomVideoView videoView, AdValue instance, int position) {
        super(context, R.style.dialog_full_screen);//通过style设置，保证我们的Dialog全屏
        mXAdInstance = instance;
        mVideoView = videoView;
        mPosition = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.xadsdk_dialog_video_layout);
        initVideoView();
    }

    private void initVideoView() {
        mParentView = (RelativeLayout) findViewById(R.id.content_layout);
        mBackButton = (ImageView) findViewById(R.id.xadsdk_player_close_btn);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBackBtn();
            }
        });
        mVideoView.setListener(this);
        mVideoView.mute(false);
        mParentView.addView(mVideoView);
    }

    /**
     * 全屏返关闭按钮点击事件
     */
    private void clickBackBtn() {
        dismiss();
        if (mListener != null){
            mListener.getCurrentPlayPosition(mVideoView.getCurrentPosition());
        }
    }

    @Override
    public void onBackPressed() {
        clickBackBtn();
        super.onBackPressed();
    }

    /**
     * 焦点状态改变是的回调
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus){
            //未获取焦点时逻辑
            mPosition = mVideoView.getCurrentPosition();         
            mVideoView.pause();
        }else{
            if (isFirst){
                mVideoView.seekAndResume(mPosition);
            }else{
                mVideoView.resume();
            }
            //取得焦点时的逻辑
            mVideoView.resume();
        }

        isFirst = false;
    }

    /**
     * dialog销毁的时候调用
     */
    @Override
    public void dismiss() {
        mParentView.removeView(mVideoView);
        super.dismiss();
    }

    /************************************************
     * 实现了ADVideoPlayerListener接口中的方法
     * @param time
     *************************************************/
    @Override
    public void onBufferUpdate(int time) {

    }

    @Override
    public void onClickFullScreenBtn() {

    }

    @Override
    public void onClickVideo() {

    }

    @Override
    public void onClickBackTBtn() {

    }

    @Override
    public void onClickPlay() {

    }

    @Override
    public void onAdVideoLoadSuccess() {

    }

    @Override
    public void onAdVideoLoadFailed() {

    }

    @Override
    public void onAdVideoComplete() {
        dismiss();
        if (mListener != null){
            mListener.playComplete();
        }
    }

    /**
     * 注入事件监听类
     * @param listener
     */
    public void setListener(FullToSmallListener listener) {
        mListener = listener;
    }

    public interface FullToSmallListener{
        /**
         *  全屏播放中点击关闭按钮或者点击Back键是的回调
         * @param position
         */
        void getCurrentPlayPosition(int position);

        /**
         *  全屏播放结束是回调
         */
        void playComplete();
    }
}
