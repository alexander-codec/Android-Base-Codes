package com.example.hatiboy.gpcapture;


import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class FloatingView extends Service {

    RelativeLayout record_move, capture_move, review_move, setting_move;
    ImageView record;
    ImageView capture;
    ImageView review;
    ImageView record_setting;
    ImageView close;
    public static WindowManager windowManager;
    public static ViewGroup view;
    public static WindowManager.LayoutParams params;
    int check = 0;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        view = (ViewGroup) inflater.inflate(R.layout.floatingview, null);
        record = (ImageView) view.findViewById(R.id.record);
        capture = (ImageView) view.findViewById(R.id.capture);
        review = (ImageView) view.findViewById(R.id.review);
        record_setting = (ImageView) view.findViewById(R.id.record_setting);
        close = (ImageView) view.findViewById(R.id.close);

        capture_move = (RelativeLayout) view.findViewById(R.id.capture_move);
        record_move = (RelativeLayout) view.findViewById(R.id.record_move);
        review_move = (RelativeLayout) view.findViewById(R.id.review_move);
        setting_move = (RelativeLayout) view.findViewById(R.id.setting_move);

        Touch();
        Click();
    }

    public void Touch() {
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        params.alpha = 0.75f;
        windowManager.addView(view, params);
        View[] v = {record_move, capture_move, review_move, setting_move};
        for (int i = 0; i < 4; i++) {
            try {
                v[i].setOnTouchListener(new View.OnTouchListener() {
                    private WindowManager.LayoutParams paramsF = params;
                    private int initialX;
                    private int initialY;
                    private float initialTouchX;
                    private float initialTouchY;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                // Get current time in nano seconds.
                                check = 0;
                                initialX = paramsF.x;
                                initialY = paramsF.y;
                                initialTouchX = event.getRawX();
                                initialTouchY = event.getRawY();
                                break;
                            case MotionEvent.ACTION_UP:
                                Log.e("check", String.valueOf(check));
                                Click();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                check++;
                                paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
                                paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
                                windowManager.updateViewLayout(view, paramsF);
                                break;
                        }
                        return false;
                    }
                });
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
    }

    public void Click() {
        Log.e("checked", String.valueOf(check));
        if (check >= 10) {
            View[] v = {record_move, capture_move, review_move, setting_move};
            for (int i = 0; i < 4; i++) {
                v[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("clicked", "true");
                    }
                });
            }
            return;
        }
        record_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recordIntent = new Intent(getBaseContext(), RecordActivity.class);
                recordIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(recordIntent);
//                stopSelf();
//                Toast.makeText(getApplicationContext(), "record clicked", Toast.LENGTH_SHORT).show();
            }
        });

        capture_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent captureIntent = new Intent(getBaseContext(), ScreenCaptureImageActivity.class);
                captureIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(captureIntent);
                windowManager.removeView(view);
//                stopSelf();
            }
        });
        review_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                stopSelf();
//                Settings.System.putInt(getContentResolver(), "show_touches", 0);
                Intent i = new Intent(getApplicationContext(), ReviewActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        setting_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), SettingActivity.class);
                i.putExtra("setting_start", false);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                windowManager.removeView(view);
//                stopSelf();
//                Toast.makeText(getApplicationContext(), "record setting", Toast.LENGTH_SHORT).show();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (view != null) windowManager.removeView(view);
        Settings.System.putInt(getContentResolver(), "show_touches", 0);
        //stopSelf();
    }

}