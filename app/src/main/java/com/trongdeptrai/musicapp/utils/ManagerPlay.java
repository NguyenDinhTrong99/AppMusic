package com.trongdeptrai.musicapp.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import static com.trongdeptrai.musicapp.utils.VariablePublic.isPlay;
import static com.trongdeptrai.musicapp.utils.VariablePublic.sListSong;
import static com.trongdeptrai.musicapp.utils.VariablePublic.sMediaPlayer;
import static com.trongdeptrai.musicapp.utils.VariablePublic.sPosition;

public class ManagerPlay {
    private Context mContext;

    public ManagerPlay(Context context) {
        mContext= context;
       setSong(sPosition);
    }

    public void setSong(int position){
        String filePath = "file:///" + sListSong.get(position).getData();
        sMediaPlayer = MediaPlayer.create(mContext, Uri.parse(filePath));
    }

    public void play() {
        if(sMediaPlayer != null) {
            sMediaPlayer.start();
        }
    }

    public void stop(){
        if(sMediaPlayer != null){
            sMediaPlayer.stop();
            isPlay = false;
        }
    }

    public void fastForWard(int position){
        sMediaPlayer.seekTo(position);
    }

    public int getCurrentTimeDuration(){
        return sMediaPlayer.getCurrentPosition();
    }
    public void next(){
        if(sListSong.size() - 1 == sPosition){
            sPosition = 0;
        }else {
            sPosition += 1;
        }
    }
    public void previous(){
        if(sPosition == 0){
            sPosition = sListSong.size() - 1;
        }else {
            sPosition -= 1;
        }
    }
    public static void isPlay(){
        if(sMediaPlayer.isPlaying()){
            sMediaPlayer.stop();
        }
    }
}
