package com.L2.changemyvoice;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.L2.changemyvoice.constants.AppConstants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends Activity
{
  public static Context Context;
  static AudioTrack audioTrack;
  static File file = new File(Environment.getExternalStorageDirectory(), "test.pcm");;
  static Boolean recording;
  static Spinner spFrequency;
  private ArrayAdapter<String> adapter;
  Animation animRotate;
  ImageView imageView1;
  ImageView playBack;
  ImageView startRec;
  Integer[] freqset = {6050, 8500, 11025, 16000, 22050,
          32000, 37800, 44056, 44100};
  private InterstitialAd interstitial;
 

  void playRecord(){
		
	     int i = 0;
	    String str = (String)spFrequency.getSelectedItem();
	    
	    
	file = new File(Environment.getExternalStorageDirectory(), "test.pcm");
		
      int shortSizeInBytes = Short.SIZE/Byte.SIZE;
		
		int bufferSizeInBytes = (int)(file.length()/shortSizeInBytes);
		short[] audioData = new short[bufferSizeInBytes];
		
		try {
			InputStream inputStream = new FileInputStream(file);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
			DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);
			
			int j = 0;
			while(dataInputStream.available() > 0){
				audioData[j] = dataInputStream.readShort();
				j++;
			}
			
			dataInputStream.close();
			if (str.equals(getString(R.string.vamp)))
			{
			  i = 5000;
			}
			if (str.equals(getString(R.string.slow_motion)))
            {
              i = 6050;
            }
            if (str.equals(getString(R.string.robot)))
            {
              i = 8500;
            }
            if (str.equals(getString(R.string.normal)))
            {
              i = 11025;
            }
            if (str.equals(getString(R.string.chipmunk)))
            {
              i = 16000;
            }
            if (str.equals(getString(R.string.funny)))
            {
              i = 22050;
            }
            if (str.equals(getString(R.string.bee)))
            {
              i = 41000;
            }
            if (str.equals(getString(R.string.elephent)))
            {
              i = 30000;
            }
			
			audioTrack = new AudioTrack(3,
					i,
					2,
					2,
					bufferSizeInBytes,
					1);
			
			audioTrack.play();
			audioTrack.write(audioData, 0, bufferSizeInBytes);

			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

}  

  private void startRecord(){

		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "test.pcm"); 
		
		try {
			file.createNewFile();
			
			OutputStream outputStream = new FileOutputStream(file);
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
			DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);
			
			int minBufferSize = AudioRecord.getMinBufferSize(11025, 2, 2);
			
			short[] audioData = new short[minBufferSize];
			
			AudioRecord audioRecord = new AudioRecord(1, 11025, 2, 2,minBufferSize);
			
			audioRecord.startRecording();
			
			while(recording){
				int numberOfShort = audioRecord.read(audioData, 0, minBufferSize);
				for(int i = 0; i < numberOfShort; i++){
					dataOutputStream.writeShort(audioData[i]);
				}
			}
			if (!recording.booleanValue())
	        {
			audioRecord.stop();
			dataOutputStream.close();
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

  public void onBackPressed()
  {
    super.onBackPressed();
    finish();
  }

  @SuppressWarnings({ "rawtypes" })
public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_main);
    String[] arrayOfString = new String[8];
    arrayOfString[0] = getApplicationContext().getString(R.string.vamp);
    arrayOfString[1] = getApplicationContext().getString(R.string.slow_motion);
    arrayOfString[2] = getApplicationContext().getString(R.string.robot);
    arrayOfString[3] = getApplicationContext().getString(R.string.normal);
    arrayOfString[4] = getApplicationContext().getString(R.string.chipmunk);
    arrayOfString[5] = getApplicationContext().getString(R.string.funny);
    arrayOfString[6] = getApplicationContext().getString(R.string.bee);
    arrayOfString[7] = getApplicationContext().getString(R.string.elephent);
    startRec = ((ImageView)findViewById(R.id.startrec));
    imageView1 = ((ImageView)findViewById(R.id.imageView3));
    playBack = ((ImageView)findViewById(R.id.playback));
    startRec.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent localIntent = new Intent(getApplicationContext(), RecordingDailog.class);
		      startActivity(localIntent);
		      displayInterstitial();
		     
		        new Thread(new Runnable()
		        {
		          public void run()
		          {
		            recording = Boolean.valueOf(true);
		            startRecord();
		          }
		        })
		        .start();
		      }
	});
    playBack.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (file.exists())
		           playRecord();
		}
	});
    spFrequency = (Spinner)findViewById(R.id.frequency);
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayOfString);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spFrequency.setAdapter(this.adapter);
    imageView1.setVisibility(0);
    
 // Prepare the Interstitial Ad
 		interstitial = new InterstitialAd(MainActivity.this);
 		// Insert the Ad Unit ID
 		interstitial.setAdUnitId(AppConstants.interstitial_ad_id);
  
 		//Locate the Banner Ad in activity_main.xml
 		AdView adView = (AdView) this.findViewById(R.id.adView);
  
 		// Request for Ads
 		AdRequest adRequest = new AdRequest.Builder()
 				.build();
  
 		// Load ads into Banner Ads
 		adView.loadAd(adRequest);
  
 		// Load ads into Interstitial Ads
 		interstitial.loadAd(adRequest);
  
 		// Prepare an Interstitial Ad Listener
 		interstitial.setAdListener(new AdListener() {
 			public void onAdLoaded() {
 				// Call displayInterstitial() function
 			}
 		});
  }

  protected void onDestroy()
  {
    super.onDestroy();
    recording = Boolean.valueOf(false);
    if (audioTrack != null)
    {
    	audioTrack.release();
    }
  }
  
  public void displayInterstitial() {
		// If Ads are loaded, show Interstitial else show nothing.
		if (interstitial.isLoaded()) {
			interstitial.show();
		}
	}
  
	@Override
	public void onResume() 
	{
		super.onResume();
	}

	@Override
	public void onPause() 
	{
		super.onPause();
	}
}
