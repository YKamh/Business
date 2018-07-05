package com.myself.business.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myself.business.R;
import com.myself.business.activity.base.BaseActivity;
import com.myself.business.adapter.PhotoPagerAdapter;
import com.myself.business.adutil.Utils;

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

    /**
     * DATA
     */
    private ArrayList<String> mPhoroList;
    private int mLength;
    private PhotoPagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view_layout);
        initData();
        initView();
    }

    private void initView() {
        mIndictorView = (TextView) findViewById(R.id.indictor_view);
        mIndictorView.setText("1/" + mLength);
        mShareView = (ImageView) findViewById(R.id.share_view);
        mShareView.setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.photo_pager);
        mViewPager.setPageMargin(Utils.dip2px(this, 30));

        mAdapter = new PhotoPagerAdapter(this, mPhoroList, false);
        mViewPager.setAdapter(mAdapter);
    }

    public static final String PHOTO_LIST = "photo_list";
    private void initData(){
        mPhoroList = getIntent().getStringArrayListExtra("");
        mLength = mPhoroList.size();
    }

    @Override
    public void onClick(View v) {

    }
}
