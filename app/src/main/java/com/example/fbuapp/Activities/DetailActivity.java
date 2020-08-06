package com.example.fbuapp.Activities;

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
import com.example.fbuapp.R;
import com.example.fbuapp.databinding.ActivityDetailBinding;
import com.parse.ParseUser;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding mBinding;
    private Fragment currentFragment;

    private RideOffer mRideOffer;
    private RideRequest mRideRequest;

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

        mRideOffer = (RideOffer) getIntent().getParcelableExtra("rideOffer");
        mRideRequest = (RideRequest) getIntent().getParcelableExtra("rideRequest");
        if(mRideOffer != null){
            currentFragment = RideOfferDetailFragment.newInstance(mRideOffer);
        }
        else{
            currentFragment = RideRequestDetailFragment.newInstance(mRideRequest);
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

    public void getProfile(final RideOffer rideOffer, ParseUser user){
        final ProfileFragment profileFragment = ProfileFragment.newInstance(user);
        replaceFragmentAnimation(profileFragment);
        mBackImageView.setVisibility(View.VISIBLE);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RideOfferDetailFragment fragment = RideOfferDetailFragment.newInstance(rideOffer);
                replaceFragmentAnimationBack(fragment);
                mBackImageView.setVisibility(View.GONE);
            }
        });
    }

    public void getProfile(final RideRequest rideRequest){
        final ProfileFragment profileFragment = ProfileFragment.newInstance(rideRequest.getUser());
        replaceFragmentAnimation(profileFragment);
        mBackImageView.setVisibility(View.VISIBLE);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RideRequestDetailFragment fragment = RideRequestDetailFragment.newInstance(rideRequest);
                replaceFragmentAnimationBack(fragment);
                mBackImageView.setVisibility(View.GONE);
            }
        });
    }

    public void createRideOffer(RideRequest rideRequest){
        Intent data = new Intent();
        data.putExtra("rideRequest", rideRequest);
        setResult(RESULT_OK, data);
        finish();
    }

    public void replaceFragmentAnimation(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.flContainer, fragment).commit();
    }

    public void replaceFragmentAnimationBack(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.flContainer, fragment).commit();
    }

}