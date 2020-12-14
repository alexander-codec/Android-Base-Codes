package com.example.hatiboy.gpcapture;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by HATIBOY on 7/29/2015.
 */
public class BroadcastReceiverStopRecord extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            RecordActivity.releaseEncoders();
            Toast.makeText(context, context.getString(R.string.record_stop), Toast.LENGTH_LONG).show();
//        context.startService(new Intent(context, FloatingView.class));
            Settings.System.putInt(context.getContentResolver(), "show_touches", 0);

            FloatingView.windowManager.addView(FloatingView.view, FloatingView.params);
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
    }
}
