package com.project.photoshoot.main.admin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.project.photoshoot.R;
import com.project.photoshoot.main.admin.AppointmentViewActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class PendingRequest extends Fragment {
    private static final String TAG = AppointmentViewActivity.class.getSimpleName();
    private View mView;
    private RecyclerView mPendingRequestRecyclerView;
    private DatabaseReference mDatabaseReference;

    public PendingRequest() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_pending_request, container, false);
        //mPendingRequestRecyclerView = mView.findViewById(R.id.pending_recyclerView);
        //mPendingRequestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //mPendingRequestRecyclerView.setAdapter();


        return mView;
    }
}
