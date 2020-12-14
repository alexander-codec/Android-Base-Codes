package video.record;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hatiboy.gpcapture.R;


import trimvideo.trimvideoactivity;

/**
 * Created by HATIBOY on 7/14/2015.
 */
public class VideoArrayAdapter extends ArrayAdapter<VideoList> {
    public VideoList listVideo;
    Context mContext;
    ArrayList<VideoList> myArray = new ArrayList<VideoList>();
    public static String videoSelectedPath = null;

    public VideoArrayAdapter(Context context, int resource, ArrayList<VideoList> object) {
        super(context, resource, object);
        this.mContext = context;
        this.myArray = new ArrayList<>(object);

    }
//    @Override
//    public void sort(Comparator<? super VideoList> comparator) {
//        // TODO Auto-generated method stub
//        super.sort(comparator);
//    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.custome_list_video, null);

            viewHolder = new ViewHolder();
            viewHolder.listVideoItem = (LinearLayout) rowView.findViewById(R.id.listVideoItem);
            viewHolder.video_Image = (ImageView) rowView.findViewById(R.id.video_Image);
            viewHolder.video_Name = (TextView) rowView.findViewById(R.id.video_Name);
            viewHolder.video_Length = (TextView) rowView.findViewById(R.id.video_Length);
            viewHolder.video_Size = (TextView) rowView.findViewById(R.id.video_Size);
            viewHolder.more = (ImageView) rowView.findViewById(R.id.more);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        listVideo = myArray.get(position);
        viewHolder.video_Image.setImageBitmap(listVideo.getVideo_image());
        viewHolder.video_Image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.more.setImageResource(listVideo.getMore());
        viewHolder.video_Size.setText(listVideo.getVideo_size());
        viewHolder.video_Name.setText(listVideo.getVideo_name());
        viewHolder.video_Length.setText(listVideo.getVideo_length());
        final Uri uri = Uri.parse("file://" + listVideo.getVideo_uri());
//        Log.e("video uri", "file://" + listVideo.getVideo_uri());
        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                moreOption(viewHolder, v);
                return false;
            }
        });

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "video/*");
                mContext.startActivity(intent);
            }
        });

        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreOption(viewHolder, v);
            }
        });
        return rowView;
    }

    public void moreOption(final ViewHolder viewHolder, View v) {
        PopupMenu popup = new PopupMenu(mContext, v);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.show();
        videoSelectedPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GPcapture/" + viewHolder.video_Name.getText();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_trim:
                        Intent trimVideoIntent = new Intent(getContext(), trimvideoactivity.class);
                        trimVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(trimVideoIntent);
//                                Toast.makeText(mContext, "Trim Video", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_delete:
                        //thay bang bien uri o tren la bi loi
                        File file = new File(videoSelectedPath);
                        if (file.exists()) {
                            file.delete();
                            viewHolder.listVideoItem.removeAllViews();
                            Toast.makeText(mContext, "Video Deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("can not delete video:", videoSelectedPath);
                        }
                        break;
                    case R.id.action_share:
                        shareVideo(viewHolder.video_Name.getText().toString(), videoSelectedPath);
                        break;
                }
                return false;
            }
        });
    }

    public void shareVideo(final String title, String path) {
        MediaScannerConnection.scanFile(mContext, new String[]{path},
                null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Intent shareIntent = new Intent(
                                android.content.Intent.ACTION_SEND);
                        shareIntent.setType("video/*");
                        shareIntent.putExtra(
                                android.content.Intent.EXTRA_SUBJECT, title);
                        shareIntent.putExtra(
                                android.content.Intent.EXTRA_TITLE, title);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        mContext.startActivity(Intent.createChooser(shareIntent,
                                mContext.getString(R.string.sharevideo)));

                    }
                });
    }

    static class ViewHolder {
        LinearLayout listVideoItem;
        TextView video_Name;
        TextView video_Length;
        TextView video_Size;
        ImageView video_Image;
        ImageView more;
    }
}
