package com.L2.changemyvoice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordingDailog extends Activity
  implements Animation.AnimationListener
{
  Animation animRotate;
  ImageView imgLogo;
  public void onAnimationEnd(Animation paramAnimation)
  {
    imgLogo.startAnimation(this.animRotate);
  }

  public void onAnimationRepeat(Animation paramAnimation)
  {
  }

  public void onAnimationStart(Animation paramAnimation)
  {
  }

  public void onBackPressed()
  {
    super.onBackPressed();
    MainActivity.recording = Boolean.valueOf(false);
    finish();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.record);
    TextView localButton = (TextView)findViewById(R.id.btncancel);
    imgLogo = ((ImageView)findViewById(R.id.imgLogo));
    animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
    animRotate.setAnimationListener(this);
    imgLogo.setVisibility(0);
    imgLogo.startAnimation(this.animRotate);
    
    localButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MainActivity.recording = Boolean.valueOf(false);
        RecordingDailog.this.finish();
      }
    });
  }
}
