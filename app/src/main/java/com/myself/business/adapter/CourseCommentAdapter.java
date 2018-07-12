package com.myself.business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myself.business.R;
import com.myself.business.model.course.CourseCommentValue;
import com.myself.vuandroidadsdk.imageloader.ImageLoaderManager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Kamh on 2018/7/12.
 */

public class CourseCommentAdapter extends BaseAdapter{

    private final LayoutInflater mInflater;
    private final Context mContext;

    private ArrayList<CourseCommentValue> mData;
    private ViewHolder mViewHolder;
    private ImageLoaderManager mImageLoaderManager;

    public CourseCommentAdapter(Context context, ArrayList<CourseCommentValue> data) {
        mContext = context;
        mData = data;
        mInflater = LayoutInflater.from(mContext);
        mImageLoaderManager = ImageLoaderManager.getInstance(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CourseCommentValue value = (CourseCommentValue) getItem(position);
        if (convertView == null){
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_comment_layout, parent, false);
            mViewHolder.mImageView = (CircleImageView)convertView.findViewById(R.id.photo_view);
            mViewHolder.mNameView = (TextView) convertView.findViewById(R.id.name_view);
            mViewHolder.mCommentView = (TextView) convertView.findViewById(R.id.text_view);
            mViewHolder.mOwnerView = (TextView) convertView.findViewById(R.id.owner_view);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        if (value.type == 1){
            mViewHolder.mOwnerView.setVisibility(View.VISIBLE);
        }else{
            mViewHolder.mOwnerView.setVisibility(View.GONE);
        }
        mImageLoaderManager.displayImage(mViewHolder.mImageView, value.logo);
        mViewHolder.mNameView.setText(value.name);
        mViewHolder.mCommentView.setText(value.text);

        return convertView;
    }

    public void addComment(CourseCommentValue value){
        mData.add(0, value);
        notifyDataSetChanged();
    }

    public int getCommandCount() {
        return getCount();
    }

    private static class ViewHolder{
        CircleImageView mImageView;
        TextView mNameView;
        TextView mCommentView;
        TextView mOwnerView;
    }
}
