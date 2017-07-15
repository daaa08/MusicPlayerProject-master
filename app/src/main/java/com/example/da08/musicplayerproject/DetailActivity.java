package com.example.da08.musicplayerproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.da08.musicplayerproject.domain.Const;
import com.example.da08.musicplayerproject.domain.CurrentMusic;
import com.example.da08.musicplayerproject.domain.Data;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.util.List;

import static com.example.da08.musicplayerproject.domain.CurrentMusic.currentPosition;


public class DetailActivity extends AppCompatActivity implements View.OnClickListener, ThumbNailAdapter.StartYoutube {

    Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    TextView txtTitleP;
    TextView txtSingerP;
    ViewPager viewPager;
    SeekBar seekBar;
    ImageButton btnShareP, btnUploadP, btnPlayP, btnNextP, btnPreP, btnLikeP, btnMenu, btnClose, btnPauseP, btnReStartP;
    Uri MUSIC_PLAY = null;
    List<Data.Music> datas = CurrentMusic.Instance;
    int position = currentPosition;

    DetailAdapter adapter = new DetailAdapter(datas);

    private MediaPlayer player = null;

    Boolean isPlaying = false;


    class SeekBarThread extends Thread {

        @Override
        public void run() {
            // 씨크바 막대기 조금씩 움직이기 (노래 끝날 때까지 반복)


            while (isPlaying) {
                    seekBar.setProgress(player.getCurrentPosition());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // intent값 가져오기
        Intent intent = getIntent();
        if (intent != null) {
            String temp = "";
            temp = intent.getStringExtra("MUSIC_PLAY");
            MUSIC_PLAY = Uri.parse(temp);

        }


        viewPager = (ViewPager) findViewById(R.id.viewPager);
        txtTitleP = (TextView) findViewById(R.id.txtTitleP);
        txtSingerP = (TextView) findViewById(R.id.txtSingerP);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        btnShareP = (ImageButton) findViewById(R.id.btnShareP);
        btnUploadP = (ImageButton) findViewById(R.id.btnYoutube);
        btnPlayP = (ImageButton) findViewById(R.id.btnPlayP);
        btnNextP = (ImageButton) findViewById(R.id.btnNextP);
        btnPreP = (ImageButton) findViewById(R.id.btnPreP);
        btnLikeP = (ImageButton) findViewById(R.id.btnLikeP);
        btnClose = (ImageButton) findViewById(R.id.btnClose);
        btnPauseP = (ImageButton) findViewById(R.id.btnPauseP);
        btnReStartP = (ImageButton) findViewById(R.id.btnReStartP);

        txtTitleP.setText(datas.get(position).title);
        txtSingerP.setText(datas.get(position).artist);


        btnPlayP.setOnClickListener(this);
        btnPreP.setOnClickListener(this);
        btnNextP.setOnClickListener(this);
        btnShareP.setOnClickListener(this);
        btnUploadP.setOnClickListener(this);
        btnLikeP.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnPauseP.setOnClickListener(this);
        btnReStartP.setOnClickListener(this);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isPlaying = true;
                int userControl = seekBar.getProgress(); // 사용자가 움직여놓은 위치
                player.seekTo(userControl);
                player.start();
                new SeekBarThread().start();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isPlaying = false;
                player.pause();
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getMax() == progress) {
                    isPlaying = false;
                    player.stop();
                }
            }

        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPreP:
                Pre();
                break;

            case R.id.btnPlayP:
                play(position);
                btnPlayP.setVisibility(View.INVISIBLE);
                btnPauseP.setVisibility(View.VISIBLE);
                break;

            case R.id.btnNextP:
                Next();
                break;

            case R.id.btnShareP:
                sharing();
                break;

            case R.id.btnYoutube:
                youtubeSearch(datas.get(position).title,
                        datas.get(position).artist);
                break;
            case R.id.btnLikeP:

                break;
            case R.id.btnClose:
                this.finish();
                break;

            case R.id.btnPauseP:
                player.pause();
                isPlaying = false;
                btnPauseP.setVisibility(View.INVISIBLE);
                btnReStartP.setVisibility(View.VISIBLE);
                break;

            case R.id.btnReStartP:
                player.start(); // 시작
                isPlaying = true; // 재생하도록 flag 변경
//                new SeekBarThread().start(); // 쓰레드 시작
                btnPauseP.setVisibility(View.VISIBLE);
                btnReStartP.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void sharing() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "음악공유");
        Intent chooser = Intent.createChooser(intent, "공유");
        startActivity(chooser);
    }

    private void play(int position) {
        musicUri = datas.get(position).musicUri;
        if (player != null) {
            player.release();
        }
        player = MediaPlayer.create(this, musicUri);
        player.setLooping(false);
        player.start();

        int a = player.getDuration(); // 노래의 재생시간(miliSecond)
        seekBar.setMax(a);// 씨크바의 최대 범위를 노래의 재생시간으로 설정
        new SeekBarThread().start(); // 씨크바 그려줄 쓰레드 시작
        isPlaying = true; // 씨크바 쓰레드 반복 하도록
    }

    public void Next() {
//        player.release();
        position++;
        musicUri = datas.get(position).musicUri;
        if (player != null) {
            player.release();
        }
        player = MediaPlayer.create(this, musicUri);
        player.setLooping(false);
        player.start();


        txtTitleP.setText(datas.get(position).title);
        txtSingerP.setText(datas.get(position).artist);
        viewPager.setCurrentItem(position);

        int a = player.getDuration(); // 노래의 재생시간(miliSecond)
        new SeekBarThread().start(); // 씨크바 그려줄 쓰레드 시작
        seekBar.setMax(a);// 씨크바의 최대 범위를 노래의 재생시간으로 설정

        isPlaying = true; // 씨크바 쓰레드 반복 하도록


    }

    public void Pre() {
        Log.i("Detail", "position===========================" + position);
//        player.release();
        position--;
        musicUri = datas.get(position).musicUri;

        if (player != null) {
            player.release();
        }
        player = MediaPlayer.create(this, musicUri);
        player.setLooping(false);
        player.start();

        txtTitleP.setText(datas.get(position).title);
        txtSingerP.setText(datas.get(position).artist);
        viewPager.setCurrentItem(position);

        int a = player.getDuration(); // 노래의 재생시간(miliSecond)
        new SeekBarThread().start(); // 씨크바 그려줄 쓰레드 시작
        seekBar.setMax(a);// 씨크바의 최대 범위를 노래의 재생시간으로 설정

        isPlaying = true; // 씨크바 쓰레드 반복 하도록
    }

    //유튜브에 관한 메소드
    private void youtubeSearch(String name, String artist) {
        YoutubeDialog dialog = new YoutubeDialog(this, name, artist);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        dialog.show();
    }

    @Override
    public void playYoutube(String videoId) {
        Intent intent = YouTubeStandalonePlayer.createVideoIntent(this, Const.Auth.API_KEY, videoId);
        startActivity(intent);
    }
}



