package com.project.photoshoot.adapters;

import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.photoshoot.R;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {

    public int pos = -1;
    List<String> mData;
    public ThemeAdapter(List<String> uriList) {
        mData = uriList;
    }


    @NonNull
    @Override
    public ThemeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_theme,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ThemeAdapter.ViewHolder holder, int position) {
        Picasso.get().load(mData.get(position)).fit().centerCrop().into(holder.imageView);


        if(pos == position) {
            holder.container.setBackgroundColor(Color.BLUE);
        } else holder.container.setBackgroundColor(Color.rgb(255,255,255));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        View container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.theme_item_iv);
            container = itemView.findViewById(R.id.theme_container);
        }
    }
}
