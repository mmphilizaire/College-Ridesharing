package com.example.fbuapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbuapp.Adapters.RideStreamPagerAdapter;
import com.example.fbuapp.databinding.FragmentRideStreamBinding;
import com.google.android.material.tabs.TabLayout;

public class RideStreamFragment extends Fragment {

    private FragmentRideStreamBinding mBinding;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    public RideStreamFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentRideStreamBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bind();

        viewPager.setAdapter(new RideStreamPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

    }

    private void bind() {
        viewPager = mBinding.viewPager;
        tabLayout = mBinding.slidingTabs;
    }
}