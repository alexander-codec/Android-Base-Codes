package com.pongodev.layartancepapp.adapters;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.animators.internal.ViewHelper;
import com.pongodev.layartancepapp.R;
import com.pongodev.layartancepapp.utils.MySingleton;
import com.pongodev.layartancepapp.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Design and developed by pongodev.com
 *
 * AdapterList is created to create video item in list view. Created using UltimateViewAdapter.
 */
public class AdapterList extends UltimateViewAdapter<RecyclerView.ViewHolder> {

    // Create arraylist to store data
    private final ArrayList<HashMap<String,String>> DATA;
    // Create ImageLoader object to handle loading image in background
    private final ImageLoader IMAGE_LOADER;

    // Create Interpolator object for item animation
    private Interpolator mInterpolator = new LinearInterpolator();

    // Set last position
    private int mLastPosition = 5;
    // Set default animation duration
    private final int ANIMATION_DURATION = 300;

    public AdapterList(Context context, ArrayList<HashMap<String, String>> list){
        IMAGE_LOADER = MySingleton.getInstance(context).getImageLoader();
        DATA = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_video_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position < getItemCount() && (customHeaderView != null ? position <= DATA.size() :
                position < DATA.size()) && (customHeaderView == null || position > 0)) {
            HashMap<String, String> item;
            item = DATA.get(customHeaderView != null ? position - 1 : position);
            // Set data to the view
            ((ViewHolder) holder).mTxtTitle.setText(item.get(Utils.KEY_TITLE));
            ((ViewHolder) holder).mTxtDuration.setText(item.get(Utils.KEY_DURATION));
            ((ViewHolder) holder).mTxtPublishedAt.setText(item.get(Utils.KEY_PUBLISHEDAT));

            // Set image to imageview
            IMAGE_LOADER.get(item.get((Utils.KEY_URL_THUMBNAILS)),
                    ImageLoader.getImageListener(((ViewHolder) holder).mImgThumbnail,
                            R.mipmap.empty_photo, R.mipmap.empty_photo));
        }

        boolean isFirstOnly = true;
        if (!isFirstOnly || position > mLastPosition) {
            // Add animation to the item
            for (Animator anim : getAdapterAnimations(holder.itemView,
                    AdapterAnimationType.SlideInLeft)) {
                anim.setDuration(ANIMATION_DURATION).start();
                anim.setInterpolator(mInterpolator);
            }
            mLastPosition = position;
        } else {
            ViewHelper.clear(holder.itemView);
        }
    }

    @Override
    public int getAdapterItemCount() {
        return DATA.size();
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return new UltimateRecyclerviewViewHolder(view);
    }

    @Override
    public long generateHeaderId(int i) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    public static class ViewHolder extends UltimateRecyclerviewViewHolder {
        // Create view objects
        private TextView mTxtTitle, mTxtPublishedAt, mTxtDuration;
        private ImageView mImgThumbnail;

        public ViewHolder(View v) {
            super(v);
            mTxtTitle     = (TextView) v.findViewById(R.id.txtTitle);
            mTxtDuration  = (TextView) v.findViewById(R.id.txtDuration);
            mImgThumbnail = (ImageView) v.findViewById(R.id.imgThumbnail);
            mTxtPublishedAt = (TextView) v.findViewById(R.id.txtPublishedAt);
        }
    }

}
