package com.example.hatiboy.gpcapture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import image.capture.ImageAdapter;
import image.capture.ImageList;
import video.record.VideoArrayAdapter;
import video.record.VideoList;

/**
 * Created by HATIBOY on 7/15/2015.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<VideoList> listVideo = new ArrayList<VideoList>();
    private static Context mContext;
    Fragment tmpFragment;
    ListView listView;
    VideoArrayAdapter adapter;
    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        tmpFragment = new SlidePageSupportFragment();
        ((SlidePageSupportFragment) tmpFragment).setPageNumber(position);
        return tmpFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }



    public void bindViewPage1(ViewGroup my_view) {
        listView = (ListView) my_view.findViewById(R.id.video);
        createData();
        adapter = new VideoArrayAdapter(mContext, R.layout.custome_list_video, listVideo);
//        adapter.sort(new Comparator<VideoList>() {
//            public int compare(VideoList arg0, VideoList arg1) {
//                return arg0.getVideo_name().compareTo(arg1.getVideo_name());
//            }
//        });
        listView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }


    public void bindViewPage2(ViewGroup my_view) {
        File[] listImage;
        final ArrayList<String> f = new ArrayList<String>();
        File file = new File(android.os.Environment.getExternalStorageDirectory(), "/GPscreenshots");
        if (file.isDirectory()) {
            listImage = file.listFiles();
            for (File aListImage : listImage) {
                f.add(0, aListImage.getAbsolutePath());
                Log.e("image: ", aListImage.getName());
            }
        }
//        uri = listImage[i].getAbsolutePath();
        GridView gridView = (GridView) my_view.findViewById(R.id.grid_image);
        ImageAdapter da = new ImageAdapter(mContext, f);
        gridView.setAdapter(da);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri hacked_uri = Uri.parse("file://" + f.get(arg2));
                intent.setDataAndType(hacked_uri, "image/*");
                mContext.startActivity(intent);
                Log.e("IMG error", "file://" + f.get(arg2));
//                Toast.makeText(mContext, "gridview " + arg2, Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void createData() {
//        Uri video_thumbnail = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/GPcapture.mp4");
//        Bitmap bm = ThumbnailUtils.createVideoThumbnail(video_thumbnail.toString(), MediaStore.Video.Thumbnails.MINI_KIND);
//        img.setImageBitmap(bm);


        VideoList video1;
        File[] list_Video;
        File file = new File(android.os.Environment.getExternalStorageDirectory(), "/GPcapture/");
        if (file.isDirectory()) {
            list_Video = file.listFiles();

            for (File aList_Video : list_Video) {
                video1 = new VideoList();
                Uri videoUri = Uri.parse(aList_Video.getAbsolutePath());
                video1.setVideo_uri(videoUri);

//                Bitmap bm = ThumbnailUtils.createVideoThumbnail(video_thumbnail.toString(), MediaStore.Video.Thumbnails.MINI_KIND);
//                video1.setVideo_image(bm);
                video1.setVideo_name(aList_Video.getName());
                video1.setVideo_size((double) Math.round(aList_Video.length() * 100 / 1024 / 1024) / 100 + "MB");
                video1.setMore(R.drawable.ic_more);
//                MediaPlayer mp = MediaPlayer.create(mContext, video_thumbnail);
//                Log.e("video uri", video_thumbnail.toString());
//                try {
//                    int duration = mp.getDuration();
//                    mp.release();
//                    String s = String.format("%d min, %d sec",
//                            TimeUnit.MILLISECONDS.toMinutes(duration),
//                            TimeUnit.MILLISECONDS.toSeconds(duration) -
//                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
//                    );
//                    video1.setVideo_length(s);
//                } catch (Exception e) {
//                    Log.e("error3", e.toString());
//                }
                listVideo.add(0, video1);

            }

            /*Collections.sort(listVideo, new Comparator<VideoList>() {
                public int compare(VideoList obj1, VideoList obj2) {
                    // TODO Auto-generated method stub
                    return obj1.getVideo_name().compareToIgnoreCase(obj2.getVideo_name());
                }
            });*/

            new LoadDuration().execute();


        }
    }

    class LoadDuration extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < listVideo.size(); i++) {
                Bitmap bm = ThumbnailUtils.createVideoThumbnail(listVideo.get(i).getVideo_uri().toString(), MediaStore.Video.Thumbnails.MINI_KIND);
                listVideo.get(i).setVideo_image(bm);
                MediaPlayer mp = MediaPlayer.create(mContext, listVideo.get(i).getVideo_uri());
                Log.e("video uri", listVideo.get(i).getVideo_uri().toString());
                try {
                    int duration = mp.getDuration();
//                    mp.release();
                    String s = String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes(duration),
                            TimeUnit.MILLISECONDS.toSeconds(duration) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                    );
                    listVideo.get(i).setVideo_length(s);
                    publishProgress();
                } catch (Exception e) {
                    Log.e("error3", e.toString());
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            adapter.notifyDataSetChanged();
            super.onProgressUpdate();
        }
    }
}

