package com.example.da08.musicplayerproject.domain;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Da08 on 2017. 7. 10..
 */

public class Data {

    public static List<Music> read(Context context){   // 음원 읽어오기

        setAlbumArt(context); // 앨범 이미지가 있는 테이블에서 전체 이미지를 조회해 저장해 둠

        // 음악파일 읽어오기  > media로 분류 > contentResolver로 읽어올 데이터 주소 설정 : Uri
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        // 읽어 올 데이터의 구체적인 속성 정의
        String pro[] = {
                MediaStore.Audio.Media._ID
                ,MediaStore.Audio.Media.TITLE
                ,MediaStore.Audio.Media.ALBUM_ID
                ,MediaStore.Audio.Media.ARTIST
        };

        // 정의된 주소와 설정값으로 목록을 가져오는 쿼리
        ContentResolver resolver = context.getContentResolver();
        Cursor cu = resolver.query(musicUri,pro,null,null,null);  // 데이터 가져 옴

        // 반복문을 돌면서 커서에있는 데이터를 다른 저장소에 저장
        ArrayList<Music> data = new ArrayList<>();
        if(cu != null){
            while(cu.moveToNext()){
                Music music = new Music();
                music.id = getValue(cu,pro[0]);
                music.title = getValue(cu,pro[1]);
                music.album = getValue(cu,pro[2]);
                music.artist = getValue(cu,pro[3]);

                // 음원 uri
                music.musicUri = makeMusicUri(music.id);
                // 앨범art uri
                music.albumArt = albumMap.get(Integer.parseInt(music.album));
                data.add(music);
            }
        }
        cu.close();

        return data;
    }

    private static String getValue(Cursor cu, String name){
        int index = cu.getColumnIndex(name);
        return cu.getString(index);
    }

    public static class Music {
        public String id;
        public String title;
        public String album;
        public String artist;

        public Uri musicUri;
        public String albumArt;
    }

    private static Uri makeMusicUri(String musicId){
        Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        return Uri.withAppendedPath(contentUri,musicId);
    }

    // 앨범아트 데이터만 따로 저장
    private static HashMap<Integer, String> albumMap = new HashMap<>();

    private static void setAlbumArt(Context context) {

        String[] Album_cursorColumns = new String[]{
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums._ID
        };

        Cursor Album_cursor = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                Album_cursorColumns, null, null, null);
        if (Album_cursor != null) {
            if (Album_cursor.moveToFirst()) {
                int albumArt = Album_cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
                int albumId = Album_cursor.getColumnIndex(MediaStore.Audio.Albums._ID);
                do {
                    if (!albumMap.containsKey(Integer.parseInt(Album_cursor.getString(albumId)))) {
                        albumMap.put(Integer.parseInt(Album_cursor.getString(albumId)), Album_cursor.getString(albumArt));
                    }
                } while (Album_cursor.moveToNext());
            }
        }
        Album_cursor.close();
    }

}



