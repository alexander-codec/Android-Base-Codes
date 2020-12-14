package video.record;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

/**
 * Created by HATIBOY on 7/14/2015.
 */
public class VideoList {
    public String video_size;
    private String video_name;
    private String video_length;
    private Bitmap video_image;
    private Uri video_uri;
    private int more;


    public int getMore() {
        return more;
    }

    public Uri getVideo_uri() {
        return video_uri;
    }

    public void setVideo_uri(Uri video_uri) {
        this.video_uri = video_uri;
    }

    public void setMore(int more) {
        this.more = more;
    }

    public Bitmap getVideo_image() {
        return video_image;
    }

    public void setVideo_image(Bitmap video_image) {
        this.video_image = video_image;
    }

    public String getVideo_length() {
        return video_length;
    }

    public void setVideo_length(String video_length) {
        this.video_length = video_length;
    }

    public String getVideo_size() {
        return video_size;
    }

    public void setVideo_size(String video_size) {
        this.video_size = video_size;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }
}
