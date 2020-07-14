package com.example.fbuapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbuapp.R;
import com.example.fbuapp.RideStreamFragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class RideStreamFragment extends Fragment {

    public RideStreamFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_stream, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new RideStreamFragmentPagerAdapter(getActivity().getSupportFragmentManager(), getContext()));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }
}