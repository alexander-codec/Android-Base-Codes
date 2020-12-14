package image.capture;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.hatiboy.gpcapture.R;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by HATIBOY on 7/15/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mThumbIds;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public ImageAdapter(Context c, ArrayList<String> arrIds) {
        mContext = c;
        mThumbIds = arrIds;
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup parent) {


        ImageView imgView;
        if (convertView == null) {
            imgView = new ImageView(mContext);
            imgView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imgView.setScaleType(ImageView.ScaleType.CENTER);
//            imgView.setPadding(8, 8, 8, 8);
        } else {
            imgView = (ImageView) convertView;
        }

        try {

            imgView.setImageBitmap(getThumbnail(mContext.getContentResolver(), mThumbIds.get(arg0)));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        imgView.setImageURI(Uri.parse(mThumbIds.get(arg0)));
//        imgView.setImageResource(R.drawable.demo);
        return imgView;

    }

    public static Bitmap getThumbnail(ContentResolver cr, String path) throws Exception {

        Cursor ca = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.MediaColumns._ID}, MediaStore.MediaColumns.DATA + "=?", new String[]{path}, null);
        if (ca != null && ca.moveToFirst()) {
            int id = ca.getInt(ca.getColumnIndex(MediaStore.MediaColumns._ID));
            ca.close();
            return MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
        }

        assert ca != null;
        ca.close();
        return null;

    }
}
