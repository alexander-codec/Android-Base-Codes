package com.example.hatiboy.gpcapture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodec;
import android.media.MediaMuxer;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SettingActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CAPTURE_PERM = 1234;
    private static final String VIDEO_MIME_TYPE = "video/avc";
    private static final int VIDEO_WIDTH = 1280;
    private static final int VIDEO_HEIGHT = 720;
    private final Handler mDrainHandler = new Handler(Looper.getMainLooper());
    private final DateFormat fileFormat =
            new SimpleDateFormat("'_'yyyy-MM-dd-HH-mm-ss'.mp4'", Locale.US);
    /**
     * View activity
     */
    Button buttonStart;
    Button buttonStop;
    Spinner spinnerResolution;
    Spinner spinnerOrientation;
    TextView tvResolution;
    TextView tvOrientation;
    EditText videoName;
    EditText videoBitrate;
    Switch showTouch;
    Switch recordAudio;
    ResolutionAdapter resolutionAdapter;
    OrientationAdapter orientationAdapter;
    String resolutionselected;
    String orientationselected;
    /**
     * Default value
     */
    Configuration configuration;

    private MediaProjectionManager mMediaProjectionManager;
    private MediaProjection mMediaProjection;
    // …
//    private boolean mMuxerStarted = false;
//    private Surface mInputSurface;
//    private MediaMuxer mMuxer;
//
//    private MediaCodec mVideoEncoder;
//    private MediaCodec.BufferInfo mVideoBufferInfo;
//    private int mTrackIndex = -1;
//    private Resolution screenResolution;
    private Resolution resolution;
    private LinearLayout app_center;
    private MediaRecorder recorder;
    private VirtualDisplay display;

    private com.google.android.gms.ads.AdView g_adView;
    private RelativeLayout layout_ads_main_screen;


    private void showGBannerAds(final ViewGroup myview_ads) {

        g_adView = new com.google.android.gms.ads.AdView(this);
        g_adView.setAdSize(com.google.android.gms.ads.AdSize.SMART_BANNER);
        g_adView.setBackgroundColor(Color.TRANSPARENT);
        g_adView.setAdUnitId(getString(R.string.admob_banner));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        g_adView.setLayoutParams(params);
        myview_ads.addView(g_adView);
        AdRequest.Builder builder = new AdRequest.Builder();
        builder.addTestDevice("5209A0E30EFC293107FAB2BB7ECE2FCC");
        AdRequest adRequest = builder.build();

        //adRequest.te
        g_adView.loadAd(adRequest);
    }

    private void loadGInterstitialAd() {
        g_FullAdView = new com.google.android.gms.ads.InterstitialAd(SettingActivity.this);
        g_FullAdView.setAdUnitId(getString(R.string.admob_full));

        g_FullAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

                finish();

            }

        });
        requestNewInterstitial();
    }

    private com.google.android.gms.ads.InterstitialAd g_FullAdView;

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("5209A0E30EFC293107FAB2BB7ECE2FCC")
                .build();

        g_FullAdView.loadAd(adRequest);

    }

    @Override
    public void onBackPressed() {


        if (g_FullAdView != null && g_FullAdView.isLoaded()) {
            g_FullAdView.show();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_settings_activity);

        //ads
        layout_ads_main_screen = (RelativeLayout) findViewById(R.id.layout_ads_main_screen_nl);
        showGBannerAds(layout_ads_main_screen);

        loadGInterstitialAd();

        mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        bindView();
        setListener();
        if (getIntent().getExtras().getBoolean("startrecord")) {
            start();
        }

        initActionBar();

        setConfiguration(getResources().getConfiguration());
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(getResources().getString(R.string.setting));
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196f3")));
        }
    }

    public void getPreference() {
        SharedPreferences pre = getSharedPreferences("settings", MODE_PRIVATE);
        boolean audio = pre.getBoolean("audio", false);
        boolean touch = pre.getBoolean("touch", false);
        if (audio) {
            recordAudio.setChecked(true);
        } else {
            recordAudio.setChecked(false);
        }
        if (touch) {
            showTouch.setChecked(true);
        } else {
            showTouch.setChecked(false);
        }
        int bitrate = pre.getInt("bitrate", 8);
        videoBitrate.setText(String.valueOf(bitrate));
    }

    @Override
    protected void onResume() {
        getPreference();
        super.onResume();
    }

    public void putPreference() {
        SharedPreferences pre = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();

        editor.putBoolean("audio", recordAudio.isChecked());
        editor.putBoolean("touch", showTouch.isChecked());
        editor.putInt("bitrate", Integer.parseInt(videoBitrate.getText().toString()));
        Log.e("resolution:", String.valueOf(resolution.Width) + "x" + String.valueOf(resolution.Height));
        //Toast.makeText(this, String.valueOf(resolution.Width) + "x" + String.valueOf(resolution.Height), Toast.LENGTH_LONG).show();
        editor.putString("resolution", resolutionselected);
        editor.putString("orientation", orientationselected);
        editor.commit();
        //editor.apply();
    }

    @Override
    protected void onPause() {
        putPreference();
        super.onPause();
    }

    public void start() {
//        GPApplication.fireScreenCaptureIntent(this,mMediaProjectionManager);;
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent permissionIntent = mMediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(permissionIntent, REQUEST_CODE_CAPTURE_PERM);
    }

    private void bindView() {

//        buttonStart = (Button) findViewById(R.id.buttonStartRecord);
//        buttonStop = (Button) findViewById(R.id.buttonStopRecord);
//        videoName = (EditText) findViewById(R.id.videoName);
        videoBitrate = (EditText) findViewById(R.id.videoBitrate);
        spinnerResolution = (Spinner) findViewById(R.id.spinnerResolution);
        spinnerOrientation = (Spinner) findViewById(R.id.spinnerOrientation);
        tvResolution = (TextView) findViewById(R.id.tvResolution);
        tvOrientation = (TextView) findViewById(R.id.tvOrientation);
        recordAudio = (Switch) findViewById(R.id.audio);
        showTouch = (Switch) findViewById(R.id.showTouch);
        app_center = (LinearLayout) findViewById(R.id.app_center);

    }

    private void setListener() {
        recordAudio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), getString(R.string.audioset), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.audiounset), Toast.LENGTH_SHORT).show();
                }
            }
        });
        showTouch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Settings.System.putInt(getContentResolver(), "show_touches", 1);
                    Toast.makeText(getApplicationContext(), getString(R.string.touchset), Toast.LENGTH_SHORT).show();
                } else {
                    Settings.System.putInt(getContentResolver(), "show_touches", 0);
                    Toast.makeText(getApplicationContext(), getString(R.string.touchunset), Toast.LENGTH_SHORT).show();
                }
            }
        });
        app_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://play.google.com/store/apps/developer?id=RealAppsMaker";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


