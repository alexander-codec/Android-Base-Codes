package fast.battery.charger.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BatteryReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent();
        i.setClassName(context.getPackageName(), context.getPackageName() + ".FastCharger");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
