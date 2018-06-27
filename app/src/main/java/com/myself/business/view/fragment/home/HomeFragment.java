package com.myself.business.view.fragment.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.myself.business.R;
import com.myself.business.adapter.CourseAdapter;
import com.myself.business.model.recommand.BaseRecommandModel;
import com.myself.business.network.http.RequestCenter;
import com.myself.business.view.fragment.BaseFragment;
import com.myself.business.view.home.HomeHeaderLayout;
import com.myself.business.zxing.app.CaptureActivity;
import com.myself.vuandroidadsdk.okhttp.listener.DisposeDataListener;

/**
 * Created by Kamh on 2018/5/29.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener{
    private static final String TAG = HomeFragment.class.getName();

    private static final int REQUEST_QRCODE = 0x01;
    /**
     * UI
     */
    private View mContentView;
    private ListView mListView;
    private TextView mCategroyView;
    private TextView mSearchView;
    private ImageView mLoadingView;

    /**
     * Data
     */
    private CourseAdapter mAdapter;
    private BaseRecommandModel mRecommandModel;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestRecommandData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_home_layout, container, false);
        initView();
        return mContentView;
    }

    private void initView() {
        mCategroyView = (TextView) mContentView.findViewById(R.id.category_view);
        mCategroyView.setOnClickListener(this);
        mSearchView = (TextView) mContentView.findViewById(R.id.qrcode_view);
        mSearchView.setOnClickListener(this);
        mListView = (ListView) mContentView.findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);
        mLoadingView = (ImageView) mContentView.findViewById(R.id.loading_view);
        AnimationDrawable anim = (AnimationDrawable) mLoadingView.getDrawable();
        anim.start();
    }

    /**
     * 发送首页列表数据请求
     */
    private void requestRecommandData() {
        RequestCenter.requestRecommandData(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.d(TAG, "onSuccess:" + responseObj.toString());
                mRecommandModel = (BaseRecommandModel) responseObj;
                showSuccessView();
            }

            @Override
            public void onFailure(Object reasonObj) {
                Log.e(TAG, "onFailure:" + reasonObj.toString());
            }
        });
    }

    /**
     * 请求成功执行的方法
     */
    private void showSuccessView() {
        if (mRecommandModel.data.list != null && mRecommandModel.data.list.size() > 0){
            mLoadingView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            //为ListView添加头布局
            mListView.addHeaderView(new HomeHeaderLayout(mContext, mRecommandModel.data.head));
            mAdapter = new CourseAdapter(mContext, mRecommandModel.data.list);
            mListView.setAdapter(mAdapter);
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    mAdapter.updateAdInScrollView();
                }
            });
        }else{
            showErrorView();
        }
    }

    /**
     * 请求失败执行的方法
     */
    private void showErrorView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.qrcode_view:
                Intent intent = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_QRCODE);
                break;
            case R.id.category_view:
                break;
            case R.id.search_view:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_QRCODE:
                if (resultCode == Activity.RESULT_OK){
                    String code = data.getStringExtra("SCAN_RESULT");

                }
                break;
        }
    }
}
