package com.example.fbuapp.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.fbuapp.LoginActivity;
import com.example.fbuapp.R;
import com.example.fbuapp.RideStreamPageFragment;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    private ParseUser mUser;

    private ImageView mProfilePictureImageView;
    private TextView mNameTextView;
    private TextView mUniversityTextView;
    private TextView mMemberSinceTextView;
    private TextView mRidesDrivenTextView;
    private TextView mRidesRiddenTextView;

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mUser = getArguments().getParcelable("user");

        mProfilePictureImageView = view.findViewById(R.id.ivProfilePicture);
        mNameTextView = view.findViewById(R.id.tvName);
        mUniversityTextView = view.findViewById(R.id.tvUniversity);
        mMemberSinceTextView = view.findViewById(R.id.tvMemberSince);
        mRidesDrivenTextView = view.findViewById(R.id.tvRidesDriven);
        mRidesRiddenTextView = view.findViewById(R.id.tvRidesRidden);
        mLogoutButton = view.findViewById(R.id.btnLogout);

        Glide.with(getContext()).load(mUser.getParseFile("profilePicture").getUrl()).transform(new CircleCrop()).into(mProfilePictureImageView);
        mNameTextView.setText(mUser.getString("firstName")+" "+ mUser.getString("lastName").substring(0,1)+".");
        mUniversityTextView.setText(mUser.getString("university"));
        mMemberSinceTextView.setText("Member since "+DateFormat.format("MMMM yyyy", mUser.getCreatedAt()));
        mRidesDrivenTextView.setText("Driven " + ridesDrivenCount(mUser) + " Rides");
        mRidesRiddenTextView.setText("Ridden " + ridesRiddenCount(mUser) + " Rides");

        mProfilePictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfilePicture();
            }
        });

        if(mUser == ParseUser.getCurrentUser()){
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

    private void changeProfilePicture() {
        FragmentManager fragmentManager = getFragmentManager();
        ProfilePictureDialogFragment profilePictureDialogFragment = ProfilePictureDialogFragment.newInstance();
        profilePictureDialogFragment.setTargetFragment(ProfileFragment.this, 200);
        profilePictureDialogFragment.show(fragmentManager, "profile_picture_fragment");
    }

    private void logOutUser() {
        ParseUser.logOut();
        Intent logout = new Intent(getContext(), LoginActivity.class);
        startActivity(logout);
        getActivity().finish();
    }

    private int ridesRiddenCount(ParseUser user) {
        return 0;
    }

    private int ridesDrivenCount(ParseUser user) {
        return 0;
    }

    public void setProfilePicture(Uri profilePicture){
        //mProfilePictureImageView.setImageBitmap(profilePicture);
        Glide.with(getContext()).load(profilePicture).transform(new CircleCrop()).into(mProfilePictureImageView);
    }

}
