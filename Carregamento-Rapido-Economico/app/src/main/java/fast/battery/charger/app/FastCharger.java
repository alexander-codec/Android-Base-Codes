package fast.battery.charger.app;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdScrollView;
import com.facebook.ads.NativeAdView;
import com.facebook.ads.NativeAdsManager;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.fanrunqi.waveprogress.WaveProgressView;

import static android.media.AudioManager.RINGER_MODE_NORMAL;
import static android.media.AudioManager.RINGER_MODE_SILENT;
import static android.media.AudioManager.RINGER_MODE_VIBRATE;

public class FastCharger extends AppCompatActivity implements View.OnClickListener, NativeAdsManager.Listener {

    Toolbar toolbar;
    WaveProgressView waveProgressView;
    BroadcastReceiver batteryLevelReceiver;
    ImageView FirstTickleProcess, SecondTickleProcess, ThirdTickleProcess, Tools_WiFi, Tools_Timeout, Tools_Brightness, Tools_Bluetooth, Tools_Mode, Tools_Rotate;
    Animation Rotate;
    BluetoothAdapter AdapterForBluetooth;
    RelativeLayout main_lout_toolsview, main_lout_tickleview;
    Button StartFastCharger;
    private int brightness;
    private int rotate;
    private int timeout;
    private ContentResolver cResolver;
    private Window window;
    AudioManager am;
    RelativeLayout beforeFullCharge, afterFullCharge;
    LinearLayout NativeAdContainer;
    Button btn_feedback, btn_ratenow;
    Intent i;

    CardView CardViewRate, CardViewBattery, CardViewTickleview, CardViewTools, mCardViewShare;
    RelativeLayout ratenow, sharenow;


    RelativeLayout AlertLout;
    TextView mAlertText;
    TextView PowerType;
    Integer Issue = 0;

    NativeAdsManager manager;
    NativeAdScrollView scrollView;

    private AdView adView;
    LinearLayout layout;

