package com.example.fbuapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.fbuapp.Fragments.ProfileFragment;
import com.example.fbuapp.Fragments.RideOfferDetailFragment;
import com.example.fbuapp.Fragments.RideOfferFragment;
import com.example.fbuapp.Fragments.RideRequestDetailFragment;
import com.example.fbuapp.Fragments.RideRequestFragment;
import com.example.fbuapp.Fragments.RideStreamFragment;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.Models.RideRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.Parse;
import com.parse.ParseUser;

public class DetailActivity extends AppCompatActivity {

    FragmentManager mFragmentManager = getSupportFragmentManager();
    private ImageView mCloseImageView;
    private Toolbar mToolbar;
    private ImageView mBackImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.miClose:
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });

        mBackImageView = (ImageView) findViewById(R.id.ivBack);

        RideOffer rideOffer = (RideOffer) getIntent().getParcelableExtra("rideOffer");
        if(rideOffer != null){
            mFragmentManager.beginTransaction().replace(R.id.flContainer, RideOfferDetailFragment.newInstance(rideOffer)).commit();
        }
        else{
            RideRequest rideRequest = (RideRequest) getIntent().getParcelableExtra("rideRequest");
            mFragmentManager.beginTransaction().replace(R.id.flContainer, RideRequestDetailFragment.newInstance(rideRequest)).commit();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    public void getProfile(final RideOffer rideOffer){
        final ProfileFragment profileFragment = ProfileFragment.newInstance(rideOffer.getUser());
        mFragmentManager.beginTransaction().replace(R.id.flContainer, ProfileFragment.newInstance(rideOffer.getUser())).commit();
        mBackImageView.setVisibility(View.VISIBLE);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentManager.beginTransaction().replace(R.id.flContainer, RideOfferDetailFragment.newInstance(rideOffer)).commit();
                mBackImageView.setVisibility(View.GONE);
            }
        });
    }

    public void getProfile(final RideRequest rideRequest){
        final ProfileFragment profileFragment = ProfileFragment.newInstance(rideRequest.getUser());
        mFragmentManager.beginTransaction().replace(R.id.flContainer, ProfileFragment.newInstance(rideRequest.getUser())).commit();
        mBackImageView.setVisibility(View.VISIBLE);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentManager.beginTransaction().replace(R.id.flContainer, RideRequestDetailFragment.newInstance(rideRequest)).commit();
                mBackImageView.setVisibility(View.GONE);
            }
        });
    }

}