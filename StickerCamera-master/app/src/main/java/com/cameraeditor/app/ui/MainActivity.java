package com.cameraeditor.app.ui;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.common.util.DataUtils;
import com.common.util.StringUtils;
import com.github.skykai.stickercamera.R;
import com.melnykov.fab.FloatingActionButton;
import com.cameraeditor.App;
import com.cameraeditor.AppConstants;
import com.cameraeditor.app.camera.CameraManager;
import com.cameraeditor.app.model.FeedItem;
import com.cameraeditor.app.model.TagItem;
import com.cameraeditor.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity {

    private List<FeedItem> feedList;
    private PictureAdapter mAdapter;
    ImageView imgClick;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
        initView();

        //如果没有照片则打开相机
        String str = DataUtils.getStringPreferences(App.getApp(), AppConstants.FEED_INFO);
        if (StringUtils.isNotEmpty(str)) {
            feedList = JSON.parseArray(str, FeedItem.class);
        }
        if (feedList == null) {
            CameraManager.getInst().openCamera(MainActivity.this);
        } else {
            mAdapter.setList(feedList);
        }

    }


    public void onEventMainThread(FeedItem feedItem) {
        if (feedList == null) {
            feedList = new ArrayList<FeedItem>();
        }
        feedList.add(0, feedItem);
        DataUtils.setStringPreferences(App.getApp(), AppConstants.FEED_INFO, JSON.toJSONString(feedList));
        mAdapter.setList(feedList);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        titleBar.hideLeftBtn();
        titleBar.hideRightBtn();


        mAdapter = new PictureAdapter();
        imgClick = (ImageView)findViewById(R.id.imageView2);
        imgClick.setOnClickListener(v -> CameraManager.getInst().openCamera(MainActivity.this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //照片适配器
    public class PictureAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<FeedItem> items = new ArrayList<FeedItem>();

        public void setList(List<FeedItem> list) {
            if (items.size() > 0) {
                items.clear();
            }
            items.addAll(list);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            FeedItem feedItem = items.get(position);
            holder.picture.setImageBitmap(BitmapFactory.decodeFile(feedItem.getImgPath()));
            holder.setTagList(feedItem.getTagList());

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            // 将标签移除,避免回收使用时标签重复
            holder.pictureLayout.removeViews(1, holder.pictureLayout.getChildCount() - 1);
            super.onViewRecycled(holder);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.pictureLayout)
        RelativeLayout pictureLayout;
        @InjectView(R.id.picture)
        ImageView picture;

        private List<TagItem> tagList = new ArrayList<>();

        public List<TagItem> getTagList() {
            return tagList;
        }

        public void setTagList(List<TagItem> tagList) {
            if (this.tagList.size() > 0) {
                this.tagList.clear();
            }
            this.tagList.addAll(tagList);
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

}