//        buttonStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
//                Intent permissionIntent = mMediaProjectionManager.createScreenCaptureIntent();
//                startActivityForResult(permissionIntent, REQUEST_CODE_CAPTURE_PERM);
//            }
//        });
//        buttonStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                releaseEncoders();
//            }
//        });
    }

    private void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
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
        this.orientationAdapter = OrientationAdapter.createAdapter(this, configuration);
        this.spinnerOrientation.setAdapter(this.orientationAdapter);
        this.spinnerOrientation.setSelection(this.orientationAdapter.getOrientationPosition("ORIENTATION_CURRENT"));
        this.spinnerOrientation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orientationselected = parent.getItemAtPosition(position).toString();
                tvOrientation.setText(orientationselected);
                Log.e("orientation", orientationselected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //this.d.setOnItemSelectedListener((AdapterView$OnItemSelectedListener)new MainActivity$RecordingOrientationClickListener(this));
        int orientation = configuration.orientation;
        if ("ORIENTATION_CURRENT".equals("ORIENTATION_LANDSCAPE")) {
            orientation = 2;
        } else if ("ORIENTATION_CURRENT".equals("ORIENTATION_PORTRAIT")) {
            orientation = 1;
        }
        this.updateResolutionAdapterWithOrientation(orientation);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE_CAPTURE_PERM) {
            if (resultCode == RESULT_OK) {
                mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, intent);
                startRecording(); // defined below
            } else {
//                finish();
                // user did not grant permissions
            }
        }
    }

    private void startRecording() {
//        DisplayManager dm = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
//        Display defaultDisplay = dm.getDisplay(Display.DEFAULT_DISPLAY);
//        if (defaultDisplay == null) {
//            throw new RuntimeException("No display found.");
//        }
//        prepareVideoEncoder();
//        if (showAudio.isChecked()) {
//            startRecordingWithAudio();
//        } else {
//            startRecordingWithoutAudio();
//        }
//    }
//
//
//    private void startRecordingWithAudio() {
        recorder = new MediaRecorder();
        if (recordAudio.isChecked()) {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        }
        recorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setVideoFrameRate(30);
        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        recorder.setVideoSize(resolution.Width, resolution.Height);
        recorder.setVideoEncodingBitRate(Integer.parseInt(videoBitrate.getText().toString()) * 1000 * 1000);
        if (recordAudio.isChecked()) {
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        }
        // int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        // int orientation = ORIENTATIONS.get(rotation);
        //mMediaRecorder.setOrientationHint(orientation);
        String outputName = fileFormat.format(new Date());
        String outputFile = new File("/sdcard/" + videoName.getText() + outputName).getAbsolutePath();
        recorder.setOutputFile(outputFile);
        recorder.setMaxDuration(300000);
//        recorder.setMaxFileSize(5000000);
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

        String s = screenWidth + "_" + screenHeight + "_" + screenDensity;
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

        Surface surface = recorder.getSurface();
        display = mMediaProjection.createVirtualDisplay("Recording Display", screenWidth, screenHeight,
                screenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_PRESENTATION, surface, null, null);
        recorder.start();
    }

