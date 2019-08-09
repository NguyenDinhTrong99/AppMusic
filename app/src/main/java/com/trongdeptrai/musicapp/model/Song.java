package com.trongdeptrai.musicapp.model;

public class Song {
    private String mName; // tên bài hát
    private String mDuration; // thời gian bài hát
    private String mArtistName; // tên ca sĩ/ nghệ sĩ
    private String mData; // tên ca sĩ/ nghệ sĩ

    public Song(String name, String duration, String artistName, String data) {
        mName = name;
        mDuration = duration;
        mArtistName = artistName;
        mData = data;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }

    public String getArtistName() {
        return mArtistName;
    }

    public void setArtistName(String artistName) {
        mArtistName = artistName;
    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }
}
