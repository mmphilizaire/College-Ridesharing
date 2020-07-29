package com.example.fbuapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.example.fbuapp.Fragments.FilterRideOfferDialogFragment;
import com.example.fbuapp.Fragments.FilterRideRequestDialogFragment;
import com.example.fbuapp.Models.Location;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.Models.RideOfferFilter;
import com.example.fbuapp.Models.RideRequest;
import com.example.fbuapp.Models.RideRequestFilter;
import com.example.fbuapp.databinding.FragmentRideStreamPageBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RideStreamPageFragment extends Fragment implements FilterRideOfferDialogFragment.FilterRideOfferDialogListener, FilterRideRequestDialogFragment.FilterRideRequestDialogListener {

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final int DETAIL_REQUEST_CODE = 212;

    private FragmentRideStreamPageBinding mBinding;

    private int mPage;
    private int mSortBy = 0;

    private RideOfferFilter mRideOfferFilter;
    private RideRequestFilter mRideRequestFilter;

    private RideOffersAdapter mOffersAdapter;
    private RideRequestsAdapter mRequestsAdpater;
    private ArrayList<RideOffer> mRideOffers;
    private ArrayList<RideRequest> mRideRequests;
    private RecyclerView mRideOffersRecyclerView;
    private RecyclerView mRideRequestsRecyclerView;
    private Button mFilterButton;
    private Spinner mSortSpinner;

    public RideStreamPageFragment() {
        // Required empty public constructor
    }

    public static RideStreamPageFragment newInstance(int mPage) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, mPage);
        RideStreamPageFragment fragment = new RideStreamPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = FragmentRideStreamPageBinding.inflate(inflater, container, false);
        mPage = getArguments().getInt(ARG_PAGE);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind();

        if(mPage == 1){
            createRideOfferStream();
        }
        else{
            createRideRequestStream();
        }
    }

    private void createRideRequestStream() {
        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterRideRequestResults();
            }
        });
        mRideRequestFilter = new RideRequestFilter();
        mRideRequestsRecyclerView = mBinding.rvRideOffers;
        mRideRequests = new ArrayList<>();
        mRequestsAdpater = new RideRequestsAdapter(mRideRequests, getContext());
        mRideRequestsRecyclerView.setAdapter(mRequestsAdpater);
        mRideRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setSortListener();
        rideRequestsList();
    }

    private void createRideOfferStream() {
        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterRideOfferResults();
            }
        });
        mRideOfferFilter = new RideOfferFilter();
        mRideOffersRecyclerView = mBinding.rvRideOffers;
        mRideOffers = new ArrayList<>();
        mOffersAdapter = new RideOffersAdapter(mRideOffers, this);
        mRideOffersRecyclerView.setAdapter(mOffersAdapter);
        mRideOffersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setSortListener();
        rideOffersList();
    }

    private void setSortListener() {
        mSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSortBy = i;
                if(mPage == 1){
                    rideOffersList();
                }
                else{
                    rideRequestsList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    private void bind() {
        mFilterButton = mBinding.btnFilter;
        mSortSpinner = mBinding.spSort;
    }

    private void filterRideOfferResults(){
        FragmentManager fragmentManager = getFragmentManager();
        FilterRideOfferDialogFragment filterDialogFragment = new FilterRideOfferDialogFragment();
        filterDialogFragment.setTargetFragment(RideStreamPageFragment.this, 200);
        filterDialogFragment.show(fragmentManager, "filter_fragment");
    }

    private void filterRideRequestResults(){
        FragmentManager fragmentManager = getFragmentManager();
        FilterRideRequestDialogFragment filterDialogFragment = new FilterRideRequestDialogFragment();
        filterDialogFragment.setTargetFragment(RideStreamPageFragment.this, 200);
        filterDialogFragment.show(fragmentManager, "filter_fragment");
    }

    private void rideOffersList() {
        ParseQuery<RideOffer> query = ParseQuery.getQuery(RideOffer.class);
        query.include(RideOffer.KEY_USER);
        query.include(RideOffer.KEY_START_LOCATION);
        query.include(RideOffer.KEY_END_LOCATION);
        sortRideOffers(query);
        query.setLimit(20);
        query.findInBackground(new FindCallback<RideOffer>() {
            @Override
            public void done(List<RideOffer> objects, ParseException e) {
                if(e != null){
                    //error handling
                    return;
                }
                List<RideOffer> filtered = new ArrayList<>();
                for(RideOffer object : objects){
                    if(mRideOfferFilter.hasStartLocation() && object.getStartLocation().getGeoPoint().distanceInMilesTo(mRideOfferFilter.getStartLocation().getGeoPoint()) > mRideOfferFilter.getStartMileRadius()){
                        continue;
                    }
                    if(mRideOfferFilter.hasEndLocation() && object.getEndLocation().getGeoPoint().distanceInMilesTo(mRideOfferFilter.getEndLocation().getGeoPoint()) > mRideOfferFilter.getEndMileRadius()){
                        continue;
                    }
                    if(mRideOfferFilter.hasEarliestDeparture() && !object.getDepartureTime().after(mRideOfferFilter.getEarliestDeparture())){
                        continue;
                    }
                    if(mRideOfferFilter.hasLatestDeparture() && !object.getDepartureTime().before(mRideOfferFilter.getLatestDeparture())){
                        continue;
                    }
                    if(mRideOfferFilter.getHideFullRides() && object.getPassengers().length() == object.getSeatCount().intValue()){
                        continue;
                    }
                    filtered.add(object);
                }
                mOffersAdapter.clear();
                mOffersAdapter.addAll(filtered);
            }
        });

    }

    private void rideRequestsList() {
        ParseQuery<RideRequest> query = ParseQuery.getQuery(RideRequest.class);
        query.include(RideRequest.KEY_USER);
        query.include(RideRequest.KEY_START_LOCATION);
        query.include(RideRequest.KEY_END_LOCATION);
        sortRideRequests(query);
        query.setLimit(20);
        query.findInBackground(new FindCallback<RideRequest>() {
            @Override
            public void done(List<RideRequest> objects, ParseException e) {
                if(e != null){
                    //error handling
                    return;
                }
                List<RideRequest> filtered = new ArrayList<>();
                for(RideRequest object : objects){
                    if(mRideRequestFilter.hasStartLocation() && object.getStartLocation().getGeoPoint().distanceInMilesTo(mRideRequestFilter.getStartLocation().getGeoPoint()) > mRideRequestFilter.getStartMileRadius()){
                        continue;
                    }
                    if(mRideRequestFilter.hasEndLocation() && object.getEndLocation().getGeoPoint().distanceInMilesTo(mRideRequestFilter.getEndLocation().getGeoPoint()) > mRideRequestFilter.getEndMileRadius()){
                        continue;
                    }
                    if(mRideRequestFilter.hasDeparture() && (!object.getEarliestDeparture().before(mRideRequestFilter.getDeparture()) || !object.getLatestDeparture().after(mRideRequestFilter.getDeparture()))){
                        continue;
                    }
                    filtered.add(object);
                }
                mRequestsAdpater.clear();
                mRequestsAdpater.addAll(filtered);
            }
        });
    }

    @Override
    public void onFinishRideRequestFilterDialog(RideRequestFilter filter){
        mRideRequestFilter = filter;
        rideRequestsList();
    }

    @Override
    public void onFinishRideOfferFilterDialog(RideOfferFilter filter){
        mRideOfferFilter = filter;
        rideOffersList();
    }

    private void sortRideOffers(ParseQuery<RideOffer> query) {
        if(mSortBy == 0){
            query.orderByDescending("createdAt");
        }
        else if(mSortBy == 1){
            query.orderByAscending("departureTime");
        }
        else{
            query.orderByDescending("departureTime");
        }
    }

    private void sortRideRequests(ParseQuery<RideRequest> query) {
        if(mSortBy == 0){
            query.orderByDescending("createdAt");
        }
        else if(mSortBy == 1){
            query.orderByAscending("earliestDeparture");
        }
        else{
            query.orderByDescending("earliestDeparture");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DETAIL_REQUEST_CODE){
            rideOffersList();
        }
    }
}