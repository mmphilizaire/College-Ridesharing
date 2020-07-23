package com.example.fbuapp.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.fbuapp.MapActivity;
import com.example.fbuapp.Models.Location;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RideOfferDetailFragment extends DialogFragment implements OnMapReadyCallback {

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 5313;

    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private RideOffer mRideOffer;

    private ImageView mCloseImageView;
    private TextView mDateTextView;
    private TextView mTimeTextView;
    private  TextView mStartLocationTextView;
    private TextView mEndLocationTextView;
    private TextView mPricePerSeatTextView;
    private TextView mSeatsAvailableTextView;

    private RelativeLayout mDriverInfoRelativeLayout;

    private ImageView mDriverProfilePictureImageView;
    private TextView mDriverNameTextView;
    private TextView mDriverUniversityTextView;

    public RideOfferDetailFragment(){
    }

    public static RideOfferDetailFragment newInstance(RideOffer rideOffer){
        RideOfferDetailFragment detailFragment = new RideOfferDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("rideOffer", rideOffer);
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_ride_offer_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRideOffer = getArguments().getParcelable("rideOffer");

        mDriverInfoRelativeLayout = view.findViewById(R.id.rlDriverInfo);

        mDateTextView = view.findViewById(R.id.tvDate);
        mTimeTextView = view.findViewById(R.id.tvTime);
        mStartLocationTextView = view.findViewById(R.id.tvStart);
        mEndLocationTextView = view.findViewById(R.id.tvEnd);
        mPricePerSeatTextView = view.findViewById(R.id.tvSeatPrice);
        mSeatsAvailableTextView = view.findViewById(R.id.tvSeatsAvailable);

        mDriverProfilePictureImageView = view.findViewById(R.id.ivProfilePicture);
        mDriverNameTextView = view.findViewById(R.id.tvDriverName);
        mDriverUniversityTextView = view.findViewById(R.id.tvUniversity);

        bind();

        mDriverInfoRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.flContainer, ProfileFragment.newInstance(mRideOffer.getUser())).commit();
                //launchProfileFragment(mRideOffer.getUser());
            }
        });

        if (isServicesOK()) {
            getLocationPermission();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getContext(), "Map is Ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        if (mLocationPermissionsGranted) {
            getRoute();

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
    }

    private void getRoute() {
        Location startLocation = mRideOffer.getStartLocation();
        Location endLocation = mRideOffer.getEndLocation();

        LatLng start = new LatLng(startLocation.getLatitude().doubleValue(), startLocation.getLongitude().doubleValue());
        LatLng end = new LatLng(endLocation.getLatitude().doubleValue(), endLocation.getLongitude().doubleValue());

        addMarker(start, "Start Location");
        addMarker(end, "End Location");


    }

    private void addMarker(LatLng location, String title) {
        MarkerOptions options = new MarkerOptions().position(location).title(title);
        mMap.addMarker(options);
    }

    public boolean isServicesOK(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());

        if(available == ConnectionResult.SUCCESS){
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(getContext(), "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void initializeMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(getContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initializeMap();
            }else{
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                    initializeMap();
                }
            }
        }
    }

    private void bind(){
        mDateTextView.setText(mRideOffer.getDateWithYear());
        mTimeTextView.setText(mRideOffer.getTime());
        mStartLocationTextView.setText(mRideOffer.getStartLocation().getCity() + ",\n" + mRideOffer.getStartLocation().getState());
        mEndLocationTextView.setText(mRideOffer.getEndLocation().getCity() + ",\n" + mRideOffer.getEndLocation().getState());
        mPricePerSeatTextView.setText("$" + mRideOffer.getSeatPrice() + " PER SEAT");
        mSeatsAvailableTextView.setText(mRideOffer.getSeatsAvailable() + " seats\navailable");

        Glide.with(this).load(mRideOffer.getUser().getParseFile("profilePicture").getUrl()).transform(new CircleCrop()).into(mDriverProfilePictureImageView);
        mDriverNameTextView.setText(mRideOffer.getUser().getString("firstName"));
        mDriverUniversityTextView.setText(mRideOffer.getUser().getString("university"));
    }

}
