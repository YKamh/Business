package com.myself.business.share;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myself.business.R;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by Kamh on 2018/7/9.
 */

public class ShareDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private DisplayMetrics dm;

    private RelativeLayout mWeixinLayout;
    private RelativeLayout mWeixinMomentLayout;
    private RelativeLayout mQQLayout;
    private RelativeLayout mQZoneLayout;
    private RelativeLayout mDownloadLayout;
    private TextView mCancelView;

    /**
     * 当前支持的数据类型
     */
    private int mShareType;
    private String mShareTitle;
    private String mShareText;
    private String mSharePhoto;
    private String mShareTitleUrl;
    private String mShareSite;
    private String mShareSiteUrl;
    private String mUrl;

    public ShareDialog(@NonNull Context context) {
        super(context, R.style.SheetDialogStyle);
        dm = mContext.getResources().getDisplayMetrics();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_layout);
        initView();
    }

    private void initView() {
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = dm.widthPixels;
        dialogWindow.setAttributes(lp);

        mWeixinLayout  = (RelativeLayout) findViewById(R.id.weixin_layout);
        mWeixinLayout.setOnClickListener(this);
        mWeixinMomentLayout = (RelativeLayout) findViewById(R.id.moment_layout);
        mWeixinMomentLayout.setOnClickListener(this);
        mQQLayout = (RelativeLayout) findViewById(R.id.qq_layout);
        mQQLayout.setOnClickListener(this);
        mQZoneLayout = (RelativeLayout) findViewById(R.id.qzone_layout);
        mQZoneLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.weixin_layout:
                share(PlatFromType.WeChat);
                break;
            case R.id.moment_layout:
                share(PlatFromType.WeChatMoment);
                break;
            case R.id.qq_layout:
                share(PlatFromType.QQ);
                break;
            case R.id.qzone_layout:
                share(PlatFromType.QZONE);
                dismiss();
                break;
            default:
                break;
        }
    }

    public void setShareType(int shareType) {
        mShareType = shareType;
    }

    public void setShareTitle(String shareTitle) {
        mShareTitle = shareTitle;
    }

    public void setShareText(String shareText) {
        mShareText = shareText;
    }

    public void setSharePhoto(String sharePhoto) {
        mSharePhoto = sharePhoto;
    }

    public void setShareTitleUrl(String shareTitleUrl) {
        mShareTitleUrl = shareTitleUrl;
    }

    public void setShareSite(String shareSite) {
        mShareSite = shareSite;
    }

    public void setShareSiteUrl(String shareSiteUrl) {
        mShareSiteUrl = shareSiteUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    //完成分享的方法
    private void share(PlatFromType type) {
        //封装ShareData
        ShareData data = new ShareData();
        Platform.ShareParams params = new Platform.ShareParams();
        params.setShareType(mShareType);
        params.setTitle(mShareTitle);
        params.setTitleUrl(mShareTitleUrl);
        params.setText(mShareText);
        params.setImagePath(mSharePhoto);
        params.setSite(mShareSite);
        params.setSiteUrl(mShareSiteUrl);
        params.setUrl(mUrl);

        data.mShareParams = params;
        data.mType = type;

        ShareManager.getInstance().shareData(data, mListener);
    }

    private PlatformActionListener mListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(Platform platform, int i) {

        }
    };
}
