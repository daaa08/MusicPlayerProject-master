package com.example.da08.musicplayerproject;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.da08.musicplayerproject.domain.CurrentMusic;
import com.example.da08.musicplayerproject.domain.Data;

import java.util.List;

/**
 * Created by Da08 on 2017. 7. 10..
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.Holder>{
//    Callback callback;
    Context context;
    List<Data.Music> datas = CurrentMusic.Instance;

    MediaPlayer player = null;  // 음원 재생 사용하기위해서 선언

    public ListAdapter(Context context) {
        this.context = context;
//        callback = (Callback) context;
    }

    public void setData(List<Data.Music> datas){  // 음악 목록 데이터 세팅
        this.datas = datas;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.position = position;
        holder.txtTitle.setText(datas.get(position).title);
        holder.txtSinger.setText(datas.get(position).artist);
        Glide.with(context).load(datas.get(position).albumArt).into(holder.imgAlbum);
//        callback.itemSelected(position);

    }

    class Holder extends RecyclerView.ViewHolder {

        public View view;
        public int position;
        public TextView txtTitle, txtSinger;
        public ImageButton btnMenu;
        public ImageView imgAlbum;

        public Holder(View itemView) {
            super(itemView);
            view = itemView;
            txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
            txtSinger = (TextView)itemView.findViewById(R.id.txtSinger);
            imgAlbum = (ImageView)itemView.findViewById(R.id.imgAlbum);
            btnMenu = (ImageButton)itemView.findViewById(R.id.btnMenu);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(view.getContext(), DetailActivity.class);
                    intent.putExtra("MUSIC_PLAY", datas.get(position).musicUri.toString());   // musicUri 값 넘기기
                    CurrentMusic.currentPosition = position;
                    view.getContext().startActivity(intent);

                    return true;
                }
            });


        }

    }

     public interface Callback{
        public void itemSelected(int position);
    }
}
