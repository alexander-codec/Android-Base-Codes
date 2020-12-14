package com.pongodev.layartancepapp.fragments;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.pongodev.layartancepapp.activities.ActivityHome;

/**
 * Created by Pongodev on 11/4/2015.
 */
public final class FragmentVideo extends YouTubePlayerFragment
        implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayer player;
    private String videoId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize("AIzaSyB__E_VxHmsJ5qw1kvbjLpiEwXvspCJCFI", this);
    }

    @Override
    public void onDestroy() {
        if (player != null) {
            player.release();
        }
        super.onDestroy();
    }

    public void setVideoId(String videoId) {
        if (videoId != null && !videoId.equals(this.videoId)) {
            this.videoId = videoId;
            if (player != null) {
                player.cueVideo(videoId);
            }
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean restored) {
        this.player = player;
        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        player.setOnFullscreenListener((ActivityHome) getActivity());
        if (!restored && videoId != null) {
            player.cueVideo(videoId);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult result) {
        this.player = null;
    }

    public void backnormal(){
        player.setFullscreen(false);
    }

}
