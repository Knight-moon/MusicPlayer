package com.example.huchen.musicplayer.Service;

/**
 * Created by 54571 on 2017/6/2.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.huchen.musicplayer.Utils.MusicUtils;
import com.example.huchen.musicplayer.Bean.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicService extends Service {

    public List<Song> list;
    private int musicIndex = 0;
    private int maxIndex;
    public final IBinder binder = new MyBinder();
    public static int playMode = 2;//1.单曲循环 2.列表循环 0.随机播放
    private MusicService musicService;

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
        try {
            maxIndex=list.size();
            System.out.print(maxIndex);
            mp.setDataSource(list.get(musicIndex).getUrl());
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
        if (mp != null && musicIndex < maxIndex - 1) {
            mp.stop();
            try {

                mp.reset();

                if (playMode % 3 == 1) {//1.单曲循环
                    mp.setDataSource(list.get(musicIndex).getUrl());
                } else if (playMode % 3 == 2) {//2.列表播放
                    mp.setDataSource(list.get(musicIndex + 1).getUrl());
                    //mp.setDataSource(musicDir[musicIndex+1]);
                    musicIndex++;
                } else if (playMode % 3 == 0) {// 0.随机播放
                    mp.setDataSource(list.get(getRandom()).getUrl());
                }

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

                if (playMode % 3 == 1) {//1.单曲循环
                    mp.setDataSource(list.get(musicIndex).getUrl());
                } else if (playMode % 3 == 2) {//2.列表播放
                    mp.setDataSource(list.get(musicIndex - 1).getUrl());
                    //mp.setDataSource(musicDir[musicIndex+1]);
                    musicIndex--;
                } else if (playMode % 3 == 0) {// 0.随机播放
                    mp.setDataSource(list.get(getRandom()).getUrl());
                }

                mp.prepare();
                mp.seekTo(0);
                mp.start();
            } catch (Exception e) {
                Log.d("hint", "can't jump pre music");
                e.printStackTrace();
            }
        }
    }

    public void onDestroy() {
        if(mp != null) {
            mp.stop();
            mp.release();
            super.onDestroy();
        }
    }

    public String getName(){
        return list.get(musicIndex).getTitle();
    }
    public String getArtist(){
        return list.get(musicIndex).getArtist();
    }

    public void startIndex(int index) throws IOException {
        if(mp != null) {
            musicIndex=index;
            mp.reset();
            mp.setDataSource(list.get(musicIndex).getUrl());
            //mp.setDataSource(musicDir[musicIndex-1]);
            mp.prepare();
            mp.seekTo(0);
            mp.start();
        }
    }

    private int getRandom() {
        Random mRandom=new Random();
        musicIndex = mRandom.nextInt(maxIndex);
        return musicIndex;
    }

    public int getIndex(){
        return musicIndex;
    }

    public IBinder onBind(Intent intent) {
        return binder;
    }
}
