package com.kinemarts.modern.music.event;

import com.kinemarts.modern.music.data.model.PlayList;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 9/11/16
 * Time: 9:22 PM
 * Desc: PlayListNowEvent
 */
public class PlayListNowEvent {

    public PlayList playList;
    public int playIndex;

    public PlayListNowEvent(PlayList playList, int playIndex) {
        this.playList = playList;
        this.playIndex = playIndex;
    }
}
