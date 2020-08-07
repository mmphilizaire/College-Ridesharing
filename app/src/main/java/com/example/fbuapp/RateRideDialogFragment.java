package com.example.fbuapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.databinding.FragmentRateRideBinding;

import org.json.JSONArray;
import org.w3c.dom.Text;

public class RateRideDialogFragment extends DialogFragment {

    FragmentRateRideBinding mBinding;

    private RideOffer mRide;

    private TextView mDescriptionTextView;
    private TextView mNameTextView;
    private RatingBar mReviewRatingBar;
    private TextView mSubmitTextView;

    public RateRideDialogFragment() {
        // Required empty public constructor
    }

    public static RateRideDialogFragment newInstance(RideOffer rideOffer) {
        RateRideDialogFragment fragment = new RateRideDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("ride", rideOffer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRide = getArguments().getParcelable("ride");
        mBinding = FragmentRateRideBinding.inflate(getLayoutInflater(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindViews();
        bindData();

        mSubmitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rating = mReviewRatingBar.getNumStars();
                JSONArray ratings = mRide.getUser().getJSONArray("ratings");
                ratings.put(rating);
                mRide.getUser().put("ratings", ratings);
            }
        });

    }

    private void bindViews() {
        mDescriptionTextView = mBinding.tvDescription;
        mReviewRatingBar = mBinding.rbDriver;
        mNameTextView = mBinding.tvDriver;
        mSubmitTextView = mBinding.tvSubmit;
    }

    private void bindData(){
        mDescriptionTextView.setText("From " + mRide.getStartLocation().getCity() + ", " + mRide.getStartLocation().getState() + " to " + mRide.getEndLocation().getCity() + ", " + mRide.getEndLocation().getState() + " on " + mRide.getDateNoYear());
        mNameTextView.setText(mRide.getUser().getString("firstName"));
    }
}