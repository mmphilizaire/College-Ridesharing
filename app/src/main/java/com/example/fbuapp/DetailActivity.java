package com.example.fbuapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.fbuapp.Fragments.ProfileFragment;
import com.example.fbuapp.Fragments.RideOfferDetailFragment;
import com.example.fbuapp.Fragments.RideRequestDetailFragment;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.Models.RideRequest;
import com.example.fbuapp.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding mBinding;
    private Fragment currentFragment;

    FragmentManager mFragmentManager = getSupportFragmentManager();
    private Toolbar mToolbar;
    private ImageView mBackImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        configureToolbar();

        mBackImageView = (ImageView) findViewById(R.id.ivBack);

        RideOffer rideOffer = (RideOffer) getIntent().getParcelableExtra("rideOffer");
        RideRequest rideRequest = (RideRequest) getIntent().getParcelableExtra("rideRequest");
        if(rideOffer != null){
            currentFragment = RideOfferDetailFragment.newInstance(rideOffer);
        }
        else{
            currentFragment = RideRequestDetailFragment.newInstance(rideRequest);
        }
        mFragmentManager.beginTransaction().replace(R.id.flContainer, currentFragment).commit();
    }

    private void configureToolbar() {
        mToolbar = mBinding.toolbar;
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
        final FragmentTransaction transaction = mFragmentManager.beginTransaction();

        final ProfileFragment profileFragment = ProfileFragment.newInstance(rideRequest.getUser());
        transaction.hide(currentFragment).commit();
        transaction.add(R.id.flContainer, profileFragment).commit();

        mBackImageView.setVisibility(View.VISIBLE);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction.show(currentFragment).commit();
                transaction.remove(profileFragment).commit();
                mBackImageView.setVisibility(View.GONE);
            }
        });
    }

    public void createRideOffer(RideRequest rideRequest){
        Log.e("Mishka", rideRequest.getUser().getUsername());
        Intent data = new Intent();
        data.putExtra("rideRequest", rideRequest);
        setResult(RESULT_OK, data);
        finish();
    }

}