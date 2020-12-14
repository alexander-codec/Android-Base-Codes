package trimvideo;

/**
 * Created by HATIBOY on 8/6/2015.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.hatiboy.gpcapture.R;
import com.example.hatiboy.gpcapture.ReviewActivity;

import java.io.File;

import video.record.VideoArrayAdapter;

public class trimvideoactivity extends Activity {

    private static final String TAG = trimvideoactivity.class.getSimpleName();
    String filePrefix;
    TextView textViewLeft, textViewRight;
    VideoSliceSeekBar videoSliceSeekBar;
    VideoView videoView;
    View trimVideo;
    private int duration = -1;
    private int rightthumb = -1;
    private int leftthumb = -1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trimvideo);
        textViewLeft = (TextView) findViewById(R.id.left_pointer);
        textViewRight = (TextView) findViewById(R.id.right_pointer);

        videoSliceSeekBar = (VideoSliceSeekBar) findViewById(R.id.seek_bar);
        videoView = (VideoView) findViewById(R.id.video);
//        performVideoViewClick();
        Uri vidFile = Uri.parse(VideoArrayAdapter.videoSelectedPath);
        //videoView.setVideoPath(vidFile.toString());
        videoView.setVideoURI(vidFile);
        videoView.setMediaController(new MediaController(this));
//        videoView.requestFocus();
        videoView.start();
        trimVideo = findViewById(R.id.trimVideo);
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), vidFile);
        duration = mp.getDuration();

        if (duration < 0) {
            Toast.makeText(getApplicationContext(), "Can't trim this video !", Toast.LENGTH_SHORT).show();
            finish();
        }

        /*int u = videoSliceSeekBar.getLeftProgress();
        int j = videoSliceSeekBar.getRightProgress();
        //videoSliceSeekBar.setLeftProgress(25);
        videoSliceSeekBar.setLeft(duration*25 / 100);
        //videoSliceSeekBar.setRightProgress(75);

        videoSliceSeekBar.setRight(duration*75/100);

        int u1 = videoSliceSeekBar.getLeft();
        int j1 = videoSliceSeekBar.getRight();*/

        textViewLeft.setText(getTimeForTrackFormat(0, true));
        textViewRight.setText(getTimeForTrackFormat(duration, true));

        //init
        rightthumb = duration;
        leftthumb = 0;

        videoSliceSeekBar.setSeekBarChangeListener(new VideoSliceSeekBar.SeekBarChangeListener() {
            @Override
            public void SeekBarValueChanged(int leftThumb, int rightThumb) {
                //Toast.makeText(getApplicationContext(), leftThumb + "    " + rightThumb, Toast.LENGTH_SHORT).show();
                if (VideoSliceSeekBar.selectedThumb == VideoSliceSeekBar.SELECT_THUMB_LEFT) {
                    videoView.seekTo(leftThumb * duration / 100);
                    textViewLeft.setText(getTimeForTrackFormat(leftThumb * duration / 100, true));

                } else {
                    videoView.seekTo(rightThumb * duration / 100);
                    textViewRight.setText(getTimeForTrackFormat(rightThumb * duration / 100, true));
                }

                rightthumb = rightThumb * duration / 100;
                leftthumb = leftThumb * duration / 100;
                //trim();
            }

        });


        //initVideoView();


        trimVideo.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Log.d(TAG, "Left progress : " + leftthumb);
                                             Log.d(TAG, "Right progress : " + rightthumb);

                                             //Log.d(TAG, "Total Duration : " + mp.getDuration() / 1000);
                                             Log.e("VIDEO", VideoArrayAdapter.videoSelectedPath);
                                             Log.e("Video goc", Environment.getExternalStorageDirectory().getAbsolutePath() + "/GPcapture.mp4");
                                             executeTrimCommand(new File(VideoArrayAdapter.videoSelectedPath), leftthumb, rightthumb);


                                         }

                                     }

        );
    }


    public static String getTimeForTrackFormat(int timeInMills, boolean display2DigitsInMinsSection) {
        int minutes = (timeInMills / (60 * 1000));
        int seconds = (timeInMills - minutes * 60 * 1000) / 1000;
        String result = display2DigitsInMinsSection && minutes < 10 ? "0" : "";
        result += minutes + ":";
        if (seconds < 10) {
            result += "0" + seconds;
        } else {
            result += seconds;
        }
        return result;
    }


    private StateObserver videoStateObserver = new StateObserver();

    private class StateObserver extends Handler {

        private boolean alreadyStarted = false;

        private void startVideoProgressObserving() {
            if (!alreadyStarted) {
                alreadyStarted = true;
                sendEmptyMessage(0);
            }
        }

        private Runnable observerWork = new Runnable() {
            @Override
            public void run() {
                startVideoProgressObserving();
            }
        };

        @Override
        public void handleMessage(Message msg) {
            alreadyStarted = false;
            videoSliceSeekBar.videoPlayingProgress(videoView.getCurrentPosition());
            if (videoView.isPlaying() && videoView.getCurrentPosition() < videoSliceSeekBar.getRightProgress()) {
                postDelayed(observerWork, 50);
            } else {

                if (videoView.isPlaying()) videoView.pause();

                videoSliceSeekBar.setSliceBlocked(false);
                videoSliceSeekBar.removeVideoStatusThumb();
            }
        }
    }

    private void executeTrimCommand(File file, int startMs, int endMs) {
        File moviesDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/GPcapture/");
        String s = new File(VideoArrayAdapter.videoSelectedPath).getName();
        filePrefix = s.substring(0, s.length() - 4);
        Log.e("log", filePrefix);
        String fileExtn = ".mp4";

        try {
            File dest = new File(moviesDir, filePrefix + "_1" + fileExtn);
            if (dest.exists()) {
                dest.delete();
            }

            Log.d(TAG, "startTrim: src: " + file.getAbsolutePath());
            Log.d(TAG, "startTrim: dest: " + dest.getAbsolutePath());
            Log.d(TAG, "startTrim: startMs: " + startMs);
            Log.d(TAG, "startTrim: endMs: " + endMs);

            ShortenExample.crop(file, dest, startMs / 1000, endMs / 1000);

            Toast.makeText(getApplicationContext(), "Done.  File saved at " + dest.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        Intent i = new Intent(getApplicationContext(), ReviewActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        super.onDestroy();
    }
}
