package com.example.da08.musicplayerproject;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.da08.musicplayerproject.domain.CurrentMusic;
import com.example.da08.musicplayerproject.domain.Data;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Da08 on 2017. 7. 13..
 */

public class DetailAdapter extends PagerAdapter {

    List<Data.Music> datas = CurrentMusic.Instance;

    public DetailAdapter(List<Data.Music> datas) {
        this.datas = datas;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.detail_item, null);
        ImageView imageViewD = (ImageView) view.findViewById(R.id.imageViewD);
        Glide.with(container.getContext())
                .load(datas.get(position).albumArt)
                .placeholder(R.mipmap.ic_launcher_round)
                .bitmapTransform(new CropCircleTransformation(container.getContext()))
                .into(imageViewD);

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