//    private void startRecordingWithoutAudio() {
//        try {
//            mMuxer = new MediaMuxer("/sdcard/" + videoName.getText() + ".mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
//        } catch (IOException ioe) {
//            throw new RuntimeException("MediaMuxer creation failed", ioe);
//        }
//
//        // Get the display size and density.
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        int screenWidth = metrics.widthPixels;
//        int screenHeight = metrics.heightPixels;
//        int screenDensity = metrics.densityDpi;
//
//        // Start the video input.
//        mMediaProjection.createVirtualDisplay("Recording Display", screenWidth,
//                screenHeight, screenDensity, 0 /* flags */, mInputSurface,
//                null /* callback */, null /* handler */);
//
//        // Start the encoders
//        drainEncoder();
//    }
//    private boolean drainEncoder() {
//        mDrainHandler.removeCallbacks(mDrainEncoderRunnable);
//        while (true) {
//            int bufferIndex = mVideoEncoder.dequeueOutputBuffer(mVideoBufferInfo, 0);
//
//            if (bufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
//                // nothing available yet
//                break;
//            } else if (bufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
//                // should happen before receiving buffers, and should only happen once
//                if (mTrackIndex >= 0) {
//                    throw new RuntimeException("format changed twice");
//                }
//                mTrackIndex = mMuxer.addTrack(mVideoEncoder.getOutputFormat());
//                if (!mMuxerStarted && mTrackIndex >= 0) {
//                    mMuxer.start();
//                    mMuxerStarted = true;
//                }
//            } else if (bufferIndex < 0) {
//                // not sure what's going on, ignore it
//            } else {
//                ByteBuffer encodedData = mVideoEncoder.getOutputBuffer(bufferIndex);
//                if (encodedData == null) {
//                    throw new RuntimeException("couldn't fetch buffer at index " + bufferIndex);
//                }
//
//                if ((mVideoBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
//                    mVideoBufferInfo.size = 0;
//                }
//
//                if (mVideoBufferInfo.size != 0) {
//                    if (mMuxerStarted) {
//                        encodedData.position(mVideoBufferInfo.offset);
//                        encodedData.limit(mVideoBufferInfo.offset + mVideoBufferInfo.size);
//
//                        mMuxer.writeSampleData(mTrackIndex, encodedData, mVideoBufferInfo);
//                    } else {
//                        // muxer not started
//                    }
//                }
//
//                mVideoEncoder.releaseOutputBuffer(bufferIndex, false);
//
//                if ((mVideoBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
//                    break;
//                }
//
//            }
//        }
//
//        mDrainHandler.postDelayed(mDrainEncoderRunnable, 100);
//        //                finish();
//        return false;
//
//    }
//    private Runnable mDrainEncoderRunnable = new Runnable() {
//        @Override
//        public void run() {
//            drainEncoder();
//        }
//    };


