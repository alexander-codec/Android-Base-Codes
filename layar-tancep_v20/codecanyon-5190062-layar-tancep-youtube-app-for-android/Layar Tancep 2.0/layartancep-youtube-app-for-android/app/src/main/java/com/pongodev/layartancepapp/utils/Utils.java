package com.pongodev.layartancepapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.github.mrengineer13.snackbar.SnackBar;
import com.google.android.gms.ads.AdView;
import com.pongodev.layartancepapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Design and developed by pongodev.com
 *
 * Utils is created to set application configuration, from database path, ad visibility.
 */
public class Utils {
    // YOUTUBE API V3 base url
    public static final String API_YOUTUBE  = "https://www.googleapis.com/youtube/v3/";

    // Youtube Functions
    public static final String FUNCTION_SEARCH_YOUTUBE  = "search?";
    public static final String FUNCTION_VIDEO_YOUTUBE   = "videos?";
    public static final String FUNCTION_PLAYLIST_ITEMS_YOUTUBE   = "playlistItems?";

    // Youtube Parameters
    public static final String PARAM_KEY_YOUTUBE   	    = "key=";
    public static final String PARAM_CHANNEL_ID_YOUTUBE = "channelId=";
    public static final String PARAM_PLAYLIST_ID_YOUTUBE = "playlistId=";
    public static final String PARAM_VIDEO_ID_YOUTUBE   = "id=";
    public static final String PARAM_PART_YOUTUBE   	= "part=";
    public static final String PARAM_PAGE_TOKEN_YOUTUBE = "pageToken=";
    public static final String PARAM_ORDER_YOUTUBE   	= "order=date";
    public static final String PARAM_MAX_RESULT_YOUTUBE = "maxResults=";
    public static final String PARAM_TYPE_YOUTUBE   	= "type=video";
    public static final String PARAM_FIELD_SEARCH_YOUTUBE = "fields=nextPageToken," +
            "pageInfo(totalResults),items(id(videoId),snippet(title,thumbnails,publishedAt))";
    public static final String PARAM_FIELD_VIDEO_YOUTUBE  = "fields=pageInfo(totalResults)," +
            "items(contentDetails(duration))&";
    public static final String PARAM_FIELD_PLAYLIST_YOUTUBE = "fields=nextPageToken," +
            "pageInfo(totalResults),items(snippet(title,thumbnails,publishedAt,resourceId(videoId)))";
    public static final int PARAM_RESULT_PER_PAGE   	  = 8;

    // Youtube Arrays
    public static final String ARRAY_PAGE_TOKEN  = "nextPageToken";
    public static final String ARRAY_ITEMS       = "items";

    // Youtube Objects
    public static final String OBJECT_ITEMS_ID                        = "id";
    public static final String OBJECT_ITEMS_CONTENT_DETAIL            = "contentDetails";
    public static final String OBJECT_ITEMS_SNIPPET                   = "snippet";
    public static final String OBJECT_ITEMS_SNIPPET_THUMBNAILS        = "thumbnails";
    public static final String OBJECT_ITEMS_SNIPPET_RESOURCEID        = "resourceId";
    public static final String OBJECT_ITEMS_SNIPPET_THUMBNAILS_MEDIUM = "medium";

    // Youtube Keys
    public static final String KEY_VIDEO_ID        = "videoId";
    public static final String KEY_TITLE           = "title";
    public static final String KEY_PUBLISHEDAT     = "publishedAt";
    public static final String KEY_URL_THUMBNAILS  = "url";
    public static final String KEY_DURATION        = "duration";

    // Request time out value
    public static final Integer ARG_TIMEOUT_MS  = 4000;

    // Debugging tag for the application
    private static final String TAG          = Utils.class.getSimpleName();
    public static final String TAG_PONGODEV  = "Pongodev:";

    public static final String ARG_ADMOB_PREFERENCE = "admobPreference";

    // Key to pass data to other activity or fragment
    public static final String TAG_CHANNEL_NAMES  = "channel_names";
    public static final String TAG_CHANNEL_IDS  = "channel_ids";
    public static final String TAG_CHANNEL_ID  = "channel_id";
    public static final String TAG_VIDEO_TYPE  = "video_type";


    // Argument for ad interstitial, do NOT change this
    public static final int ARG_NUMBER_OF_NEW_VIDEO = 1;
    // Argument for ad interstitial, do NOT change this
    public static final String ARG_TRIGGER = "trigger";
    // For every detail you want to display interstitial ad.
    // 3 means interstitial ad will display after user open detail page three times.
    public static final int ARG_TRIGGER_VALUE = 3;
    // Admob visibility parameter. Set true to show false and false to hide.
    public static final boolean IS_ADMOB_VISIBLE = false;
    // Set value to true if you are still in development process,
    // and false if you are ready to publish the app.
    public static final boolean IS_ADMOB_IN_DEBUG = true;

    // Method to check admob visibility
    public static boolean admobVisibility(AdView ad, boolean isInDebugMode){
        if(isInDebugMode) {
            ad.setVisibility(View.VISIBLE);
            return true;
        }else {
            ad.setVisibility(View.GONE);
            return false;
        }
    }

    // Method to save integer value to SharedPreferences
    public static void saveIntPreferences(Context ctx, String key, String param, int value){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(param, value);
        editor.apply();
    }

    // Method to save string value to SharedPreferences
    public static void saveStringPreferences(Context ctx, String key, String param, String value){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(param, value);
        editor.apply();
    }

