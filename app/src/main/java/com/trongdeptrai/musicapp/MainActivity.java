package com.trongdeptrai.musicapp;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.trongdeptrai.musicapp.adpater.SongAdapter;
import com.trongdeptrai.musicapp.model.Song;
import com.trongdeptrai.musicapp.service.SongService;

import static com.trongdeptrai.musicapp.utils.VariablePublic.isPlay;
import static com.trongdeptrai.musicapp.utils.VariablePublic.sListSong;
import static com.trongdeptrai.musicapp.utils.VariablePublic.sMediaPlayer;
import static com.trongdeptrai.musicapp.utils.VariablePublic.sPosition;

public class MainActivity extends AppCompatActivity {
    private static final int READ_EXTERNALSTORE_PERMISSON = 1;
    public static final String TAG = MainActivity.class.getSimpleName();
    public static ServiceConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isReadExternalStoreGranted();
        initView();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNALSTORE_PERMISSON:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                        getSong();
                    } else {
                        Toast.makeText(this, "No Permission Granted!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void isReadExternalStoreGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                        READ_EXTERNALSTORE_PERMISSON);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                        READ_EXTERNALSTORE_PERMISSON);
            }
        } else {
            getSong();
        }
    }


    private void getSong() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int songData = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            while (!songCursor.isAfterLast()) {
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentDuration = songCursor.getString(songDuration);
                String currentData = songCursor.getString(songData);
                sListSong.add(new Song(currentTitle, currentDuration, currentArtist, currentData));
                songCursor.moveToNext();
            }
        }
    }

    private void initView() {
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        RecyclerView songRecycler = findViewById(R.id.recycle_list_song);
        SongAdapter songAdapter = new SongAdapter(sListSong);
        songRecycler.setHasFixedSize(true);
        songRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        songRecycler.setAdapter(songAdapter);
        //add divider in item
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        songRecycler.addItemDecoration(itemDecoration);
        //bắt sự kiện onlclick item recyclerview
        songAdapter.setOnItemClickListener(new SongAdapter.ClickListener() {
            @Override
            public void onItemClickListener(int position, View v) {
                if (sMediaPlayer != null) sMediaPlayer.stop();
                sPosition = position;
                isPlay = true;
                bindService(new Intent(MainActivity.this, SongService.class), connection,
                        BIND_AUTO_CREATE);
                ManagerSongUI managerSongUI = new ManagerSongUI(MainActivity.this);
                managerSongUI.showUiPlaySong();
            }
        });
    }
}
