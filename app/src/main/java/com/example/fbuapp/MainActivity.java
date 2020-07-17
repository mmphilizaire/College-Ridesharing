package com.example.fbuapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.fbuapp.Fragments.RideOfferFragment;
import com.example.fbuapp.Fragments.RideRequestFragment;
import com.example.fbuapp.Fragments.RideStreamFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    final FragmentManager mFragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_ride_stream:
                        fragment = new RideStreamFragment();
                        break;
                    case R.id.action_ride_offer:
                        fragment = new RideOfferFragment(mFragmentManager);
                        break;
                    case R.id.action_ride_request:
                        fragment = new RideRequestFragment();
                        break;
                    case R.id.action_profile:
                    default:
                        fragment = new RideStreamFragment();
                        break;
                }
                mFragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        mBottomNavigationView.setSelectedItemId(R.id.action_ride_stream);
    }
}