    // Method to load int value from SharedPreferences
    public static int loadIntPreferences(Context ctx, String key, String param){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(key, Context.MODE_PRIVATE);
        if(key.equals(ARG_ADMOB_PREFERENCE)){
            return sharedPreferences.getInt(param, 1);
        }else {
            return sharedPreferences.getInt(param, 1000);
        }
    }

    // Method to show snackbar
    public static void showSnackBar(Activity activity, String message){
        new SnackBar.Builder(activity)
                .withMessage(message)
                .show();
    }

    // Method to convert date to string
    public static String formatPublishedDate(Activity activity, String publishedDate){
        Date result = new Date();
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            result = df1.parse(publishedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return getTimeAgo(result, activity);
    }

    // Method to get current date
    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    // Method to get how many time ago
    public static String getTimeAgo(Date date, Context ctx) {

        if(date == null) {
            return null;
        }

        long time = date.getTime();

        Date curDate = currentDate();
        long now = curDate.getTime();
        if (time > now || time <= 0) {
            return null;
        }

        int dim = getTimeDistanceInMinutes(time);

        String timeAgo = null;

        if (dim == 0) {
            timeAgo = ctx.getResources().getString(R.string.date_util_term_less) + " " +
                    ctx.getResources().getString(R.string.date_util_term_a) + " " +
                    ctx.getResources().getString(R.string.date_util_unit_minute);
        } else if (dim == 1) {
            return "1 " + ctx.getResources().getString(R.string.date_util_unit_minute);
        } else if (dim >= 2 && dim <= 44) {
            timeAgo = dim + " " + ctx.getResources().getString(R.string.date_util_unit_minutes);
        } else if (dim >= 45 && dim <= 89) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " "+
                    ctx.getResources().getString(R.string.date_util_term_an)+ " " +
                    ctx.getResources().getString(R.string.date_util_unit_hour);
        } else if (dim >= 90 && dim <= 1439) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " " +
                    (Math.round(dim / 60)) + " " +
                    ctx.getResources().getString(R.string.date_util_unit_hours);
        } else if (dim >= 1440 && dim <= 2519) {
            timeAgo = "1 " + ctx.getResources().getString(R.string.date_util_unit_day);
        } else if (dim >= 2520 && dim <= 43199) {
            timeAgo = (Math.round(dim / 1440)) + " " +
                    ctx.getResources().getString(R.string.date_util_unit_days);
        } else if (dim >= 43200 && dim <= 86399) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " "+
                    ctx.getResources().getString(R.string.date_util_term_a)+ " " +
                    ctx.getResources().getString(R.string.date_util_unit_month);
        } else if (dim >= 86400 && dim <= 525599) {
            timeAgo = (Math.round(dim / 43200)) + " " +
                    ctx.getResources().getString(R.string.date_util_unit_months);
        } else if (dim >= 525600 && dim <= 655199) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " "+
                    ctx.getResources().getString(R.string.date_util_term_a)+ " " +
                    ctx.getResources().getString(R.string.date_util_unit_year);
        } else if (dim >= 655200 && dim <= 914399) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_over) + " "+
                    ctx.getResources().getString(R.string.date_util_term_a)+ " " +
                    ctx.getResources().getString(R.string.date_util_unit_year);
        } else if (dim >= 914400 && dim <= 1051199) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_almost) + " 2 " +
                    ctx.getResources().getString(R.string.date_util_unit_years);
        } else {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " " +
                    (Math.round(dim / 525600)) + " " +
                    ctx.getResources().getString(R.string.date_util_unit_years);
        }

        return timeAgo + " " + ctx.getResources().getString(R.string.date_util_suffix);
    }

    // Method to get time distance in minute
    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }

    // Method to convert ISO 8601 time to string
    public static String getTimeFromString(String duration) {
        // TODO Auto-generated method stub
        String time = "";
        boolean hourexists = false, minutesexists = false, secondsexists = false;
        if (duration.contains("H"))
            hourexists = true;
        if (duration.contains("M"))
            minutesexists = true;
        if (duration.contains("S"))
            secondsexists = true;
        if (hourexists) {
            String hour;
            hour = duration.substring(duration.indexOf("T") + 1,
                    duration.indexOf("H"));
            if (hour.length() == 1)
                hour = "0" + hour;
            time += hour + ":";
        }
        if (minutesexists) {
            String minutes;
            if (hourexists)
                minutes = duration.substring(duration.indexOf("H") + 1,
                        duration.indexOf("M"));
            else
                minutes = duration.substring(duration.indexOf("T") + 1,
                        duration.indexOf("M"));
            if (minutes.length() == 1)
                minutes = "0" + minutes;
            time += minutes + ":";
        } else {
            time += "00:";
        }
        if (secondsexists) {
            String seconds;
            if (hourexists) {
                if (minutesexists)
                    seconds = duration.substring(duration.indexOf("M") + 1,
                            duration.indexOf("S"));
                else
                    seconds = duration.substring(duration.indexOf("H") + 1,
                            duration.indexOf("S"));
            } else if (minutesexists)
                seconds = duration.substring(duration.indexOf("M") + 1,
                        duration.indexOf("S"));
            else
                seconds = duration.substring(duration.indexOf("T") + 1,
                        duration.indexOf("S"));
            if (seconds.length() == 1)
                seconds = "0" + seconds;
            time += seconds;
        }else {
            time += "00";
        }
        return time;
    }

}
