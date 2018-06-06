package com.myself.business.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myself.business.R;
import com.myself.business.adutil.Utils;
import com.myself.business.model.recommand.RecommandBodyValue;
import com.myself.business.util.Util;
import com.myself.vuandroidadsdk.imageloader.ImageLoaderManager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Kamh on 2018/6/5.
 */

public class CourseAdapter extends BaseAdapter {

    /**
     * ListView 不同类型的Item标识
     */
    private static final int CARD_COUNT = 4;
    private static final int VIDOE_TYPE = 0x00;
    private static final int CARD_SIGNAL_PIC = 0x01;
    private static final int CARD_MULTI_PIC = 0x02;
    private static final int CARD_VIEW_PAGE = 0x03;

    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<RecommandBodyValue> mList;
    private ViewHolder mViewHolder;
    //    private VideoAdContext mVideoAdContext;
    private ImageLoaderManager mImageLoaderManager;

    /**
     * 构造方法，传入上下文和数据
     *
     * @param context
     * @param list
     */
    public CourseAdapter(Context context, ArrayList<RecommandBodyValue> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
        mImageLoaderManager = ImageLoaderManager.getInstance(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return CARD_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        RecommandBodyValue value = (RecommandBodyValue) getItem(position);
        return value.type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        final RecommandBodyValue value = (RecommandBodyValue) getItem(position);
        if (convertView == null) {
            switch (type) {
                case VIDOE_TYPE:
                    mViewHolder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.item_video_layout, parent, false);
                    mViewHolder.mVideoContentLayout = (RelativeLayout) convertView.findViewById(R.id.video_ad_layout);
                    mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.item_title_view);
                    mViewHolder.mInfoView = (TextView) convertView.findViewById(R.id.item_info_view);
                    mViewHolder.mFooterView = (TextView) convertView.findViewById(R.id.item_footer_view);
                    mViewHolder.mShareView = (ImageView) convertView.findViewById(R.id.item_share_view);
                    //为对应布局创建播放器

                    break;
                case CARD_SIGNAL_PIC:
                    mViewHolder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.item_product_card_one_layout, parent, false);
                    mViewHolder.mLogoView = (CircleImageView) convertView.findViewById(R.id.item_logo_view);
                    mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.item_title_view);
                    mViewHolder.mInfoView = (TextView) convertView.findViewById(R.id.item_info_view);
                    mViewHolder.mFooterView = (TextView) convertView.findViewById(R.id.item_footer_view);
                    mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.item_price_view);
                    mViewHolder.mFromView = (TextView) convertView.findViewById(R.id.item_from_view);
                    mViewHolder.mLikeView = (TextView) convertView.findViewById(R.id.item_zan_view);
                    mViewHolder.mProductLayout = (LinearLayout) convertView.findViewById(R.id.product_photo_layout);
                    break;
                case CARD_MULTI_PIC:
                    mViewHolder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.item_product_card_two_layout, parent, false);
                    mViewHolder.mLogoView = (CircleImageView) convertView.findViewById(R.id.item_logo_view);
                    mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.item_title_view);
                    mViewHolder.mInfoView = (TextView) convertView.findViewById(R.id.item_info_view);
                    mViewHolder.mFooterView = (TextView) convertView.findViewById(R.id.item_footer_view);
                    mViewHolder.mProductView = (ImageView) convertView.findViewById(R.id.product_photo_view);
                    mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.item_price_view);
                    mViewHolder.mFromView = (TextView) convertView.findViewById(R.id.item_from_view);
                    mViewHolder.mLikeView = (TextView) convertView.findViewById(R.id.item_zan_view);
                    break;
                case CARD_VIEW_PAGE:
                    mViewHolder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.item_product_card_three_layout, parent, false);
                    mViewHolder.mViewPager = (ViewPager) convertView.findViewById(R.id.pager);
                    mViewHolder.mViewPager.setPageMargin(Utils.dip2px(mContext, 12));
                    ArrayList<RecommandBodyValue> recommandBodyValues = Util.handleData(value);
                    mViewHolder.mViewPager.setAdapter(new HotSalePagerAdapter(mContext, recommandBodyValues));
                    //一开始ViewPager处于一个比较靠中间的Item项
                    mViewHolder.mViewPager.setCurrentItem(recommandBodyValues.size() * 100);
                    break;
                default:
                    break;
            }
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        //开始绑定数据
        switch (type) {
            case CARD_SIGNAL_PIC:
                mViewHolder.mTitleView.setText(value.title);
                mViewHolder.mInfoView.setText(value.info.concat(mContext.getString(R.string.tian_qian)));
                mViewHolder.mFooterView.setText(value.text);
                mViewHolder.mPriceView.setText(value.price);
                mViewHolder.mFromView.setText(value.from);
                mViewHolder.mLikeView.setText(mContext.getString(R.string.dian_zan).concat(value.zan));
                mImageLoaderManager.displayImage(mViewHolder.mLogoView, value.logo);
                mImageLoaderManager.displayImage(mViewHolder.mProductView, value.url.get(0));
                break;
            case CARD_VIEW_PAGE:
                mViewHolder.mTitleView.setText(value.title);
                mViewHolder.mInfoView.setText(value.info.concat(mContext.getString(R.string.tian_qian)));
                mViewHolder.mFooterView.setText(value.text);
                mViewHolder.mPriceView.setText(value.price);
                mViewHolder.mFromView.setText(value.from);
                mViewHolder.mLikeView.setText(mContext.getString(R.string.dian_zan).concat(value.zan));
                mImageLoaderManager.displayImage(mViewHolder.mLogoView, value.logo);
                mViewHolder.mProductLayout.removeAllViews();
                for (String url : value.url) {
                    mViewHolder.mProductLayout.addView(createImageView(url));
                }
                break;
        }
        return null;
    }

    private ImageView createImageView(String url){
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utils.dip2px(mContext, 100), ViewGroup.LayoutParams.MATCH_PARENT);
        params.leftMargin = Utils.dip2px(mContext, 5);
        imageView.setLayoutParams(params);
        mImageLoaderManager.displayImage(imageView, url);
        return imageView;
    }

    private static class ViewHolder {
        //所有card的共有属性
        private CircleImageView mLogoView;
        private TextView mTitleView;
        private TextView mInfoView;
        private TextView mFooterView;
        //Video Card特有的属性
        private RelativeLayout mVideoContentLayout;
        private ImageView mShareView;

        //Video Card外所有的Card具有属性
        private TextView mPriceView;
        private TextView mFromView;
        private TextView mLikeView;
        //Card One特有的属性
        private LinearLayout mProductLayout;
        //Card Two特有的属性
        private ImageView mProductView;
        //Card Three特有的属性
        private ViewPager mViewPager;
    }
}
