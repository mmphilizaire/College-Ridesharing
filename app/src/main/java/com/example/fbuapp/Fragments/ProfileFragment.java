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
import com.example.fbuapp.Activities.LoginActivity;
import com.example.fbuapp.R;
import com.example.fbuapp.databinding.FragmentProfileBinding;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding mBinding;

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
        mBinding = FragmentProfileBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mUser = getArguments().getParcelable("user");

        bindViews();
        bindData();

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
        mMemberSinceTextView.setText("Member since "+DateFormat.format("MMMM yyyy", mUser.getCreatedAt()));
        mRidesDrivenTextView.setText("Driven " + ridesDrivenCount(mUser) + " Rides");
        mRidesRiddenTextView.setText("Ridden " + ridesRiddenCount(mUser) + " Rides");
    }

    private void bindViews() {
        mProfilePictureImageView = mBinding.ivProfilePicture;
        mNameTextView = mBinding.tvName;
        mUniversityTextView = mBinding.tvUniversity;
        mMemberSinceTextView = mBinding.tvMemberSince;
        mRidesDrivenTextView = mBinding.tvRidesDriven;
        mRidesRiddenTextView = mBinding.tvRidesRidden;
        mLogoutButton = mBinding.btnLogout;
    }

    private void changeProfilePicture() {
        FragmentManager fragmentManager = getFragmentManager();
        ProfilePictureDialogFragment profilePictureDialogFragment = new ProfilePictureDialogFragment();
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
        Glide.with(getContext()).load(profilePicture).transform(new CircleCrop()).into(mProfilePictureImageView);
    }

    public void setProfilePicture(Bitmap profilePicture) {
        Glide.with(getContext()).load(profilePicture).transform(new CircleCrop()).into(mProfilePictureImageView);
    }

    public ParseUser getUser(){
        return mUser;
    }

}
