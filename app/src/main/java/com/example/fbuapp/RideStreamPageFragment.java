package com.example.fbuapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fbuapp.Fragments.FilterDialogFragment;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.Models.RideRequest;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class RideStreamPageFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private RideOffersAdapter mOffersAdapter;
    private RideRequestsAdapter mRequestsAdpater;
    private ArrayList<RideOffer> mRideOffers;
    private ArrayList<RideRequest> mRideRequests;
    private RecyclerView mRideOffersRecyclerView;
    private Button mFilterButton;
    private Button mSortButton;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ride_page_stream, container, false);

        mFilterButton = view.findViewById(R.id.btnFilter);
        mSortButton = view.findViewById(R.id.btnSort);

        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterResults();
            }
        });
        mSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortResults();
            }
        });

        if(mPage == 1){
            mRideOffersRecyclerView = view.findViewById(R.id.rvRideOffers);
            mRideOffers = new ArrayList<>();
            mOffersAdapter = new RideOffersAdapter(mRideOffers, getContext());
            mRideOffersRecyclerView.setAdapter(mOffersAdapter);
            mRideOffersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            rideOffersList();
        }
        else{
            mRideOffersRecyclerView = view.findViewById(R.id.rvRideOffers);
            mRideRequests = new ArrayList<>();
            mRequestsAdpater = new RideRequestsAdapter(mRideRequests, getContext());
            mRideOffersRecyclerView.setAdapter(mRequestsAdpater);
            mRideOffersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            rideRequestsList();
        }
        return view;
    }

    private void filterResults(){
        FragmentManager fragmentManager = getChildFragmentManager();
        FilterDialogFragment filterDialogFragment = FilterDialogFragment.newInstance();
        filterDialogFragment.show(fragmentManager, "filter_fragment");
    }

    private void sortResults(){

    }

    private void rideOffersList() {
        ParseQuery<RideOffer> query = ParseQuery.getQuery(RideOffer.class);
        query.include(RideOffer.KEY_USER);
        query.include(RideOffer.KEY_START_LOCATION);
        query.include(RideOffer.KEY_END_LOCATION);
        query.setLimit(20);
        query.findInBackground(new FindCallback<RideOffer>() {
            @Override
            public void done(List<RideOffer> objects, ParseException e) {
                if(e != null){
                    //error handling
                    return;
                }
                mOffersAdapter.addAll(objects);
            }
        });
    }

    private void rideRequestsList() {
        ParseQuery<RideRequest> query = ParseQuery.getQuery(RideRequest.class);
        query.include(RideRequest.KEY_USER);
        query.include(RideRequest.KEY_START_LOCATION);
        query.include(RideRequest.KEY_END_LOCATION);
        query.setLimit(20);
        query.findInBackground(new FindCallback<RideRequest>() {
            @Override
            public void done(List<RideRequest> objects, ParseException e) {
                if(e != null){
                    //error handling
                    return;
                }
                mRequestsAdpater.addAll(objects);
            }
        });
    }
}