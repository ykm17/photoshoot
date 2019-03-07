package com.project.photoshoot;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final int mSpace,mStaggerSpace;

    public SpacesItemDecoration(int space,int mStaggerSpace) {
        this.mSpace = space;this.mStaggerSpace = mStaggerSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace+mSpace;


        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) == 1)
            outRect.top = mStaggerSpace;
    }
}