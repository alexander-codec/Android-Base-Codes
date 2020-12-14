package com.mp3player.searchonline;
/**
 * Created by Usman Jamil on 02/02/2017.
 * Usmans.net
 * Skype usman.jamil78
 * email usmanjamil547@gmail.com
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by Usman Jamil on 11/12/2015.
 */
public class PlayerActivity extends Activity implements OnClickListener  {
        MediaPlayer mp;
    TextView title,sduration,currenttime,ld;
    String urlv,titlev,artworkv;
    ImageView aw,btnplay;
    SeekBar sb;
    private Handler durationHandler = new Handler();
    protected PowerManager.WakeLock mWakeLock;
    volatile boolean stop = false;
    boolean pause = false;
    boolean buffer=true;

    Thread updateSeekBar;
   // int ;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Music Player");
        setContentView(R.layout.playerdialog);


        title = (TextView)findViewById(R.id.title);
        sduration = (TextView)findViewById(R.id.duration);
        currenttime = (TextView)findViewById(R.id.currentTime);
        ld = (TextView)findViewById(R.id.ld);

        btnplay=(ImageView)findViewById(R.id.btnplay);
        aw=(ImageView)findViewById(R.id.img_songs);
        sb=(SeekBar)findViewById(R.id.seekBar);
//Load Ad

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("BE9BB532808E71B746B8C96E58F9576D").addTestDevice("FFCE8C2A9F940F24B9B69E0D0728064C").build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();

         urlv=extras.getString("STREAM");
        titlev=extras.getString("TITLE");
        artworkv=extras.getString("ARTWORK");


        btnplay.setOnClickListener(this);



        if (mp != null) {
            mp.reset();
            mp.release();
            mp = null;
        }
        title.setText(titlev);
//        duration.setText(durationv);
        Picasso.with(getApplicationContext()).load(artworkv).placeholder(R.drawable.ic_my_library_music_blue_500_24dp).transform(new CircleTransform()).into(aw);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                currenttime.setText(milliSecondsToTimer(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(mp.isPlaying()) {
                    mp.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(!pause) {
                    mp.start();
                }
                mp.seekTo(seekBar.getProgress());
            }
        });


        updateSeekBar = new Thread(){
            @SuppressLint("SetTextI18n")
            @Override
            public  void  run(){
                int totalduartion=mp.getDuration();
                int currentpostion=0;
                sb.setMax(totalduartion);
                while(currentpostion < totalduartion &&  !this.isInterrupted()){
                    try {


                            sleep(500);

                        if(mp.isPlaying()) {
                            currentpostion = mp.getCurrentPosition();
                            sb.setProgress(currentpostion);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        };


        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new Player().execute(extras.getString("STREAM"));



    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
            case R.id.btnplay :
                if(mp != null && mp.isPlaying()){

                    mp.pause();
                    pause=true;
                    btnplay.setImageResource(R.drawable.ic_play_arrow_black_18dp);
                    ld.setText("Paused!");
                }else{

                   if(!buffer) {
                       pause = false;
                       ld.setText("Playing..");
                       mp.start();
                       btnplay.setImageResource(R.drawable.ic_pause_black_18dp);
                   }else{
                       Toast.makeText(getApplicationContext(),"Still Buffering",Toast.LENGTH_LONG).show();
                   }

                }
                break;

        }

    }


    class Player extends AsyncTask<String, Void, Boolean> {
        //private ProgressDialog progress;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            Boolean prepared;
            try {

                mp.setDataSource(params[0]);

                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        mp.stop();
                        pause=true;
                        mp.seekTo(0);
                      //  mp.reset();
                        durationHandler.removeCallbacksAndMessages(null);
                        ld.setText("Stopped!");


                    }
                });
                mp.prepare();
                prepared = true;
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                Log.d("IllegarArgument", e.getMessage());
                prepared = false;
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            Log.d("Prepared", "//" + result);
            mp.start();
            ld.setText("Playing...");
            buffer=false;
          // durationHandler.postDelayed(updateSeekBarTime, 1000);
            updateSeekBar.start();
            int TDuration = mp.getDuration();
            sduration.setText(milliSecondsToTimer(TDuration));

            mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int progress) {
                    if (progress < sb.getMax()) {
                        sb.setSecondaryProgress(progress * 5000);

                    }
                }
            });


        }

        public Player() {
           // progress = new ProgressDialog(MainActivity.this);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
          // this.progress.setMessage("Buffering...");
            //this.progress.show();

        }
    }
  //  btnplay.setClickable(true);


    public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + 0+minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();


        if (mp != null) {
            mp.reset();
            mp.release();
            mp = null;
        }
        durationHandler.removeCallbacksAndMessages(null);
    }
    protected void onStop() {
        super.onStop();


        durationHandler.removeCallbacksAndMessages(null);
       // this.mWakeLock.release();
        super.onStop();
        if (mp != null) {
            mp.reset();
            mp.release();
            mp = null;
            mp.stop();
        }
    }

    @Override
    public void onDestroy() {

//        this.mWakeLock.release();
        super.onDestroy();
        if (mp != null) {
            mp.reset();
            mp.release();
            mp = null;
        }
    }

}
