package com.example.fbuapp.Fragments;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends Fragment {

    private ParseUser user;

    private ImageView mProfilePictureImageView;
    private TextView mNameTextView;
    private TextView mUniversityTextView;
    private TextView mMemberSinceTextView;
    private TextView mRidesDrivenTextView;
    private TextView mRidesRiddenTextView;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        user = ParseUser.getCurrentUser();

        mProfilePictureImageView = view.findViewById(R.id.ivProfilePicture);
        mNameTextView = view.findViewById(R.id.tvName);
        mUniversityTextView = view.findViewById(R.id.tvUniversity);
        mMemberSinceTextView = view.findViewById(R.id.tvMemberSince);
        mRidesDrivenTextView = view.findViewById(R.id.tvRidesDriven);
        mRidesRiddenTextView = view.findViewById(R.id.tvRidesRidden);

        Glide.with(getContext()).load(user.getParseFile("profilePicture").getUrl()).transform(new CircleCrop()).into(mProfilePictureImageView);
        mNameTextView.setText(user.getString("firstName")+" "+user.getString("lastName").substring(0,1)+".");
        mUniversityTextView.setText(user.getString("university"));
        mMemberSinceTextView.setText("Member since "+DateFormat.format("MMMM yyyy", user.getCreatedAt()));
        mRidesDrivenTextView.setText("Driven " + ridesDrivenCount(user) + " Rides");
        mRidesRiddenTextView.setText("Ridden " + ridesRiddenCount(user) + " Rides");
    }

    private int ridesRiddenCount(ParseUser user) {
        return 0;
    }

    private int ridesDrivenCount(ParseUser user) {
        return 0;
    }


}
