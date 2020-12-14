package com.mp3player.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mp3player.searchonline.CircleTransform;
import com.mp3player.searchonline.R;
import com.mp3player.searchonline.constants;
import com.mp3player.model.Song;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Usman Jamil on 11/9/2015.
 */
public class CustomAdapter extends BaseAdapter implements constants {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Song> songItems;

    public CustomAdapter(Activity activity, List<Song> songItems) {
        this.activity = activity;
        this.songItems = songItems;
    }
    @Override
    public int getCount() {
        return songItems.size();
    }

    @Override
    public Object getItem(int location) {
        return songItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.song_layout, null);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView Sduration = (TextView) convertView.findViewById(R.id.Sduration);
        TextView playcount = (TextView) convertView.findViewById(R.id.playcount);



        // getting song data for the row
        final Song m = songItems.get(position);
        // title
        title.setText(m.getTitle());
        String strPlayerCount = formatVisualNumber(m.getLike(), ",");




        playcount.setText(strPlayerCount);
        ImageView Song_img = (ImageView) convertView.findViewById(R.id.img_songs);

        Picasso.with(activity).load(m.getArtwork_url()).placeholder(R.drawable.ic_my_library_music_blue_500_24dp).transform(new CircleTransform()).into(Song_img);

        long duration = m.getDuration() / 1000;
        String minute = String.valueOf((int) (duration / 60));
        String seconds = String.valueOf((int) (duration % 60));
        if (minute.length() < 2) {
            minute = "0" + minute;
        }
        if (seconds.length() < 2) {
            seconds = "0" + seconds;
        }
        Sduration.setText(minute + ":" + seconds);
        return convertView;
    }

    public static String formatVisualNumber(long numberValue, String demiter) {
        String value = String.valueOf(numberValue);
        if (value.length() > 3) {
            try {
                int number = (int) Math.floor(value.length() / 3);
                int lenght = value.length();
                String total = "";
                for (int i = 0; i < number; i++) {
                    for (int j = 0; j < 3; j++) {
                        int index = lenght - 1 - (i * 3 + j);
                        total = value.charAt(index) + total;
                    }
                    if (i != number - 1) {
                        total = demiter + total;
                    }
                    else {
                        int delta = lenght - number * 3;
                        if (delta > 0) {
                            total = demiter + total;
                        }
                    }
                }
                total = value.substring(0, lenght - number * 3) + total;
                return total;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return String.valueOf(value);
    }



}
