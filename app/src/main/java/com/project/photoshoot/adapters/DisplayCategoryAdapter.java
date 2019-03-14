package com.project.photoshoot.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.photoshoot.R;
import com.project.photoshoot.main.DisplayCategoryImagesActivity;
import com.project.photoshoot.models.ImageFile;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DisplayCategoryAdapter extends RecyclerView.Adapter<DisplayCategoryAdapter.DisplayCategoryViewHolder> {

    List<ImageFile> imageFileList;
    int userCode;

    public DisplayCategoryAdapter(List<ImageFile> imageFileList, int userCode) {
        this.imageFileList = imageFileList;
        this.userCode = userCode;
    }

    @NonNull
    @Override
    public DisplayCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view;
        if (userCode == 0)
            view = inflater.inflate(R.layout.display_category_admin, viewGroup, false);
        else
            view = inflater.inflate(R.layout.display_category_user, viewGroup, false);

        return new DisplayCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayCategoryViewHolder displayCategoryViewHolder, int i) {
        displayCategoryViewHolder.mCategoryName.setText(imageFileList.get(i).getCategoryname());
        Picasso.get().load(imageFileList.get(i).getDisplayImage())
                .placeholder(R.drawable.placeholder_white)
                .fit().centerCrop().into(displayCategoryViewHolder.mDisplayImage);
    }

    @Override
    public int getItemCount() {
        return imageFileList.size();
    }

    class DisplayCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mCategoryName;
        ImageView mDisplayImage;

        public DisplayCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mCategoryName = itemView.findViewById(R.id.categoryname);
            mDisplayImage = itemView.findViewById(R.id.displayimage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(itemView.getContext(), DisplayCategoryImagesActivity.class);
            intent.putExtra("category_name", String.valueOf(imageFileList.get(getAdapterPosition()).getCategoryname()));
            itemView.getContext().startActivity(intent);

        }
    }
}
