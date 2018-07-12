package com.myself.business.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.myself.business.R;
import com.myself.business.activity.base.BaseActivity;
import com.myself.business.adapter.CourseCommentAdapter;
import com.myself.business.model.course.BaseCourseModel;
import com.myself.business.network.http.RequestCenter;
import com.myself.vuandroidadsdk.okhttp.listener.DisposeDataListener;

/**
 * Created by Kamh on 2018/6/6.
 */

public class CourseDetailActivity extends BaseActivity implements View.OnClickListener{

    public static String COURSE_ID = "courseID";

    /**
     * UI
     */
    private ImageView mBackView;
    private ListView mListView;
    private ImageView mLoadingView;

    /**
     * DATA
     */
    private CourseCommentAdapter mAdapter;
    private String mCourseID;
    private BaseCourseModel mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail_layout);
        initData();
        initView();
    }

    private void initData(){
        mCourseID = getIntent().getStringExtra(COURSE_ID);

    }

    //发送请求
    private void requestDetails(){
        RequestCenter.requestCourseDetails(mCourseID, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mData = (BaseCourseModel) responseObj;
                upDataUI();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    private void upDataUI() {
        mLoadingView.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        mAdapter = new CourseCommentAdapter(this, mData.data.body);
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.back_view);
        mBackView.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.comment_list_view);
        mListView.setVisibility(View.GONE);
        mLoadingView = (ImageView) findViewById(R.id.loading_view);
        mLoadingView.setVisibility(View.VISIBLE);

        AnimationDrawable anim = (AnimationDrawable) mLoadingView.getDrawable();
        anim.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_view:
                break;
            default:
                break;
        }
    }
}
