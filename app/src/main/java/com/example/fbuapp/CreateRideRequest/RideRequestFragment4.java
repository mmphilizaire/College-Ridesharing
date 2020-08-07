package com.example.fbuapp.CreateRideRequest;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fbuapp.Activities.MainActivity;
import com.example.fbuapp.Adapters.RideOffersAdapter;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.Models.RideRequest;
import com.example.fbuapp.databinding.FragmentRideRequest4Binding;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class RideRequestFragment4 extends Fragment {

    private static final String ARG_RIDE_REQUEST = "rideRequest";

    FragmentRideRequest4Binding mBinding;

    private RideRequest mRideRequest;
    private ArrayList<RideOffer> mRideOffers;
    private RideOffersAdapter mAdapter;

    private TextView mStartLocationTextView;
    private TextView mEndLocationTextView;
    private TextView mEarliestTimeTextView;
    private TextView mEarliestDateTextView;
    private TextView mLatestTimeTextView;
    private TextView mLatestDateTextView;
    private TextView mResultsTextView;
    private RecyclerView mResultsRecyclerView;
    public Button mCreateButton;
    public Button mCancelButton;

    public RideRequestFragment4() {
        // Required empty public constructor
    }

    public static RideRequestFragment4 newInstance(RideRequest rideRequest) {
        RideRequestFragment4 fragment = new RideRequestFragment4();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RIDE_REQUEST, rideRequest);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRideRequest = getArguments().getParcelable(ARG_RIDE_REQUEST);
        mBinding = FragmentRideRequest4Binding.inflate(getLayoutInflater(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bind();
        configureResults();
        bindData();

        loadButton();

        if(mRideOffers.size() == 0){
            mResultsTextView.setText("No Matching Ride Offers");
        }

        setOnClickListeners();
    }

    private void loadButton() {
        if(isBooked()){
            mCreateButton.setVisibility(View.GONE);
            mCancelButton.setVisibility(View.VISIBLE);
        }
    }

    private void setOnClickListeners() {
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRideRequest.saveInBackground();
                MainActivity activity = (MainActivity) getActivity();
                activity.goToRideRequestStream(mRideRequest);
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                //TODO: don't put random rideoffer
                activity.goToRideOfferStream(new RideOffer());
            }
        });
    }

    private void configureResults() {
        mRideOffers = new ArrayList<>();
        mAdapter = new RideOffersAdapter(mRideOffers, this);
        matchingRideOffers(0);
        mResultsRecyclerView.setAdapter(mAdapter);
        mResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void bind() {
        mEarliestDateTextView = mBinding.tvEarliestDate;
        mEarliestTimeTextView = mBinding.tvEarliestTime;
        mLatestDateTextView = mBinding.tvLatestDate;
        mLatestTimeTextView = mBinding.tvLatestTime;
        mStartLocationTextView = mBinding.tvStartLocatoion;
        mEndLocationTextView = mBinding.tvEndLocation;
        mResultsTextView = mBinding.tvResults;
        mResultsRecyclerView = mBinding.rvResults;
        mCreateButton = mBinding.btnCreate;
        mCancelButton = mBinding.btnCancel;
    }

    public void bindData() {
        mEarliestDateTextView.setText(mRideRequest.getDay(mRideRequest.getEarliestDeparture()) + ", " + mRideRequest.getDateNoYear(mRideRequest.getEarliestDeparture()));
        mEarliestTimeTextView.setText(mRideRequest.getTime(mRideRequest.getEarliestDeparture()));
        mLatestDateTextView.setText(mRideRequest.getDay(mRideRequest.getLatestDeparture()) + ", " + mRideRequest.getDateNoYear(mRideRequest.getLatestDeparture()));
        mLatestTimeTextView.setText(mRideRequest.getTime(mRideRequest.getLatestDeparture()));
        mStartLocationTextView.setText(mRideRequest.getStartLocation().getCity()+", "+mRideRequest.getStartLocation().getState());
        mEndLocationTextView.setText(mRideRequest.getEndLocation().getCity()+", "+mRideRequest.getEndLocation().getState());
    }

    private void matchingRideOffers(int page) {
        ParseQuery<RideOffer> query = ParseQuery.getQuery(RideOffer.class);
        query.include(RideOffer.KEY_USER);
        query.include(RideOffer.KEY_START_LOCATION);
        query.include(RideOffer.KEY_END_LOCATION);
        query.setLimit(100);
        query.setSkip(100*page);
        try {
            List<RideOffer> objects = query.find();
            List<RideOffer> filtered = new ArrayList<>();
            for(RideOffer object : objects){
                if(object.getStartLocation().getGeoPoint().distanceInMilesTo(mRideRequest.getStartLocation().getGeoPoint()) > 20){
                    continue;
                }
                if(object.getEndLocation().getGeoPoint().distanceInMilesTo(mRideRequest.getEndLocation().getGeoPoint()) > 20){
                    continue;
                }
                if(!object.getDepartureTime().after(mRideRequest.getEarliestDeparture())){
                    continue;
                }
                if(!object.getDepartureTime().before(mRideRequest.getLatestDeparture())){
                    continue;
                }
                if(object.getPassengers().length() == object.getSeatCount().intValue()){
                    continue;
                }
                filtered.add(object);
            }
            mRideOffers.addAll(filtered);
            mAdapter.notifyDataSetChanged();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean isBooked(){
        for(int i = 0; i < mRideOffers.size(); i++){
            if(mRideOffers.get(i).hasPassenger(ParseUser.getCurrentUser())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadButton();
    }
}