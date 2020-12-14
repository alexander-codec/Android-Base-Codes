package fast.battery.charger.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

public class BatteryDetailActivity extends AppCompatActivity {

    public static final String key1 = "e8zzc9";
    ListView detailList;
    ListAdepter listAdepter;
    Toolbar toolbar;
    LinearLayout layout;
    String[] detail_name = {"Temperatura", "Voltagem", "Level", "Technologia", "Vida Util"};
    String[] detail_value = new String[6];
    Integer[] detailImage = {R.drawable.ic_temperature, R.drawable.ic_voltage, R.drawable.ic_battery_level, R.drawable.ic_technology, R.drawable.ic_battery_health};
    public BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            detail_value[0] = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10 + Character.toString((char) 176) + " C";
            detail_value[1] = (float) intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) / (float) 1000 + Character.toString((char) 176) + " V";
            detail_value[2] = Integer.toString(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0));
            detail_value[3] = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
            if (detail_value[3].equalsIgnoreCase("")) {
                detail_value[3] = "-";
            }

            int battery_helth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);

            switch (battery_helth) {
                case 1:
                    detail_value[4] = "DESONHECIDO";
                    break;
                case 2:
                    detail_value[4] = "BOA";
                    break;
                case 3:
                    detail_value[4] = "SOBREAQUECIMENTO";
                    break;
                case 4:
                    detail_value[4] = "MORTA";
                    break;
                case 5:
                    detail_value[4] = "SOBRETENSÃO";
                    break;
                case 6:
                    detail_value[4] = "FALHOU";
                    break;
                case 7:
                    detail_value[4] = "FRIO";
                    break;

            }

            listAdepter = new ListAdepter(BatteryDetailActivity.this, detail_name, detail_value, detailImage);
            detailList.setAdapter(listAdepter);

        }
    };
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_battery_detail);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Informações da Bateria");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        detailList = (ListView) findViewById(R.id.detailList);
        registerReceiver(this.batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        layout = (LinearLayout) findViewById(R.id.BannerAd);
        AdSettings.addTestDevice("61dc0d19895f5c64f7013fa7ee898521");
        adView = new AdView(this, getResources().getString(R.string.Ad_Battery_Detail_Activity_banner), AdSize.BANNER_HEIGHT_50);
        layout.addView(adView);
        adView.loadAd();



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
}

