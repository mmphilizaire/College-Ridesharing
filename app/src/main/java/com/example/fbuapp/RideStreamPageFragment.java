package com.example.fbuapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.fbuapp.Models.RideRequest;
import com.example.fbuapp.databinding.FragmentRideStreamPageBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RideStreamPageFragment extends Fragment implements FilterRideOfferDialogFragment.FilterRideOfferDialogListener, FilterRideRequestDialogFragment.FilterRideRequestDialogListener {

    public static final String ARG_PAGE = "ARG_PAGE";

    private FragmentRideStreamPageBinding mBinding;

    private int mPage;
    private int mSortBy = 0;

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

        bind();

        if(mPage == 1){
            createRideOfferStream();
        }
        else{
            createRideRequestStream();
        }

        return mBinding.getRoot();
    }

    private void createRideRequestStream() {
        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterRideRequestResults();
            }
        });
        mRideRequestsRecyclerView = mBinding.rvRideOffers;
        mRideRequests = new ArrayList<>();
        mRequestsAdpater = new RideRequestsAdapter(mRideRequests, getContext(), this);
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
        mRideOffersRecyclerView = mBinding.rvRideOffers;
        mRideOffers = new ArrayList<>();
        mOffersAdapter = new RideOffersAdapter(mRideOffers, getContext(), this);
        mRideOffersRecyclerView.setAdapter(mOffersAdapter);
        mRideOffersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setSortListener();
        rideOffersList();
    }

    private void setSortListener() {
        mSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        mSortBy = 0;
                        break;
                    case 1:
                        mSortBy = 1;
                        break;
                    case 2:
                        mSortBy = 2;
                        break;
                }
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
        FilterRideOfferDialogFragment filterDialogFragment = FilterRideOfferDialogFragment.newInstance();
        filterDialogFragment.setTargetFragment(RideStreamPageFragment.this, 200);
        filterDialogFragment.show(fragmentManager, "filter_fragment");
    }

    private void filterRideRequestResults(){
        FragmentManager fragmentManager = getFragmentManager();
        FilterRideRequestDialogFragment filterDialogFragment = FilterRideRequestDialogFragment.newInstance();
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
                mOffersAdapter.clear();
                mOffersAdapter.addAll(objects);
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
                mRequestsAdpater.clear();
                mRequestsAdpater.addAll(objects);
            }
        });
    }

    @Override
    public void onFinishRideRequestFilterDialog(final Location start, final int radiusStart, final Location end, final int radiusEnd, final Calendar date){
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
                    if(start != null && object.getStartLocation().getGeoPoint().distanceInMilesTo(start.getGeoPoint()) > radiusStart){
                        continue;
                    }
                    if(end != null && object.getEndLocation().getGeoPoint().distanceInMilesTo(end.getGeoPoint()) > radiusEnd){
                        continue;
                    }
                    if(date != null && (!object.getEarliestDeparture().before(date.getTime()) || !object.getLatestDeparture().after(date.getTime()))){
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
    public void onFinishRideOfferFilterDialog(final Location start, final int radiusStart, final Location end, final int radiusEnd, final Calendar earliest, final Calendar latest, final boolean hideFullRides){
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
                    if(start != null && object.getStartLocation().getGeoPoint().distanceInMilesTo(start.getGeoPoint()) > radiusStart){
                        continue;
                    }
                    if(end != null && object.getEndLocation().getGeoPoint().distanceInMilesTo(end.getGeoPoint()) > radiusEnd){
                        continue;
                    }
                    if(earliest != null && !object.getDepartureTime().after(earliest.getTime())){
                        continue;
                    }
                    if(latest != null && !object.getDepartureTime().before(latest.getTime())){
                        continue;
                    }
                    if(hideFullRides && object.getPassengers().length() == object.getSeatCount().intValue()){
                        continue;
                    }
                    filtered.add(object);
                }
                mOffersAdapter.clear();
                mOffersAdapter.addAll(filtered);
            }
        });
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

}