package com.example.fbuapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbuapp.Adapters.RideStreamPagerAdapter;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.Models.RideRequest;
import com.example.fbuapp.databinding.FragmentRideStreamBinding;
import com.google.android.material.tabs.TabLayout;

public class RideStreamFragment extends Fragment {

    private FragmentRideStreamBinding mBinding;

    private RideRequest mRideRequest;
    private RideOffer mRideOffer;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    public RideStreamFragment() {
        // Required empty public constructor
    }

    public static RideStreamFragment newInstance(RideOffer rideOffer) {
        Bundle args = new Bundle();
        RideStreamFragment fragment = new RideStreamFragment();
        args.putParcelable("rideOffer", rideOffer);
        fragment.setArguments(args);
        return fragment;
    }

    public static RideStreamFragment newInstance(RideRequest rideRequest) {
        Bundle args = new Bundle();
        RideStreamFragment fragment = new RideStreamFragment();
        args.putParcelable("rideRequest", rideRequest);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getArguments() != null){
            mRideOffer = getArguments().getParcelable("rideOffer");
            mRideRequest = getArguments().getParcelable("rideRequest");
        }
        mBinding = FragmentRideStreamBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bind();

        RideStreamPagerAdapter adapter = new RideStreamPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        if(mRideRequest != null){
            viewPager.setCurrentItem(1);
        }
        else{
            viewPager.setCurrentItem(0);
        }
        tabLayout.setupWithViewPager(viewPager);

    }

    private void bind() {
        viewPager = mBinding.viewPager;
        tabLayout = mBinding.slidingTabs;
    }
}