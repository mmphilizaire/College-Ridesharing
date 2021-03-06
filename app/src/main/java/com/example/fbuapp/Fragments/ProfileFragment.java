package com.example.fbuapp.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.fbuapp.Activities.LoginActivity;
import com.example.fbuapp.Adapters.RideOffersProfileAdapter;
import com.example.fbuapp.Adapters.RideRequestsAdapter;
import com.example.fbuapp.Adapters.RideRequestsProfileAdapter;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.Models.RideRequest;
import com.example.fbuapp.R;
import com.example.fbuapp.databinding.FragmentProfileBinding;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProfileFragment extends Fragment implements DeleteDialogFragment.DeleteDialogListener {

    private FragmentProfileBinding mBinding;

    private ParseUser mUser;

    public ProfilePictureDialogFragment profilePictureDialogFragment;
    public List<RideOffer> mRideOffers;
    public List<RideRequest> mRideRequests;
    public RideOffersProfileAdapter mRideOffersAdapter;
    public RideRequestsProfileAdapter mRideRequestsAdapter;

    private ImageView mProfilePictureImageView;
    private TextView mNameTextView;
    private TextView mUniversityTextView;
    private TextView mMemberSinceTextView;
    private TextView mRidesDrivenTextView;
    private TextView mRidesRiddenTextView;
    private TextView mUpcomingRideOffers;
    private TextView mUpcomingRideRequests;
    private RecyclerView mRideOffersRecyclerView;
    private RecyclerView mRideRequestsRecyclerView;
    private Button mLogoutButton;

    public static ProfileFragment newInstance(ParseUser user){
        ProfileFragment profile = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        profile.setArguments(args);
        return profile;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentProfileBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mUser = getArguments().getParcelable("user");

        bindViews();
        bindData();

        if(isCurrentUser()){
            configureRecyclerViews();
        }
        else{
            mUpcomingRideRequests.setVisibility(View.GONE);
            mUpcomingRideOffers.setVisibility(View.GONE);
        }

        mProfilePictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCurrentUser()){
                    changeProfilePicture();
                }
            }
        });

        setupLogout();

    }

    private void configureRecyclerViews() {
        mRideOffers = new ArrayList<>();
        List<RideOffer> futureRideOffers = getFutureRideOffers();
        mRideOffers.addAll(getUsersRideOffers(futureRideOffers));
        mRideOffers.addAll(getUsersBookedRides(futureRideOffers));

        mRideRequests = new ArrayList<>();
        List<RideRequest> futureRideRequests = getFutureRideRequests();
        mRideRequests.addAll(getUsersRideRequests(futureRideRequests));

        if(mRideOffers.isEmpty()){
            mUpcomingRideOffers.setVisibility(View.GONE);
        }
        if(mRideRequests.isEmpty()){
            mUpcomingRideRequests.setVisibility(View.GONE);
        }

        mRideOffersAdapter = new RideOffersProfileAdapter(mRideOffers, this);
        mRideRequestsAdapter = new RideRequestsProfileAdapter(mRideRequests, this);

        mRideOffersRecyclerView.setAdapter(mRideOffersAdapter);
        mRideRequestsRecyclerView.setAdapter(mRideRequestsAdapter);
        mRideOffersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        mRideRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
    }

    private void setupLogout() {
        if(isCurrentUser()){
            mLogoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logOutUser();
                }
            });
        }
        else{
            mLogoutButton.setVisibility(View.GONE);
        }
    }

    private boolean isCurrentUser() {
        return mUser.getObjectId().equals(ParseUser.getCurrentUser().getObjectId());
    }

    private void bindData() {
        Glide.with(getContext()).load(mUser.getParseFile("profilePicture").getUrl()).transform(new CircleCrop()).into(mProfilePictureImageView);
        mNameTextView.setText(mUser.getString("firstName")+" "+ mUser.getString("lastName").substring(0,1)+".");
        mUniversityTextView.setText(mUser.getString("university"));
        List<RideOffer> pastRideOffers = getPastRideOffers();
        mMemberSinceTextView.setText("Member since "+DateFormat.format("MMMM yyyy", mUser.getCreatedAt()));
        mRidesDrivenTextView.setText("Driven " + ridesDrivenCount(pastRideOffers) + " Rides with " + passengersDrivenCount(pastRideOffers) + " Passengers");
        mRidesRiddenTextView.setText("Ridden " + ridesRiddenCount(pastRideOffers) + " Rides");
    }

    private List<RideOffer> getFutureRideOffers() {
        ArrayList<RideOffer> rideOffers = new ArrayList<>();
        boolean finished = false;
        while(!finished) {
            ParseQuery<RideOffer> query = ParseQuery.getQuery("RideOffer");
            query.include("user");
            query.include("startLocation");
            query.include("endLocation");
            query.whereGreaterThan("departureTime", Calendar.getInstance().getTime());
            List<RideOffer> results = null;
            try {
                results = query.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            rideOffers.addAll(results);
            if (results.size() < 100) {
                finished = true;
            }
        }
        return rideOffers;
    }

    private List<RideRequest> getFutureRideRequests() {
        ArrayList<RideRequest> rideRequests = new ArrayList<>();
        boolean finished = false;
        while(!finished) {
            ParseQuery<RideRequest> query = ParseQuery.getQuery("RideRequest");
            query.include("user");
            query.include("startLocation");
            query.include("endLocation");
            query.whereGreaterThan("earliestDeparture", Calendar.getInstance().getTime());
            List<RideRequest> results = null;
            try {
                results = query.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            rideRequests.addAll(results);
            if (results.size() < 100) {
                finished = true;
            }
        }
        return rideRequests;
    }

    private List<RideOffer> getUsersRideOffers(List<RideOffer> rideOffers){
        List<RideOffer> usersRideOffers = new ArrayList<>();
        for(RideOffer rideOffer : rideOffers){
            if(rideOffer.getUser().getObjectId().equals(mUser.getObjectId())){
                usersRideOffers.add(rideOffer);
            }
        }
        return usersRideOffers;
    }

    private List<RideOffer> getUsersBookedRides(List<RideOffer> rideOffers){
        List<RideOffer> bookedRideOffers = new ArrayList<>();
        for(RideOffer rideOffer : rideOffers){
            if(rideOffer.hasPassenger(mUser)){
                bookedRideOffers.add(rideOffer);
            }
        }
        return bookedRideOffers;
    }

    private List<RideRequest> getUsersRideRequests(List<RideRequest> rideRequests){
        List<RideRequest> usersRideRequests = new ArrayList<>();
        for(RideRequest rideRequest : rideRequests){
            if(rideRequest.getUser().getObjectId().equals(mUser.getObjectId())){
                usersRideRequests.add(rideRequest);
            }
        }
        return usersRideRequests;
    }

    private List<RideOffer> getPastRideOffers() {
        ArrayList<RideOffer> rideOffers = new ArrayList<>();
        boolean finished = false;
        while(!finished) {
            ParseQuery<RideOffer> query = ParseQuery.getQuery("RideOffer");
            query.include("user");
            query.whereLessThanOrEqualTo("departureTime", Calendar.getInstance().getTime());
            List<RideOffer> results = null;
            try {
                results = query.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            rideOffers.addAll(results);
            if (results.size() < 100) {
                finished = true;
            }
        }
        return rideOffers;
    }

    private void bindViews() {
        mProfilePictureImageView = mBinding.ivProfilePicture;
        mNameTextView = mBinding.tvName;
        mUniversityTextView = mBinding.tvUniversity;
        mMemberSinceTextView = mBinding.tvMemberSince;
        mRidesDrivenTextView = mBinding.tvRidesDriven;
        mRidesRiddenTextView = mBinding.tvRidesRidden;
        mUpcomingRideOffers = mBinding.tvRideOffers;
        mUpcomingRideRequests = mBinding.tvRideRequests;
        mRideOffersRecyclerView = mBinding.rvRideOffers;
        mRideRequestsRecyclerView = mBinding.rvRideRequests;
        mLogoutButton = mBinding.btnLogout;
    }

    private void changeProfilePicture() {
        FragmentManager fragmentManager = getFragmentManager();
        profilePictureDialogFragment = new ProfilePictureDialogFragment();
        profilePictureDialogFragment.setTargetFragment(ProfileFragment.this, 200);
        profilePictureDialogFragment.show(fragmentManager, "profile_picture_fragment");
    }

    private void logOutUser() {
        ParseUser.logOut();
        Intent logout = new Intent(getContext(), LoginActivity.class);
        startActivity(logout);
        getActivity().finish();
    }

    private int ridesRiddenCount(List<RideOffer> rideOffers) {
        int ridesRidden = 0;
        for(RideOffer rideOffer : rideOffers){
            if(rideOffer.hasPassenger(mUser)){
                ridesRidden++;
            }
        }
        return ridesRidden;
    }

    private int ridesDrivenCount(List<RideOffer> rideOffers){
        int ridesDriven = 0;
        for(RideOffer rideOffer : rideOffers){
            if(rideOffer.getUser().getObjectId().equals(mUser.getObjectId())){
                ridesDriven++;
            }
        }
        return ridesDriven;
    }

    private int passengersDrivenCount(List<RideOffer> rideOffers){
        int passengersDriven = 0;
        for(RideOffer rideOffer : rideOffers){
            if(rideOffer.getUser().getObjectId().equals(mUser.getObjectId())){
                passengersDriven += rideOffer.getPassengers().length();
            }
        }
        return passengersDriven;
    }

    public void setProfilePicture(Uri profilePicture){
        Glide.with(getContext()).load(profilePicture).transform(new CircleCrop()).into(mProfilePictureImageView);
    }

    public void setProfilePicture(Bitmap profilePicture) {
        Glide.with(getContext()).load(profilePicture).transform(new CircleCrop()).into(mProfilePictureImageView);
    }

    public void grantPermissions(){
        profilePictureDialogFragment.startGallery();
    }

    public ParseUser getUser(){
        return mUser;
    }

    @Override
    public void onFinishDeleteDialog(RideOffer rideOffer, RideRequest rideRequest) {
        if(rideOffer != null){
            if(rideOffer.hasPassenger(ParseUser.getCurrentUser())){
                try {
                    rideOffer.removePassenger(ParseUser.getCurrentUser());
                    rideOffer.saveInBackground();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                rideOffer.deleteInBackground();
            }
            for(int i = 0; i < mRideOffers.size(); i++){
                if(mRideOffers.get(i).getObjectId().equals(rideOffer.getObjectId())){
                    mRideOffers.remove(i);
                    break;
                }
            }
            mRideOffersAdapter.notifyDataSetChanged();
        }
        else{
            rideRequest.deleteInBackground();
            for(int i = 0; i < mRideRequests.size(); i++){
                if(mRideRequests.get(i).getObjectId().equals(rideRequest.getObjectId())){
                    mRideRequests.remove(i);
                    break;
                }
            }
            mRideRequestsAdapter.notifyDataSetChanged();
        }
    }
}
