package com.example.fbuapp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.fbuapp.Fragments.RideStreamFragment;

public class RideStreamFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Offers", "Requests"};

    public RideStreamFragmentPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
        //super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("mishka", "okayyyy"+position);
        return RideStreamPageFragment.newInstance(position+1);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
