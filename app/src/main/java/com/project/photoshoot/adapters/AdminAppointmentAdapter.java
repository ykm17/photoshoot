package com.project.photoshoot.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.photoshoot.R;
import com.project.photoshoot.models.Appointment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class AdminAppointmentAdapter extends RecyclerView.Adapter<AdminAppointmentAdapter.AppointmentViewHolder> {
    List<Appointment> mAppointmentList;


    public AdminAppointmentAdapter(List<Appointment> mAppointmentList) {
        this.mAppointmentList = mAppointmentList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.display_appointment_admin, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        String name = mAppointmentList.get(position).getBookingName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        holder.mBookingNameTextView.setText(name);
        holder.mEmailTextView.setText(mAppointmentList.get(position).getEmail());
        holder.mDateTextView.setText(mAppointmentList.get(position).getDate());
        holder.mTimeTextView.setText(mAppointmentList.get(position).getTime());
        holder.mCategoryTextView.setText("Category: " + mAppointmentList.get(position).getCategory());

        if (mAppointmentList.get(position).getStatus().equals("pending"))
            holder.mButtonLinearLayout.setVisibility(View.VISIBLE);

        holder.mAcceptButton.setOnClickListener(v -> {
            changeStatus("confirm", position);
        });


        holder.mRejectButton.setOnClickListener(v -> {
            changeStatus("declined", position);
        });

    }

    private void changeStatus(String status, int position) {
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("admin");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot datasnapshotobject : children) {

                    if (datasnapshotobject.getValue(Appointment.class).getAllValues()
                            .equals(mAppointmentList.get(position).getAllValues())) {
                        mDatabaseReference.child(datasnapshotobject.getKey()).child("status").setValue(status);
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mAppointmentList.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {

        TextView mBookingNameTextView, mEmailTextView, mDateTextView, mTimeTextView, mCategoryTextView;
        ImageView mAcceptButton, mRejectButton;
        LinearLayout mButtonLinearLayout;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            mBookingNameTextView = itemView.findViewById(R.id.name_edittext);
            mEmailTextView = itemView.findViewById(R.id.email_edittext);

            mDateTextView = itemView.findViewById(R.id.date_edittext);
            mTimeTextView = itemView.findViewById(R.id.time_edittext);
            mCategoryTextView = itemView.findViewById(R.id.category_edittext);

            mAcceptButton = itemView.findViewById(R.id.accept_button);
            mRejectButton = itemView.findViewById(R.id.reject_button);
            mButtonLinearLayout = itemView.findViewById(R.id.button_linearLayout);
        }
    }
}
