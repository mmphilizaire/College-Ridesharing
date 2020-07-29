package com.example.fbuapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.fbuapp.Fragments.ProfileFragment;
import com.example.fbuapp.Fragments.RideOfferFragment;
import com.example.fbuapp.Fragments.RideRequestFragment;
import com.example.fbuapp.Fragments.RideStreamFragment;
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
                        mCurrentFragment = new RideRequestFragment();
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