    Integer Profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fast_charger);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        cResolver = getContentResolver();

        window = getWindow();


        initilizeVariables();
        getBatteryPercentage();
        CheckOnAndOff();
        SetClickListner();
        CheckIntentToolsOnOrOff();
        setColorToCardview();


        layout = (LinearLayout) findViewById(R.id.BannerAd);
        AdSettings.addTestDevice("61dc0d19895f5c64f7013fa7ee898521");
        adView = new AdView(this, getResources().getString(R.string.Ad_FastCharger_banner), AdSize.BANNER_HEIGHT_50);
        layout.addView(adView);
        adView.loadAd();

        manager = new NativeAdsManager(this, getResources().getString(R.string.Ad_Fast_Charger_native), 2);
        manager.setListener(FastCharger.this);
        manager.loadAds(NativeAd.MediaCacheFlag.ALL);

        Rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise);

        cResolver = getContentResolver();
        window = getWindow();

        try {
            Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
            rotate = Settings.System.getInt(cResolver, Settings.System.ACCELEROMETER_ROTATION);
            timeout = Settings.System.getInt(cResolver, Settings.System.SCREEN_OFF_TIMEOUT);

            if (timeout > 40000) {
                setTimeout(3);
                timeout = 40000;
            }


        } catch (Settings.SettingNotFoundException e) {
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }


    }


    private void SetClickListner() {
        Tools_WiFi.setOnClickListener(this);
        StartFastCharger.setOnClickListener(this);
        Tools_Mode.setOnClickListener(this);
        Tools_Bluetooth.setOnClickListener(this);
        Tools_Brightness.setOnClickListener(this);
        Tools_Timeout.setOnClickListener(this);
        Tools_Rotate.setOnClickListener(this);
        mCardViewShare.setOnClickListener(this);
        btn_feedback.setOnClickListener(this);
        btn_ratenow.setOnClickListener(this);
        AlertLout.setOnClickListener(this);
    }

    private void initilizeVariables() {

        waveProgressView = (WaveProgressView) findViewById(R.id.waveProgressbar);
        FirstTickleProcess = (ImageView) findViewById(R.id.FirstTickleProcess);
        SecondTickleProcess = (ImageView) findViewById(R.id.SecondTickleProcess);
        ThirdTickleProcess = (ImageView) findViewById(R.id.ThirdTickleProcess);
        Tools_WiFi = (ImageView) findViewById(R.id.tool_wifi);
        Tools_Rotate = (ImageView) findViewById(R.id.tool_rotate);
        Tools_Brightness = (ImageView) findViewById(R.id.tool_brightness);
        Tools_Bluetooth = (ImageView) findViewById(R.id.tool_bluetooth);
        Tools_Mode = (ImageView) findViewById(R.id.tool_mode);
        Tools_Timeout = (ImageView) findViewById(R.id.tool_timeout);
        StartFastCharger = (Button) findViewById(R.id.StartFastCharger);
        main_lout_toolsview = (RelativeLayout) findViewById(R.id.main_lout_toolsview);
        main_lout_tickleview = (RelativeLayout) findViewById(R.id.main_lout_tickleview);
        beforeFullCharge = (RelativeLayout) findViewById(R.id.beforeFullCharge);
        afterFullCharge = (RelativeLayout) findViewById(R.id.afterFullCharge);
        CardViewRate = (CardView) findViewById(R.id.CardViewRate);
        CardViewBattery = (CardView) findViewById(R.id.CardViewBattery);
        CardViewTickleview = (CardView) findViewById(R.id.CardViewTickleview);
        CardViewTools = (CardView) findViewById(R.id.CardViewTools);
        NativeAdContainer = (LinearLayout) findViewById(R.id.NativeAdContainer);
        mCardViewShare = (CardView) findViewById(R.id.CardViewShare);
        ratenow = (RelativeLayout) findViewById(R.id.ratenow);
        sharenow = (RelativeLayout) findViewById(R.id.sharenow);
        btn_feedback = (Button) findViewById(R.id.btn_feedbak);
        btn_ratenow = (Button) findViewById(R.id.btn_ratenow);
        AlertLout = (RelativeLayout) findViewById(R.id.AlertLout);
        mAlertText = (TextView) findViewById(R.id.AlertText);
        PowerType = (TextView) findViewById(R.id.PowerType);


    }

    private void setColorToCardview() {
        CardViewRate.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        CardViewBattery.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        CardViewTickleview.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        CardViewTools.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        mCardViewShare.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        CardViewBattery.setCardElevation(0);
        CardViewTools.setCardElevation(0);
        CardViewTickleview.setCardElevation(0);
        CardViewRate.setCardElevation(0);
        mCardViewShare.setCardElevation(0);
    }

    public void CheckIntentToolsOnOrOff() {

        Issue = 0;

        if (isMobileDataEnabled()) {
            Issue = Issue + 1;
        }

        LocationManager ManagerForLocation = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Boolean statusOfLocation = ManagerForLocation.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (statusOfLocation) {
            Issue = Issue + 1;
        }

        if (!isAirplaneModeOn(getApplicationContext())) {
            Issue = Issue + 1;
        }

        mAlertText.setText(String.valueOf(Issue));

        if (Issue == 0) {
            AlertLout.setVisibility(View.GONE);
        }
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

    private static boolean isAirplaneModeOn(Context context) {

        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;

    }


    private void getBatteryPercentage() {
        batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == Intent.ACTION_BATTERY_CHANGED) {
                    int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                    int level = -1;
                    if (currentLevel >= 0 && scale > 0) {
                        level = (currentLevel * 100) / scale;
                        Log.e("%", "" + level);
                    }

                    waveProgressView.setMaxProgress(100);
                    waveProgressView.setCurrent(level, String.valueOf(level) + "%");
                    waveProgressView.setWave(8, 130);
                    waveProgressView.setText("#4D000000", 70);
                    waveProgressView.setWaveColor("#4D76FF03");
                    waveProgressView.setmWaveSpeed(15);

                    FirstTickleProcess.setVisibility(View.GONE);
                    SecondTickleProcess.setVisibility(View.GONE);
                    ThirdTickleProcess.setVisibility(View.GONE);

                    if (level <= 90) {
                        FirstTickleProcess.startAnimation(Rotate);
                        SecondTickleProcess.clearAnimation();
                        ThirdTickleProcess.clearAnimation();
                        FirstTickleProcess.setVisibility(View.VISIBLE);
                        SecondTickleProcess.setVisibility(View.GONE);
                        ThirdTickleProcess.setVisibility(View.GONE);
                    }
                    if (level <= 98 && level >= 91) {
                        SecondTickleProcess.startAnimation(Rotate);
                        FirstTickleProcess.clearAnimation();
                        ThirdTickleProcess.clearAnimation();
                        SecondTickleProcess.setVisibility(View.VISIBLE);
                        FirstTickleProcess.setVisibility(View.GONE);
                        ThirdTickleProcess.setVisibility(View.GONE);
                    }
                    if (level >= 99) {
                        ThirdTickleProcess.startAnimation(Rotate);
                        FirstTickleProcess.clearAnimation();
                        SecondTickleProcess.clearAnimation();
                        ThirdTickleProcess.setVisibility(View.VISIBLE);
                        FirstTickleProcess.setVisibility(View.GONE);
                        SecondTickleProcess.setVisibility(View.GONE);
                    }

                    switch (status) {
                        case BatteryManager.BATTERY_PLUGGED_AC:
                            PowerType.setText("AC");
                            break;
                        case BatteryManager.BATTERY_PLUGGED_USB:
                            PowerType.setText("USB");
                            break;
                        case BatteryManager.BATTERY_STATUS_FULL:
                            ChangeStateFullCharged();
                            break;

                    }
                }
                if (intent.getAction() == Intent.ACTION_POWER_DISCONNECTED) {
                    context.unregisterReceiver(this);
                    finish();
                }


            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        IntentFilter PowerDisconnectFilter = new IntentFilter(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
        registerReceiver(batteryLevelReceiver, PowerDisconnectFilter);

    }

    private void ChangeStateFullCharged() {
        beforeFullCharge.setVisibility(View.GONE);
        afterFullCharge.setVisibility(View.VISIBLE);


        final Animation slideinforratenow = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        final Animation slideinforsharenow = AnimationUtils.loadAnimation(this, R.anim.slide_in);

        CardViewRate.setVisibility(View.VISIBLE);
        CardViewRate.startAnimation(slideinforratenow);
        slideinforratenow.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mCardViewShare.setVisibility(View.VISIBLE);
                mCardViewShare.startAnimation(slideinforsharenow);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void CheckOnAndOff() {

        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            Tools_WiFi.setImageResource(R.drawable.ic_tool_wifi_on);
        } else {
            Tools_WiFi.setImageResource(R.drawable.ic_tool_wifi_off);
        }

        if (rotate == 1) {
            Tools_Rotate.setImageResource(R.drawable.ic_tool_rotate_autorotate);
        } else {
            Tools_Rotate.setImageResource(R.drawable.ic_tool_rotate_portiat);
        }

        AdapterForBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (AdapterForBluetooth == null) {
        } else {
            if (AdapterForBluetooth.isEnabled()) {
                Tools_Bluetooth.setImageResource(R.drawable.ic_tool_bluetooth_on);
            } else {
                Tools_Bluetooth.setImageResource(R.drawable.ic_tool_bluetooth_off);
            }
        }

        if (brightness > 20) {
            Tools_Brightness.setImageResource(R.drawable.ic_tool_brightness_on);
        } else {
            Tools_Brightness.setImageResource(R.drawable.ic_tool_brightness_off);
        }


        if (timeout == 10000) {
            Tools_Timeout.setImageResource(R.drawable.ic_tool_timeout_ten);
        } else if (timeout == 20000) {
            Tools_Timeout.setImageResource(R.drawable.ic_tool_timeout_twenty);
        } else if (timeout == 30000) {
            Tools_Timeout.setImageResource(R.drawable.ic_tool_timeout_thirty);
        } else if (timeout == 40000) {
            Tools_Timeout.setImageResource(R.drawable.ic_tool_timeout_fourty);
        } else {
            Tools_Timeout.setImageResource(R.drawable.ic_tool_timeout_fourty);
            setTimeout(3);
            timeout = 40000;
        }

        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        switch (am.getRingerMode()) {
            case RINGER_MODE_SILENT:
                Tools_Mode.setImageResource(R.drawable.ic_tool_profile_silent);
                Profile = RINGER_MODE_SILENT;
                break;
            case RINGER_MODE_VIBRATE:
                Tools_Mode.setImageResource(R.drawable.ic_tool_profile_vibrate);
                Profile = RINGER_MODE_VIBRATE;
                break;
            case RINGER_MODE_NORMAL:
                Tools_Mode.setImageResource(R.drawable.ic_tool_profile_normal);
                Profile = RINGER_MODE_NORMAL;
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckOnAndOff();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CheckOnAndOff();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        CheckOnAndOff();
    }

    @Override
    protected void onDestroy() {
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(Profile);
        setTimeout(3);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.StartFastCharger:


                final Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

                final Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);


                CardViewTools.startAnimation(slide_down);
                slide_down.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        CardViewTools.setVisibility(View.GONE);
                        CardViewTickleview.setVisibility(View.VISIBLE);
                        CardViewTickleview.startAnimation(slide_up);

                        StartFastChargerMethod();


                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


                break;

            case R.id.tool_wifi:
                WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(false);
                Tools_WiFi.setImageResource(R.drawable.ic_tool_wifi_off);
                break;

            case R.id.tool_rotate:

                if (rotate == 1) {

                    Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, true ? 0 : 1);
                    Tools_Rotate.setImageResource(R.drawable.ic_tool_rotate_portiat);
                    rotate = 1;
                }

                break;


            case R.id.tool_mode:

                am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                switch (am.getRingerMode()) {
                    case RINGER_MODE_SILENT:
                        break;
                    case RINGER_MODE_VIBRATE:
                        Tools_Mode.setImageResource(R.drawable.ic_tool_profile_silent);
                        am.setRingerMode(RINGER_MODE_SILENT);
                        break;
                    case RINGER_MODE_NORMAL:
                        Tools_Mode.setImageResource(R.drawable.ic_tool_profile_silent);
                        am.setRingerMode(RINGER_MODE_SILENT);
                        break;

                    default:
                        Tools_Mode.setImageResource(R.drawable.ic_tool_profile_silent);
                        am.setRingerMode(RINGER_MODE_SILENT);
                }


                break;

            case R.id.tool_bluetooth:

                AdapterForBluetooth = BluetoothAdapter.getDefaultAdapter();
                if (AdapterForBluetooth == null) {
                } else {
                    if (AdapterForBluetooth.isEnabled()) {
                        Tools_Bluetooth.setImageResource(R.drawable.ic_tool_bluetooth_off);
                        AdapterForBluetooth.disable();
                    }
                }
                break;

            case R.id.tool_brightness:

                if (brightness > 20) {
                    Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, 20);
                    WindowManager.LayoutParams layoutpars = window.getAttributes();
                    layoutpars.screenBrightness = 20;
                    window.setAttributes(layoutpars);
                    Tools_Brightness.setImageResource(R.drawable.ic_tool_brightness_off);
                    brightness = 20;
                }

                break;

            case R.id.tool_timeout:

                if (timeout == 10000) {
                    Tools_Timeout.setImageResource(R.drawable.ic_tool_timeout_ten);
                    setTimeout(0);
                    timeout = 10000;
                } else {
                    Tools_Timeout.setImageResource(R.drawable.ic_tool_timeout_ten);
                    setTimeout(0);
                    timeout = 10000;
                }

                break;

            case R.id.CardViewShare:

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

                break;

            case R.id.btn_feedbak:

                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int height = displaymetrics.heightPixels;
                int width = displaymetrics.widthPixels;

                PackageManager manager = getApplicationContext()
                        .getPackageManager();
                PackageInfo info = null;
                try {
                    info = manager.getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String version = info.versionName;

                i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"contato@kinemarts.com.br"});  // PUT YOUR EMAIL HERE
                i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name)  + version);
                i.putExtra(Intent.EXTRA_TEXT,
                        "\n" + " Dispositivo :" + getDeviceName() +
                                "\n" + " Android:" + Build.VERSION.SDK_INT +
                                "\n" + " Display Height  :" + height + "px" +
                                "\n" + " Display Width  :" + width + "px" +
                                "\n\n" + " Por favor informe problema a ser corrigido .." +
                                "\n");

                startActivity(Intent.createChooser(i, "Enviar Email"));

                break;

            case R.id.btn_ratenow:

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri
                            .parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }

                break;


            case R.id.AlertLout:
                i = new Intent(getApplicationContext(), CloseAllTools.class);
                i.putExtra("SetValue", 1);
                startActivity(i);
                break;


        }


    }

    public void StartFastChargerMethod() {
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = getPackageManager();
        //get a list of installed apps.
        packages = pm.getInstalledApplications(0);

        ActivityManager mActivityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        //  String myPackage = getApplicationContext().getPackageName();
        for (ApplicationInfo packageInfo : packages) {
            Log.e("pakages", packages + "");
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) continue;
            //if (packageInfo.packageName.equals(myPackage)) continue;
            mActivityManager.killBackgroundProcesses(packageInfo.packageName);
        }

        if (brightness > 20) {
            Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, 20);
            WindowManager.LayoutParams layoutpars = window.getAttributes();
            layoutpars.screenBrightness = 20;
            window.setAttributes(layoutpars);
            Tools_Brightness.setImageResource(R.drawable.ic_tool_brightness_off);
            brightness = 20;
        }

        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
        Tools_WiFi.setImageResource(R.drawable.ic_tool_wifi_off);

        AdapterForBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (AdapterForBluetooth == null) {
        } else {
            if (AdapterForBluetooth.isEnabled()) {

                AdapterForBluetooth.disable();
            }
        }
        Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        Tools_Rotate.setImageResource(R.drawable.ic_tool_rotate_portiat);

        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(RINGER_MODE_SILENT);
        Tools_Mode.setImageResource(R.drawable.ic_tool_profile_silent);

        setTimeout(0);
        timeout = 10000;
        Tools_Timeout.setImageResource(R.drawable.ic_tool_timeout_ten);

        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);


        StartFastCharger.startAnimation(fadeInAnimation);
        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                StartFastCharger.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


    private void setTimeout(int screenOffTimeout) {
        int time;
        switch (screenOffTimeout) {
            case 0:
                time = 10000;
                break;
            case 1:
                time = 20000;
                break;
            case 2:
                time = 30000;
                break;
            case 3:
                time = 40000;
                break;
            default:
                time = -1;
        }
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, time);
    }

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
    public void onAdsLoaded() {
        if (scrollView != null) {
            ((LinearLayout) findViewById(R.id.NativeAdContainer)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.NativeAdContainer)).removeView(scrollView);
        }
        scrollView = new NativeAdScrollView(FastCharger.this, manager, NativeAdView.Type.HEIGHT_300);
        ((LinearLayout) findViewById(R.id.NativeAdContainer)).setVisibility(View.VISIBLE);
        ((LinearLayout) findViewById(R.id.NativeAdContainer)).addView(scrollView);
    }

    @Override
    public void onAdError(AdError adError) {

    }


}
