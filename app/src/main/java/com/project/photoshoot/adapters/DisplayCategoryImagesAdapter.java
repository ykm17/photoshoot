package com.project.photoshoot.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.photoshoot.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DisplayCategoryImagesAdapter extends RecyclerView.Adapter<DisplayCategoryImagesAdapter.DisplayCategoryImagesViewHolder> {

    List<String> downloadlink;

    public DisplayCategoryImagesAdapter(List<String> downloadlink) {
        this.downloadlink = downloadlink;
    }

    @NonNull
    @Override
    public DisplayCategoryImagesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.display_category_images, viewGroup, false);
        return new DisplayCategoryImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayCategoryImagesViewHolder displayCategoryImagesViewHolder, int i) {
        Picasso.get().load(downloadlink.get(i))
                .placeholder(R.drawable.placeholder_black)
                .fit().centerCrop().into(displayCategoryImagesViewHolder.mDisplayImage);
    }

    @Override
    public int getItemCount() {
        return downloadlink.size();
    }

    class DisplayCategoryImagesViewHolder extends RecyclerView.ViewHolder {

        ImageView mDisplayImage;

        public DisplayCategoryImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            mDisplayImage = itemView.findViewById(R.id.displayimage);
        }

    }
}
