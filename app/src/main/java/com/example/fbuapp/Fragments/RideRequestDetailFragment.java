package com.example.fbuapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.fbuapp.Models.RideRequest;
import com.example.fbuapp.R;

public class RideRequestDetailFragment extends DialogFragment {

    private RideRequest mRideRequest;

    private ImageView mCloseImageView;
    private TextView mEarliestDateTextView;
    private TextView mEarliestTimeTextView;
    private TextView mLatestDateTextView;
    private TextView mLatestTimeTextView;
    private  TextView mStartLocationTextView;
    private TextView mEndLocationTextView;

    private ImageView mDriverProfilePictureImageView;
    private TextView mDriverNameTextView;
    private TextView mDriverUniversityTextView;

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
        return inflater.inflate(R.layout.fragment_ride_request_detail, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRideRequest = getArguments().getParcelable("rideRequest");

        mCloseImageView = view.findViewById(R.id.ivClose);
        mEarliestDateTextView = view.findViewById(R.id.tvEarliestDate);
        mEarliestTimeTextView = view.findViewById(R.id.tvEarliestTime);
        mLatestDateTextView = view.findViewById(R.id.tvLatestDate);
        mLatestTimeTextView = view.findViewById(R.id.tvLatestTime);
        mStartLocationTextView = view.findViewById(R.id.tvStart);
        mEndLocationTextView = view.findViewById(R.id.tvEnd);

        mDriverProfilePictureImageView = view.findViewById(R.id.ivProfilePicture);
        mDriverNameTextView = view.findViewById(R.id.tvDriverName);
        mDriverUniversityTextView = view.findViewById(R.id.tvUniversity);

        bind();

        mCloseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    private void bind(){
        mEarliestDateTextView.setText(mRideRequest.getDateWithYear(mRideRequest.getEarliestDeparture()));
        mEarliestTimeTextView.setText(mRideRequest.getTime(mRideRequest.getEarliestDeparture()));
        mLatestDateTextView.setText(mRideRequest.getDateWithYear(mRideRequest.getLatestDeparture()));
        mLatestTimeTextView.setText(mRideRequest.getTime(mRideRequest.getLatestDeparture()));
        mStartLocationTextView.setText(mRideRequest.getStartLocation().getCity() + ",\n" + mRideRequest.getStartLocation().getState());
        mEndLocationTextView.setText(mRideRequest.getEndLocation().getCity() + ",\n" + mRideRequest.getEndLocation().getState());

        Glide.with(this).load(mRideRequest.getUser().getParseFile("profilePicture").getUrl()).transform(new CircleCrop()).into(mDriverProfilePictureImageView);
        mDriverNameTextView.setText(mRideRequest.getUser().getString("firstName"));
        mDriverUniversityTextView.setText(mRideRequest.getUser().getString("university"));
    }

}
