package com.myself.business.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myself.business.R;
import com.myself.business.activity.base.BaseActivity;

import java.util.ArrayList;

/**
 * Created by Kamh on 2018/7/4.
 */

public class PhotoViewActivity extends BaseActivity implements View.OnClickListener {

    /**
     * UI
     */
    private ViewPager mViewPager;
    private TextView mIndictorView;
    private ImageView mShareView;

    private ArrayList<String> mPhoroList;
    private int mLength;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view_layout);
        initView();
    }

    private void initView() {

    }


    private void initData(){
        mPhoroList = getIntent().getStringArrayListExtra("");
    }

    @Override
    public void onClick(View v) {

    }
}
