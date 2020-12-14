package fast.battery.charger.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdScrollView;
import com.facebook.ads.NativeAdView;
import com.facebook.ads.NativeAdsManager;

import java.lang.reflect.Method;

public class CloseAllTools extends AppCompatActivity implements View.OnClickListener, NativeAdsManager.Listener {

    CardView LocationCardView, AroplaneCardView, MoblieDataCardView;
    Toolbar toolbar;
    Button LocationTurnOff, AroplaneTurnOn, MoblieDataTurnOff;
    LocationManager ManagerForLocation;
    Boolean statusOfLocation;
    TextView txtNoMoreIssue;
    LinearLayout layout;
    NativeAdsManager manager;
    NativeAdScrollView scrollView;
    private AdView adView;
    Integer SetValue;

    TextView TxtTitleLocation, txtDicLocation, TxtTitleAroplane, txtDicAroplane, TxtTitleMobileData, txtDicMobileData;

    private static boolean isAirplaneModeOn(Context context) {

        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SetValue = getIntent().getIntExtra("SetValue", 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_close_all_tools);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if (SetValue == 0) {
            getSupportActionBar().setTitle("Reduzir descarga");
        } else {
            getSupportActionBar().setTitle("Carregamento Rapido");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LocationCardView = (CardView) findViewById(R.id.LocationCardView);
        AroplaneCardView = (CardView) findViewById(R.id.AroplaneCardView);
        MoblieDataCardView = (CardView) findViewById(R.id.MoblieDataCardView);
        LocationTurnOff = (Button) findViewById(R.id.LocationTurnOff);
        AroplaneTurnOn = (Button) findViewById(R.id.AroplaneTurnOn);
        MoblieDataTurnOff = (Button) findViewById(R.id.MoblieDataTurnOff);
        txtNoMoreIssue = (TextView) findViewById(R.id.txtNoMoreIssue);
        TxtTitleLocation = (TextView) findViewById(R.id.TxtTitleLocation);
        txtDicLocation = (TextView) findViewById(R.id.txtDicLocation);
        TxtTitleAroplane = (TextView) findViewById(R.id.TxtTitleAroplane);
        txtDicAroplane = (TextView) findViewById(R.id.txtDicAroplane);
        TxtTitleMobileData = (TextView) findViewById(R.id.TxtTitleMobileData);
        txtDicMobileData = (TextView) findViewById(R.id.txtDicMobileData);
        LocationCardView.setOnClickListener(this);
        AroplaneCardView.setOnClickListener(this);
        MoblieDataCardView.setOnClickListener(this);
        LocationTurnOff.setOnClickListener(this);
        AroplaneTurnOn.setOnClickListener(this);
        MoblieDataTurnOff.setOnClickListener(this);


        SetCarViewColor();

        CheckWhatOn();
        layout = (LinearLayout) findViewById(R.id.BannerAd);
        AdSettings.addTestDevice("61dc0d19895f5c64f7013fa7ee898521");
        adView = new AdView(this, getResources().getString(R.string.Ad_Close_All_Tools_banner), AdSize.BANNER_HEIGHT_50);
        layout.addView(adView);
        adView.loadAd();
        manager = new NativeAdsManager(this, getResources().getString(R.string.Ad_Close_All_Tools_native), 2);
        manager.setListener(CloseAllTools.this);
        manager.loadAds(NativeAd.MediaCacheFlag.ALL);


        if (SetValue == 0) {
            TxtTitleLocation.setText("2x Reduzir descarga");
            txtDicLocation.setText("Desative o serviço de localização e descarga da bateria reduz 2x");
            TxtTitleAroplane.setText("3x Reduzir descarga");
            txtDicAroplane.setText("Ative modo avião e descarga da bateria reduz 3x");
            TxtTitleMobileData.setText("2x Reduzir descarga");
            txtDicMobileData.setText("Desative o serviço de dados moveis e descarga da bateria reduz 2x");
        } else {
            TxtTitleLocation.setText("2x Carregamento Rapido");
            txtDicLocation.setText("Desative o serviço de localização e carregamento é 2x mais rapido");
            TxtTitleAroplane.setText("3x Carregamento Rapido");
            txtDicAroplane.setText("Ative modo avião e carregamento é 3x mais rapido");
            TxtTitleMobileData.setText("2x Carregamento Rapido");
            txtDicMobileData.setText("Desative o serviço de dados moveis e carregamento é 2x mais rapido");
        }

    }

    private void SetCarViewColor() {

        LocationCardView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        AroplaneCardView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        MoblieDataCardView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        LocationCardView.setCardElevation(0);
        AroplaneCardView.setCardElevation(0);
        MoblieDataCardView.setCardElevation(0);

    }

    private void CheckWhatOn() {

        if (!isMobileDataEnabled()) {
            MoblieDataCardView.setVisibility(View.GONE);
        }

        ManagerForLocation = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        statusOfLocation = ManagerForLocation.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!statusOfLocation) {
            LocationCardView.setVisibility(View.GONE);
        }

        if (isAirplaneModeOn(getApplicationContext())) {
            AroplaneCardView.setVisibility(View.GONE);
        }

        if (LocationCardView.getVisibility() == View.GONE && AroplaneCardView.getVisibility() == View.GONE && MoblieDataCardView.getVisibility() == View.GONE) {
            txtNoMoreIssue.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.LocationCardView:

                SetUpLocationIntent();

                break;

            case R.id.AroplaneCardView:

                SetUpAroplaneIntent();

                break;

            case R.id.MoblieDataCardView:

                SetUpMobileDataIntent();

                break;

            case R.id.LocationTurnOff:

                SetUpLocationIntent();

                break;

            case R.id.AroplaneTurnOn:

                SetUpAroplaneIntent();

                break;

            case R.id.MoblieDataTurnOff:

                SetUpMobileDataIntent();

                break;
        }
    }

    private void SetUpMobileDataIntent() {

        Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void SetUpAroplaneIntent() {
        Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        startActivity(intent);
    }

    private void SetUpLocationIntent() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    public Boolean isMobileDataEnabled() {
        Object connectivityService = getSystemService(Context.CONNECTIVITY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) connectivityService;

        try {
            Class<?> c = Class.forName(cm.getClass().getName());
            Method m = c.getDeclaredMethod("getMobileDataEnabled");
            m.setAccessible(true);
            return (Boolean) m.invoke(cm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        CheckWhatOn();
    }

    @Override
    public void onPause() {
        super.onPause();
        CheckWhatOn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;


            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public void onAdsLoaded() {
        if (scrollView != null) {
            ((LinearLayout) findViewById(R.id.NativeAdContainer)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.NativeAdContainer)).removeView(scrollView);
        }

        scrollView = new NativeAdScrollView(CloseAllTools.this, manager,
                NativeAdView.Type.HEIGHT_300);
        ((LinearLayout) findViewById(R.id.NativeAdContainer)).setVisibility(View.VISIBLE);
        ((LinearLayout) findViewById(R.id.NativeAdContainer)).addView(scrollView);
    }

    @Override
    public void onAdError(AdError adError) {

    }

}



