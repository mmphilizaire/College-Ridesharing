package com.example.fbuapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.R;

public class RideOfferDetailFragment extends DialogFragment {

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

        mCloseImageView = view.findViewById(R.id.ivClose);
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

        mCloseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });

        mDriverInfoRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getFragmentManager().beginTransaction().replace(R.id.flContainer, ProfileFragment.newInstance(mRideOffer.getUser())).commit();
                //launchProfileFragment(mRideOffer.getUser());
            }
        });

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
