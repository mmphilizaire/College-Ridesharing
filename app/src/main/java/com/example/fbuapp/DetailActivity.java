package com.example.fbuapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.fbuapp.Fragments.ProfileFragment;
import com.example.fbuapp.Fragments.RideOfferDetailFragment;
import com.example.fbuapp.Fragments.RideOfferFragment;
import com.example.fbuapp.Fragments.RideRequestFragment;
import com.example.fbuapp.Fragments.RideStreamFragment;
import com.example.fbuapp.Models.RideOffer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.Parse;
import com.parse.ParseUser;

public class DetailActivity extends AppCompatActivity {

    FragmentManager mFragmentManager = getSupportFragmentManager();
    private ImageView mCloseImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mCloseImageView = findViewById(R.id.ivClose);
        mCloseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RideOffer rideOffer = (RideOffer) getIntent().getParcelableExtra("rideOffer");

        mFragmentManager.beginTransaction().replace(R.id.flContainer, RideOfferDetailFragment.newInstance(rideOffer)).commit();

    }

//    public void launchProfileFragment(ParseUser user){
//        mFragmentManager.beginTransaction().replace(R.id.flContainer, ProfileFragment.newInstance(user)).commit();
//    }

}