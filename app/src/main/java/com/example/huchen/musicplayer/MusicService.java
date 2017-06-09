package com.example.huchen.musicplayer;

/**
 * Created by 54571 on 2017/6/2.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service {

    private String[] musicDir = new String[]{Environment.getExternalStorageDirectory().getAbsolutePath()+"/Music/music1.mp3",
            Environment.getExternalStorageDirectory().getAbsolutePath()+"/Music/music2.mp3",
            Environment.getExternalStorageDirectory().getAbsolutePath()+"/Music/music3.mp3",
            Environment.getExternalStorageDirectory().getAbsolutePath() +"/Music/music4.mp3"};

    public List<Song> list;
    private int musicIndex = 0;
    private int maxIndex;
    public final IBinder binder = new MyBinder();
    public class MyBinder extends Binder{
        MusicService getService() {
            return MusicService.this;
        }
    }
    public static MediaPlayer mp = new MediaPlayer();

    public MusicService(Context context) {
        list = new ArrayList<>();
        //把扫描到的音乐赋值给list
        list = MusicUtils.getMusicData(context);
        Log.d("hint",list.get(0).path);
        try {
            maxIndex=list.size();
            mp.setDataSource(list.get(musicIndex).path);
            //Log.d("hint",list.get(1).path);
            //mp.setDataSource(musicDir[musicIndex]);
            //mp.setDataSource(Environment.getDataDirectory().getAbsolutePath()+"/You.mp3");
            mp.prepare();
        } catch (Exception e) {
            Log.d("hint","can't get song");
            e.printStackTrace();
        }
    }

    public void playOrPause() {
        if(mp.isPlaying()){
            mp.pause();
        } else {
            mp.start();
        }
    }
    public void stop() {
        if(mp != null) {
            mp.stop();
            try {
                mp.prepare();
                mp.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void nextMusic() {
        if(mp != null && musicIndex < maxIndex) {
            mp.stop();
            try {
                mp.reset();
                mp.setDataSource(list.get(musicIndex+1).path);
                //mp.setDataSource(musicDir[musicIndex+1]);
                musicIndex++;
                mp.prepare();
                mp.seekTo(0);
                mp.start();
            } catch (Exception e) {
                Log.d("hint", "can't jump next music");
                e.printStackTrace();
            }
        }
    }
    public void preMusic() {
        if(mp != null && musicIndex > 0) {
            mp.stop();
            try {
                mp.reset();
                mp.setDataSource(list.get(musicIndex-1).path);
                //mp.setDataSource(musicDir[musicIndex-1]);
                musicIndex--;
                mp.prepare();
                mp.seekTo(0);
                mp.start();
            } catch (Exception e) {
                Log.d("hint", "can't jump pre music");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        mp.stop();
        mp.release();
        super.onDestroy();
    }

    public String getName(){
        return list.get(musicIndex).song;
    }

    public void startIndex(int index) throws IOException {
        musicIndex=index;
        mp.setDataSource(list.get(musicIndex).path);
        mp.prepare();
        mp.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
