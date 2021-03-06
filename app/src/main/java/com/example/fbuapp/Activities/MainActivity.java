package com.example.fbuapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fbuapp.CreateRideOffer.RideOfferFragment1;
import com.example.fbuapp.CreateRideOffer.RideOfferFragment2;
import com.example.fbuapp.CreateRideOffer.RideOfferFragment3;
import com.example.fbuapp.CreateRideOffer.RideOfferFragment4;
import com.example.fbuapp.CreateRideRequest.RideRequestFragment1;
import com.example.fbuapp.CreateRideRequest.RideRequestFragment2;
import com.example.fbuapp.CreateRideRequest.RideRequestFragment3;
import com.example.fbuapp.CreateRideRequest.RideRequestFragment4;
import com.example.fbuapp.Fragments.ProfileFragment;
import com.example.fbuapp.Fragments.RideStreamFragment;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.Models.RideRequest;
import com.example.fbuapp.R;
import com.example.fbuapp.RateRideDialogFragment;
import com.example.fbuapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.fbuapp.Fragments.ProfilePictureDialogFragment.GALLERY_IMAGE_REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    private Fragment mCurrentFragment;
    private Toolbar mToolbar;
    private ImageView mBackImageView;
    private MenuItem mCloseMenuItem;
    private TextView mToolbarTitleTextView;
    private BottomNavigationView mBottomNavigationView;
    final FragmentManager mFragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        bind();

        configureToolbar();

