package com.mp3player.searchonline;
/**
 * Created by Usman Jamil on 02/02/2017.
 * Usmans.net
 * Skype usman.jamil78
 * email usmanjamil547@gmail.com
 */

import com.mp3player.searchonline.R;



import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
    }
}