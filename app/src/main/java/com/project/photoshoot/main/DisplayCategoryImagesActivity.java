package com.project.photoshoot.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.photoshoot.R;
import com.project.photoshoot.adapters.DisplayCategoryImagesAdapter;

import java.util.ArrayList;
import java.util.List;

public class DisplayCategoryImagesActivity extends AppCompatActivity {

    public static String TAG = DisplayCategoryImagesActivity.class.getSimpleName();
    private DatabaseReference mDatabaseReference;
    private RecyclerView mDisplayCategoryRecyclerView;
    private List<String> mDownloadLinkList = new ArrayList<>();
    private TextView mCategoryTextView;
    private ImageView mBackButtonImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoinment);

        Intent intent = getIntent();
        String category_name = intent.getStringExtra("category_name");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("categories").child(category_name);

        //---------------------------------------------------------------------------------//

        mCategoryTextView = findViewById(R.id.categoryview);
        mBackButtonImageView = findViewById(R.id.back_button);

        mCategoryTextView.setText(category_name+"'s album");
        mDisplayCategoryRecyclerView = findViewById(R.id.displayCategories_recyclerView);
        mDisplayCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(mDisplayCategoryRecyclerView);

        mBackButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DisplayCategoryImagesActivity.this,AdminHomeActivity.class));
                finish();
            }
        });

        loadCategory();

    }

    private void loadCategory() {

        final DisplayCategoryImagesAdapter mDisplayCategoryImagesAdapter = new DisplayCategoryImagesAdapter(mDownloadLinkList);
        mDisplayCategoryRecyclerView.smoothScrollToPosition(0);
        mDisplayCategoryRecyclerView.setAdapter(mDisplayCategoryImagesAdapter);


        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                mDownloadLinkList.clear();

                Log.d(TAG, "onDataChange: " + dataSnapshot.getValue());
                //              imageFileList.clear();
                for (DataSnapshot datasnapshotobject : children) {
                    //   Toast.makeText(AdminHomeActivity.this, ""+datasnapshotobject.child("displayImage").getValue(), Toast.LENGTH_SHORT).show();

                    String downloadlink = String.valueOf(datasnapshotobject.getValue());
                    if (!datasnapshotobject.getKey().equals("displayImage"))
                        mDownloadLinkList.add(downloadlink);
                }
                mDisplayCategoryImagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DisplayCategoryImagesActivity.this,AdminHomeActivity.class));
        finish();
    }
}
