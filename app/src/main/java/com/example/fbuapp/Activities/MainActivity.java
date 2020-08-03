package com.example.fbuapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.fbuapp.CreateRideRequest.RideRequestFragment1;
import com.example.fbuapp.CreateRideRequest.RideRequestFragment2;
import com.example.fbuapp.CreateRideRequest.RideRequestFragment3;
import com.example.fbuapp.CreateRideRequest.RideRequestFragment4;
import com.example.fbuapp.Fragments.ProfileFragment;
import com.example.fbuapp.CreateRideOffer.RideOfferFragment;
import com.example.fbuapp.Fragments.RideStreamFragment;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.Models.RideRequest;
import com.example.fbuapp.R;
import com.example.fbuapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

import static com.example.fbuapp.Fragments.ProfilePictureDialogFragment.GALLERY_IMAGE_REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    private Fragment mCurrentFragment;
    private BottomNavigationView mBottomNavigationView;
    final FragmentManager mFragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBottomNavigationView = mBinding.bottomNavigation;

        setBottomNavigationListener();

    }

    private void setBottomNavigationListener() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_ride_stream:
                        mCurrentFragment = new RideStreamFragment();
                        break;
                    case R.id.action_ride_offer:
                        mCurrentFragment = new RideOfferFragment();
                        break;
                    case R.id.action_ride_request:
                        RideRequest rideRequest = new RideRequest();
                        rideRequest.setUser(ParseUser.getCurrentUser());
                        mCurrentFragment = RideRequestFragment1.newInstance(rideRequest);
                        break;
                    case R.id.action_profile:
                    default:
                        mCurrentFragment = ProfileFragment.newInstance(ParseUser.getCurrentUser());
                        break;
                }
                replaceFragment(mCurrentFragment);
                return true;
            }
        });
        mBottomNavigationView.setSelectedItemId(R.id.action_ride_stream);
    }

    public void replaceFragment(Fragment fragment){
        mFragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
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
            replaceFragment(mCurrentFragment);
        }
        else if(mCurrentFragment instanceof RideRequestFragment2){
            mCurrentFragment = RideRequestFragment3.newInstance(rideRequest);
            replaceFragment(mCurrentFragment);
        }
        else{
            mCurrentFragment = RideRequestFragment4.newInstance(rideRequest);
            replaceFragment(mCurrentFragment);
        }
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
}