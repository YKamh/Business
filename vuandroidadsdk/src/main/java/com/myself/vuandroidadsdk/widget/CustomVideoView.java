package com.myself.vuandroidadsdk.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.os.Handler;

import com.myself.vuandroidadsdk.R;
import com.myself.vuandroidadsdk.adutil.LogUtils;
import com.myself.vuandroidadsdk.constant.SDKConstant;

/**
 * Created by Kamh on 2018/6/12.
 * @function 负责视频播放，暂停以及各类事件的出发
 */

public class CustomVideoView extends RelativeLayout implements View.OnClickListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener, TextureView.SurfaceTextureListener{

    /**
     * Constant
     */
    private static final String TAG = CustomVideoView.class.getName();
    private static final int TIME_MSG = 0x01;
    private static final int TIME_INVAL = 1000;

    //播放器生命周期状态
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PLAYING = 1;
    private static final int STATE_PAUSING = 2;
    //加载重试次数
    private static final int LOAD_TOTAL_COUNT = 3;

    /**
     * UI
     */
    private ViewGroup mParentContainer;
    private RelativeLayout mPlayerView;
    private TextureView mVideoView;
    private Button mMiniPlayButton;
    private ImageView mFullBtn;
    private ImageView mLoadingBar;
    private ImageView mFrameView;
    private AudioManager mAudioManager;//音量控制器
    private Surface videoSurface;//真正显示帧数据的类

    /**
     * DATA
     */
    private String mUrl;//要加载的视频地址
    private String mFrameURI;
    private boolean isMute;//是否静音
    private int mScreenWidth, mDestationHeight;

    /**
     * Status状态保护
     */
    private boolean mIsRealPause;
    private boolean mIsComplete;
    private int mCurrentCount;
    private int playerState = STATE_IDLE;//默认处于空闲状态

