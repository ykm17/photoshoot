package com.project.photoshoot.adapters;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemState;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import com.project.photoshoot.models.ImageBookModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.project.photoshoot.R;

public class ImageBookDragAdapter  extends RecyclerView.Adapter<ImageBookDragAdapter.MyViewHolder>
                            implements DraggableItemAdapter<ImageBookDragAdapter.MyViewHolder>{


    private static final String TAG = ImageBookDragAdapter.class.getCanonicalName();
    private int mItemMoveMode = RecyclerViewDragDropManager.ITEM_MOVE_MODE_DEFAULT;



    private List<ImageBookModel> mData;
    public ImageBookDragAdapter(List<ImageBookModel> data) {
        mData = data;
        setHasStableIds(true);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.itemview_imagebook,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        if(mData.get(position).getPhotoURI() != null)
            Picasso.get().load(mData.get(position).getPhotoURI()).fit().centerCrop().into(holder.mImageView);
        else Picasso.get().load(mData.get(position).getPhotoUrl()).fit().centerCrop().into(holder.mImageView);

    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public boolean onCheckCanStartDrag(@NonNull MyViewHolder holder, int position, int x, int y) {


        return position != 0;
    }

    @Nullable
    @Override
    public ItemDraggableRange onGetItemDraggableRange(@NonNull MyViewHolder holder, int position) {
        return null;
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {


        if(toPosition == 0)
            return;


        ImageBookModel removed = mData.remove(fromPosition);
        mData.add(toPosition, removed);
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return true;
    }

    @Override
    public void onItemDragStarted(int position) {
        notifyDataSetChanged();
    }

    @Override
    public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends AbstractDraggableItemViewHolder {

        public View mDragHandle;
        public ImageView mImageView;
        boolean clicked = false;
        public MyViewHolder(View v) {
            super(v);
            mDragHandle = v.findViewById(R.id.drag_handle);
            mImageView = v.findViewById(R.id.imagebook_iv);


        }

    }
}
