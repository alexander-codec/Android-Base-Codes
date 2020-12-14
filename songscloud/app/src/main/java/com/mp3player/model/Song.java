package com.mp3player.model;

import com.mp3player.searchonline.constants;

/**
 * Created by Usman Jamil on 11/9/2015.
 */
public class Song implements constants {
    private String title,artwork_url,stream_url;
    private  int duration,like,fav;
    public Song() {
    }

    public Song(String title, String artwork_url, String stream_url, int duration, int like, int fav){
        this.title=title;
        this.artwork_url=artwork_url;
        this.stream_url=stream_url;
        this.duration=duration;
        this.like=like;
        this.fav=fav;

    }

    public String getTitle(){
        return  title;
    }
    public void setTitle(String value){
        this.title=value;
    }

    public String getArtwork_url(){
        return artwork_url;
    }
    public  void  setArtwork_url(String value){
        this.artwork_url=value;
    }
    public String getStream_url(){
        return stream_url+"?client_id="+CLIENT_ID2;
    }

    public  void  setStream_url(String value){
        this.stream_url=value;
    }

    public int getDuration(){
        return  duration;
    }

    public  void  setDuration(int value){
        this.duration=value;
    }

    public int getLike(){
        return  like;
    }

    public  void  setLike(int value){
        this.like=value;
    }

    public int getFav(){
        return  fav;
    }

    public  void  setFav(int value){
        this.fav=value;
    }


}
