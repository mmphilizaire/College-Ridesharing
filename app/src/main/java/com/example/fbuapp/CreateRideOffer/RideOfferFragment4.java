package com.example.fbuapp.CreateRideOffer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fbuapp.Activities.MainActivity;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.databinding.FragmentRideOffer4Binding;


public class RideOfferFragment4 extends Fragment {

    private static final String ARG_RIDE_OFFER = "rideOffer";

    FragmentRideOffer4Binding mBinding;

    private RideOffer mRideOffer;

    public TextView mDateTextView;
    public TextView mTimeTextView;
    public TextView mStartLocationTextView;
    public TextView mEndLocationTextView;
    public TextView mSeatPriceTextView;
    public TextView mSeatCountTextView;
    public Button mCreateButton;

    public RideOfferFragment4() {
        // Required empty public constructor
    }

    public static RideOfferFragment4 newInstance(RideOffer rideOffer) {
        RideOfferFragment4 fragment = new RideOfferFragment4();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RIDE_OFFER, rideOffer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRideOffer = getArguments().getParcelable(ARG_RIDE_OFFER);
        mBinding = FragmentRideOffer4Binding.inflate(getLayoutInflater(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bind();
        bindData();

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRideOffer.saveInBackground();
                MainActivity activity = (MainActivity) getActivity();
                activity.goToRideOfferStream(mRideOffer);
            }
        });
    }

    private void bind() {
        mDateTextView = mBinding.tvDate;
        mTimeTextView = mBinding.tvTime;
        mStartLocationTextView = mBinding.tvStartLocatoion;
        mEndLocationTextView = mBinding.tvEndLocation;
        mSeatPriceTextView = mBinding.tvSeatPrice;
        mSeatCountTextView = mBinding.tvSeatCount;
        mCreateButton = mBinding.btnCreate;
    }

    public void bindData() {
        mDateTextView.setText(mRideOffer.getDay() + ", " + mRideOffer.getDateNoYear());
        mTimeTextView.setText(mRideOffer.getTime());
        mStartLocationTextView.setText(mRideOffer.getStartLocation().getCity()+", "+mRideOffer.getStartLocation().getState());
        mEndLocationTextView.setText(mRideOffer.getEndLocation().getCity()+", "+mRideOffer.getEndLocation().getState());
        mSeatPriceTextView.setText("$" + mRideOffer.getSeatPrice().toString() + " per seat");
        mSeatCountTextView.setText(mRideOffer.getSeatCount() + " seats available");
    }

}