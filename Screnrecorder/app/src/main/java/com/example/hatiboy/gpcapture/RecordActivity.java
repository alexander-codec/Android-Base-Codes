package com.example.hatiboy.gpcapture;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by HATIBOY on 7/27/2015.
 */
public class RecordActivity extends Activity {
    public static boolean recording = false;
    public static MediaProjectionManager mMediaProjectionManager;
    public static MediaProjection mMediaProjection;
    public static MediaRecorder recorder;
    public static VirtualDisplay display;
    static String videoName;
    boolean audio;
    boolean touch;
    int bitrate;
    int width, height;
    /**
     * Default value
     */
    public static int REQUEST_CODE_CAPTURE_PERM = 1234;
    private static String STORE_DIRECTORY;
    private final DateFormat fileFormat =
            new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss'.mp4'", Locale.US);
    Configuration configuration;
    private Resolution resolution;
    ResolutionAdapter resolutionAdapter;


    public void getSetting() {
        SharedPreferences pre = getSharedPreferences
                ("settings", MODE_PRIVATE);
        audio = pre.getBoolean("audio", false);
        touch = pre.getBoolean("touch", false);
        bitrate = pre.getInt("bitrate", 8);
//        String s = pre.getString("resolution", "");
//        Log.d("resolution", s);
//        width = Integer.parseInt(s.substring(s.indexOf("x")));
//        height = Integer.parseInt(s.substring(0, s.indexOf("x")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recording = true;
//        if (mMediaProjection != null) {
//            mMediaProjection.stop();
//           mMediaProjection = null;
//       }

        getSetting();
        try {
            FloatingView.windowManager.removeView(FloatingView.view);
        } catch (IllegalArgumentException | NullPointerException e) {
            Log.e("error", e.toString());
        }
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        Intent intent = mMediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(intent, REQUEST_CODE_CAPTURE_PERM);
        setConfiguration(getResources().getConfiguration());

    }

    @Override
    protected void onResume() {
        if (mMediaProjection != null) {
            releaseEncoders();
        }
        super.onResume();
    }

    private void showNotification() {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, BroadcastReceiverStopRecord.class), 0);
        Notification.Builder mBuilder;

        mBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_record)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setContentTitle(getString(R.string.start_record))
                .setStyle(new Notification.BigTextStyle()
                        .bigText("name"))
                .setContentText(getString(R.string.stop_record))
                .setLights(Color.YELLOW, 500, 1000)
                .setAutoCancel(true);


        mBuilder.setContentIntent(contentIntent);
        notificationManager.notify(REQUEST_CODE_CAPTURE_PERM, mBuilder.build());
        //REQUEST_CODE_CAPTURE_PERM++;

    }

    private void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        if (touch) {
            Settings.System.putInt(getContentResolver(), "show_touches", 1);
        }

        Resolution screenResolution = ResolutionHelper.getScreenResolution(this);
//        Resolution resolution = null;

        if (resolution != null) {
            if (screenResolution.isPortrait() != resolution.isPortrait()) {
                resolution.rotate();
            }
//            this.resolution = resolution;
        } else {
            this.resolution = ResolutionHelper.getRecordingResolutionForScreenSize(screenResolution);
        }
        int orientation = configuration.orientation;
        Log.e("orientation", String.valueOf(orientation));
        if ("ORIENTATION_CURRENT".equals("ORIENTATION_LANDSCAPE")) {
            orientation = 2;
        } else if ("ORIENTATION_CURRENT".equals("ORIENTATION_PORTRAIT")) {
            orientation = 1;
        }
        this.updateResolutionAdapterWithOrientation(orientation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAPTURE_PERM) {
            if (resultCode == RESULT_OK) {
                mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
                startRecording(); // defined below
            } else {
//                startService(new Intent(getApplicationContext(), FloatingView.class));
                try {
                    FloatingView.windowManager.addView(FloatingView.view, FloatingView.params);
                } catch (IllegalArgumentException | NullPointerException e) {
                    Log.e("error", e.toString());
                }
                finish();
                // user did not grant permissions
            }
        }
    }


    private void startRecording() {


        recorder = new MediaRecorder();
        if (audio) {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        }
        recorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setVideoFrameRate(30);
        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        recorder.setVideoSize(resolution.Width, resolution.Height);
//        recorder.setVideoSize(resolution.Height, resolution.Width);
        recorder.setVideoEncodingBitRate(bitrate * 1000 * 1000);
        if (audio) {
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        }

//        mCamera= Camera.open();
//        Camera.Parameters parameters=mCamera.getParameters();
//        parameters.setPreviewSize(352,288);
//        parameters.set("orientation","portrait");
//        mCamera.setParameters(parameters);
//        mCamera.unlock();
//        mRecorder.setCamera(mCamera);
//        Thread.sleep(1000);
//        mCamera.lock();
//        mCamera.release();
//        videoRecordedResult=validateVideo(MediaNames.RECORDED_PORTRAIT_H263,352,288);


        STORE_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GPcapture/";
        File storeDirectory = new File(STORE_DIRECTORY);
        if (!storeDirectory.exists()) {
            boolean success = storeDirectory.mkdirs();
            if (!success) {
                Log.e("error", "failed to create file storage directory.");
                return;
            }
        }
        String getTime = fileFormat.format(new Date());
        File fileVideo = new File(STORE_DIRECTORY + getTime);
        String fileVideoPath = fileVideo.getAbsolutePath();
        videoName = fileVideoPath;

//        Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        Uri fileContentUri = Uri.fromFile(fileVideo);
//        mediaScannerIntent.setData(fileContentUri);
//        this.sendBroadcast(mediaScannerIntent);
        addVideo(fileVideo, fileVideoPath);
        Log.e("video file:", fileVideoPath);

        recorder.setOutputFile(fileVideoPath);
        try {
            recorder.prepare();
        } catch (IOException e) {
            throw new RuntimeException("Unable to prepare MediaRecorder.", e);
        }
        // Get the display size and density.
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        int screenDensity = metrics.densityDpi;

//        String s = screenWidth + "_" + screenHeight + "_" + screenDensity;
//        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

        Surface surface = recorder.getSurface();
        display = mMediaProjection.createVirtualDisplay("Recording Display", screenWidth, screenHeight,
                screenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_PRESENTATION, surface, null, null);
        recorder.start();
        showNotification();
        finish();
    }

    public Uri addVideo(File videoFile, String title) {
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Video.Media.TITLE, title);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/*");
        values.put(MediaStore.Video.Media.DATA, videoFile.getAbsolutePath());
        return getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }

    public void updateResolutionAdapterWithOrientation(int orientation) {
        this.resolutionAdapter = ResolutionAdapter.createAdapter(this, orientation);
//        this.spinnerResolution.setAdapter(this.resolutionAdapter);
//        this.spinnerResolution.setSelection(this.resolutionAdapter.getPosition(this.resolution));
    }

    public static void releaseEncoders() {
        try {
            if (mMediaProjection != null) {
                mMediaProjection.stop();
                mMediaProjection = null;
            }
            if (recorder != null) {
                recorder.stop();
                recorder.reset();
                recorder.release();
            }
            if (display != null) {
                display.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
//        startService(new Intent(getApplicationContext(), FloatingView.class));
        super.onDestroy();
    }
}
