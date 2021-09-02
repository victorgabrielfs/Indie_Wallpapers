package com.hauntersoft.indiewallpapers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.hauntersoft.indiewallpapers.Utils.AdMobService;


import java.util.ArrayList;


public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.WallpaperViewHolder> {


    private ArrayList<String> list;
    private Context context;
    private Activity activity;
    private AdMobService adMob;

    public WallpaperAdapter(ArrayList<String> list, Context context,
                            Activity activity, AdMobService admob) {
        this.list = list;
        this.context = context;
        this.activity = activity;
        this.adMob = admob;

    }

    @NonNull
    @Override
    public WallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_image_layout, parent,
                false);

        return new WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperAdapter.WallpaperViewHolder holder, int position) {
        Glide.with(context).load(list.get(position)).into(holder.imageView);

        adMob.createPersonalizedAd();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adMob.showAd(4);
                Intent intent = new Intent(context, FullImageActivity.class);
                intent.putExtra("image", list.get(position));
                intent.putExtra("image", list.get(position));
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class WallpaperViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public WallpaperViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
        }
    }
}