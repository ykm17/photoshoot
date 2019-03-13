package com.project.photoshoot.main.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.project.photoshoot.R;
import com.project.photoshoot.SpacesItemDecoration;
import com.project.photoshoot.adapters.DisplayCategoryAdapter;
import com.project.photoshoot.basic.LoginActivity;
import com.project.photoshoot.models.ImageFile;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class UserHomeActivity extends AppCompatActivity {

    private static final String TAG = UserHomeActivity.class.getSimpleName();
    private Button mLogoutButton, mAppointmentButton;
    private ImageView mMenuButton;
    private RecyclerView mDisplayCategoryRecyclerView;
    private ProgressBar mProgressBar;
    private List<ImageFile> imageFileList = new ArrayList<>();
    private LinearLayout mMenuLinearLayout;

    private FirebaseAuth mAuth;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("categories");

        mAppointmentButton = findViewById(R.id.appoinment_button);
        mLogoutButton = findViewById(R.id.logout_button);

        mMenuButton = findViewById(R.id.menu_button);
        mMenuLinearLayout = findViewById(R.id.menu_linearlayout);


        mDisplayCategoryRecyclerView = findViewById(R.id.displayCategories_recyclerView);
        mDisplayCategoryRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        SpacesItemDecoration decoration = new SpacesItemDecoration(10, 186);
        mDisplayCategoryRecyclerView.addItemDecoration(decoration);

        loadCategory();

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
                startActivity(new Intent(UserHomeActivity.this, LoginActivity.class));
                finish();
            }
        });

        mAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserHomeActivity.this, AppointmentActivity.class));
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

}
