package com.myself.business.view.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myself.business.R;
import com.myself.business.adapter.PhotoPagerAdapter;
import com.myself.business.model.recommand.RecommandHeadValue;
import com.myself.business.view.viewpagerindictor.CirclePageIndicator;
import com.myself.vuandroidadsdk.imageloader.ImageLoaderManager;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by Kamh on 2018/6/7.
 */

public class HomeHeaderLayout extends RelativeLayout {

    private Context mContext;
    /**
     * UI
     */
    private RelativeLayout mRootView;
    private AutoScrollViewPager mViewPager;
    private CirclePageIndicator mPagerIndictor;
    private ImageView[] mImageViews = new ImageView[4];
    private TextView mHotView;
    private PhotoPagerAdapter mAdapter;
    private LinearLayout mFootLayout;
    /**
     * Data
     */
    private RecommandHeadValue mHeadValue;
    private ImageLoaderManager mImageLoader;

    public HomeHeaderLayout(Context context, RecommandHeadValue headValue) {
        this(context, null, headValue);
    }

    public HomeHeaderLayout(Context context, AttributeSet attrs, RecommandHeadValue headValue) {
        super(context, attrs);
        mContext = context;
        mHeadValue = headValue;
        mImageLoader = ImageLoaderManager.getInstance(mContext);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRootView = (RelativeLayout) inflater.inflate(R.layout.listview_home_head_layout, this);
        mViewPager = (AutoScrollViewPager) mRootView.findViewById(R.id.pager);
        mPagerIndictor = (CirclePageIndicator) mRootView.findViewById(R.id.pager_indictor_view);
        mHotView = (TextView) mRootView.findViewById(R.id.zuixing_view);
        mImageViews[0] = (ImageView) mRootView.findViewById(R.id.head_image_one);
        mImageViews[1] = (ImageView) mRootView.findViewById(R.id.head_image_two);
        mImageViews[2] = (ImageView) mRootView.findViewById(R.id.head_image_three);
        mImageViews[3] = (ImageView) mRootView.findViewById(R.id.head_image_four);
        mFootLayout = (LinearLayout) mRootView.findViewById(R.id.content_layout);

        mAdapter = new PhotoPagerAdapter(mContext, mHeadValue.ads, true);
        mViewPager.setAdapter(mAdapter);
        mViewPager.startAutoScroll(3000);
        mPagerIndictor.setViewPager(mViewPager);

        for (int i = 0; i < mImageViews.length; i++){
            mImageLoader.displayImage(mImageViews[i], mHeadValue.middle.get(i));
        }
    }


}
