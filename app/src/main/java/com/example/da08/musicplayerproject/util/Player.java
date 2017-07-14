package com.example.da08.musicplayerproject.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.da08.musicplayerproject.domain.CurrentMusic;
import com.example.da08.musicplayerproject.domain.Data;

import java.util.List;

/**
 * Created by Da08 on 2017. 7. 14..
 */

public class Player {

    static int position = CurrentMusic.currentPosition;
    static Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    static List<Data.Music> datas = CurrentMusic.Instance;
    static MediaPlayer player = null;
    static Context context;

    public static void getInstance(Context context1) {
        context = context1;
    }

    public static void Next() {
        player.release();
        position++;
        musicUri = datas.get(position).musicUri;
        if (player != null) {
            player.release();
        }
        player = MediaPlayer.create(context, musicUri);
        player.setLooping(false);
        player.start();
    }

    public static void Pre() {
        Log.i("Detail", "position===========================" + position);
        player.release();
        position--;
        musicUri = datas.get(position).musicUri;

        if (player != null) {
            player.release();
        }
        player = MediaPlayer.create(context, musicUri);
        player.setLooping(false);
        player.start();
    }

}
