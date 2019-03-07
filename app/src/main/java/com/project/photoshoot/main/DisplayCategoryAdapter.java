package com.project.photoshoot.main;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.photoshoot.ImageFile;
import com.project.photoshoot.R;
import com.squareup.picasso.Picasso;

import java.util.List;

class DisplayCategoryAdapter extends RecyclerView.Adapter<DisplayCategoryAdapter.DisplayCategoryViewHolder> {

    List<ImageFile> imageFileList;
    public DisplayCategoryAdapter(List<ImageFile> imageFileList) {
            this.imageFileList = imageFileList;
    }

    @NonNull
    @Override
    public DisplayCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.display_category, viewGroup, false);
        return new DisplayCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayCategoryViewHolder displayCategoryViewHolder, int i) {
        displayCategoryViewHolder.mCategoryName.setText(imageFileList.get(i).getCategoryname());
        Picasso.get().load(imageFileList.get(i).getDisplayImage())
                .fit().centerCrop().into(displayCategoryViewHolder.mDisplayImage);
    }

    @Override
    public int getItemCount() {
        return imageFileList.size();
    }

    class DisplayCategoryViewHolder extends RecyclerView.ViewHolder{

        TextView mCategoryName;
        ImageView mDisplayImage;

        public DisplayCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mCategoryName = itemView.findViewById(R.id.categoryname);
            mDisplayImage = itemView.findViewById(R.id.displayimage);
        }
    }
}
