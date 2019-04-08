package com.project.photoshoot;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.photoshoot.adapters.ThemeAdapter;
import com.project.photoshoot.listener.RecyclerTouchListener;
import com.project.photoshoot.main.user.ImageBookActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ImageBookThemeActivity extends AppCompatActivity {


    RecyclerView mRecyclerView;
    ThemeAdapter mAdapter;
    List<String> mThemeList = new ArrayList<>();

    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_book_theme);


        //TODO Add more themes


        mRecyclerView = findViewById(R.id.theme_rv);
        mAdapter = new ThemeAdapter(mThemeList);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        mFab = findViewById(R.id.next_fab);

        initThemes();


        int spanCount = 2; // 50px
        boolean includeEdge = true;
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, 40, includeEdge));

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {


                if(mAdapter.pos == position)
                    mAdapter.pos = -1;
                else mAdapter.pos = position;
                mAdapter.notifyDataSetChanged();


            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));



    }

    public void next(View view) {


        if(mAdapter.pos == -1) {
            Toast.makeText(this, "Select a Image", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i  = new Intent(this,ImageBookActivity.class);
        i.putExtra("cover",mThemeList.get(mAdapter.pos));
        startActivity(i);


    }

    public void initThemes() {

        mThemeList.add("http://www.ypcatering.com/wp-content/uploads/2015/12/Happy-New-Year.jpg");
        mThemeList.add("https://i.pinimg.com/originals/88/39/f4/8839f44842403586283b4574df38e9e0.jpg    ");
        mThemeList.add("https://i.pinimg.com/originals/5d/2e/33/5d2e33f8eb3bf01e86fd7639af38bf34.png");
        mThemeList.add("https://i.pinimg.com/originals/8c/97/f3/8c97f396734c5a3ab2a8dae5be519b1d.jpg");
        mThemeList.add("https://static.vecteezy.com/system/resources/previews/000/210/989/original/happy-fathers-day-vector.jpg");
        mThemeList.add("https://cdn2.vectorstock.com/i/1000x1000/39/71/happy-friendship-day-poster-vector-15713971.jpg");
        mThemeList.add("https://asset.holidaycardsapp.com/assets/card/valentine32-5acf38538d3395d6ea280e7b721dfa3d.png");
        mThemeList.add("https://images.unsplash.com/photo-1482329833197-916d32bdae74?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80");

        mAdapter.notifyDataSetChanged();
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
