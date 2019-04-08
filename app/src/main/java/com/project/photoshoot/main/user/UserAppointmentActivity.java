package com.project.photoshoot.main.user;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.photoshoot.R;
import com.project.photoshoot.adapters.UserAppointmentAdapter;
import com.project.photoshoot.models.Appointment;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserAppointmentActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = UserAppointmentActivity.class.getSimpleName();
    private Button mBookAppointmentButton, mConfirmButton, mExitButton;
    private ImageView mBackButtonImageView;
    private EditText mBookingNameEditText;
    private AlertDialog mAlertDialog;
    private TimePickerDialog mTimePickerDialog;
    private DatePickerDialog mDatePickerDialog;
    int mFound = 0;
    private FirebaseAuth mAuth;

    private int mYear = -1, mMonthOfYear = -1, mDayOfMonth = -1;
    private int mHourOfDay = -1, mMinute = -1;
    private RecyclerView mAppointmentRecyclerView;

    private DatabaseReference mDatabaseReference;
    private List<Appointment> mAppointmentList = new ArrayList<>();
    private TextView mSelectedDate, mSelectedTime, mNoAppointment;
    private UserAppointmentAdapter mUserAppointmentAdapter;
    private Spinner spinner2;
    private String mSelectedCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_appointment);
        FirebaseApp.initializeApp(this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("admin");

        mBookAppointmentButton = findViewById(R.id.book_appointment_button);
        mAppointmentRecyclerView = findViewById(R.id.appointment_recyclerView);
        mBackButtonImageView = findViewById(R.id.back_button);

        mNoAppointment = findViewById(R.id.noappointment_textView);

        mAppointmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchAppointments();

        mBookAppointmentButton.setOnClickListener(v -> openBookingDialog());
        mBackButtonImageView.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void openBookingDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        View mView = getLayoutInflater().inflate(R.layout.dailog_book_appointment, null);

        mBookingNameEditText = mView.findViewById(R.id.booking_name_editext);
        mConfirmButton = mView.findViewById(R.id.confirm_button);
        mExitButton = mView.findViewById(R.id.exit_button);

        mSelectedDate = mView.findViewById(R.id.datepicker_TextView);
        mSelectedTime = mView.findViewById(R.id.timepicker_TextView);

        mSelectedDate.setOnClickListener(v -> selectDate());
        mSelectedTime.setOnClickListener(v -> selectTime());

        mConfirmButton.setOnClickListener(view -> {
            if (!mBookingNameEditText.getText().toString().isEmpty()) {
                bookAppointment();
            } else {
                Toast.makeText(getApplicationContext(), "Please enter booking name !", Toast.LENGTH_SHORT).show();

            }

        });
        mExitButton.setOnClickListener(view -> {
            onBackPressed();
        });


        //
        addItemsOnSpinner2(mView);
        //
        mBuilder.setView(mView);
        mAlertDialog = mBuilder.create();
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.show();
    }

    public void addItemsOnSpinner2(View mView) {

        spinner2 = mView.findViewById(R.id.category_spinner);
        List<String> list = new ArrayList<String>();
        list.add("Select Category");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("categories");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot datasnapshotobject : children) {
                    list.add(datasnapshotobject.getKey());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }


    private void fetchAppointments() {

        mUserAppointmentAdapter = new UserAppointmentAdapter(mAppointmentList);
        mAppointmentRecyclerView.setAdapter(mUserAppointmentAdapter);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                mAppointmentList.clear();

                for (DataSnapshot datasnapshotobject : children) {

                    Appointment appointment = datasnapshotobject.getValue(Appointment.class);
                    if (appointment.getEmail().equals(mAuth.getCurrentUser().getEmail()))
                        mAppointmentList.add(appointment);

                }
                mUserAppointmentAdapter.notifyDataSetChanged();

                if (mAppointmentList.isEmpty())
                    mNoAppointment.setVisibility(View.VISIBLE);
                else
                    mNoAppointment.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void bookAppointment() {
        Calendar currentCalender = Calendar.getInstance();
        Calendar userCalender = Calendar.getInstance();

        userCalender.set(mYear,
                mMonthOfYear - 1,
                mDayOfMonth,
                mHourOfDay,
                mMinute,
                00);

        if (userCalender.compareTo(currentCalender) <= 0) {
            //The set Date/Time already passed
            //admin has set wrong timer for tasks
            Toast.makeText(getApplicationContext(),
                    "Invalid Date/Time",
                    Toast.LENGTH_LONG).show();
            mHourOfDay = -1;
            mMinute = -1;
        } else {

            //push data now to firebase

            if (mSelectedDate.getText().toString().isEmpty() || mSelectedTime.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please select date and time !", Toast.LENGTH_SHORT).show();
            } else {
                pushData();
            }

        }
    }

    private void pushData() {
        mSelectedCategory = String.valueOf(spinner2.getSelectedItem());
        if (!mSelectedCategory.equals("Select Category")) {
            mFound = 0;
            Appointment appointment = new Appointment(mBookingNameEditText.getText() + "",
                    mAuth.getCurrentUser().getEmail(),
                    mSelectedDate.getText() + "",
                    mSelectedTime.getText() + "",
                    "pending",
                    mSelectedCategory);

            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                    for (DataSnapshot datasnapshotobject : children) {

                        if (datasnapshotobject.getValue(Appointment.class).getDate()
                                .equals(appointment.getDate()) &&
                                datasnapshotobject.getValue(Appointment.class).getTime()
                                        .equals(appointment.getTime())) {
                            mFound++;
                            break;
                        }
                    }

                    if (mFound == 0)
                        mDatabaseReference.push().setValue(appointment);
                    else
                        Toast.makeText(UserAppointmentActivity.this, "Appointment is booked for this date and time ! Try other :(", Toast.LENGTH_LONG).show();


                    mAlertDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else Toast.makeText(this, "Select category !", Toast.LENGTH_SHORT).show();

    }

    private void selectTime() {
        Calendar now = Calendar.getInstance();

        mTimePickerDialog = TimePickerDialog.newInstance(
                UserAppointmentActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false);

        //function end

        mTimePickerDialog.setTitle("Timer Picker dialog");
        mTimePickerDialog.show(getSupportFragmentManager(), "TimePicker");

    }

    private void selectDate() {
        Calendar now = Calendar.getInstance();
        mDatePickerDialog =
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        UserAppointmentActivity.this
                        , now.get(Calendar.YEAR)
                        , now.get(Calendar.MONTH)
                        , now.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.setTitle("Date picker mAlertDialog");

        mDatePickerDialog.show(getSupportFragmentManager(), "Datepickerdialog");
        mDatePickerDialog.setMinDate(now);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mYear = year;
        mMonthOfYear = monthOfYear + 1;
        mDayOfMonth = dayOfMonth;
        String date = mDayOfMonth + "/" + mMonthOfYear + "/" + mYear;
        mSelectedDate.setText(date);
        Toast.makeText(UserAppointmentActivity.this, "Date: " + date, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        mHourOfDay = hourOfDay;
        mMinute = minute;

        String time;

        if (mHourOfDay >= 0 && mMinute < 12) {
            time = mHourOfDay + " : " + mMinute;
        } else {
            if (hourOfDay == 12) {
                time = mHourOfDay + " : " + mMinute;
            } else {
                hourOfDay = hourOfDay - 12;
                time = mHourOfDay + " : " + mMinute;
            }
        }


        mSelectedTime.setText(time);
        Toast.makeText(getApplicationContext(), "Time: " + time, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        } else
            super.onBackPressed();

    }
}
