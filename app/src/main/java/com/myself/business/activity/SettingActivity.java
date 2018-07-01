package com.myself.business.activity;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.myself.business.R;
import com.myself.business.activity.base.BaseActivity;
import com.myself.business.db.SPManager;
import com.myself.vuandroidadsdk.constant.SDKConstant;
import com.myself.vuandroidadsdk.core.AdParameters;

/**
 * Created by Kamh on 2018/7/1.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout mWifiLayout;
    private RelativeLayout mAlwaylayout;
    private RelativeLayout mNeverLayout;
    private CheckBox mWifiBox, mAlwayBox, mNeverBox;
    private ImageView mBackView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_layout);

        initView();
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.back_view);
        mWifiLayout = (RelativeLayout) findViewById(R.id.wifi_layout);
        mWifiBox = (CheckBox) findViewById(R.id.wifi_check_box);
        mAlwaylayout = (RelativeLayout) findViewById(R.id.alway_layout);
        mAlwayBox = (CheckBox) findViewById(R.id.alway_check_box);
        mNeverLayout = (RelativeLayout) findViewById(R.id.close_layout);
        mNeverBox = (CheckBox) findViewById(R.id.close_check_box);

        mBackView.setOnClickListener(this);
        mWifiLayout.setOnClickListener(this);
        mAlwaylayout.setOnClickListener(this);
        mNeverLayout.setOnClickListener(this);

        int currentSetting = SPManager.getInstance().getInt(SPManager.VIDEO_SETTING, 1);
        switch (currentSetting){
            case 0:
                mAlwayBox.setBackgroundResource(R.drawable.setting_selected);
                mWifiBox.setBackgroundResource(0);
                mNeverBox.setBackgroundResource(0);
                break;
            case 1:
                mAlwayBox.setBackgroundResource(0);
                mWifiBox.setBackgroundResource(R.drawable.setting_selected);
                mNeverBox.setBackgroundResource(0);
                break;
            case 2:
                mAlwayBox.setBackgroundResource(0);
                mWifiBox.setBackgroundResource(0);
                mNeverBox.setBackgroundResource(R.drawable.setting_selected);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_view:
                finish();
                break;
            case R.id.wifi_layout:
                SPManager.getInstance().putInt(SPManager.VIDEO_SETTING, 1);
                //通知当前设置到视频播放SDK
                AdParameters.setCurrentSetting(SDKConstant.AutoPlaySetting.AUTO_PLAY_ONLY_WIFI);
                mAlwayBox.setBackgroundResource(R.drawable.setting_selected);
                mWifiBox.setBackgroundResource(0);
                mNeverBox.setBackgroundResource(0);
                break;
            case R.id.alway_layout:
                SPManager.getInstance().putInt(SPManager.VIDEO_SETTING, 0);
                //通知当前设置到视频播放SDK
                AdParameters.setCurrentSetting(SDKConstant.AutoPlaySetting.AUTO_PLAY_3G_4G_WIFI);
                mAlwayBox.setBackgroundResource(R.drawable.setting_selected);
                mWifiBox.setBackgroundResource(0);
                mNeverBox.setBackgroundResource(0);
                break;
            case R.id.close_layout:
                SPManager.getInstance().putInt(SPManager.VIDEO_SETTING, 2);
                //通知当前设置到视频播放SDK
                AdParameters.setCurrentSetting(SDKConstant.AutoPlaySetting.AUTO_PLAY_NEVER);
                mAlwayBox.setBackgroundResource(R.drawable.setting_selected);
                mWifiBox.setBackgroundResource(0);
                mNeverBox.setBackgroundResource(0);
                break;
            default:
                break;
        }
    }
}
