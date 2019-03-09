package com.project.photoshoot.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.photoshoot.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class SelectedImageAdapter extends RecyclerView.Adapter<SelectedImageAdapter.SelectedImageViewHolder> {

    private List<Uri> mUriList = new ArrayList<>() ;

    public SelectedImageAdapter(List<Uri> mUriList) {
        this.mUriList = mUriList;
    }

    @NonNull
    @Override
    public SelectedImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.selected_images, viewGroup, false);
        return new SelectedImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedImageViewHolder selectedImageViewHolder, int i) {
        //Log.d(TAG, "onBindViewHolder: $$"+i);
        Picasso.get().load(mUriList.get(i))
                .placeholder(R.drawable.placeholder_white)
                .fit().centerCrop().into(selectedImageViewHolder.mSelectedImageView);
    }

    @Override
    public int getItemCount() {
        return mUriList.size();
    }

    class SelectedImageViewHolder extends RecyclerView.ViewHolder {

        ImageView mSelectedImageView;

        public SelectedImageViewHolder(@NonNull View itemView) {
            super(itemView);

            mSelectedImageView = itemView.findViewById(R.id.selectedImage);

        }
    }
}