//    private void prepareVideoEncoder() {
//        mVideoBufferInfo = new MediaCodec.BufferInfo();
//        MediaFormat format = MediaFormat.createVideoFormat(VIDEO_MIME_TYPE, resolution.Width, resolution.Height);
//        int frameRate = 30; // 30 fps
////         Set some required properties. The media codec may fail if these aren't defined.
//        format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
//                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
//        format.setInteger(MediaFormat.KEY_BIT_RATE, 6000000); // 6Mbps
//        format.setInteger(MediaFormat.KEY_FRAME_RATE, frameRate);
//        format.setInteger(MediaFormat.KEY_CAPTURE_RATE, frameRate);
//        format.setInteger(MediaFormat.KEY_REPEAT_PREVIOUS_FRAME_AFTER, 1000000 / frameRate);
//        format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
//        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1); // 1 seconds between I-frames
//
//        // Create a MediaCodec encoder and configure it. Get a Surface we can use for recording into.
//        try {
//            mVideoEncoder = MediaCodec.createEncoderByType(VIDEO_MIME_TYPE);
//            mVideoEncoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
//            mInputSurface = mVideoEncoder.createInputSurface();
//            mVideoEncoder.start();
//
//        } catch (IOException e) {
//            releaseEncoders();
//        }
//    }

    private void releaseEncoders() {
        try {
            // Stop the projection in order to flush everything to the recorder.
            mMediaProjection.stop();
            // Stop the recorder which writes the contents to the file.
            recorder.stop();
            recorder.reset();
            recorder.release();
            display.release();

            Toast.makeText(getApplicationContext(), "Stop capture", Toast.LENGTH_LONG).show();
        } catch (Exception e) {

        }
//            mDrainHandler.removeCallbacks(mDrainEncoderRunnable);
//        if (mMediaProjection != null) {
//            mMediaProjection.stop();
//            mMediaProjection = null;
//        }
//        if (mMuxer != null) {
//            if (mMuxerStarted) {
//                mMuxer.stop();
//            }
//            mMuxer.release();
//            mMuxer = null;
//            mMuxerStarted = false;
//        }
//        if (mVideoEncoder != null) {
//            mVideoEncoder.stop();
//            mVideoEncoder.release();
//            mVideoEncoder = null;
//        }
//        if (mInputSurface != null) {
//            mInputSurface.release();
//            mInputSurface = null;
//        }

//        mVideoBufferInfo = null;
//            mDrainEncoderRunnable = null;
//        mTrackIndex = -1;

    }

    public void updateResolutionAdapterWithOrientation(int orientation) {
        this.resolutionAdapter = ResolutionAdapter.createAdapter(this, orientation);
        this.spinnerResolution.setAdapter(this.resolutionAdapter);
        this.spinnerResolution.setSelection(this.resolutionAdapter.getPosition(this.resolution));
        //this.spinnerResolution.setOnItemSelectedListener((AdapterView$OnItemSelectedListener) new MainActivity$RecordingResolutionClickListener(this));
        this.spinnerResolution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resolutionselected = parent.getItemAtPosition(position).toString();
                tvResolution.setText(resolutionselected);
                Log.e("OnItemSelectedListener:", resolutionselected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            if (g_FullAdView != null && g_FullAdView.isLoaded()) {
                g_FullAdView.show();
            } else {
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
//        startService(new Intent(getApplicationContext(), FloatingView.class));
//        Settings.System.putInt(getContentResolver(), "show_touches", 0);
//        if (getIntent().getBooleanExtra("setting_start", true)) {
        FloatingView.windowManager.addView(FloatingView.view, FloatingView.params);
//        }
        super.onDestroy();
    }
}
