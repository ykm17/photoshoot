package com.project.photoshoot.main.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import ja.burhanrashid52.photoeditor.PhotoEditor;

import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.burhanrashid52.imageeditor.EditImageActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.project.photoshoot.ImageBookPaymentActivity;
import com.project.photoshoot.adapters.ImageBookDragAdapter;
import com.project.photoshoot.listener.RecyclerTouchListener;
import com.project.photoshoot.models.ImageBookModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.project.photoshoot.R;


import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;

import static android.view.View.GONE;

public class ImageBookActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 123;
    private static final String TAG = ImageBookActivity.class.getCanonicalName();
    private static final int EDITED_IMAGE = 1244;
    private List<ImageBookModel> mImagesForBook = new ArrayList<>();


    private View mUploadLayout;
    private View mRecyclerLayout;
    private RecyclerView mImageRecyclerView;
    RecyclerView.Adapter mAdapter;


    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_book);


        initRecyclerView();


        mUploadLayout  = findViewById(R.id.upload_layout);
        mRecyclerLayout = findViewById(R.id.images_layout);
        mRecyclerLayout.setVisibility(GONE);


        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);








    }

    private void initRecyclerView() {

        mImageRecyclerView = findViewById(R.id.imagebook_rv);

        RecyclerViewDragDropManager dragDropManager = new RecyclerViewDragDropManager();

        ImageBookDragAdapter adapter = new ImageBookDragAdapter(mImagesForBook);


        mAdapter = dragDropManager.createWrappedAdapter(adapter);

        mImageRecyclerView.setAdapter(mAdapter);
        mImageRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        // disable change animations
        ((SimpleItemAnimator) mImageRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);


        dragDropManager.attachRecyclerView(mImageRecyclerView);

        mImageRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mImageRecyclerView, new RecyclerTouchListener.ClickListener() {

            boolean  doubleClick = false;
            @Override
            public void onClick(View view, final int position) {




                if (doubleClick) {

                    doubleClick = false;

                    if(position == 0)
                        return;



                    new MaterialAlertDialogBuilder(ImageBookActivity.this)
                            .setMessage("Edit Image")
                            .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(ImageBookActivity.this,EditImageActivity.class);
                                    i.putExtra("id",position);
                                    i.putExtra("img",mImagesForBook.get(position).getPhotoURI().toString());
                                    startActivityForResult(i,EDITED_IMAGE);
                                }
                            })
                            .show();


                }else {
                    doubleClick=true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doubleClick = false;
                        }
                    }, 500);
                }



            }

            @Override
            public void onLongClick(View view, int position) {





            }
        }));



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getClipData() != null) {
                    ClipData clipData = data.getClipData();
                    //Log.d(TAG, "onActivityResult: "+clipData);
                    int count = clipData.getItemCount();
                    Log.d(TAG, "onActivityResult: " + count);
                    clearList();

                    for (int c = 0; c < count; c++) {
                        Uri imageUri = clipData.getItemAt(c).getUri();
                        mImagesForBook.add(new ImageBookModel(imageUri));
                    }


                } else if (data.getData() != null) {
                    Uri imagePath = data.getData();
                    clearList();
                    mImagesForBook.add(new ImageBookModel(imagePath));
                }

                mRecyclerLayout.setVisibility(View.VISIBLE);
                mUploadLayout.setVisibility(GONE);
            } else {
                mRecyclerLayout.setVisibility(GONE);
                mUploadLayout.setVisibility(View.VISIBLE);
            }
        }


        if(requestCode == EDITED_IMAGE) {
            if(resultCode == RESULT_OK) {

                Uri uri = data.getData();
                int id = data.getIntExtra("id",-1);
                if(id == -1) {
                    Toast.makeText(this, "-1", Toast.LENGTH_SHORT).show();
                } else {
                    mImagesForBook.get(id).setPhotoURI(uri);
                }
                mAdapter.notifyDataSetChanged();

            }
        }


       /*
       if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mUri = data.getData();

            //       Picasso.get().load(mUri).into(mPreviewImage);
        }
        */
    }

    public void openFileChooser(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST); //SELECT_PICTURES is simply a global int used to check the calling intent in onActivityResult

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.imagebook_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_ib_done: goToPayment();
            default: return  false;
        }
    }

    private void goToPayment() {

        Intent i  =  new Intent(this,ImageBookPaymentActivity.class);

        Type type = new TypeToken<List<ImageBookModel>>() {}.getType();


        Gson g = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new ImageBookModel.UriSerializer())
                .create();

        i.putExtra("data",g.toJson(mImagesForBook,type));
        startActivity(i);



    }


    public void clearList() {
        String coverUrl = getIntent().getStringExtra("cover");
        mImagesForBook.clear();
        mImagesForBook.add(new ImageBookModel(coverUrl));
    }
}