//        List<RideOffer> recentRides = getRecentRides(lastLogin());
//        for(RideOffer rideOffer : recentRides){
//            if(rideOffer.hasPassenger(ParseUser.getCurrentUser())){
//                FragmentManager fm = getSupportFragmentManager();
//                RateRideDialogFragment rateRideDialogFragment = RateRideDialogFragment.newInstance(rideOffer);
//                rateRideDialogFragment.show(fm, "rateRide");
//            }
//        }

    }

    private void configureToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.miClose:
                        mBottomNavigationView.setSelectedItemId(R.id.action_ride_stream);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mCloseMenuItem = menu.findItem(R.id.miClose);
        setBottomNavigationListener();
        return true;
    }

    private void bind() {
        mToolbar = mBinding.toolbar;
        mBackImageView = mBinding.ivBack;
        mBottomNavigationView = mBinding.bottomNavigation;
        mToolbarTitleTextView = mBinding.toolbarTitle;
    }

    private void setBottomNavigationListener() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_ride_stream:
                        mCurrentFragment = new RideStreamFragment();
                        mBackImageView.setVisibility(View.GONE);
                        mCloseMenuItem.setVisible(false);
                        mToolbarTitleTextView.setText("MileShare");
                        break;
                    case R.id.action_ride_offer:
                        RideOffer rideOffer = new RideOffer();
                        rideOffer.setUser(ParseUser.getCurrentUser());
                        mCurrentFragment = RideOfferFragment1.newInstance(rideOffer);
                        mBackImageView.setVisibility(View.GONE);
                        mCloseMenuItem.setVisible(true);
                        setRideOfferBackListener(rideOffer);
                        mToolbarTitleTextView.setText("Create Ride Offer");
                        break;
                    case R.id.action_ride_request:
                        RideRequest rideRequest = new RideRequest();
                        rideRequest.setUser(ParseUser.getCurrentUser());
                        mCurrentFragment = RideRequestFragment1.newInstance(rideRequest);
                        mBackImageView.setVisibility(View.GONE);
                        mCloseMenuItem.setVisible(true);
                        setRideRequestBackListener(rideRequest);
                        mToolbarTitleTextView.setText("Create Ride Request");
                        break;
                    case R.id.action_profile:
                    default:
                        mCurrentFragment = ProfileFragment.newInstance(ParseUser.getCurrentUser());
                        mBackImageView.setVisibility(View.GONE);
                        mCloseMenuItem.setVisible(false);
                        mToolbarTitleTextView.setText("Profile");
                        break;
                }
                replaceFragment(mCurrentFragment);
                return true;
            }
        });
        mBottomNavigationView.setSelectedItemId(R.id.action_ride_stream);
    }

    private void setRideRequestBackListener(final RideRequest rideRequest) {
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLastRideRequestFragment(rideRequest);
            }
        });
    }

    private void setRideOfferBackListener(final RideOffer rideOffer) {
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLastRideOfferFragment(rideOffer);
            }
        });
    }

    public void replaceFragment(Fragment fragment){
        mFragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
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

    public void goToRideOfferStream(RideOffer rideOffer){
        mCurrentFragment = RideStreamFragment.newInstance(rideOffer);
        replaceFragment(mCurrentFragment);
        mBottomNavigationView.setSelectedItemId(R.id.action_ride_stream);
    }

    public void goToRideRequestStream(RideRequest rideRequest){
        mBottomNavigationView.setSelectedItemId(R.id.action_ride_stream);
        mCurrentFragment = RideStreamFragment.newInstance(rideRequest);
        replaceFragment(mCurrentFragment);
    }

    public void goToNextRideRequestFragment(RideRequest rideRequest){
        if(mCurrentFragment instanceof RideRequestFragment1){
            mCurrentFragment = RideRequestFragment2.newInstance(rideRequest);
            replaceFragmentAnimation(mCurrentFragment);
            mBackImageView.setVisibility(View.VISIBLE);
        }
        else if(mCurrentFragment instanceof RideRequestFragment2){
            mCurrentFragment = RideRequestFragment3.newInstance(rideRequest);
            replaceFragmentAnimation(mCurrentFragment);
        }
        else{
            mCurrentFragment = RideRequestFragment4.newInstance(rideRequest);
            replaceFragmentAnimation(mCurrentFragment);
        }
    }

    public void goToNextRideOfferFragment(RideOffer rideOffer){
        if(mCurrentFragment instanceof RideOfferFragment1){
            mCurrentFragment = RideOfferFragment2.newInstance(rideOffer);
            replaceFragmentAnimation(mCurrentFragment);
            mBackImageView.setVisibility(View.VISIBLE);
        }
        else if(mCurrentFragment instanceof RideOfferFragment2){
            mCurrentFragment = RideOfferFragment3.newInstance(rideOffer);
            replaceFragmentAnimation(mCurrentFragment);
        }
        else{
            mCurrentFragment = RideOfferFragment4.newInstance(rideOffer);
            replaceFragmentAnimation(mCurrentFragment);
        }
    }

    public void goToLastRideRequestFragment(RideRequest rideRequest){
        if(mCurrentFragment instanceof RideRequestFragment2){
            mCurrentFragment = RideRequestFragment1.newInstance(rideRequest);
            replaceFragmentAnimationBack(mCurrentFragment);
            mBackImageView.setVisibility(View.GONE);
        }
        else if(mCurrentFragment instanceof RideRequestFragment3){
            mCurrentFragment = RideRequestFragment2.newInstance(rideRequest);
            replaceFragmentAnimationBack(mCurrentFragment);
        }
        else{
            mCurrentFragment = RideRequestFragment3.newInstance(rideRequest);
            replaceFragmentAnimationBack(mCurrentFragment);
        }
    }

    public void goToLastRideOfferFragment(RideOffer rideOffer){
        if(mCurrentFragment instanceof RideOfferFragment2){
            mCurrentFragment = RideOfferFragment1.newInstance(rideOffer);
            replaceFragmentAnimationBack(mCurrentFragment);
            mBackImageView.setVisibility(View.GONE);
        }
        else if(mCurrentFragment instanceof RideOfferFragment3){
            mCurrentFragment = RideOfferFragment2.newInstance(rideOffer);
            replaceFragmentAnimationBack(mCurrentFragment);
        }
        else{
            mCurrentFragment = RideOfferFragment3.newInstance(rideOffer);
            replaceFragmentAnimationBack(mCurrentFragment);
        }
    }

    public void createOfferFromRequest(RideRequest rideRequest){
        RideOffer rideOffer = new RideOffer();
        rideOffer.setUser(ParseUser.getCurrentUser());
        rideOffer.setStartLocation(rideRequest.getStartLocation());
        rideOffer.setEndLocation(rideRequest.getEndLocation());
        mCurrentFragment = RideOfferFragment1.newInstance(rideOffer);
        replaceFragment(mCurrentFragment);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case GALLERY_IMAGE_REQUEST_CODE:
                ProfileFragment fragment = (ProfileFragment) mCurrentFragment;
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fragment.grantPermissions();
                }
                break;
        }
    }

//    public Date lastLogin(){
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("_Session");
//        query.orderByDescending("createdAt");
//        try {
//            List<ParseObject> list = query.find();
//            if(list.size() > 1){
//                return list.get(1).getCreatedAt();
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return Calendar.getInstance().getTime();
//    }
//
//    public List<RideOffer> getRecentRides(Date lastLogin){
//        ParseQuery<RideOffer> query = ParseQuery.getQuery("RideOffer");
//        query.include("startLocation");
//        query.include("endLocation");
//        query.include("user");
//        query.whereGreaterThan("departureTime", lastLogin);
//        query.whereLessThan("departureTime", Calendar.getInstance().getTime());
//        try {
//            List<RideOffer> rideOffers = query.find();
//            return rideOffers;
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

}