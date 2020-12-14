package fast.battery.charger.app.prefrence;

import android.content.Context;
import android.graphics.Color;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;


public class mPreference extends Preference {

    public mPreference(Context context) {
        super(context);
    }

    public mPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public mPreference(Context context, AttributeSet attrs,
                       int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        TextView titleView = (TextView) view.findViewById(android.R.id.title);
        TextView summaryView = (TextView) view.findViewById(android.R.id.summary);
        titleView.setTextColor(Color.WHITE);
        summaryView.setTextColor(Color.WHITE);
    }
}