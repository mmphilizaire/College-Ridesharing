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
import android.widget.Toast;

import com.example.fbuapp.Activities.MainActivity;
import com.example.fbuapp.Activities.MapActivity;
import com.example.fbuapp.Models.Location;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.Models.RideRequest;
import com.example.fbuapp.R;
import com.example.fbuapp.databinding.FragmentRideOffer1Binding;
import com.example.fbuapp.databinding.FragmentRideRequest1Binding;
import com.parse.ParseGeoPoint;

import static android.app.Activity.RESULT_OK;

public class RideOfferFragment1 extends Fragment {

    public static final int START_REQUEST_CODE = 1234;
    public static final int END_REQUEST_CODE = 4321;

    private static final String ARG_RIDE_OFFER = "rideOffer";

    FragmentRideOffer1Binding mBinding;

    private RideOffer mRideOffer;
    public Location mStartLocation;
    public Location mEndLocation;

    private EditText mStartLocationEditText;
    private EditText mEndLocationEditText;
    public Button mNextButton;

    public RideOfferFragment1() {
        // Required empty public constructor
    }

    public static RideOfferFragment1 newInstance(RideOffer rideOffer) {
        RideOfferFragment1 fragment = new RideOfferFragment1();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RIDE_OFFER, rideOffer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRideOffer = getArguments().getParcelable(ARG_RIDE_OFFER);
        mBinding = FragmentRideOffer1Binding.inflate(getLayoutInflater(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStartLocation = new Location();
        mEndLocation = new Location();

        bind();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        mStartLocationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map = new Intent(getActivity(), MapActivity.class);
                startActivityForResult(map, START_REQUEST_CODE);
            }
        });

        mEndLocationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map = new Intent(getActivity(), MapActivity.class);
                startActivityForResult(map, END_REQUEST_CODE);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!missingInfo()){
                    mRideOffer.setStartLocation(mStartLocation);
                    mRideOffer.setEndLocation(mEndLocation);
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
        if(mStartLocationEditText.getText().toString().equals("") || mEndLocationEditText.getText().toString().equals("")){
            return true;
        }
        return false;
    }

    private void bind() {
        mStartLocationEditText = mBinding.etStartLocation;
        mEndLocationEditText = mBinding.etEndLocation;
        mNextButton = mBinding.btnNext;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == START_REQUEST_CODE) {
            double latitude = data.getExtras().getDouble("latitude");
            double longitude = data.getExtras().getDouble("longitude");
            mStartLocation.setLatitude((Number) latitude );
            mStartLocation.setLongitude((Number) longitude);
            mStartLocation.setCity(data.getExtras().getString("city"));
            mStartLocation.setState(data.getExtras().getString("state"));
            mStartLocation.setGeoPoint(new ParseGeoPoint(latitude, longitude));
            mStartLocationEditText.setText(mStartLocation.getCity() + ", " + mStartLocation.getState());
        }
        else if(resultCode == RESULT_OK && requestCode == END_REQUEST_CODE){
            double latitude = data.getExtras().getDouble("latitude");
            double longitude = data.getExtras().getDouble("longitude");
            mEndLocation.setLatitude((Number) latitude);
            mEndLocation.setLongitude((Number) longitude);
            mEndLocation.setCity(data.getExtras().getString("city"));
            mEndLocation.setState(data.getExtras().getString("state"));
            mEndLocation.setGeoPoint(new ParseGeoPoint(latitude, longitude));
            mEndLocationEditText.setText(mEndLocation.getCity() + ", " + mEndLocation.getState());
        }
    }

}