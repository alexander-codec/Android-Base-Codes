package com.mp3player.searchonline;

import java.util.Random;

/**
 * Created by Usman Jamil on 02/02/2017.
 * Usmans.net
 * Skype usman.jamil78
 * email usmanjamil547@gmail.com
 */
public interface constants {

    public static final String URL_FORMAT_SUGESSTION="http://suggestqueries.google.com/complete/search?q=%1$s&client=firefox&hl=fr";

    public static final String FORMAT_URI = "content://media/external/audio/media/%1$s";

    public static final String URL_SEARCH_API="https://api.soundcloud.com/tracks";

    public static String[] SOUNDCLOUND_CLIENT_ID2 = {"a3e059563d7fd3372b49b37f00a00bcf", "cUa40O3Jg3Emvp6Tv4U6ymYYO50NUGpJ", "fDoItMDbsbZz8dY16ZzARCZmzgHBPotA"};

    public static final String DOWNLOAD_DIRECTORY="/SongsCloudMusicDownloader";

    Random rn = new Random();
    int range2 =2;
    int randomNum2 =  rn.nextInt(range2);
    public static String CLIENT_ID2 = SOUNDCLOUND_CLIENT_ID2[randomNum2];

}
