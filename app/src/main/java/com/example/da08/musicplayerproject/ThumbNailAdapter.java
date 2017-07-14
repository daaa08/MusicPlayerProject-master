package com.example.da08.musicplayerproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.da08.musicplayerproject.domain.Const;
import com.example.da08.musicplayerproject.domain.SearchData;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;


/**
 * Created by iyeongjun on 2017. 7. 11..
 */

public class ThumbNailAdapter extends RecyclerView.Adapter<ThumbNailAdapter.Holder> {
    List<SearchData> datas = new ArrayList<>();
    Retrofit retrofit;
    StartYoutube startYoutube;
    public ThumbNailAdapter(List<SearchData> datas, Context context) {
        this.datas = datas;
        startYoutube = (StartYoutube) context;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_youtube, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        setYoutubeThumbnail(holder, position);
        holder.txtTop.setText(datas.get(position).getTitle());
        holder.txtBot.setText(datas.get(position).getDescription());
        holder.setPostion(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
    public void setYoutubeThumbnail(Holder holder, final int position) {
        holder.thumbnailView.initialize(Const.Auth.API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(datas.get(position).getId());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }
                });

            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

    }


    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.youTubeThumbnailView)
        YouTubeThumbnailView thumbnailView;
        @BindView(R.id.txtTop)
        TextView txtTop;
        @BindView(R.id.txtBot)
        TextView txtBot;
        int position;
        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        private void setPostion(int position){
            this.position=position;
        }
        @OnClick(R.id.ItemContainer)
        public void onClickItemContanier(View view){
            startYoutube.playYoutube(datas.get(position).getId());
        }
    }
    interface StartYoutube{
        public void playYoutube(String videoId);
    }
}
