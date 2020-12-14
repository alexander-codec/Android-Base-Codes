package fast.battery.charger.app;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdScrollView;
import com.facebook.ads.NativeAdView;
import com.facebook.ads.NativeAdsManager;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AboutActivity extends AppCompatActivity implements NativeAdsManager.Listener {

    Toolbar toolbar;
    CardView about_1, mCardViewShare, CardViewRate;
    Button btn_feedback, btn_ratenow;
    LinearLayout layout;
    NativeAdsManager manager;
    NativeAdScrollView scrollView;
    Intent i;
    private AdView adView;


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setInitialConfiguration();
        setScreenElements();

        layout = (LinearLayout) findViewById(R.id.BannerAd);
        AdSettings.addTestDevice("61dc0d19895f5c64f7013fa7ee898521");
        adView = new AdView(this, getResources().getString(R.string.Ad_About_Activity_banner), AdSize.BANNER_HEIGHT_50);
        layout.addView(adView);
        adView.loadAd();

        manager = new NativeAdsManager(this, getResources().getString(R.string.Ad_About_Activity_native), 2);
        manager.setListener(AboutActivity.this);
        manager.loadAds(NativeAd.MediaCacheFlag.ALL);
    }

    private void setInitialConfiguration() {
        getSupportActionBar().setTitle("Sobre o ccarregamento rapido e economico");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void setScreenElements() {
        about_1 = (CardView) findViewById(R.id.about_1);
        mCardViewShare = (CardView) findViewById(R.id.CardViewShare);
        btn_feedback = (Button) findViewById(R.id.btn_feedbak);
        btn_ratenow = (Button) findViewById(R.id.btn_ratenow);
        CardViewRate = (CardView) findViewById(R.id.CardViewRate);

        about_1.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        mCardViewShare.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        CardViewRate.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        about_1.setCardElevation(0);
        CardViewRate.setCardElevation(0);
        mCardViewShare.setCardElevation(0);

        about_1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("market://search?q=kinemarts")); // ADD YOUR DEVELOPER NAME HERE USE + FOR SPACE
                if (getPackageManager().queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY).size() >= 1) {
                    startActivity(i);
                }
            }
        });

        btn_ratenow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri
                            .parse("market://details?id=" + getPackageName())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });

        btn_feedback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int height = displaymetrics.heightPixels;
                int width = displaymetrics.widthPixels;
                PackageManager manager = getApplicationContext().getPackageManager();
                PackageInfo info = null;
                try {
                    info = manager.getPackageInfo(getPackageName(), 0);
                } catch (NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String version = info.versionName;

                i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"contato@kinemarts.com.br"}); // PUT YOUR EMAIL HERE
                i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name)  + version);
                i.putExtra(Intent.EXTRA_TEXT,
                        "\n" + " Dispositivo :" + getDeviceName() +
                                "\n" + " Android:" + Build.VERSION.SDK_INT +
                                "\n" + " Display Height  :" + height + "px" +
                                "\n" + " Display Width  :" + width + "px" +
                                "\n\n" + " Por favor informe problema a ser corrigido .." +
                                "\n");

                startActivity(Intent.createChooser(i, "Enviar Email"));
            }
        });

        mCardViewShare.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                i = new Intent();
                i.setAction(Intent.ACTION_SEND);
                i.setType("text/plain");
                final String text = "Conheça um novo aplicativo com "
                        + getResources().getString(R.string.app_name)
                        + ", o aplicativo e gratuito e você pode baixar em https://play.google.com/store/apps/details?id="
                        + getPackageName();
                i.putExtra(Intent.EXTRA_TEXT, text);
                Intent sender = Intent.createChooser(i, "Compartilhar " + getResources().getString(R.string.app_name));
                startActivity(sender);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onAdsLoaded() {
        if (scrollView != null) {
            ((LinearLayout) findViewById(R.id.NativeAdContainer)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.NativeAdContainer)).removeView(scrollView);
        }

        scrollView = new NativeAdScrollView(AboutActivity.this, manager,
                NativeAdView.Type.HEIGHT_300);
        ((LinearLayout) findViewById(R.id.NativeAdContainer)).setVisibility(View.VISIBLE);
        ((LinearLayout) findViewById(R.id.NativeAdContainer)).addView(scrollView);
    }

    @Override
    public void onAdError(AdError adError) {

    }


}