    private MediaPlayer mMediaPlayer;
    private ADVideoPlayerListener mListener;
    private ScreenEventReceiver mEventReceiver;
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case TIME_MSG:
                    if (isPlaying()) {
                        //还可以在这里更新progressbar
                        //LogUtils.i(TAG, "TIME_MSG");
                        mListener.onBufferUpdate(getCurrentPosition());
                        sendEmptyMessageDelayed(TIME_MSG, TIME_INVAL);
                    }
                    break;
            }
        }
    };
    private int mCurrentPlayState;
    private boolean mIsPausedClicked;

    private ADFrameImageLoadListener mFrameLoadListener;

    public CustomVideoView(Context context, ViewGroup parentContainer){
        super(context);
        mParentContainer = parentContainer;
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        initData();
        initView();
        registerBroadcastReceiver();
    }

    private void registerBroadcastReceiver() {

    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        mPlayerView = (RelativeLayout) inflater.inflate(R.layout.xadsdk_video_player, this);
        mVideoView = (TextureView) mPlayerView.findViewById(R.id.xadsdk_player_video_textureView);
        mVideoView.setOnClickListener(this);
        mVideoView.setKeepScreenOn(true);
        mVideoView.setSurfaceTextureListener(this);
        initSmallLayoutMode(); //init the small mode
    }

    private void initSmallLayoutMode() {

    }

    private void initData() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mDestationHeight = (int) (mScreenWidth * SDKConstant.VIDEO_HEIGHT_PERCENT);
    }

    public boolean isPlaying() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            return true;
        }
        return false;
    }

    public int getCurrentPosition() {
        if (this.mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    public void setDataSource(String url) {
        this.mUrl = url;
    }

    public void setFrameURI(String url) {
        mFrameURI = url;
    }

    public void setFrameLoadListener(ADFrameImageLoadListener frameLoadListener) {
        this.mFrameLoadListener = frameLoadListener;
    }

    /**
     * 在播放器播放完成之后回调的一个方法
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mListener != null){
            mListener.onAdVideoComplete();
        }
        setIsComplete(true);
        setIsPausedClicked(true);
        playBack();//回到初始状态
    }

    private void playBack() {
        setCurrentPlayState(STATE_PAUSING);
        mHandler.removeCallbacksAndMessages(null);
        if (mMediaPlayer != null){
            mMediaPlayer.setOnSeekCompleteListener(null);
            mMediaPlayer.seekTo(0);
            mMediaPlayer.pause();
        }
        showPauseOrPlayView(false);
    }

    /**
     * 播放器产生异常的时候回调
     * @param mp
     * @param what
     * @param extra
     * @return
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        this.playerState = STATE_IDLE;
        if (mCurrentCount >= LOAD_TOTAL_COUNT){
            if (mListener != null){
                mListener.onAdVideoLoadFailed();
            }
            showPauseOrPlayView(false);
        }
        stop();
        return true;
    }

    /**
     * 播放器处于就绪状态
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer = mp;
        if (mMediaPlayer != null){
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mCurrentCount = 0;
            if (mListener != null){
                mListener.onAdVideoComplete();
            }

            decideCanPlay();
        }
    }

    public int getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    public void setIsRealPause(boolean isRealPause) {
        this.mIsRealPause = isRealPause;
    }

    public void destroy() {
        LogUtils.d(TAG, " do destroy");
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setOnSeekCompleteListener(null);
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
        setCurrentPlayState(STATE_IDLE);
        mCurrentCount = 0;
        setIsComplete(false);
        setIsRealPause(false);
        unRegisterBroadcastReceiver();
        mHandler.removeCallbacksAndMessages(null); //release all message and runnable
        showPauseView(false); //除了播放和loading外其余任何状态都显示pause
    }

    private void unRegisterBroadcastReceiver() {
        if (mEventReceiver != null) {
            getContext().unregisterReceiver(mEventReceiver);
        }
    }

    private void showPauseView(boolean show) {
        mFullBtn.setVisibility(show ? View.VISIBLE : View.GONE);
        mMiniPlayButton.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoadingBar.clearAnimation();
        mLoadingBar.setVisibility(View.GONE);
        if (!show) {
            mFrameView.setVisibility(View.VISIBLE);
            loadFrameImage();
        } else {
            mFrameView.setVisibility(View.GONE);
        }
    }

    private void decideCanPlay() {
//        if (Utils.canAutoPlay(getContext(), SDKConstant.getCurrentSetting())
//                && Utils.getVisiblePercent(mParentContainer) >=
//                SDKConstant.VIDEO_SCREEN_PERCENT){
//            //来回滑动页面时，只有 》 50
//            resume();
//        }else{
//            pause();
//        }
    }

    /**
     * 表示TextureView处于就绪状态
     * @param surface
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        videoSurface = new Surface(surface);
        load();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 加载视频Url
     */
    public void load() {
        if (this.playerState != STATE_IDLE) {
            return;
        }

        try {
            showLoadingView();
            setCurrentPlayState(STATE_IDLE);
            checkMediaPlayer();//完成播放器的创建工作
            mMediaPlayer.setDataSource(mUrl);
            mMediaPlayer.prepareAsync();//异步开始加载视频
        } catch (Exception e) {
            stop();
        }
    }

    /**
     * 暂停视频
     */
    public void pause(){
        if (this.playerState != STATE_PLAYING){
            return;
        }
        setCurrentPlayState(STATE_PAUSING);
        if (isPlaying()){
            mMediaPlayer.pause();
        }
        showPauseOrPlayView(false);
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 恢复视频播放
     */
    public void resume(){
        if (playerState != STATE_PAUSING){
            return;
        }
        if (!isPlaying()){
            entryResumeState();//置为播放中的状态值
            showPauseOrPlayView(true);
            mMediaPlayer.start();
            mHandler.sendEmptyMessage(TIME_MSG);
        }
    }

    private void entryResumeState() {
        setCurrentPlayState(STATE_PLAYING);
        setIsPausedClicked(false);
        setIsComplete(false);
    }

    /**
     * 播放完成后回到初始状态
     */
    public void palyBack(){

    }

    /**
     * 视频处于停滞状态
     */
    public void stop(){
        Log.d(TAG, "stop(): do stop");
        if (this.mMediaPlayer != null){
            this.mMediaPlayer.reset();
            this.mMediaPlayer.setOnSeekCompleteListener(null);
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
        mHandler.removeCallbacksAndMessages(null);
        setCurrentPlayState(STATE_IDLE);

        //去重新Load
        if (mCurrentCount < LOAD_TOTAL_COUNT){
            mCurrentCount += 1;
            load();
        }else{
            //停止重试
            showPauseOrPlayView(false);
        }
    }

    private void showPauseOrPlayView(boolean show) {
        mFullBtn.setVisibility(show ? View.VISIBLE : View.GONE);
        mMiniPlayButton.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoadingBar.clearAnimation();
        mLoadingBar.setVisibility(View.GONE);
        if (!show) {
            mFrameView.setVisibility(View.VISIBLE);
            loadFrameImage();
        } else {
            mFrameView.setVisibility(View.GONE);
        }
    }

    /**
     * 销毁当前的自定义View
     */
    public void destory(){

    }

    /**
     * 调到指定点播放视频
     * @param position
     */
    public void seekAndResume(int position){

    }

    /**
     * 调到指定点暂停视频
     * @param position
     */
    public void seekAndPause(int position){
        if (this.playerState != STATE_PLAYING) {
            return;
        }
        showPauseView(false);
        setCurrentPlayState(STATE_PAUSING);
        if (isPlaying()) {
            mMediaPlayer.seekTo(position);
            mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    LogUtils.d(TAG, "do seek and pause");
                    mMediaPlayer.pause();
                    mHandler.removeCallbacksAndMessages(null);
                }
            });
        }
    }

    private void showLoadingView() {
        mFullBtn.setVisibility(GONE);
        mLoadingBar.setVisibility(VISIBLE);
        AnimationDrawable anim = (AnimationDrawable) mLoadingBar.getBackground();
        anim.start();
        mMiniPlayButton.setVisibility(GONE);
        mFrameView.setVisibility(GONE);
        loadFrameImage();
    }

    private void loadFrameImage() {

    }

    public void setListener(ADVideoPlayerListener listener){
        mListener = listener;
    }

    private synchronized  void checkMediaPlayer(){
        if (mMediaPlayer == null){
            mMediaPlayer = createMediaPlayer();
        }
    }

    private MediaPlayer createMediaPlayer() {
        return mMediaPlayer;
    }

    public void setCurrentPlayState(int currentPlayState) {
        mCurrentPlayState = currentPlayState;
    }

    public void setIsPausedClicked(boolean isPausedClicked) {
        mIsPausedClicked = isPausedClicked;
    }

    public boolean isRealPause() {
        return mIsRealPause;
    }

    public boolean isComplete() {
        return mIsComplete;
    }

    public void setIsComplete(boolean isComplete) {
        mIsComplete = isComplete;
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE && playerState == STATE_PAUSING) {
            if (isRealPause() || isComplete()) {
                pause();
            } else {
                decideCanPlay();
            }
        } else {
            pause();
        }
    }

    /**
     * 监听锁屏事件的广播接收器
     */
    private class ScreenEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //主动锁屏时 pause, 主动解锁屏幕时，resume
            switch (intent.getAction()) {
                case Intent.ACTION_USER_PRESENT:
                    if (playerState == STATE_PAUSING) {
                        if (mIsRealPause) {
                            //手动点的暂停，回来后还暂停
                            pause();
                        } else {
                            decideCanPlay();
                        }
                    }
                    break;
                case Intent.ACTION_SCREEN_OFF:
                    if (playerState == STATE_PLAYING) {
//                        pause();
                    }
                    break;
            }
        }
    }

    public interface ADFrameImageLoadListener {

        void onStartFrameLoad(String url, ImageLoaderListener listener);
    }

    public interface ImageLoaderListener {
        /**
         * 如果图片下载不成功，传null
         *
         * @param loadedImage
         */
        void onLoadingComplete(Bitmap loadedImage);
    }
}
