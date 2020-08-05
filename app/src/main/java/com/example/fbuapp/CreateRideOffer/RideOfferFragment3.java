package com.example.fbuapp.CreateRideOffer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.fbuapp.Activities.MainActivity;
import com.example.fbuapp.Activities.MapActivity;
import com.example.fbuapp.Models.Location;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.Models.RideRequest;
import com.example.fbuapp.R;
import com.example.fbuapp.databinding.FragmentRideOffer1Binding;
import com.example.fbuapp.databinding.FragmentRideOffer3Binding;
import com.example.fbuapp.databinding.FragmentRideRequest1Binding;
import com.parse.ParseGeoPoint;

import static android.app.Activity.RESULT_OK;

public class RideOfferFragment3 extends Fragment {

    private static final String ARG_RIDE_OFFER = "rideOffer";

    FragmentRideOffer3Binding mBinding;

    private RideOffer mRideOffer;

    private NumberPicker mSeatCountNumberPicker;
    private EditText mSeatPriceEditText;
    public Button mNextButton;

    public RideOfferFragment3() {
        // Required empty public constructor
    }

    public static RideOfferFragment3 newInstance(RideOffer rideOffer) {
        RideOfferFragment3 fragment = new RideOfferFragment3();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RIDE_OFFER, rideOffer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRideOffer = getArguments().getParcelable(ARG_RIDE_OFFER);
        mBinding = FragmentRideOffer3Binding.inflate(getLayoutInflater(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bind();
        configureNumberPicker();

        if(mRideOffer.getSeatCount() != null){
            mSeatCountNumberPicker.setValue(mRideOffer.getSeatCount().intValue());
            mSeatPriceEditText.setText(mRideOffer.getSeatPrice().toString());
        }

        setOnClickListeners();
    }

    private void configureNumberPicker() {
        mSeatCountNumberPicker.setMinValue(1);
        mSeatCountNumberPicker.setMaxValue(6);
        mSeatCountNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    private void setOnClickListeners() {
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!missingInfo()){
                    mRideOffer.setSeatPrice((Number) Integer.parseInt(mSeatPriceEditText.getText().toString()));
                    mRideOffer.setSeatCount((Number) mSeatCountNumberPicker.getValue());
                    MainActivity activity = (MainActivity) getActivity();
                    activity.goToNextRideOfferFragment(mRideOffer);
                }
                else{
                    Toast.makeText(getActivity(), "Missing information!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean missingInfo() {
        if(mSeatPriceEditText.getText().toString().equals("")){
            return true;
        }
        return false;
    }

    private void bind() {
        mSeatCountNumberPicker = mBinding.npSeatCount;
        mSeatPriceEditText = mBinding.etSeatPrice;
        mNextButton = mBinding.btnNext;
    }

}