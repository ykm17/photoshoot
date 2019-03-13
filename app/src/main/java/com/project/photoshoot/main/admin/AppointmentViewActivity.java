package com.project.photoshoot.main.admin;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.project.photoshoot.R;
import com.project.photoshoot.adapters.RequestViewPagerApdater;
import com.project.photoshoot.main.admin.fragments.ConfirmRequest;
import com.project.photoshoot.main.admin.fragments.DeclinedRequest;
import com.project.photoshoot.main.admin.fragments.PendingRequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class AppointmentViewActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    PendingRequest pendingRequest = new PendingRequest();
    ConfirmRequest confirmRequest = new ConfirmRequest();
    DeclinedRequest declinedRequest = new DeclinedRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_view);

        viewPager = findViewById(R.id.viewpager_id);
        RequestViewPagerApdater requestPagerApdater = new RequestViewPagerApdater(getSupportFragmentManager());

        requestPagerApdater.addFragment(pendingRequest, "Pending");
        requestPagerApdater.addFragment(confirmRequest, "Confirm");
        requestPagerApdater.addFragment(declinedRequest, "Declined");

        viewPager.setAdapter(requestPagerApdater);
        tabLayout = findViewById(R.id.tablayout_id);
        tabLayout.setupWithViewPager(viewPager);

    }
}
