package com.myself.business.view.course;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myself.business.R;
import com.myself.business.activity.CourseDetailActivity;
import com.myself.business.model.course.CourseFooterRecommandValue;
import com.myself.business.model.course.CourseFooterValue;
import com.myself.vuandroidadsdk.adutil.ImageLoaderUtil;

/**
 * Created by Kamh on 2018/7/15.
 */

public class CourseDetailFooterView extends RelativeLayout {

    private Context mContext;
    /**
     * UI
     */
    private RelativeLayout mRootView;
    /**
     * 图表相关UI
     */
    private LinearLayout contentLayout;
//    private LineChart lineView;
    private Resources resource;
    private float yAxisMax = -1.0f;
    private float yAxisMin = 100.0f;
    private float yAxisGap = 10f;
    private int yAxislabelNum = 5;
    /**
     * 推荐相关UI
     */
    private ImageView[] mImageViews = new ImageView[2];
    private TextView[] mNameViews = new TextView[2];
    private TextView[] mPriceViews = new TextView[2];
    private TextView[] mZanViews = new TextView[2];
    /**
     * data
     */
    private ImageLoaderUtil mImageLoader;
    private CourseFooterValue mFooterValue;

    public CourseDetailFooterView(Context context, CourseFooterValue footerValue) {
        this(context, null, footerValue);
    }

    public CourseDetailFooterView(Context context, AttributeSet attrs, CourseFooterValue footerValue) {
        super(context, attrs);
        mContext = context;
        resource = mContext.getResources();
        mImageLoader = ImageLoaderUtil.getInstance(mContext);
        mFooterValue = footerValue;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRootView = (RelativeLayout) inflater.inflate(R.layout.listview_course_comment_footer_layout, this);
        contentLayout = (LinearLayout) mRootView.findViewById(R.id.line_Layout);
        mImageViews[0] = (ImageView) mRootView.findViewById(R.id.image_one_view);
        mImageViews[1] = (ImageView) mRootView.findViewById(R.id.image_two_view);
        mNameViews[0] = (TextView) mRootView.findViewById(R.id.name_one_view);
        mNameViews[1] = (TextView) mRootView.findViewById(R.id.name_two_view);
        mPriceViews[0] = (TextView) mRootView.findViewById(R.id.price_one_view);
        mPriceViews[1] = (TextView) mRootView.findViewById(R.id.price_two_view);
        mZanViews[0] = (TextView) mRootView.findViewById(R.id.zan_one_view);
        mZanViews[1] = (TextView) mRootView.findViewById(R.id.zan_two_view);

        for (int i = 0; i < mFooterValue.recommand.size(); i++) {
            final CourseFooterRecommandValue value = mFooterValue.recommand.get(i);
            mImageLoader.displayImage(mImageViews[i], value.imageUrl);
            mImageViews[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CourseDetailActivity.class);
                    intent.putExtra(CourseDetailActivity.COURSE_ID, value.courseId);
                    mContext.startActivity(intent);
                }
            });
            mNameViews[i].setText(value.name);
            mPriceViews[i].setText(value.price);
            mZanViews[i].setText(value.zan);
        }
        addLineChartView();
    }

    /**
     * 获取最大最小值
     *
     * @param currentNum
     */
    private void initMaxMin(float currentNum) {
        if (currentNum >= yAxisMax) {
            yAxisMax = currentNum;
        }
        if (currentNum < yAxisMin) {
            yAxisMin = currentNum;
        }
    }

    private void addLineChartView() {

    }
}
