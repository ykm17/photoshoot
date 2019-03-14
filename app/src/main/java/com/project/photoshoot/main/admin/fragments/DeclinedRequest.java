package com.project.photoshoot.main.admin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.photoshoot.R;
import com.project.photoshoot.adapters.AdminAppointmentAdapter;
import com.project.photoshoot.main.admin.AdminAppointmentActivity;
import com.project.photoshoot.models.Appointment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DeclinedRequest extends Fragment {

    private static final String TAG = AdminAppointmentActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private View mView;
    private RecyclerView mPendingRequestRecyclerView;
    private DatabaseReference mDatabaseReference;
    private List<Appointment> mAppointmentList = new ArrayList<>();
    private AdminAppointmentAdapter mAdminAppointmentAdapter;
    private TextView mNoAppointmentTextView;

    public DeclinedRequest() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_declined_request, container, false);
        FirebaseApp.initializeApp(getActivity());
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("admin");


        mView = inflater.inflate(R.layout.fragment_declined_request, container, false);

        mNoAppointmentTextView = mView.findViewById(R.id.noappointment_textView);

        mPendingRequestRecyclerView = mView.findViewById(R.id.declined_recyclerView);
        mPendingRequestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fetchAppointments();

        return mView;
    }

    private void fetchAppointments() {

        mAdminAppointmentAdapter = new AdminAppointmentAdapter(mAppointmentList);
        mPendingRequestRecyclerView.setAdapter(mAdminAppointmentAdapter);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                mAppointmentList.clear();

                for (DataSnapshot datasnapshotobject : children) {

                    Appointment appointment = datasnapshotobject.getValue(Appointment.class);
                    if (appointment.getStatus().equals("declined"))
                        mAppointmentList.add(appointment);

                }
                mAdminAppointmentAdapter.notifyDataSetChanged();


                if (mAppointmentList.isEmpty())
                    mNoAppointmentTextView.setVisibility(View.VISIBLE);
                else
                    mNoAppointmentTextView.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
