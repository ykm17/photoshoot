package com.project.photoshoot.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.photoshoot.R;
import com.project.photoshoot.models.Appointment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
    List<Appointment> mAppointmentList;

    public AppointmentAdapter(List<Appointment> mAppointmentList) {
        this.mAppointmentList = mAppointmentList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.display_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        String name = mAppointmentList.get(position).getBookingName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        holder.mBookingNameTextView.setText(name);
        holder.mDateTextView.setText(mAppointmentList.get(position).getDate());
        holder.mTimeTextView.setText(mAppointmentList.get(position).getTime());
        holder.mStatusTextView.setText("status: " + mAppointmentList.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return mAppointmentList.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {

        TextView mBookingNameTextView, mDateTextView, mTimeTextView, mStatusTextView;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            mBookingNameTextView = itemView.findViewById(R.id.apt_booking_name_edittext);
            mDateTextView = itemView.findViewById(R.id.date_edittext);
            mTimeTextView = itemView.findViewById(R.id.time_edittext);
            mStatusTextView = itemView.findViewById(R.id.status_edittext);

        }
    }
}
