package com.example.fbuapp.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.fbuapp.DetailActivity;
import com.example.fbuapp.FetchURL;
import com.example.fbuapp.Models.Location;
import com.example.fbuapp.Models.RideRequest;
import com.example.fbuapp.R;
import com.example.fbuapp.TaskLoadedCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.ParseUser;

public class RideRequestDetailFragment extends Fragment implements OnMapReadyCallback, TaskLoadedCallback {

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private GoogleMap mMap;
    private Polyline mPolyline;

    private RideRequest mRideRequest;

    private RelativeLayout mUserInfoRelativeLayout;
    private TextView mEarliestDateTextView;
    private TextView mEarliestTimeTextView;
    private TextView mLatestDateTextView;
    private TextView mLatestTimeTextView;
    private  TextView mStartLocationTextView;
    private TextView mEndLocationTextView;

    private ImageView mUserProfilePictureImageView;
    private TextView mUserNameTextView;
    private TextView mUserUniversityTextView;

    private Button mOfferRideButton;

    public RideRequestDetailFragment(){
    }

    public static RideRequestDetailFragment newInstance(RideRequest rideRequest){
        RideRequestDetailFragment detailFragment = new RideRequestDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("rideRequest", rideRequest);
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_ride_request_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRideRequest = getArguments().getParcelable("rideRequest");

        mUserInfoRelativeLayout = view.findViewById(R.id.rlUserInfo);

        mEarliestDateTextView = view.findViewById(R.id.tvEarliestDate);
        mEarliestTimeTextView = view.findViewById(R.id.tvEarliestTime);
        mLatestDateTextView = view.findViewById(R.id.tvLatestDate);
        mLatestTimeTextView = view.findViewById(R.id.tvLatestTime);
        mStartLocationTextView = view.findViewById(R.id.tvStart);
        mEndLocationTextView = view.findViewById(R.id.tvEnd);

        mUserProfilePictureImageView = view.findViewById(R.id.ivProfilePicture);
        mUserNameTextView = view.findViewById(R.id.tvUserName);
        mUserUniversityTextView = view.findViewById(R.id.tvUniversity);

        mOfferRideButton = view.findViewById(R.id.btnOfferRide);

        bindData();

        mUserInfoRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DetailActivity)getActivity()).getProfile(mRideRequest);
            }
        });

        if(myRideRequest()){
            mOfferRideButton.setVisibility(View.INVISIBLE);
        }
        else {
            mOfferRideButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        if (isServicesOK()) {
            initializeMap();
        }

    }

    public boolean myRideRequest(){
        if(mRideRequest.getUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
            return true;
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getContext(), "Map is Ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Location startLocation = mRideRequest.getStartLocation();
                Location endLocation = mRideRequest.getEndLocation();
                LatLng start = new LatLng(startLocation.getLatitude().doubleValue(), startLocation.getLongitude().doubleValue());
                LatLng end = new LatLng(endLocation.getLatitude().doubleValue(), endLocation.getLongitude().doubleValue());
                Uri uri = Uri.parse(getMapLink(start, end, "driving"));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
        getRoute();
    }

    private void getRoute() {
        Location startLocation = mRideRequest.getStartLocation();
        Location endLocation = mRideRequest.getEndLocation();

        LatLng start = new LatLng(startLocation.getLatitude().doubleValue(), startLocation.getLongitude().doubleValue());
        LatLng end = new LatLng(endLocation.getLatitude().doubleValue(), endLocation.getLongitude().doubleValue());

        addMarker(start, "Start Location");
        addMarker(end, "End Location");

        moveCamera(start, end);

        String url = getUrl(start, end, "driving");
        new FetchURL(getContext(), this).execute(url, "driving");

    }

    private void moveCamera(LatLng start, LatLng end) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(start);
        builder.include(end);

        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.35);

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(cu);

    }

    private String getMapLink(LatLng start, LatLng end, String directionMode) {
        String origin = "origin=" + start.latitude + "," + start.longitude;
        String destination = "destination=" + end.latitude + "," + end.longitude;
        String mode = "travelmode=" + directionMode;
        String parameters = origin + "&" + destination + "&" + mode;
        String url = "https://www.google.com/maps/dir/?api=1&" + parameters;
        return url;
    }

    private String getUrl(LatLng start, LatLng end, String directionMode) {
        String origin = "origin=" + start.latitude + "," + start.longitude;
        String destination = "destination=" + end.latitude + "," + end.longitude;
        String mode = "mode=" + directionMode;
        String parameters = origin + "&" + destination + "&" + mode;
        String url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters + "&key=" + getString(R.string.google_apps_API_key);
        return url;
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

    private void bindData(){
        mEarliestDateTextView.setText(mRideRequest.getDateWithYear(mRideRequest.getEarliestDeparture()));
        mEarliestTimeTextView.setText(mRideRequest.getTime(mRideRequest.getEarliestDeparture()));
        mLatestDateTextView.setText(mRideRequest.getDateWithYear(mRideRequest.getLatestDeparture()));
        mLatestTimeTextView.setText(mRideRequest.getTime(mRideRequest.getLatestDeparture()));
        mStartLocationTextView.setText(mRideRequest.getStartLocation().getCity() + ",\n" + mRideRequest.getStartLocation().getState());
        mEndLocationTextView.setText(mRideRequest.getEndLocation().getCity() + ",\n" + mRideRequest.getEndLocation().getState());

        Glide.with(this).load(mRideRequest.getUser().getParseFile("profilePicture").getUrl()).transform(new CircleCrop()).into(mUserProfilePictureImageView);
        mUserNameTextView.setText(mRideRequest.getUser().getString("firstName"));
        mUserUniversityTextView.setText(mRideRequest.getUser().getString("university"));
    }

    @Override
    public void onTaskDone(Object... values) {
        if(mPolyline != null){
            mPolyline.remove();
        }
        mPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

}
