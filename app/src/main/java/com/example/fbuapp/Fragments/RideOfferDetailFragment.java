package com.example.fbuapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.fbuapp.MapActivity;
import com.example.fbuapp.R;

public class RideOfferDetailFragment extends DialogFragment {

    private TextView mDateTextView;
    private TextView mTimeTextView;

    public RideOfferDetailFragment(){
    }

    public static RideOfferDetailFragment newInstance(){
        RideOfferDetailFragment detailFragment = new RideOfferDetailFragment();
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_ride_offer_detail, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

}
