package com.project.photoshoot.main;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.photoshoot.ImageFile;
import com.project.photoshoot.R;
import com.project.photoshoot.SpacesItemDecoration;
import com.project.photoshoot.adapters.DisplayCategoryAdapter;
import com.project.photoshoot.adapters.SelectedImageAdapter;
import com.project.photoshoot.basic.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = AdminHomeActivity.class.getSimpleName();
    private ImageView mMenuButton;
    private Button mAddCategoryButton, mAlertAddCategoryButton, mBrowseButton, mLogoutButton, mHomeButton;
    private RecyclerView mDisplayCategoryRecyclerView;
    private ProgressBar mProgressBar;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference mDatabaseReference;
    private Uri mUri = null;

    private List<Uri> mUriList = new ArrayList<>();
    private List<ImageFile> imageFileList = new ArrayList<>();
    private List<String> mDownloadLink = new ArrayList<>();

    private AlertDialog mDailog;
    private RecyclerView mSelectedImageRecyclerView;
    private FirebaseAuth mAuth;
    private EditText mCategoryNameEdittext;
    private TextView mStatusTextView;
    private int filecount = 0;
    private LinearLayout mMenuLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        FirebaseApp.initializeApp(this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("categories");

        mMenuButton = findViewById(R.id.menu_button);
        mHomeButton = findViewById(R.id.home_button);

        mAddCategoryButton = findViewById(R.id.add_category_button);
        mLogoutButton = findViewById(R.id.logout_button);

        mMenuLinearLayout = findViewById(R.id.menu_linearlayout);

        mDisplayCategoryRecyclerView = findViewById(R.id.displayCategories_recyclerView);
        mDisplayCategoryRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        SpacesItemDecoration decoration = new SpacesItemDecoration(10, 186);
        mDisplayCategoryRecyclerView.addItemDecoration(decoration);

        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMenuLinearLayout.getVisibility() == View.VISIBLE) {
                    mMenuLinearLayout.setVisibility(View.GONE);
                } else {
                    mMenuLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(AdminHomeActivity.this, LoginActivity.class));
                finish();
            }
        });
        loadCategory();
        mAddCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AdminHomeActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dailog_addcategory, null);

                mCategoryNameEdittext = mView.findViewById(R.id.categoryName_editText);
                mAlertAddCategoryButton = mView.findViewById(R.id.alertAddCategory_button);

                mBrowseButton = mView.findViewById(R.id.browse_button);
                mProgressBar = mView.findViewById(R.id.upload_progress);
                mStatusTextView = mView.findViewById(R.id.status_imageView);

                mProgressBar.getProgressDrawable().setColorFilter(
                        Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);

                mSelectedImageRecyclerView = mView.findViewById(R.id.selectedImages_recyclerView);
                mSelectedImageRecyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext(), LinearLayoutManager.HORIZONTAL, true));
                SnapHelper helper = new LinearSnapHelper();
                helper.attachToRecyclerView(mSelectedImageRecyclerView);


                mBrowseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mUriList.clear();
                        openFileChooser();
                    }
                });

                mAlertAddCategoryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mCategoryNameEdittext.getText().toString().isEmpty()) {

                            Toast.makeText(getApplicationContext(), "Category name cannot be empty !", Toast.LENGTH_SHORT).show();

                        } else if (mUriList.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Please select Image !", Toast.LENGTH_SHORT).show();
                        } else {
                            //AddProduct();
                            mAlertAddCategoryButton.setEnabled(false);
                            uploadFile();
                            Toast.makeText(getApplicationContext(), "Uploading...", Toast.LENGTH_SHORT).show();
                            //mDailog.dismiss();
                        }
                    }
                });
                mBuilder.setView(mView);
                mDailog = mBuilder.create();
                mDailog.setCancelable(true);
                mDailog.show();
                mDailog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            }
        });

    }

    private void loadCategory() {

        final DisplayCategoryAdapter mDisplayCategoryAdapter = new DisplayCategoryAdapter(imageFileList);
        mDisplayCategoryRecyclerView.setAdapter(mDisplayCategoryAdapter);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                imageFileList.clear();

                Log.d(TAG, "onDataChange: " + dataSnapshot.getValue());
                //              imageFileList.clear();
                for (DataSnapshot datasnapshotobject : children) {
                    //   Toast.makeText(AdminHomeActivity.this, ""+datasnapshotobject.child("displayImage").getValue(), Toast.LENGTH_SHORT).show();

                    String categoryname = datasnapshotobject.getKey();
                    String displayImage = String.valueOf(datasnapshotobject.child("displayImage").getValue());

                    imageFileList.add(new ImageFile(categoryname, displayImage));
                }
                mDisplayCategoryAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(intent, PICK_IMAGE_REQUEST);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST); //SELECT_PICTURES is simply a global int used to check the calling intent in onActivityResult

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
                    mUriList.clear();

                    for (int c = 0; c < count; c++) {
                        //Log.d(TAG, "onActivityResult: "+clipData.getItemAt(c).getUri());
                        Uri imageUri = clipData.getItemAt(c).getUri();
                        mUriList.add(imageUri);
                        //do something
                    }
                    SelectedImageAdapter mSelectedImageAdapter = new SelectedImageAdapter(mUriList);
                    mSelectedImageRecyclerView.setAdapter(mSelectedImageAdapter);
                    mStatusTextView.setText(filecount + "/" + mUriList.size());
                } else if (data.getData() != null) {
                    Uri imagePath = data.getData();
                    mUriList.clear();
                    mUriList.add(imagePath);

                    SelectedImageAdapter mSelectedImageAdapter = new SelectedImageAdapter(mUriList);
                    mSelectedImageRecyclerView.setAdapter(mSelectedImageAdapter);
                    mStatusTextView.setText(filecount + "/" + mUriList.size());
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                }
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

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void uploadFile() {

        filecount = 0;
        final int size = mUriList.size();

        for (Uri mUriEach : mUriList) {
            String fileName = System.currentTimeMillis() + "." + getFileExtension(mUriEach);
            final String path = "uploads/" + fileName;

            final StorageReference storageRef = storage.getReference();

            final StorageReference productImage = storageRef.child(path);
            final UploadTask uploadTask = productImage.putFile(mUriEach);


            Task<Uri> urlTask = uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mProgressBar.setProgress((int) progress);
                }
            }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return productImage.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        mDownloadLink.add(String.valueOf(downloadUri));
                        //AddProduct(downloadUri.toString());
                        filecount++;
                        mStatusTextView.setText(filecount + "/" + size);
                        mProgressBar.setProgress(0);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (mDailog.isShowing() && filecount == size) {
                                    mDailog.dismiss();
                                    mUriList.clear();
                                    Toast.makeText(AdminHomeActivity.this, "Files Uploaded", Toast.LENGTH_SHORT).show();
                                    mAlertAddCategoryButton.setEnabled(true);
                                    //Toast.makeText(AdminHomeActivity.this, "" + mDownloadLink.size(), Toast.LENGTH_SHORT).show();

                                    mDatabaseReference.child(String.valueOf(mCategoryNameEdittext.getText()).trim())
                                            .child("displayImage").setValue(mDownloadLink.get(0));

                                    for (String downloadlink : mDownloadLink) {
                                        mDatabaseReference.child(String.valueOf(mCategoryNameEdittext.getText()).trim())
                                                .push().setValue(downloadlink);
                                    }
                                    mDownloadLink.clear();


                                }
                            }
                        }, 500);

                    } else {
                        // Handle failures
                    }
                }
            });


        }

    }

}
