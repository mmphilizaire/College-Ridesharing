package com.example.fbuapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.example.fbuapp.Activities.MainActivity;
import com.example.fbuapp.Adapters.RideOffersAdapter;
import com.example.fbuapp.Adapters.RideRequestsAdapter;
import com.example.fbuapp.CreateRideOffer.RideOfferFragment1;
import com.example.fbuapp.CreateRideOffer.RideOfferFragment2;
import com.example.fbuapp.Fragments.FilterRideOfferDialogFragment;
import com.example.fbuapp.Fragments.FilterRideRequestDialogFragment;
import com.example.fbuapp.CreateRideOffer.RideOfferFragment;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.Models.RideOfferFilter;
import com.example.fbuapp.Models.RideRequest;
import com.example.fbuapp.Models.RideRequestFilter;
import com.example.fbuapp.databinding.FragmentRideStreamPageBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class RideStreamPageFragment extends Fragment implements FilterRideOfferDialogFragment.FilterRideOfferDialogListener, FilterRideRequestDialogFragment.FilterRideRequestDialogListener {

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final int REFRESH_REQUEST_CODE = 212;
    public static final int CREATE_REQUEST_CODE = 571;

    private FragmentRideStreamPageBinding mBinding;

    private int mPage;
    private int mSortBy = 0;

    private RideOfferFilter mRideOfferFilter;
    private RideRequestFilter mRideRequestFilter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RideOffersAdapter mOffersAdapter;
    private RideRequestsAdapter mRequestsAdpater;
    private ArrayList<RideOffer> mRideOffers;
    private ArrayList<RideRequest> mRideRequests;
    private RecyclerView mRideOffersRecyclerView;
    private RecyclerView mRideRequestsRecyclerView;
    private EndlessRecyclerViewScrollListener mScrollListener;
    private LinearLayoutManager mLinearLayoutManager;
    private DividerItemDecoration mItemDecor;
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

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                if(mPage == 1){
                    rideOffersList(0, true);
                }
                else{
                    rideRequestsList(0, true);
                }
            }
        });

        mLinearLayoutManager = new LinearLayoutManager(getContext());

        mScrollListener = new EndlessRecyclerViewScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(mPage == 1){
                    rideOffersList(page, false);
                }
                else{
                    rideRequestsList(page, false);
                }
            }
        };

        mItemDecor = new DividerItemDecoration(getContext(), mLinearLayoutManager.getOrientation());

        if(mPage == 1){
            createRideOfferStream();
        }
        else{
            createRideRequestStream();
        }

    }

    private void createRideRequestStream() {
        mRideRequestFilter = new RideRequestFilter();
        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterRideRequestResults();
            }
        });
        mRideRequestsRecyclerView = mBinding.rvRideOffers;
        mRideRequests = new ArrayList<>();
        mRequestsAdpater = new RideRequestsAdapter(mRideRequests, this);
        mRideRequestsRecyclerView.setAdapter(mRequestsAdpater);
        mRideRequestsRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRideRequestsRecyclerView.addOnScrollListener(mScrollListener);
        mRideRequestsRecyclerView.addItemDecoration(mItemDecor);
        setSortListener();
        rideRequestsList(0, false);
    }

    private void createRideOfferStream() {
        mRideOfferFilter = new RideOfferFilter();
        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterRideOfferResults();
            }
        });
        mRideOffersRecyclerView = mBinding.rvRideOffers;
        mRideOffers = new ArrayList<>();
        mOffersAdapter = new RideOffersAdapter(mRideOffers, this);
        mRideOffersRecyclerView.setAdapter(mOffersAdapter);
        mRideOffersRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRideOffersRecyclerView.addOnScrollListener(mScrollListener);
        mRideOffersRecyclerView.addItemDecoration(mItemDecor);
        setSortListener();
        rideOffersList(0, false);
    }

    private void setSortListener() {
        mSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSortBy = i;
                if(mPage == 1){
                    rideOffersList(0, true);
                    mRideOffersRecyclerView.scrollToPosition(0);
                }
                else{
                    rideRequestsList(0, true);
                    mRideRequestsRecyclerView.scrollToPosition(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    private void bind() {
        mFilterButton = mBinding.btnFilter;
        mSortSpinner = mBinding.spSort;
        mSwipeRefreshLayout = mBinding.swipeContainer;
    }

    private void filterRideOfferResults(){
        FragmentManager fragmentManager = getFragmentManager();
        FilterRideOfferDialogFragment filterDialogFragment = FilterRideOfferDialogFragment.newInstance(mRideOfferFilter);
        filterDialogFragment.setTargetFragment(RideStreamPageFragment.this, 200);
        filterDialogFragment.show(fragmentManager, "filter_fragment");
    }

    private void filterRideRequestResults(){
        FragmentManager fragmentManager = getFragmentManager();
        FilterRideRequestDialogFragment filterDialogFragment = FilterRideRequestDialogFragment.newInstance(mRideRequestFilter);
        filterDialogFragment.setTargetFragment(RideStreamPageFragment.this, 200);
        filterDialogFragment.show(fragmentManager, "filter_fragment");
    }

    private void rideOffersList(int page, final boolean clear) {
        ParseQuery<RideOffer> query = ParseQuery.getQuery(RideOffer.class);
        query.include(RideOffer.KEY_USER);
        query.include(RideOffer.KEY_START_LOCATION);
        query.include(RideOffer.KEY_END_LOCATION);
        sortRideOffers(query);
        query.setLimit(20);
        query.setSkip(20*page);
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
                if(clear){
                    mOffersAdapter.clear();
                    mScrollListener.resetState();
                }
                mOffersAdapter.addAll(filtered);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void rideRequestsList(int page, final boolean clear) {
        ParseQuery<RideRequest> query = ParseQuery.getQuery(RideRequest.class);
        query.include(RideRequest.KEY_USER);
        query.include(RideRequest.KEY_START_LOCATION);
        query.include(RideRequest.KEY_END_LOCATION);
        sortRideRequests(query);
        query.setLimit(20);
        query.setSkip(20*page);
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
                if(clear){
                    mRequestsAdpater.clear();
                    mScrollListener.resetState();
                }
                mRequestsAdpater.addAll(filtered);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onFinishRideRequestFilterDialog(RideRequestFilter filter){
        mRideRequestFilter = filter;
        rideRequestsList(0, true);
        mRideRequestsRecyclerView.scrollToPosition(0);
    }

    @Override
    public void onFinishRideOfferFilterDialog(RideOfferFilter filter){
        mRideOfferFilter = filter;
        rideOffersList(0, true);
        mRideOffersRecyclerView.scrollToPosition(0);
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
        if(requestCode == REFRESH_REQUEST_CODE){
            rideOffersList(0, true);
        }
        else if(requestCode == CREATE_REQUEST_CODE && data != null){
            //TODO: use new fragments
            RideRequest rideRequest = data.getParcelableExtra("rideRequest");
            MainActivity activity = (MainActivity) getActivity();
            activity.createOfferFromRequest(rideRequest);
        }
    }
}