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


public class UserAppointmentAdapter extends RecyclerView.Adapter<UserAppointmentAdapter.AppointmentViewHolder> {
    List<Appointment> mAppointmentList;

    public UserAppointmentAdapter(List<Appointment> mAppointmentList) {
        this.mAppointmentList = mAppointmentList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.display_appointment_user, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        String name = mAppointmentList.get(position).getBookingName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        holder.mBookingNameTextView.setText(name);
        holder.mDateTextView.setText(mAppointmentList.get(position).getDate());
        holder.mTimeTextView.setText(mAppointmentList.get(position).getTime());
        String status = mAppointmentList.get(position).getStatus();
        if (status.equals("confirm"))
            holder.mStatusTextView.setTextColor(holder.itemView.getResources().getColor(R.color.green1));
        else if (status.equals("declined"))
            holder.mStatusTextView.setTextColor(holder.itemView.getResources().getColor(R.color.red_A200));
        else
            holder.mStatusTextView.setTextColor(holder.itemView.getResources().getColor(R.color.yellow_200));

        holder.mStatusTextView.setText("status: " + status);
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
