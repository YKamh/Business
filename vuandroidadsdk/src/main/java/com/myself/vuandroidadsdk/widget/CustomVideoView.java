package com.myself.vuandroidadsdk.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
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

    /**
     * 在播放器播放完成之后回调的一个方法
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {

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
        return false;
    }

    /**
     * 播放器处于就绪状态
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    /**
     * 表示TextureView处于就绪状态
     * @param surface
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

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
    public void load(){

    }

    /**
     * 暂停视频
     */
    public void pause(){

    }

    /**
     * 恢复视频播放
     */
    public void resume(){

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
//                            pause();
                        } else {
//                            decideCanPlay();
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

}
