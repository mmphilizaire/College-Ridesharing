package com.example.fbuapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class RideStreamPageFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private RideOffersAdapter adapter;
    private ArrayList<RideOffer> rideOffers;
    private RecyclerView rideOffersRecyclerView;

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
        if(mPage == 1){
            rideOffersRecyclerView = view.findViewById(R.id.rvRideOffers);
            rideOffers = new ArrayList<>();
            adapter = new RideOffersAdapter(rideOffers, getContext());
            rideOffersRecyclerView.setAdapter(adapter);
            rideOffersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            rideOffersList();
        }
        else{

        }
        return view;
    }

    private void rideOffersList() {
        ParseQuery<RideOffer> query = ParseQuery.getQuery(RideOffer.class);
        query.include(RideOffer.KEY_USER);
        query.setLimit(20);
        query.findInBackground(new FindCallback<RideOffer>() {
            @Override
            public void done(List<RideOffer> objects, ParseException e) {
                if(e != null){
                    //error handling
                    return;
                }
                adapter.addAll(objects);
            }
        });
    }
}