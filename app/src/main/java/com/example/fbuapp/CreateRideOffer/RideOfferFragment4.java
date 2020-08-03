package com.example.fbuapp.CreateRideOffer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.fbuapp.Activities.MainActivity;
import com.example.fbuapp.Adapters.RideOffersAdapter;
import com.example.fbuapp.CreateRideOffer.RideOfferFragment;
import com.example.fbuapp.CreateRideRequest.RideRequestFragment4;
import com.example.fbuapp.Models.RideOffer;
import com.example.fbuapp.Models.RideRequest;
import com.example.fbuapp.R;
import com.example.fbuapp.databinding.FragmentRideOffer4Binding;
import com.example.fbuapp.databinding.FragmentRideRequest1Binding;
import com.example.fbuapp.databinding.FragmentRideRequest2Binding;
import com.example.fbuapp.databinding.FragmentRideRequest4Binding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.fbuapp.RideStreamPageFragment.REFRESH_REQUEST_CODE;

public class RideOfferFragment4 extends Fragment {

    private static final String ARG_RIDE_OFFER = "rideOffer";

    FragmentRideOffer4Binding mBinding;

    private RideOffer mRideOffer;

    public TextView mDateTextView;
    public TextView mTimeTextView;
    public TextView mStartLocationTextView;
    public TextView mEndLocationTextView;
    public TextView mSeatPriceTextView;
    public TextView mSeatCountTextView;
    public Button mCreateButton;

    public RideOfferFragment4() {
        // Required empty public constructor
    }

    public static RideOfferFragment4 newInstance(RideOffer rideOffer) {
        RideOfferFragment4 fragment = new RideOfferFragment4();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RIDE_OFFER, rideOffer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRideOffer = getArguments().getParcelable(ARG_RIDE_OFFER);
        mBinding = FragmentRideOffer4Binding.inflate(getLayoutInflater(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bind();
        bindData();

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRideOffer.saveInBackground();
                MainActivity activity = (MainActivity) getActivity();
                activity.goToRideOfferStream(mRideOffer);
            }
        });
    }

    private void bind() {
        mDateTextView = mBinding.tvDate;
        mTimeTextView = mBinding.tvTime;
        mStartLocationTextView = mBinding.tvStartLocatoion;
        mEndLocationTextView = mBinding.tvEndLocation;
        mSeatPriceTextView = mBinding.tvSeatPrice;
        mSeatCountTextView = mBinding.tvSeatCount;
        mCreateButton = mBinding.btnCreate;
    }

    public void bindData() {
        mDateTextView.setText(mRideOffer.getDay() + ", " + mRideOffer.getDateNoYear());
        mTimeTextView.setText(mRideOffer.getTime());
        mStartLocationTextView.setText(mRideOffer.getStartLocation().getCity()+", "+mRideOffer.getStartLocation().getState());
        mEndLocationTextView.setText(mRideOffer.getEndLocation().getCity()+", "+mRideOffer.getEndLocation().getState());
        mSeatPriceTextView.setText("$" + mRideOffer.getSeatPrice().toString() + "\nper seat");
        mSeatCountTextView.setText(mRideOffer.getSeatsAvailable() + " seats available");
    }

}