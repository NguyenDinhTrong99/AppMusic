package com.trongdeptrai.musicapp.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import com.trongdeptrai.musicapp.MainActivity;
import com.trongdeptrai.musicapp.ManagerSongUI;
import com.trongdeptrai.musicapp.utils.ManagerPlay;

import static com.trongdeptrai.musicapp.utils.VariablePublic.sMediaPlayer;
import static com.trongdeptrai.musicapp.utils.VariablePublic.sPosition;

public class SongService extends Service {
    private ManagerPlay mManagerPlay;
    @Override
    public void onCreate() {
        mManagerPlay = new ManagerPlay(SongService.this);
        super.onCreate();
    }


    @Override
    public IBinder onBind(Intent intent) {
        mManagerPlay.setSong(sPosition);
        return null;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        mManagerPlay.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mManagerPlay.stop();
    }

    class MyBinder extends Binder{
        public SongService getService(){
            return SongService.this;
        }
    }
}
